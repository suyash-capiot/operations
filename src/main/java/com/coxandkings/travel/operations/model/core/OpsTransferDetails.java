package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsTransferDetails {

    //Holidays sub product transfer specific fields
    @JsonProperty("transferType")
    private String transferType;

    @JsonProperty("name")
    private String name;

    @JsonProperty("departureCity")
    private String departureCity;

    @JsonProperty("arrivalCity")
    private String arrivalCity;

    @JsonProperty("departureDate")
    private String departureDate;

    @JsonProperty("arrivalDate")
    private String arrivalDate;

    @JsonProperty("description")
    private String description;

    @JsonProperty("airportName")
    private String airportName;

    @JsonProperty("pickUpLocation")
    private String pickUpLocation;

    @JsonProperty("start")
    private String start;

    @JsonProperty("end")
    private String end;

    @JsonProperty("duration")
    private String duration;

    //Ops common fields

    @JsonProperty("credentialsName")
    private String credentialsName;

    @JsonProperty("supplierRateType")
    private String supplierRateType;

    @JsonProperty("refundable")
    private boolean refundable;

    @JsonProperty("paxInfo")
    private List<OpsAccommodationPaxInfo> opsPaxInfo;

    @JsonProperty("orderSupplierPriceInfo")
    private OpsTransferSupplierPriceInfo opsTransferSupplierPriceInfo;

    @JsonProperty("orderClientTotalPriceInfo")
    private OpsTransferTotalPriceInfo opsTransferTotalPriceInfo;

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCredentialsName() {
        return credentialsName;
    }

    public void setCredentialsName(String credentialsName) {
        this.credentialsName = credentialsName;
    }

    public String getSupplierRateType() {
        return supplierRateType;
    }

    public void setSupplierRateType(String supplierRateType) {
        this.supplierRateType = supplierRateType;
    }

    public boolean isRefundable() {
        return refundable;
    }

    public void setRefundable(boolean refundable) {
        this.refundable = refundable;
    }

    public List<OpsAccommodationPaxInfo> getOpsPaxInfo() {
        return opsPaxInfo;
    }

    public void setOpsPaxInfo(List<OpsAccommodationPaxInfo> opsPaxInfo) {
        this.opsPaxInfo = opsPaxInfo;
    }

    public OpsTransferSupplierPriceInfo getOpsTransferSupplierPriceInfo() {
        return opsTransferSupplierPriceInfo;
    }

    public void setOpsTransferSupplierPriceInfo(
            OpsTransferSupplierPriceInfo opsTransferSupplierPriceInfo) {
        this.opsTransferSupplierPriceInfo = opsTransferSupplierPriceInfo;
    }

    public OpsTransferTotalPriceInfo getOpsTransferTotalPriceInfo() {
        return opsTransferTotalPriceInfo;
    }

    public void setOpsTransferTotalPriceInfo(OpsTransferTotalPriceInfo opsTransferTotalPriceInfo) {
        this.opsTransferTotalPriceInfo = opsTransferTotalPriceInfo;
    }


}
