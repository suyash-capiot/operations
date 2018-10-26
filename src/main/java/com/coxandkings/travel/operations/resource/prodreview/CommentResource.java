package com.coxandkings.travel.operations.resource.prodreview;

import com.coxandkings.travel.operations.enums.prodreview.ProdReviewStatus;
import com.coxandkings.travel.operations.model.prodreview.ProductReviewUser;

public class CommentResource {

    private String id;
    private ProductReviewUser productReviewUser;
    private ProdReviewStatus prodReviewStatus;
    private String userId;
    private Boolean customerAcceptance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProductReviewUser getProductReviewUser() {
        return productReviewUser;
    }

    public void setProductReviewUser(ProductReviewUser productReviewUser) {
        this.productReviewUser = productReviewUser;
    }

    public ProdReviewStatus getProdReviewStatus() {
        return prodReviewStatus;
    }

    public void setProdReviewStatus(ProdReviewStatus prodReviewStatus) {
        this.prodReviewStatus = prodReviewStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getCustomerAcceptance() {
        return customerAcceptance;
    }

    public void setCustomerAcceptance(Boolean customerAcceptance) {
        this.customerAcceptance = customerAcceptance;
    }
}
