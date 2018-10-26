package com.coxandkings.travel.ext.model.be;

import java.math.BigDecimal;

public class CompanyTax {

    private BigDecimal amount;

    private String hsnCode;

    private String taxComponent;

    private String sacCode;

    private String taxPercent;

    private String taxCode;

    private String currencyCode;

    public CompanyTax() {
    }

    public CompanyTax(BigDecimal amount, String hsnCode, String taxComponent, String sacCode, String taxPercent, String taxCode, String currencyCode) {
        this.amount = amount;
        this.hsnCode = hsnCode;
        this.taxComponent = taxComponent;
        this.sacCode = sacCode;
        this.taxPercent = taxPercent;
        this.taxCode = taxCode;
        this.currencyCode = currencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getTaxComponent() {
        return taxComponent;
    }

    public void setTaxComponent(String taxComponent) {
        this.taxComponent = taxComponent;
    }

    public String getSacCode() {
        return sacCode;
    }

    public void setSacCode(String sacCode) {
        this.sacCode = sacCode;
    }

    public String getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(String taxPercent) {
        this.taxPercent = taxPercent;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
