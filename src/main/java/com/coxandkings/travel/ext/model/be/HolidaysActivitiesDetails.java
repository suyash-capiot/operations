package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({

        "activityId",
        "activityType",
        "activityInfo",
        "paxInfo",
        "clientCommercials",
        "supplierCommercials",
        "supplierPriceInfo",
        "totalPriceInfo"
})
public class HolidaysActivitiesDetails {

    @JsonProperty("activityId")
    private String activityId;

    @JsonProperty("activityType")
    private String activityType;

    @JsonProperty("activityInfo")
    private HolidaysActivitiesInfo activityInfo;

    @JsonProperty("paxInfo")
    private List<PaxInfo> paxInfo = new ArrayList<PaxInfo>();

    //Commercial Prices
    @JsonProperty("clientEntityCommercials")
    private List<OrderClientCommercial> orderClientCommercials = new ArrayList<OrderClientCommercial>();

    @JsonProperty("supplierCommercials")
    private List<OrderSupplierCommercial> orderSupplierCommercials = new ArrayList<OrderSupplierCommercial>();

    //Supplier Price
    @JsonProperty("supplierPriceInfo")
    private HolidaysSupplierPriceInfo supplierPriceInfo;

    //Total Price
    @JsonProperty("totalPriceInfo")
    private HolidaysTotalPriceInfo totalPriceInfo;
  
  /*//Selling price Pax Type fares
  @JsonProperty("paxTypeFares")
  private List<PaxTypeFare> paxTypeFares = new ArrayList<PaxTypeFare>();
*/


    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public HolidaysActivitiesInfo getActivityInfo() {
        return activityInfo;
    }

    public void setActivityInfo(HolidaysActivitiesInfo activityInfo) {
        this.activityInfo = activityInfo;
    }

    public List<PaxInfo> getPaxInfo() {
        return paxInfo;
    }

    public void setPaxInfo(List<PaxInfo> paxInfo) {
        this.paxInfo = paxInfo;
    }

    public List<OrderClientCommercial> getOrderClientCommercials() {
        return orderClientCommercials;
    }

    public void setOrderClientCommercials(List<OrderClientCommercial> orderClientCommercials) {
        this.orderClientCommercials = orderClientCommercials;
    }

    public List<OrderSupplierCommercial> getOrderSupplierCommercials() {
        return orderSupplierCommercials;
    }

    public void setOrderSupplierCommercials(List<OrderSupplierCommercial> orderSupplierCommercials) {
        this.orderSupplierCommercials = orderSupplierCommercials;
    }

    public HolidaysSupplierPriceInfo getSupplierPriceInfo() {
        return supplierPriceInfo;
    }

    public void setSupplierPriceInfo(HolidaysSupplierPriceInfo supplierPriceInfo) {
        this.supplierPriceInfo = supplierPriceInfo;
    }

    public HolidaysTotalPriceInfo getTotalPriceInfo() {
        return totalPriceInfo;
    }

    public void setTotalPriceInfo(HolidaysTotalPriceInfo totalPriceInfo) {
        this.totalPriceInfo = totalPriceInfo;
    }
  

  
  
}
