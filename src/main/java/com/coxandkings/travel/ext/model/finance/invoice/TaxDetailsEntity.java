package com.coxandkings.travel.ext.model.finance.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxDetailsEntity {
    private UUID id;
    private Double taxAmount;
    private String taxCode;
    private String currency;
    private Double taxRate;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TaxDetailsEntity other = (TaxDetailsEntity) obj;
        if (taxCode == null) {
            if (other.taxCode != null)
                return false;
        } else if (!taxCode.equals(other.taxCode))
            return false;

        this.taxAmount += other.taxAmount;
        other.taxAmount = this.taxAmount;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((taxCode == null) ? 0 : taxCode.hashCode());
        return result;
    }
}
