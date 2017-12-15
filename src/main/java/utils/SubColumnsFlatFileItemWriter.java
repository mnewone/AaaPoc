package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.util.Assert;

public class SubColumnsFlatFileItemWriter extends FlatFileItemWriter<List<String>>{
	private static final Logger log = LoggerFactory.getLogger(SubColumnsFlatFileItemWriter.class);
	
	private final Map<String,String> columnListOrder;
	
	public SubColumnsFlatFileItemWriter(Map<String,String> columnListOrder) {
		super();
		this.columnListOrder = columnListOrder;
	}
	
	@Override
	public void afterPropertiesSet(){
		Assert.notNull(columnListOrder, "Column list must be set");
		try {
			super.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void write(java.util.List<? extends List<String>> items) throws Exception{
		
		List<List<String>> myColumnLists= new ArrayList<>();
		
		log.info("Table column names are " + columnListOrder.values().toString());
		for(List<String> it:items) {
			List<String> myColumnList = new ArrayList<>();
			int columnsMatched = 0;
			for(String column:columnListOrder.values()) {
				for(int i=0;i<it.size();i++) {
					if(it.get(i) != null) {
						if(it.get(i).equals(column)) {
							myColumnList.add(it.get(i + 1));
							columnsMatched = columnsMatched + 1;
							log.info("belonging to table1 " +column + "=" + it.get(i + 1));
						}
					}
					
				}
			}
			if(columnListOrder.size() == columnsMatched) {
				log.info("all columns found " + myColumnList.toString());
			} else {
				log.error("Some columns missing for this table, exiting...");
				System.exit(1);
			}
			myColumnLists.add(myColumnList);
		}
		log.info("start to write columns " + columnListOrder.toString());
		super.write(myColumnLists);
	}

}
