
package com.coxandkings.travel.operations.resource.changesuppliername.response.acco;

import java.util.List;

public class Receivables {

    private Integer amount;
    private List<Object> receivable = null;
    private String currencyCode;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public List<Object> getReceivable() {
        return receivable;
    }

    public void setReceivable(List<Object> receivable) {
        this.receivable = receivable;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

}
