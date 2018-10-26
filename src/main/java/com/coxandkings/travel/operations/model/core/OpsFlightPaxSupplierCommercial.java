package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class OpsFlightPaxSupplierCommercial implements Serializable {

    @JsonProperty("commercialCurrency")
    private String commercialCurrency;
    @JsonProperty("commercialType")
    private String commercialType;
    @JsonProperty("commercialAmount")
    private Double commercialAmount;
    @JsonProperty("commercialName")
    private String commercialName;
    @JsonProperty("mdmRuleID")
    private String mdmRuleID;
    @JsonProperty("supplierID")
    private String supplierID;

    @JsonProperty("commercialCalculationAmount")
    private Double commercialCalculationAmount;

    @JsonProperty("commercialFareComponent")
    private String commercialFareComponent;

    @JsonProperty("commercialCalculationPercentage")
    private Double commercialCalculationPercentage;

    public String getMdmRuleID() {
        return mdmRuleID;
    }

    public void setMdmRuleID(String mdmRuleID) {
        this.mdmRuleID = mdmRuleID;
    }

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
    public Double getCommercialAmount() {
        return commercialAmount;
    }

    @JsonProperty("commercialAmount")
    public void setCommercialAmount(Double commercialAmount) {
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

	public Double getCommercialCalculationAmount() {
		return commercialCalculationAmount;
	}

	public void setCommercialCalculationAmount(Double commercialCalculationAmount) {
		this.commercialCalculationAmount = commercialCalculationAmount;
	}

	public String getCommercialFareComponent() {
		return commercialFareComponent;
	}

	public void setCommercialFareComponent(String commercialFareComponent) {
		this.commercialFareComponent = commercialFareComponent;
	}

	public Double getCommercialCalculationPercentage() {
		return commercialCalculationPercentage;
	}

	public void setCommercialCalculationPercentage(Double commercialCalculationPercentage) {
		this.commercialCalculationPercentage = commercialCalculationPercentage;
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
