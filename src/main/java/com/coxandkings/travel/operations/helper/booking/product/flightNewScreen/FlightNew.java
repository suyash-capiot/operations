package com.coxandkings.travel.operations.helper.booking.product.flightNewScreen;

import com.coxandkings.travel.operations.helper.booking.Location;
import com.coxandkings.travel.operations.helper.booking.product.Product;

public class FlightNew extends Product {

    private Location startLocation;
    private Location endLocation;
    private String flightId;
    private String journeyType;
    private Boolean isRoundTrip;
    private String classFareBasisTicketNo;
    private String gdsPNR;
    private String airlinePNR;
    private Boolean refundable;
    private FlightNew returnFlight;

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getJourneyType() {
        return journeyType;
    }

    public void setJourneyType(String journeyType) {
        this.journeyType = journeyType;
    }

    public FlightNew getReturnFlight() {
        return returnFlight;
    }

    public void setReturnFlight(FlightNew returnFlight) {
        this.returnFlight = returnFlight;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public String getClassFareBasisTicketNo() {
        return classFareBasisTicketNo;
    }

    public void setClassFareBasisTicketNo(String classFareBasisTicketNo) {
        this.classFareBasisTicketNo = classFareBasisTicketNo;
    }

    public String getGdsPNR() {
        return gdsPNR;
    }

    public void setGdsPNR(String gdsPNR) {
        this.gdsPNR = gdsPNR;
    }

    public String getAirlinePNR() {
        return airlinePNR;
    }

    public void setAirlinePNR(String airlinePNR) {
        this.airlinePNR = airlinePNR;
    }

    public Boolean getIsRoundTrip() {
        return isRoundTrip;
    }

    public void setIsRoundTrip(Boolean roundTrip) {
        isRoundTrip = roundTrip;
    }

    public Boolean getRefundable() {
        return refundable;
    }

    public void setRefundable(Boolean refundable) {
        this.refundable = refundable;
    }
}
