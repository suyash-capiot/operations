package com.coxandkings.travel.operations.resource.commercialStatement;

public class PassengerDetails {
    private String passengerName;
    private String passengerID;
    private String passengerTitle;
    private Boolean isLeadPassenger;
    private String passengerType;

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    public String getPassengerTitle() {
        return passengerTitle;
    }

    public void setPassengerTitle(String passengerTitle) {
        this.passengerTitle = passengerTitle;
    }

    public Boolean getLeadPassenger() {
        return isLeadPassenger;
    }

    public void setLeadPassenger(Boolean leadPassenger) {
        isLeadPassenger = leadPassenger;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }
}
