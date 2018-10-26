package com.coxandkings.travel.operations.response.prodreview;

import com.coxandkings.travel.operations.model.prodreview.ProductReview;

import java.util.List;

public class ProductReviewResposneClient {
    List<ProductReview> productReviewList;

    int numOfPages;

    public List<ProductReview> getProductReviewList() {
        return productReviewList;
    }

    public void setProductReviewList(List<ProductReview> productReviewList) {
        this.productReviewList = productReviewList;
    }

    public int getNumOfPages() {
        return numOfPages;
    }

    public void setNumOfPages(int numOfPages) {
        this.numOfPages = numOfPages;
    }
}
