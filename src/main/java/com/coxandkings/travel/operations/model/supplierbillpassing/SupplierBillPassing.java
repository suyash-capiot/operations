package com.coxandkings.travel.operations.model.supplierbillpassing;

import com.coxandkings.travel.operations.model.BaseModel;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.FinalServiceOrder;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.FinalSupplierLiability;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.ProvisionalServiceOrder;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.ProvisionalSupplierLiability;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name="supplier_bill_passing")
public class SupplierBillPassing extends BaseModel {

    @NotEmpty
    @Column(name="invoice_id")
    private String invoiceNumber;

    @NotNull
    private Boolean manualEntry;

    @NotEmpty
    private String supplierId;

    @ElementCollection
    private Set<String> productName;

    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime invoiceDate;

    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime invoiceReceivedDate;

    //@NotNull
    private BigDecimal supplierCost;

    //@NotNull
    private BigDecimal supplierGst;

    @NotNull
    private BigDecimal supplierInvoiceTotalCost;

    //@NotNull
    private BigDecimal supplierInvoiceTotalCommission;

    @NotNull
    private String supplierInvoiceCurrency;

    @NotNull
    private BigDecimal equivalentServiceOrderAmount;

    @Column
    @NotEmpty
    private String supplierName;

    private String remarks;

    private String hsn_SAS_code;

    private String taxableAmount;

    private String gstIN_number;

    private String supplierState;

    private String purchaseRefNo;

    private String taxAmount;

    public String getHsn_SAS_code() {
        return hsn_SAS_code;
    }

    public void setHsn_SAS_code(String hsn_SAS_code) {
        this.hsn_SAS_code = hsn_SAS_code;
    }

    public String getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(String taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public String getGstIN_number() {
        return gstIN_number;
    }

    public void setGstIN_number(String gstIN_number) {
        this.gstIN_number = gstIN_number;
    }

    public String getSupplierState() {
        return supplierState;
    }

    public void setSupplierState(String supplierState) {
        this.supplierState = supplierState;
    }

    public String getPurchaseRefNo() {
        return purchaseRefNo;
    }

    public void setPurchaseRefNo(String purchaseRefNo) {
        this.purchaseRefNo = purchaseRefNo;
    }

    @NotNull
    private BigDecimal netPayableToSupplier;

    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime paymentDueDate;


    @OneToMany(mappedBy="supplierBillPassing",cascade = CascadeType.ALL)
    @Audited
    @JsonIgnore
    private Set<ProvisionalServiceOrder> provisionalServiceOrders;

    @OneToMany(mappedBy="supplierBillPassing",cascade = CascadeType.ALL)
    @Audited
    @JsonIgnore
    private Set<FinalServiceOrder> finalServiceOrders;

    @OneToMany(mappedBy="supplierBillPassing",cascade = CascadeType.ALL)
    @Audited
    @JsonIgnore
    private Set<ProvisionalSupplierLiability> provisionalSupplierLiabilities;

    @OneToMany(mappedBy="supplierBillPassing",cascade = CascadeType.ALL)
    @Audited
    @JsonIgnore
    private Set<FinalSupplierLiability> finalSupplierLiabilities;

    private String media;

    private String supplierBillPassingStatus;

    public BigDecimal getSupplierGst() { return supplierGst; }

    public void setSupplierGst(BigDecimal supplierGst) { this.supplierGst = supplierGst; }

    public String getSupplierId() { return supplierId; }

    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public ZonedDateTime getInvoiceDate() { return invoiceDate; }

    public void setInvoiceDate(ZonedDateTime invoiceDate) { this.invoiceDate = invoiceDate; }

    public ZonedDateTime getInvoiceReceivedDate() { return invoiceReceivedDate; }

    public void setInvoiceReceivedDate(ZonedDateTime invoiceReceivedDate) { this.invoiceReceivedDate = invoiceReceivedDate; }

    public String getSupplierInvoiceCurrency() { return supplierInvoiceCurrency; }

    public void setSupplierInvoiceCurrency(String supplierInvoiceCurrency) { this.supplierInvoiceCurrency = supplierInvoiceCurrency; }

    public BigDecimal getNetPayableToSupplier() { return netPayableToSupplier; }

    public void setNetPayableToSupplier(BigDecimal netPayableToSupplier) { this.netPayableToSupplier = netPayableToSupplier; }

    public ZonedDateTime getPaymentDueDate() { return paymentDueDate; }

    public void setPaymentDueDate(ZonedDateTime paymentDueDate) { this.paymentDueDate = paymentDueDate; }

    public Set<ProvisionalServiceOrder> getProvisionalServiceOrders() {
        return provisionalServiceOrders;
    }

    public void setProvisionalServiceOrders(Set<ProvisionalServiceOrder> provisionalServiceOrders) {
        this.provisionalServiceOrders = provisionalServiceOrders;
    }

    public Boolean getManualEntry() {
        return manualEntry;
    }

    public void setManualEntry(Boolean manualEntry) {
        this.manualEntry = manualEntry;
    }

    public BigDecimal getSupplierCost() {
        return supplierCost;
    }

    public void setSupplierCost(BigDecimal supplierCost) {
        this.supplierCost = supplierCost;
    }

    public BigDecimal getSupplierInvoiceTotalCost() {
        return supplierInvoiceTotalCost;
    }

    public void setSupplierInvoiceTotalCost(BigDecimal supplierInvoiceTotalCost) { this.supplierInvoiceTotalCost = supplierInvoiceTotalCost; }

    public BigDecimal getSupplierInvoiceTotalCommission() {
        return supplierInvoiceTotalCommission;
    }

    public void setSupplierInvoiceTotalCommission(BigDecimal supplierInvoiceTotalCommission) { this.supplierInvoiceTotalCommission = supplierInvoiceTotalCommission; }

    public BigDecimal getEquivalentServiceOrderAmount() {
        return equivalentServiceOrderAmount;
    }

    public void setEquivalentServiceOrderAmount(BigDecimal equivalentServiceOrderAmount) { this.equivalentServiceOrderAmount = equivalentServiceOrderAmount; }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public Set<FinalServiceOrder> getFinalServiceOrders() {
        return finalServiceOrders;
    }

    public void setFinalServiceOrders(Set<FinalServiceOrder> finalServiceOrders) {
        this.finalServiceOrders = finalServiceOrders;
    }

    public Set<ProvisionalSupplierLiability> getProvisionalSupplierLiabilities() {
        return provisionalSupplierLiabilities;
    }

    public void setProvisionalSupplierLiabilities(Set<ProvisionalSupplierLiability> provisionalSupplierLiabilities) {
        this.provisionalSupplierLiabilities = provisionalSupplierLiabilities;
    }

    public Set<FinalSupplierLiability> getFinalSupplierLiabilities() {
        return finalSupplierLiabilities;
    }

    public void setFinalSupplierLiabilities(Set<FinalSupplierLiability> finalSupplierLiabilities) {
        this.finalSupplierLiabilities = finalSupplierLiabilities;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Set<String> getProductName() {
        return productName;
    }

    public void setProductName(Set<String> productName) {
        this.productName = productName;
    }

    public String getSupplierBillPassingStatus() {
        return supplierBillPassingStatus;
    }

    public void setSupplierBillPassingStatus(String supplierBillPassingStatus) {
        this.supplierBillPassingStatus = supplierBillPassingStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }
}
