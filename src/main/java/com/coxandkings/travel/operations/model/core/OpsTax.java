package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsTax {

    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("taxCode")
    private String taxCode;
    @JsonProperty("currencyCode")
    private String currencyCode;

    private final static long serialVersionUID = -105007122859364851L;

    public OpsTax() {
    }

    public OpsTax(Double amount, String taxCode, String currencyCode) {
        this.amount = amount;
        this.taxCode = taxCode;
        this.currencyCode = currencyCode;
    }

//    public OpsTaxBreakup(TaxBreakup taxBreakup) {
//        this.amount = taxBreakup.getAmount();
//        this.taxCode = taxBreakup.getTaxCode();
//        this.currencyCode = taxBreakup.getCurrencyCode();
//    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsTax opsTax = (OpsTax) o;
        return Objects.equals(amount, opsTax.amount) &&
                Objects.equals(taxCode, opsTax.taxCode) &&
                Objects.equals(currencyCode, opsTax.currencyCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(amount, taxCode, currencyCode);
    }
}
