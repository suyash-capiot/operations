package com.coxandkings.travel.operations.zmock.resource;

public class DobResource {

    private String userID;
    private String paxID;
    private String birthDate;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPaxID() {
        return paxID;
    }

    public void setPaxID(String paxID) {
        this.paxID = paxID;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "DobResource{" +
                "userID='" + userID + '\'' +
                ", paxID='" + paxID + '\'' +
                ", birthDate='" + birthDate + '\'' +
                '}';
    }
}
