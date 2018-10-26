package com.coxandkings.travel.operations.helper.booking.product.forex;

import java.math.BigDecimal;

public class Buying {
    private Integer id;
    private BigDecimal buyingAmount;//currecny usd 23
    private Float rateOfExchange;
    private BigDecimal iNREquivalent;// currency INR
    private BigDecimal commission;
    private String currency;
    private DisbursementDetails disbursementDetails;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getBuyingAmount() {
        return buyingAmount;
    }

    public void setBuyingAmount(BigDecimal buyingAmount) {
        this.buyingAmount = buyingAmount;
    }

    public Float getRateOfExchange() {
        return rateOfExchange;
    }

    public void setRateOfExchange(Float rateOfExchange) {
        this.rateOfExchange = rateOfExchange;
    }

    public BigDecimal getiNREquivalent() {
        return iNREquivalent;
    }

    public void setiNREquivalent(BigDecimal iNREquivalent) {
        this.iNREquivalent = iNREquivalent;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public DisbursementDetails getDisbursementDetails() {
        return disbursementDetails;
    }

    public void setDisbursementDetails(DisbursementDetails disbursementDetails) {
        this.disbursementDetails = disbursementDetails;
    }
}
