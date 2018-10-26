
package com.coxandkings.travel.operations.resource.changesuppliername.request.acco;

import java.util.List;

public class RoomInfo {

    private MealInfo mealInfo;
    private HotelInfo hotelInfo;
    private List<Object> references = null;
    private RoomTypeInfo roomTypeInfo;
    private RatePlanInfo ratePlanInfo;
    private String availabilityStatus;
    private Integer requestedRoomIndex;

    public MealInfo getMealInfo() {
        return mealInfo;
    }

    public void setMealInfo(MealInfo mealInfo) {
        this.mealInfo = mealInfo;
    }

    public HotelInfo getHotelInfo() {
        return hotelInfo;
    }

    public void setHotelInfo(HotelInfo hotelInfo) {
        this.hotelInfo = hotelInfo;
    }

    public List<Object> getReferences() {
        return references;
    }

    public void setReferences(List<Object> references) {
        this.references = references;
    }

    public RoomTypeInfo getRoomTypeInfo() {
        return roomTypeInfo;
    }

    public void setRoomTypeInfo(RoomTypeInfo roomTypeInfo) {
        this.roomTypeInfo = roomTypeInfo;
    }

    public RatePlanInfo getRatePlanInfo() {
        return ratePlanInfo;
    }

    public void setRatePlanInfo(RatePlanInfo ratePlanInfo) {
        this.ratePlanInfo = ratePlanInfo;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public Integer getRequestedRoomIndex() {
        return requestedRoomIndex;
    }

    public void setRequestedRoomIndex(Integer requestedRoomIndex) {
        this.requestedRoomIndex = requestedRoomIndex;
    }

}
