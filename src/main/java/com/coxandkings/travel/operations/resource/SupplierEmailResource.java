package com.coxandkings.travel.operations.resource;

public class SupplierEmailResource {
    private String bookId;
    private String orderId;
    private String supplierId;


    public SupplierEmailResource() {
    }

    public SupplierEmailResource(String bookId, String orderId, String supplierId) {
        this.bookId = bookId;
        this.orderId = orderId;
        this.supplierId = supplierId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
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
}
