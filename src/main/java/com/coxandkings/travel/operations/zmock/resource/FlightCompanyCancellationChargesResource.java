package com.coxandkings.travel.operations.zmock.resource;

public class FlightCompanyCancellationChargesResource {
    private String userID;
    private String orderID;
    private String companyCancellationCharges;

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

    public String getCompanyCancellationCharges() {
        return companyCancellationCharges;
    }

    public void setCompanyCancellationCharges(String companyCancellationCharges) {
        this.companyCancellationCharges = companyCancellationCharges;
    }

    @Override
    public String toString() {
        return "FlightCompanyCancellationChargesResource{" +
                "userID='" + userID + '\'' +
                ", orderID='" + orderID + '\'' +
                ", companyCancellationCharges='" + companyCancellationCharges + '\'' +
                '}';
    }
}
