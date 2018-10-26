package com.coxandkings.travel.operations.resource.productsharing;

import com.coxandkings.travel.operations.resource.BaseResource;

public class SharedBookingResource extends BaseResource {

    private String sharedBookingId;
    private String bookingReferenceId;
    private String customerId;
    private String suggestedCustomerId;
    private String productId;
    private String status;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSharedBookingId() {
        return sharedBookingId;
    }

    public void setSharedBookingId(String sharedBookingId) {
        this.sharedBookingId = sharedBookingId;
    }

    public String getBookingReferenceId() {
        return bookingReferenceId;
    }

    public void setBookingReferenceId(String bookingReferenceId) {
        this.bookingReferenceId = bookingReferenceId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSuggestedCustomerId() {
        return suggestedCustomerId;
    }

    public void setSuggestedCustomerId(String suggestedCustomerId) {
        this.suggestedCustomerId = suggestedCustomerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
