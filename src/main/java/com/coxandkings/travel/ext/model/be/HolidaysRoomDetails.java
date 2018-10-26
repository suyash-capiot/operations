package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "roomTypeInfo"
})
public class HolidaysRoomDetails {

    @JsonProperty("hotelInfo")
    private HolidaysHotelDetails hotelInfo;

    @JsonProperty("roomTypeInfo")
    private RoomTypeInfo roomTypeInfo;

    @JsonProperty("ratePlanInfo")
    private RatePlanInfo ratePlanInfo;

    @JsonProperty("addressDetails")
    private AddressDetails addressDetails;

    @JsonProperty("timeSpan")
    private HolidaysTimeSpan timeSpan;

    public HolidaysHotelDetails getHotelInfo() {
        return hotelInfo;
    }

    public void setHotelInfo(HolidaysHotelDetails hotelInfo) {
        this.hotelInfo = hotelInfo;
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

    public AddressDetails getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(AddressDetails addressDetails) {
        this.addressDetails = addressDetails;
    }

    public HolidaysTimeSpan getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(HolidaysTimeSpan timeSpan) {
        this.timeSpan = timeSpan;
    }


}
