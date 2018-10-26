package com.coxandkings.travel.operations.resource.userfavourites.products;

public class ActivitiesResource {
    private String country;
    private String city;
    private String productName;
    private String productNameSubType;
    private String supplierName;
    private String supplierReferenceNo;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierReferenceNo() {
        return supplierReferenceNo;
    }

    public void setSupplierReferenceNo(String supplierReferenceNo) {
        this.supplierReferenceNo = supplierReferenceNo;
    }
}
