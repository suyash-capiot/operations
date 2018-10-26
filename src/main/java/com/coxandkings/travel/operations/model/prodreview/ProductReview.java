package com.coxandkings.travel.operations.model.prodreview;


import com.coxandkings.travel.operations.enums.prodreview.CustomerResponseFlg;
import com.coxandkings.travel.operations.enums.prodreview.ProdReviewStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "product_review")
@SQLDelete(sql = "UPDATE product_review SET deleted = '1' WHERE id = ?")
//Filter added to retrieve only records that have not been soft deleted.
@Where(clause = "deleted <> '1'")
@JsonIgnoreProperties(ignoreUnknown = true)
@IdClass(ProductReviewVersion.class)
public class ProductReview {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Id
    @Column
    private Float versionNumber;

    @Column(name = "company_market")
    private String companyMarket;

    @Column(name = "company")
    private String company;

    @Column(name = "template_type")
    private String templateType;

    @Column(name = "template_sub_type")
    private String templateSubType;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "passengerName")
    private String passengerName;

    @Column(name = "point_of_sale")
    private String pointOfSale;

    @Column(name = "package_type")
    private String packageType;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "product_sub_category")
    private String productSubCategory;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "orderID")
    private String orderID;

    @Column(name = "client_type")
    private String clientType;

    @Column(name = "client_category")
    private String clientCategory;

    @Column(name = "client_sub_category")
    private String clientSubCategory;

    @Column(name = "client_group")
    private String clientGroup;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "product_review_status")
    private ProdReviewStatus productReviewStatus = ProdReviewStatus.OPEN;

    @OneToOne(cascade = CascadeType.ALL)
    private ReviewFormSubmitted reviewFormSubmitted;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productReview")
    //(name = "product_review_user_id")
    private List<ProductReviewUser> productReviewUsers;

    @Column(name = "customer_response_flg")
    private CustomerResponseFlg customerResponseFlag = CustomerResponseFlg.NOT_SENT;


/*

    @Column
    private Long aging;
*/

    @Column
    private Boolean reviewCompleted;

    @Column
    @NotNull(message = "unique reference number should not be null")
    private String uniqueReferenceNumber;

    @Column
    private boolean customerAcceptanceRequired;

 /*   @Column
    private String toDoTaskType;*/

    @Column
    private Boolean deleted = false;

    @Column
    private ZonedDateTime submitDate;

    @Column
    @NotNull(message = "travel Date should not be null")
    private ZonedDateTime travelDate;

    public Float getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Float versionNumber) {
        this.versionNumber = versionNumber;
    }

    public ZonedDateTime getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(ZonedDateTime travelDate) {
        this.travelDate = travelDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public ZonedDateTime getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(ZonedDateTime submitDate) {
        this.submitDate = submitDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyMarket() {
        return companyMarket;
    }

    public void setCompanyMarket(String companyMarket) {
        this.companyMarket = companyMarket;
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

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPointOfSale() {
        return pointOfSale;
    }

    public void setPointOfSale(String pointOfSale) {
        this.pointOfSale = pointOfSale;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public ProdReviewStatus getProductReviewStatus() {
        return productReviewStatus;
    }

    public void setProductReviewStatus(ProdReviewStatus productReviewStatus) {
        this.productReviewStatus = productReviewStatus;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public ReviewFormSubmitted getReviewFormSubmitted() {
        return reviewFormSubmitted;
    }

    public void setReviewFormSubmitted(ReviewFormSubmitted reviewFormSubmitted) {
        this.reviewFormSubmitted = reviewFormSubmitted;
    }

    public CustomerResponseFlg getCustomerResponseFlag() {
        return customerResponseFlag;
    }

    public void setCustomerResponseFlag(CustomerResponseFlg customerResponseFlag) {
        this.customerResponseFlag = customerResponseFlag;
    }

    public List<ProductReviewUser> getProductReviewUsers() {
        return productReviewUsers;
    }

    public void setProductReviewUsers(List<ProductReviewUser> productReviewUsers) {
        this.productReviewUsers = productReviewUsers;
    }

    public String getUniqueReferenceNumber() {
        return uniqueReferenceNumber;
    }

    public void setUniqueReferenceNumber(String uniqueReferenceNumber) {
        this.uniqueReferenceNumber = uniqueReferenceNumber;
    }

    public Boolean getReviewCompleted() {
        return reviewCompleted;
    }

    public void setReviewCompleted(Boolean reviewCompleted) {
        this.reviewCompleted = reviewCompleted;
    }

    public boolean isCustomerAcceptanceRequired() {
        return customerAcceptanceRequired;
    }

    public void setCustomerAcceptanceRequired(boolean customerAcceptanceRequired) {
        this.customerAcceptanceRequired = customerAcceptanceRequired;
    }

 /*   public Long getAging() {
        return aging;
    }

    public void setAging(Long aging) {
        this.aging = aging;
    }*/

   /* public String getToDoTaskType() {
        return toDoTaskType;
    }

    public void setToDoTaskType(String toDoTaskType) {
        this.toDoTaskType = toDoTaskType;
    }*/

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getClientGroup() {
        return clientGroup;
    }

    public void setClientGroup(String clientGroup) {
        this.clientGroup = clientGroup;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
