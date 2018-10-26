package com.coxandkings.travel.operations.resource.productsharing;

import com.coxandkings.travel.operations.resource.BaseResource;

public class BookingResource extends BaseResource {

    String bookingReferenceId;
    String customerId;

    Boolean isProductSharing;

    String productName;
    String productNameSubType;
    String productFlavourName;
    long travelDateFrom;
    long travelDateTo;
    String roomType;
    String roomCategory;
    String genderPreference;
    String passengerName;
    int numberofDays;
    String city;
    String country;
    String productId;
    String customerMail;

    public String getCustomerMail() {
        return customerMail;
    }

    public void setCustomerMail(String customerMail) {
        this.customerMail = customerMail;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public int getNumberofDays() {
        return numberofDays;
    }

    public void setNumberofDays(int numberofDays) {
        this.numberofDays = numberofDays;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNameSubType() {
        return productNameSubType;
    }

    public void setProductNameSubType(String productNameSubType) {
        this.productNameSubType = productNameSubType;
    }

    public String getProductFlavourName() {
        return productFlavourName;
    }

    public void setProductFlavourName(String productFlavourName) {
        this.productFlavourName = productFlavourName;
    }

    public long getTravelDateFrom() {
        return travelDateFrom;
    }

    public void setTravelDateFrom(long travelDateFrom) {
        this.travelDateFrom = travelDateFrom;
    }

    public long getTravelDateTo() {
        return travelDateTo;
    }

    public void setTravelDateTo(long travelDateTo) {
        this.travelDateTo = travelDateTo;
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

    public String getGenderPreference() {
        return genderPreference;
    }

    public void setGenderPreference(String genderPreference) {
        this.genderPreference = genderPreference;
    }

    public String getBookingReferenceId() {
        return bookingReferenceId;
    }

    public void setBookingReferenceId(String bookingReferenceId) {
        this.bookingReferenceId = bookingReferenceId;
    }

    public Boolean getIsProductSharing() {
        return isProductSharing;
    }

    public void setIsProductSharing(Boolean productSharing) {
        isProductSharing = productSharing;
    }


}
