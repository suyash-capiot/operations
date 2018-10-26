package com.coxandkings.travel.operations.resource.changesuppliername;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SupplierResource {
    @JsonIgnore
    @JsonProperty(value = "credentials")
    private List<Object> credentials;
    private String supplierId;
    private String supplierName;

    public SupplierResource() {
    }

    public SupplierResource(String supplierId, String supplierName) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Override
    public String toString() {
        return "SupplierResource{" +
                "supplierId='" + supplierId + '\'' +
                ", supplierName='" + supplierName + '\'' +
                '}';
    }
}
