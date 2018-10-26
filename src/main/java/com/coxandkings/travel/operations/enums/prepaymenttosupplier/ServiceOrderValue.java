package com.coxandkings.travel.operations.enums.prepaymenttosupplier;

public enum ServiceOrderValue {
    PSO("Provisional Service Order"),
    FSO("Final Service Order"),
    CCS("Client Commercial Statement"),
    SCS("Supplier Commercial Statement");

    private String serviceOrderValue;

    ServiceOrderValue(String serviceOrder) {
        this.serviceOrderValue = serviceOrder;

    }
}
