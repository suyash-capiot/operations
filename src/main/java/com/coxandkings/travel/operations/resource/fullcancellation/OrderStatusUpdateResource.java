package com.coxandkings.travel.operations.resource.fullcancellation;

import com.coxandkings.travel.operations.model.core.OpsOrderStatus;

public class OrderStatusUpdateResource {
    private String bookId;
    private String orderId;
    private OpsOrderStatus orderStatus;

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

    public OpsOrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OpsOrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
