
package com.coxandkings.travel.operations.resource.changesuppliername.response.acco;

import java.util.List;

public class RoomStay {

    private String supplierRef;
    private Boolean payAtHotel;
    private TotalPriceInfo totalPriceInfo;
    private List<OccupancyInfo> occupancyInfo = null;
    private String accommodationSubType;
    private List<NightlyPriceInfo> nightlyPriceInfo = null;
    private RoomInfo roomInfo;

    public String getSupplierRef() {
        return supplierRef;
    }

    public void setSupplierRef(String supplierRef) {
        this.supplierRef = supplierRef;
    }

    public Boolean getPayAtHotel() {
        return payAtHotel;
    }

    public void setPayAtHotel(Boolean payAtHotel) {
        this.payAtHotel = payAtHotel;
    }

    public TotalPriceInfo getTotalPriceInfo() {
        return totalPriceInfo;
    }

    public void setTotalPriceInfo(TotalPriceInfo totalPriceInfo) {
        this.totalPriceInfo = totalPriceInfo;
    }

    public List<OccupancyInfo> getOccupancyInfo() {
        return occupancyInfo;
    }

    public void setOccupancyInfo(List<OccupancyInfo> occupancyInfo) {
        this.occupancyInfo = occupancyInfo;
    }

    public String getAccommodationSubType() {
        return accommodationSubType;
    }

    public void setAccommodationSubType(String accommodationSubType) {
        this.accommodationSubType = accommodationSubType;
    }

    public List<NightlyPriceInfo> getNightlyPriceInfo() {
        return nightlyPriceInfo;
    }

    public void setNightlyPriceInfo(List<NightlyPriceInfo> nightlyPriceInfo) {
        this.nightlyPriceInfo = nightlyPriceInfo;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }

}
