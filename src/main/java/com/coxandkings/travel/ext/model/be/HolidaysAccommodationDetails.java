package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({

        "accoID",
        "accomodationType",
        "roomInfo",
        "paxInfo",
        "clientCommercials",
        "supplierCommercials",
        "supplierPriceInfo",
        "totalPriceInfo"
})
public class HolidaysAccommodationDetails {


    @JsonProperty("accoID")
    private String accoID;

    @JsonProperty("roomID")
    private String roomID;

    @JsonProperty("accomodationType")
    private String accomodationType;

    @JsonProperty("roomInfo")
    private HolidaysRoomDetails roomInfo;

    @JsonProperty("paxInfo")
    private List<PaxInfo> paxInfo = new ArrayList<PaxInfo>();

    //Commercial Prices
    @JsonProperty("clientEntityCommercials")
    private List<ClientEntityCommercial> clientEntityCommercials = new ArrayList<ClientEntityCommercial>();

    @JsonProperty("supplierCommercials")
    private List<SupplierCommercial> supplierCommercials = new ArrayList<SupplierCommercial>();

    //Supplier Price
    @JsonProperty("supplierPriceInfo")
    private HolidaysSupplierPriceInfo supplierPriceInfo;

    //Total Price
    @JsonProperty("totalPriceInfo")
    private HolidaysTotalPriceInfo totalPriceInfo;
  
  /*//Selling price Pax Type fares
  @JsonProperty("paxTypeFares")
  private List<PaxTypeFare> paxTypeFares = new ArrayList<PaxTypeFare>();*/

    public String getAccoID() {
        return accoID;
    }

    public void setAccoID(String accoID) {
        this.accoID = accoID;
    }

    public String getAccomodationType() {
        return accomodationType;
    }

    public void setAccomodationType(String accomodationType) {
        this.accomodationType = accomodationType;
    }

    public HolidaysRoomDetails getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(HolidaysRoomDetails roomInfo) {
        this.roomInfo = roomInfo;
    }

    public List<PaxInfo> getPaxInfo() {
        return paxInfo;
    }

    public void setPaxInfo(List<PaxInfo> paxInfo) {
        this.paxInfo = paxInfo;
    }

    public List<ClientEntityCommercial> getClientCommercials() {
        return clientEntityCommercials;
    }

    public void setClientCommercials(List<ClientEntityCommercial> clientEntityCommercials) {
        this.clientEntityCommercials = clientEntityCommercials;
    }

    public List<SupplierCommercial> getSupplierCommercials() {
        return supplierCommercials;
    }

    public void setSupplierCommercials(List<SupplierCommercial> supplierCommercials) {
        this.supplierCommercials = supplierCommercials;
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

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

/*  public List<PaxTypeFare> getPaxTypeFares() {
    return paxTypeFares;
  }

  public void setPaxTypeFares(List<PaxTypeFare> paxTypeFares) {
    this.paxTypeFares = paxTypeFares;
  }
  */


}
