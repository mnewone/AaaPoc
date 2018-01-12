package main;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.SuffixRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.PassThroughFieldExtractor;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import dataSchema.TableSubAssetClassAttributes;
import dataSchema.TablesBeanMeta;
import dataSchema.TablesBeanMeta.OutputTablesBeanMeta;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	private Map<String,Job> batchJobs =new HashMap<>();
	
	@Value("#{'${feedTables}'.split(',')}")
	private List<String> feedTables;
	
	@Autowired
	private ApplicationContext myContext;
	@Autowired
	private JobRegistry myJobRegistry;
	
	public Map<String,Job> getBatchJobs(){
		return this.batchJobs;
	}
	
	public void setFeedTables(List<String> tables) {
		this.feedTables = tables;
	}
	public List<String> getFeedTables(){
		return this.feedTables;
	}
	
	@Bean
	public DataSource myDataSource() {
		SimpleDriverDataSource myDS = new SimpleDriverDataSource();
		myDS.setDriver(new org.hsqldb.jdbc.JDBCDriver());
		myDS.setUsername("sa");
		myDS.setPassword("");
		myDS.setUrl("jdbc:hsqldb:mem:mydb");
		return myDS;
	}
	
	@PostConstruct
	public void setBatchJobs() {
		
		for(String tableName:feedTables) {
			StringBuilder builderTableName = new StringBuilder(tableName);
			String fullTableName = builderTableName.insert(0, "dataSchema.").toString();
			
			try {
				GenericApplicationContext ctx = (GenericApplicationContext)myContext;
				Class tableClass = Class.forName(fullTableName);
				Method getBeanMeta = tableClass.getDeclaredMethod("getTablesBeanMeta", null);
				TablesBeanMeta tableBeanMeta = (TablesBeanMeta)getBeanMeta.invoke(null, null);
				
				ctx.registerBeanDefinition(tableBeanMeta.getReaderBeanName(),
						tableFlatFileItemReaderBeanDefinition(tableBeanMeta.getReaderBeanClassName(),
								tableBeanMeta.getInputFile(),
								tableBeanMeta.getFieldSpliter(),
								tableBeanMeta.getEndOfLine()));
				ctx.registerBeanDefinition(tableBeanMeta.getProcessorBeanName(),
						createTableProcessorBeanDefinition(tableBeanMeta.getProcessorBeanClassName()));
				
				List outputTablesWriterBean = new ArrayList();
				for(OutputTablesBeanMeta outputTable:tableBeanMeta.getOutputTables()) {
					
					ctx.registerBeanDefinition(outputTable.getBeanName(),
							createTableOutFlatFileItemWriterBeanDefinition(outputTable.getBeanClassName(),
									outputTable.getOrderedColumnList(),
									outputTable.getMismatchedColumnNameMap(),
									outputTable.getTableOutputFile(),
									outputTable.getFieldSpliter()));
					outputTablesWriterBean.add(ctx.getBean(outputTable.getBeanName()));
					
				}
				
				ctx.registerBeanDefinition(tableBeanMeta.getCompositeWriterBeanName(), 
						createCompositeItemWriterBeanDefinition(outputTablesWriterBean));
				
				StringBuilder tableStepName = new StringBuilder("jsonProcessing");
				String fullTableStepName = tableStepName.insert(0, tableName).toString();
				
				StepBuilder stepBuilder = ctx.getBean(StepBuilderFactory.class)
						.get(fullTableStepName)
						.allowStartIfComplete(true);
				
				Step stepA = stepBuilder.chunk(10)
						.reader((ItemReader)ctx.getBean(tableBeanMeta.getReaderBeanName()))
						.processor((ItemProcessor)ctx.getBean(tableBeanMeta.getProcessorBeanName()))
						//.processor((LiquidityInformationProcessor)ctx.getBean("liquidityInformationProcessor"))
						.writer((CompositeItemWriter)ctx.getBean(tableBeanMeta.getCompositeWriterBeanName()))
						.build();
				
				Job tableJob = ctx.getBean(JobBuilderFactory.class).get(tableName)
						.incrementer(new RunIdIncrementer())
						.flow(stepA)
						.end()
						.build();
				myJobRegistry.register(new ReferenceJobFactory(tableJob));
				batchJobs.put(tableName,tableJob);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static GenericBeanDefinition createCompositeItemWriterBeanDefinition(
			List outputTablesFlatFileItermWriter) {
		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setBeanClass(CompositeItemWriter.class);
		bd.getPropertyValues().add("delegates", outputTablesFlatFileItermWriter);
		return bd;
	}
	
	private GenericBeanDefinition tableFlatFileItemReaderBeanDefinition(
			String beanClassName,
			String inputFile,
			String fieldSpliter,
			String endOfLine) {
		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setBeanClassName(beanClassName);
		DelimitedLineTokenizer myTokenizer = new DelimitedLineTokenizer();
		myTokenizer.setDelimiter(fieldSpliter);
		myTokenizer.setNames(TableSubAssetClassAttributes.getDeclaredFieldsNames().toArray(new String[0]));
		
		
		BeanWrapperFieldSetMapper<TableSubAssetClassAttributes> myMapper = new BeanWrapperFieldSetMapper<>();
		myMapper.setTargetType(TableSubAssetClassAttributes.class);
		
		DefaultLineMapper<TableSubAssetClassAttributes> myLineMapper = new DefaultLineMapper<>();
		myLineMapper.setLineTokenizer(myTokenizer);
		myLineMapper.setFieldSetMapper(myMapper);
		
		
		
		SuffixRecordSeparatorPolicy myRecordSeparatorPolicy = new SuffixRecordSeparatorPolicy();
		System.out.println("lineEND is " + endOfLine);
		myRecordSeparatorPolicy.setSuffix(endOfLine);
		
		bd.getPropertyValues().add("lineMapper", myLineMapper);
		bd.getPropertyValues().add("resource", new FileSystemResource(inputFile));
		bd.getPropertyValues().add("recordSeparatorPolicy", myRecordSeparatorPolicy);
		
		return bd;
	}
	
	private GenericBeanDefinition createTableOutFlatFileItemWriterBeanDefinition(String beanClassName,
			List<String> orderedColumnList,
			Map<String,String> mismatchedColumnNameMap,
			String outputFile,
			String fieldSpliter) {
		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setBeanClassName(beanClassName);
		bd.setResource(new FileSystemResource(outputFile));
		bd.getPropertyValues().add("resource", new FileSystemResource(outputFile));
		bd.getPropertyValues().add("orderedColumnList", orderedColumnList);
		bd.getPropertyValues().add("mismatchedColumnNameMap", mismatchedColumnNameMap);
		DelimitedLineAggregator<List<String>> myAggregator = new DelimitedLineAggregator<>();
		myAggregator.setDelimiter(fieldSpliter);
		myAggregator.setFieldExtractor(new PassThroughFieldExtractor<List<String>>());
		bd.getPropertyValues().add("lineAggregator",myAggregator);
		return bd;
	}
	
	private GenericBeanDefinition createTableProcessorBeanDefinition(String beanClassName) {
		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setBeanClassName(beanClassName);
		return bd;
	}
}