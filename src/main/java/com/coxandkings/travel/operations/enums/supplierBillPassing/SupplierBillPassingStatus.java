package com.coxandkings.travel.operations.enums.supplierBillPassing;

public enum SupplierBillPassingStatus {
    PENDING("Pending"),
    PENDING_APPROVAL("Pending_Approval"),
    APPROVED("Approved"),
    DONE("Done"),
    REJECTED("Rejected");
    private String value;
    SupplierBillPassingStatus(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
