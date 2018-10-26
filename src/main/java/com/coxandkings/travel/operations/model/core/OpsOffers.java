package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpsOffers {

    @JsonProperty("calculationType")
    private String calculationType;

    @JsonProperty("longDescription")
    private String longDescription;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("offerName")
    private String offerName;

    @JsonProperty("offerApplicability")
    private String offerApplicability;

    @JsonProperty("shortDescription")
    private String shortDescription;

    @JsonProperty("offerValue")
    private String offerValue;

    @JsonProperty("offerType")
    private String offerType;

    @JsonProperty("offerID")
    private String offerID;

    @JsonProperty("offerSubType")
    private String offerSubType;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("percentage")
    private BigDecimal percentage;

    @JsonProperty("offerAmount")
    private String offerAmount;

    public OpsOffers(){

    }

    public String getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(String calculationType) {
        this.calculationType = calculationType;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferApplicability() {
        return offerApplicability;
    }

    public void setOfferApplicability(String offerApplicability) {
        this.offerApplicability = offerApplicability;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getOfferValue() {
        return offerValue;
    }

    public void setOfferValue(String offerValue) {
        this.offerValue = offerValue;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getOfferID() {
        return offerID;
    }

    public void setOfferID(String offerID) {
        this.offerID = offerID;
    }

    public String getOfferSubType() {
        return offerSubType;
    }

    public void setOfferSubType(String offerSubType) {
        this.offerSubType = offerSubType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public String getOfferAmount() {
        return offerAmount;
    }

    public void setOfferAmount(String offerAmount) {
        this.offerAmount = offerAmount;
    }
}
