package com.coxandkings.travel.operations.resource.outbound.be;

import com.coxandkings.travel.ext.model.be.FlightDetails;

public class AirlineResourceBE {
    private String orderID;
    private String userID;
    private FlightDetails flightDetails;

    public AirlineResourceBE(AirlineResource airlineResource) {
        this.orderID = airlineResource.getProductId();
        this.userID = airlineResource.getUserID();
        this.flightDetails = airlineResource.getFlightDetails();
    }

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
}
