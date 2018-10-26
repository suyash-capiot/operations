package com.coxandkings.travel.operations.enums.mailroomanddispatch;

public enum RecipientDetailUserType {
    INTERNAL("Internal"),
    EXTERNAL("External");

    private String value;

    RecipientDetailUserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
