package com.coxandkings.travel.operations.criteria.prodreview;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemplateCriteria {
    private String companyID;
    private String templateType;
    private String subTypeForm;
    private String companyMarket;
    private String clientType;
    private String clientGroup;
    private String clientName;
    private String productCategory;
    private String productCategorySubType;
    private String bookingReferenceNumber;
    private String orderId;
    private String fromDate;
    private String toDate;

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getSubTypeForm() {
        return subTypeForm;
    }

    public void setSubTypeForm(String subTypeForm) {
        this.subTypeForm = subTypeForm;
    }

    public String getCompanyMarket() {
        return companyMarket;
    }

    public void setCompanyMarket(String companyMarket) {
        this.companyMarket = companyMarket;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientGroup() {
        return clientGroup;
    }

    public void setClientGroup(String clientGroup) {
        this.clientGroup = clientGroup;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductCategorySubType() {
        return productCategorySubType;
    }

    public void setProductCategorySubType(String productCategorySubType) {
        this.productCategorySubType = productCategorySubType;
    }

    public String getBookingReferenceNumber() {
        return bookingReferenceNumber;
    }

    public void setBookingReferenceNumber(String bookingReferenceNumber) {
        this.bookingReferenceNumber = bookingReferenceNumber;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
