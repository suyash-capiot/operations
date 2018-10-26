package com.coxandkings.travel.operations.resource.userfavourites.products;

public class CarResource {
    private String category;
    private String pickupPointType;
    private String pickupPointTo;
    private String suppierName;
    private String supplierReferenceNo;
    private String PNR;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPickupPointType() {
        return pickupPointType;
    }

    public void setPickupPointType(String pickupPointType) {
        this.pickupPointType = pickupPointType;
    }

    public String getPickupPointTo() {
        return pickupPointTo;
    }

    public void setPickupPointTo(String pickupPointTo) {
        this.pickupPointTo = pickupPointTo;
    }

    public String getSuppierName() {
        return suppierName;
    }

    public void setSuppierName(String suppierName) {
        this.suppierName = suppierName;
    }

    public String getSupplierReferenceNo() {
        return supplierReferenceNo;
    }

    public void setSupplierReferenceNo(String supplierReferenceNo) {
        this.supplierReferenceNo = supplierReferenceNo;
    }

    public String getPNR() {
        return PNR;
    }

    public void setPNR(String PNR) {
        this.PNR = PNR;
    }
}
