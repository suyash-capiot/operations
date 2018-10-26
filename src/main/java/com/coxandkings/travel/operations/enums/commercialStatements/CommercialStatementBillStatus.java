package com.coxandkings.travel.operations.enums.commercialStatements;

public enum CommercialStatementBillStatus {

    PENDING("Pending"),
    PENDING_APPROVAL("Pending_Approval"),
    APPROVED("Approved"),
    DONE("Done"),
    REJECTED("Rejected");
    private String value;

    CommercialStatementBillStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}


