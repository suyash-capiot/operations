package com.coxandkings.travel.operations.enums.timelimit;

public enum MDMType {
    EXPIRY_TYPE_BOOKING_DATE("booking date"),
    EXPIRY_TYPE_TRAVEL_DATE("travel date");
    public String type;

    MDMType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
