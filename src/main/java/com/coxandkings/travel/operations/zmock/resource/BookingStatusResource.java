package com.coxandkings.travel.operations.zmock.resource;

public class BookingStatusResource {

    private String userID;
    private String BookID;
    private String status;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getBookID() {
        return BookID;
    }

    public void setBookID(String bookID) {
        BookID = bookID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BookingStatusResource{" +
                "userID='" + userID + '\'' +
                ", BookID='" + BookID + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
