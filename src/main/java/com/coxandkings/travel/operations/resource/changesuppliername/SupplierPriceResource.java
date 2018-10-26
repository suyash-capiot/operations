package com.coxandkings.travel.operations.resource.changesuppliername;

import java.math.BigDecimal;

public class SupplierPriceResource {
    private BigDecimal margin;
    private BigDecimal totalSellingPrice;
    private BigDecimal netPayableToSupplier;

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public BigDecimal getTotalSellingPrice() {
        return totalSellingPrice;
    }

    public void setTotalSellingPrice(BigDecimal totalSellingPrice) {
        this.totalSellingPrice = totalSellingPrice;
    }

    public BigDecimal getNetPayableToSupplier() {
        return netPayableToSupplier;
    }

    public void setNetPayableToSupplier(BigDecimal netPayableToSupplier) {
        this.netPayableToSupplier = netPayableToSupplier;
    }
}
