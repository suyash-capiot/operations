package com.coxandkings.travel.operations.enums.managedocumentation;

public enum ApplyCutOff {

    IMMEDIATE_AFTER_BOOKING("Immediate After Booking"),
    PRIOR_TO_TRAVEL_DATE("Prior to Travel Date"),
    FROM_BOOKING_DATE("From Booking Date"),
    DAYS("Days"),
    HOURS("Hours");

    private String value;

    ApplyCutOff(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
