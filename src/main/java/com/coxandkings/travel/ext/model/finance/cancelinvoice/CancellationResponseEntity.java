package com.coxandkings.travel.ext.model.finance.cancelinvoice;


public class CancellationResponseEntity {
    private String bookingAckNumber;
    private String debitNoteNumber;
    private String taxInvoiceNumber;

    public String getBookingAckNumber() {
        return bookingAckNumber;
    }

    public void setBookingAckNumber(String bookingAckNumber) {
        this.bookingAckNumber = bookingAckNumber;
    }

    public String getDebitNoteNumber() {
        return debitNoteNumber;
    }

    public void setDebitNoteNumber(String debitNoteNumber) {
        this.debitNoteNumber = debitNoteNumber;
    }

    public String getTaxInvoiceNumber() {
        return taxInvoiceNumber;
    }

    public void setTaxInvoiceNumber(String taxInvoiceNumber) {
        this.taxInvoiceNumber = taxInvoiceNumber;
    }
}
