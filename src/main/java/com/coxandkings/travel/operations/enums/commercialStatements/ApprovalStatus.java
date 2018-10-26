package com.coxandkings.travel.operations.enums.commercialStatements;

public enum ApprovalStatus {
    APPROVED("Approve"),
    REJECTED("Reject");
    private String value;
    ApprovalStatus(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
