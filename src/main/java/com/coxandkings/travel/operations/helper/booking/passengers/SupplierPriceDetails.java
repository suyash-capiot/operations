package com.coxandkings.travel.operations.helper.booking.passengers;

import java.math.BigDecimal;

public class SupplierPriceDetails{
        private BigDecimal netPyblToSupplier;
        private BigDecimal suplrCncltnChrgs;
        private BigDecimal suplrAmndmtChrgs;
        private BigDecimal ttlNetPyblSplr;

    public BigDecimal getNetPyblToSupplier() {
        return netPyblToSupplier;
    }

    public void setNetPyblToSupplier(BigDecimal netPyblToSupplier) {
        this.netPyblToSupplier = netPyblToSupplier;
    }

    public BigDecimal getSuplrCncltnChrgs() {
        return suplrCncltnChrgs;
    }

    public void setSuplrCncltnChrgs(BigDecimal suplrCncltnChrgs) {
        this.suplrCncltnChrgs = suplrCncltnChrgs;
    }

    public BigDecimal getSuplrAmndmtChrgs() {
        return suplrAmndmtChrgs;
    }

    public void setSuplrAmndmtChrgs(BigDecimal suplrAmndmtChrgs) {
        this.suplrAmndmtChrgs = suplrAmndmtChrgs;
    }

    public BigDecimal getTtlNetPyblSplr() {
        return ttlNetPyblSplr;
    }

    public void setTtlNetPyblSplr(BigDecimal ttlNetPyblSplr) {
        this.ttlNetPyblSplr = ttlNetPyblSplr;
    }
}
