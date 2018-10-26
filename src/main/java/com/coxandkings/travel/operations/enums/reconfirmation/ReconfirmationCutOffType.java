package com.coxandkings.travel.operations.enums.reconfirmation;

public enum ReconfirmationCutOffType {

    PRIOR_TO_TRAVEL_DATE("priortotraveldate"),
    FROM_BOOKING_DATE("frombookingdate");

    private String value;

    ReconfirmationCutOffType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
