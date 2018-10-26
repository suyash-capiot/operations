package com.coxandkings.travel.operations.model.refund.finaceRefund;

public enum ClientType {

    B2B("B2B"), B2C("B2C");

    String clientType;

    ClientType(String clientType) {
        this.clientType = clientType;
    }



    public static ClientType getClientType(String type) {
        ClientType clientType = null;
        for (ClientType clientType1 : ClientType.values()) {
            if (clientType1.getClientType().equalsIgnoreCase(type)) {
                clientType = clientType1;
                break;
            }
        }
        return clientType;
    }

    public String getClientType() {
        return clientType;
    }
}
