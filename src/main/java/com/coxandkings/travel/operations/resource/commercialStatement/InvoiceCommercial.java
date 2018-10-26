package com.coxandkings.travel.operations.resource.commercialStatement;

import java.util.List;

public class InvoiceCommercial {
    private String invoiceNumber;
    private Integer version;
    private String productCategory;
    private String productName;
    private String productCategorySubType;
    private String productSubName;
    private String commercialType;
    private String invoiceType;
    private String invoiceStatus;
    private String createdOn;
    private String paymentDueDate;
    private String currency;
    private Double invoiceValue;
    private Double amountReceived;
    private String paymentStatus;
    private String statementID;
    private String commercialHead;
    private String supplierState;
    private String clientName;
    private String clientID;
    private String clientType;
    private List<BookingCommercial> bookingDetails;
    private String bu;

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategorySubType() {
        return productCategorySubType;
    }

    public void setProductCategorySubType(String productCategorySubType) {
        this.productCategorySubType = productCategorySubType;
    }

    public String getProductSubName() {
        return productSubName;
    }

    public void setProductSubName(String productSubName) {
        this.productSubName = productSubName;
    }

    public String getCommercialType() {
        return commercialType;
    }

    public void setCommercialType(String commercialType) {
        this.commercialType = commercialType;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(String paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(Double invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    public Double getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(Double amountReceived) {
        this.amountReceived = amountReceived;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getStatementID() {
        return statementID;
    }

    public void setStatementID(String statementID) {
        this.statementID = statementID;
    }

    public String getCommercialHead() {
        return commercialHead;
    }

    public void setCommercialHead(String commercialHead) {
        this.commercialHead = commercialHead;
    }

    public String getSupplierState() {
        return supplierState;
    }

    public void setSupplierState(String supplierState) {
        this.supplierState = supplierState;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public List<BookingCommercial> getBookingDetails() {
        return bookingDetails;
    }

    public void setBookingDetails(List<BookingCommercial> bookingDetails) {
        this.bookingDetails = bookingDetails;
    }

    public String getBu() {
        return bu;
    }

    public void setBu(String bu) {
        this.bu = bu;
    }
}
