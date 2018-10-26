package com.coxandkings.travel.operations.service.reconfirmation.common;

public class CustomSupplierDetails {

    private String supplierId;
    private String emailId;
    private String supplierName;
    private boolean onlineSupplier;

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public boolean isOnlineSupplier() {
        return onlineSupplier;
    }

    public void setOnlineSupplier(boolean onlineSupplier) {
        this.onlineSupplier = onlineSupplier;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
