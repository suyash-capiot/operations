package com.coxandkings.travel.operations.resource.commercialStatement;

import java.math.BigDecimal;

public class AttachedCommercialStatement {

    private String commercialHead;
    private String statementId;
    private String statementName;
    private String supplierOrClientName;
    private String productName;
    private BigDecimal totalPayable;
    private BigDecimal balancePayable;
    private BigDecimal clientServiceTax;
    private BigDecimal amountToBePaid;
    private BigDecimal totalPaid;
    private String supplierorClientId;
    private String currency;

    public BigDecimal getBalancePayable() {
        return balancePayable;
    }

    public void setBalancePayable(BigDecimal balancePayable) {
        this.balancePayable = balancePayable;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCommercialHead() {
        return commercialHead;
    }

    public void setCommercialHead(String commercialHead) {
        this.commercialHead = commercialHead;
    }

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    public String getStatementName() {
        return statementName;
    }

    public void setStatementName(String statementName) {
        this.statementName = statementName;
    }

    public String getSupplierOrClientName() {
        return supplierOrClientName;
    }

    public void setSupplierOrClientName(String supplierOrClientName) {
        this.supplierOrClientName = supplierOrClientName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getTotalPayable() {
        return totalPayable;
    }

    public void setTotalPayable(BigDecimal totalPayable) {
        this.totalPayable = totalPayable;
    }

    public BigDecimal getAmountToBePaid() {
        return amountToBePaid;
    }

    public void setAmountToBePaid(BigDecimal amountToBePaid) {
        this.amountToBePaid = amountToBePaid;
    }

    public BigDecimal getClientServiceTax() {
        return clientServiceTax;
    }

    public void setClientServiceTax(BigDecimal clientServiceTax) {
        this.clientServiceTax = clientServiceTax;
    }

    public String getSupplierorClientId() {
        return supplierorClientId;
    }

    public void setSupplierorClientId(String supplierorClientId) {
        this.supplierorClientId = supplierorClientId;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

}
