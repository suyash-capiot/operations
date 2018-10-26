package com.coxandkings.travel.operations.helper.enums;

public enum BookingType {
    PAID_BOOKING("PaidBooking"),
    TIME_LIMIT("TimeLimitBooking");

    private String bookingType;
    BookingType(String bookingType) {
        this.bookingType=bookingType;
    }

    public String getBookingType() {
        return this.bookingType;
    }
}
