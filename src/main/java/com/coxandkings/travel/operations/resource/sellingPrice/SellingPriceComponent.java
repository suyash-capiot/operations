package com.coxandkings.travel.operations.resource.sellingPrice;

import java.math.BigDecimal;

public class SellingPriceComponent {
    private BigDecimal amount;
    private String priceComponentCode;

    public SellingPriceComponent() {
    }

    public SellingPriceComponent(BigDecimal amount, String priceComponentCode) {
        this.amount = amount;
        this.priceComponentCode = priceComponentCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPriceComponentCode() {
        return priceComponentCode;
    }

    public void setPriceComponentCode(String priceComponentCode) {
        this.priceComponentCode = priceComponentCode;
    }
}
