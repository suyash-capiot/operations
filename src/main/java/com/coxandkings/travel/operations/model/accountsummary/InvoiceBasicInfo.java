package com.coxandkings.travel.operations.model.accountsummary;

import java.util.List;

public class InvoiceBasicInfo {

    private String invoiceID;

    private String invoiceDate;

    private String invoiceAmount;

    private String invoiceCurrency;

    private String outStandingAmount;

    private List<InvoicePaymentInfo> invoicePayments;

    private List<InvoiceCreditDebitNoteInfo> invoiceCreditDebitNotes;

    private List<InvoiceRefundInfo> invoiceRefunds;

    public InvoiceBasicInfo()   {
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
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

    public String getInvoiceCurrency() {
        return invoiceCurrency;
    }

    public void setInvoiceCurrency(String invoiceCurrency) {
        this.invoiceCurrency = invoiceCurrency;
    }

    public List<InvoicePaymentInfo> getInvoicePayments() {
        return invoicePayments;
    }

    public void setInvoicePayments(List<InvoicePaymentInfo> invoicePayments) {
        this.invoicePayments = invoicePayments;
    }

    public List<InvoiceCreditDebitNoteInfo> getInvoiceCreditDebitNotes() {
        return invoiceCreditDebitNotes;
    }

    public void setInvoiceCreditDebitNotes(List<InvoiceCreditDebitNoteInfo> invoiceCreditDebitNotes) {
        this.invoiceCreditDebitNotes = invoiceCreditDebitNotes;
    }

    public List<InvoiceRefundInfo> getInvoiceRefunds() {
        return invoiceRefunds;
    }

    public void setInvoiceRefunds(List<InvoiceRefundInfo> invoiceRefunds) {
        this.invoiceRefunds = invoiceRefunds;
    }

    public String getOutStandingAmount() {
        return outStandingAmount;
    }

    public void setOutStandingAmount(String outStandingAmount) {
        this.outStandingAmount = outStandingAmount;
    }
}
