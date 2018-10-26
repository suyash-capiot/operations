package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "HotelCode"
})
public class HolidaysHotelDetails {

    @JsonProperty("HotelSegmentCategoryCode")
    private String HotelSegmentCategoryCode;

    @JsonProperty("HotelCode")
    private String HotelCode;

    @JsonProperty("hotelName")
    private String hotelName;

    @JsonProperty("hotelRef")
    private String hotelRef;

    public String getHotelSegmentCategoryCode() {
        return HotelSegmentCategoryCode;
    }

    public void setHotelSegmentCategoryCode(String HotelSegmentCategoryCode) {
        this.HotelSegmentCategoryCode = HotelSegmentCategoryCode;
    }

    public String getHotelCode() {
        return HotelCode;
    }

    public void setHotelCode(String HotelCode) {
        this.HotelCode = HotelCode;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelRef() {
        return hotelRef;
    }

    public void setHotelRef(String hotelRef) {
        this.hotelRef = hotelRef;
    }


}
