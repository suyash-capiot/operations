package com.coxandkings.travel.operations.enums.thirdPartyVoucher;

public enum UnitOfMeasurement {

    PER_DAY("Per Day"),
    PER_PET("Per Pet"),
    PER_ROOM("Per Room"),
    PER_NIGHT("Per Night"),
    PER_PERSON("Per Person"),
    PER_PERSON_PER_NIGHT("Per Person-Per Night"),
    PER_RESERVATION("Per Reservation");

    String value;

    UnitOfMeasurement(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
