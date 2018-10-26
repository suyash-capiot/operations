package com.coxandkings.travel.operations.enums.mailroomanddispatch;

public enum WorkflowEnums {

    PENDING_APPROVAL("Pending_Approval"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private String value;

    WorkflowEnums(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
