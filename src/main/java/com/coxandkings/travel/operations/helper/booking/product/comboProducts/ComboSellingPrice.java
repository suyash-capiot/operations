package com.coxandkings.travel.operations.helper.booking.product.comboProducts;

import java.math.BigDecimal;


public class ComboSellingPrice {

    private Integer id;
    private BigDecimal totalSellingPrice;
    private BigDecimal commission;
    private BigDecimal discountValue;
    private BigDecimal totalNetSP;
    private BigDecimal cmpyCancellationCharges;
    private BigDecimal cmpyAmendmentCharges;
    private float exchangeRate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getTotalSellingPrice() {
        return totalSellingPrice;
    }

    public void setTotalSellingPrice(BigDecimal totalSellingPrice) {
        this.totalSellingPrice = totalSellingPrice;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getTotalNetSP() {
        return totalNetSP;
    }

    public void setTotalNetSP(BigDecimal totalNetSP) {
        this.totalNetSP = totalNetSP;
    }

    public BigDecimal getCmpyCancellationCharges() {
        return cmpyCancellationCharges;
    }

    public void setCmpyCancellationCharges(BigDecimal cmpyCancellationCharges) {
        this.cmpyCancellationCharges = cmpyCancellationCharges;
    }

    public BigDecimal getCmpyAmendmentCharges() {
        return cmpyAmendmentCharges;
    }

    public void setCmpyAmendmentCharges(BigDecimal cmpyAmendmentCharges) {
        this.cmpyAmendmentCharges = cmpyAmendmentCharges;
    }

    public float getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(float exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
