package com.coxandkings.travel.operations.model.accountsummary;

public class InvoicePaymentInfo {

    private String paymentTowards;

    private String paymentDate;

    private String paymentCollection;

    private boolean isRefundable;

    private String receiptNumber;

    private String paymentStatus;

    private String paymentCurrency;

    public InvoicePaymentInfo() {
    }

    public String getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(String paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }

    public String getPaymentTowards() {
        return paymentTowards;
    }

    public void setPaymentTowards(String paymentTowards) {
        this.paymentTowards = paymentTowards;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentCollection() {
        return paymentCollection;
    }

    public void setPaymentCollection(String paymentCollection) {
        this.paymentCollection = paymentCollection;
    }

    public boolean isRefundable() {
        return isRefundable;
    }

    public void setRefundable(boolean refundable) {
        isRefundable = refundable;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
