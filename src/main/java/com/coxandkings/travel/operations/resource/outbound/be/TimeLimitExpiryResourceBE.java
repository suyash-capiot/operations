package com.coxandkings.travel.operations.resource.outbound.be;

public class TimeLimitExpiryResourceBE {
    private String userId;
    private String orderId;
    private String newDate;
    private String bookId;

    public TimeLimitExpiryResourceBE(TimeLimitExpiryResource timeLimitExpiryResource) {
        this.userId = timeLimitExpiryResource.getUserId();
        this.orderId = timeLimitExpiryResource.getProductId();
        this.newDate = timeLimitExpiryResource.getTimeLimitExpiryDate();
        this.bookId = timeLimitExpiryResource.getBookingRefId();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getNewDate() {
        return newDate;
    }

    public void setNewDate(String newDate) {
        this.newDate = newDate;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
