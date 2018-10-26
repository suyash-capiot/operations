package com.coxandkings.travel.operations.resource.forex;

import java.math.BigDecimal;

public class PersonalExpenseResource {

    private String id;

    private Float rateOfExchange;
    private BigDecimal inrEquivalent;// currency INR
    private BigDecimal commission;
    private String currency;
    private String delivery;
    private String billTo;

    private BigDecimal travellersChequeAmount;
    private BigDecimal cashAmount;
    private BigDecimal cardAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public BigDecimal getTravellersChequeAmount() {
        return travellersChequeAmount;
    }

    public void setTravellersChequeAmount(BigDecimal travellersChequeAmount) {
        this.travellersChequeAmount = travellersChequeAmount;
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
    }

    public BigDecimal getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(BigDecimal cardAmount) {
        this.cardAmount = cardAmount;
    }
}
