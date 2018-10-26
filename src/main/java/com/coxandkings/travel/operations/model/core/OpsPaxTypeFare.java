package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class OpsPaxTypeFare implements Serializable {

    @JsonProperty("totalFare")
    private OpsTotalFare totalFare;

    @JsonProperty("paxType")
    private String paxType;

    @JsonProperty("taxes")
    private OpsTaxes taxes;

    @JsonProperty("baseFare")
    private OpsBaseFare baseFare;

    @JsonProperty("fees")
    private OpsFees fees;

//    @JsonProperty("offers")
//    private OpsOffers offers;

    private final static long serialVersionUID = -3552859557203203036L;

    public OpsPaxTypeFare() {
    }

    public OpsTotalFare getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(OpsTotalFare totalFare) {
        this.totalFare = totalFare;
    }

    public String getPaxType() {
        return paxType;
    }

    public void setPaxType(String paxType) {
        this.paxType = paxType;
    }

    public OpsTaxes getTaxes() {
        return taxes;
    }

    public void setTaxes(OpsTaxes taxes) {
        this.taxes = taxes;
    }

    public OpsBaseFare getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(OpsBaseFare baseFare) {
        this.baseFare = baseFare;
    }

    public OpsFees getFees() {
        return fees;
    }

    public void setFees(OpsFees fees) {
        this.fees = fees;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}

