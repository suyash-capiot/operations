package com.coxandkings.travel.operations.model.core;

public enum OpsBookingStatus {
    CNF("Confirmed"),
    RQ("On Request"),
    VCH("Vouchered"),
    XL("Cancelled"),
    TKD("Ticketed"),
    OK("Confirmed"),
    WL("Waitlisted"),
    RXL("Request for Cancellation"),
    REJ("Rejected"),
    RAMD("Request for Amendment"),
    TKDVCH("Ticketed/Vouchered"),
    PARTIALLYCONFIRMED("Partially Confirmed");

    private String bookingStatus;

    OpsBookingStatus(String newStatus) {
        bookingStatus = newStatus;
    }

    public static OpsBookingStatus fromString(String newStatus) {
        OpsBookingStatus aBookingStatus = null;
        if (newStatus != null && !newStatus.isEmpty()) {
//            return aBookingStatus;
//        }

            for (OpsBookingStatus tmpBookingStatus : OpsBookingStatus.values()) {
                if (tmpBookingStatus.getBookingStatus().equalsIgnoreCase(newStatus)) {
                    aBookingStatus = tmpBookingStatus;
                    break;
                }
            }
        }
        return aBookingStatus;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }
}
