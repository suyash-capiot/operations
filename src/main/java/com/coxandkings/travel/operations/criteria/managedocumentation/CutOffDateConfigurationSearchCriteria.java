package com.coxandkings.travel.operations.criteria.managedocumentation;

import java.time.ZonedDateTime;

public class CutOffDateConfigurationSearchCriteria {

    private String id;
    private String bookId;
    private String orderId;
    private String paxId;
    private String roomId;
    private String documentSettingId;
    private String documentBy;
    private String documentWise;
    private ZonedDateTime cutOffDate;
    private String documentName;
    private String productName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaxId() {
        return paxId;
    }

    public void setPaxId(String paxId) {
        this.paxId = paxId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getDocumentSettingId() {
        return documentSettingId;
    }

    public void setDocumentSettingId(String documentSettingId) {
        this.documentSettingId = documentSettingId;
    }

    public String getDocumentWise() {
        return documentWise;
    }

    public void setDocumentWise(String documentWise) {
        this.documentWise = documentWise;
    }

    public ZonedDateTime getCutOffDate() {
        return cutOffDate;
    }

    public void setCutOffDate(ZonedDateTime cutOffDate) {
        this.cutOffDate = cutOffDate;
    }

    public String getDocumentBy() {
        return documentBy;
    }

    public void setDocumentBy(String documentBy) {
        this.documentBy = documentBy;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
