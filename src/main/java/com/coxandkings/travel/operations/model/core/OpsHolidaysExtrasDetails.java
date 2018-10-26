package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsHolidaysExtrasDetails {
    @JsonProperty("extraType")
    private String extraType;
    @JsonProperty("code")
    private String code;
    @JsonProperty("quantity")
    private String quantity;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
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

    public OpsHolidaysExtrasDetails() {
    }

    public String getExtraType() {
        return extraType;
    }

    public void setExtraType(String extraType) {
        this.extraType = extraType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
