package com.coxandkings.travel.operations.enums.supplierBillPassing;

public enum StopPaymentUntil {
    NOTIFICATION("Notification"),
    DATE("Date");
    private String value;

    StopPaymentUntil(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
