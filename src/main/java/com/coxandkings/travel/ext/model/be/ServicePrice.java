package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServicePrice implements Serializable {

    @JsonProperty("basePrice")
    private String basePrice;

    @JsonProperty("baseFare")
    private String baseFare;

    @JsonProperty("taxes")
    private String taxes;

    @JsonProperty("currencyCode")
    private String currencyCode;


    public ServicePrice() {
    }

    public ServicePrice(String basePrice, String baseFare, String taxes, String currencyCode) {
        this.basePrice = basePrice;
        this.baseFare = baseFare;
        this.taxes = taxes;
        this.currencyCode = currencyCode;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }

    public String getTaxes() {
        return taxes;
    }

    public void setTaxes(String taxes) {
        this.taxes = taxes;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
