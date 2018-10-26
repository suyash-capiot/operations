package com.coxandkings.travel.operations.resource.pricedetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ROE {
    @JsonProperty("ROE")
    private BigDecimal roe;
    @JsonProperty("toCurrency")
    private String toCurrency;
    @JsonProperty("fromCurrency")
    private String fromCurrency;
    @JsonProperty("_id")
    private String id;

    public BigDecimal getRoe() {
        return roe;
    }

    public void setRoe(BigDecimal roe) {
        this.roe = roe;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
