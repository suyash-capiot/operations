package com.coxandkings.travel.operations.model.managearrivallist;


import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Arrival_List_Accomodation")
public class AccommodationArrivalListItem extends BaseModel {

    @Column(name = "productCategorySubType")
    private String productCategorySubType;

    @Column(name = "productName")
    private String productName;

    @Column(name = "bookingReferenceId")
    private String bookingReferenceId;

    @Column(name = "passengerName")
    private String passengerName;

    @Column(name = "passengerType")
    private String passengerType;

    @Column(name = "checkInDate")
    private String checkInDate;

    @Column(name = "checkOutDate")
    private String checkOutDate;

    @Column(name = "supplierName")
    private String supplierName;

    @Column(name = "supplierReferenceNo")
    private String supplierReferenceNo;

    @Column(name = "roomCategory")
    private String roomCategory;

    @Column(name = "roomType")
    private String roomType;

    @Column(name = "totalNumberOfRooms")
    private Integer totalNumberOfRooms;

    @Column(name = "totalNumberOfPassengers")
    private Integer totalNumberOfPassengers;

    public String getProductCategorySubType() {
        return productCategorySubType;
    }

    public void setProductCategorySubType(String productCategorySubType) {
        this.productCategorySubType = productCategorySubType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBookingReferenceId() {
        return bookingReferenceId;
    }

    public void setBookingReferenceId(String bookingReferenceId) {
        this.bookingReferenceId = bookingReferenceId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierReferenceNo() {
        return supplierReferenceNo;
    }

    public void setSupplierReferenceNo(String supplierReferenceNo) {
        this.supplierReferenceNo = supplierReferenceNo;
    }

    public String getRoomCategory() {
        return roomCategory;
    }

    public void setRoomCategory(String roomCategory) {
        this.roomCategory = roomCategory;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getTotalNumberOfRooms() {
        return totalNumberOfRooms;
    }

    public void setTotalNumberOfRooms(Integer totalNumberOfRooms) {
        this.totalNumberOfRooms = totalNumberOfRooms;
    }

    public Integer getTotalNumberOfPassengers() {
        return totalNumberOfPassengers;
    }

    public void setTotalNumberOfPassengers(Integer totalNumberOfPassengers) {
        this.totalNumberOfPassengers = totalNumberOfPassengers;
    }
}
