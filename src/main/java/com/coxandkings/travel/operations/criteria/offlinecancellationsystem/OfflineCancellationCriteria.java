package com.coxandkings.travel.operations.criteria.offlinecancellationsystem;

public class OfflineCancellationCriteria {
    private String bookingId;
    private String orderId;
    private String supplierId;
    private String supplierMarket;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
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

    public String getSupplierMarket() {
        return supplierMarket;
    }

    public void setSupplierMarket(String supplierMarket) {
        this.supplierMarket = supplierMarket;
    }
}
