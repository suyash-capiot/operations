package com.coxandkings.travel.operations.resource.reconfirmation.supplier;

public class SupplierCancelReconfirmationResource {

    private String supplierReconfirmationID;

    public String getSupplierReconfirmationID( ) {
        return supplierReconfirmationID;
    }

    public void setSupplierReconfirmationID( String supplierReconfirmationID ) {
        this.supplierReconfirmationID = supplierReconfirmationID;
    }

    @Override
    public String toString() {
        return "SupplierCancelReconfirmationResource{" +
                "supplierReconfirmationID='" + supplierReconfirmationID + '\'' +
                '}';
    }
}
