package com.coxandkings.travel.operations.model.productsharing;

import com.coxandkings.travel.operations.model.BaseModel;
import com.coxandkings.travel.operations.resource.productsharing.ProductSharingStatus;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "product_sharing")
public class ProductSharing extends BaseModel implements Serializable {

    @Column(name = "bookRefNo")
    private String bookRefNo;

    @Column(name = "orderID")
    private String orderID;

    @Column(name = "passengerId")
    private String passengerId;

    @Column(name = "toSerialNumber")
    private String toSerialNumber;

    @Column(name = "fromSerialNumber")
    private String fromSerialNumber;

    @Column(name = "fromBookRefNo")
    private String fromBookRefNo;

    @Column(name = "fromOrderID")
    private String fromOrderID;

    @Column(name = "fromPassengerId")
    private String fromPassengerId;

    @Enumerated(EnumType.STRING)
    private ProductSharingStatus status;


    @Column(name = "gender")
    private String genderPreferenceForSharing;

    @Column(name = "toDoTaskID")
    private String toDoTaskID;

    @Column(name = "isSentEmail")
    private boolean isSentEmail;

    @Column(name = "isExpiredLink")
    private boolean isExpiredLink;

    @Column(name = "hash")
    private String hash;

    @Column(name = "secondRef")
    private String secondRef;

    @Column(name = "isConverted")
    private boolean isConverted;

//    @Column(name = "comment")
//    private String comment;

    public boolean isConverted() {
        return isConverted;
    }

    public void setConverted(boolean converted) {
        isConverted = converted;
    }

    public String getToDoTaskID() {
        return toDoTaskID;
    }

    public void setToDoTaskID(String toDoTaskID) {
        this.toDoTaskID = toDoTaskID;
    }

    public String getBookRefNo() {
        return bookRefNo;
    }

    public void setBookRefNo(String bookRefNo) {
        this.bookRefNo = bookRefNo;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public ProductSharingStatus getStatus() {
        return status;
    }

    public void setStatus(ProductSharingStatus status) {
        this.status = status;
    }

    public ProductSharing() {
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getGenderPreferenceForSharing() {
        return genderPreferenceForSharing;
    }

    public void setGenderPreferenceForSharing(String genderPreferenceForSharing) {
        this.genderPreferenceForSharing = genderPreferenceForSharing;
    }

    public boolean isSentEmail() {
        return isSentEmail;
    }

    public void setSentEmail(boolean sentEmail) {
        isSentEmail = sentEmail;
    }

    public String getToSerialNumber() {
        return toSerialNumber;
    }

    public void setToSerialNumber(String toSerialNumber) {
        this.toSerialNumber = toSerialNumber;
    }

    public String getFromSerialNumber() {
        return fromSerialNumber;
    }

    public void setFromSerialNumber(String fromSerialNumber) {
        this.fromSerialNumber = fromSerialNumber;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public boolean isExpiredLink() {
        return isExpiredLink;
    }

    public void setExpiredLink(boolean expiredLink) {
        isExpiredLink = expiredLink;
    }

    public String getFromBookRefNo() {
        return fromBookRefNo;
    }

    public void setFromBookRefNo(String fromBookRefNo) {
        this.fromBookRefNo = fromBookRefNo;
    }

    public String getFromOrderID() {
        return fromOrderID;
    }

    public void setFromOrderID(String fromOrderID) {
        this.fromOrderID = fromOrderID;
    }

    public String getFromPassengerId() {
        return fromPassengerId;
    }

    public void setFromPassengerId(String fromPassengerId) {
        this.fromPassengerId = fromPassengerId;
    }

    public String getSecondRef() {
        return secondRef;
    }

    public void setSecondRef(String secondRef) {
        this.secondRef = secondRef;
    }

//    public String getComment() {
//        return comment;
//    }
//
//    public void setComment(String comment) {
//        this.comment = comment;
//    }

    @Override
    public String toString() {
        return "ProductSharing{" +
                "bookRefNo='" + bookRefNo + '\'' +
                ", orderID='" + orderID + '\'' +
                ", passengerId='" + passengerId + '\'' +
                ", toSerialNumber='" + toSerialNumber + '\'' +
                ", fromSerialNumber='" + fromSerialNumber + '\'' +
                ", fromBookRefNo='" + fromBookRefNo + '\'' +
                ", fromOrderID='" + fromOrderID + '\'' +
                ", fromPassengerId='" + fromPassengerId + '\'' +
                ", status=" + status +
                ", genderPreferenceForSharing='" + genderPreferenceForSharing + '\'' +
                ", toDoTaskID='" + toDoTaskID + '\'' +
                ", isSentEmail=" + isSentEmail +
                ", isExpiredLink=" + isExpiredLink +
                ", hash='" + hash + '\'' +
                '}';
    }
}
