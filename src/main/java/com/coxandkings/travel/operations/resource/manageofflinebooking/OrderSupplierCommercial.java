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
    "supplierID",
    "suppCommId",
    "isEligible"
})
public class OrderSupplierCommercial {

    @JsonProperty("commercialCurrency")
    private String commercialCurrency;
    @JsonProperty("commercialType")
    private String commercialType;
    @JsonProperty("commercialAmount")
    private String commercialAmount;
    @JsonProperty("commercialName")
    private String commercialName;
    @JsonProperty("supplierID")
    private Object supplierID;
    @JsonProperty("suppCommId")
    private String suppCommId;
    @JsonProperty("isEligible")
    private Boolean isEligible;
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

    @JsonProperty("supplierID")
    public Object getSupplierID() {
        return supplierID;
    }

    @JsonProperty("supplierID")
    public void setSupplierID(Object supplierID) {
        this.supplierID = supplierID;
    }

    @JsonProperty("suppCommId")
    public String getSuppCommId() {
        return suppCommId;
    }

    @JsonProperty("suppCommId")
    public void setSuppCommId(String suppCommId) {
        this.suppCommId = suppCommId;
    }

    @JsonProperty("isEligible")
    public Boolean getIsEligible() {
        return isEligible;
    }

    @JsonProperty("isEligible")
    public void setIsEligible(Boolean isEligible) {
        this.isEligible = isEligible;
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
