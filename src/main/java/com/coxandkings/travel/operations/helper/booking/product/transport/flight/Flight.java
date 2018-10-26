package com.coxandkings.travel.operations.helper.booking.product.transport.flight;

import com.coxandkings.travel.operations.helper.booking.product.transport.Transfer;

public class Flight extends Transfer {
    private String flightId;
    private String journeyType;
    private String classFareBasisTicketNo;
    private String gdsPNR;
    private String airlinePNR;

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
}
