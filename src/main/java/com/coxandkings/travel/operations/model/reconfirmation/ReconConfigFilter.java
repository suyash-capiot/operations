package com.coxandkings.travel.operations.model.reconfirmation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReconConfigFilter {

    @JsonProperty("productDetails.configurationFor")
    private String configurationFor;

    @JsonProperty("productDetails.productCategory")
    private String productCategory;

    @JsonProperty("productDetails.productCatSubtype")
    private String productSubCategory;

    public String getConfigurationFor() {
        return configurationFor;
    }

    public void setConfigurationFor(String configurationFor) {
        this.configurationFor = configurationFor;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }
}

