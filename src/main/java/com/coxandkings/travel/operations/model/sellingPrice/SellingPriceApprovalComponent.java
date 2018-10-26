package com.coxandkings.travel.operations.model.sellingPrice;

import com.coxandkings.travel.operations.resource.sellingPrice.SellingPrice;

public class SellingPriceApprovalComponent {
    private SellingPrice originalSellingPrice;
    private SellingPrice estimatedSellingPrice;
    private Discount discount;

    public SellingPrice getOriginalSellingPrice() {
        return originalSellingPrice;
    }

    public void setOriginalSellingPrice(SellingPrice originalSellingPrice) {
        this.originalSellingPrice = originalSellingPrice;
    }

    public SellingPrice getEstimatedSellingPrice() {
        return estimatedSellingPrice;
    }

    public void setEstimatedSellingPrice(SellingPrice estimatedSellingPrice) {
        this.estimatedSellingPrice = estimatedSellingPrice;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
}
