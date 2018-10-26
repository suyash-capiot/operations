package com.coxandkings.travel.operations.criteria.merge;

public class ActivityWithSharedTransferCriteria {
    private String productName;
    private String productNameSubType;
    private Long travelDate;
    private Boolean airConditioned;
    private String vehicleCategory;
    private String supplierId;

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

    public Long getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Long travelDate) {
        this.travelDate = travelDate;
    }

    public Boolean getAirConditioned() {
        return airConditioned;
    }

    public void setAirConditioned(Boolean airConditioned) {
        this.airConditioned = airConditioned;
    }

    public String getVehicleCategory() {
        return vehicleCategory;
    }

    public void setVehicleCategory(String vehicleCategory) {
        this.vehicleCategory = vehicleCategory;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
}
