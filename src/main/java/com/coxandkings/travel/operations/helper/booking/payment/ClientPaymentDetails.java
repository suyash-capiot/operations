package com.coxandkings.travel.operations.helper.booking.payment;


public class ClientPaymentDetails {

    private String clientID;
    private String notification;
    private String totalCollection;
    private String totalOutstandingAmount;
    private String currency;
    private String invoice;
    private String invoiceDate;
    private String invoiceAmount;
    private String currencyCode;
    private ClientPayment clientPayment;

    private String noteNo;
    private Refund refundDetails;

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getTotalCollection() {
        return totalCollection;
    }

    public void setTotalCollection(String totalCollection) {
        this.totalCollection = totalCollection;
    }

    public String getTotalOutstandingAmount() {
        return totalOutstandingAmount;
    }

    public void setTotalOutstandingAmount(String totalOutstandingAmount) {
        this.totalOutstandingAmount = totalOutstandingAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public ClientPayment getClientPayment() {
        return clientPayment;
    }

    public void setClientPayment(ClientPayment clientPayment) {
        this.clientPayment = clientPayment;
    }

    public String getNoteNo() {
        return noteNo;
    }

    public void setNoteNo(String noteNo) {
        this.noteNo = noteNo;
    }

    public Refund getRefundDetails() {
        return refundDetails;
    }

    public void setRefundDetails(Refund refundDetails) {
        this.refundDetails = refundDetails;
    }
}
