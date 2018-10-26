package com.coxandkings.travel.operations.zmock.resource;

public class AccoSupplierAmendmentChargesResource {
    private String userID;
    private String roomID;
    private String suppAmendmentCharges;

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

    public String getSuppAmendmentCharges() {
        return suppAmendmentCharges;
    }

    public void setSuppAmendmentCharges(String suppAmendmentCharges) {
        this.suppAmendmentCharges = suppAmendmentCharges;
    }

    @Override
    public String toString() {
        return "AccoSupplierAmendmentChargesResource{" +
                "userID='" + userID + '\'' +
                ", roomID='" + roomID + '\'' +
                ", suppAmendmentCharges='" + suppAmendmentCharges + '\'' +
                '}';
    }
}
