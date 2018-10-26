package com.coxandkings.travel.operations.resource.changesuppliername;

public class SupplierHeaderRate {
    private String validityFrom;
    private String validityTo;
    private String supplierMarket;
    private String supplierCurrency;
    private boolean taxesApplicable;

    public String getValidityFrom() {
        return validityFrom;
    }

    public void setValidityFrom(String validityFrom) {
        this.validityFrom = validityFrom;
    }

    public String getValidityTo() {
        return validityTo;
    }

    public void setValidityTo(String validityTo) {
        this.validityTo = validityTo;
    }

    public String getSupplierMarket() {
        return supplierMarket;
    }

    public void setSupplierMarket(String supplierMarket) {
        this.supplierMarket = supplierMarket;
    }

    public String getSupplierCurrency() {
        return supplierCurrency;
    }

    public void setSupplierCurrency(String supplierCurrency) {
        this.supplierCurrency = supplierCurrency;
    }

    public boolean isTaxesApplicable() {
        return taxesApplicable;
    }

    public void setTaxesApplicable(boolean taxesApplicable) {
        this.taxesApplicable = taxesApplicable;
    }
}
