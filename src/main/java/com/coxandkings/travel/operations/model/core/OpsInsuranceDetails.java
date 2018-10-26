package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsInsuranceDetails {

    //Holidays sub product transfer specific fields
    @JsonProperty("insuranceType")
    private String insuranceType;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("insuranceId")
    private String insuranceId;

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
    private OpsInsuranceSupplierPriceInfo opsInsuranceSupplierPriceInfo;

    @JsonProperty("orderClientTotalPriceInfo")
    private OpsInsuranceTotalPriceInfo opsInsuranceTotalPriceInfo;

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
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

    public String getinsuranceId() {
        return insuranceId;
    }

    public void setinsuranceId(String insuranceId) {
        insuranceId = insuranceId;
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

    public OpsInsuranceSupplierPriceInfo getOpsInsuranceSupplierPriceInfo() {
        return opsInsuranceSupplierPriceInfo;
    }

    public void setOpsInsuranceSupplierPriceInfo(
            OpsInsuranceSupplierPriceInfo opsInsuranceSupplierPriceInfo) {
        this.opsInsuranceSupplierPriceInfo = opsInsuranceSupplierPriceInfo;
    }

    public OpsInsuranceTotalPriceInfo getOpsInsuranceTotalPriceInfo() {
        return opsInsuranceTotalPriceInfo;
    }

    public void setOpsInsuranceTotalPriceInfo(OpsInsuranceTotalPriceInfo opsInsuranceTotalPriceInfo) {
        this.opsInsuranceTotalPriceInfo = opsInsuranceTotalPriceInfo;
    }


}
