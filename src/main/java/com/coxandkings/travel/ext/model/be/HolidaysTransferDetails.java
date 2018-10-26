package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({

        "transferId",
        "transferType",
        "transferInfo",
        "paxInfo",
        "clientCommercials",
        "supplierCommercials",
        "supplierPriceInfo",
        "totalPriceInfo"
})
public class HolidaysTransferDetails {

    @JsonProperty("transferId")
    private String transferId;

    @JsonProperty("transferType")
    private String transferType;

    @JsonProperty("transferInfo")
    private HolidaysTransferInfo transferInfo;

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
  private List<PaxTypeFare> paxTypeFares = new ArrayList<PaxTypeFare>();*/

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public HolidaysTransferInfo getTransferInfo() {
        return transferInfo;
    }

    public void setTransferInfo(HolidaysTransferInfo transferInfo) {
        this.transferInfo = transferInfo;
    }

    public List<PaxInfo> getPaxInfo() {
        return paxInfo;
    }

    public void setPaxInfo(List<PaxInfo> paxInfo) {
        this.paxInfo = paxInfo;
    }

    public List<OrderClientCommercial> getClientCommercials() {
        return clientEntityCommercials;
    }

    public void setClientCommercials(List<OrderClientCommercial> clientEntityCommercials) {
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

 /* public List<PaxTypeFare> getPaxTypeFares() {
    return paxTypeFares;
  }

  public void setPaxTypeFares(List<PaxTypeFare> paxTypeFares) {
    this.paxTypeFares = paxTypeFares;
  }

*/

}
