package com.coxandkings.travel.operations.helper.booking.passengers;

import java.math.BigDecimal;

public class PriceDetails {
    String id;
    private boolean refundable;
    private String supplierRateType;
    private BigDecimal actualMarginAmnt;
    private String currency;
    private SellingPriceDetails sellingPriceDetails;
    private SupplierPriceDetails supplierPriceDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isRefundable() {
        return refundable;
    }

    public void setRefundable(boolean refundable) {
        this.refundable = refundable;
    }

    public String getSupplierRateType() {
        return supplierRateType;
    }

    public void setSupplierRateType(String supplierRateType) {
        this.supplierRateType = supplierRateType;
    }

    public BigDecimal getActualMarginAmnt() {
        return actualMarginAmnt;
    }

    public void setActualMarginAmnt(BigDecimal actualMarginAmnt) {
        this.actualMarginAmnt = actualMarginAmnt;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public SellingPriceDetails getSellingPriceDetails() {
        return sellingPriceDetails;
    }

    public void setSellingPriceDetails(SellingPriceDetails sellingPriceDetails) {
        this.sellingPriceDetails = sellingPriceDetails;
    }

    public SupplierPriceDetails getSupplierPriceDetails() {
        return supplierPriceDetails;
    }

    public void setSupplierPriceDetails(SupplierPriceDetails supplierPriceDetails) {
        this.supplierPriceDetails = supplierPriceDetails;
    }
}
