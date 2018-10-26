package com.coxandkings.travel.operations.criteria.booking;

import com.coxandkings.travel.operations.criteria.BaseCriteria;

public class BookingCriteria extends BaseCriteria {
    private String[] bookingRefId;
    private String assignedByUserId;
    private String assignedToUserId;

    public String[] getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String... bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public String getAssignedByUserId() {
        return assignedByUserId;
    }

    public void setAssignedByUserId(String assignedByUserId) {
        this.assignedByUserId = assignedByUserId;
    }

    public String getAssignedToUserId() {
        return assignedToUserId;
    }

    public void setAssignedToUserId(String assignedToUserId) {
        this.assignedToUserId = assignedToUserId;
    }
}
