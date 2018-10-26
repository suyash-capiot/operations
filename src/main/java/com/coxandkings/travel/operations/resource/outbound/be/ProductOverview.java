package com.coxandkings.travel.operations.resource.outbound.be;

import com.coxandkings.travel.operations.model.core.OpsProduct;

public class ProductOverview {
    private String productId;
    private String productCategory;
    private String productSubCategory;

    public ProductOverview() {
    }

    public ProductOverview(OpsProduct product) {
        this.productId = product.getOrderID();
        this.productCategory = product.getProductCategory();
        this.productSubCategory = product.getProductSubCategory();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
