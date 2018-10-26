package com.coxandkings.travel.operations.resource.managedocumentation;

import java.util.List;

public class OrderDocumentsRequest {
    private String productCategory;
    private String productSubCategory;
    private String orderID;
    private List<BookingDocumentDetailsResource> orderDocumentIDs;
    private String userID;

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

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public List<BookingDocumentDetailsResource> getOrderDocumentIDs() {
        return orderDocumentIDs;
    }

    public void setOrderDocumentIDs(List<BookingDocumentDetailsResource> orderDocumentIDs) {
        this.orderDocumentIDs = orderDocumentIDs;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
