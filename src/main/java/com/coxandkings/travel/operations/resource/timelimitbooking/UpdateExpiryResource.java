package com.coxandkings.travel.operations.resource.timelimitbooking;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "orderID",
        "expiryTimeLimit",
        "productSubCategory",
        "userID"
})
public class UpdateExpiryResource {

    @JsonProperty("orderID")
    private String orderID;
    @JsonProperty("expiryTimeLimit")
    private String expiryTimeLimit;
    @JsonProperty("productSubCategory")
    private String productSubCategory;
    @JsonProperty("userID")
    private String userID;

    @JsonProperty("orderID")
    public String getOrderID() {
        return orderID;
    }

    @JsonProperty("orderID")
    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    @JsonProperty("expiryTimeLimit")
    public String getExpiryTimeLimit() {
        return expiryTimeLimit;
    }

    @JsonProperty("expiryTimeLimit")
    public void setExpiryTimeLimit(String expiryTimeLimit) {
        this.expiryTimeLimit = expiryTimeLimit;
    }

    @JsonProperty("productSubCategory")
    public String getProductSubCategory() {
        return productSubCategory;
    }

    @JsonProperty("productSubCategory")
    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    @JsonProperty("userID")
    public String getUserID() {
        return userID;
    }

    @JsonProperty("userID")
    public void setUserID(String userID) {
        this.userID = userID;
    }
}
