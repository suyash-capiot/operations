package com.coxandkings.travel.operations.resource.manageofflinebooking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "supplierPrice",
    "currencyCode",
    "paxTypeFares"
})
public class OrderSupplierPriceInfo {

    @JsonProperty("supplierPrice")
    private String supplierPrice;

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("paxTypeFares")
    private List<OrderSupplierPriceInfoPaxTypeFare> paxTypeFares = null;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("supplierPrice")
    public String getSupplierPrice() {
        return supplierPrice;
    }

    @JsonProperty("supplierPrice")
    public void setSupplierPrice(String supplierPrice) {
        this.supplierPrice = supplierPrice;
    }

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @JsonProperty("paxTypeFares")
    public List<OrderSupplierPriceInfoPaxTypeFare> getPaxTypeFares() {
        return paxTypeFares;
    }

    @JsonProperty("paxTypeFares")
    public void setPaxTypeFares(List<OrderSupplierPriceInfoPaxTypeFare> paxTypeFares) {
        this.paxTypeFares = paxTypeFares;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
