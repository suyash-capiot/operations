package com.coxandkings.travel.operations.response.prodreview;

import com.coxandkings.travel.operations.enums.prodreview.CustomerResponseFlg;
import com.coxandkings.travel.operations.enums.prodreview.ProdReviewStatus;
import com.coxandkings.travel.operations.model.prodreview.ProductReviewUser;
import com.coxandkings.travel.operations.response.ProductDetailsResponse;

import java.util.List;

public class ReviewResponse {

    private String id;
    private Float versionNumber;
    private String companyMarket;
    private String templateType;
    private String templateSubType;
    private String templateName;
    private String clientType;
    private String clientName;
    private String clientCategory;
    private String clientCategorySubType;
    private String passengerName;
    private ProdReviewStatus status;
    private Boolean customerAcceptanceRequired;
    private CustomerResponseFlg customerResponseFlg;
    private String bookingReferenceNumber;
    private Boolean notBookedWithUs;
    private ProductDetailsResponse productDetailsResponse;
    private List<ProductReviewUser> productReviewUsers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Float versionNumber) {
        this.versionNumber = versionNumber;
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

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public ProdReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ProdReviewStatus status) {
        this.status = status;
    }

    public String getBookingReferenceNumber() {
        return bookingReferenceNumber;
    }

    public void setBookingReferenceNumber(String bookingReferenceNumber) {
        this.bookingReferenceNumber = bookingReferenceNumber;
    }

    public Boolean getNotBookedWithUs() {
        return notBookedWithUs;
    }

    public void setNotBookedWithUs(Boolean notBookedWithUs) {
        this.notBookedWithUs = notBookedWithUs;
    }

    public ProductDetailsResponse getProductDetailsResponse() {
        return productDetailsResponse;
    }

    public void setProductDetailsResponse(ProductDetailsResponse productDetailsResponse) {
        this.productDetailsResponse = productDetailsResponse;
    }

    public String getClientCategory() {
        return clientCategory;
    }

    public void setClientCategory(String clientCategory) {
        this.clientCategory = clientCategory;
    }

    public String getClientCategorySubType() {
        return clientCategorySubType;
    }

    public void setClientCategorySubType(String clientCategorySubType) {
        this.clientCategorySubType = clientCategorySubType;
    }

    public List<ProductReviewUser> getProductReviewUsers() {
        return productReviewUsers;
    }

    public void setProductReviewUsers(List<ProductReviewUser> productReviewUsers) {
        this.productReviewUsers = productReviewUsers;
    }

    public Boolean getCustomerAcceptanceRequired() {
        return customerAcceptanceRequired;
    }

    public void setCustomerAcceptanceRequired(Boolean customerAcceptanceRequired) {
        this.customerAcceptanceRequired = customerAcceptanceRequired;
    }

    public CustomerResponseFlg getCustomerResponseFlg() {
        return customerResponseFlg;
    }

    public void setCustomerResponseFlg(CustomerResponseFlg customerResponseFlg) {
        this.customerResponseFlg = customerResponseFlg;
    }
}
