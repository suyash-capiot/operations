package com.coxandkings.travel.operations.zmock.resource;

public class FlightSupplierAmendmentChargesResource {

    private String userID;
    private String orderID;
    private String suppAmendmentCharges;

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

    public String getSuppAmendmentCharges() {
        return suppAmendmentCharges;
    }

    public void setSuppAmendmentCharges(String suppAmendmentCharges) {
        this.suppAmendmentCharges = suppAmendmentCharges;
    }

    @Override
    public String toString() {
        return "FlightSupplierAmendmentChargesResource{" +
                "userID='" + userID + '\'' +
                ", orderID='" + orderID + '\'' +
                ", suppAmendmentCharges='" + suppAmendmentCharges + '\'' +
                '}';
    }
}
