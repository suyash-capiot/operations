package com.coxandkings.travel.operations.resource.merge;

import com.coxandkings.travel.operations.resource.BaseResource;

public class ProductResource extends BaseResource {
    private String type;

    private String supplierId;
    private Boolean withinCancellation;

    //Shared Transfer
    private Long pickUpTime;
    private Long travelDate;
    private String pickupLocation;
    private Boolean airConditioned;
    //Accommodation
    private String roomCategory;
    private String roomType;
    private Long checkInDate;
    private String productname;
    //Activity - without transfer
    private String productNameSubType;
    // -with shred transfer
    private String vehicleCategory;
    //Holoday set package
    private String productFlavourName;
    private String city;
    private Integer noOfNights;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public Boolean getWithinCancellation() {
        return withinCancellation;
    }

    public void setWithinCancellation(Boolean withinCancellation) {
        this.withinCancellation = withinCancellation;
    }

    public Long getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(Long pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public Long getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Long travelDate) {
        this.travelDate = travelDate;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public Boolean getAirCondition() {
        return airConditioned;
    }

    public void setAirCondition(Boolean airCondition) {
        this.airConditioned = airCondition;
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

    public Long getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Long checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Boolean getAirConditioned() {
        return airConditioned;
    }

    public void setAirConditioned(Boolean airConditioned) {
        this.airConditioned = airConditioned;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductNameSubType() {
        return productNameSubType;
    }

    public void setProductNameSubType(String productNameSubType) {
        this.productNameSubType = productNameSubType;
    }

    public String getVehicleCategory() {
        return vehicleCategory;
    }

    public void setVehicleCategory(String vehicleCategory) {
        this.vehicleCategory = vehicleCategory;
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
}
