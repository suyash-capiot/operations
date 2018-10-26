package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({

        "insuranceId",
        "insuranceType",
        "insCoverageDetail",
        "paxInfo",
        "clientCommercials",
        "supplierCommercials",
        "supplierPriceInfo",
        "totalPriceInfo"
})
public class HolidaysInsuranceDetails {

    @JsonProperty("insuranceId")
    private String insuranceId;

    @JsonProperty("insuranceType")
    private String insuranceType;

    @JsonProperty("insCoverageDetail")
    private HolidaysInsuranceInfo insCoverageDetail;

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

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public HolidaysInsuranceInfo getInsCoverageDetail() {
        return insCoverageDetail;
    }

    public void setInsCoverageDetail(HolidaysInsuranceInfo insCoverageDetail) {
        this.insCoverageDetail = insCoverageDetail;
    }

    public List<PaxInfo> getPaxInfo() {
        return paxInfo;
    }

    public void setPaxInfo(List<PaxInfo> paxInfo) {
        this.paxInfo = paxInfo;
    }

    public HolidaysTotalPriceInfo getTotalPriceInfo() {
        return totalPriceInfo;
    }

    public void setTotalPriceInfo(HolidaysTotalPriceInfo totalPriceInfo) {
        this.totalPriceInfo = totalPriceInfo;
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
  


  /*//Selling price Pax Type fares
  @JsonProperty("paxTypeFares")
  private List<PaxTypeFare> paxTypeFares = new ArrayList<PaxTypeFare>();*/


}
