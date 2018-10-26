
package com.coxandkings.travel.operations.resource.changesuppliername.response.acco;

import java.util.List;

public class Taxes {

    private Integer amount;
    private List<Tax> tax = null;
    private String currencyCode;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public List<Tax> getTax() {
        return tax;
    }

    public void setTax(List<Tax> tax) {
        this.tax = tax;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

}
