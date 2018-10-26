package com.coxandkings.travel.operations.resource.holidayinvoice;

public class HolidayInvoiceResource {
    private String invoiceNumber;

    public HolidayInvoiceResource(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
}
