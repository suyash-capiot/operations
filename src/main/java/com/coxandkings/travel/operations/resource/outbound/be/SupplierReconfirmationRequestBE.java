package com.coxandkings.travel.operations.resource.outbound.be;

public class SupplierReconfirmationRequestBE {
    private String userID;
    private String orderID;
    private String suppReconfirmDate;

    public SupplierReconfirmationRequestBE(ReconfirmationRequestResource reconfirmationRequestResource) {
        this.userID = reconfirmationRequestResource.getUserID();
        this.orderID = reconfirmationRequestResource.getProductId();
        this.suppReconfirmDate = reconfirmationRequestResource.getDate();
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

    public String getSuppReconfirmDate() {
        return suppReconfirmDate;
    }

    public void setSuppReconfirmDate(String suppReconfirmDate) {
        this.suppReconfirmDate = suppReconfirmDate;
    }
}
