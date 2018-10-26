package com.coxandkings.travel.operations.enums.supplierBillPassing;

public enum  StopPaymentStatus {
    ACTIVE("Active"),
    INACTIVE("InActive");
    private String value;
    StopPaymentStatus(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}

