package com.coxandkings.travel.operations.model.modifiedFileProfitabiliy;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Prof_Supplier_Cost_price")
public class ProfSupplierCostPrice {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @OneToOne(cascade = CascadeType.ALL)
    private SellingPrice grossSupplierCost;
    private BigDecimal supplierCommercialsPayable;
    private boolean supplierCPUpdated;
    private BigDecimal supplierCommercialsReceivable;
    private boolean supplierCRUpdated;
    private BigDecimal totalNetPayableToSupplier;
    private boolean totNetPayToSuppUpdated;

    public boolean getSupplierCPUpdated() {
        return supplierCPUpdated;
    }

    public void setSupplierCPUpdated(boolean supplierCPUpdated) {
        this.supplierCPUpdated = supplierCPUpdated;
    }

    public boolean getSupplierCRUpdated() {
        return supplierCRUpdated;
    }

    public void setSupplierCRUpdated(boolean supplierCRUpdated) {
        this.supplierCRUpdated = supplierCRUpdated;
    }

    public boolean getTotNetPayToSuppUpdated() {
        return totNetPayToSuppUpdated;
    }

    public void setTotNetPayToSuppUpdated(boolean totNetPayToSuppUpdated) {
        this.totNetPayToSuppUpdated = totNetPayToSuppUpdated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SellingPrice getGrossSupplierCost() {
        return grossSupplierCost;
    }

    public void setGrossSupplierCost(SellingPrice grossSupplierCost) {
        this.grossSupplierCost = grossSupplierCost;
    }

    public BigDecimal getSupplierCommercialsPayable() {
        return supplierCommercialsPayable;
    }

    public void setSupplierCommercialsPayable(BigDecimal supplierCommercialsPayable) {
        this.supplierCommercialsPayable = supplierCommercialsPayable;
    }

    public BigDecimal getSupplierCommercialsReceivable() {
        return supplierCommercialsReceivable;
    }

    public void setSupplierCommercialsReceivable(BigDecimal supplierCommercialsReceivable) {
        this.supplierCommercialsReceivable = supplierCommercialsReceivable;
    }

    public BigDecimal getTotalNetPayableToSupplier() {
        return totalNetPayableToSupplier;
    }

    public void setTotalNetPayableToSupplier(BigDecimal totalNetPayableToSupplier) {
        this.totalNetPayableToSupplier = totalNetPayableToSupplier;
    }
}
