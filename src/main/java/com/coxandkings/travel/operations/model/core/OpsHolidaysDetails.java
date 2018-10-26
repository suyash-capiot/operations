package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsHolidaysDetails {
    @JsonProperty("tourStartDate")
    private String tourStartDate;
    @JsonProperty("tourEndDate")
    private String tourEndDate;
    @JsonProperty("timeLimitExpiryDate")
    private String timeLimitExpiryDate;
    @JsonProperty("reconfirmationDateTime")
    private String reconfirmationDateTime;
    @JsonProperty("amendmentDateTime")
    private String amendmentDateTime;
    @JsonProperty("hub")
    private String hub;
    @JsonProperty("tourDetails")
    private OpsHolidaysTourDetails tourDetails;
    @JsonProperty("roe")
    private String roe;
    @JsonProperty("bookingStatus")
    private String bookingStatus;
    @JsonProperty("holidaySummary")
    private List<OpsHolidaySummary> holidaySummary;
    @JsonProperty("paxInfo")
    private List<OpsHolidaysPaxInfo> paxInfo;
    @JsonProperty("productDetails")
    private List<OpsHolidaysProductOrderDetails> productDetails;
    @JsonProperty("orderSupplierPriceInfo")
    private OpsHolidaysOrderPriceInfo orderSupplierPriceInfo;
    @JsonProperty("orderClientTotalPriceInfo")
    private OpsHolidaysOrderPriceInfo orderClientTotalPriceInfo;

    //Added for Forex
    @JsonProperty("travelStartDate")
    private String travelStartDate;

    @JsonProperty("travelEndDate")
    private String travelEndDate;

    @JsonProperty("multiCurrencyBooking")
    private Boolean isMultiCcyBooking;

    public OpsHolidaysDetails() {
    }

    public String getTourStartDate() {
        return tourStartDate;
    }

    public void setTourStartDate(String tourStartDate) {
        this.tourStartDate = tourStartDate;
    }

    public String getTourEndDate() {
        return tourEndDate;
    }

    public void setTourEndDate(String tourEndDate) {
        this.tourEndDate = tourEndDate;
    }

    public String getTimeLimitExpiryDate() {
        return timeLimitExpiryDate;
    }

    public void setTimeLimitExpiryDate(String timeLimitExpiryDate) {
        this.timeLimitExpiryDate = timeLimitExpiryDate;
    }

    public String getReconfirmationDateTime() {
        return reconfirmationDateTime;
    }

    public void setReconfirmationDateTime(String reconfirmationDateTime) {
        this.reconfirmationDateTime = reconfirmationDateTime;
    }

    public String getAmendmentDateTime() {
        return amendmentDateTime;
    }

    public void setAmendmentDateTime(String amendmentDateTime) {
        this.amendmentDateTime = amendmentDateTime;
    }

    public String getHub() {
        return hub;
    }

    public void setHub(String hub) {
        this.hub = hub;
    }

    public OpsHolidaysTourDetails getTourDetails() {
        return tourDetails;
    }

    public void setTourDetails(OpsHolidaysTourDetails tourDetails) {
        this.tourDetails = tourDetails;
    }

    public String getRoe() {
        return roe;
    }

    public void setRoe(String roe) {
        this.roe = roe;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public List<OpsHolidaySummary> getHolidaySummary() {
        return holidaySummary;
    }

    public void setHolidaySummary(List<OpsHolidaySummary> holidaySummary) {
        this.holidaySummary = holidaySummary;
    }

    public List<OpsHolidaysPaxInfo> getPaxInfo() {
        return paxInfo;
    }

    public void setPaxInfo(List<OpsHolidaysPaxInfo> paxInfo) {
        this.paxInfo = paxInfo;
    }

    public List<OpsHolidaysProductOrderDetails> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<OpsHolidaysProductOrderDetails> productDetails) {
        this.productDetails = productDetails;
    }

    public OpsHolidaysOrderPriceInfo getOrderSupplierPriceInfo() {
        return orderSupplierPriceInfo;
    }

    public void setOrderSupplierPriceInfo(OpsHolidaysOrderPriceInfo orderSupplierPriceInfo) {
        this.orderSupplierPriceInfo = orderSupplierPriceInfo;
    }

    public OpsHolidaysOrderPriceInfo getOrderClientTotalPriceInfo() {
        return orderClientTotalPriceInfo;
    }

    public void setOrderClientTotalPriceInfo(OpsHolidaysOrderPriceInfo orderClientTotalPriceInfo) {
        this.orderClientTotalPriceInfo = orderClientTotalPriceInfo;
    }

    public Boolean getIsMultiCcyBooking() {
        return isMultiCcyBooking;
    }

    public void setIsMultiCcyBooking(Boolean isMultiCcyBooking) {
        this.isMultiCcyBooking = isMultiCcyBooking;
    }
    public String getTravelStartDate() {
        return travelStartDate;
    }

    public void setTravelStartDate(String travelStartDate) {
        this.travelStartDate = travelStartDate;
    }

    public String getTravelEndDate() {
        return travelEndDate;
    }

    public void setTravelEndDate(String travelEndDate) {
        this.travelEndDate = travelEndDate;
    }
}
