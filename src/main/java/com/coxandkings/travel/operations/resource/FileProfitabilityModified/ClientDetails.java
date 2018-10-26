package com.coxandkings.travel.operations.resource.FileProfitabilityModified;

public class ClientDetails {
    private String clientName;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return "ClientDetails{" +
                "clientName='" + clientName + '\'' +
                '}';
    }
}
