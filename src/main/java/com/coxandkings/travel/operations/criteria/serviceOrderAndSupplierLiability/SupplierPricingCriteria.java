package com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class SupplierPricingCriteria {
    private String id;
    private Set<PassengerDetailsCriteria> passengerDetailsCriteria;
    private BigDecimal cancellationCharges;
    private BigDecimal amendmentCharges;
    private BigDecimal supplierCommercials;
    private BigDecimal surcharges;
    private BigDecimal supplements;
    private BigDecimal upgrades;
    private BigDecimal ancillaries;
    private BigDecimal supplierGst;
    private BigDecimal supplierCost;
    private BigDecimal supplierTotalCost;
    private BigDecimal netPayableToSupplier;
    private BigDecimal amountPaidToSupplier;
    private BigDecimal totalBalanceAmountPayable;
    private String paymentStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<PassengerDetailsCriteria> getPassengerDetailsCriteria() {
        return passengerDetailsCriteria;
    }

    public void setPassengerDetailsCriteria(Set<PassengerDetailsCriteria> passengerDetailsCriteria) {
        this.passengerDetailsCriteria = passengerDetailsCriteria;
    }

    public BigDecimal getCancellationCharges() {
        return cancellationCharges;
    }

    public void setCancellationCharges(BigDecimal cancellationCharges) {
        this.cancellationCharges = cancellationCharges;
    }

    public BigDecimal getAmendmentCharges() {
        return amendmentCharges;
    }

    public void setAmendmentCharges(BigDecimal amendmentCharges) {
        this.amendmentCharges = amendmentCharges;
    }

    public BigDecimal getSupplierCommercials() {
        return supplierCommercials;
    }

    public void setSupplierCommercials(BigDecimal supplierCommercials) {
        this.supplierCommercials = supplierCommercials;
    }

    public BigDecimal getSurcharges() {
        return surcharges;
    }

    public void setSurcharges(BigDecimal surcharges) {
        this.surcharges = surcharges;
    }

    public BigDecimal getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(BigDecimal upgrades) {
        this.upgrades = upgrades;
    }

    public BigDecimal getAncillaries() {
        return ancillaries;
    }

    public void setAncillaries(BigDecimal ancillaries) {
        this.ancillaries = ancillaries;
    }

    public BigDecimal getSupplierGst() {
        return supplierGst;
    }

    public void setSupplierGst(BigDecimal supplierGst) {
        this.supplierGst = supplierGst;
    }

    public BigDecimal getSupplierCost() {
        return supplierCost;
    }

    public void setSupplierCost(BigDecimal supplierCost) {
        this.supplierCost = supplierCost;
    }

    public BigDecimal getSupplierTotalCost() {
        return supplierTotalCost;
    }

    public void setSupplierTotalCost(BigDecimal supplierTotalCost) {
        this.supplierTotalCost = supplierTotalCost;
    }

    public BigDecimal getAmountPaidToSupplier() {
        return amountPaidToSupplier;
    }

    public void setAmountPaidToSupplier(BigDecimal amountPaidToSupplier) {
        this.amountPaidToSupplier = amountPaidToSupplier;
    }

    public BigDecimal getTotalBalanceAmountPayable() {
        return totalBalanceAmountPayable;
    }

    public void setTotalBalanceAmountPayable(BigDecimal totalBalanceAmountPayable) {
        this.totalBalanceAmountPayable = totalBalanceAmountPayable;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getSupplements() {
        return supplements;
    }

    public void setSupplements(BigDecimal supplements) {
        this.supplements = supplements;
    }

    public BigDecimal getNetPayableToSupplier() {
        return netPayableToSupplier;
    }

    public void setNetPayableToSupplier(BigDecimal netPayableToSupplier) {
        this.netPayableToSupplier = netPayableToSupplier;
    }


}
