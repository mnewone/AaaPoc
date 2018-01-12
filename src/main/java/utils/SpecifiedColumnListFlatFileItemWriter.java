package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.util.Assert;

public class SpecifiedColumnListFlatFileItemWriter extends FlatFileItemWriter<List<String>>{
	private static final Logger log = LoggerFactory.getLogger(SpecifiedColumnListFlatFileItemWriter.class);
	
	private List<String> orderedColumnList;
	
	private Map<String,String> mismatchedColumnNameMap;
	
	public void setOrderedColumnList (List<String> orderedColumnList) {
		this.orderedColumnList = orderedColumnList;
	}
	public List<String> getOrderedColumnList() {
		return this.orderedColumnList;
	}
	
	public void setMismatchedColumnNameMap(Map<String,String> mismatchedColumnNameMap) {
		this.mismatchedColumnNameMap = mismatchedColumnNameMap;
	}
	public Map<String,String> getMismatchedColumnNameMap(){
		return this.mismatchedColumnNameMap;
	}
	
	
	@Override
	public void write(java.util.List<? extends List<String>> items) throws Exception{
		
		List<List<String>> myColumnLists= new ArrayList<>();
		
		log.info("Table column names are " + mismatchedColumnNameMap.values().toString());
		for(List<String> it:items) {
			List<String> myColumnList = new ArrayList<>();
			int columnsFound = 0;
			for(String column:orderedColumnList) {
				if(mismatchedColumnNameMap.containsKey(column)) {
					column = mismatchedColumnNameMap.get(column);
				}
				for(int i=0;i<it.size();i++) {
					if(it.get(i) != null) {
						if(it.get(i).equals(column)) {
							myColumnList.add(it.get(i + 1));
							columnsFound = columnsFound + 1;
						}
					}
				}
			}
			/*
			for(String column:tableOrderedColumnListMap.values()) {
				for(int i=0;i<it.size();i++) {
					if(it.get(i) != null) {
						if(it.get(i).equals(column)) {
							myColumnList.add(it.get(i + 1));
							columnsMatched = columnsMatched + 1;
							//log.info("belonging to table1 " +column + "=" + it.get(i + 1));
						}
					}
					
				}
			}
			*/
			if(orderedColumnList.size() == columnsFound) {
				log.info("all columns found " + myColumnList.toString());
			} else {
				log.error("Some columns missing for this table, exiting...");
				System.exit(1);
			}
			myColumnLists.add(myColumnList);
		}
		log.info("start to write columns " + orderedColumnList.toString());
		super.write(myColumnLists);
	}

}
