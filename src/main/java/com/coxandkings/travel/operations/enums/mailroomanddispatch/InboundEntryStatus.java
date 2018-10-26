package com.coxandkings.travel.operations.enums.mailroomanddispatch;

public enum InboundEntryStatus {

    PENDING("Pending"),
    DELIVERY("Delivered"),
    UNDELIVERY("Undelivered"),
    ASSIGNED_TO_DELIVERY_BOY("Assigned to delivery boy"),
    RECIEVED_AT_MAILROOM_LOCATION("Received at mailroom location"),
    SENT_TO_MAILROOM_LOCATION("Sent to mailroom location"),
    PARCEL_NOT_CREATED("Parcel not created"),
    PARCEL_CREATED_BUT_NOT_DISPATCHED("Parcel created but not dispatched");

    private String value;

    InboundEntryStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
