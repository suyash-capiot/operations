package com.coxandkings.travel.operations.model.merge;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class AccommodationBookProduct extends BookProduct {

    @Column(name = "check_in_date")
    private String checkInDate;

    @Column(name = "check_out_date")
    private String checkOutDate;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "room_category")
    private String roomCategory;

    @Column(name = "product_name")
    private String hotelName;

    @Column(name = "supplier_name")
    private String supplierName; 
	
  
    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomCategory() {
        return roomCategory;
    }

    public void setRoomCategory(String roomCategory) {
        this.roomCategory = roomCategory;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public AccommodationBookProduct(String checkInDate, String checkOutDate, String roomType, String roomCategory,
                                    String hotelName, String supplierName) {
        super();
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomType = roomType;
        this.roomCategory = roomCategory;
        this.hotelName = hotelName;
        this.supplierName = supplierName;
    }

    public AccommodationBookProduct() {
        super();
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }


}
