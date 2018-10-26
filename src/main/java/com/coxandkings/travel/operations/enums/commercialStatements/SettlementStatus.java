package com.coxandkings.travel.operations.enums.commercialStatements;

public enum SettlementStatus {

    UNSETTLED("UNSETTLED"),
    SETTLED("SETTLED"),
    PARTIALLY_SETTLED("PARTIALLY_SETTLED");

    private String value;

    SettlementStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
