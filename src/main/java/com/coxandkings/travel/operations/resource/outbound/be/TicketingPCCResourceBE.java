package com.coxandkings.travel.operations.resource.outbound.be;

public class TicketingPCCResourceBE {
    private String userID;
    private String orderID;
    private String ticketingPCC;

    public TicketingPCCResourceBE() {
    }

    public TicketingPCCResourceBE(TicketingPCCResource ticketingPCCResource) {
        this.userID = ticketingPCCResource.getUserId();
        this.orderID = ticketingPCCResource.getProductId();
        this.ticketingPCC = ticketingPCCResource.getTicketingPCC();
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

    public String getTicketingPCC() {
        return ticketingPCC;
    }

    public void setTicketingPCC(String ticketingPCC) {
        this.ticketingPCC = ticketingPCC;
    }
}
