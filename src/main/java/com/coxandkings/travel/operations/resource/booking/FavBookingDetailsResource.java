package com.coxandkings.travel.operations.resource.booking;

import com.coxandkings.travel.operations.resource.BaseResource;

import java.util.Date;


public class FavBookingDetailsResource extends BaseResource {

    private String  bookingRefId;
    private String  bookingTypeId;
    private Long bookingFromDate;
    private Date    bookingToDate;
    private String  bookingStatusId;
    private String  priority;
    private String  assignment;
    private String  userId;

    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public String getBookingTypeId() {
        return bookingTypeId;
    }

    public void setBookingTypeId(String bookingTypeId) {
        this.bookingTypeId = bookingTypeId;
    }

    public Long getBookingFromDate() {
        return bookingFromDate;
    }

    public void setBookingFromDate(Long bookingFromDate) {
        this.bookingFromDate = bookingFromDate;
    }

    public Date getBookingToDate() {
        return bookingToDate;
    }

    public void setBookingToDate(Date bookingToDate) {
        this.bookingToDate = bookingToDate;
    }

    public String getBookingStatusId() {
        return bookingStatusId;
    }

    public void setBookingStatusId(String bookingStatusId) {
        this.bookingStatusId = bookingStatusId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
