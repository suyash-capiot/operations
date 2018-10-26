package com.coxandkings.travel.operations.zmock.resource;

public class FlightSupplierCancellationChargesResource {
    private String userID;
    private String orderID;
    private String suppCancellationCharges;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getSuppCancellationCharges() {
        return suppCancellationCharges;
    }

    public void setSuppCancellationCharges(String suppCancellationCharges) {
        this.suppCancellationCharges = suppCancellationCharges;
    }

    @Override
    public String toString() {
        return "FlightSupplierCancellationChargesResource{" +
                "userID='" + userID + '\'' +
                ", orderID='" + orderID + '\'' +
                ", suppCancellationCharges='" + suppCancellationCharges + '\'' +
                '}';
    }
}
