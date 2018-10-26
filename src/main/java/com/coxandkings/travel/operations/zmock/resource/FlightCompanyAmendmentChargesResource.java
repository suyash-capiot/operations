package com.coxandkings.travel.operations.zmock.resource;

public class FlightCompanyAmendmentChargesResource {
    private String userID;
    private String orderID;
    private String companyAmendmentCharges;

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

    public String getCompanyAmendmentCharges() {
        return companyAmendmentCharges;
    }

    public void setCompanyAmendmentCharges(String companyAmendmentCharges) {
        this.companyAmendmentCharges = companyAmendmentCharges;
    }

    @Override
    public String toString() {
        return "FlightCompanyAmendmentChargesResource{" +
                "userID='" + userID + '\'' +
                ", orderID='" + orderID + '\'' +
                ", companyAmendmentCharges='" + companyAmendmentCharges + '\'' +
                '}';
    }
}
