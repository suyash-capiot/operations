package com.coxandkings.travel.operations.resource.creditDebitNote;

import com.coxandkings.travel.operations.enums.debitNote.*;

import java.util.List;

public class CreditDebitNote {
        private String id;
        private String invoiceNumber;
        private String generalInvoiceNumber;
        private String serviceOrderNumber;
        private TypeOfInvoice typeOfInvoice;
        private NotePhase notePhase;
        private NoteType noteType;
        private String noteNumber;
        private Integer versionNumber;
        private Boolean latest;
        private Long issuedDate;
        private IssuedTo issuedTo;
        private String client_supplierName;
        private String client_supplierID;
        private String productID;
        private String productCategory;
        private String productCategorySubType;
        private String productName;
        private String productSubName;
        private List<String> bookingRefNumber;
        private Status status;
        private Double roe;
        private FundType fundType;
        private TaxType taxType;
        private Long transactionDate;
        private List<CostHeaderCharges> costHeaderCharges;
        private Integer totalPax;
        private Double totalAmount;
        private Double taxAmount;
        private Double totalAmountWithTax;
        private Double taxRate;
        private Double supplierCost;
        private Double sellingPrice;
        private String remark;
        private String rejectedReason;
        private String createdBy;
        private String lastUpdatedBy;
        private String createdOn;
        private String lastUpdatedOn;
        private Boolean consumed;
        private Boolean taxApplicable;
        private String currency;
        private String companyId;
        private String clientType;

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

    public TypeOfInvoice getTypeOfInvoice() {
        return typeOfInvoice;
    }

    public void setTypeOfInvoice(TypeOfInvoice typeOfInvoice) {
        this.typeOfInvoice = typeOfInvoice;
    }

    public NotePhase getNotePhase() {
        return notePhase;
    }

    public void setNotePhase(NotePhase notePhase) {
        this.notePhase = notePhase;
    }

    public NoteType getNoteType() {
        return noteType;
    }

    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }

    public String getNoteNumber() {
        return noteNumber;
    }

    public void setNoteNumber(String noteNumber) {
        this.noteNumber = noteNumber;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Boolean getLatest() {
        return latest;
    }

    public void setLatest(Boolean latest) {
        this.latest = latest;
    }

    public Long getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Long issuedDate) {
        this.issuedDate = issuedDate;
    }

    public IssuedTo getIssuedTo() {
        return issuedTo;
    }

    public void setIssuedTo(IssuedTo issuedTo) {
        this.issuedTo = issuedTo;
    }

    public String getClient_supplierName() {
        return client_supplierName;
    }

    public void setClient_supplierName(String client_supplierName) {
        this.client_supplierName = client_supplierName;
    }

    public String getClient_supplierID() {
        return client_supplierID;
    }

    public void setClient_supplierID(String client_supplierID) {
        this.client_supplierID = client_supplierID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductCategorySubType() {
        return productCategorySubType;
    }

    public void setProductCategorySubType(String productCategorySubType) {
        this.productCategorySubType = productCategorySubType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSubName() {
        return productSubName;
    }

    public void setProductSubName(String productSubName) {
        this.productSubName = productSubName;
    }

    public String getGeneralInvoiceNumber() {
        return generalInvoiceNumber;
    }

    public void setGeneralInvoiceNumber(String generalInvoiceNumber) {
        this.generalInvoiceNumber = generalInvoiceNumber;
    }

    public String getServiceOrderNumber() {
        return serviceOrderNumber;
    }

    public void setServiceOrderNumber(String serviceOrderNumber) {
        this.serviceOrderNumber = serviceOrderNumber;
    }

    public List<String> getBookingRefNumber() {
        return bookingRefNumber;
    }

    public void setBookingRefNumber(List<String> bookingRefNumber) {
        this.bookingRefNumber = bookingRefNumber;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getRoe() {
        return roe;
    }

    public void setRoe(Double roe) {
        this.roe = roe;
    }


    public Long getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Long transactionDate) {
        this.transactionDate = transactionDate;
    }

    public List<CostHeaderCharges> getCostHeaderCharges() {
        return costHeaderCharges;
    }

    public void setCostHeaderCharges(List<CostHeaderCharges> costHeaderCharges) {
        this.costHeaderCharges = costHeaderCharges;
    }

    public Integer getTotalPax() {
        return totalPax;
    }

    public void setTotalPax(Integer totalPax) {
        this.totalPax = totalPax;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getTotalAmountWithTax() {
        return totalAmountWithTax;
    }

    public void setTotalAmountWithTax(Double totalAmountWithTax) {
        this.totalAmountWithTax = totalAmountWithTax;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Double getSupplierCost() {
        return supplierCost;
    }

    public void setSupplierCost(Double supplierCost) {
        this.supplierCost = supplierCost;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(String lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public Boolean getConsumed() {
        return consumed;
    }

    public void setConsumed(Boolean consumed) {
        this.consumed = consumed;
    }

    public Boolean getTaxApplicable() {
        return taxApplicable;
    }

    public void setTaxApplicable(Boolean taxApplicable) {
        this.taxApplicable = taxApplicable;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public FundType getFundType() {
        return fundType;
    }

    public void setFundType(FundType fundType) {
        this.fundType = fundType;
    }

    public TaxType getTaxType() {
        return taxType;
    }

    public void setTaxType(TaxType taxType) {
        this.taxType = taxType;
    }
}
