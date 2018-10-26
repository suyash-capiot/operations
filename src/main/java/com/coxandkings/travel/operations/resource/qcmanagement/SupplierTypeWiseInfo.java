package com.coxandkings.travel.operations.resource.qcmanagement;

import java.util.Objects;

public class SupplierTypeWiseInfo {
    private String supplierId;
    private String supplierName;
    private String flightNumber;


    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupplierTypeWiseInfo that = (SupplierTypeWiseInfo) o;
        return Objects.equals(supplierId, that.supplierId) &&
                Objects.equals(supplierName, that.supplierName) &&
                Objects.equals(flightNumber, that.flightNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(supplierId, supplierName, flightNumber);
    }

    @Override
    public String toString() {
        return "SupplierTypeWiseInfo{" +
                "supplierId='" + supplierId + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                '}';
    }
}
