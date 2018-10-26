package com.coxandkings.travel.operations.model.core;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Embeddable
public class OpsSellingPriceDetails implements Serializable {
    @NotNull
    private Double totalSellingPrice;
    @NotNull
    private BigDecimal commission;
    @NotNull
    private BigDecimal discountValue;
    @NotNull
    private BigDecimal totalNetSP;
    @NotNull
    private BigDecimal cmpyCancellationCharges;
    @NotNull
    private BigDecimal cmpyAmendmentCharges;
    @NotNull
    private double exchangeRate;

    public Double getTotalSellingPrice() {
        return totalSellingPrice;
    }

    public void setTotalSellingPrice(Double totalSellingPrice) {
        this.totalSellingPrice = totalSellingPrice;
    }

    public BigDecimal getCommission( ) {
        return commission;
    }

    public void setCommission( BigDecimal commission ) {
        this.commission = commission;
    }

    public BigDecimal getDiscountValue( ) {
        return discountValue;
    }

    public void setDiscountValue( BigDecimal discountValue ) {
        this.discountValue = discountValue;
    }

    public BigDecimal getTotalNetSP( ) {
        return totalNetSP;
    }

    public void setTotalNetSP( BigDecimal totalNetSP ) {
        this.totalNetSP = totalNetSP;
    }

    public BigDecimal getCmpyCancellationCharges( ) {
        return cmpyCancellationCharges;
    }

    public void setCmpyCancellationCharges( BigDecimal cmpyCancellationCharges ) {
        this.cmpyCancellationCharges = cmpyCancellationCharges;
    }

    public BigDecimal getCmpyAmendmentCharges( ) {
        return cmpyAmendmentCharges;
    }

    public void setCmpyAmendmentCharges( BigDecimal cmpyAmendmentCharges ) {
        this.cmpyAmendmentCharges = cmpyAmendmentCharges;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

//    public OpsSellingPriceDetails(){
//        totalSellingPrice = new BigDecimal(8000);
//        commission = new BigDecimal(800);
//        discountValue = new BigDecimal(800);
//        totalNetSP = new BigDecimal(6400);
//        cmpyCancellationCharges = new BigDecimal(0);
//        cmpyAmendmentCharges = new BigDecimal(0);
//        exchangeRate = 1.5;
//    }
}
