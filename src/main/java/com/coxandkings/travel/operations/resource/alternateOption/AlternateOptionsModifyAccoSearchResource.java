package com.coxandkings.travel.operations.resource.alternateOption;

import java.util.List;
import org.json.JSONObject;

public class AlternateOptionsModifyAccoSearchResource {

  private String paxNationality;
  
  private String checkIn;
  
  private String checkOut;
  
  private String countryCode;
  
  private String cityCode;
  
  private String hotelCode;
  
  private List<JSONObject> roomConfig;
  
  private List<String> accommodationSubTypes;

  public String getPaxNationality() {
    return paxNationality;
  }

  public void setPaxNationality(String paxNationality) {
    this.paxNationality = paxNationality;
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

  public List<JSONObject> getRoomConfig() {
    return roomConfig;
  }

  public void setRoomConfig(List<JSONObject> roomConfig) {
    this.roomConfig = roomConfig;
  }

  public List<String> getAccommodationSubTypes() {
    return accommodationSubTypes;
  }

  public void setAccommodationSubTypes(List<String> accommodationSubTypes) {
    this.accommodationSubTypes = accommodationSubTypes;
  }

  
  
  
}
