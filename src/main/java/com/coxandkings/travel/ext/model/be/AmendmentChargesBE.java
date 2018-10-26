package com.coxandkings.travel.ext.model.be;

import java.io.Serializable;

public class AmendmentChargesBE implements Serializable {

    private final static long serialVersionUID = -5031637811167272163L;

    private String product;// INFO: It's product category sub type
    private String id;// INFO: This id means AmendID/CancelID of latest amendment/cancellation on room/passenger which will come from UI.
    private String companyCharges;
    private String supplierCharges;
    private String userID;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyCharges() {
        return companyCharges;
    }

    public void setCompanyCharges(String companyCharges) {
        this.companyCharges = companyCharges;
    }

    public String getSupplierCharges() {
        return supplierCharges;
    }

    public void setSupplierCharges(String supplierCharges) {
        this.supplierCharges = supplierCharges;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
