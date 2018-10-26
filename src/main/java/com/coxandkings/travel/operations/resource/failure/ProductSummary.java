package com.coxandkings.travel.operations.resource.failure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSummary {

    private String productSubCategory;

    private List<OrderDetailsResource> orders;

    public String getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public List<OrderDetailsResource> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDetailsResource> orders) {
        this.orders = orders;
    }
}
