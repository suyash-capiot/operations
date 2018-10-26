package com.coxandkings.travel.operations.zmock.resource;

public class AccoSupplierCancellationChargesResource {
    private String userID;
    private String roomID;
    private String suppCancellationCharges;

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

    public String getSuppCancellationCharges() {
        return suppCancellationCharges;
    }

    public void setSuppCancellationCharges(String suppCancellationCharges) {
        this.suppCancellationCharges = suppCancellationCharges;
    }

    @Override
    public String toString() {
        return "AccoSupplierCancellationChargesResource{" +
                "userID='" + userID + '\'' +
                ", roomID='" + roomID + '\'' +
                ", suppCancellationCharges='" + suppCancellationCharges + '\'' +
                '}';
    }
}
