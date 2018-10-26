package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsAccoOrderSupplierPriceInfo {

    @JsonProperty("taxes")
    private OpsTaxes taxes;
    @JsonProperty("supplierPrice")
    private String supplierPrice;
    @JsonProperty("currencyCode")
    private String currencyCode;

    public OpsAccoOrderSupplierPriceInfo() {
    }

    public OpsTaxes getTaxes() {
        return taxes;
    }

    public void setTaxes(OpsTaxes taxes) {
        this.taxes = taxes;
    }

    public String getSupplierPrice() {
        return supplierPrice;
    }

    public void setSupplierPrice(String supplierPrice) {
        this.supplierPrice = supplierPrice;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
