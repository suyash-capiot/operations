package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsUniqueProductSubCategory {

    @JsonProperty("productCategory")
    private String productCategory;

    @JsonProperty("productSubCategory")
    private String productSubCategory;

    public OpsUniqueProductSubCategory() {
    }

    public OpsUniqueProductSubCategory(String productCategory, String productSubCategory) {
        this.productCategory = productCategory;
        this.productSubCategory = productSubCategory;
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


    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.productCategory == null)? 0 :this.productCategory.hashCode()));
        result = ((result* 31)+((this.productSubCategory == null)? 0 :this.productSubCategory.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null)
            return false;
        if (obj instanceof OpsUniqueProductSubCategory){
            OpsUniqueProductSubCategory opsUniqueProductSubCategory = (OpsUniqueProductSubCategory) obj ;
            return this.getProductSubCategory().equals(opsUniqueProductSubCategory.getProductSubCategory())
                    && this.getProductCategory().equals(opsUniqueProductSubCategory.getProductCategory());
        }
        return false;
    }

    @Override
    public String toString() {
        return "ProductCategory : " + productCategory + " " +
                "ProductSubCateogory : " + productSubCategory;
    }
}
