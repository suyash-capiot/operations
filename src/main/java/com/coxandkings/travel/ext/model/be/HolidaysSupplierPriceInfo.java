package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "taxes",
        "supplierPrice",
        "currencyCode"
})
public class HolidaysSupplierPriceInfo {

    @JsonProperty("supplierPriceAfterTax")
    private String supplierPriceAfterTax;

    @JsonProperty("supplierPriceBeforeTax")
    private String supplierPriceBeforeTax;

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("taxAmount")
    private String taxAmount;

    @JsonProperty("taxes")
    private Taxes taxes;

    @JsonProperty("paxTypeFares")
    private List<HolidaysPaxTypeFares> paxTypeFares;

    public String getSupplierPriceAfterTax() {
        return supplierPriceAfterTax;
    }

    public void setSupplierPriceAfterTax(String supplierPriceAfterTax) {
        this.supplierPriceAfterTax = supplierPriceAfterTax;
    }

    public String getSupplierPriceBeforeTax() {
        return supplierPriceBeforeTax;
    }

    public void setSupplierPriceBeforeTax(String supplierPriceBeforeTax) {
        this.supplierPriceBeforeTax = supplierPriceBeforeTax;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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

    public List<HolidaysPaxTypeFares> getPaxTypeFares() {
        return paxTypeFares;
    }

    public void setPaxTypeFares(List<HolidaysPaxTypeFares> paxTypeFares) {
        this.paxTypeFares = paxTypeFares;
    }


}
