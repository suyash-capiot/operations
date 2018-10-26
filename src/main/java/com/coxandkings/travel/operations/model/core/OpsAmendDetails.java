package com.coxandkings.travel.operations.model.core;

import java.io.Serializable;

public class OpsAmendDetails implements Serializable {
    private static final long serialVersionUID = 5320333530466531360L;

    private String amendID;
    private String companyAmendCharges;
    private String supplierAmendCharges;
    private String companyAmendChargesCurrencyCode;
    private String supplierAmendChargesCurrencyCode;
    private String amendmentDateAndTime;
    private String amendmentBy;
    private String type;

    public String getAmendID() {
        return amendID;
    }

    public void setAmendID(String amendID) {
        this.amendID = amendID;
    }

    public String getCompanyAmendCharges() {
        return companyAmendCharges;
    }

    public void setCompanyAmendCharges(String companyAmendCharges) {
        this.companyAmendCharges = companyAmendCharges;
    }

    public String getSupplierAmendCharges() {
        return supplierAmendCharges;
    }

    public void setSupplierAmendCharges(String supplierAmendCharges) {
        this.supplierAmendCharges = supplierAmendCharges;
    }

    public String getCompanyAmendChargesCurrencyCode() {
        return companyAmendChargesCurrencyCode;
    }

    public void setCompanyAmendChargesCurrencyCode(String companyAmendChargesCurrencyCode) {
        this.companyAmendChargesCurrencyCode = companyAmendChargesCurrencyCode;
    }

    public String getSupplierAmendChargesCurrencyCode() {
        return supplierAmendChargesCurrencyCode;
    }

    public void setSupplierAmendChargesCurrencyCode(String supplierAmendChargesCurrencyCode) {
        this.supplierAmendChargesCurrencyCode = supplierAmendChargesCurrencyCode;
    }

    public String getAmendmentDateAndTime() {
        return amendmentDateAndTime;
    }

    public void setAmendmentDateAndTime(String amendmentDateAndTime) {
        this.amendmentDateAndTime = amendmentDateAndTime;
    }

    public String getAmendmentBy() {
        return amendmentBy;
    }

    public void setAmendmentBy(String amendmentBy) {
        this.amendmentBy = amendmentBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
