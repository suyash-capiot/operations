package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsHolidaysExtensionNightDetails {
    @JsonProperty("extensionType")
    private String extensionType;
    @JsonProperty("roomTypeInfo")
    private OpsRoomTypeInfo roomTypeInfo;
    @JsonProperty("ratePlanInfo")
    private OpsRatePlanInfo ratePlanInfo;
    @JsonProperty("credentialsName")
    private String credentialsName;
    @JsonProperty("supplierRateType")
    private String supplierRateType;
    @JsonProperty("refundable")
    private boolean refundable;
    @JsonProperty("paxInfo")
    private List<OpsAccommodationPaxInfo> opsPaxInfo;
    @JsonProperty("orderSupplierPriceInfo")
    private OpsHolidaysSupplierPriceInfo opsHolidaysSupplierPriceInfo;
    @JsonProperty("orderClientTotalPriceInfo")
    private OpsHolidaysTotalPriceInfo opsHolidaysTotalPriceInfo;

    public OpsHolidaysExtensionNightDetails() {
    }

    public String getExtensionType() {
        return extensionType;
    }

    public void setExtensionType(String extensionType) {
        this.extensionType = extensionType;
    }

    public OpsRoomTypeInfo getRoomTypeInfo() {
        return roomTypeInfo;
    }

    public void setRoomTypeInfo(OpsRoomTypeInfo roomTypeInfo) {
        this.roomTypeInfo = roomTypeInfo;
    }

    public OpsRatePlanInfo getRatePlanInfo() {
        return ratePlanInfo;
    }

    public void setRatePlanInfo(OpsRatePlanInfo ratePlanInfo) {
        this.ratePlanInfo = ratePlanInfo;
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

    public OpsHolidaysSupplierPriceInfo getOpsHolidaysSupplierPriceInfo() {
        return opsHolidaysSupplierPriceInfo;
    }

    public void setOpsHolidaysSupplierPriceInfo(OpsHolidaysSupplierPriceInfo opsHolidaysSupplierPriceInfo) {
        this.opsHolidaysSupplierPriceInfo = opsHolidaysSupplierPriceInfo;
    }

    public OpsHolidaysTotalPriceInfo getOpsHolidaysTotalPriceInfo() {
        return opsHolidaysTotalPriceInfo;
    }

    public void setOpsHolidaysTotalPriceInfo(OpsHolidaysTotalPriceInfo opsHolidaysTotalPriceInfo) {
        this.opsHolidaysTotalPriceInfo = opsHolidaysTotalPriceInfo;
    }
}
