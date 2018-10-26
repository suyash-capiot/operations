package com.coxandkings.travel.operations.resource.failure;

public class PnrResource {
    private String orderID;
    private String userID;
    private String airlinePnr;
    private String gdsPnr;
    private String ticketPNR;

    public PnrResource() {
        this.airlinePnr = "";
        this.gdsPnr = "";
        this.ticketPNR = "";

    }

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

    public String getAirlinePnr() {
        return airlinePnr;
    }

    public void setAirlinePnr(String airlinePnr) {
        this.airlinePnr = airlinePnr;
    }

    public String getGdsPnr() {
        return gdsPnr;
    }

    public void setGdsPnr(String gdsPnr) {
        this.gdsPnr = gdsPnr;
    }

    public String getTicketPNR() {
        return ticketPNR;
    }

    public void setTicketPNR(String ticketPNR) {
        this.ticketPNR = ticketPNR;
    }
}
