package com.coxandkings.travel.operations.model.manageproductupdates;

import java.util.List;

public class CheaperPriceUpdatesFlightInfo extends CheaperPriceBookingInfo   {

    private List<ProductUpdatesFlightSegmentInfo> flightSegments;

    private String tripType;

    private String status;

    public CheaperPriceUpdatesFlightInfo()   {};

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProductUpdatesFlightSegmentInfo> getFlightSegments() {
        return flightSegments;
    }

    public void setFlightSegments(List<ProductUpdatesFlightSegmentInfo> flightSegments) {
        this.flightSegments = flightSegments;
    }
}

