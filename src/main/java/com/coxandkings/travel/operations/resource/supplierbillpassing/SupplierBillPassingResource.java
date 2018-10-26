package com.coxandkings.travel.operations.resource.supplierbillpassing;

import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateSerializer;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

public class SupplierBillPassingResource {

    private String id;

    private String invoiceNumber;

    private Boolean manualEntry;

    private String supplierId;

    private Set<String> productName;

    private String supplierName;

    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime invoiceDate;

    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime invoiceReceivedDate;

    private BigDecimal supplierCost;

    private BigDecimal supplierGst;

    private BigDecimal supplierInvoiceTotalCost;

    private BigDecimal supplierInvoiceTotalCommission;

    private String supplierInvoiceCurrency;

    private BigDecimal equivalentServiceOrderAmount;

    private BigDecimal netPayableToSupplier;

    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime paymentDueDate;

    private String media;

    private String supplierBillPassingStatus;

    private String remarks;

    private String hsn_SAS_code;

    private String taxableAmount;

    private String gstIN_number;

    private String supplierState;

    private String purchaseRefNo;

    private String taxAmount;

    private String invoiceOcrId;

    private Set<AttachedServiceOrder> attachedServiceOrders;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Boolean getManualEntry() {
        return manualEntry;
    }

    public void setManualEntry(Boolean manualEntry) {
        this.manualEntry = manualEntry;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public BigDecimal getSupplierCost() {
        return supplierCost;
    }

    public void setSupplierCost(BigDecimal supplierCost) {
        this.supplierCost = supplierCost;
    }

    public BigDecimal getSupplierGst() {
        return supplierGst;
    }

    public void setSupplierGst(BigDecimal supplierGst) {
        this.supplierGst = supplierGst;
    }

    public BigDecimal getSupplierInvoiceTotalCost() {
        return supplierInvoiceTotalCost;
    }

    public void setSupplierInvoiceTotalCost(BigDecimal supplierInvoiceTotalCost) {
        this.supplierInvoiceTotalCost = supplierInvoiceTotalCost;
    }

    public BigDecimal getSupplierInvoiceTotalCommission() {
        return supplierInvoiceTotalCommission;
    }

    public void setSupplierInvoiceTotalCommission(BigDecimal supplierInvoiceTotalCommission) {
        this.supplierInvoiceTotalCommission = supplierInvoiceTotalCommission;
    }

    public String getSupplierInvoiceCurrency() {
        return supplierInvoiceCurrency;
    }

    public void setSupplierInvoiceCurrency(String supplierInvoiceCurrency) {
        this.supplierInvoiceCurrency = supplierInvoiceCurrency;
    }

    public BigDecimal getEquivalentServiceOrderAmount() {
        return equivalentServiceOrderAmount;
    }

    public void setEquivalentServiceOrderAmount(BigDecimal equivalentServiceOrderAmount) {
        this.equivalentServiceOrderAmount = equivalentServiceOrderAmount;
    }

    public BigDecimal getNetPayableToSupplier() {
        return netPayableToSupplier;
    }

    public void setNetPayableToSupplier(BigDecimal netPayableToSupplier) {
        this.netPayableToSupplier = netPayableToSupplier;
    }

    public ZonedDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(ZonedDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public ZonedDateTime getInvoiceReceivedDate() {
        return invoiceReceivedDate;
    }

    public void setInvoiceReceivedDate(ZonedDateTime invoiceReceivedDate) {
        this.invoiceReceivedDate = invoiceReceivedDate;
    }

    public Set<String> getProductName() {
        return productName;
    }

    public void setProductName(Set<String> productName) {
        this.productName = productName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public ZonedDateTime getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(ZonedDateTime paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getSupplierBillPassingStatus() {
        return supplierBillPassingStatus;
    }

    public void setSupplierBillPassingStatus(String supplierBillPassingStatus) {
        this.supplierBillPassingStatus = supplierBillPassingStatus;
    }

    public Set<AttachedServiceOrder> getAttachedServiceOrders() {
        return attachedServiceOrders;
    }

    public void setAttachedServiceOrders(Set<AttachedServiceOrder> attachedServiceOrders) {
        this.attachedServiceOrders = attachedServiceOrders;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

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

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }
}
