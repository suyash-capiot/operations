package com.coxandkings.travel.operations.resource.outbound.be;

public class ProductResponse {
    private String id;
    private String type;
    private String subType;
    private PassengerResponse leadPassenger;
    private Integer numberOfPassengers;

    private String fileHandlerId;
    private String productName;
    private String productCategorySubCategory;
    private Long travelDate;
    private String supplierId;
    private String productStatus;

    private ProductSummary productSummary;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PassengerResponse getLeadPassenger() {
        return leadPassenger;
    }

    public void setLeadPassenger(PassengerResponse leadPassenger) {
        this.leadPassenger = leadPassenger;
    }

    public Integer getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(Integer numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Long travelDate) {
        this.travelDate = travelDate;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public ProductSummary getProductSummary() {
        return productSummary;
    }

    public void setProductSummary(ProductSummary productSummary) {
        this.productSummary = productSummary;
    }

    public String getFileHandlerId() {
        return fileHandlerId;
    }

    public void setFileHandlerId(String fileHandlerId) {
        this.fileHandlerId = fileHandlerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getProductCategorySubCategory() {
        return productCategorySubCategory;
    }

    public void setProductCategorySubCategory(String productCategorySubCategory) {
        this.productCategorySubCategory = productCategorySubCategory;
    }
}
