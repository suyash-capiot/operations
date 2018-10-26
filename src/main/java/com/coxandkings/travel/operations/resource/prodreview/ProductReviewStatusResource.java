package com.coxandkings.travel.operations.resource.prodreview;


import com.coxandkings.travel.operations.enums.prodreview.ProdReviewStatus;

public class ProductReviewStatusResource {
    private String reviewId;
    private ProdReviewStatus productReviewStatus;

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public ProdReviewStatus getProductReviewStatus() {
        return productReviewStatus;
    }

    public void setProductReviewStatus(ProdReviewStatus productReviewStatus) {
        this.productReviewStatus = productReviewStatus;
    }
}
