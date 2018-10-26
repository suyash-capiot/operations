
package com.coxandkings.travel.ext.model.finance.creditdebitnote;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class CreditDebitNote {

    private UUID id;
    private String invoiceNumber;
    private String generalInvoiceNumber;
    private String serviceOrderNumber;
    private String typeOfInvoice;
    private String notePhase;
    private String noteType;
    private String noteNumber;
    private long versionNumber;
    private boolean latest;
    private long issuedDate;
    private String issuedTo;
    private String client_supplierName;
    private String client_supplierID;
    private String productID;
    private String productCategory;
    private String productCategorySubType;
    private String productName;
    private String productSubName;
    private Set<String> bookingRefNumber;
    private String currency;
    private double roe;
    private String status;
    private String fundType;
    private boolean isTaxApplicable;
    private String taxType;
    private long transactionDate;
    private List<CostHeaderCharge> costHeaderCharges =
            new ArrayList<CostHeaderCharge>();
    private long totalPax;
    private double totalAmount;
    private double taxAmount;
    private double totalAmountWithTax;
    private double taxRate;
    private double supplierCost;
    private double sellingPrice;
    private String remark;
    private String rejectedReason;
    private String createdBy;
    private String lastUpdatedBy;
    private long createdOn;
    private long lastUpdatedOn;
    private boolean isConsumed;
    private String companyId;
    private String clientType;
    private Boolean isFromUI = false;
    private String linkedNoteNumber;
    private String linkedNoteType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
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

    public String getTypeOfInvoice() {
        return typeOfInvoice;
    }

    public void setTypeOfInvoice(String typeOfInvoice) {
        this.typeOfInvoice = typeOfInvoice;
    }

    public String getNotePhase() {
        return notePhase;
    }

    public void setNotePhase(String notePhase) {
        this.notePhase = notePhase;
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public String getNoteNumber() {
        return noteNumber;
    }

    public void setNoteNumber(String noteNumber) {
        this.noteNumber = noteNumber;
    }

    public long getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(long versionNumber) {
        this.versionNumber = versionNumber;
    }

    public boolean isLatest() {
        return latest;
    }

    public void setLatest(boolean latest) {
        this.latest = latest;
    }

    public long getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(long issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getIssuedTo() {
        return issuedTo;
    }

    public void setIssuedTo(String issuedTo) {
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

    public Set<String> getBookingRefNumber() {
        return bookingRefNumber;
    }

    public void setBookingRefNumber(Set<String> bookingRefNumber) {
        this.bookingRefNumber = bookingRefNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getRoe() {
        return roe;
    }

    public void setRoe(double roe) {
        this.roe = roe;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public boolean isTaxApplicable() {
        return isTaxApplicable;
    }

    public void setTaxApplicable(boolean taxApplicable) {
        isTaxApplicable = taxApplicable;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public long getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(long transactionDate) {
        this.transactionDate = transactionDate;
    }

    public List<CostHeaderCharge> getCostHeaderCharges() {
        return costHeaderCharges;
    }

    public void setCostHeaderCharges(List<CostHeaderCharge> costHeaderCharges) {
        this.costHeaderCharges = costHeaderCharges;
    }

    public long getTotalPax() {
        return totalPax;
    }

    public void setTotalPax(long totalPax) {
        this.totalPax = totalPax;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getTotalAmountWithTax() {
        return totalAmountWithTax;
    }

    public void setTotalAmountWithTax(double totalAmountWithTax) {
        this.totalAmountWithTax = totalAmountWithTax;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getSupplierCost() {
        return supplierCost;
    }

    public void setSupplierCost(double supplierCost) {
        this.supplierCost = supplierCost;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
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

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public long getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(long lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public boolean isConsumed() {
        return isConsumed;
    }

    public void setConsumed(boolean consumed) {
        isConsumed = consumed;
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

    public Boolean getIsFromUI() {
        return isFromUI;
    }

    public void setIsFromUI(Boolean fromUI) {
        isFromUI = fromUI;
    }

    public String getLinkedNoteNumber() {
        return linkedNoteNumber;
    }

    public void setLinkedNoteNumber(String linkedNoteNumber) {
        this.linkedNoteNumber = linkedNoteNumber;
    }

    public String getLinkedNoteType() {
        return linkedNoteType;
    }

    public void setLinkedNoteType(String linkedNoteType) {
        this.linkedNoteType = linkedNoteType;
    }
}
