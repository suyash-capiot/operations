package com.coxandkings.travel.operations.resource.booking;

public class UpdatePaxInfoResource {

    String bookId;
    String orderId;
    Object paxDetails;

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

    public Object getPaxDetails() {
        return paxDetails;
    }

    public void setPaxDetails(Object paxDetails) {
        this.paxDetails = paxDetails;
    }
}
