package com.coxandkings.travel.operations.model.modifiedFileProfitabiliy;

public class FileProfReqBean {
    private String bookingRefId;
    private String productCategory;
    private String productSubCategory;
    private String requesType;


    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
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

    public String getRequesType() {
        return requesType;
    }

    public void setRequesType(String requesType) {
        this.requesType = requesType;
    }
}
