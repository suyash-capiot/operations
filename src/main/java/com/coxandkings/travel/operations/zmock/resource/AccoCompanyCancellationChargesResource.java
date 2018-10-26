package com.coxandkings.travel.operations.zmock.resource;

public class AccoCompanyCancellationChargesResource {
    private String userID;
    private String roomID;
    private String companyCancellationCharges;

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

    public String getCompanyCancellationCharges() {
        return companyCancellationCharges;
    }

    public void setCompanyCancellationCharges(String companyCancellationCharges) {
        this.companyCancellationCharges = companyCancellationCharges;
    }

    @Override
    public String toString() {
        return "AccoCompanyCancellationChargesResource{" +
                "userID='" + userID + '\'' +
                ", roomID='" + roomID + '\'' +
                ", companyCancellationCharges='" + companyCancellationCharges + '\'' +
                '}';
    }
}
