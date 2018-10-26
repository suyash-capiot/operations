package com.coxandkings.travel.ext.model.be;

import java.math.BigDecimal;
import java.util.List;

public class CompanyTaxes {

    private BigDecimal amount;

    private List<CompanyTax> companyTax;

    private String currencyCode;

    public CompanyTaxes() {
    }

    public CompanyTaxes(BigDecimal amount, List<CompanyTax> companyTax, String currencyCode) {
        this.amount = amount;
        this.companyTax = companyTax;
        this.currencyCode = currencyCode;
    }

    public void setCompanyTax(List<CompanyTax> companyTax) {
        this.companyTax = companyTax;
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

    public List<CompanyTax> getCompanyTax() {
        return companyTax;
    }
}
