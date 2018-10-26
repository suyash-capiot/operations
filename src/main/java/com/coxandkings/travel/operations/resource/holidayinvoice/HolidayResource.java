package com.coxandkings.travel.operations.resource.holidayinvoice;

import java.math.BigDecimal;

public class HolidayResource {
    private String bookingNo;
    private String orderId;
    private String invoiceNo;
    private BigDecimal roe;

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public BigDecimal getRoe() {
        return roe;
    }

    public void setRoe(BigDecimal roe) {
        this.roe = roe;
    }

    @Override
    public String toString() {
        return "HolidayResource{" +
                "bookingNo='" + bookingNo + '\'' +
                ", orderId='" + orderId + '\'' +
                ", invoiceNo='" + invoiceNo + '\'' +
                ", roe=" + roe +
                '}';
    }
}
