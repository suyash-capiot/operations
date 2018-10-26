package com.coxandkings.travel.operations.resource.commercialStatement;

import java.time.ZonedDateTime;

public class CommercialStatementGeneration {

    private String productCategory;
    private String productSubCategory;
    private String productName;
    private String clientId;
    private String supplierId;
    private String commercialHead;
    private String commercialtype;
    private ZonedDateTime statementPeriodFromDate;
    private ZonedDateTime statementPeriodToDate;
    private String currency;
    private ZonedDateTime settlementDueDate;
    private String companyMarket;
    private String supplierOrClientName;
    private String settlementSchedule;
    private String commercialId;
    private String appliedOnCommercialHead;

    public String getAppliedOnCommercialHead() {
        return appliedOnCommercialHead;
    }

    public void setAppliedOnCommercialHead(String appliedOnCommercialHead) {
        this.appliedOnCommercialHead = appliedOnCommercialHead;
    }

    public String getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(String commercialId) {
        this.commercialId = commercialId;
    }

    public String getSupplierOrClientName() {
        return supplierOrClientName;
    }

    public void setSupplierOrClientName(String supplierOrClientName) {
        this.supplierOrClientName = supplierOrClientName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getCommercialHead() {
        return commercialHead;
    }

    public void setCommercialHead(String commercialHead) {
        this.commercialHead = commercialHead;
    }

    public String getCommercialtype() {
        return commercialtype;
    }

    public void setCommercialtype(String commercialtype) {
        this.commercialtype = commercialtype;
    }

    public ZonedDateTime getStatementPeriodFromDate() {
        return statementPeriodFromDate;
    }

    public ZonedDateTime getStatementPeriodToDate() {
        return statementPeriodToDate;
    }

    public ZonedDateTime getSettlementDueDate() {
        return settlementDueDate;
    }

    public void setStatementPeriodFromDate(ZonedDateTime statementPeriodFromDate) {
        this.statementPeriodFromDate = statementPeriodFromDate;
    }


    public void setStatementPeriodToDate(ZonedDateTime statementPeriodToDate) {
        this.statementPeriodToDate = statementPeriodToDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public void setSettlementDueDate(ZonedDateTime settlementDueDate) {
        this.settlementDueDate = settlementDueDate;
    }

    public String getCompanyMarket() {
        return companyMarket;
    }

    public void setCompanyMarket(String companyMarket) {
        this.companyMarket = companyMarket;
    }

    public String getSettlementSchedule() {
        return settlementSchedule;
    }

    public void setSettlementSchedule(String settlementSchedule) {
        this.settlementSchedule = settlementSchedule;
    }
}
