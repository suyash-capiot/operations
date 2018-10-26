package com.coxandkings.travel.operations.criteria.productbookedthrother;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;

public class ProductBookedThrOtherCriteria {

    private OpsProductSubCategory productCategorySubTypeValue;
    private String bookingRefId;
    private String orderId;


    public OpsProductSubCategory getProductCategorySubTypeValue() {
        return productCategorySubTypeValue;
    }

    public void setProductCategorySubTypeValue(OpsProductSubCategory productCategorySubTypeValue) {
        this.productCategorySubTypeValue = productCategorySubTypeValue;
    }

    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
