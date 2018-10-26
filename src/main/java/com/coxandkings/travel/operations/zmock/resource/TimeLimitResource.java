package com.coxandkings.travel.operations.zmock.resource;

public class TimeLimitResource {
    private String supplierId;
    private String orderID;

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    @Override
    public String toString() {
        return "TimeLimitResource{" +
                "supplierId='" + supplierId + '\'' +
                ", orderID='" + orderID + '\'' +
                '}';
    }
}
