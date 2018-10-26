package com.coxandkings.travel.operations.model.accountsummary;

import java.math.BigDecimal;

public class PaymentAdvisePaymentDetails {

    private String typeOfSettlement;

    private String paymentDueDate;

    private BigDecimal amount;

    private String currency;

    private String paymentDate;

    private String paymentAdviseID;

    private String paymentRefNumber;

    private String status;

    public PaymentAdvisePaymentDetails()    {}

    public String getTypeOfSettlement() {
        return typeOfSettlement;
    }

    public void setTypeOfSettlement(String typeOfSettlement) {
        this.typeOfSettlement = typeOfSettlement;
    }

    public String getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(String paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentAdviseID() {
        return paymentAdviseID;
    }

    public void setPaymentAdviseID(String paymentAdviseID) {
        this.paymentAdviseID = paymentAdviseID;
    }

    public String getPaymentRefNumber() {
        return paymentRefNumber;
    }

    public void setPaymentRefNumber(String paymentRefNumber) {
        this.paymentRefNumber = paymentRefNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
