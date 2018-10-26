package com.coxandkings.travel.operations.enums.mailroomanddispatch;

public enum DispatchAgainst {
    BOOKING("Booking"),
    ENQUIRY("Enquiry"),
    OTHERS("Others");

    private String value;

    DispatchAgainst(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
