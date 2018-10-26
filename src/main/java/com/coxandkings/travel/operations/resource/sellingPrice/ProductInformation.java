package com.coxandkings.travel.operations.resource.sellingPrice;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductInformation {
    private String bookingRefId;
    private String orderId;

    public ProductInformation() {
    }

    public ProductInformation(String bookingRefId, String productId) {
        this.bookingRefId = bookingRefId;
        this.orderId = productId;
    }

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
}
