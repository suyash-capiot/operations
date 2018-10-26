package com.coxandkings.travel.operations.resource.sellingPrice;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SellingPriceTable {
    private BigDecimal totalSellingPrice;
    private BigDecimal totalSupplierPrice;
    private BigDecimal margin;

    public BigDecimal getTotalSellingPrice() {
        return totalSellingPrice;
    }

    public void setTotalSellingPrice(BigDecimal totalSellingPrice) {
        this.totalSellingPrice = totalSellingPrice;
    }

    public BigDecimal getTotalSupplierPrice() {
        return totalSupplierPrice;
    }

    public void setTotalSupplierPrice(BigDecimal totalSupplierPrice) {
        this.totalSupplierPrice = totalSupplierPrice;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }
}
