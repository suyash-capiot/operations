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
    "clientID",
    "parentClientID",
    "commercialEntityType",
    "companyFlag",
    "commercialCurrency",
    "commercialType",
    "commercialEntityID",
    "commercialAmount",
    "commercialName",
    "clientCommId",
    "isEligible"
})
public class OrderClientCommercial {

    @JsonProperty("clientID")
    private String clientID;
    @JsonProperty("parentClientID")
    private String parentClientID;
    @JsonProperty("commercialEntityType")
    private String commercialEntityType;
    @JsonProperty("companyFlag")
    private Boolean companyFlag;
    @JsonProperty("commercialCurrency")
    private String commercialCurrency;
    @JsonProperty("commercialType")
    private String commercialType;
    @JsonProperty("commercialEntityID")
    private String commercialEntityID;
    @JsonProperty("commercialAmount")
    private String commercialAmount;
    @JsonProperty("commercialName")
    private String commercialName;
    @JsonProperty("clientCommId")
    private String clientCommId;
    @JsonProperty("isEligible")
    private Boolean isEligible;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("clientID")
    public String getClientID() {
        return clientID;
    }

    @JsonProperty("clientID")
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    @JsonProperty("parentClientID")
    public String getParentClientID() {
        return parentClientID;
    }

    @JsonProperty("parentClientID")
    public void setParentClientID(String parentClientID) {
        this.parentClientID = parentClientID;
    }

    @JsonProperty("commercialEntityType")
    public String getCommercialEntityType() {
        return commercialEntityType;
    }

    @JsonProperty("commercialEntityType")
    public void setCommercialEntityType(String commercialEntityType) {
        this.commercialEntityType = commercialEntityType;
    }

    @JsonProperty("companyFlag")
    public Boolean getCompanyFlag() {
        return companyFlag;
    }

    @JsonProperty("companyFlag")
    public void setCompanyFlag(Boolean companyFlag) {
        this.companyFlag = companyFlag;
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

    @JsonProperty("commercialEntityID")
    public String getCommercialEntityID() {
        return commercialEntityID;
    }

    @JsonProperty("commercialEntityID")
    public void setCommercialEntityID(String commercialEntityID) {
        this.commercialEntityID = commercialEntityID;
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

    @JsonProperty("clientCommId")
    public String getClientCommId() {
        return clientCommId;
    }

    @JsonProperty("clientCommId")
    public void setClientCommId(String clientCommId) {
        this.clientCommId = clientCommId;
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