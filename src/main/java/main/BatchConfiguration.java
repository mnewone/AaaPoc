package main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.ArrayFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.SuffixRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.PassThroughFieldExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import dataSchema.SubAssetClassAttributes;
import utils.LiquidityInformationProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	@Value("${inputJsonFile}")
	private String inputFile;
	@Value("${outputProcessedFile}")
	private String outputFile;
	@Value("${inputJsonFile.FieldSeparator}")
	private String fieldSpliter;
	@Value("${inputJsonFile.LineEnd}")
	private String endOfLine;
	
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
	public FlatFileItemReader<String[]> myFlatFileItemReader() {
		FlatFileItemReader<String[]> myReader = new FlatFileItemReader<>();
		
		DelimitedLineTokenizer myTokenizer = new DelimitedLineTokenizer();
		myTokenizer.setDelimiter(fieldSpliter);
		
		List<String> columnNamesOfInput = new ArrayList<>();
		for(Field field:SubAssetClassAttributes.class.getDeclaredFields()) {
			columnNamesOfInput.add(field.getName());
		}
		
		myTokenizer.setNames(columnNamesOfInput.toArray(new String[0]));
		
		SuffixRecordSeparatorPolicy myRecordSeparatorPolicy = new SuffixRecordSeparatorPolicy();
		myRecordSeparatorPolicy.setSuffix(endOfLine);
		
		DefaultLineMapper<String[]> myLineMapper = new DefaultLineMapper<>();
		myLineMapper.setLineTokenizer(myTokenizer);
		myLineMapper.setFieldSetMapper(new ArrayFieldSetMapper());
		
		myReader.setResource(new FileSystemResource(inputFile));
		myReader.setLineMapper(myLineMapper);
		myReader.setRecordSeparatorPolicy(myRecordSeparatorPolicy);
		return myReader;
	}
	
	/*
	@Bean
	public FlatFileItemReader<SubAssetClassAttributes> myFlatFileItemReader() {
		FlatFileItemReader<SubAssetClassAttributes> myReader = new FlatFileItemReader<>();
		
		DelimitedLineTokenizer myTokenizer = new DelimitedLineTokenizer();
		myTokenizer.setDelimiter(fieldSpliter);
		List<String> columnNamesOfInput = new ArrayList<>();
		for(Field field:SubAssetClassAttributes.class.getDeclaredFields()) {
			columnNamesOfInput.add(field.getName());
		}
		myTokenizer.setNames(columnNamesOfInput.toArray(new String[0]));
		
		SuffixRecordSeparatorPolicy myRecordSeparatorPolicy = new SuffixRecordSeparatorPolicy();
		myRecordSeparatorPolicy.setSuffix(endOfLine);
		
		BeanWrapperFieldSetMapper<SubAssetClassAttributes> myMapper = new BeanWrapperFieldSetMapper<>();
		myMapper.setTargetType(SubAssetClassAttributes.class);
		DefaultLineMapper<SubAssetClassAttributes> myLineMapper = new DefaultLineMapper<>();
		myLineMapper.setLineTokenizer(myTokenizer);
		myLineMapper.setFieldSetMapper(myMapper);
		
		myReader.setResource(new FileSystemResource(inputFile));
		myReader.setLineMapper(myLineMapper);
		myReader.setRecordSeparatorPolicy(myRecordSeparatorPolicy);
		return myReader;
	}
	*/
	
	@Bean
	public LiquidityInformationProcessor liquidityInformationProcessor() {
		return new LiquidityInformationProcessor();
		
	}
	
	@Bean
	public FlatFileItemWriter<List<String>> myFlatFileItemWriter() {
		FlatFileItemWriter<List<String>> myWriter = new FlatFileItemWriter<>();
		myWriter.setResource(new FileSystemResource(outputFile));
		DelimitedLineAggregator<List<String>> myAggregator = new DelimitedLineAggregator<>();
		myAggregator.setDelimiter(fieldSpliter);
		myAggregator.setFieldExtractor(new PassThroughFieldExtractor<List<String>>());
		myWriter.setLineAggregator(myAggregator);
		return myWriter;
		
	}
	
	@Bean
	public Step step1() {
		return myStepBuilderFactory.get("jsonProcessing")
				.<String[],List<String>>chunk(10)
				.reader(myFlatFileItemReader())
				.processor(liquidityInformationProcessor())
				.writer(myFlatFileItemWriter())
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
