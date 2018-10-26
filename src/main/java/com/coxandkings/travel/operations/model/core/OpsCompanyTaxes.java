package com.coxandkings.travel.operations.model.core;

import java.math.BigDecimal;
import java.util.List;

public class OpsCompanyTaxes {

    private BigDecimal amount;

    private String currencyCode;

    private List<OpsCompanyTax> companyTax;

    public OpsCompanyTaxes() {
    }

    public OpsCompanyTaxes(BigDecimal amount, String currencyCode, List<OpsCompanyTax> companyTax) {
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.companyTax = companyTax;
    }

    public void setCompanyTax(List<OpsCompanyTax> companyTax) {
        this.companyTax = companyTax;
    }

    public List<OpsCompanyTax> getCompanyTax() {
        return companyTax;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
