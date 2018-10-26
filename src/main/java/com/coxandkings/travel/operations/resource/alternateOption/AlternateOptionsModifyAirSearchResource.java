package com.coxandkings.travel.operations.resource.alternateOption;

import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

public class AlternateOptionsModifyAirSearchResource {

  private String tripType;
  
  private List<JSONObject> originDestinationInfo;
  
  private List<JSONObject> paxInfo;
  
  private String cabinType;

  public String getTripType() {
    return tripType;
  }

  public void setTripType(String tripType) {
    this.tripType = tripType;
  }

  public List<JSONObject> getOriginDestinationInfo() {
    return originDestinationInfo;
  }

  public void setOriginDestinationInfo(List<JSONObject> originDestinationInfo) {
    this.originDestinationInfo = originDestinationInfo;
  }

  public List<JSONObject> getPaxInfo() {
    return paxInfo;
  }

  public void setPaxInfo(List<JSONObject> paxInfo) {
    this.paxInfo = paxInfo;
  }

  public String getCabinType() {
    return cabinType;
  }

  public void setCabinType(String cabinType) {
    this.cabinType = cabinType;
  }
  
  
}
