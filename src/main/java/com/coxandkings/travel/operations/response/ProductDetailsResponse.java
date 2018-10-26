package com.coxandkings.travel.operations.response;

import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

public class ProductDetailsResponse {

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private ZonedDateTime travelDate;

    private String productCategory;
    private String productCategorySubType;
    private String productName;

    private QuestionRatingResponse overallRating;

    private String titleOfReview;
    private String yourReview;

    private QuestionResponse ratingQuestion;
    private QuestionResponse productTypeQuestions;
    private QuestionResponse productStyleQuestions;
    @NotNull
    public ZonedDateTime getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(ZonedDateTime travelDate) {
        this.travelDate = travelDate;
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

    public QuestionRatingResponse getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(QuestionRatingResponse overallRating) {
        this.overallRating = overallRating;
    }

    public String getTitleOfReview() {
        return titleOfReview;
    }

    public void setTitleOfReview(String titleOfReview) {
        this.titleOfReview = titleOfReview;
    }

    public String getYourReview() {
        return yourReview;
    }

    public void setYourReview(String yourReview) {
        this.yourReview = yourReview;
    }

    public QuestionResponse getRatingQuestion() {
        return ratingQuestion;
    }

    public void setRatingQuestion(QuestionResponse ratingQuestion) {
        this.ratingQuestion = ratingQuestion;
    }


    public QuestionResponse getProductTypeQuestions() {
        return productTypeQuestions;
    }

    public void setProductTypeQuestions(QuestionResponse productTypeQuestions) {
        this.productTypeQuestions = productTypeQuestions;
    }

    public QuestionResponse getProductStyleQuestions() {
        return productStyleQuestions;
    }

    public void setProductStyleQuestions(QuestionResponse productStyleQuestions) {
        this.productStyleQuestions = productStyleQuestions;
    }
}
