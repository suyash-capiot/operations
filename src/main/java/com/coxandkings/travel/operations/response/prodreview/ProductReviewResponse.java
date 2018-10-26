package com.coxandkings.travel.operations.response.prodreview;

import com.coxandkings.travel.operations.enums.prodreview.ProdReviewStatus;
import com.coxandkings.travel.operations.model.prodreview.*;

import java.time.ZonedDateTime;
import java.util.List;

public class ProductReviewResponse {
    private String id;

    private String companyMarket;

    private String templateType;

    private String templateSubType;

    private String templateName;

    private String passengerName;

    private ProdReviewStatus productReviewStatus;

    private String aging;

    private String pointOfSale;

    private List<RatingAnswer> ratingAnswerList;

    private List<ProductRelatedQuestionAnswer> productRelatedQuestionAnswerList;

    private List<ProductStyleQuestionAnswer> productStyleQuestionAnswerList;

    private ReviewFormSubmitted reviewFormSubmitted;

    private String toDoTaskType;

    private List<ProductReviewUser> productReviewUsers;

    private ZonedDateTime submitDate;
    private Boolean deleted;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAging() {
        return aging;
    }

    public void setAging(String aging) {
        this.aging = aging;
    }

    public List<ProductRelatedQuestionAnswer> getProductRelatedQuestionAnswerList() {
        return productRelatedQuestionAnswerList;
    }

    public void setProductRelatedQuestionAnswerList(List<ProductRelatedQuestionAnswer> productRelatedQuestionAnswerList) {
        this.productRelatedQuestionAnswerList = productRelatedQuestionAnswerList;
    }

    public ProdReviewStatus getProductReviewStatus() {
        return productReviewStatus;
    }

    public void setProductReviewStatus(ProdReviewStatus productReviewStatus) {
        this.productReviewStatus = productReviewStatus;
    }

    public List<RatingAnswer> getRatingAnswerList() {
        return ratingAnswerList;
    }

    public void setRatingAnswerList(List<RatingAnswer> ratingAnswerList) {
        this.ratingAnswerList = ratingAnswerList;
    }

    public String getPointOfSale() {
        return pointOfSale;
    }

    public void setPointOfSale(String pointOfSale) {
        this.pointOfSale = pointOfSale;
    }

    public List<ProductStyleQuestionAnswer> getProductStyleQuestionAnswerList() {
        return productStyleQuestionAnswerList;
    }

    public void setProductStyleQuestionAnswerList(List<ProductStyleQuestionAnswer> productStyleQuestionAnswerList) {
        this.productStyleQuestionAnswerList = productStyleQuestionAnswerList;
    }

    public ReviewFormSubmitted getReviewFormSubmitted() {
        return reviewFormSubmitted;
    }

    public void setReviewFormSubmitted(ReviewFormSubmitted reviewFormSubmitted) {
        this.reviewFormSubmitted = reviewFormSubmitted;
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

    public String getToDoTaskType() {
        return toDoTaskType;
    }

    public void setToDoTaskType(String toDoTaskType) {
        this.toDoTaskType = toDoTaskType;
    }

    public List<ProductReviewUser> getProductReviewUsers() {
        return productReviewUsers;
    }

    public void setProductReviewUsers(List<ProductReviewUser> productReviewUsers) {
        this.productReviewUsers = productReviewUsers;
    }
}
