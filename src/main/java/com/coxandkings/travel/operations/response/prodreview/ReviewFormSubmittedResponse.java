package com.coxandkings.travel.operations.response.prodreview;

import com.coxandkings.travel.operations.enums.prodreview.ProdReviewStatus;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;

public class ReviewFormSubmittedResponse {

    private Long aging;
    private String clientGroup;
    private String clientName;
    private String clientType;
    private String productCategory;
    private String productCategorySubType;
    private String productName;
    private ProdReviewStatus productReviewStatus;
    private String reviewId;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private ZonedDateTime submitDate;
    private String templateSubType;
    private String templateType;
    private String toDoTasks;

    public Long getAging() {
        return aging;
    }

    public void setAging(Long aging) {
        this.aging = aging;
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

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProdReviewStatus getProductReviewStatus() {
        return productReviewStatus;
    }

    public void setProductReviewStatus(ProdReviewStatus productReviewStatus) {
        this.productReviewStatus = productReviewStatus;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public ZonedDateTime getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(ZonedDateTime submitDate) {
        this.submitDate = submitDate;
    }

    public String getTemplateSubType() {
        return templateSubType;
    }

    public void setTemplateSubType(String templateSubType) {
        this.templateSubType = templateSubType;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getToDoTasks() {
        return toDoTasks;
    }

    public void setToDoTasks(String toDoTasks) {
        this.toDoTasks = toDoTasks;
    }
}
