package com.coxandkings.travel.operations.criteria.managedocumentation;

public class ReceivedDocsBookingInfoSearchCriteria {

    private String id;
    private String bookId;
    private String orderId;

    public ReceivedDocsBookingInfoSearchCriteria() {

    }

    public ReceivedDocsBookingInfoSearchCriteria(String bookId) {
        this.bookId = bookId;
    }

    public ReceivedDocsBookingInfoSearchCriteria(String bookId, String orderId) {
        this.bookId = bookId;
        this.orderId = orderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
