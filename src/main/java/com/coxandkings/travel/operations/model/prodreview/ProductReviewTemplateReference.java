package com.coxandkings.travel.operations.model.prodreview;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "product_review_template_reference")
public class ProductReviewTemplateReference {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String company;
    private String templateType;
    private String templateSubType;
    private String companyMarket;
    private String clientType;
    private String clientGroup;// in case of B2B user
    private String clientNameId;
    private String templateId; //or template name

    private String productCategory;
    private String productSubCategory;
    private String bookingReference;

    //if user complete product review then mark it as true
    @JsonIgnore
    private boolean isReviewDone = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getClientGroup() {
        return clientGroup;
    }

    public void setClientGroup(String clientGroup) {
        this.clientGroup = clientGroup;
    }

    public String getClientNameId() {
        return clientNameId;
    }

    public void setClientNameId(String clientNameId) {
        this.clientNameId = clientNameId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public boolean isReviewDone() {
        return isReviewDone;
    }

    public void setReviewDone(boolean reviewDone) {
        isReviewDone = reviewDone;
    }

    @Override
    public String toString() {
        return "ProductReviewTemplateReference{" +
                "id='" + id + '\'' +
                ", company='" + company + '\'' +
                ", templateType='" + templateType + '\'' +
                ", templateSubType='" + templateSubType + '\'' +
                ", companyMarket='" + companyMarket + '\'' +
                ", clientType='" + clientType + '\'' +
                ", clientGroup='" + clientGroup + '\'' +
                ", clientNameId='" + clientNameId + '\'' +
                ", templateId='" + templateId + '\'' +
                ", productCategory='" + productCategory + '\'' +
                ", productSubCategory='" + productSubCategory + '\'' +
                ", bookingReference='" + bookingReference + '\'' +
                ", isReviewDone=" + isReviewDone +
                '}';
    }
}
