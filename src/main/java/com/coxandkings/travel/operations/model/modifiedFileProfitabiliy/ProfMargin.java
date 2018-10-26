package com.coxandkings.travel.operations.model.modifiedFileProfitabiliy;

import java.math.BigDecimal;

public class ProfMargin {
    private BigDecimal netMarginAmt;
    private BigDecimal netMarginPercentage;
    private boolean marginUpdated;

    public boolean getMarginUpdated() {
        return marginUpdated;
    }

    public void setMarginUpdated(boolean marginUpdated) {
        this.marginUpdated = marginUpdated;
    }

    public BigDecimal getNetMarginAmt() {
        return netMarginAmt;
    }

    public void setNetMarginAmt(BigDecimal netMarginAmt) {
        this.netMarginAmt = netMarginAmt;
    }

    public BigDecimal getNetMarginPercentage() {
        return netMarginPercentage;
    }

    public void setNetMarginPercentage(BigDecimal netMarginPercentage) {
        this.netMarginPercentage = netMarginPercentage;
    }
}
