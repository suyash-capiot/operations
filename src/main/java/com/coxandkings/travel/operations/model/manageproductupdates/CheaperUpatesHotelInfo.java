package com.coxandkings.travel.operations.model.manageproductupdates;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;

import java.time.ZonedDateTime;

public class CheaperUpatesHotelInfo {

    private String bookingID;

    private String orderID;

    private OpsProductSubCategory productSubCategory;

    private ZonedDateTime checkInDate;

    private ZonedDateTime checkOutDate;

    private String roomCategory;

    private String roomType;

    private String mealType;

    private String rating;

    public CheaperUpatesHotelInfo()   {};

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public OpsProductSubCategory getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(OpsProductSubCategory productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public ZonedDateTime getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(ZonedDateTime checkInDate) {
        this.checkInDate = checkInDate;
    }

    public ZonedDateTime getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(ZonedDateTime checkOutDate) {
        this.checkOutDate = checkOutDate;
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

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
