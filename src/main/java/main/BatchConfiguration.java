package main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.ArrayFieldSetMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.SuffixRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.PassThroughFieldExtractor;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import dataSchema.TableSubAssetClassAttributes;
import utils.LiquidityInformationProcessor;
import utils.SubColumnsFlatFileItemWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	@Value("${input.file}")
	private String inputFile;
	@Value("${input.file.FieldSeparator}")
	private String fieldSpliter;
	@Value("${input.file.LineEnd}")
	private String endOfLine;
	
	@Value("${output.totalColumns.outputFile}")
	private String totalColumnsOutputFile;
	
	@Value("${output.table1.outputFile}")
	private String table1OutputFile;
	@Value("#{${output.table1.OrderedColumnListMap}}")
	private Map<String,String> table1OrderedColumnListMap;
	
	@Value("${output.table2.outputFile}")
	private String table2OutputFile;
	@Value("#{${output.table2.OrderedColumnListMap}}")
	private Map<String,String> table2OrderedColumnListMap;
	
	@Autowired
	private JobBuilderFactory myJobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory myStepBuilderFactory;
	
	
	@Bean
	public DataSource myDataSource() {
		SimpleDriverDataSource myDS = new SimpleDriverDataSource();
		myDS.setDriver(new org.hsqldb.jdbc.JDBCDriver());
		myDS.setUsername("sa");
		myDS.setPassword("");
		myDS.setUrl("jdbc:hsqldb:mem:mydb");
		return myDS;
	}
	
	@Bean
	public FlatFileItemReader<TableSubAssetClassAttributes> myFlatFileItemReader() {
		FlatFileItemReader<TableSubAssetClassAttributes> myReader = new FlatFileItemReader<>();
		
		DelimitedLineTokenizer myTokenizer = new DelimitedLineTokenizer();
		myTokenizer.setDelimiter(fieldSpliter);
		myTokenizer.setNames(TableSubAssetClassAttributes.getDeclaredFieldsNames().toArray(new String[0]));
		
		
		BeanWrapperFieldSetMapper<TableSubAssetClassAttributes> myMapper = new BeanWrapperFieldSetMapper<>();
		myMapper.setTargetType(TableSubAssetClassAttributes.class);
		
		DefaultLineMapper<TableSubAssetClassAttributes> myLineMapper = new DefaultLineMapper<>();
		myLineMapper.setLineTokenizer(myTokenizer);
		myLineMapper.setFieldSetMapper(myMapper);
		
		myReader.setResource(new FileSystemResource(inputFile));
		myReader.setLineMapper(myLineMapper);
		
		SuffixRecordSeparatorPolicy myRecordSeparatorPolicy = new SuffixRecordSeparatorPolicy();
		System.out.println("lineEND is " + endOfLine);
		myRecordSeparatorPolicy.setSuffix(endOfLine);
		myReader.setRecordSeparatorPolicy(myRecordSeparatorPolicy);
		return myReader;
	}
	
	@Bean
	public LiquidityInformationProcessor liquidityInformationProcessor() {
		return new LiquidityInformationProcessor();
	}
	
	@Bean
	public FlatFileItemWriter<List<String>> totalColumnsFlatFileItemWriter() {
		FlatFileItemWriter<List<String>> myWriter = new FlatFileItemWriter<>();
		myWriter.setResource(new FileSystemResource(totalColumnsOutputFile));
		DelimitedLineAggregator<List<String>> myAggregator = new DelimitedLineAggregator<>();
		myAggregator.setDelimiter(fieldSpliter);
		myAggregator.setFieldExtractor(new PassThroughFieldExtractor<List<String>>());
		myWriter.setLineAggregator(myAggregator);
		return myWriter;
		
	}
	
	@Bean
	public SubColumnsFlatFileItemWriter table1FlatFileItemWriter(){
		SubColumnsFlatFileItemWriter myWriter = new SubColumnsFlatFileItemWriter(table1OrderedColumnListMap);
		myWriter.setResource(new FileSystemResource(table1OutputFile));
		DelimitedLineAggregator<List<String>> myAggregator = new DelimitedLineAggregator<>();
		myAggregator.setDelimiter(fieldSpliter);
		myAggregator.setFieldExtractor(new PassThroughFieldExtractor<List<String>>());
		myWriter.setLineAggregator(myAggregator);
		myWriter.afterPropertiesSet();
		return myWriter;
	}
	
	@Bean
	public SubColumnsFlatFileItemWriter table2FlatFileItemWriter() {
		SubColumnsFlatFileItemWriter myWriter = new SubColumnsFlatFileItemWriter(table2OrderedColumnListMap);
		myWriter.setResource(new FileSystemResource(table2OutputFile));
		DelimitedLineAggregator<List<String>> myAggregator = new DelimitedLineAggregator<>();
		myAggregator.setDelimiter(fieldSpliter);
		myAggregator.setFieldExtractor(new PassThroughFieldExtractor<List<String>>());
		myWriter.setLineAggregator(myAggregator);
		myWriter.afterPropertiesSet();
		return myWriter;
	}
	
	@Bean
	public CompositeItemWriter<List<String>> myCompositeItemWriter() {
		CompositeItemWriter<List<String>> myWriter = new CompositeItemWriter<>();
		myWriter.setDelegates(Arrays.asList(table1FlatFileItemWriter(),table2FlatFileItemWriter(),
				totalColumnsFlatFileItemWriter()));
		return myWriter;
	}
	
	@Bean
	public Step step1() {
		return myStepBuilderFactory.get("jsonProcessing")
				.<TableSubAssetClassAttributes,List<String>>chunk(10)
				.reader(myFlatFileItemReader())
				.processor(liquidityInformationProcessor())
				.writer(myCompositeItemWriter())
				.build();
	}
	
	@Bean
	public Job jobA() {
		return myJobBuilderFactory.get("firstTry")
				.incrementer(new RunIdIncrementer())
				.flow(step1())
				.end()
				.build();
				
	}

}
