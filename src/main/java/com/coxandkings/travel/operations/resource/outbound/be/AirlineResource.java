package com.coxandkings.travel.operations.resource.outbound.be;

import com.coxandkings.travel.ext.model.be.FlightDetails;

public class AirlineResource {
    private String productId;
    private String userID;
    private FlightDetails flightDetails;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
