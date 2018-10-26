package com.coxandkings.travel.operations.resource.amendentitycommercial;

import java.math.BigDecimal;

public class MarginDetails {

    private BigDecimal netMargin;
    private BigDecimal netSupplierCost;
    private BigDecimal netSellingPrice;
    private BigDecimal clientCommercialPayables;
    private BigDecimal clientCommercialsReveivables;
    private BigDecimal supplierCommercialPayables;
    private BigDecimal supplierCommercialsReveivables;
    private String currencyCode;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getClientCommercialsReveivables() {
        return clientCommercialsReveivables;
    }

    public void setClientCommercialsReveivables(BigDecimal clientCommercialsReveivables) {
        this.clientCommercialsReveivables = clientCommercialsReveivables;
    }

    public BigDecimal getSupplierCommercialPayables() {
        return supplierCommercialPayables;
    }

    public void setSupplierCommercialPayables(BigDecimal supplierCommercialPayables) {
        this.supplierCommercialPayables = supplierCommercialPayables;
    }

    public BigDecimal getSupplierCommercialsReveivables() {
        return supplierCommercialsReveivables;
    }

    public void setSupplierCommercialsReveivables(BigDecimal supplierCommercialsReveivables) {
        this.supplierCommercialsReveivables = supplierCommercialsReveivables;
    }

    public BigDecimal getNetMargin() {
        return netMargin;
    }

    public void setNetMargin(BigDecimal netMargin) {
        this.netMargin = netMargin;
    }

    public BigDecimal getNetSupplierCost() {
        return netSupplierCost;
    }

    public void setNetSupplierCost(BigDecimal netSupplierCost) {
        this.netSupplierCost = netSupplierCost;
    }

    public BigDecimal getNetSellingPrice() {
        return netSellingPrice;
    }

    public void setNetSellingPrice(BigDecimal netSellingPrice) {
        this.netSellingPrice = netSellingPrice;
    }

    public BigDecimal getClientCommercialPayables() {
        return clientCommercialPayables;
    }

    public void setClientCommercialPayables(BigDecimal clientCommercialPayables) {
        this.clientCommercialPayables = clientCommercialPayables;
    }
}
