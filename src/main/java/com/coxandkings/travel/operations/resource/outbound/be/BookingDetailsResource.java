package com.coxandkings.travel.operations.resource.outbound.be;


import com.coxandkings.travel.operations.model.core.OpsBookingStatus;

public class BookingDetailsResource {

    private String bookingRefId;
    private String bookingTypeId;
    private Long bookingFromDate;
    private OpsBookingStatus opsBookingStatus;
    private String bookingStatusId;
    private Long bookingToDate;
    private String priority;
    private Boolean assignment;
    private String financialControlId;
    private String userId;

    public String getBookingRefId( ) {
        return bookingRefId;
    }

    public void setBookingRefId( String bookingRefId ) {
        this.bookingRefId = bookingRefId;
    }

    public String getBookingTypeId() {
        return bookingTypeId;
    }

    public void setBookingTypeId(String bookingTypeId) {
        this.bookingTypeId = bookingTypeId;
    }

    public Long getBookingFromDate( ) {
        return bookingFromDate;
    }

    public void setBookingFromDate( Long bookingFromDate ) {
        this.bookingFromDate = bookingFromDate;
    }

    public String getBookingStatusId() {
        return bookingStatusId;
    }

    public void setBookingStatusId(String bookingStatusId) {
        this.bookingStatusId = bookingStatusId;
    }

    public OpsBookingStatus getOpsBookingStatus() {
        return opsBookingStatus;
    }

    public void setOpsBookingStatus( OpsBookingStatus opsBookingStatus ) {
        this.opsBookingStatus = opsBookingStatus;
    }

    public Long getBookingToDate() {
        return bookingToDate;
    }

    public void setBookingToDate(Long bookingToDate) {
        this.bookingToDate = bookingToDate;
    }

    public String getPriority( ) {
        return priority;
    }

    public void setPriority( String priority ) {
        this.priority = priority;
    }

    public Boolean getAssignment( ) {
        return assignment;
    }

    public void setAssignment( Boolean assignment ) {
        this.assignment = assignment;
    }

    public String getFinancialControlId( ) {
        return financialControlId;
    }

    public void setFinancialControlId( String financialControlId ) {
        this.financialControlId = financialControlId;
    }

    public String getUserId( ) {
        return userId;
    }

    public void setUserId( String userId ) {
        this.userId = userId;
    }
}
