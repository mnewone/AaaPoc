package dataSchema;

import java.util.List;
import java.util.Map;

public class TablesBeanMeta {
	private String tableName;
	private String readerBeanName;
	private String readerBeanClassName;
	private String inputFile;
	private String fieldSpliter;
	private String endOfLine;
	private String processorBeanName;
	private String processorBeanClassName;
	private String compositeWriterBeanName;
	private List composisteWriterDelegates;
	private List<OutputTablesBeanMeta> outputTables;
	
	//public TablesBeanMeta(String tableName) {
	//	this.tableName = tableName;
	//}
	
	public String getTableName() {
		return this.tableName;
	}
	public void setTableName(String str) {
		this.tableName = str;
	}
	
	public String getReaderBeanName() {
		return this.readerBeanName;
	}
	public void setReaderBeanName(String str) {
		this.readerBeanName = str;
	}
	
	public String getReaderBeanClassName() {
		return this.readerBeanClassName;
	}
	public void setReaderBeanClassName(String str) {
		this.readerBeanClassName = str;
	}
	
	public String getInputFile() {
		return this.inputFile;
	}
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	
	public String getFieldSpliter() {
		return this.fieldSpliter;
	}
	public void setFieldSpliter(String fieldSpliter) {
		this.fieldSpliter = fieldSpliter;
	}
	
	public String getEndOfLine() {
		return this.endOfLine;
	}
	public void setEndOfLine(String endOfLine){
		this.endOfLine = endOfLine;
	}
	
	public String getProcessorBeanName() {
		return this.processorBeanName;
	}
	public void setProcessorBeanName(String str) {
		this.processorBeanName = str;
	}
	
	public String getProcessorBeanClassName() {
		return this.processorBeanClassName;
	}
	public void setProcessorBeanClassName(String str) {
		this.processorBeanClassName = str;
	}
	
	public List getComposisteWriterDelegates() {
		return this.composisteWriterDelegates;
	}
	public void setComposisteWriterDelegates(List composisteWriterDelegates) {
		this.composisteWriterDelegates  = composisteWriterDelegates;
	}
	
	public String getCompositeWriterBeanName() {
		return this.compositeWriterBeanName;
	}
	public void setCompositeWriterBeanName(String str) {
		this.compositeWriterBeanName = str;
	}
	
	public List<OutputTablesBeanMeta> getOutputTables(){
		return this.outputTables;
	}
	public void setOutputTables(List<OutputTablesBeanMeta> outputTables){
		this.outputTables = outputTables;
	}
	
	
	public static final class OutputTablesBeanMeta{
		private String outPutTableName;
		private String beanName;
		private String beanClassName;
		private List<String> orderedColumnList;
		private Map<String,String> mismatchedColumnNameMap;
		private String tableOutputFile;
		private String fieldSpliter;
		
		//public TablesBeanMeta(String tableName) {
		//	this.tableName = tableName;
		//}
		
		public String getOutputTableName() {
			return this.outPutTableName;
		}
		public void setOutputTableName(String str) {
			this.outPutTableName = str;
		}
		
		public String getBeanName() {
			return this.beanName;
		}
		public void setBeanName(String str) {
			this.beanName = str;
		}
		
		public String getBeanClassName() {
			return this.beanClassName;
		}
		public void setBeanClassName(String str) {
			this.beanClassName = str;
		}
		
		public List<String> getOrderedColumnList(){
			return this.orderedColumnList;
		}
		public void setOrderedColumnList(List<String> orderedColumnList) {
			this.orderedColumnList = orderedColumnList;
		}
		
		public Map<String,String> getMismatchedColumnNameMap(){
			return this.mismatchedColumnNameMap;
		}
		public void setMismatchedColumnNameMapListMap(Map<String,String> mismatchedColumnNameMap) {
			this.mismatchedColumnNameMap = mismatchedColumnNameMap;
		}
		
		public String getTableOutputFile() {
			return this.tableOutputFile;
		}
		public void setTableOutputFile(String tableOutputFile) {
			this.tableOutputFile = tableOutputFile;
		}
		
		public String getFieldSpliter() {
			return this.fieldSpliter;
		}
		public void setFieldSpliter(String fieldSpliter) {
			this.fieldSpliter = fieldSpliter;
		}
	}
}
