package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "price_details")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsPriceDetails implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    private boolean refundable;

    @NotNull
    private String supplierRateType;

    @NotNull
    private BigDecimal actualMarginAmnt;

    @NotNull
    private String currency;

    @Embedded
    private OpsSellingPriceDetails opsSellingPriceDetails;

    @Embedded
    private List<OpsSupplierPriceDetails> opsSupplierPriceDetails;

    public boolean isRefundable() {
        return refundable;
    }

    public void setRefundable(boolean refundable) {
        this.refundable = refundable;
    }

    public String getSupplierRateType() {
        return supplierRateType;
    }

    public void setSupplierRateType(String supplierRateType) {
        this.supplierRateType = supplierRateType;
    }

    public BigDecimal getActualMarginAmnt() {
        return actualMarginAmnt;
    }

    public void setActualMarginAmnt(BigDecimal actualMarginAmnt) {
        this.actualMarginAmnt = actualMarginAmnt;
    }

    public OpsSellingPriceDetails getOpsSellingPriceDetails() {
        return opsSellingPriceDetails;
    }

    public void setOpsSellingPriceDetails(OpsSellingPriceDetails opsSellingPriceDetails) {
        this.opsSellingPriceDetails = opsSellingPriceDetails;
    }

    public List<OpsSupplierPriceDetails> getOpsSupplierPriceDetails() {
        return opsSupplierPriceDetails;
    }

    public void setOpsSupplierPriceDetails(List<OpsSupplierPriceDetails> opsSupplierPriceDetails) {
        this.opsSupplierPriceDetails = opsSupplierPriceDetails;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public OpsPriceDetails(){
        id = 1;
        refundable = true;
        supplierRateType = "Published";
        actualMarginAmnt = new BigDecimal(400);
        currency = "INR";
        opsSellingPriceDetails = new OpsSellingPriceDetails();

        opsSupplierPriceDetails = new ArrayList<>();
        opsSupplierPriceDetails.add(new OpsSupplierPriceDetails());
    }

//    public OpsPriceDetails(String supplierName){
//        id = 1;
//        refundable = true;
//        supplierRateType = "Published";
//        actualMarginAmnt = new BigDecimal(400);
//        currency = "INR";
//        opsSellingPriceDetails = new OpsSellingPriceDetails();
//
//        opsSupplierPriceDetails = new ArrayList<>();
//        opsSupplierPriceDetails.add(new OpsSupplierPriceDetails(supplierName));
//    }
}
