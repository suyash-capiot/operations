
package com.coxandkings.travel.operations.resource.productsharing;

import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;

import java.io.Serializable;
import java.util.List;

public class ProductSharingResource implements Serializable {

    private String bookingReferenceNo;
    private String orderID;
    private String roomID;
    private OpsProductCategory productCategory;
    private OpsProductSubCategory productSubCategory;

    private ProductSharingMainResource paxInfo;
    private List<ProductSharingMainResource> sharedInProgress = null;
    private List<ProductSharingMainResource> sharedAccepted = null;
    private List<ProductSharingMainResource> sharedRejected = null;

    private final static long serialVersionUID = 2095561706841586563L;

    public String getBookingReferenceNo() {
        return bookingReferenceNo;
    }

    public void setBookingReferenceNo(String bookingReferenceNo) {
        this.bookingReferenceNo = bookingReferenceNo;
    }

    public ProductSharingResource withBookingReferenceNo(String bookingReferenceNo) {
        this.bookingReferenceNo = bookingReferenceNo;
        return this;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public ProductSharingResource withOrderID(String orderID) {
        this.orderID = orderID;
        return this;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public ProductSharingResource withRoomID(String roomID) {
        this.roomID = roomID;
        return this;
    }

    public ProductSharingMainResource getPaxInfo() {
        return paxInfo;
    }

    public void setPaxInfo(ProductSharingMainResource paxInfo) {
        this.paxInfo = paxInfo;
    }

    public ProductSharingResource withPaxInfo(ProductSharingMainResource productSharingMainResource) {
        this.paxInfo = productSharingMainResource;
        return this;
    }

    public List<ProductSharingMainResource> getSharedInProgress() {
        return sharedInProgress;
    }

    public void setSharedInProgress(List<ProductSharingMainResource> sharedInProgress) {
        this.sharedInProgress = sharedInProgress;
    }

    public List<ProductSharingMainResource> getSharedAccepted() {
        return sharedAccepted;
    }

    public void setSharedAccepted(List<ProductSharingMainResource> sharedAccepted) {
        this.sharedAccepted = sharedAccepted;
    }

    public List<ProductSharingMainResource> getSharedRejected() {
        return sharedRejected;
    }

    public void setSharedRejected(List<ProductSharingMainResource> sharedRejected) {
        this.sharedRejected = sharedRejected;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public OpsProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(OpsProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public OpsProductSubCategory getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(OpsProductSubCategory productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    @Override
    public String toString() {
        return "ProductSharingResource{" +
                "bookingReferenceNo='" + bookingReferenceNo + '\'' +
                ", orderID='" + orderID + '\'' +
                ", roomID='" + roomID + '\'' +
                ", productCategory=" + productCategory +
                ", productSubCategory=" + productSubCategory +
                ", paxInfo=" + paxInfo +
                ", sharedInProgress=" + sharedInProgress +
                ", sharedAccepted=" + sharedAccepted +
                ", sharedRejected=" + sharedRejected +
                '}';
    }
}
