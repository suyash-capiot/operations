package com.coxandkings.travel.operations.resource.manageofflinebooking;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "commercialCurrency",
    "commercialType",
    "commercialAmount",
    "commercialName",
    "mdmRuleID",
    "supplierID",
    "commercialCalculationAmount",
    "commercialFareComponent",
    "commercialCalculationPercentage"
})
public class SupplierCommercial {

    @JsonProperty("commercialCurrency")
    private String commercialCurrency;
    @JsonProperty("commercialType")
    private String commercialType;
    @JsonProperty("commercialAmount")
    private Double commercialAmount;
    @JsonProperty("commercialName")
    private String commercialName;
    @JsonProperty("mdmRuleID")
    private Object mdmRuleID;
    @JsonProperty("supplierID")
    private Object supplierID;
    @JsonProperty("commercialCalculationAmount")
    private Object commercialCalculationAmount;
    @JsonProperty("commercialFareComponent")
    private Object commercialFareComponent;
    @JsonProperty("commercialCalculationPercentage")
    private Object commercialCalculationPercentage;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    @JsonProperty("mdmRuleID")
    public Object getMdmRuleID() {
        return mdmRuleID;
    }

    @JsonProperty("mdmRuleID")
    public void setMdmRuleID(Object mdmRuleID) {
        this.mdmRuleID = mdmRuleID;
    }

    @JsonProperty("supplierID")
    public Object getSupplierID() {
        return supplierID;
    }

    @JsonProperty("supplierID")
    public void setSupplierID(Object supplierID) {
        this.supplierID = supplierID;
    }

    @JsonProperty("commercialCalculationAmount")
    public Object getCommercialCalculationAmount() {
        return commercialCalculationAmount;
    }

    @JsonProperty("commercialCalculationAmount")
    public void setCommercialCalculationAmount(Object commercialCalculationAmount) {
        this.commercialCalculationAmount = commercialCalculationAmount;
    }

    @JsonProperty("commercialFareComponent")
    public Object getCommercialFareComponent() {
        return commercialFareComponent;
    }

    @JsonProperty("commercialFareComponent")
    public void setCommercialFareComponent(Object commercialFareComponent) {
        this.commercialFareComponent = commercialFareComponent;
    }

    @JsonProperty("commercialCalculationPercentage")
    public Object getCommercialCalculationPercentage() {
        return commercialCalculationPercentage;
    }

    @JsonProperty("commercialCalculationPercentage")
    public void setCommercialCalculationPercentage(Object commercialCalculationPercentage) {
        this.commercialCalculationPercentage = commercialCalculationPercentage;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
