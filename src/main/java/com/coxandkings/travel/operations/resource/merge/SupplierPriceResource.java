package com.coxandkings.travel.operations.resource.merge;

import com.coxandkings.travel.operations.resource.changesuppliername.request.acco.RoomInfo;

import java.math.BigDecimal;

public class SupplierPriceResource {
    private String rateFor;
    private String rateDefinedFor;
    private String roomCategoryName;
    private String supplierRef;
    private Integer adultCount;
    private BigDecimal totalPrice;
    private String accommodationSubType;
    private String checkIn;
    private String checkOut;
    private String cityCode;
    private String countryCode;

    private RoomInfo roomInfo;

    public SupplierPriceResource() {
        this.rateFor = "Hotel";
        this.rateDefinedFor = "Per Night";
    }

    public SupplierPriceResource(String roomCategoryName, String supplierRef, BigDecimal totalPrice) {
        this.rateFor = "Hotel";
        this.rateDefinedFor = "Per Night";
        this.roomCategoryName = roomCategoryName;
        this.supplierRef = supplierRef;
        this.totalPrice = totalPrice;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getAccommodationSubType() {
        return accommodationSubType;
    }

    public void setAccommodationSubType(String accommodationSubType) {
        this.accommodationSubType = accommodationSubType;
    }

    public Integer getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(Integer adultCount) {
        this.adultCount = adultCount;
    }

    public String getSupplierRef() {
        return supplierRef;
    }

    public void setSupplierRef(String supplierRef) {
        this.supplierRef = supplierRef;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getRateFor() {
        return rateFor;
    }

    public void setRateFor(String rateFor) {
        this.rateFor = rateFor;
    }

    public String getRateDefinedFor() {
        return rateDefinedFor;
    }

    public void setRateDefinedFor(String rateDefinedFor) {
        this.rateDefinedFor = rateDefinedFor;
    }

    public String getRoomCategoryName() {
        return roomCategoryName;
    }

    public void setRoomCategoryName(String roomCategoryName) {
        this.roomCategoryName = roomCategoryName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }
}
