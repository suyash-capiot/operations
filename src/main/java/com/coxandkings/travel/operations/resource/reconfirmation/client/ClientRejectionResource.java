package com.coxandkings.travel.operations.resource.reconfirmation.client;

public class ClientRejectionResource {

    private String clientReconfirmationID;
    private String remarks;

    public String getClientReconfirmationID( ) {
        return clientReconfirmationID;
    }

    public void setClientReconfirmationID( String clientReconfirmationID ) {
        this.clientReconfirmationID = clientReconfirmationID;
    }

    public String getRemarks( ) {
        return remarks;
    }

    public void setRemarks( String remarks ) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "ClientRejectionResource{" +
                "clientReconfirmationID='" + clientReconfirmationID + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
