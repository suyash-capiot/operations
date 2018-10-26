
package com.coxandkings.travel.operations.resource.qcmanagement.supplier;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "commercialName",
    "commercialInitialAmount",
    "commercialCalculationPercentage",
    "commercialCalculationAmount",
    "commercialAmount",
    "commercialTotalAmount",
    "commercialFareComponent",
    "fareBreakUp",
    "mdmruleId"
})
public class BrmsSupplierCommercialDetail {

    @JsonProperty("commercialName")
    private String commercialName;
    @JsonProperty("commercialInitialAmount")
    private Integer commercialInitialAmount;
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
    @JsonProperty("mdmruleId")
    private String mdmruleId;
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

    @JsonProperty("commercialInitialAmount")
    public Integer getCommercialInitialAmount() {
        return commercialInitialAmount;
    }

    @JsonProperty("commercialInitialAmount")
    public void setCommercialInitialAmount(Integer commercialInitialAmount) {
        this.commercialInitialAmount = commercialInitialAmount;
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
