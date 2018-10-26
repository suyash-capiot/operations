
package com.coxandkings.travel.operations.resource.changesuppliername.response.acco;


public class TotalPriceInfo {

    private Double amount;
    private Taxes taxes;
    private Receivables receivables;
    private String currencyCode;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Taxes getTaxes() {
        return taxes;
    }

    public void setTaxes(Taxes taxes) {
        this.taxes = taxes;
    }

    public Receivables getReceivables() {
        return receivables;
    }

    public void setReceivables(Receivables receivables) {
        this.receivables = receivables;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

}
