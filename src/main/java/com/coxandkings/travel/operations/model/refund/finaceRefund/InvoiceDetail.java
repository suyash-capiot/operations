package com.coxandkings.travel.operations.model.refund.finaceRefund;

import java.util.Objects;
import java.util.UUID;


public class InvoiceDetail {

    private UUID id;
    private String invoiceorCreditId;
    private TypeOFNote typeOfNote;
    private String invoiceOrCreditNote;
    private String transactionCurrency;
    private double transactionAmount;
    private double roe;
    private String functionalCurrency;
    private double functionalAmount;
    private Long createdOn;
    private Long lastUpdatedOn;
    private String createdBy;
    private String lastUpdatedBy;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getInvoiceorCreditId() {
        return invoiceorCreditId;
    }

    public void setInvoiceorCreditId(String invoiceorCreditId) {
        this.invoiceorCreditId = invoiceorCreditId;
    }

    public TypeOFNote getTypeOfNote() {
        return typeOfNote;
    }

    public void setTypeOfNote(TypeOFNote typeOfNote) {
        this.typeOfNote = typeOfNote;
    }

    public String getInvoiceOrCreditNote() {
        return invoiceOrCreditNote;
    }

    public void setInvoiceOrCreditNote(String invoiceOrCreditNote) {
        this.invoiceOrCreditNote = invoiceOrCreditNote;
    }

    public String getTransactionCurrency() {
        return transactionCurrency;
    }

    public void setTransactionCurrency(String transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public double getRoe() {
        return roe;
    }

    public void setRoe(double roe) {
        this.roe = roe;
    }

    public String getFunctionalCurrency() {
        return functionalCurrency;
    }

    public void setFunctionalCurrency(String functionalCurrency) {
        this.functionalCurrency = functionalCurrency;
    }

    public double getFunctionalAmount() {
        return functionalAmount;
    }

    public void setFunctionalAmount(double functionalAmount) {
        this.functionalAmount = functionalAmount;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public Long getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Long lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return id.equals(((InvoiceDetail) o).id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, invoiceorCreditId, typeOfNote, invoiceOrCreditNote, transactionCurrency, transactionAmount, roe, functionalCurrency, functionalAmount, createdOn, lastUpdatedOn, createdBy, lastUpdatedBy);
    }
}
