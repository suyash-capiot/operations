package com.coxandkings.travel.operations.resource.outbound.be;

public class StayDatesBE {
    String userID;
    String roomID;
    String checkInDate;
    String checkOutDate;

    public StayDatesBE() {
    }

    public StayDatesBE(StayDatesResource stayDatesResource) {
        this.userID = stayDatesResource.getUserID();
        this.roomID = stayDatesResource.getRoomID();
        this.checkInDate = stayDatesResource.getCheckInDate();
        this.checkOutDate = stayDatesResource.getCheckOutDate();
    }

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

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
}
