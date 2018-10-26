package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonProperty;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class OpsHolidaysTotalPriceInfo {
    @JsonProperty("baseFare")
    private OpsBaseFare opsBaseFare;
    @JsonProperty("fees")
    private OpsFees opsFees;
    @JsonProperty("totalPrice")
    private String totalPrice;
    @JsonProperty("receivables")
    private OpsReceivables opsReceivables;
    @JsonProperty("taxes")
    private OpsTaxes opsTaxes;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("paxTypeFares")
    private java.util.List<OpsPaxTypeFare> opsPaxTypeFare;

    public OpsHolidaysTotalPriceInfo() {
    }

    public OpsBaseFare getOpsBaseFare() {
        return opsBaseFare;
    }

    public void setOpsBaseFare(OpsBaseFare opsBaseFare) {
        this.opsBaseFare = opsBaseFare;
    }

    public OpsFees getOpsFees() {
        return opsFees;
    }

    public void setOpsFees(OpsFees opsFees) {
        this.opsFees = opsFees;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OpsReceivables getOpsReceivables() {
        return opsReceivables;
    }

    public void setOpsReceivables(OpsReceivables opsReceivables) {
        this.opsReceivables = opsReceivables;
    }

    public OpsTaxes getOpsTaxes() {
        return opsTaxes;
    }

    public void setOpsTaxes(OpsTaxes opsTaxes) {
        this.opsTaxes = opsTaxes;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public java.util.List<OpsPaxTypeFare> getOpsPaxTypeFare() {
        return opsPaxTypeFare;
    }

    public void setOpsPaxTypeFare(java.util.List<OpsPaxTypeFare> opsPaxTypeFare) {
        this.opsPaxTypeFare = opsPaxTypeFare;
    }
}
