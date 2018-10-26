package com.coxandkings.travel.operations.enums.alternateOptions;

public enum  AlternateOptionsStatus {
    PENDING_APPROVAL("Pending_Approval"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private String value;

    AlternateOptionsStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
