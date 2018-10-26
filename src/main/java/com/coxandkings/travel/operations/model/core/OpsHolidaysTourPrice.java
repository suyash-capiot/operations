package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class OpsHolidaysTourPrice {
    @JsonProperty("totalPackagePrice")
    private String totalPackagePrice;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("productPrice")
    private List<OpsHolidaysProductPrice> productPrice;

    public OpsHolidaysTourPrice() {
    }

    public String getTotalPackagePrice() {
        return totalPackagePrice;
    }

    public void setTotalPackagePrice(String totalPackagePrice) {
        this.totalPackagePrice = totalPackagePrice;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public List<OpsHolidaysProductPrice> getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(List<OpsHolidaysProductPrice> productPrice) {
        this.productPrice = productPrice;
    }
}
