
package com.coxandkings.travel.operations.resource.qcmanagement.client;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "commercialAmount",
        "commercialCalculationPercentage",
        "commercialCalculationAmount",
        "commercialFareComponent",
        "commercialCurrency",
        "totalFare",
        "fareBreakUp",
        "commercialName",
        "mdmruleId"
})
public class MarkUpCommercialDetails {

    @JsonProperty("commercialAmount")
    private Integer commercialAmount;
    @JsonProperty("commercialCalculationPercentage")
    private Integer commercialCalculationPercentage;
    @JsonProperty("commercialCalculationAmount")
    private Integer commercialCalculationAmount;
    @JsonProperty("commercialFareComponent")
    private Object commercialFareComponent;
    @JsonProperty("commercialCurrency")
    private String commercialCurrency;
    @JsonProperty("totalFare")
    private Integer totalFare;
    @JsonProperty("commercialName")
    private String commercialName;
    @JsonProperty("mdmruleId")
    private String mdmruleId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("commercialAmount")
    public Integer getCommercialAmount() {
        return commercialAmount;
    }

    @JsonProperty("commercialAmount")
    public void setCommercialAmount(Integer commercialAmount) {
        this.commercialAmount = commercialAmount;
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

    @JsonProperty("commercialFareComponent")
    public Object getCommercialFareComponent() {
        return commercialFareComponent;
    }

    @JsonProperty("commercialFareComponent")
    public void setCommercialFareComponent(Object commercialFareComponent) {
        this.commercialFareComponent = commercialFareComponent;
    }

    @JsonProperty("commercialCurrency")
    public String getCommercialCurrency() {
        return commercialCurrency;
    }

    @JsonProperty("commercialCurrency")
    public void setCommercialCurrency(String commercialCurrency) {
        this.commercialCurrency = commercialCurrency;
    }

    @JsonProperty("totalFare")
    public Integer getTotalFare() {
        return totalFare;
    }

    @JsonProperty("totalFare")
    public void setTotalFare(Integer totalFare) {
        this.totalFare = totalFare;
    }

    @JsonProperty("commercialName")
    public String getCommercialName() {
        return commercialName;
    }

    @JsonProperty("commercialName")
    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    @JsonProperty("mdmruleId")
    public String getMdmruleId() {
        return mdmruleId;
    }

    @JsonProperty("mdmruleId")
    public void setMdmruleId(String mdmruleId) {
        this.mdmruleId = mdmruleId;
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
