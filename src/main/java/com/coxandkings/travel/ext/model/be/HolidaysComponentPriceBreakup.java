package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "amountAfterTax",
        "taxes",
        "taxAmount",
        "currencyCode"
})
public class HolidaysComponentPriceBreakup {


    @JsonProperty("amountAfterTax")
    private String amountAfterTax;

    @JsonProperty("taxes")
    private Taxes taxes;

    @JsonProperty("taxAmount")
    private String taxAmount;

    @JsonProperty("currencyCode")
    private String currencyCode;

    public String getAmountAfterTax() {
        return amountAfterTax;
    }

    public void setAmountAfterTax(String amountAfterTax) {
        this.amountAfterTax = amountAfterTax;
    }

    public Taxes getTaxes() {
        return taxes;
    }

    public void setTaxes(Taxes taxes) {
        this.taxes = taxes;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }


}
