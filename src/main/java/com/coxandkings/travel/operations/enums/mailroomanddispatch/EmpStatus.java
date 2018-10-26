package com.coxandkings.travel.operations.enums.mailroomanddispatch;

public enum EmpStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private String value;

    EmpStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
