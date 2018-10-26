package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class OpsHolidaysProductPrice {
    @JsonProperty("productName")
    private String productName;
    @JsonProperty("productDetails")
    private List<OpsHolidaysProductDetails> productDetails;

    public OpsHolidaysProductPrice() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<OpsHolidaysProductDetails> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<OpsHolidaysProductDetails> productDetails) {
        this.productDetails = productDetails;
    }
}
