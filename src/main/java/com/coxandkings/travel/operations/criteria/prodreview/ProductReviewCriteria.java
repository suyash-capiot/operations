package com.coxandkings.travel.operations.criteria.prodreview;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductReviewCriteria {

    private String companyMarketId;
    private String templateType;
    private String templateSubType;
    private String clientType;
    private String clientGroupId;

    private String clientId;
    //this is clientName
    private String clientNameId;
    private String productCategory;
    private String productSubCategory;
    private String fromDate;
    private String toDate;
    private String productName;


    //pagination
    private Integer pageSize;
    private String order;//ASE OR DESC
    private String sortingField;
    private Integer pageNumber;


    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getCompanyMarketId() {
        return companyMarketId;
    }

    public void setCompanyMarketId(String companyMarketId) {
        this.companyMarketId = companyMarketId;
    }

    public String getClientGroupId() {
        return clientGroupId;
    }

    public void setClientGroupId(String clientGroupId) {
        this.clientGroupId = clientGroupId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientNameId() {
        return clientNameId;
    }

    public void setClientNameId(String clientNameId) {
        this.clientNameId = clientNameId;
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

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getTemplateSubType() {
        return templateSubType;
    }

    public void setTemplateSubType(String templateSubType) {
        this.templateSubType = templateSubType;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getSortingField() {
        return sortingField;
    }

    public void setSortingField(String sortingField) {
        this.sortingField = sortingField;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
