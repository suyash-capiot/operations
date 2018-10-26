package com.coxandkings.travel.operations.resource.commercialStatement;

import com.coxandkings.travel.operations.enums.commercialStatements.CommercialStatementSortingCriteria;
import com.coxandkings.travel.operations.enums.commercialStatements.CommercialStatementSortingOrder;

import java.util.Set;

public class CommercialStatementSearchCriteria {

    private String commercialStatementFor;
    private String commercialType;
    private String commercialHead;
    private String statementId;

    private String bookingDateFrom;
    private String bookingDateTo;
    private String travelDateFrom;
    private String travelDateTo;

    private String supplierName;
    private String clientName;
    private String companyMarketId;
    private String commercialStatementName;
    private String settlementDueDateFrom;
    private String settlementDueDateTo;
    private String settlementStatus;
    private String currency;


    private String productCategory;
    private String productCategorySubType;
    private String productNameSubType;
    private String productFlavourName;
    private String productName;

    private Integer size;
    private Integer page;

    private String clientType;
    private String clientCategory;
    private String clientSubCategory;

    private Boolean billPassingResource;
    private Boolean paymentAdviceResource;
    private Set<String> attachedStatementIds;

    private CommercialStatementSortingCriteria commercialStatementSortingCriteria;
    private CommercialStatementSortingOrder commercialStatementSortingOrder;

    public CommercialStatementSortingCriteria getCommercialStatementSortingCriteria() {
        return commercialStatementSortingCriteria;
    }

    public void setCommercialStatementSortingCriteria(CommercialStatementSortingCriteria commercialStatementSortingCriteria) {
        this.commercialStatementSortingCriteria = commercialStatementSortingCriteria;
    }

    public CommercialStatementSortingOrder getCommercialStatementSortingOrder() {
        return commercialStatementSortingOrder;
    }

    public void setCommercialStatementSortingOrder(CommercialStatementSortingOrder commercialStatementSortingOrder) {
        this.commercialStatementSortingOrder = commercialStatementSortingOrder;
    }

    public Boolean getBillPassingResource() {
        return billPassingResource;
    }

    public void setBillPassingResource(Boolean billPassingResource) {
        this.billPassingResource = billPassingResource;
    }

    public Boolean getPaymentAdviceResource() {
        return paymentAdviceResource;
    }

    public void setPaymentAdviceResource(Boolean paymentAdviceResource) {
        this.paymentAdviceResource = paymentAdviceResource;
    }

    public Set<String> getAttachedStatementIds() {
        return attachedStatementIds;
    }

    public void setAttachedStatementIds(Set<String> attachedStatementIds) {
        this.attachedStatementIds = attachedStatementIds;
    }

    public String getCommercialStatementFor() {
        return commercialStatementFor;
    }

    public void setCommercialStatementFor(String commercialStatementFor) {
        this.commercialStatementFor = commercialStatementFor;
    }

    public String getCommercialType() {
        return commercialType;
    }

    public void setCommercialType(String commercialType) {
        this.commercialType = commercialType;
    }

    public String getCommercialHead() {
        return commercialHead;
    }

    public void setCommercialHead(String commercialHead) {
        this.commercialHead = commercialHead;
    }

    public String getBookingDateFrom() {
        return bookingDateFrom;
    }

    public void setBookingDateFrom(String bookingDateFrom) {
        this.bookingDateFrom = bookingDateFrom;
    }

    public String getBookingDateTo() {
        return bookingDateTo;
    }

    public void setBookingDateTo(String bookingDateTo) {
        this.bookingDateTo = bookingDateTo;
    }

    public String getTravelDateFrom() {
        return travelDateFrom;
    }

    public void setTravelDateFrom(String travelDateFrom) {
        this.travelDateFrom = travelDateFrom;
    }

    public String getTravelDateTo() {
        return travelDateTo;
    }

    public void setTravelDateTo(String travelDateTo) {
        this.travelDateTo = travelDateTo;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getCommercialStatementName() {
        return commercialStatementName;
    }

    public void setCommercialStatementName(String commercialStatementName) {
        this.commercialStatementName = commercialStatementName;
    }

    public String getSettlementDueDateFrom() {
        return settlementDueDateFrom;
    }

    public void setSettlementDueDateFrom(String settlementDueDateFrom) {
        this.settlementDueDateFrom = settlementDueDateFrom;
    }

    public String getSettlementDueDateTo() {
        return settlementDueDateTo;
    }

    public void setSettlementDueDateTo(String settlementDueDateTo) {
        this.settlementDueDateTo = settlementDueDateTo;
    }

    public String getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductCategorySubType() {
        return productCategorySubType;
    }

    public void setProductCategorySubType(String productCategorySubType) {
        this.productCategorySubType = productCategorySubType;
    }

    public String getProductNameSubType() {
        return productNameSubType;
    }

    public void setProductNameSubType(String productNameSubType) {
        this.productNameSubType = productNameSubType;
    }

    public String getProductFlavourName() {
        return productFlavourName;
    }

    public void setProductFlavourName(String productFlavourName) {
        this.productFlavourName = productFlavourName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getCompanyMarketId() {
        return companyMarketId;
    }

    public void setCompanyMarketId(String companyMarketId) {
        this.companyMarketId = companyMarketId;
    }

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientCategory() {
        return clientCategory;
    }

    public void setClientCategory(String clientCategory) {
        this.clientCategory = clientCategory;
    }

    public String getClientSubCategory() {
        return clientSubCategory;
    }

    public void setClientSubCategory(String clientSubCategory) {
        this.clientSubCategory = clientSubCategory;
    }
}
