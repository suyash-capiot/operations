package com.coxandkings.travel.operations.model.prodreview.mdmtemplate;


import javax.validation.constraints.NotNull;


public class Products {


    private String productId;

    @NotNull
    private String clientType;

    private String clientCategory;

    private String clientSubCategory;

    private String clientName;

    @NotNull
    private String productCategory;

    @NotNull
    private String productCategorySubType;

    @NotNull
    private String productName;

    private String productNameSubType;

    private String productType;

    private String productFlavorName;

    private String brandName;

    private String brochureName;

    private String interest;

    private String crusiePackageName;

    private String packageType;

    private String productFlavorType;


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getProductCategorySubType() {
        return productCategorySubType;
    }

    public void setProductCategorySubType(String productCategorySubType) {
        this.productCategorySubType = productCategorySubType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNameSubType() {
        return productNameSubType;
    }

    public void setProductNameSubType(String productNameSubType) {
        this.productNameSubType = productNameSubType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductFlavorName() {
        return productFlavorName;
    }

    public void setProductFlavorName(String productFlavorName) {
        this.productFlavorName = productFlavorName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrochureName() {
        return brochureName;
    }

    public void setBrochureName(String brochureName) {
        this.brochureName = brochureName;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getCrusiePackageName() {
        return crusiePackageName;
    }

    public void setCrusiePackageName(String crusiePackageName) {
        this.crusiePackageName = crusiePackageName;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getProductFlavorType() {
        return productFlavorType;
    }

    public void setProductFlavorType(String productFlavorType) {
        this.productFlavorType = productFlavorType;
    }

    @Override
    public String toString() {
        return "Products{" +
                "productId='" + productId + '\'' +
                ", clientType='" + clientType + '\'' +
                ", clientCategory='" + clientCategory + '\'' +
                ", clientSubCategory='" + clientSubCategory + '\'' +
                ", clientName='" + clientName + '\'' +
                ", productCategory='" + productCategory + '\'' +
                ", productCategorySubType='" + productCategorySubType + '\'' +
                ", productName='" + productName + '\'' +
                ", productNameSubType='" + productNameSubType + '\'' +
                ", productType='" + productType + '\'' +
                ", productFlavorName='" + productFlavorName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", brochureName='" + brochureName + '\'' +
                ", interest='" + interest + '\'' +
                ", cruisePackageName='" + crusiePackageName + '\'' +
                ", packageType='" + packageType + '\'' +
                ", productFlavorType='" + productFlavorType + '\'' +
                '}';
    }
}
