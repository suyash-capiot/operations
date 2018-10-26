package com.coxandkings.travel.operations.model.merge;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "holiday_set_package")
public class HolidaySetPackageMerge extends Merge {

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_flavour_name")
    private String productFlavourName;

    @Column(name = "city")
    private String city;

    @Column(name = "no_of_nights")
    private Integer noOfNights;

    @Column(name = "check_in_date")
    private Long checkInDate;

    @Column(name = "room_category")
    private String roomCategory;

    @Column(name = "room_type")
    private String roomType;

    public HolidaySetPackageMerge() {
    }

    public HolidaySetPackageMerge(String id, String productName, String productFlavourName, String city, Integer noOfNights, Long checkInDate, String roomCategory, String roomType, String supplierId, Merge merge) {
//        this.id = id;
        this.productName = productName;
        this.productFlavourName = productFlavourName;
        this.city = city;
        this.noOfNights = noOfNights;
        this.checkInDate = checkInDate;
        this.roomCategory = roomCategory;
        this.roomType = roomType;
    }

    public HolidaySetPackageMerge(HolidaySetPackageMerge holidaySetPackageMerge) {
//        this.id = holidaySetPackageMerge.getId();
        this.productName = holidaySetPackageMerge.getProductName();
        this.productFlavourName = holidaySetPackageMerge.getProductFlavourName();
        this.city = holidaySetPackageMerge.getCity();
        this.noOfNights = holidaySetPackageMerge.getNoOfNights();
        this.checkInDate = holidaySetPackageMerge.getCheckInDate();
        this.roomCategory = holidaySetPackageMerge.getRoomCategory();
        this.roomType = holidaySetPackageMerge.getRoomType();
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductFlavourName() {
        return productFlavourName;
    }

    public void setProductFlavourName(String productFlavourName) {
        this.productFlavourName = productFlavourName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getNoOfNights() {
        return noOfNights;
    }

    public void setNoOfNights(Integer noOfNights) {
        this.noOfNights = noOfNights;
    }

    public Long getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Long checkInDate) {
        this.checkInDate = checkInDate;
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
}
