package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsServiceFees implements Serializable {

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("code")
    private String code;

    @JsonProperty("taxes")
    private OpsTaxes taxes;

    @JsonProperty("type")
    private String type;

    @JsonProperty("currencyCode")
    private String currencyCode;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public OpsTaxes getTaxes() {
        return taxes;
    }

    public void setTaxes(OpsTaxes taxes) {
        this.taxes = taxes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public OpsServiceFees(String amount, String code, OpsTaxes taxes, String type, String currencyCode) {
        this.amount = amount;
        this.code = code;
        this.taxes = taxes;
        this.type = type;
        this.currencyCode = currencyCode;
    }

    public OpsServiceFees() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsServiceFees that = (OpsServiceFees) o;
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
}
