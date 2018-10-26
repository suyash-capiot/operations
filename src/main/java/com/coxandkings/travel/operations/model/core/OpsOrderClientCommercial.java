package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsOrderClientCommercial implements Serializable {

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
    private boolean isEligible;

    private final static long serialVersionUID = -6073622402921393398L;

    public OpsOrderClientCommercial() {
    }

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

    public String getClientCommId() {
        return clientCommId;
    }

    public void setClientCommId(String clientCommId) {
        this.clientCommId = clientCommId;
    }
    @JsonProperty("isEligible")
	public boolean isEligible() {
		return isEligible;
	}
    @JsonProperty("isEligible")
	public void setEligible(boolean isEligible) {
		this.isEligible = isEligible;
	}
}
