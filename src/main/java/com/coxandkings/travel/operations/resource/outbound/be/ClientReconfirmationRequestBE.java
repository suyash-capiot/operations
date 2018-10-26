package com.coxandkings.travel.operations.resource.outbound.be;

public class ClientReconfirmationRequestBE {
    private String userID;
    private String orderID;
    private String clientReconfirmDate;

    public ClientReconfirmationRequestBE() {
    }

    public ClientReconfirmationRequestBE(ReconfirmationRequestResource reconfirmationRequestResource) {
        this.userID = reconfirmationRequestResource.getUserID();
        this.orderID = reconfirmationRequestResource.getProductId();
        this.clientReconfirmDate = reconfirmationRequestResource.getDate();
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

    public String getClientReconfirmDate() {
        return clientReconfirmDate;
    }

    public void setClientReconfirmDate(String clientReconfirmDate) {
        this.clientReconfirmDate = clientReconfirmDate;
    }
}
