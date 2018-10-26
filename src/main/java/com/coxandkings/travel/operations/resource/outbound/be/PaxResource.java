package com.coxandkings.travel.operations.resource.outbound.be;

import com.coxandkings.travel.ext.model.be.PaxInfo;

import java.util.List;

public class PaxResource {
    private String userId;
    private String bookingRefId;
    private String productId;
    private List<PaxInfo> paxInfo;

    public PaxResource() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public List<PaxInfo> getPaxInfo() {
        return paxInfo;
    }

    public void setPaxInfo(List<PaxInfo> paxInfo) {
        this.paxInfo = paxInfo;
    }
}
