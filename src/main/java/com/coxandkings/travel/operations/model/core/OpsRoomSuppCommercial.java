package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsRoomSuppCommercial implements Serializable {

    @JsonProperty("commercialCalculationAmount")
    private Double commercialCalculationAmount;

    @JsonProperty("commercialFareComponent")
    private String commercialFareComponent;

    @JsonProperty("commercialCalculationPercentage")
    private Double commercialCalculationPercentage;

    @JsonProperty("commercialInitialAmount")
    private Double commercialInitialAmount;

    @JsonProperty("commercialTotalAmount")
    private Double commercialTotalAmount;

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

    public OpsRoomSuppCommercial() {
    }

    public String getMdmRuleID() {
        return mdmRuleID;
    }

    public void setMdmRuleID(String mdmRuleID) {
        this.mdmRuleID = mdmRuleID;
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

    public String getCommercialCurrency() {
        return commercialCurrency;
    }

    public void setCommercialCurrency(String commercialCurrency) {
        this.commercialCurrency = commercialCurrency;
    }

    public Double getCommercialInitialAmount() {
        return commercialInitialAmount;
    }

    public void setCommercialInitialAmount(Double commercialInitialAmount) {
        this.commercialInitialAmount = commercialInitialAmount;
    }

    public Double getCommercialTotalAmount() {
        return commercialTotalAmount;
    }

    public void setCommercialTotalAmount(Double commercialTotalAmount) {
        this.commercialTotalAmount = commercialTotalAmount;
    }

    public String getCommercialType() {
        return commercialType;
    }

    public void setCommercialType(String commercialType) {
        this.commercialType = commercialType;
    }

    public Double getCommercialAmount() {
        return commercialAmount;
    }

    public void setCommercialAmount(Double commercialAmount) {
        this.commercialAmount = commercialAmount;
    }

    public String getCommercialName() {
        return commercialName;
    }

    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsRoomSuppCommercial that = (OpsRoomSuppCommercial) o;
        return Objects.equals(commercialCalculationAmount, that.commercialCalculationAmount) &&
                Objects.equals(commercialFareComponent, that.commercialFareComponent) &&
                Objects.equals(commercialCalculationPercentage, that.commercialCalculationPercentage) &&
                Objects.equals(commercialInitialAmount, that.commercialInitialAmount) &&
                Objects.equals(commercialTotalAmount, that.commercialTotalAmount) &&
                Objects.equals(commercialCurrency, that.commercialCurrency) &&
                Objects.equals(commercialType, that.commercialType) &&
                Objects.equals(commercialAmount, that.commercialAmount) &&
                Objects.equals(commercialName, that.commercialName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(commercialCalculationAmount, commercialFareComponent, commercialCalculationPercentage, commercialInitialAmount, commercialTotalAmount, commercialCurrency, commercialType, commercialAmount, commercialName);
    }

	public String getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}
}
