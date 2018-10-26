package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsFlightSupplierPriceInfo implements Serializable {

    //TODO : Inform BE Supplier price should not be String
    @JsonProperty("supplierPrice")
    private String supplierPrice;

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("paxTypeFares")
    private List<OpsPaxTypeFareFlightSupplier> paxTypeFares = new ArrayList<OpsPaxTypeFareFlightSupplier>();

    private final static long serialVersionUID = -632343077136117663L;

    public OpsFlightSupplierPriceInfo() {
    }

    public List<OpsPaxTypeFareFlightSupplier> getPaxTypeFares() {
        return paxTypeFares;
    }

    public void setPaxTypeFares(List<OpsPaxTypeFareFlightSupplier> paxTypeFares) {
        this.paxTypeFares = paxTypeFares;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSupplierPrice() {
        return supplierPrice;
    }

    public void setSupplierPrice(String supplierPrice) {
        this.supplierPrice = supplierPrice;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
