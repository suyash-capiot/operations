package com.coxandkings.travel.operations.model.forex;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class ForexBuyingCurrency {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "indent_id")
    private String indentId;

    private String supplierName;
    private String buyingCurrency;
    private BigDecimal buyingAmount;
    private BigDecimal discountedRoe;
    private BigDecimal inrEquivalent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuyingCurrency() {
        return buyingCurrency;
    }

    public void setBuyingCurrency(String buyingCurrency) {
        this.buyingCurrency = buyingCurrency;
    }

    public BigDecimal getBuyingAmount() {
        return buyingAmount;
    }

    public void setBuyingAmount(BigDecimal buyingAmount) {
        this.buyingAmount = buyingAmount;
    }

    public BigDecimal getDiscountedRoe() {
        return discountedRoe;
    }

    public void setDiscountedRoe(BigDecimal discountedRoe) {
        this.discountedRoe = discountedRoe;
    }

    public BigDecimal getInrEquivalent() {
        return inrEquivalent;
    }

    public void setInrEquivalent(BigDecimal inrEquivalent) {
        this.inrEquivalent = inrEquivalent;
    }

    public String getIndentId() {
        return indentId;
    }

    public void setIndentId(String indentId) {
        this.indentId = indentId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
