package com.coxandkings.travel.operations.resource.refund.booking;

import java.io.Serializable;

public class ProductDetailsResource implements Serializable {
    private String id;
    private String bookingRefId;
    private String productId;
    private String productCategoryId;
    private String productSubCategoryId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(String productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductSubCategoryId() {
        return productSubCategoryId;
    }

    public void setProductSubCategoryId(String productSubCategoryId) {
        this.productSubCategoryId = productSubCategoryId;
    }
}
