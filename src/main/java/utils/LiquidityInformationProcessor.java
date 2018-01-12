package utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import dataSchema.JsonLiquidityInformation;
import dataSchema.JsonLiquidityInformation.IlliquidThreshold;
import dataSchema.JsonLiquidityInformation.LiquidAnyTradePercentile;
import dataSchema.JsonLiquidityInformation.LiquidThreshold;
import dataSchema.JsonLiquidityInformation.LiquidityInformation;
import dataSchema.JsonLiquidityInformation.QuantitativeLiquidityCriteria;
import dataSchema.JsonLiquidityInformation.Test;
import dataSchema.TableSubAssetClassAttributes;

public class LiquidityInformationProcessor implements ItemProcessor<TableSubAssetClassAttributes,
List<String>>{
	private static final Logger log = LoggerFactory.getLogger(LiquidityInformationProcessor.class);
	
	public List<String> process(TableSubAssetClassAttributes item) throws Exception{
		Map<String,String> oneRow = new HashMap<>();
		oneRow.putAll(item.getAllNonJsonFields());
		log.info(oneRow.toString());
		ObjectMapper myMapper = new ObjectMapper();
		JsonLiquidityInformation myLiquidityInformation = myMapper.readValue(item.getLiquidityInformation(),
				JsonLiquidityInformation.class);
		LiquidityInformation liquidityInformation = myLiquidityInformation.getLiquidityInformation();
		
		oneRow.put("LiquidityFlag",liquidityInformation.getLiquidityFlag());
		int i = 1;
		for(Test it:liquidityInformation.getTests()) {
			oneRow.put("Test" + i + "Order",it.getOrder());
			int j = 1;
			for(QuantitativeLiquidityCriteria jt:it.getQuantitativeLiquidityCriterias()) {
				
				oneRow.put("Test" + i + "QuantitativeLiquidityCriteria" + j + "TypeId",jt.getTypeId());
				
				oneRow.put("Test" + i + "QuantitativeLiquidityCriteria" + j + "Value",jt.getValue());
				j = j + 1;
			}
			j = 1;
			for(LiquidThreshold jt:it.getLiquidThresholds()) {
				
				oneRow.put("Test" + i + "LiquidThreshold" + j + "ADNArangeId", jt.getADNArangeId());
				
				int k = 1;
				for(LiquidAnyTradePercentile kt:jt.getSSTIpreTrade().getPercentiles()) {
					
					oneRow.put("Test" + i + "LiquidThreshold" + j + 
							"SSTIpreTradePercentile" + k + "Id", kt.getId());
					
					oneRow.put("Test" + i + "LiquidThreshold" + j + 
							"SSTIpreTradePercentile" + k + "Value", kt.getValue());
					k = k + 1;
				}
				
				oneRow.put("Test" + i + "LiquidThreshold" + j + "SSTIpreTradeFloor",
						jt.getSSTIpreTrade().getFloor());
				k = 1;
				for(LiquidAnyTradePercentile kt:jt.getLISTpreTrade().getPercentiles()) {
					
					oneRow.put("Test" + i + "LiquidThreshold" + j + 
							"LISTpreTradePercentile" + k + "Id", kt.getId());
					
					oneRow.put("Test" + i + "LiquidThreshold" + j + 
							"LISTpreTradePercentile" + k + "Value", kt.getValue());
					k = k + 1;
				}
				
				oneRow.put("Test" + i + "LiquidThreshold" + j + "LISTpreTradeFloor",
						jt.getLISTpreTrade().getFloor());
				k = 1;
				for(LiquidAnyTradePercentile kt:jt.getSSTIpostTrade().getPercentiles()) {
				
					oneRow.put("Test" + i + "LiquidThreshold" + j + 
							"SSTIpostTradePercentile" + k + "Id", kt.getId());
					
					oneRow.put("Test" + i + "LiquidThreshold" + j + 
							"SSTIpostTradePercentile" + k + "Value", kt.getValue());
					k = k + 1;
				}
				
				oneRow.put("Test" + i + "LiquidThreshold" + j + "SSTIpostTradeFloor",
						jt.getSSTIpostTrade().getFloor());
				k = 1;
				for(LiquidAnyTradePercentile kt:jt.getLISTpostTrade().getPercentiles()) {
				
					oneRow.put("Test" + i + "LiquidThreshold" + j + 
							"LISTpostTradePercentile" + k + "Id", kt.getId());
					
					oneRow.put("Test" + i + "LiquidThreshold" + j + 
							"LISTpostTradePercentile" + k + "Value", kt.getValue());
					k = k + 1;
				}
				
				oneRow.put("Test" + i + "LiquidThreshold" + j + "LISTpostTradeFloor",
						jt.getLISTpostTrade().getFloor());
				j = j + 1;
			}
			j = 1;
			for(IlliquidThreshold jt:it.getIlliquidThresholds()) {
				
				oneRow.put("Test" + i + "IlliquidThreshold" + j + "SSTIpreTradeValue",
						jt.getSSTIpreTrade().getValue());
				
				
				oneRow.put("Test" + i + "IlliquidThreshold" + j + "LISTpreTradeValue",
						jt.getLISTpreTrade().getValue());
				
				
				oneRow.put("Test" + i + "IlliquidThreshold" + j + "SSTIpostTradeValue",
						jt.getSSTIpostTrade().getValue());
				
				
				oneRow.put("Test" + i + "IlliquidThreshold" + j + "LISTpostTradeValue",
						jt.getLISTpostTrade().getValue());
				j = j + 1;
			}
			i = i +1;
		}
		List<String> bcpRowList = new ArrayList<>();
		for(String column:oneRow.keySet()) {
			//log.info("In processor print column name " + column + " = "+ oneRow.get(column));
			bcpRowList.add(column);
			bcpRowList.add(oneRow.get(column));
		}
		log.info("processor has come to succeed!");
		return bcpRowList;
		
	}
	
	public void close() {
		System.out.print("liquitdityINformationProcessor bean is destroying...");
	}
	
}
