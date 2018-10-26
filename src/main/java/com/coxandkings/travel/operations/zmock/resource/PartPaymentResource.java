package com.coxandkings.travel.operations.zmock.resource;

public class PartPaymentResource {
    private String supplierCurrency;
    private String netPayableToSupplier;
    private String balanceAmtPayableToSupplier;

    public String getSupplierCurrency() {
        return supplierCurrency;
    }

    public void setSupplierCurrency(String supplierCurrency) {
        this.supplierCurrency = supplierCurrency;
    }

    public String getNetPayableToSupplier() {
        return netPayableToSupplier;
    }

    public void setNetPayableToSupplier(String netPayableToSupplier) {
        this.netPayableToSupplier = netPayableToSupplier;
    }

    public String getBalanceAmtPayableToSupplier() {
        return balanceAmtPayableToSupplier;
    }

    public void setBalanceAmtPayableToSupplier(String balanceAmtPayableToSupplier) {
        this.balanceAmtPayableToSupplier = balanceAmtPayableToSupplier;
    }

    public PartPaymentResource() {
        this.supplierCurrency = "INR";
        this.netPayableToSupplier = "INR 1,00,000";
        this.balanceAmtPayableToSupplier = "INR 0";
    }

    @Override
    public String toString() {
        return "PartPaymentResource{" +
                "supplierCurrency='" + supplierCurrency + '\'' +
                ", netPayableToSupplier='" + netPayableToSupplier + '\'' +
                ", balanceAmtPayableToSupplier='" + balanceAmtPayableToSupplier + '\'' +
                '}';
    }
}
