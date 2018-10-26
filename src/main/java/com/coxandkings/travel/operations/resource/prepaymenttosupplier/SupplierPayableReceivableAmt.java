package com.coxandkings.travel.operations.resource.prepaymenttosupplier;

import java.math.BigDecimal;

public class SupplierPayableReceivableAmt {

    private BigDecimal payableAmt;
    private BigDecimal receivableAmt;

    public BigDecimal getPayableAmt() {
        return payableAmt;
    }

    public void setPayableAmt(BigDecimal payableAmt) {
        this.payableAmt = payableAmt;
    }

    public BigDecimal getReceivableAmt() {
        return receivableAmt;
    }

    public void setReceivableAmt(BigDecimal receivableAmt) {
        this.receivableAmt = receivableAmt;
    }
}
