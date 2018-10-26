package com.coxandkings.travel.operations.model.commercialstatements;

import com.coxandkings.travel.operations.model.BaseModel;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
public class CommercialStatementsBillPassing extends BaseModel {

    @NotEmpty
    @Column(name="invoice_id")
    private String invoiceNumber;

    @NotNull
    private Boolean manualEntry;

    @NotEmpty
    private String supplierOrClientId;

    @NotEmpty
    private String supplierOrClientName;

    @NotEmpty
    private String commercialStatementFor;

    @NotEmpty
    @ElementCollection
    private Set<String> productName;

    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime invoiceDate;

    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime invoiceReceivedDate;

    @NotNull
    private BigDecimal invoiceAmount;

    @NotNull
    private String invoiceCurrency;

    private BigDecimal equivalentCommercialStatementAmount;

    @Column
    private String clientServiceTaxAmount;

    private String remarks;

    @NotNull
    private BigDecimal netPayableToSupplierOrClient;

    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime paymentDueDate;

    private String media;

    private String billPassingStatus;

    @OneToMany(mappedBy = "commercialStatementsBillPassing",cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<SupplierCommercialStatement> supplierCommercialStatements;

    @OneToMany(mappedBy = "commercialStatementsBillPassing",cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ClientCommercialStatement> clientCommercialStatements;

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

    public String getClientServiceTaxAmount() {
        return clientServiceTaxAmount;
    }

    public void setClientServiceTaxAmount(String clientServiceTaxAmount) {
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

    public Set<SupplierCommercialStatement> getSupplierCommercialStatements() {
        return supplierCommercialStatements;
    }

    public void setSupplierCommercialStatements(Set<SupplierCommercialStatement> supplierCommercialStatements) {
        this.supplierCommercialStatements = supplierCommercialStatements;
    }

    public Set<ClientCommercialStatement> getClientCommercialStatements() {
        return clientCommercialStatements;
    }

    public void setClientCommercialStatements(Set<ClientCommercialStatement> clientCommercialStatements) {
        this.clientCommercialStatements = clientCommercialStatements;
    }
}
