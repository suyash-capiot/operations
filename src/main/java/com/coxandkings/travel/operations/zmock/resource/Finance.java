package com.coxandkings.travel.operations.zmock.resource;

public class Finance {

    private String bookingRefId;
    private String orderId;
    private String supplierId;
    private String balanceAmtPayableToSupplier;

    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getBalanceAmtPayableToSupplier() {
        return balanceAmtPayableToSupplier;
    }

    public void setBalanceAmtPayableToSupplier(String balanceAmtPayableToSupplier) {
        this.balanceAmtPayableToSupplier = balanceAmtPayableToSupplier;
    }
}
