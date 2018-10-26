package com.coxandkings.travel.operations.zmock.resource;

public class SupplierReconfirmationDate {

    private String userID;
    private String orderID;
    private String suppReconfirmDate;


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

    public String getSuppReconfirmDate() {
        return suppReconfirmDate;
    }

    public void setSuppReconfirmDate(String suppReconfirmDate) {
        this.suppReconfirmDate = suppReconfirmDate;
    }

    @Override
    public String toString() {
        return "SupplierReconfirmationDate{" +
                "userID='" + userID + '\'' +
                ", orderID='" + orderID + '\'' +
                ", suppReconfirmDate='" + suppReconfirmDate + '\'' +
                '}';
    }
}
