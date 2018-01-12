
package dataSchema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

import dataSchema.TablesBeanMeta.OutputTablesBeanMeta;
import interfaces.TableInfo;
public class TableSubAssetClassAttributes{
//public class TableSubAssetClassAttributes implements TableInfo{
	
	private String subAssetClassId;
	private String segmentationCriteria;
	private String liquidityInformation;
	private String illiquidSSTIpreTrade;
	private String illiquidLISpreTrade;
	private String illiquidSSTIpostTrade;
	private String illiquidLISpostTrade;
	private String systemFrom;
	private String effectiveFrom;
	private String effectiveTo;
	private String systemTo;
	
	
	public void setSubAssetClassId(String subAssetClassId) {
		this.subAssetClassId = subAssetClassId;
	}
	public String getSubAssetClassId() {
		return this.subAssetClassId;
	}
	
	public void setSegmentationCriteria(String segmentationCriteria) {
		this.segmentationCriteria = segmentationCriteria;
	}
	public String getSegmentationCriteria() {
		return this.segmentationCriteria;
	}
	
	public void setLiquidityInformation(String liquidityInformation) {
		this.liquidityInformation = liquidityInformation;
	}
	public String getLiquidityInformation() {
		return this.liquidityInformation;
	}
	
	public void setIlliquidSSTIpreTrade(String illiquidSSTIpreTrade) {
		this.illiquidSSTIpreTrade = illiquidSSTIpreTrade;
	}
	public String getIlliquidSSTIpreTrade() {
		return this.illiquidSSTIpreTrade;
	}
	
	public void setIlliquidLISpreTrade(String illiquidLISpreTrade) {
		this.illiquidLISpreTrade = illiquidLISpreTrade;
	}
	public String getIlliquidLISpreTrade() {
		return this.illiquidLISpreTrade;
	}
	
	public void setIlliquidSSTIpostTrade(String illiquidSSTIpostTrade) {
		this.illiquidSSTIpostTrade = illiquidSSTIpostTrade;
	}
	public String getIlliquidSSTIpostTrade() {
		return this.illiquidSSTIpostTrade;
	}
	
	public void setIlliquidLISpostTrade(String illiquidLISpostTrade) {
		this.illiquidLISpostTrade = illiquidLISpostTrade;
	}
	public String getIlliquidLISpostTrade() {
		return this.illiquidLISpostTrade;
	}
	
	public void setSystemFrom(String systemFrom) {
		this.systemFrom = systemFrom;
	}
	public String getSystemFrom() {
		return this.systemFrom;
	}
	
	public void setEffectiveFrom(String effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}
	public String getEffectiveFrom() {
		return this.effectiveFrom;
	}
	
	public void setEffectiveTo(String effectiveTo) {
		this.effectiveTo = effectiveTo;
	}
	public String getEffectiveTo() {
		return this.effectiveTo;
	}
	
	public void setSystemTo(String systemTo) {
		this.systemTo = systemTo;
	}
	public String getSystemTo() {
		return this.systemTo;
	}
	
	public Map<String,String> getAllNonJsonFields(){
		Map<String,String> allFields = new HashMap<>();
		allFields.put("SubAssetClassId",this.getSubAssetClassId());
		allFields.put("SegmentationCriteria",this.getSegmentationCriteria());
		//allFields.put("LiquidityInformation",this.getLiquidityInformation());
		allFields.put("IlliquidSSTIpreTrade",this.getIlliquidSSTIpreTrade());
		allFields.put("IlliquidLISpreTrade",this.getIlliquidLISpreTrade());
		allFields.put("IlliquidSSTIpostTrade",this.getIlliquidSSTIpostTrade());
		allFields.put("IlliquidLISpostTrade",this.getIlliquidLISpostTrade());
		allFields.put("SystemFrom",this.getSystemFrom());
		allFields.put("EffectiveFrom",this.getEffectiveFrom());
		allFields.put("EffectiveTo",this.getEffectiveTo());
		allFields.put("SystemTo",this.getSystemTo());	
		return allFields;
	}
	
