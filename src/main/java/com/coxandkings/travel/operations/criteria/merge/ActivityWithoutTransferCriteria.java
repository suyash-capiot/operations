package com.coxandkings.travel.operations.criteria.merge;

public class ActivityWithoutTransferCriteria {
    private String productName;
    private String productNameSubType;
    private Long travelDate;
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

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
}
