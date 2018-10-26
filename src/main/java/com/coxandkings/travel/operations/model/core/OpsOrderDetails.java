package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsOrderDetails implements Serializable {

    @JsonProperty("hotelDetails")
    private OpsHotelDetails hotelDetails;

    @JsonProperty("orderClientCommercials")
    private List<OpsOrderClientCommercial> clientCommercials = new ArrayList<OpsOrderClientCommercial>();

    @JsonProperty("flightDetails")
    private OpsFlightDetails flightDetails;


    @JsonProperty("orderSupplierCommercials")
    private List<OpsOrderSupplierCommercial> supplierCommercials = new ArrayList<OpsOrderSupplierCommercial>();

    @JsonProperty("orderStatus")
    private OpsOrderStatus opsOrderStatus;

    @JsonProperty("bookingAttributes")
    private List<OpsBookingAttribute> opsBookingAttribute;

    @JsonProperty("bookingAttributeValues")
    private Set<String> opsBookingAttributeValues;

    @JsonProperty("supplierType")
    @Enumerated(EnumType.STRING)
    private OpsSupplierType supplierType;

    @JsonProperty("packageDetails")
    private OpsHolidaysDetails packageDetails;

    @JsonProperty("activityDetails")
    private OpsActivitiesDetails activityDetails;

    @JsonProperty("transferDetails")
    private OpsTransferDetails transferDetails;

    @JsonProperty("insuranceDetails")
    private OpsInsuranceDetails insuranceDetails;

    @JsonProperty("holidaysExtrasDetails")
    private OpsHolidaysExtrasDetails holidaysExtrasDetails;

    @JsonProperty("holidaysExtensionNightDetails")
    private OpsHolidaysExtensionNightDetails holidaysExtensionNightDetails;

    private OpsFareRule fareRule;

    public OpsFareRule getFareRule() {
        return fareRule;
    }

    public void setFareRule(OpsFareRule fareRule) {
        this.fareRule = fareRule;
    }

    public OpsOrderDetails() {
    }

    public OpsActivitiesDetails getActivityDetails() {
        return activityDetails;
    }

    public void setActivityDetails(OpsActivitiesDetails activityDetails) {
        this.activityDetails = activityDetails;
    }

    public OpsSupplierType getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(OpsSupplierType supplierType) {
        this.supplierType = supplierType;
    }

    public OpsHotelDetails getHotelDetails() {
        return hotelDetails;
    }

    public void setHotelDetails(OpsHotelDetails hotelDetails) {
        this.hotelDetails = hotelDetails;
    }

    public List<OpsOrderClientCommercial> getClientCommercials() {
        return clientCommercials;
    }

    public void setClientCommercials(List<OpsOrderClientCommercial> clientCommercials) {
        this.clientCommercials = clientCommercials;
    }

    public OpsFlightDetails getFlightDetails() {
        return flightDetails;
    }

    public void setFlightDetails(OpsFlightDetails flightDetails) {
        this.flightDetails = flightDetails;
    }

    public List<OpsOrderSupplierCommercial> getSupplierCommercials() {
        return supplierCommercials;
    }

    public void setSupplierCommercials(List<OpsOrderSupplierCommercial> supplierCommercials) {
        this.supplierCommercials = supplierCommercials;
    }

    public OpsOrderStatus getOpsOrderStatus() {
        return opsOrderStatus;
    }

    public void setOpsOrderStatus(OpsOrderStatus opsOrderStatus) {
        this.opsOrderStatus = opsOrderStatus;
    }

    public List<OpsBookingAttribute> getOpsBookingAttribute() {
        return opsBookingAttribute;
    }

    public void setOpsBookingAttribute(List<OpsBookingAttribute> opsBookingAttribute) {
        this.opsBookingAttribute = opsBookingAttribute;
    }

    public OpsHolidaysDetails getPackageDetails() {
        return packageDetails;
    }

    public void setPackageDetails(OpsHolidaysDetails packageDetails) {
        this.packageDetails = packageDetails;
    }

    public Set<String> getOpsBookingAttributeValues() {
        return opsBookingAttributeValues;
    }

    public void setOpsBookingAttributeValues(Set<String> opsBookingAttributeValues) {
        this.opsBookingAttributeValues = opsBookingAttributeValues;
    }

    public OpsTransferDetails getTransferDetails() {
        return transferDetails;
    }


    public void setTransferDetails(OpsTransferDetails transferDetails) {
        this.transferDetails = transferDetails;
    }


    public OpsInsuranceDetails getInsuranceDetails() {
        return insuranceDetails;
    }


    public void setInsuranceDetails(OpsInsuranceDetails insuranceDetails) {
        this.insuranceDetails = insuranceDetails;
    }


    public OpsHolidaysExtrasDetails getHolidaysExtrasDetails() {
        return holidaysExtrasDetails;
    }


    public void setHolidaysExtrasDetails(OpsHolidaysExtrasDetails holidaysExtrasDetails) {
        this.holidaysExtrasDetails = holidaysExtrasDetails;
    }


    public OpsHolidaysExtensionNightDetails getHolidaysExtensionNightDetails() {
        return holidaysExtensionNightDetails;
    }


    public void setHolidaysExtensionNightDetails(
            OpsHolidaysExtensionNightDetails holidaysExtensionNightDetails) {
        this.holidaysExtensionNightDetails = holidaysExtensionNightDetails;
    }
}
