package com.coxandkings.travel.operations.enums.mailroomanddispatch;

public enum ParcelType {
    GROUP("Group"),
    INDIVIDUAL("Individual");

    private String value;

    ParcelType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
