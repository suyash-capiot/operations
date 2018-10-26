package com.coxandkings.travel.operations.resource.outbound.be;

public class TimeLimitExpiryResource {
    private String userId;
    private String bookingRefId;
    private String productId;
    private String timeLimitExpiryDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTimeLimitExpiryDate() {
        return timeLimitExpiryDate;
    }

    public void setTimeLimitExpiryDate(String timeLimitExpiryDate) {
        this.timeLimitExpiryDate = timeLimitExpiryDate;
    }
}
