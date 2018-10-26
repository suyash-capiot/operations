package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class ServiceFees  implements Serializable
{
    @JsonProperty("amount")
    private String amount;

    @JsonProperty("code")
    private String code;

    @JsonProperty("taxes")
    private Taxes taxes;

    @JsonProperty("type")
    private String type;

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("taxes")
    public Taxes getTaxes() {
        return taxes;
    }

    @JsonProperty("taxes")
    public void setTaxes(Taxes taxes) {
        this.taxes = taxes;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceFees that = (ServiceFees) o;
        return Objects.equals(amount, that.amount) &&
                Objects.equals(code, that.code) &&
                Objects.equals(taxes, that.taxes) &&
                Objects.equals(type, that.type) &&
                Objects.equals(currencyCode, that.currencyCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(amount, code, taxes, type, currencyCode);
    }

    public ServiceFees(String amount, String code, Taxes taxes, String type, String currencyCode) {
        this.amount = amount;
        this.code = code;
        this.taxes = taxes;
        this.type = type;
        this.currencyCode = currencyCode;
    }

    public ServiceFees() {
    }
}
