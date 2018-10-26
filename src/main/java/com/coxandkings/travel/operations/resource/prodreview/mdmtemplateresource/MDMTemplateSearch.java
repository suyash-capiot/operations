package com.coxandkings.travel.operations.resource.prodreview.mdmtemplateresource;

import java.time.ZonedDateTime;

public class MDMTemplateSearch {
    private String companyMarket;
    private String clientType;
    private String clientName;
    private String productCategory;
    private String productName;
    private String reviewId;
    private String status;
    private String createdBy;
    private ZonedDateTime createdAt;
    private String days;

    public String getCompanyMarket() {
        return companyMarket;
    }

    public void setCompanyMarket(String companyMarket) {
        this.companyMarket = companyMarket;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "MDMTemplateSearch{" +
                "companyMarket='" + companyMarket + '\'' +
                ", clientType='" + clientType + '\'' +
                ", clientName='" + clientName + '\'' +
                ", productCategory='" + productCategory + '\'' +
                ", productName='" + productName + '\'' +
                ", reviewId='" + reviewId + '\'' +
                ", status='" + status + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdAt=" + createdAt +
                ", days='" + days + '\'' +
                '}';
    }
}
