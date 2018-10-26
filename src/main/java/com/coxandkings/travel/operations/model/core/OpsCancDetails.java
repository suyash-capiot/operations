package com.coxandkings.travel.operations.model.core;

import java.io.Serializable;

public class OpsCancDetails implements Serializable {
    private static final long serialVersionUID = -8784079972025051759L;

    private String cancelID;
    private String companyCancelCharges;
    private String supplierCancelCharges;
    private String companyCancelChargesCurrencyCode;
    private String supplierCancelChargesCurrencyCode;
    private String cancellationDateAndTime;
    private String cancellationBy;
    private String type;

    public String getCancelID() {
        return cancelID;
    }

    public void setCancelID(String cancelID) {
        this.cancelID = cancelID;
    }

    public String getCompanyCancelCharges() {
        return companyCancelCharges;
    }

    public void setCompanyCancelCharges(String companyCancelCharges) {
        this.companyCancelCharges = companyCancelCharges;
    }

    public String getSupplierCancelCharges() {
        return supplierCancelCharges;
    }

    public void setSupplierCancelCharges(String supplierCancelCharges) {
        this.supplierCancelCharges = supplierCancelCharges;
    }

    public String getCompanyCancelChargesCurrencyCode() {
        return companyCancelChargesCurrencyCode;
    }

    public void setCompanyCancelChargesCurrencyCode(String companyCancelChargesCurrencyCode) {
        this.companyCancelChargesCurrencyCode = companyCancelChargesCurrencyCode;
    }

    public String getSupplierCancelChargesCurrencyCode() {
        return supplierCancelChargesCurrencyCode;
    }

    public void setSupplierCancelChargesCurrencyCode(String supplierCancelChargesCurrencyCode) {
        this.supplierCancelChargesCurrencyCode = supplierCancelChargesCurrencyCode;
    }

    public String getCancellationDateAndTime() {
        return cancellationDateAndTime;
    }

    public void setCancellationDateAndTime(String cancellationDateAndTime) {
        this.cancellationDateAndTime = cancellationDateAndTime;
    }

    public String getCancellationBy() {
        return cancellationBy;
    }

    public void setCancellationBy(String cancellationBy) {
        this.cancellationBy = cancellationBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
