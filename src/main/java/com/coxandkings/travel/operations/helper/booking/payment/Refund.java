package com.coxandkings.travel.operations.helper.booking.payment;

import com.coxandkings.travel.operations.helper.booking.product.Product;

import java.math.BigDecimal;

public class Refund {
    private String refundClaimId;
    private BigDecimal refundAmount;
    private String refundStatus;
    private Product product;
    private String currencyCode;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getRefundClaimId() {
        return refundClaimId;
    }

    public void setRefundClaimId(String refundClaimId) {
        this.refundClaimId = refundClaimId;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
