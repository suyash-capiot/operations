package com.coxandkings.travel.operations.model.modifiedFileProfitabiliy;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Entity
//@Table(name = "Price_component")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class PriceComponent {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private Double basFare;
    private boolean BaseFareUpdated;
    private Double total;
    private Double otherTaxesDoubAccom;
    @ElementCollection
    private Map<String, BigDecimal> fees;
    private boolean FeeUpdated;
    @ElementCollection
    private Map<String, BigDecimal> taxes;
    private boolean taxesUpdated;
    private boolean totalFeeUpdated;
    private BigDecimal totalFees;
    private boolean totalTaxesAmtUpdated;
    private BigDecimal totalTaxesAmt;
    @ElementCollection
    private List<String> modifiedTaxes;
    @ElementCollection
    private List<String> modifiedFees;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getBasFare() {
        return basFare;
    }

    public void setBasFare(Double basFare) {
        this.basFare = basFare;
    }

    public boolean isBaseFareUpdated() {
        return BaseFareUpdated;
    }

    public void setBaseFareUpdated(boolean baseFareUpdated) {
        BaseFareUpdated = baseFareUpdated;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getOtherTaxesDoubAccom() {
        return otherTaxesDoubAccom;
    }

    public void setOtherTaxesDoubAccom(Double otherTaxesDoubAccom) {
        this.otherTaxesDoubAccom = otherTaxesDoubAccom;
    }

    public Map<String, BigDecimal> getFees() {
        return fees;
    }

    public void setFees(Map<String, BigDecimal> fees) {
        this.fees = fees;
    }

    public boolean isFeeUpdated() {
        return FeeUpdated;
    }

    public void setFeeUpdated(boolean feeUpdated) {
        FeeUpdated = feeUpdated;
    }

    public Map<String, BigDecimal> getTaxes() {
        return taxes;
    }

    public void setTaxes(Map<String, BigDecimal> taxes) {
        this.taxes = taxes;
    }

    public boolean isTaxesUpdated() {
        return taxesUpdated;
    }

    public void setTaxesUpdated(boolean taxesUpdated) {
        this.taxesUpdated = taxesUpdated;
    }

    public boolean isTotalFeeUpdated() {
        return totalFeeUpdated;
    }

    public void setTotalFeeUpdated(boolean totalFeeUpdated) {
        this.totalFeeUpdated = totalFeeUpdated;
    }

    public BigDecimal getTotalFees() {
        return totalFees;
    }

    public void setTotalFees(BigDecimal totalFees) {
        this.totalFees = totalFees;
    }

    public boolean isTotalTaxesAmtUpdated() {
        return totalTaxesAmtUpdated;
    }

    public void setTotalTaxesAmtUpdated(boolean totalTaxesAmtUpdated) {
        this.totalTaxesAmtUpdated = totalTaxesAmtUpdated;
    }

    public BigDecimal getTotalTaxesAmt() {
        return totalTaxesAmt;
    }

    public void setTotalTaxesAmt(BigDecimal totalTaxesAmt) {
        this.totalTaxesAmt = totalTaxesAmt;
    }

    public List<String> getModifiedTaxes() {
        return modifiedTaxes;
    }

    public void setModifiedTaxes(List<String> modifiedTaxes) {
        this.modifiedTaxes = modifiedTaxes;
    }

    public List<String> getModifiedFees() {
        return modifiedFees;
    }

    public void setModifiedFees(List<String> modifiedFees) {
        this.modifiedFees = modifiedFees;
    }
}
