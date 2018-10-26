package com.coxandkings.travel.operations.model.prodreview.mdmtemplate;

import com.coxandkings.travel.operations.enums.prodreview.ProdReviewStatus;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;


public class MDMTemplate {


    private String templateId;


    private String uniqueReferenceNumber;

    @Column
    private String companyID;
    @Column
    @NotNull
    private String templateType;
    @Column
    private String subtypeForm;
    @Column
    @NotNull
    private String templateName;
    @Column
    @NotNull
    private String pos;
    @Column
    private String createdBy;
    @Column
    private ZonedDateTime createdAt;
    @Column
    private String updatedBy;
    @Column
    private ZonedDateTime lastUpdated;
    @Column
    private Boolean deleted;
    @OneToOne(cascade = CascadeType.ALL)
    private Products products;

    @OneToOne(cascade = CascadeType.ALL)
    private Details details;

    @Column
    private ProdReviewStatus status = ProdReviewStatus.OPEN;


    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getSubtypeForm() {
        return subtypeForm;
    }

    public void setSubtypeForm(String subtypeForm) {
        this.subtypeForm = subtypeForm;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ZonedDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public ProdReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ProdReviewStatus status) {
        this.status = status;
    }

    public String getUniqueReferenceNumber() {
        return uniqueReferenceNumber;
    }

    public void setUniqueReferenceNumber(String uniqueReferenceNumber) {
        this.uniqueReferenceNumber = uniqueReferenceNumber;
    }

    @Override
    public String toString() {
        return "MDMTemplate{" +
                "templateId='" + templateId + '\'' +
                ", uniqueReferenceNumber='" + uniqueReferenceNumber + '\'' +
                ", companyID='" + companyID + '\'' +
                ", templateType='" + templateType + '\'' +
                ", subtypeForm='" + subtypeForm + '\'' +
                ", templateName='" + templateName + '\'' +
                ", pos='" + pos + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdAt=" + createdAt +
                ", updatedBy='" + updatedBy + '\'' +
                ", lastUpdated=" + lastUpdated +
                ", deleted=" + deleted +
                ", products=" + products +
                ", details=" + details +
                ", status=" + status +
                '}';
    }
}
