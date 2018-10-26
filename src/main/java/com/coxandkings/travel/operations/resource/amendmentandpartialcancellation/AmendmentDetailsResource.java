package com.coxandkings.travel.operations.resource.amendmentandpartialcancellation;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class AmendmentDetailsResource implements Serializable {
    private static final long serialVersionUID = -6554832190614329489L;

    @NotEmpty(message = "User ID should not be Empty.")
    private String userID;

    @NotNull(message = "Client ID should not be Empty.")
    private String clientID;

    @NotNull(message = "Booking reference number should not be Empty.")
    private String bookingRefNo;

    @NotEmpty(message = "Order ID should not be Empty.")
    private String orderID;

    @NotEmpty(message = "Supplier ID should not be Empty.")
    private String supplierID;

    @NotEmpty(message = "AbstractProductFactory Name should not be Empty.")
    private String productName;

    @NotEmpty(message = "AbstractProductFactory Category should not be Empty.")
    private String productCategory;

    @NotEmpty(message = "AbstractProductFactory Sub Category should not be Empty.")
    private String productSubCategory;

    @NotEmpty(message = "Action type should not be Empty.")
    private String actionType;

    @NotEmpty(message = "Unique ID should not be Empty.")
    private String uniqueID;

    @NotNull(message = "Old value should not be Empty.")
    @Min(value = 0,message = "Old value should not be negative.")
    private Double oldValue;

    @NotNull(message = "New value should not be Empty.")
    @Min(value = 0,message = "New value should not be negative.")
    private Double newValue;

    private String amendCancID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getBookingRefNo() {
        return bookingRefNo;
    }

    public void setBookingRefNo(String bookingRefNo) {
        this.bookingRefNo = bookingRefNo;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public Double getOldValue() {
        return oldValue;
    }

    public void setOldValue(Double oldValue) {
        this.oldValue = oldValue;
    }

    public Double getNewValue() {
        return newValue;
    }

    public void setNewValue(Double newValue) {
        this.newValue = newValue;
    }

    public String getAmendCancID() {
        return amendCancID;
    }

    public void setAmendCancID(String amendCancID) {
        this.amendCancID = amendCancID;
    }
}
