package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "clientID",
    "adultCount",
    "orderTotalPriceInfo",
    "endDate",
    "cityCode",
    "paxInfo",
    "orderSupplierCommercials",
    "clientType",
    "supplier_Details"
})
public class ActivityDetails {

	    @JsonProperty("clientID")
	    private String clientID;
	    @JsonProperty("adultCount")
	    private String adultCount;
	    @JsonProperty("orderClientTotalPriceInfo")
	    private List<orderClientTotalPriceInfo> orderClientTotalPriceInfo = new ArrayList<orderClientTotalPriceInfo>();
	    @JsonProperty("endDate")
	    private String endDate;
	    @JsonProperty("cityCode")
	    private String cityCode;
	    @JsonProperty("orderSupplierCommercials")
	    private List<OrderSupplierCommercial> orderSupplierCommercial = new ArrayList<OrderSupplierCommercial>();
	    @JsonProperty("clientType")
	    private String clientType;
	    @JsonProperty("supplier_Details")
	    private SupplierDetails supplier_Details;
	    @JsonProperty("countryCode")
	    private String countryCode;
	    @JsonProperty("pickupDropoff")
	    private PickupDropoff pickupDropoff;
	    @JsonProperty("supplierPriceInfo")
	    private List<SupplierPriceInfo> supplierPriceInfo;  
	    @JsonProperty("supplierID")
	    private String supplierID;  
	    @JsonProperty("clientCurrency") 
	    private String clientCurrency; 
	    @JsonProperty("supplierBrandCode") 
	    private String supplierBrandCode;  
	    @JsonProperty("childCount")
	    private String childCount; 
	    @JsonProperty("orderClientCommercials")
	    private List<OrderClientCommercial> orderClientCommercials = new ArrayList<OrderClientCommercial>();
	    @JsonProperty("supplierProductCode")
	    private String supplierProductCode;
	    @JsonProperty("name")
	    private String name;
	    @JsonProperty("timeSlotDetails")
	    private List<TimeSlotDetails> timeSlotDetails;
	    @JsonProperty("startDate")
	    private String startDate;
	    @JsonProperty("status")
	    private String status;
	    @JsonProperty("supp_booking_reference")
	    private String supp_booking_reference;
	    
	    
	    public List<orderClientTotalPriceInfo> getOrderClientTotalPriceInfo() {
			return orderClientTotalPriceInfo;
		}
		public void setOrderClientTotalPriceInfo(List<orderClientTotalPriceInfo> orderClientTotalPriceInfo) {
			this.orderClientTotalPriceInfo = orderClientTotalPriceInfo;
		}
		public String getSupp_booking_reference() {
			return supp_booking_reference;
		}
		public void setSupp_booking_reference(String supp_booking_reference) {
			this.supp_booking_reference = supp_booking_reference;
		}
		public String getSupplierProductCode() {
			return supplierProductCode;
		}
		public void setSupplierProductCode(String supplierProductCode) {
			this.supplierProductCode = supplierProductCode;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<TimeSlotDetails> getTimeSlotDetails() {
			return timeSlotDetails;
		}
		public void setTimeSlotDetails(List<TimeSlotDetails> timeSlotDetails) {
			this.timeSlotDetails = timeSlotDetails;
		}
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getClientID() {
			return clientID;
		}
		public void setClientID(String clientID) {
			this.clientID = clientID;
		}
		public String getAdultCount() {
			return adultCount;
		}
		public void setAdultCount(String adultCount) {
			this.adultCount = adultCount;
		}
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		public String getCityCode() {
			return cityCode;
		}
		public void setCityCode(String cityCode) {
			this.cityCode = cityCode;
		}
		public List<OrderSupplierCommercial> getOrderSupplierCommercial() {
			return orderSupplierCommercial;
		}
		public void setOrderSupplierCommercial(List<OrderSupplierCommercial> orderSupplierCommercial) {
			this.orderSupplierCommercial = orderSupplierCommercial;
		}
		public String getClientType() {
			return clientType;
		}
		public void setClientType(String clientType) {
			this.clientType = clientType;
		}
		public SupplierDetails getSupplier_Details() {
			return supplier_Details;
		}
		public void setSupplier_Details(SupplierDetails supplier_Details) {
			this.supplier_Details = supplier_Details;
		}
		public String getCountryCode() {
			return countryCode;
		}
		public void setCountryCode(String countryCode) {
			this.countryCode = countryCode;
		}
		public PickupDropoff getPickupDropoff() {
			return pickupDropoff;
		}
		public void setPickupDropoff(PickupDropoff pickupDropoff) {
			this.pickupDropoff = pickupDropoff;
		}
		public List<SupplierPriceInfo> getSupplierPriceInfo() {
			return supplierPriceInfo;
		}
		public void setSupplierPriceInfo(List<SupplierPriceInfo> supplierPriceInfo) {
			this.supplierPriceInfo = supplierPriceInfo;
		}
		public String getSupplierID() {
			return supplierID;
		}
		public void setSupplierID(String supplierID) {
			this.supplierID = supplierID;
		}
		public String getClientCurrency() {
			return clientCurrency;
		}
		public void setClientCurrency(String clientCurrency) {
			this.clientCurrency = clientCurrency;
		}
		public String getSupplierBrandCode() {
			return supplierBrandCode;
		}
		public void setSupplierBrandCode(String supplierBrandCode) {
			this.supplierBrandCode = supplierBrandCode;
		}
		public String getChildCount() {
			return childCount;
		}
		public void setChildCount(String childCount) {
			this.childCount = childCount;
		}
		public List<OrderClientCommercial> getOrderClientCommercials() {
			return orderClientCommercials;
		}
		public void setOrderClientCommercials(List<OrderClientCommercial> orderClientCommercials) {
			this.orderClientCommercials = orderClientCommercials;
		}
	    
	    
	    
	    
}
