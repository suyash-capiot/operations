package com.coxandkings.travel.operations.model.core;

import com.coxandkings.travel.ext.model.be.Discounts;
import com.coxandkings.travel.ext.model.be.Incentives;
import com.coxandkings.travel.ext.model.be.SpecialServiceRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsFlightTotalPriceInfo implements Serializable {

    @JsonProperty("baseFare")
    private OpsBaseFare baseFare;

    @JsonProperty("fees")
    private OpsFees fees;

    @JsonProperty("totalPrice")
    private String totalPrice;

    @JsonProperty("receivables")
    private OpsReceivables receivables;

    @JsonProperty("taxes")
    private OpsTaxes taxes;

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("companyTaxes")
    private OpsCompanyTaxes companyTaxes;

    @JsonProperty("paxTypeFares")
    private List<OpsPaxTypeFareFlightClient> paxTypeFares = new ArrayList<OpsPaxTypeFareFlightClient>();

    @JsonProperty("discounts")
    private Discounts discounts;
    
    @JsonProperty("incentives")
    private Incentives incentives;

    @JsonProperty("specialServiceRequests")
    private  List<SpecialServiceRequest> specialServiceRequests;


    public OpsFlightTotalPriceInfo() {
    }

    public OpsBaseFare getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(OpsBaseFare baseFare) {
        this.baseFare = baseFare;
    }

    public OpsFees getFees() {
        return fees;
    }

    public void setFees(OpsFees fees) {
        this.fees = fees;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OpsReceivables getReceivables() {
        return receivables;
    }

    public void setReceivables(OpsReceivables receivables) {
        this.receivables = receivables;
    }

    public OpsTaxes getTaxes() {
        return taxes;
    }

    public void setTaxes(OpsTaxes taxes) {
        this.taxes = taxes;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public List<OpsPaxTypeFareFlightClient> getPaxTypeFares() {
        return paxTypeFares;
    }

    public void setPaxTypeFares(List<OpsPaxTypeFareFlightClient> paxTypeFares) {
        this.paxTypeFares = paxTypeFares;
    }

    public Discounts getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Discounts discounts) {
        this.discounts = discounts;
    }

    public List<SpecialServiceRequest> getSpecialServiceRequests() {
        return specialServiceRequests;
    }

    public void setSpecialServiceRequests(List<SpecialServiceRequest> specialServiceRequests) {
        this.specialServiceRequests = specialServiceRequests;
    }

    public OpsCompanyTaxes getCompanyTaxes() {
        return companyTaxes;
    }

    public void setCompanyTaxes(OpsCompanyTaxes companyTaxes) {
        this.companyTaxes = companyTaxes;
    }

	public Incentives getIncentives() {
		return incentives;
	}

	public void setIncentives(Incentives incentives) {
		this.incentives = incentives;
	}
}

