package com.coxandkings.travel.operations.zmock.resource;

public class UpdateExpiryResource {
    private String userID;
    private String expiryDate;
    private String bookID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    @Override
    public String toString() {
        return "UpdateExpiryResource{" +
                "userID='" + userID + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", bookID='" + bookID + '\'' +
                '}';
    }
}
