package com.coxandkings.travel.operations.resource.forex;

public class ForexCountResource {

    private Integer requestPending;
    private Integer requestCompleted;
    private Integer indentPending;
    private Integer indentCompleted;
    private Integer disbursementPending;
    private Integer disbursementCompleted;


    public Integer getRequestPending() {
        return requestPending;
    }

    public void setRequestPending(Integer requestPending) {
        this.requestPending = requestPending;
    }

    public Integer getRequestCompleted() {
        return requestCompleted;
    }

    public void setRequestCompleted(Integer requestCompleted) {
        this.requestCompleted = requestCompleted;
    }

    public Integer getIndentPending() {
        return indentPending;
    }

    public void setIndentPending(Integer indentPending) {
        this.indentPending = indentPending;
    }

    public Integer getIndentCompleted() {
        return indentCompleted;
    }

    public void setIndentCompleted(Integer indentCompleted) {
        this.indentCompleted = indentCompleted;
    }

    public Integer getDisbursementPending() {
        return disbursementPending;
    }

    public void setDisbursementPending(Integer disbursementPending) {
        this.disbursementPending = disbursementPending;
    }

    public Integer getDisbursementCompleted() {
        return disbursementCompleted;
    }

    public void setDisbursementCompleted(Integer disbursementCompleted) {
        this.disbursementCompleted = disbursementCompleted;
    }
}
