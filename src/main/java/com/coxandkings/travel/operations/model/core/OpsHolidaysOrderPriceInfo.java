package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsHolidaysOrderPriceInfo {
    @JsonProperty("taxes")
    private OpsTaxes taxes;
    @JsonProperty("amountAfterTax")
    private String amountAfterTax;
    @JsonProperty("amountBeforeTax")
    private String amountBeforeTax;
    @JsonProperty("currencyCode")
    private String currencyCode;

    public OpsHolidaysOrderPriceInfo() {
    }

    public OpsTaxes getTaxes() {
        return taxes;
    }

    public void setTaxes(OpsTaxes taxes) {
        this.taxes = taxes;
    }

    public String getAmountAfterTax() {
        return amountAfterTax;
    }

    public void setAmountAfterTax(String amountAfterTax) {
        this.amountAfterTax = amountAfterTax;
    }

    public String getAmountBeforeTax() {
        return amountBeforeTax;
    }

    public void setAmountBeforeTax(String amountBeforeTax) {
        this.amountBeforeTax = amountBeforeTax;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
