package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsMarkUpCommercialDetails implements Serializable{

    @JsonProperty("totalFare")
    private Double totalFare;
    @JsonProperty("commercialCalculationAmount")
    private Integer commercialCalculationAmount;
    @JsonProperty("commercialFareComponent")
    private String commercialFareComponent;
    @JsonProperty("commercialCalculationPercentage")
    private Integer commercialCalculationPercentage;
    @JsonProperty("commercialCurrency")
    private Object commercialCurrency;
    @JsonProperty("commercialAmount")
    private Double commercialAmount;
    @JsonProperty("fareBreakUp")
    private OpsFareBreakUp fareBreakUp;
    @JsonProperty("commercialName")
    private String commercialName;
    private final static long serialVersionUID = -6389961395323513254L;
    public OpsMarkUpCommercialDetails() {
    }

    @JsonProperty("totalFare")
    public Double getTotalFare() {
        return totalFare;
    }

    @JsonProperty("totalFare")
    public void setTotalFare(Double totalFare) {
        this.totalFare = totalFare;
    }

    @JsonProperty("commercialCalculationAmount")
    public Integer getCommercialCalculationAmount() {
        return commercialCalculationAmount;
    }

    @JsonProperty("commercialCalculationAmount")
    public void setCommercialCalculationAmount(Integer commercialCalculationAmount) {
        this.commercialCalculationAmount = commercialCalculationAmount;
    }

    @JsonProperty("commercialFareComponent")
    public String getCommercialFareComponent() {
        return commercialFareComponent;
    }

    @JsonProperty("commercialFareComponent")
    public void setCommercialFareComponent(String commercialFareComponent) {
        this.commercialFareComponent = commercialFareComponent;
    }

    @JsonProperty("commercialCalculationPercentage")
    public Integer getCommercialCalculationPercentage() {
        return commercialCalculationPercentage;
    }

    @JsonProperty("commercialCalculationPercentage")
    public void setCommercialCalculationPercentage(Integer commercialCalculationPercentage) {
        this.commercialCalculationPercentage = commercialCalculationPercentage;
    }

    @JsonProperty("commercialCurrency")
    public Object getCommercialCurrency() {
        return commercialCurrency;
    }

    @JsonProperty("commercialCurrency")
    public void setCommercialCurrency(Object commercialCurrency) {
        this.commercialCurrency = commercialCurrency;
    }

    @JsonProperty("commercialAmount")
    public Double getCommercialAmount() {
        return commercialAmount;
    }

    @JsonProperty("commercialAmount")
    public void setCommercialAmount(Double commercialAmount) {
        this.commercialAmount = commercialAmount;
    }

    @JsonProperty("fareBreakUp")
    public OpsFareBreakUp getFareBreakUp() {
        return fareBreakUp;
    }

    @JsonProperty("fareBreakUp")
    public void setFareBreakUp(OpsFareBreakUp fareBreakUp) {
        this.fareBreakUp = fareBreakUp;
    }

    @JsonProperty("commercialName")
    public String getCommercialName() {
        return commercialName;
    }

    @JsonProperty("commercialName")
    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }
}
