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
    "baseFare",
    "fees",
    "totalPrice",
    "receivables",
    "taxes",
    "currencyCode",
    "paxTypeFares"
})
public class OrderClientTotalPriceInfo {

    @JsonProperty("baseFare")
    private BaseFare baseFare;
    @JsonProperty("fees")
    private Fees fees;
    @JsonProperty("totalPrice")
    private String totalPrice;
    @JsonProperty("receivables")
    private Receivables receivables;
    @JsonProperty("taxes")
    private Taxes taxes;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("paxTypeFares")
    private List<OrderClientTotalPriceInfoPaxTypeFare> paxTypeFares = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("baseFare")
    public BaseFare getBaseFare() {
        return baseFare;
    }

    @JsonProperty("baseFare")
    public void setBaseFare(BaseFare baseFare) {
        this.baseFare = baseFare;
    }

    @JsonProperty("fees")
    public Fees getFees() {
        return fees;
    }

    @JsonProperty("fees")
    public void setFees(Fees fees) {
        this.fees = fees;
    }

    @JsonProperty("totalPrice")
    public String getTotalPrice() {
        return totalPrice;
    }

    @JsonProperty("totalPrice")
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    @JsonProperty("receivables")
    public Receivables getReceivables() {
        return receivables;
    }

    @JsonProperty("receivables")
    public void setReceivables(Receivables receivables) {
        this.receivables = receivables;
    }

    @JsonProperty("taxes")
    public Taxes getTaxes() {
        return taxes;
    }

    @JsonProperty("taxes")
    public void setTaxes(Taxes taxes) {
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

    @JsonProperty("paxTypeFares")
    public List<OrderClientTotalPriceInfoPaxTypeFare> getPaxTypeFares() {
        return paxTypeFares;
    }

    @JsonProperty("paxTypeFares")
    public void setPaxTypeFares(List<OrderClientTotalPriceInfoPaxTypeFare> paxTypeFares) {
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