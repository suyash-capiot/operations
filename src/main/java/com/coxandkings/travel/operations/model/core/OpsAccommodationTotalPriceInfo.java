package com.coxandkings.travel.operations.model.core;

import com.coxandkings.travel.ext.model.be.CompanyTaxes;
import com.coxandkings.travel.ext.model.be.Discounts;
import com.coxandkings.travel.ext.model.be.Incentives;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsAccommodationTotalPriceInfo  implements Serializable {

    @JsonProperty("totalPrice")
    private String totalPrice;

    @JsonProperty("taxes")
    private OpsTaxes opsTaxes;

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("companyTaxes")
    private OpsCompanyTaxes companyTaxes;

    @JsonProperty("discounts")
    private OpsDiscounts discounts;

    @JsonProperty("incentives")
    private OpsIncentives incentives;



    public OpsAccommodationTotalPriceInfo() {
    }

    public OpsIncentives getIncentives() {
        return incentives;
    }

    public void setIncentives(OpsIncentives incentives) {
        this.incentives = incentives;
    }

    public OpsCompanyTaxes getCompanyTaxes() {
        return companyTaxes;
    }

    public void setCompanyTaxes(OpsCompanyTaxes companyTaxes) {
        this.companyTaxes = companyTaxes;
    }

    public OpsDiscounts getDiscounts() {
        return discounts;
    }

    public void setDiscounts(OpsDiscounts discounts) {
        this.discounts = discounts;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
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
}
