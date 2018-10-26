package com.coxandkings.travel.operations.criteria.managedocumentation;

public class HandoverDocGenSearchCriteria {

    private String id;
    private String orderId;
    private String bookID;
    private String documentSettingId;
    private String approvalRequestJustification;
    private String paxId;
    private String roomId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getDocumentSettingId() {
        return documentSettingId;
    }

    public void setDocumentSettingId(String documentSettingId) {
        this.documentSettingId = documentSettingId;
    }

    public String getApprovalRequestJustification() {
        return approvalRequestJustification;
    }

    public void setApprovalRequestJustification(String approvalRequestJustification) {
        this.approvalRequestJustification = approvalRequestJustification;
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
}
