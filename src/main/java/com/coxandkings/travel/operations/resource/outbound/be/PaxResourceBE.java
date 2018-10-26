package com.coxandkings.travel.operations.resource.outbound.be;


import com.coxandkings.travel.ext.model.be.PaxInfo;

public class PaxResourceBE {
    private String userID;
    private String orderID;
    private PaxInfo paxInfo;
    private  String bookID;

    public PaxResourceBE(PaxResource paxResource) {
        this.userID = paxResource.getUserId();
        this.orderID = paxResource.getProductId();
        this.bookID = paxResource.getBookingRefId();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public PaxInfo getPaxInfo() {
        return paxInfo;
    }

    public void setPaxInfo(PaxInfo paxInfo) {
        this.paxInfo = paxInfo;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }
}
