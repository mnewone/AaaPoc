package utils;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import dataSchema.SubAssetClassAttributes;
import dataSchema.SubAssetClassAttributes.IlliquidThreshold;
import dataSchema.SubAssetClassAttributes.LiquidAnyTradePercentile;
import dataSchema.SubAssetClassAttributes.LiquidThreshold;
import dataSchema.SubAssetClassAttributes.LiquidityInformation;
import dataSchema.SubAssetClassAttributes.QuantitativeLiquidityCriteria;
import dataSchema.SubAssetClassAttributes.Test;

@Component(value = "liquidityInformationProcessor")
public class LiquidityInformationProcessor implements ItemProcessor<String[],List<String>>{
	private static final Logger log = LoggerFactory.getLogger(LiquidityInformationProcessor.class);
	@Value("#{'${tableSubAssetClassAttributes.columnListOrder}'.split(',')}")
	private List<String> columnListOrder;
	
	@Value("#{${liquidityInformation.columnNameMap}}")
	private Map<String,String> columnNameMap;
	
	
	public List<String> process(String[] item) throws Exception{
		Map<String,String> oneRow = new HashMap<>();
		int orderNum = 0;
		for(Field field:SubAssetClassAttributes.class.getDeclaredFields()) {
			
			if(field.getType() != LiquidityInformation.class) {
				oneRow.put(field.getName(), item[orderNum]);
				//System.out.println(field.getName() + orderNum + item[orderNum]);
			} else {
				ObjectMapper myMapper = new ObjectMapper();
				
				SubAssetClassAttributes myLiquidityInformation = myMapper.readValue(
						item[orderNum],SubAssetClassAttributes.class);
				LiquidityInformation liquidityInformation = myLiquidityInformation.getLiquidityInformation();
				
				oneRow.put(columnNameMap.get("LiquidityFlag"),liquidityInformation.getLiquidityFlag());
				int i = 1;
				for(Test it:liquidityInformation.getTests()) {
					
					oneRow.put(columnNameMap.get("Test" + i + "Order"),it.getOrder());
					int j = 1;
					for(QuantitativeLiquidityCriteria jt:it.getQuantitativeLiquidityCriterias()) {
						oneRow.put(columnNameMap.get("Test" + i + "QuantitativeLiquidityCriteria" + j +
								"TypeId"),jt.getTypeId());
						oneRow.put(columnNameMap.get("Test" + i + "QuantitativeLiquidityCriteria" + j +
								"Value"),jt.getValue());
						j = j +1;
					}
					j = 1;
					for(LiquidThreshold jt:it.getLiquidThresholds()) {
						oneRow.put(columnNameMap.get("Test" + i + "LiquidThreshold" + j + "ADNArangeId"), 
								jt.getADNArangeId());
						
						int k = 1;
						for(LiquidAnyTradePercentile kt:jt.getSSTIpreTrade().getPercentiles()) {
							oneRow.put(columnNameMap.get("Test" + i + "LiquidThreshold" + j + 
									"SSTIpreTradePercentile" + k + "Id"), kt.getId());
							oneRow.put(columnNameMap.get("Test" + i + "LiquidThreshold" + j + 
									"SSTIpreTradePercentile" + k + "Value"), kt.getValue());
							k = k + 1;
						}
						oneRow.put(columnNameMap.get("Test" + i + "LiquidThreshold" + j + "SSTIpreTradeFloor"),
								jt.getSSTIpreTrade().getFloor());
						k = 1;
						for(LiquidAnyTradePercentile kt:jt.getLISTpreTrade().getPercentiles()) {
							oneRow.put(columnNameMap.get("Test" + i + "LiquidThreshold" + j + 
									"LISTpreTradePercentile" + k + "Id"), kt.getId());
							oneRow.put(columnNameMap.get("Test" + i + "LiquidThreshold" + j + 
									"LISTpreTradePercentile" + k + "Value"), kt.getValue());
							k = k + 1;
						}
						oneRow.put(columnNameMap.get("Test" + i + "LiquidThreshold" + j + "LISTpreTradeFloor"),
								jt.getLISTpreTrade().getFloor());
						k = 1;
						for(LiquidAnyTradePercentile kt:jt.getSSTIpostTrade().getPercentiles()) {
							oneRow.put(columnNameMap.get("Test" + i + "LiquidThreshold" + j + 
									"SSTIpostTradePercentile" + k + "Id"), kt.getId());
							oneRow.put(columnNameMap.get("Test" + i + "LiquidThreshold" + j + 
									"SSTIpostTradePercentile" + k + "Value"), kt.getValue());
							k = k + 1;
						}
						oneRow.put(columnNameMap.get("Test" + i + "LiquidThreshold" + j + "SSTIpostTradeFloor"),
								jt.getSSTIpostTrade().getFloor());
						k = 1;
						for(LiquidAnyTradePercentile kt:jt.getLISTpostTrade().getPercentiles()) {
							oneRow.put(columnNameMap.get("Test" + i + "LiquidThreshold" + j + 
									"LISTpostTradePercentile" + k + "Id"), kt.getId());
							oneRow.put(columnNameMap.get("Test" + i + "LiquidThreshold" + j + 
									"LISTpostTradePercentile" + k + "Value"), kt.getValue());
							k = k + 1;
						}
						oneRow.put(columnNameMap.get("Test" + i + "LiquidThreshold" + j + "LISTpostTradeFloor"),
								jt.getLISTpostTrade().getFloor());
						j = j + 1;
					}
					j = 1;
					for(IlliquidThreshold jt:it.getIlliquidThresholds()) {
						oneRow.put(columnNameMap.get("Test" + i + "IlliquidThreshold" + j + "SSTIpreTradeValue"),
								jt.getSSTIpreTrade().getValue());
						oneRow.put(columnNameMap.get("Test" + i + "IlliquidThreshold" + j + "LISTpreTradeValue"),
								jt.getLISTpreTrade().getValue());
						oneRow.put(columnNameMap.get("Test" + i + "IlliquidThreshold" + j + "SSTIpostTradeValue"),
								jt.getSSTIpostTrade().getValue());
						oneRow.put(columnNameMap.get("Test" + i + "IlliquidThreshold" + j + "LISTpostTradeValue"),
								jt.getLISTpostTrade().getValue());
						j = j + 1;
					}
					i = i +1;
				}
				
			}
			orderNum = orderNum + 1;
		}

		List<String> bcpRowList = new ArrayList<>();
		for(String column:columnListOrder) {
			bcpRowList.add(oneRow.get(column));
		}
		log.info("processor has come to succeed!");
		return bcpRowList;
	}
}
