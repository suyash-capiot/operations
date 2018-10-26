package com.coxandkings.travel.operations.resource.reconfirmation.client;

public class ClientSendReconfirmationResource {

    private String clientReconfirmationID;

    public String getClientReconfirmationID( ) {
        return clientReconfirmationID;
    }

    public void setClientReconfirmationID( String clientReconfirmationID ) {
        this.clientReconfirmationID = clientReconfirmationID;
    }


    @Override
    public String toString() {
        return "ClientSendReconfirmationResource{" +
                "clientReconfirmationID='" + clientReconfirmationID + '\'' +
                '}';
    }
}
