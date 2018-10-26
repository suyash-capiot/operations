package com.coxandkings.travel.operations.helper.booking.payment;

public class ClientPayment {

    private String paymentStatus;
    private String  paymentTowards;
    private String paymentDate;
    private String paymentCollection;
    private String receiptNo;
    private String currencyCode;
    private String refundable;
    private String modeOfTransfer;


    public String getRefundable() {
        return refundable;
    }

    public void setRefundable(String refundable) {
        this.refundable = refundable;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getModeOfTransfer() {
        return modeOfTransfer;
    }

    public void setModeOfTransfer(String modeOfTransfer) {
        this.modeOfTransfer = modeOfTransfer;
    }
}
