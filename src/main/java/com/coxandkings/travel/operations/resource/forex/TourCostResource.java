package com.coxandkings.travel.operations.resource.forex;

import com.coxandkings.travel.operations.model.forex.ForexPassenger;

import java.math.BigDecimal;

public class TourCostResource {

    private String id;
    private BigDecimal buyingAmount;
    private Float rateOfExchange;
    private BigDecimal inrEquivalent;// currency INR
    private BigDecimal commission;
    private String currency;
    private String billTo;

    private ForexPassenger forexPassenger;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public BigDecimal getInrEquivalent() {
        return inrEquivalent;
    }

    public void setInrEquivalent(BigDecimal inrEquivalent) {
        this.inrEquivalent = inrEquivalent;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public ForexPassenger getForexPassenger() {
        return forexPassenger;
    }

    public void setForexPassenger(ForexPassenger forexPassenger) {
        this.forexPassenger = forexPassenger;
    }
}
