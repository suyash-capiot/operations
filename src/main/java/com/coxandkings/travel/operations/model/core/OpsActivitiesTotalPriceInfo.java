package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

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
public class OpsActivitiesTotalPriceInfo {
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
    private List<OpsPaxTypeFare> opsPaxTypeFare;
	
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

    public List<OpsPaxTypeFare> getOpsPaxTypeFare() {
        return opsPaxTypeFare;
    }

    public void setOpsPaxTypeFare(List<OpsPaxTypeFare> opsPaxTypeFare) {
        this.opsPaxTypeFare = opsPaxTypeFare;
    }


}