	public static TablesBeanMeta getTablesBeanMeta(){
		
		List<TablesBeanMeta.OutputTablesBeanMeta> outputTables = new ArrayList<>();
		
		//---table1---
		List<String> orderedColumnList = new ArrayList<>();
		orderedColumnList.add("SubAssetClassId");
		orderedColumnList.add("test1Order");
		orderedColumnList.add("liquidityFlag");
		Map<String,String> mismatchedColumnNameMap = new HashMap<>();
		mismatchedColumnNameMap.put("test1Order","Test1Order");
		mismatchedColumnNameMap.put("liquidityFlag","LiquidityFlag");
		outputTables.add(generateOutputTable(
				"table1",
				orderedColumnList,
				mismatchedColumnNameMap,
				"table1FlatFileItemWriter",
				"utils.SpecifiedColumnListFlatFileItemWriter",
				"|","C:/Users/u6041989/springtest/table1OutputFile"));
		
		//---table2
		orderedColumnList = new ArrayList<>();
		orderedColumnList.add("SubAssetClassId");
		orderedColumnList.add("test1Order");
		orderedColumnList.add("liquidityFlag");
		orderedColumnList.add("Test1QuantitativeLiquidityCriteria1TypeId");
		mismatchedColumnNameMap = new HashMap<>();
		mismatchedColumnNameMap.put("test1Order","Test1Order");
		mismatchedColumnNameMap.put("liquidityFlag","LiquidityFlag");
		outputTables.add(generateOutputTable(
				"table2",
				orderedColumnList,
				mismatchedColumnNameMap,
				"table2FlatFileItemWriter",
				"utils.SpecifiedColumnListFlatFileItemWriter",
				"|","C:/Users/u6041989/springtest/table2OutputFile"));
		
		TablesBeanMeta myTablesBeanMeta = generateTableBeanMeta(
				"tableSubAssetClassAttributes",
				"tableSubAssetClassAttributesFlatFileItemReader",
				"org.springframework.batch.item.file.FlatFileItemReader",
				"C:/Users/u6041989/springtest/subclassidliquidinfo2",
				"|","EEOODD","liquidityInformationProcessor",
				"utils.LiquidityInformationProcessor",
				"tableOutputFlatFileItemCompositeWriter",
				outputTables);
		return myTablesBeanMeta;
		
	}
	
	private static TablesBeanMeta generateTableBeanMeta(
			String tableName,
			String readerBeanName,
			String readerBeanClassName,
			String inputFile,
			String fieldSpliter,
			String endOfLine,
			String processorBeanName,
			String processorBeanClassName,
			String compositeWriterBeanName,
			List<OutputTablesBeanMeta> outputTables) {
		TablesBeanMeta myTableMeta = new TablesBeanMeta();
		myTableMeta.setTableName(tableName);
		myTableMeta.setReaderBeanName(readerBeanName);
		myTableMeta.setReaderBeanClassName(readerBeanClassName);
		myTableMeta.setInputFile(inputFile);
		myTableMeta.setFieldSpliter(fieldSpliter);
		myTableMeta.setEndOfLine(endOfLine);
		myTableMeta.setProcessorBeanName(processorBeanName);
		myTableMeta.setProcessorBeanClassName(processorBeanClassName);
		myTableMeta.setCompositeWriterBeanName(compositeWriterBeanName);
		myTableMeta.setOutputTables(outputTables);
		return myTableMeta;
	}
	
	private static TablesBeanMeta.OutputTablesBeanMeta generateOutputTable(
			String outputTableName,
			List<String> orderedColumnList,
			Map<String,String> mismatchedColumnNameMap,
			String beanName,
			String beanClassName,
			String fieldSpliter,
			String tableOutputFile) {
		TablesBeanMeta.OutputTablesBeanMeta myTableMeta = new TablesBeanMeta.OutputTablesBeanMeta();
		myTableMeta.setOutputTableName(outputTableName);
		myTableMeta.setOrderedColumnList(orderedColumnList);
		myTableMeta.setMismatchedColumnNameMapListMap(mismatchedColumnNameMap);
		myTableMeta.setBeanName(beanName);
		myTableMeta.setBeanClassName(beanClassName);
		myTableMeta.setFieldSpliter(fieldSpliter);
		myTableMeta.setTableOutputFile(tableOutputFile);
		return myTableMeta;
	}
	
	public static List<String> getDeclaredFieldsNames() {
		List<String> allDeclaredFieldsNames = new ArrayList<>();
		allDeclaredFieldsNames.add("SubAssetClassId");
		allDeclaredFieldsNames.add("SegmentationCriteria");
		allDeclaredFieldsNames.add("LiquidityInformation");
		allDeclaredFieldsNames.add("IlliquidSSTIpreTrade");
		allDeclaredFieldsNames.add("IlliquidLISpreTrade");
		allDeclaredFieldsNames.add("IlliquidSSTIpostTrade");
		allDeclaredFieldsNames.add("IlliquidLISpostTrade");
		allDeclaredFieldsNames.add("SystemFrom");
		allDeclaredFieldsNames.add("EffectiveFrom");
		allDeclaredFieldsNames.add("EffectiveTo");
		allDeclaredFieldsNames.add("SystemTo");
		return allDeclaredFieldsNames;
		
	}
}