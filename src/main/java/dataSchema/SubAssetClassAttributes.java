
package dataSchema;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubAssetClassAttributes {
	
	private String subAssetClassId;
	private String segmentationCriteria;
	@JsonProperty("LiquidityInformation")
	private LiquidityInformation liquidityInformation;
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
	
	public void setLiquidityInformation(LiquidityInformation liquidityInformation) {
		this.liquidityInformation = liquidityInformation;
	}
	public LiquidityInformation getLiquidityInformation() {
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
	
	public static final class LiquidityInformation{
		@JsonProperty("LiquidityFlag")
		private String liquidityFlag;
		@JsonProperty("Tests")
		private List<Test> tests;
		
		
		public void setLiquidityFlag(String liquidityFlag) {
			this.liquidityFlag = liquidityFlag;
		}
		public String getLiquidityFlag() {
			return this.liquidityFlag;
		}
		
		public void setTests(List<Test> tests) {
			this.tests = tests;
		}
		public List<Test> getTests() {
			return this.tests;
		}
		
	}
	
	public static final class Test{
		@JsonProperty("Order")
		private String order;
		@JsonProperty("QuantitativeLiquidityCriteria")
		private List<QuantitativeLiquidityCriteria> quantitativeLiquidityCriteria;
		@JsonProperty("LiquidThresholds")
		private List<LiquidThreshold> liquidThresholds;
		@JsonProperty("IlliquidThresholds")
		private List<IlliquidThreshold> illiquidThresholds;
		
		public void setOrder(String order) {
			this.order = order;
		}
		public String getOrder() {
			return this.order;
		}
		
		public void setQuantitativeLiquidityCriteria(List<QuantitativeLiquidityCriteria> quantitativeLiquidityCriteria) {
			this.quantitativeLiquidityCriteria = quantitativeLiquidityCriteria;
		}
		public List<QuantitativeLiquidityCriteria> getQuantitativeLiquidityCriterias(){
			return this.quantitativeLiquidityCriteria;
		}
		
		public void setLiquidThreshold(List<LiquidThreshold> liquidThresholds) {
			this.liquidThresholds = liquidThresholds;
		}
		public List<LiquidThreshold> getLiquidThresholds(){
			return this.liquidThresholds;
		}
		
		public void setIlliquidThreshold(List<IlliquidThreshold> illiquidThresholds) {
			this.illiquidThresholds = illiquidThresholds;
		}
		public List<IlliquidThreshold> getIlliquidThresholds(){
			return this.illiquidThresholds;
		}
	}
	
	public static final class QuantitativeLiquidityCriteria{
		//@JsonProperty("TypeId")
		private String typeId;
		//@JsonProperty("Value")
		private String value;
		
		public void setTypeId(String typeId) {
			this.typeId = typeId;
		}
		public String getTypeId() {
			return this.typeId;
		}
		
		public void setValue(String value) {
			this.value = value;
		}
		public String getValue() {
			return this.value;
		}
	}
	
	public static final class LiquidThreshold{
		@JsonProperty("ADNArangeId")
		private String aDNArangeId;
		@JsonProperty("SSTIpreTrade")
		private LiquidAnyTrade sSTIpreTrade;
		@JsonProperty("LISTpreTrade")
		private LiquidAnyTrade lISTpreTrade;
		@JsonProperty("SSTIpostTrade")
		private LiquidAnyTrade sSTIpostTrade;
		@JsonProperty("LISTpostTrade")
		private LiquidAnyTrade lISTpostTrade;
		
		public void setADNArangeId(String aDNArangeId) {
			this.aDNArangeId = aDNArangeId;
		}
		public String getADNArangeId() {
			return this.aDNArangeId;
		}
		
		public void setSSTIpreTrade(LiquidAnyTrade sSTIpreTrade) {
			this.sSTIpreTrade = sSTIpreTrade;
		}
		public LiquidAnyTrade getSSTIpreTrade() {
			return this.sSTIpreTrade;
		}
		
		public void setLISTpreTrade(LiquidAnyTrade lISTpreTrade) {
			this.lISTpreTrade = lISTpreTrade;
		}
		public LiquidAnyTrade getLISTpreTrade() {
			return this.lISTpreTrade;
		}
		
		public void setSSTIpostTrade(LiquidAnyTrade sSTIpostTrade) {
			this.sSTIpostTrade = sSTIpostTrade;
		}
		public LiquidAnyTrade getSSTIpostTrade() {
			return this.sSTIpostTrade;
		}
		
		public void setLISTpostTrade(LiquidAnyTrade lISTpostTrade) {
			this.lISTpostTrade = lISTpostTrade;
		}
		public LiquidAnyTrade getLISTpostTrade() {
			return this.lISTpostTrade;
		}
		
		
	}
	
	
	public static final class LiquidAnyTrade{
		@JsonProperty("Percentiles")
		private List<LiquidAnyTradePercentile> percentiles;
		@JsonProperty("Floor")
		private String floor;
		
		public void setPercentiles(List<LiquidAnyTradePercentile> percentiles) {
			this.percentiles = percentiles;
		}
		public List<LiquidAnyTradePercentile> getPercentiles(){
			return this.percentiles;
		}
		
		public void setFloor(String floor) {
			this.floor = floor;
		}
		public String getFloor() {
			return this.floor;
		}
		
	}
	
	public static final class LiquidAnyTradePercentile{
		//@JsonProperty("Id")
		private String id;
		//@JsonProperty("Value")
		private String value;
		
		public void setId(String id) {
			this.id = id;
		}
		public String getId() {
			return this.id;
		}
		
		public void setValue(String value) {
			this.value = value;
		}
		public String getValue() {
			return this.value;
		}
	}
	
	public static final class IlliquidThreshold{
		@JsonProperty("SSTIpreTrade")
		private IlliquidAnyTrade sSTIpreTrade;
		@JsonProperty("LISTpreTrade")
		private IlliquidAnyTrade lISTpreTrade;
		@JsonProperty("SSTIpostTrade")
		private IlliquidAnyTrade sSTIpostTrade;
		@JsonProperty("LISTpostTrade")
		private IlliquidAnyTrade lISTpostTrade;
		
		public void setSSTIpreTrade(IlliquidAnyTrade sSTIpreTrade) {
			this.sSTIpreTrade = sSTIpreTrade;
		}
		public IlliquidAnyTrade getSSTIpreTrade() {
			return this.sSTIpreTrade;
		}
		
		public void setLISTpreTrade(IlliquidAnyTrade lISTpreTrade) {
			this.lISTpreTrade = lISTpreTrade;
		}
		public IlliquidAnyTrade getLISTpreTrade() {
			return this.lISTpreTrade;
		}
		
		public void setSSTIpostTrade(IlliquidAnyTrade sSTIpostTrade) {
			this.sSTIpostTrade = sSTIpostTrade;
		}
		public IlliquidAnyTrade getSSTIpostTrade()
		{
			return this.sSTIpostTrade;
		}
		
		public void setLISTpostTrade(IlliquidAnyTrade lISTpostTrade) {
			this.lISTpostTrade = lISTpostTrade;
		}
		public IlliquidAnyTrade getLISTpostTrade() {
			return this.lISTpostTrade;
		}
	}
	
	
	public static final  class IlliquidAnyTrade{
		@JsonProperty("Value")
		private String value;
		
		public void setValue(String value) {
			this.value = value;
		}
		public String getValue() {
			return this.value;
		}
	}
	/*
	@JsonCreator
	public SubAssetClassIdLiquidityInformation(
			@JsonProperty("subAssetClassId") String subAssetClassId,
			@JsonProperty("liquidityInformation") LiquidityInformation liquidityInformation) {
		this.subAssetClassId = subAssetClassId;
		this.liquidityInformation = liquidityInformation;
	}
	
	public static final class LiquidityInformation{
		private String liquidityFlag;
		private List<Test> tests;
		
		@JsonCreator
		public LiquidityInformation(
				@JsonProperty("liquidityFlag") String liquidityFlag,
				@JsonProperty("tests") List<Test> tests) {
			this.liquidityFlag = liquidityFlag;
			this.tests = tests;
		}
	}
	
	
	
	public static final class Test{
		private String order;
		private List<QuantitativeLiquidityCriteria> quantitativeLiquidityCriterias;
		private List<LiquidThreshold> liquidThresholds;
		private List<IlliquidThreshold> illiquidThresholds;
		
		@JsonCreator
		public Test(
				@JsonProperty("order") String order,
				@JsonProperty("quantitativeLiquidityCriterias") List<QuantitativeLiquidityCriteria> quantitativeLiquidityCriterias,
				@JsonProperty("liquidThresholds") List<LiquidThreshold> liquidThresholds,
				@JsonProperty("illiquidThresholds") List<IlliquidThreshold> illiquidThresholds) {
			this.order = order;
			this.quantitativeLiquidityCriterias = quantitativeLiquidityCriterias;
			this.liquidThresholds = liquidThresholds;
			this.illiquidThresholds = illiquidThresholds;
		}
	}
	
	public static final class QuantitativeLiquidityCriteria{
		private String typeId;
		private String value;
		
		@JsonCreator
		public QuantitativeLiquidityCriteria(
				@JsonProperty("typeId") String typeId,
				@JsonProperty("value") String value) {
			this.typeId = typeId;
			this.value = value;
		}
	}
	
	public static final class LiquidThreshold{
		private String aDNArangeId;
		private LiquidAnyTrade sSTIpreTrade;
		private LiquidAnyTrade lISTpreTrade;
		private LiquidAnyTrade sSTIpostTrade;
		private LiquidAnyTrade lISTpostTrade;
		
		@JsonCreator
		public LiquidThreshold(
				@JsonProperty("aDNArangeId") String aDNArangeId,
				@JsonProperty("sSTIpreTrade") LiquidAnyTrade sSTIpreTrade,
				@JsonProperty("lISTpreTrade") LiquidAnyTrade lISTpreTrade,
				@JsonProperty("sSTIpostTrade") LiquidAnyTrade sSTIpostTrade,
				@JsonProperty("lSTIpostTrade") LiquidAnyTrade lSTIpostTrade) {
			this.aDNArangeId = aDNArangeId;
			this.sSTIpreTrade = sSTIpreTrade;
			this.lISTpreTrade = lISTpreTrade;
			this.sSTIpostTrade = sSTIpostTrade;
			this.lISTpostTrade = lSTIpostTrade;
		}
	}
	
	public static final class LiquidAnyTrade{
		private List<LiquidAnyTradePercentile> percentiles;
		private String floor;
		
		@JsonCreator
		public LiquidAnyTrade(
				@JsonProperty("percentiles") List<LiquidAnyTradePercentile> percentiles,
				@JsonProperty("floor") String floor) {
			this.percentiles = percentiles;
			this.floor = floor;
		}
	}
	
	public static final class LiquidAnyTradePercentile{
		private String id;
		private String value;
		
		@JsonCreator
		public LiquidAnyTradePercentile(
				@JsonProperty("id") String id,
				@JsonProperty("value") String value) {
			this.id = id;
			this.value = value;
		}
	}
	
	public static final class IlliquidThreshold{
		private IlliquidAnyTrade sSTIpreTrade;
		private IlliquidAnyTrade lISTpreTrade;
		private IlliquidAnyTrade sSTIpostTrade;
		private IlliquidAnyTrade lISTpostTrade;
		
		@JsonCreator
		public IlliquidThreshold(
				@JsonProperty("sSTIpreTrade") IlliquidAnyTrade sSTIpreTrade,
				@JsonProperty("lISTpreTrade") IlliquidAnyTrade lISTpreTrade,
				@JsonProperty("sSTIpostTrade") IlliquidAnyTrade sSTIpostTrade,
				@JsonProperty("lSTIpostTrade") IlliquidAnyTrade lSTIpostTrade) {
			this.sSTIpreTrade = sSTIpreTrade;
			this.lISTpreTrade = lISTpreTrade;
			this.sSTIpostTrade = sSTIpostTrade;
			this.lISTpostTrade = lSTIpostTrade;
		}
	}
	
	public static final  class IlliquidAnyTrade{
		private String value;
		
		@JsonCreator
		public IlliquidAnyTrade(@JsonProperty("value") String value) {
			this.value = value;
		}
	}
	*/

}
