
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "rooms",
    "countryCode",
    "cityCode",
    "hotelCode",
    "hotelName"
})
public class HotelDetails implements Serializable
{

    @JsonProperty("rooms")
    private List<Room> rooms = new ArrayList<Room>();
    @JsonProperty("countryCode")
    private String countryCode;
    @JsonProperty("cityCode")
    private String cityCode;
    @JsonProperty("hotelCode")
    private String hotelCode;
    @JsonProperty("hotelName")
    private String hotelName;
    private final static long serialVersionUID = 6233438655029810725L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public HotelDetails() {
    }

    /**
     * 
     * @param rooms
     * @param countryCode
     * @param cityCode
     * @param hotelCode
     * @param hotelName
     */
    public HotelDetails(List<Room> rooms, String countryCode, String cityCode, String hotelCode, String hotelName) {
        super();
        this.rooms = rooms;
        this.countryCode = countryCode;
        this.cityCode = cityCode;
        this.hotelCode = hotelCode;
        this.hotelName = hotelName;
    }

    @JsonProperty("rooms")
    public List<Room> getRooms() {
        return rooms;
    }

    @JsonProperty("rooms")
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @JsonProperty("countryCode")
    public String getCountryCode() {
        return countryCode;
    }

    @JsonProperty("countryCode")
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @JsonProperty("cityCode")
    public String getCityCode() {
        return cityCode;
    }

    @JsonProperty("cityCode")
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @JsonProperty("hotelCode")
    public String getHotelCode() {
        return hotelCode;
    }

    @JsonProperty("hotelCode")
    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
    }

    @JsonProperty("hotelName")
    public String getHotelName() {
        return hotelName;
    }

    @JsonProperty("hotelName")
    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(HotelDetails.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("rooms");
        sb.append('=');
        sb.append(((this.rooms == null)?"<null>":this.rooms));
        sb.append(',');
        sb.append("countryCode");
        sb.append('=');
        sb.append(((this.countryCode == null)?"<null>":this.countryCode));
        sb.append(',');
        sb.append("cityCode");
        sb.append('=');
        sb.append(((this.cityCode == null)?"<null>":this.cityCode));
        sb.append(',');
        sb.append("hotelCode");
        sb.append('=');
        sb.append(((this.hotelCode == null)?"<null>":this.hotelCode));
        sb.append(',');
        sb.append("hotelName");
        sb.append('=');
        sb.append(((this.hotelName == null)?"<null>":this.hotelName));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.rooms == null)? 0 :this.rooms.hashCode()));
        result = ((result* 31)+((this.hotelCode == null)? 0 :this.hotelCode.hashCode()));
        result = ((result* 31)+((this.hotelName == null)? 0 :this.hotelName.hashCode()));
        result = ((result* 31)+((this.countryCode == null)? 0 :this.countryCode.hashCode()));
        result = ((result* 31)+((this.cityCode == null)? 0 :this.cityCode.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof HotelDetails) == false) {
            return false;
        }
        HotelDetails rhs = ((HotelDetails) other);
        return ((((((this.rooms == rhs.rooms)||((this.rooms!= null)&&this.rooms.equals(rhs.rooms)))&&((this.hotelCode == rhs.hotelCode)||((this.hotelCode!= null)&&this.hotelCode.equals(rhs.hotelCode))))&&((this.hotelName == rhs.hotelName)||((this.hotelName!= null)&&this.hotelName.equals(rhs.hotelName))))&&((this.countryCode == rhs.countryCode)||((this.countryCode!= null)&&this.countryCode.equals(rhs.countryCode))))&&((this.cityCode == rhs.cityCode)||((this.cityCode!= null)&&this.cityCode.equals(rhs.cityCode))));
    }

}
