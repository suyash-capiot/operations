package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsActivitiesDetails {
	
	@JsonProperty("activityDetails")
	private String activityDetails;

	@JsonProperty("dateTimeOfVist")
	private String dateTimeOfVist;
	
	@JsonProperty("ticketType")
	private String ticketType;
	
	@JsonProperty("seatingClass")
	private String seatingClass;
	
	@JsonProperty("supplierRefNumber")
	private String supplierRefNumber;
	
	@JsonProperty("timeLimit")
	private String timeLimit;
	
	@JsonProperty("bookingStatus")
	private String bookingStatus;
	
	@JsonProperty("inventory")
	private String inventory;
	
	@JsonProperty("transferDetails")
	private String transferDetails;
	
	@JsonProperty("pickupDropOff")
	private String pickupDropOff;
	
	@JsonProperty("vechileInfo")
	private String vechileInfo;
	
	@JsonProperty("boardingDropOffPoint")
	private String boardingDropOffPoint;
	
	@JsonProperty("credentialsName")
	private String credentialsName;
	
	@JsonProperty("sourceSupplier")
	private String sourceSupplier;
	
	@JsonProperty("enableSupplier")
	private String enableSupplier;
	
	@JsonProperty("supplierRateType")
	private String supplierRateType;
	
	@JsonProperty("refundable")
	private boolean refundable;
	
	@JsonProperty("paxInfo")
	private List<OpsPaxParticular> opsPaxParticulars;
	
	 @JsonProperty("orderSupplierPriceInfo")
	 private OpsActivitiesSupplierPriceInfo opsActivitiesSupplierPriceInfo;

	  @JsonProperty("orderClientTotalPriceInfo")
	  private OpsActivitiesTotalPriceInfo opsActivitiesTotalPriceInfo;

	
	
	public OpsActivitiesSupplierPriceInfo getOpsActivitiesSupplierPriceInfo() {
		return opsActivitiesSupplierPriceInfo;
	}

	public void setOpsActivitiesSupplierPriceInfo(OpsActivitiesSupplierPriceInfo opsActivitiesSupplierPriceInfo) {
		this.opsActivitiesSupplierPriceInfo = opsActivitiesSupplierPriceInfo;
	}

	public OpsActivitiesTotalPriceInfo getOpsActivitiesTotalPriceInfo() {
		return opsActivitiesTotalPriceInfo;
	}

	public void setOpsActivitiesTotalPriceInfo(OpsActivitiesTotalPriceInfo opsActivitiesTotalPriceInfo) {
		this.opsActivitiesTotalPriceInfo = opsActivitiesTotalPriceInfo;
	}

	public List<OpsPaxParticular> getOpsPaxParticulars() {
		return opsPaxParticulars;
	}

	public void setOpsPaxParticulars(List<OpsPaxParticular> opsPaxParticulars) {
		this.opsPaxParticulars = opsPaxParticulars;
	}

	public boolean getRefundable() {
		return refundable;
	}

	public void setRefundable(boolean refundable) {
		this.refundable = refundable;
	}

	public String getActivityDetails() {
		return activityDetails;
	}

	public void setActivityDetails(String activityDetails) {
		this.activityDetails = activityDetails;
	}

	public String getDateTimeOfVist() {
		return dateTimeOfVist;
	}

	public void setDateTimeOfVist(String dateTimeOfVist) {
		this.dateTimeOfVist = dateTimeOfVist;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public String getSeatingClass() {
		return seatingClass;
	}

	public void setSeatingClass(String seatingClass) {
		this.seatingClass = seatingClass;
	}

	public String getSupplierRefNumber() {
		return supplierRefNumber;
	}

	public void setSupplierRefNumber(String supplierRefNumber) {
		this.supplierRefNumber = supplierRefNumber;
	}

	public String getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(String timeLimit) {
		this.timeLimit = timeLimit;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public String getInventory() {
		return inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}

	public String getTransferDetails() {
		return transferDetails;
	}

	public void setTransferDetails(String transferDetails) {
		this.transferDetails = transferDetails;
	}

	public String getPickupDropOff() {
		return pickupDropOff;
	}

	public void setPickupDropOff(String pickupDropOff) {
		this.pickupDropOff = pickupDropOff;
	}

	public String getVechileInfo() {
		return vechileInfo;
	}

	public void setVechileInfo(String vechileInfo) {
		this.vechileInfo = vechileInfo;
	}

	public String getBoardingDropOffPoint() {
		return boardingDropOffPoint;
	}

	public void setBoardingDropOffPoint(String boardingDropOffPoint) {
		this.boardingDropOffPoint = boardingDropOffPoint;
	}

	public String getCredentialsName() {
		return credentialsName;
	}

	public void setCredentialsName(String credentialsName) {
		this.credentialsName = credentialsName;
	}

	public String getSourceSupplier() {
		return sourceSupplier;
	}

	public void setSourceSupplier(String sourceSupplier) {
		this.sourceSupplier = sourceSupplier;
	}

	public String getEnableSupplier() {
		return enableSupplier;
	}

	public void setEnableSupplier(String enableSupplier) {
		this.enableSupplier = enableSupplier;
	}

	public String getSupplierRateType() {
		return supplierRateType;
	}

	public void setSupplierRateType(String supplierRateType) {
		this.supplierRateType = supplierRateType;
	}
	
	
	
	
	
	
	
	
	
	
}
