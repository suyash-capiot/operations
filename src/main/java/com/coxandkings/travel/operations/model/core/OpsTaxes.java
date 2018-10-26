package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsTaxes {

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("amount")
    private Double amount;

//    @JsonProperty("totalTaxBreakUp")
//    private List<OpsTotalTaxBreakUp> totalTaxBreakUp = new ArrayList<OpsTotalTaxBreakUp>();

    @JsonProperty("tax")
    private List<OpsTax> tax = new ArrayList<OpsTax>();

//    @JsonProperty("TotalAmount")
//    private Integer totalAmount;

//    @JsonProperty("taxBreakup")
//    private List<OpsTaxBreakup> taxBreakup = new ArrayList<OpsTaxBreakup>();

//    @JsonProperty("Total")
//    private Integer total;

    private final static long serialVersionUID = -6455399713605965090L;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<OpsTax> getTax() {
        return tax;
    }

    public void setTax(List<OpsTax> tax) {
        this.tax = tax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsTaxes opsTaxes = (OpsTaxes) o;
        return Objects.equals(currencyCode, opsTaxes.currencyCode) &&
                Objects.equals(amount, opsTaxes.amount) &&
                Objects.equals(tax, opsTaxes.tax);
    }

    @Override
    public int hashCode() {

        return Objects.hash(currencyCode, amount, tax);
    }
}
