package com.coxandkings.travel.operations.resource.prodreview;

import com.coxandkings.travel.operations.model.prodreview.ProductReview;
import com.coxandkings.travel.operations.response.prodreview.ReviewFormSubmittedResponse;

import java.util.List;

public class SearchProductReviewResponse {

    private Integer numOfPages;
    private List<ReviewFormSubmittedResponse> reviewFormSubmittedResponses;
    private List<ProductReview> productReviews;

    public Integer getNumOfPages() {
        return numOfPages;
    }

    public void setNumOfPages(Integer numOfPages) {
        this.numOfPages = numOfPages;
    }

    public List<ReviewFormSubmittedResponse> getReviewFormSubmittedResponses() {
        return reviewFormSubmittedResponses;
    }

    public void setReviewFormSubmittedResponses(List<ReviewFormSubmittedResponse> reviewFormSubmittedResponses) {
        this.reviewFormSubmittedResponses = reviewFormSubmittedResponses;
    }

    public List<ProductReview> getProductReviews() {
        return productReviews;
    }

    public void setProductReviews(List<ProductReview> productReviews) {
        this.productReviews = productReviews;
    }
}
