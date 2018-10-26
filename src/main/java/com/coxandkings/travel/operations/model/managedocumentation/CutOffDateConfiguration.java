package com.coxandkings.travel.operations.model.managedocumentation;


import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table
public class CutOffDateConfiguration {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column
    private String id;
    private String orderId;
    private String bookId;
    private String paxId;
    private String roomId;
    private String documentSettingId;
    private String documentWise;
    private String documentBy;
    private String paxName;
    private String documentName;
    private String productName;
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime cutOffDate;

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

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
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

    public String getPaxName() {
        return paxName;
    }

    public void setPaxName(String paxName) {
        this.paxName = paxName;
    }
}