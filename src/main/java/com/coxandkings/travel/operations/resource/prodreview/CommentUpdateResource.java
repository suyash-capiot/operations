package com.coxandkings.travel.operations.resource.prodreview;

import com.coxandkings.travel.operations.model.prodreview.ProductReviewUser;

import java.util.List;

public class CommentUpdateResource {

    private String id;
    private List<ProductReviewUser> productReviewUsers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ProductReviewUser> getProductReviewUsers() {
        return productReviewUsers;
    }

    public void setProductReviewUsers(List<ProductReviewUser> productReviewUsers) {
        this.productReviewUsers = productReviewUsers;
    }
}
