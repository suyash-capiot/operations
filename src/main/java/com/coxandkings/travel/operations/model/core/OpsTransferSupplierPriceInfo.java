package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsTransferSupplierPriceInfo {
    @JsonProperty("supplierPrice")
    private String supplierPrice;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("paxTypeFares")
    private List<OpsPaxTypeFare> opsPaxTypeFare;


    public List<OpsPaxTypeFare> getOpsPaxTypeFare() {
        return opsPaxTypeFare;
    }

    public void setOpsPaxTypeFare(List<OpsPaxTypeFare> opsPaxTypeFare) {
        this.opsPaxTypeFare = opsPaxTypeFare;
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
