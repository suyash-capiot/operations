
package com.coxandkings.travel.operations.resource.productsharing;

import java.io.Serializable;

public class AccommodationDetails implements Serializable {

    private String id;
    private String hotelName;
    private String hotelCategory;
    private String hotelSubCategory;
    private String hotelType;
    private String roomCapacity;
    private String country;
    private String city;
    private String checkInDate;
    private String checkOutDate;
    private String numberOfDays;
    private String numberOfNights;

    private final static long serialVersionUID = -8965261541842389824L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getHotelName() {

        return hotelName;
    }

    public void setHotelName(String hotelName) {

        this.hotelName = hotelName;
    }

    public AccommodationDetails withHotelName(String hotelName) {
        this.hotelName = hotelName;
        return this;
    }

    public String getHotelCategory()
    {
        return hotelCategory;
    }

    public void setHotelCategory(String hotelCategory) {
        this.hotelCategory = hotelCategory;
    }

    public AccommodationDetails withHotelCategory(String hotelcategory) {
        this.hotelCategory = hotelcategory;
        return this;
    }

    public String getHotelSubCategory() {
        return hotelSubCategory;
    }

    public void setHotelSubCategory(String hotelSubCategory) {
        this.hotelSubCategory = hotelSubCategory;
    }

    public AccommodationDetails withHotelSubCategory(String hotelSubCategory) {
        this.hotelSubCategory = hotelSubCategory;
        return this;
    }

    public String getHotelType() {
        return hotelType;
    }

    public void setHotelType(String hotelType) {
        this.hotelType = hotelType;
    }

    public AccommodationDetails withHotelType(String hotelType) {
        this.hotelType = hotelType;
        return this;
    }

    public String getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(String roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public AccommodationDetails withRoomCapacity(String roomCapacity) {
        this.roomCapacity = roomCapacity;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public AccommodationDetails withCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public AccommodationDetails withCity(String city) {
        this.city = city;
        return this;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public AccommodationDetails withCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
        return this;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public AccommodationDetails withCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
        return this;
    }

    public String getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(String numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public AccommodationDetails withNumberOfDays(String numberOfDays) {
        this.numberOfDays = numberOfDays;
        return this;
    }

    public String getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(String numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public AccommodationDetails withNumberOfNights(String numberOfNights) {
        this.numberOfNights = numberOfNights;
        return this;
    }

    @Override
    public String toString() {
        return "AccommodationDetails{" +
                "id='" + id + '\'' +
                ", hotelName='" + hotelName + '\'' +
                ", hotelCategory='" + hotelCategory + '\'' +
                ", hotelSubCategory='" + hotelSubCategory + '\'' +
                ", hotelType='" + hotelType + '\'' +
                ", roomCapacity='" + roomCapacity + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", checkInDate='" + checkInDate + '\'' +
                ", checkOutDate='" + checkOutDate + '\'' +
                ", numberOfDays='" + numberOfDays + '\'' +
                ", numberOfNights='" + numberOfNights + '\'' +
                '}';
    }
}
