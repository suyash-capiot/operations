package com.coxandkings.travel.operations.resource.booking;

import com.coxandkings.travel.operations.model.bookingHistory.BookingHistory;

public class BookingHistoryItem {

    private String bookID;
    private String orderID;
    private String productCategory;
    private String productSubCategory;
    private String status;
    private String timestamp;
    private String action;
    private String description;
    private Float versionNo;

    public BookingHistoryItem() {
    }

    public BookingHistoryItem(BookingHistory bookingHistory) {
        this.bookID = bookingHistory.getBookId();
        this.orderID = bookingHistory.getOrderId();
        this.productCategory = bookingHistory.getProductCategory();
        this.productSubCategory = bookingHistory.getProductSubCategory();
        this.status = bookingHistory.getStatus();
        this.timestamp = bookingHistory.getTimeStamp().toString();
        this.action = bookingHistory.getActionType();
        this.description = bookingHistory.getOperation(); //TODO: To Set Description
        this.versionNo = bookingHistory.getVersionNo();
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Float versionNo) {
        this.versionNo = versionNo;
    }

}
