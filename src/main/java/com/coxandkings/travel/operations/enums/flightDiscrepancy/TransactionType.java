package com.coxandkings.travel.operations.enums.flightDiscrepancy;

public enum TransactionType {
    SALE("Sale"),
    REFUND("refund");

    private String value;

    TransactionType(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
