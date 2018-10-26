package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpsOfferDetailSet {

    @JsonProperty("longDescription")
    private String longDescription;

    @JsonProperty("offerType")
    private String offerType;

    @JsonProperty("offerName")
    private String offerName;

    @JsonProperty("redemptionConstruct")
    private OpsRedemptionConstruct opsRedemptionConstruct;

    @JsonProperty("offerAmount")
    private BigDecimal offerAmount;

    @JsonProperty("offerApplicability")
    private String offerApplicability;

    @JsonProperty("offerID")
    private String offerID;

    @JsonProperty("offerSubType")
    private String offerSubType;

    @JsonProperty("shortDescription")
    private String shortDescription;

    public OpsOfferDetailSet(){

    }

    public OpsRedemptionConstruct getOpsRedemptionConstruct() {
        return opsRedemptionConstruct;
    }

    public void setOpsRedemptionConstruct(OpsRedemptionConstruct opsRedemptionConstruct) {
        this.opsRedemptionConstruct = opsRedemptionConstruct;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }



    public BigDecimal getOfferAmount() {
        return offerAmount;
    }

    public void setOfferAmount(BigDecimal offerAmount) {
        this.offerAmount = offerAmount;
    }

    public String getOfferApplicability() {
        return offerApplicability;
    }

    public void setOfferApplicability(String offerApplicability) {
        this.offerApplicability = offerApplicability;
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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
}
