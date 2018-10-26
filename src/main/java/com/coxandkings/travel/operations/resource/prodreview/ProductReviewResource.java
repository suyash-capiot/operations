package com.coxandkings.travel.operations.resource.prodreview;

import com.coxandkings.travel.operations.enums.prodreview.CustomerResponseFlg;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.coxandkings.travel.operations.model.prodreview.ProductReviewUser;
import com.coxandkings.travel.operations.model.prodreview.ReviewFormSubmitted;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateSerializer;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

public class ProductReviewResource {
    private String id;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime submitDate;
    private Boolean deleted;
    private String companyMarket;
    private String company;
    private String templateType;
    private String templateSubType;
    private String templateName;
    private String passengerName;
    private String pointOfSale;
    private String packageType;
    private String productCategory;
    private String productSubCategory;
    private String productName;
    private String orderID;
    private String clientType;
    private String clientCategory;
    private String clientSubCategory;
    private String clientGroup;
    private String clientName;
    private List<ProductReviewUser> productReviewUsers;
    private ReviewFormSubmitted reviewFormSubmitted;
    private CustomerResponseFlg customerResponseFlag;
    private String templateReferenceNo;
    private Boolean reviewCompleted;
    private String toDoTaskType;
    private String uniqueReferenceNumber;
    @NotNull(message = "Travel Date cannot be null")
    private ZonedDateTime travelDate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientCategory() {
        return clientCategory;
    }

    public void setClientCategory(String clientCategory) {
        this.clientCategory = clientCategory;
    }

    public String getClientSubCategory() {
        return clientSubCategory;
    }

    public void setClientSubCategory(String clientSubCategory) {
        this.clientSubCategory = clientSubCategory;
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

    public ZonedDateTime getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(ZonedDateTime submitDate) {
        this.submitDate = submitDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getUniqueReferenceNumber() {
        return uniqueReferenceNumber;
    }

    public void setUniqueReferenceNumber(String uniqueReferenceNumber) {
        this.uniqueReferenceNumber = uniqueReferenceNumber;
    }

    public String getCompanyMarket() {
        return companyMarket;
    }

    public void setCompanyMarket(String companyMarket) {
        this.companyMarket = companyMarket;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getTemplateSubType() {
        return templateSubType;
    }

    public void setTemplateSubType(String templateSubType) {
        this.templateSubType = templateSubType;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPointOfSale() {
        return pointOfSale;
    }

    public void setPointOfSale(String pointOfSale) {
        this.pointOfSale = pointOfSale;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public List<ProductReviewUser> getProductReviewUsers() {
        return productReviewUsers;
    }

    public void setProductReviewUsers(List<ProductReviewUser> productReviewUsers) {
        this.productReviewUsers = productReviewUsers;
    }

    public ReviewFormSubmitted getReviewFormSubmitted() {
        return reviewFormSubmitted;
    }

    public void setReviewFormSubmitted(ReviewFormSubmitted reviewFormSubmitted) {
        this.reviewFormSubmitted = reviewFormSubmitted;
    }

    public CustomerResponseFlg getCustomerResponseFlag() {
        return customerResponseFlag;
    }

    public void setCustomerResponseFlag(CustomerResponseFlg customerResponseFlag) {
        this.customerResponseFlag = customerResponseFlag;
    }

    public String getTemplateReferenceNo() {
        return templateReferenceNo;
    }

    public void setTemplateReferenceNo(String templateReferenceNo) {
        this.templateReferenceNo = templateReferenceNo;
    }

    public Boolean getReviewCompleted() {
        return reviewCompleted;
    }

    public void setReviewCompleted(Boolean reviewCompleted) {
        this.reviewCompleted = reviewCompleted;
    }

    public String getToDoTaskType() {
        return toDoTaskType;
    }

    public void setToDoTaskType(String toDoTaskType) {
        this.toDoTaskType = toDoTaskType;
    }

    public ZonedDateTime getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(ZonedDateTime travelDate) {
        this.travelDate = travelDate;
    }
}
