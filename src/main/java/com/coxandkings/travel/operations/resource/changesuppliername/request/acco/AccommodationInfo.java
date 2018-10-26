
package com.coxandkings.travel.operations.resource.changesuppliername.request.acco;

import java.util.List;

public class AccommodationInfo {

    private String accommodationSubType;
    private String supplierRef;
    private String countryCode;
    private String cityCode;
    private String hotelCode;
    private String checkIn;
    private String checkOut;
    private String paxNationality;
    private List<RoomConfig> roomConfig = null;

    public String getAccommodationSubType() {
        return accommodationSubType;
    }

    public void setAccommodationSubType(String accommodationSubType) {
        this.accommodationSubType = accommodationSubType;
    }

    public String getSupplierRef() {
        return supplierRef;
    }

    public void setSupplierRef(String supplierRef) {
        this.supplierRef = supplierRef;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
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

    public String getPaxNationality() {
        return paxNationality;
    }

    public void setPaxNationality(String paxNationality) {
        this.paxNationality = paxNationality;
    }

    public List<RoomConfig> getRoomConfig() {
        return roomConfig;
    }

    public void setRoomConfig(List<RoomConfig> roomConfig) {
        this.roomConfig = roomConfig;
    }

}
