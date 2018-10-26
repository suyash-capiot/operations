package com.coxandkings.travel.operations.criteria.mailroomanddispatch;

import com.coxandkings.travel.operations.criteria.BaseCriteria;
import com.coxandkings.travel.operations.enums.mailroomanddispatch.InboundEntryStatus;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateTimeDeserializer;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.ZonedDateTime;

public class InboundEntryCriteria extends BaseCriteria {

    private String id;
    private String inboundNo;
    private String awbNo;
    private String mailRoomName;
    private String senderName;
    private String recipientName;
    private String floor;
    private InboundEntryStatus statusId;
    @JsonDeserialize(using= MailroomZonedDateTimeDeserializer.class)
    private ZonedDateTime receiptFromDate;
    @JsonDeserialize(using= MailroomZonedDateTimeDeserializer.class)
    private ZonedDateTime receiptToDate;
    private String department;
    private Integer pageSize;
    private Integer pageNumber;

    public String getInboundNo() {
        return inboundNo;
    }

    public void setInboundNo(String inboundNo) {
        this.inboundNo = inboundNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAwbNo() {
        return awbNo;
    }

    public void setAwbNo(String awbNo) {
        this.awbNo = awbNo;
    }

    public String getMailRoomName() {
        return mailRoomName;
    }

    public void setMailRoomName(String mailRoomName) {
        this.mailRoomName = mailRoomName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

   /* public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }*/

    public InboundEntryStatus getStatusId() {
        return statusId;
    }

    public void setStatusId(InboundEntryStatus statusId) {
        this.statusId = statusId;
    }

    public ZonedDateTime getReceiptFromDate() {
        return receiptFromDate;
    }

    public void setReceiptFromDate(ZonedDateTime receiptFromDate) {
        this.receiptFromDate = receiptFromDate;
    }

    public ZonedDateTime getReceiptToDate() {
        return receiptToDate;
    }

    public void setReceiptToDate(ZonedDateTime receiptToDate) {
        this.receiptToDate = receiptToDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public Integer getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public Integer getPageNumber() {
        return pageNumber;
    }

    @Override
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
