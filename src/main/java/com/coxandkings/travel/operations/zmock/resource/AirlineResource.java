package com.coxandkings.travel.operations.zmock.resource;

import com.coxandkings.travel.ext.model.be.FlightDetails;

public class AirlineResource {

    private String orderID;

    private String userID;
    private FlightDetails flightDetails;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public FlightDetails getFlightDetails() {
        return flightDetails;
    }

    public void setFlightDetails(FlightDetails flightDetails) {
        this.flightDetails = flightDetails;
    }

    @Override
    public String toString() {
        return "AirlineResource{" +
                "orderID='" + orderID + '\'' +
                ", userID='" + userID + '\'' +
                ", flightDetails=" + flightDetails +
                '}';
    }
}
