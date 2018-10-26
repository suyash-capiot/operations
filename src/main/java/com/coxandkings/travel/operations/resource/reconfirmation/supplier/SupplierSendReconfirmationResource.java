package com.coxandkings.travel.operations.resource.reconfirmation.supplier;

public class SupplierSendReconfirmationResource {

    private String supplierReconfirmationID;
    private String productConfirmationNumber;

    public String getSupplierReconfirmationID( ) {
        return supplierReconfirmationID;
    }

    public void setSupplierReconfirmationID( String supplierReconfirmationID ) {
        this.supplierReconfirmationID = supplierReconfirmationID;
    }

    public String getProductConfirmationNumber( ) {
        return productConfirmationNumber;
    }

    public void setProductConfirmationNumber( String productConfirmationNumber ) {
        this.productConfirmationNumber = productConfirmationNumber;
    }

    @Override
    public String toString( ) {
        return "SupplierSendReconfirmationResource{" +
                "supplierReconfirmationID='" + supplierReconfirmationID + '\'' +
                ", productConfirmationNumber='" + productConfirmationNumber + '\'' +
                '}';
    }
}
