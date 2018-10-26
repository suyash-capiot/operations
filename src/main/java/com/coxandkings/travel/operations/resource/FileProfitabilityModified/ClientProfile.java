package com.coxandkings.travel.operations.resource.FileProfitabilityModified;

public class ClientProfile {
    private ClientDetails clientDetails;

    public ClientDetails getClientDetails() {
        return clientDetails;
    }

    public void setClientDetails(ClientDetails clientDetails) {
        this.clientDetails = clientDetails;
    }

    @Override
    public String toString() {
        return "ClientProfile{" +
                "clientDetails=" + clientDetails +
                '}';
    }
}
