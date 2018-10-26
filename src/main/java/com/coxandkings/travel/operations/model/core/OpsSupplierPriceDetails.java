package com.coxandkings.travel.operations.model.core;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Embeddable
public class OpsSupplierPriceDetails {

    @NotNull
    private BigDecimal netPyblToSupplier;
    @NotNull
    private BigDecimal suplrCncltnChrgs;
    @NotNull
    private BigDecimal suplrAmndmtChrgs;
    @NotNull
    private BigDecimal ttlNetPyblSplr;

    private String supplierName;

    public BigDecimal getNetPyblToSupplier( ) {
        return netPyblToSupplier;
    }

    public void setNetPyblToSupplier( BigDecimal netPyblToSupplier ) {
        this.netPyblToSupplier = netPyblToSupplier;
    }

    public BigDecimal getSuplrCncltnChrgs( ) {
        return suplrCncltnChrgs;
    }

    public void setSuplrCncltnChrgs( BigDecimal suplrCncltnChrgs ) {
        this.suplrCncltnChrgs = suplrCncltnChrgs;
    }

    public BigDecimal getSuplrAmndmtChrgs( ) {
        return suplrAmndmtChrgs;
    }

    public void setSuplrAmndmtChrgs( BigDecimal suplrAmndmtChrgs ) {
        this.suplrAmndmtChrgs = suplrAmndmtChrgs;
    }

    public BigDecimal getTtlNetPyblSplr( ) {
        return ttlNetPyblSplr;
    }

    public void setTtlNetPyblSplr( BigDecimal ttlNetPyblSplr ) {
        this.ttlNetPyblSplr = ttlNetPyblSplr;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public OpsSupplierPriceDetails(){
        netPyblToSupplier = new BigDecimal(6000);
        suplrCncltnChrgs = new BigDecimal(0);
        suplrAmndmtChrgs = new BigDecimal(0);
        ttlNetPyblSplr = new BigDecimal(6000);
        supplierName = "String";
    }

    public OpsSupplierPriceDetails(String supplierName){
        netPyblToSupplier = new BigDecimal(6000);
        suplrCncltnChrgs = new BigDecimal(0);
        suplrAmndmtChrgs = new BigDecimal(0);
        ttlNetPyblSplr = new BigDecimal(6000);
        this.supplierName = supplierName;
    }
}
