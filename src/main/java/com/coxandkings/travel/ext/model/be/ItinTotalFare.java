
package com.coxandkings.travel.ext.model.be;


public class ItinTotalFare {

    private Double amount;
    private BaseFare baseFare;
    private Fees fees;
    private Taxes taxes;
    private Receivables receivables;
    private String currencyCode;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public BaseFare getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(BaseFare baseFare) {
        this.baseFare = baseFare;
    }

    public Fees getFees() {
        return fees;
    }

    public void setFees(Fees fees) {
        this.fees = fees;
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
