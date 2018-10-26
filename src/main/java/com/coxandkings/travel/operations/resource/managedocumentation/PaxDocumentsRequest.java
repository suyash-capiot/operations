package com.coxandkings.travel.operations.resource.managedocumentation;

public class PaxDocumentsRequest {
    private String passengerID;
    private PaxDocInfo paxDocuments;
    private String userID;

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    public PaxDocInfo getPaxDocuments() {
        return paxDocuments;
    }

    public void setPaxDocuments(PaxDocInfo paxDocuments) {
        this.paxDocuments = paxDocuments;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
