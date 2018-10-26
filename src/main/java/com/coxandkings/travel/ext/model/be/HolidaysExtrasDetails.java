package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({

        "extrasId",
        "extraType",
        "extrasInfo",
        "paxInfo",
        "clientCommercials",
        "supplierCommercials",
        "supplierPriceInfo",
        "totalPriceInfo"
})
public class HolidaysExtrasDetails {

    @JsonProperty("extrasId")
    private String extrasId;

    @JsonProperty("extraType")
    private String extraType;

    @JsonProperty("extrasInfo")
    private HolidaysExtrasInfo extrasInfo;

    @JsonProperty("paxInfo")
    private List<PaxInfo> paxInfo = new ArrayList<PaxInfo>();

    //Commercial Prices
    @JsonProperty("clientEntityCommercials")
    private List<OrderClientCommercial> clientEntityCommercials = new ArrayList<OrderClientCommercial>();

    @JsonProperty("supplierCommercials")
    private List<OrderSupplierCommercial> supplierCommercials = new ArrayList<OrderSupplierCommercial>();

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


    public String getExtrasId() {
        return extrasId;
    }

    public void setExtrasId(String extrasId) {
        this.extrasId = extrasId;
    }

    public String getExtraType() {
        return extraType;
    }

    public void setExtraType(String extraType) {
        this.extraType = extraType;
    }

    public HolidaysExtrasInfo getExtrasInfo() {
        return extrasInfo;
    }

    public void setExtrasInfo(HolidaysExtrasInfo extrasInfo) {
        this.extrasInfo = extrasInfo;
    }

    public List<PaxInfo> getPaxInfo() {
        return paxInfo;
    }

    public void setPaxInfo(List<PaxInfo> paxInfo) {
        this.paxInfo = paxInfo;
    }

    public List<OrderClientCommercial> getClientEntityCommercials() {
        return clientEntityCommercials;
    }

    public void setClientEntityCommercials(List<OrderClientCommercial> clientEntityCommercials) {
        this.clientEntityCommercials = clientEntityCommercials;
    }

    public List<OrderSupplierCommercial> getSupplierCommercials() {
        return supplierCommercials;
    }

    public void setSupplierCommercials(List<OrderSupplierCommercial> supplierCommercials) {
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
  

  

}
