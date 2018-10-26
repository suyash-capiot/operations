package com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "supplier_pricing")
@Audited
public class SupplierPricing implements Serializable {
    @Id
    @Column
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    //@JsonBackReference
    @Column
    @OneToMany(mappedBy = "supplierPricing", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Audited
    private Set<PassengersDetails> passengerDetails;

    @Column
    @NotNull(message = "cancellation charges should not be null")
    private BigDecimal cancellationCharges;

    @Column
    @NotNull(message = "amendment charges should not be null")
    private BigDecimal amendmentCharges;

    @Column
    @NotNull(message = "supplier commercials value should not be null")
    private BigDecimal supplierCommercials;

    @Column
    @NotNull(message = "surcharges should not be null")
    private BigDecimal surcharges;

    @Column
    private BigDecimal supplements;

    @Column
    private BigDecimal upgrades;

    @Column
    private BigDecimal ancillaries;

    @Column
    @NotNull(message = "supplier gst should not be null")
    private BigDecimal supplierGst;

    @Column
    @NotNull(message = "supplier cost should not be null")
    private BigDecimal supplierCost;

    @Column
    private BigDecimal supplierTotalCost;

    @Column
    @NotNull(message = "net payable to suppplier should not be null")
    private BigDecimal netPayableToSupplier;

    @Column
    @NotNull(message = "amount paid to supplier should not be null")
    private BigDecimal amountPaidToSupplier;

    @Column
    @NotNull(message = "balance amount payable to supplier should not be null")
    private BigDecimal totalBalanceAmountPayable;

    @Column
    private String paymentStatus;

    @OneToMany(mappedBy = "supplierPricing")
    //@JoinColumns({@JoinColumn(name = "uniqueId"),@JoinColumn(name = "versionNumber")})
    @JsonBackReference
    private List<BaseServiceOrderDetails> serviceOrderAndSupplierLiabilityList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<PassengersDetails> getPassengerDetails() {
        return passengerDetails;
    }

    public void setPassengerDetails(Set<PassengersDetails> passengerDetails) {
        this.passengerDetails = passengerDetails;
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

    public List<BaseServiceOrderDetails> getServiceOrderAndSupplierLiabilityList() {
        return serviceOrderAndSupplierLiabilityList;
    }

    public void setServiceOrderAndSupplierLiabilityList(List<BaseServiceOrderDetails> serviceOrderAndSupplierLiabilityList) {
        this.serviceOrderAndSupplierLiabilityList = serviceOrderAndSupplierLiabilityList;
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

    public BigDecimal getSupplierTotalCost() {
        return supplierTotalCost;
    }

    public void setSupplierTotalCost(BigDecimal supplierTotalCost) {
        this.supplierTotalCost = supplierTotalCost;
    }

    @Override
    public String toString() {
        return "SupplierPricing{" +
                "id='" + id + '\'' +
                ", passengerDetails=" + passengerDetails +
                ", cancellationCharges=" + cancellationCharges +
                ", amendmentCharges=" + amendmentCharges +
                ", supplierCommercials=" + supplierCommercials +
                ", surcharges=" + surcharges +
                ", supplements=" + supplements +
                ", upgrades=" + upgrades +
                ", ancillaries=" + ancillaries +
                ", supplierGst=" + supplierGst +
                ", supplierCost=" + supplierCost +
                ", supplierTotalCost=" + supplierTotalCost +
                ", netPayableToSupplier=" + netPayableToSupplier +
                ", amountPaidToSupplier=" + amountPaidToSupplier +
                ", totalBalanceAmountPayable=" + totalBalanceAmountPayable +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", serviceOrderAndSupplierLiabilityList=" + serviceOrderAndSupplierLiabilityList +
                '}';
    }
}
