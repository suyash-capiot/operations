package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsRoomSuppPriceInfo implements Serializable {

    @JsonProperty("roomSupplierPrice")
    private String roomSupplierPrice;
    @JsonProperty("taxes")
    private OpsTaxes taxes;
    @JsonProperty("currencyCode")
    private String currencyCode;

    public OpsRoomSuppPriceInfo() {
    }

    @JsonProperty("roomSupplierPrice")
    public String getRoomSupplierPrice() {
        return roomSupplierPrice;
    }

    @JsonProperty("roomSupplierPrice")
    public void setRoomSupplierPrice(String roomSupplierPrice) {
        this.roomSupplierPrice = roomSupplierPrice;
    }

    @JsonProperty("taxes")
    public OpsTaxes getTaxes() {
        return taxes;
    }

    @JsonProperty("taxes")
    public void setTaxes(OpsTaxes taxes) {
        this.taxes = taxes;
    }

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsRoomSuppPriceInfo that = (OpsRoomSuppPriceInfo) o;
        return Objects.equals(roomSupplierPrice, that.roomSupplierPrice) &&
                Objects.equals(taxes, that.taxes) &&
                Objects.equals(currencyCode, that.currencyCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(roomSupplierPrice, taxes, currencyCode);
    }
}
