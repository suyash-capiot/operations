package com.coxandkings.travel.operations.resource.commercialStatement;

import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateSerializer;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

public class CommercialStatementsBillPassingResource {

    private String id;

    private String invoiceNumber;

    private Boolean manualEntry;

    private String supplierOrClientId;

    private String supplierOrClientName;

    private String commercialStatementFor;

    private Set<String> productName;

    @JsonSerialize(using = ZonedDateSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime invoiceDate;

    @JsonSerialize(using = ZonedDateSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime invoiceReceivedDate;

    private BigDecimal invoiceAmount;

    private String invoiceCurrency;

    private BigDecimal equivalentCommercialStatementAmount;

    private BigDecimal clientServiceTaxAmount;

    private String remarks;

    private BigDecimal netPayableToSupplierOrClient;

    @JsonSerialize(using = ZonedDateSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime paymentDueDate;

    private String media;

    private String billPassingStatus;

    private Set<AttachedCommercialStatement> attachedCommercialStatements;

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

    public String getSupplierOrClientId() {
        return supplierOrClientId;
    }

    public void setSupplierOrClientId(String supplierOrClientId) {
        this.supplierOrClientId = supplierOrClientId;
    }

    public String getSupplierOrClientName() {
        return supplierOrClientName;
    }

    public void setSupplierOrClientName(String supplierOrClientName) {
        this.supplierOrClientName = supplierOrClientName;
    }

    public String getCommercialStatementFor() {
        return commercialStatementFor;
    }

    public void setCommercialStatementFor(String commercialStatementFor) {
        this.commercialStatementFor = commercialStatementFor;
    }

    public Set<String> getProductName() {
        return productName;
    }

    public void setProductName(Set<String> productName) {
        this.productName = productName;
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

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getInvoiceCurrency() {
        return invoiceCurrency;
    }

    public void setInvoiceCurrency(String invoiceCurrency) {
        this.invoiceCurrency = invoiceCurrency;
    }

    public BigDecimal getEquivalentCommercialStatementAmount() {
        return equivalentCommercialStatementAmount;
    }

    public void setEquivalentCommercialStatementAmount(BigDecimal equivalentCommercialStatementAmount) {
        this.equivalentCommercialStatementAmount = equivalentCommercialStatementAmount;
    }

    public BigDecimal getClientServiceTaxAmount() {
        return clientServiceTaxAmount;
    }

    public void setClientServiceTaxAmount(BigDecimal clientServiceTaxAmount) {
        this.clientServiceTaxAmount = clientServiceTaxAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getNetPayableToSupplierOrClient() {
        return netPayableToSupplierOrClient;
    }

    public void setNetPayableToSupplierOrClient(BigDecimal netPayableToSupplierOrClient) {
        this.netPayableToSupplierOrClient = netPayableToSupplierOrClient;
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

    public String getBillPassingStatus() {
        return billPassingStatus;
    }

    public void setBillPassingStatus(String billPassingStatus) {
        this.billPassingStatus = billPassingStatus;
    }

    public Set<AttachedCommercialStatement> getAttachedCommercialStatements() {
        return attachedCommercialStatements;
    }

    public void setAttachedCommercialStatements(Set<AttachedCommercialStatement> attachedCommercialStatements) {
        this.attachedCommercialStatements = attachedCommercialStatements;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
