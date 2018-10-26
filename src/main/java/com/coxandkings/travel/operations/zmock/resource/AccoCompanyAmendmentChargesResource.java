package com.coxandkings.travel.operations.zmock.resource;

public class AccoCompanyAmendmentChargesResource {
    private String userID;
    private String roomID;
    private String companyAmendmentCharges;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getCompanyAmendmentCharges() {
        return companyAmendmentCharges;
    }

    public void setCompanyAmendmentCharges(String companyAmendmentCharges) {
        this.companyAmendmentCharges = companyAmendmentCharges;
    }

    @Override
    public String toString() {
        return "AccoCompanyAmendmentChargesResource{" +
                "userID='" + userID + '\'' +
                ", roomID='" + roomID + '\'' +
                ", companyAmendmentCharges='" + companyAmendmentCharges + '\'' +
                '}';
    }
}
