package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsTotalTaxBreakUp implements Serializable
{

    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("taxCode")
    private String taxCode;
    @JsonProperty("currencyCode")
    private String currencyCode;
    private final static long serialVersionUID = -8642378280072122475L;

    public OpsTotalTaxBreakUp() {
    }

    public OpsTotalTaxBreakUp(Integer amount, String taxCode, String currencyCode) {
        this.amount = amount;
        this.taxCode = taxCode;
        this.currencyCode = currencyCode;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
