package com.coxandkings.travel.operations.helper.booking.product.forex;

import java.math.BigDecimal;

public class DisbursementDetails {
    private Long dateOfDisbursement;
    private BigDecimal disbursementAmount;

    public Long getDateOfDisbursement() {
        return dateOfDisbursement;
    }

    public void setDateOfDisbursement(Long dateOfDisbursement) {
        this.dateOfDisbursement = dateOfDisbursement;
    }

    public BigDecimal getDisbursementAmount() {
        return disbursementAmount;
    }

    public void setDisbursementAmount(BigDecimal disbursementAmount) {
        this.disbursementAmount = disbursementAmount;
    }
}
