package com.coxandkings.travel.operations.resource.thirdpartyVoucher;

import java.util.List;

public class AssignVouchersToBooking {

    private String orderID;
    private List<String> voucherIDs;
    private String productSubCategory;
    private String userID;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public List<String> getVoucherIDs() {
        return voucherIDs;
    }

    public void setVoucherIDs(List<String> voucherIDs) {
        this.voucherIDs = voucherIDs;
    }

    public String getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
