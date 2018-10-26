package com.coxandkings.travel.operations.zmock.resource;

public class ClientReconfirmationResource {

    private String userID;
    private String orderID;
    private String clientReconfirmDate;

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

    @Override
    public String toString() {
        return "ClientReconfirmationResource{" +
                "userID='" + userID + '\'' +
                ", orderID='" + orderID + '\'' +
                ", clientReconfirmDate='" + clientReconfirmDate + '\'' +
                '}';
    }
}
