package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsHolidaysProductOrderDetails {
    @JsonProperty("productName")
    private String productName;
    @JsonProperty("orderIds")
    private List<String> orderIds;
    @JsonProperty("hotelDetails")
    private OpsHotelDetails opsHotelDetails;
    @JsonProperty("activityDetails")
    private List<OpsActivitiesDetails> activityDetails;
    @JsonProperty("transferDetails")
    private List<OpsTransferDetails> transferDetails;
    @JsonProperty("insuranceDetails")
    private List<OpsInsuranceDetails> insuranceDetails;
    @JsonProperty("holidaysExtrasDetails")
    private List<OpsHolidaysExtrasDetails> holidaysExtrasDetails;
    @JsonProperty("holidaysExtensionNightDetails")
    private List<OpsHolidaysExtensionNightDetails> holidaysExtensionNightDetails;

    public OpsHolidaysProductOrderDetails() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<String> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds;
    }


    public OpsHotelDetails getOpsHotelDetails() {
        return opsHotelDetails;
    }

    public void setOpsHotelDetails(OpsHotelDetails opsHotelDetails) {
        this.opsHotelDetails = opsHotelDetails;
    }

    public List<OpsActivitiesDetails> getActivityDetails() {
        return activityDetails;
    }

    public void setActivityDetails(List<OpsActivitiesDetails> activityDetails) {
        this.activityDetails = activityDetails;
    }

    public List<OpsTransferDetails> getTransferDetails() {
        return transferDetails;
    }

    public void setTransferDetails(List<OpsTransferDetails> transferDetails) {
        this.transferDetails = transferDetails;
    }

    public List<OpsInsuranceDetails> getInsuranceDetails() {
        return insuranceDetails;
    }

    public void setInsuranceDetails(List<OpsInsuranceDetails> insuranceDetails) {
        this.insuranceDetails = insuranceDetails;
    }

    public List<OpsHolidaysExtrasDetails> getHolidaysExtrasDetails() {
        return holidaysExtrasDetails;
    }

    public void setHolidaysExtrasDetails(List<OpsHolidaysExtrasDetails> holidaysExtrasDetails) {
        this.holidaysExtrasDetails = holidaysExtrasDetails;
    }

    public List<OpsHolidaysExtensionNightDetails> getHolidaysExtensionNightDetails() {
        return holidaysExtensionNightDetails;
    }

    public void setHolidaysExtensionNightDetails(List<OpsHolidaysExtensionNightDetails> holidaysExtensionNightDetails) {
        this.holidaysExtensionNightDetails = holidaysExtensionNightDetails;
    }
}
