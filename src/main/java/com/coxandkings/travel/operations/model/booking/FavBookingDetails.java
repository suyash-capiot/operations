package com.coxandkings.travel.operations.model.booking;

import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table
public class FavBookingDetails extends BaseModel {


    @Column(name = "fav_id")
    private String favId;

    @Column(name = "booking_ref_id")
    private String bookingRefId;

    @Column(name = "booking_type_id")
    private String bookingTypeId;

    @Column(name = "booking_from_date")
    private Long bookingFromDate;

    @Column(name = "booking_to_date")
    private Date bookingToDate;

    @Column(name = "booking_status_id")
    private String bookingStatusId;

    @Column(name = "priority")
    private String priority;

    @Column(name = "assignmnt")
    private String assignment;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "financial_control_id")
    private String financialControlId;

    public String getFavId() {
        return favId;
    }

    public void setFavId(String favId) {
        this.favId = favId;
    }

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

    public String getFinancialControlId() {
        return financialControlId;
    }

    public void setFinancialControlId(String financialControlId) {
        this.financialControlId = financialControlId;
    }
}
