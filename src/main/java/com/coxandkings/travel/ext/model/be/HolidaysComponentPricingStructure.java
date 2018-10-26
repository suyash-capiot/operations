package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "sellingPriceBreakup",
        "supplierPriceBreakup"
})
public class HolidaysComponentPricingStructure {

    @JsonProperty("orderID")
    private String orderID;

    @JsonProperty("supplierPriceBreakup")
    private HolidaysComponentPriceBreakup supplierPriceBreakup;

    @JsonProperty("sellingPriceBreakup")
    private HolidaysComponentPriceBreakup sellingPriceBreakup;

    //Commercial Prices
    @JsonProperty("orderClientCommercials")
    private List<OrderClientCommercial> orderClientCommercials = new ArrayList<OrderClientCommercial>();

    @JsonProperty("orderSupplierCommercials")
    private List<OrderSupplierCommercial> orderSupplierCommercials = new ArrayList<OrderSupplierCommercial>();

    public HolidaysComponentPriceBreakup getSupplierPriceBreakup() {
        return supplierPriceBreakup;
    }

    public void setSupplierPriceBreakup(HolidaysComponentPriceBreakup supplierPriceBreakup) {
        this.supplierPriceBreakup = supplierPriceBreakup;
    }

    public HolidaysComponentPriceBreakup getSellingPriceBreakup() {
        return sellingPriceBreakup;
    }

    public void setSellingPriceBreakup(HolidaysComponentPriceBreakup sellingPriceBreakup) {
        this.sellingPriceBreakup = sellingPriceBreakup;
    }

    public List<OrderClientCommercial> getClientCommercials() {
        return orderClientCommercials;
    }

    public void setClientCommercials(List<OrderClientCommercial> orderClientCommercials) {
        this.orderClientCommercials = orderClientCommercials;
    }

    public List<OrderSupplierCommercial> getSupplierCommercials() {
        return orderSupplierCommercials;
    }

    public void setSupplierCommercials(List<OrderSupplierCommercial> orderSupplierCommercials) {
        this.orderSupplierCommercials = orderSupplierCommercials;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public List<OrderClientCommercial> getOrderClientCommercials() {
        return orderClientCommercials;
    }

    public void setOrderClientCommercials(List<OrderClientCommercial> orderClientCommercials) {
        this.orderClientCommercials = orderClientCommercials;
    }
  
  
  
}
