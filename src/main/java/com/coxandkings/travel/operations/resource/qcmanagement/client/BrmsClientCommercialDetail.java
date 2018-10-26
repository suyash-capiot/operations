
package com.coxandkings.travel.operations.resource.qcmanagement.client;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "commercialName",
        "commercialCalculationPercentage",
        "commercialCalculationAmount",
        "commercialAmount",
        "commercialTotalAmount",
        "commercialFareComponent",
        "fareBreakUp"
})
public class BrmsClientCommercialDetail {

    @JsonProperty("commercialName")
    private String commercialName;
    @JsonProperty("commercialCalculationPercentage")
    private Integer commercialCalculationPercentage;
    @JsonProperty("commercialCalculationAmount")
    private Integer commercialCalculationAmount;
    @JsonProperty("commercialAmount")
    private Double commercialAmount;
    @JsonProperty("commercialTotalAmount")
    private Double commercialTotalAmount;
    @JsonProperty("commercialFareComponent")
    private String commercialFareComponent;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("commercialName")
    public String getCommercialName() {
        return commercialName;
    }

    @JsonProperty("commercialName")
    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    @JsonProperty("commercialCalculationPercentage")
    public Integer getCommercialCalculationPercentage() {
        return commercialCalculationPercentage;
    }

    @JsonProperty("commercialCalculationPercentage")
    public void setCommercialCalculationPercentage(Integer commercialCalculationPercentage) {
        this.commercialCalculationPercentage = commercialCalculationPercentage;
    }

    @JsonProperty("commercialCalculationAmount")
    public Integer getCommercialCalculationAmount() {
        return commercialCalculationAmount;
    }

    @JsonProperty("commercialCalculationAmount")
    public void setCommercialCalculationAmount(Integer commercialCalculationAmount) {
        this.commercialCalculationAmount = commercialCalculationAmount;
    }

    @JsonProperty("commercialAmount")
    public Double getCommercialAmount() {
        return commercialAmount;
    }

    @JsonProperty("commercialAmount")
    public void setCommercialAmount(Double commercialAmount) {
        this.commercialAmount = commercialAmount;
    }

    @JsonProperty("commercialTotalAmount")
    public Double getCommercialTotalAmount() {
        return commercialTotalAmount;
    }

    @JsonProperty("commercialTotalAmount")
    public void setCommercialTotalAmount(Double commercialTotalAmount) {
        this.commercialTotalAmount = commercialTotalAmount;
    }

    @JsonProperty("commercialFareComponent")
    public String getCommercialFareComponent() {
        return commercialFareComponent;
    }

    @JsonProperty("commercialFareComponent")
    public void setCommercialFareComponent(String commercialFareComponent) {
        this.commercialFareComponent = commercialFareComponent;
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
