package com.coxandkings.travel.operations.resource.manageofflinebooking;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

public class ManualPNRSyncResource implements Serializable {
    private static final long serialVersionUID = -268328536850001184L;

    @NotEmpty(message = "Supplier Name should not be Empty.")
    private String supplierName;

    @NotEmpty(message = "Book reference ID should not be Empty.")
    private String bookRefID;

    @NotEmpty(message = "GDS PNR should not be Empty")
    private String gdsPNR;

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getBookRefID() {
        return bookRefID;
    }

    public void setBookRefID(String bookRefID) {
        this.bookRefID = bookRefID;
    }

    public String getGdsPNR() {
        return gdsPNR;
    }

    public void setGdsPNR(String gdsPNR) {
        this.gdsPNR = gdsPNR;
    }
}
