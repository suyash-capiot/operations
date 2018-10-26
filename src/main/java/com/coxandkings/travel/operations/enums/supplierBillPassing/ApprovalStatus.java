package com.coxandkings.travel.operations.enums.supplierBillPassing;

public enum ApprovalStatus {
    APPROVED("Accept"),
    REJECTED("Reject");
    private String value;
    ApprovalStatus(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
