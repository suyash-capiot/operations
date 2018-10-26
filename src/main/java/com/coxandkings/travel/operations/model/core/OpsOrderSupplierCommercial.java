package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsOrderSupplierCommercial implements Serializable {

    @JsonProperty("commercialCurrency")
    private String commercialCurrency;

    @JsonProperty("commercialType")
    private String commercialType;

    @JsonProperty("commercialAmount")
    private String commercialAmount;

    @JsonProperty("commercialName")
    private String commercialName;

    @JsonProperty("supplierID")
    private String supplierID;

    @JsonProperty("suppCommId")
    private String suppCommId;
    
    @JsonProperty("isEligible")
    private boolean isEligible;

    @JsonProperty("commercialCurrency")
    public String getCommercialCurrency() {
        return commercialCurrency;
    }

    @JsonProperty("commercialCurrency")
    public void setCommercialCurrency(String commercialCurrency) {
        this.commercialCurrency = commercialCurrency;
    }

    @JsonProperty("commercialType")
    public String getCommercialType() {
        return commercialType;
    }

    @JsonProperty("commercialType")
    public void setCommercialType(String commercialType) {
        this.commercialType = commercialType;
    }

    @JsonProperty("commercialAmount")
    public String getCommercialAmount() {
        return commercialAmount;
    }

    @JsonProperty("commercialAmount")
    public void setCommercialAmount(String commercialAmount) {
        this.commercialAmount = commercialAmount;
    }

    @JsonProperty("commercialName")
    public String getCommercialName() {
        return commercialName;
    }

    @JsonProperty("commercialName")
    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    public String getSuppCommId() {
        return suppCommId;
    }

    public void setSuppCommId(String suppCommId) {
        this.suppCommId = suppCommId;
    }

    @JsonProperty("isEligible")
	public boolean isEligible() {
		return isEligible;
	}

    @JsonProperty("isEligible")
	public void setEligible(boolean isEligible) {
		this.isEligible = isEligible;
	}

    @JsonProperty("supplierID")
    public String getSupplierID() {
        return supplierID;
    }

    @JsonProperty("supplierID")
    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }
}

