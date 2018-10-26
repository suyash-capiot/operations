package com.coxandkings.travel.operations.helper.booking.product.accommodation;


import com.coxandkings.travel.operations.helper.booking.product.Product;

public class Accommodation extends Product {

    //this might be removed in future
    //private String accommodationName;
    private String accommodationRefNum;
    private String roomCategory;
    private String roomType;
    private Boolean roomWithChild;
    private Integer numberOfRooms;
    private Integer numberOfNights;

   /* public String getAccommodationName() {
        return accommodationName;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }
*/
    public String getAccommodationRefNum() {
        return accommodationRefNum;
    }

    public void setAccommodationRefNum(String accommodationRefNum) {
        this.accommodationRefNum = accommodationRefNum;
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

    public Boolean getRoomWithChild() {
        return roomWithChild;
    }

    public void setRoomWithChild(Boolean roomWithChild) {
        this.roomWithChild = roomWithChild;
    }

    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Integer getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(Integer numberOfNights) {
        this.numberOfNights = numberOfNights;
    }
}
