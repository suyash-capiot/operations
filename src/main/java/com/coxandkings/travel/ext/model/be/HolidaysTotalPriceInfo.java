package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "totalPriceAfterTax",
        "totalPriceBeforeTax",
        "taxAmount",
        "taxes",
        "currencyCode",
        "paxTypeFares"
})
public class HolidaysTotalPriceInfo {

    @JsonProperty("totalPriceAfterTax")
    private String totalPriceAfterTax;

    @JsonProperty("totalPriceBeforeTax")
    private String totalPriceBeforeTax;

    @JsonProperty("taxAmount")
    private String taxAmount;

    @JsonProperty("taxes")
    private Taxes taxes;

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("receivables")
    private Receivables receivables;

    @JsonProperty("paxTypeFares")
    private List<HolidaysPaxTypeFares> paxTypeFares;

    public String getTotalPriceAfterTax() {
        return totalPriceAfterTax;
    }

    public void setTotalPriceAfterTax(String totalPriceAfterTax) {
        this.totalPriceAfterTax = totalPriceAfterTax;
    }

    public String getTotalPriceBeforeTax() {
        return totalPriceBeforeTax;
    }

    public void setTotalPriceBeforeTax(String totalPriceBeforeTax) {
        this.totalPriceBeforeTax = totalPriceBeforeTax;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Taxes getTaxes() {
        return taxes;
    }

    public void setTaxes(Taxes taxes) {
        this.taxes = taxes;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public List<HolidaysPaxTypeFares> getPaxTypeFares() {
        return paxTypeFares;
    }

    public void setPaxTypeFares(List<HolidaysPaxTypeFares> paxTypeFares) {
        this.paxTypeFares = paxTypeFares;
    }

    public Receivables getReceivables() {
        return receivables;
    }

    public void setReceivables(Receivables receivables) {
        this.receivables = receivables;
    }



  
}
