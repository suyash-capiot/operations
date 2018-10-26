package com.coxandkings.travel.operations.enums.commercialStatements;

public enum CommercialType {
    ALL("ALL"),
    RECEIVABLE("Receivable"),
    PAYABLE("Payable");

    private String value;

    CommercialType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
