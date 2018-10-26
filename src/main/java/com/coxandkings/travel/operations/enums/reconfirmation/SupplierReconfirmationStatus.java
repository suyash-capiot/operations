package com.coxandkings.travel.operations.enums.reconfirmation;

public enum SupplierReconfirmationStatus {

    RECONFIRMED_BY_SUPPLIER("Reconfirmed"),
    REJECTED_BY_SUPPLIER("Rejected By Supplier"),
    REJECTED_DUE_TO_NO_RESPONSE_FROM_SUPPLIER("Rejected due to No Response"),
    RECONFIRMATION_REQUEST_ON_HOLD_BY_SUPPLIER("Reconfirmation On Hold");

    private String value;

    SupplierReconfirmationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
