package com.coxandkings.travel.operations.zmock.resource;

public class TicketingPCCResource {
    private String userID;
    private String orderID;
    private String ticketingPCC;

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

    public String getTicketingPCC() {
        return ticketingPCC;
    }

    public void setTicketingPCC(String ticketingPCC) {
        this.ticketingPCC = ticketingPCC;
    }

    @Override
    public String toString() {
        return "TicketingPCCResource{" +
                "userID='" + userID + '\'' +
                ", orderID='" + orderID + '\'' +
                ", ticketingPCC='" + ticketingPCC + '\'' +
                '}';
    }
}
