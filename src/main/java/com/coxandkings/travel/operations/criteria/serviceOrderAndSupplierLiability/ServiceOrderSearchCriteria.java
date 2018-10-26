package com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.enums.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityType;
import com.coxandkings.travel.operations.enums.supplierBillPassing.SupplierBillPassingSortingCriteria;
import com.coxandkings.travel.operations.enums.supplierBillPassing.SupplierBillPassingSortingOrder;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateSerializer;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public class ServiceOrderSearchCriteria {

    private String uniqueId;
    private Float versionNumber;
    private ServiceOrderAndSupplierLiabilityType serviceOrderType;
    private String serviceOrderTypeValue;
    private String companyMarketId;
    private String bookingRefNo;
    private List<String> orderIds;
    private String orderId;
    private String productCategoryId;
    private String productCategorySubTypeId;
    private String productNameId;
    private String productCategory;
    private String productCategorySubType;
    private String productName;
    private String productNameSubTypeOrProductFlavorName;
    private String supplierName;
    private String supplierId;
    private String supplierCurrency;
    private String provisionalServiceOrderID;
    private String finalServiceOrderID;
    private String provisionalSupplierLiabilityID;
    private String finalSupplierLiabilityID;
    private String status;
    private String supplierSettlementStatus;
    private String supplierBillPassingStatus;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime fromGenerationDate;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime toGenerationDate;
    private String paymentAdviceId;
    private Float linkedVersion;
    private String creditOrDebitNoteNumber;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime travelCompletionDate;
    private Integer pageSize;
    private Integer pageNumber;
    private BigDecimal differenceInSupplierInvoice;
    private String paymentAdviceStatusValues;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime paymentAdviceFromDate;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime paymentAdviceToDate;
    private String createdByUserId;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime createdTime;
    private String lastModifiedByUserId;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime lastModifiedTime;
    private BigDecimal diffInGst;
    private BigDecimal totalDiffAmount;
    private Boolean isGeneralInvoice;
    private String invoiceId;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime paymentDueDate;
    private ZonedDateTime paymentDueDateFrom;
    private ZonedDateTime paymentDueDateTo;
    private Set<String> attachedServiceOrderIds;
    private Boolean billPassingResource;
    private Boolean paymentAdviceResource;
    private String tripIndicator;
    private Boolean isSupplierBillPassing;
    private String supplierCredentialName;
    private Boolean applyPagination;
    private SupplierBillPassingSortingCriteria supplierBillPassingSortingCriteria;
    private SupplierBillPassingSortingOrder supplierBillPassingSortingOrder;

    public SupplierBillPassingSortingCriteria getSupplierBillPassingSortingCriteria() {
        return supplierBillPassingSortingCriteria;
    }

    public void setSupplierBillPassingSortingCriteria(SupplierBillPassingSortingCriteria supplierBillPassingSortingCriteria) {
        this.supplierBillPassingSortingCriteria = supplierBillPassingSortingCriteria;
    }

    public SupplierBillPassingSortingOrder getSupplierBillPassingSortingOrder() {
        return supplierBillPassingSortingOrder;
    }

    public void setSupplierBillPassingSortingOrder(SupplierBillPassingSortingOrder supplierBillPassingSortingOrder) {
        this.supplierBillPassingSortingOrder = supplierBillPassingSortingOrder;
    }

    public Boolean getBillPassingResource() {
        return billPassingResource;
    }

    public void setBillPassingResource(Boolean billPassingResource) {
        this.billPassingResource = billPassingResource;
    }

    public Boolean getPaymentAdviceResource() {
        return paymentAdviceResource;
    }

    public void setPaymentAdviceResource(Boolean paymentAdviceResource) {
        this.paymentAdviceResource = paymentAdviceResource;
    }

    public Set<String> getAttachedServiceOrderIds() {
        return attachedServiceOrderIds;
    }

    public void setAttachedServiceOrderIds(Set<String> attachedServiceOrderIds) {
        this.attachedServiceOrderIds = attachedServiceOrderIds;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Float getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Float versionNumber) {
        this.versionNumber = versionNumber;
    }

    public ServiceOrderAndSupplierLiabilityType getServiceOrderType() {
        return serviceOrderType;
    }

    public void setServiceOrderType(ServiceOrderAndSupplierLiabilityType serviceOrderType) {
        this.serviceOrderType = serviceOrderType;
    }

    public String getServiceOrderTypeValue() {
        return serviceOrderTypeValue;
    }

    public void setServiceOrderTypeValue(String serviceOrderTypeValue) {
        this.serviceOrderTypeValue = serviceOrderTypeValue;
    }

    public String getCompanyMarketId() {
        return companyMarketId;
    }

    public void setCompanyMarketId(String companyMarketId) {
        this.companyMarketId = companyMarketId;
    }

    public String getBookingRefNo() {
        return bookingRefNo;
    }

    public void setBookingRefNo(String bookingRefNo) {
        this.bookingRefNo = bookingRefNo;
    }

    public List<String> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(String productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductCategorySubTypeId() {
        return productCategorySubTypeId;
    }

    public void setProductCategorySubTypeId(String productCategorySubTypeId) {
        this.productCategorySubTypeId = productCategorySubTypeId;
    }

    public String getProductNameId() {
        return productNameId;
    }

    public void setProductNameId(String productNameId) {
        this.productNameId = productNameId;
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

    public String getProductNameSubTypeOrProductFlavorName() {
        return productNameSubTypeOrProductFlavorName;
    }

    public void setProductNameSubTypeOrProductFlavorName(String productNameSubTypeOrProductFlavorName) {
        this.productNameSubTypeOrProductFlavorName = productNameSubTypeOrProductFlavorName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierCurrency() {
        return supplierCurrency;
    }

    public void setSupplierCurrency(String supplierCurrency) {
        this.supplierCurrency = supplierCurrency;
    }

    public String getProvisionalServiceOrderID() {
        return provisionalServiceOrderID;
    }

    public void setProvisionalServiceOrderID(String provisionalServiceOrderID) {
        this.provisionalServiceOrderID = provisionalServiceOrderID;
    }

    public String getFinalServiceOrderID() {
        return finalServiceOrderID;
    }

    public void setFinalServiceOrderID(String finalServiceOrderID) {
        this.finalServiceOrderID = finalServiceOrderID;
    }

    public String getProvisionalSupplierLiabilityID() {
        return provisionalSupplierLiabilityID;
    }

    public void setProvisionalSupplierLiabilityID(String provisionalSupplierLiabilityID) {
        this.provisionalSupplierLiabilityID = provisionalSupplierLiabilityID;
    }

    public String getFinalSupplierLiabilityID() {
        return finalSupplierLiabilityID;
    }

    public void setFinalSupplierLiabilityID(String finalSupplierLiabilityID) {
        this.finalSupplierLiabilityID = finalSupplierLiabilityID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupplierSettlementStatus() {
        return supplierSettlementStatus;
    }

    public void setSupplierSettlementStatus(String supplierSettlementStatus) {
        this.supplierSettlementStatus = supplierSettlementStatus;
    }

    public String getSupplierBillPassingStatus() {
        return supplierBillPassingStatus;
    }

    public void setSupplierBillPassingStatus(String supplierBillPassingStatus) {
        this.supplierBillPassingStatus = supplierBillPassingStatus;
    }

    public ZonedDateTime getFromGenerationDate() {
        return fromGenerationDate;
    }

    public void setFromGenerationDate(ZonedDateTime fromGenerationDate) {
        this.fromGenerationDate = fromGenerationDate;
    }

    public ZonedDateTime getToGenerationDate() {
        return toGenerationDate;
    }

    public void setToGenerationDate(ZonedDateTime toGenerationDate) {
        this.toGenerationDate = toGenerationDate;
    }

    public String getPaymentAdviceId() {
        return paymentAdviceId;
    }

    public void setPaymentAdviceId(String paymentAdviceId) {
        this.paymentAdviceId = paymentAdviceId;
    }

    public Float getLinkedVersion() {
        return linkedVersion;
    }

    public void setLinkedVersion(Float linkedVersion) {
        this.linkedVersion = linkedVersion;
    }

    public String getCreditOrDebitNoteNumber() {
        return creditOrDebitNoteNumber;
    }

    public void setCreditOrDebitNoteNumber(String creditOrDebitNoteNumber) {
        this.creditOrDebitNoteNumber = creditOrDebitNoteNumber;
    }

    public ZonedDateTime getTravelCompletionDate() {
        return travelCompletionDate;
    }

    public void setTravelCompletionDate(ZonedDateTime travelCompletionDate) {
        this.travelCompletionDate = travelCompletionDate;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public BigDecimal getDifferenceInSupplierInvoice() {
        return differenceInSupplierInvoice;
    }

    public void setDifferenceInSupplierInvoice(BigDecimal differenceInSupplierInvoice) {
        this.differenceInSupplierInvoice = differenceInSupplierInvoice;
    }

    public String getPaymentAdviceStatusValues() {
        return paymentAdviceStatusValues;
    }

    public void setPaymentAdviceStatusValues(String paymentAdviceStatusValues) {
        this.paymentAdviceStatusValues = paymentAdviceStatusValues;
    }

    public ZonedDateTime getPaymentAdviceFromDate() {
        return paymentAdviceFromDate;
    }

    public void setPaymentAdviceFromDate(ZonedDateTime paymentAdviceFromDate) {
        this.paymentAdviceFromDate = paymentAdviceFromDate;
    }

    public ZonedDateTime getPaymentAdviceToDate() {
        return paymentAdviceToDate;
    }

    public void setPaymentAdviceToDate(ZonedDateTime paymentAdviceToDate) {
        this.paymentAdviceToDate = paymentAdviceToDate;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    public void setLastModifiedByUserId(String lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }

    public ZonedDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(ZonedDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public BigDecimal getDiffInGst() {
        return diffInGst;
    }

    public void setDiffInGst(BigDecimal diffInGst) {
        this.diffInGst = diffInGst;
    }

    public BigDecimal getTotalDiffAmount() {
        return totalDiffAmount;
    }

    public void setTotalDiffAmount(BigDecimal totalDiffAmount) {
        this.totalDiffAmount = totalDiffAmount;
    }

    public Boolean getGeneralInvoice() {
        return isGeneralInvoice;
    }

    public void setGeneralInvoice(Boolean generalInvoice) {
        isGeneralInvoice = generalInvoice;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public ZonedDateTime getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(ZonedDateTime paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public String getTripIndicator() {
        return tripIndicator;
    }

    public void setTripIndicator(String tripIndicator) {
        this.tripIndicator = tripIndicator;
    }

    public Boolean getSupplierBillPassing() {
        return isSupplierBillPassing;
    }

    public void setSupplierBillPassing(Boolean supplierBillPassing) {
        isSupplierBillPassing = supplierBillPassing;
    }

    public String getSupplierCredentialName() {
        return supplierCredentialName;
    }

    public void setSupplierCredentialName(String supplierCredentialName) {
        this.supplierCredentialName = supplierCredentialName;
    }

    public Boolean getApplyPagination() {
        return applyPagination;
    }

    public void setApplyPagination(Boolean applyPagination) {
        this.applyPagination = applyPagination;
    }

    public ZonedDateTime getPaymentDueDateFrom() {
        return paymentDueDateFrom;
    }

    public void setPaymentDueDateFrom(ZonedDateTime paymentDueDateFrom) {
        this.paymentDueDateFrom = paymentDueDateFrom;
    }

    public ZonedDateTime getPaymentDueDateTo() {
        return paymentDueDateTo;
    }

    public void setPaymentDueDateTo(ZonedDateTime paymentDueDateTo) {
        this.paymentDueDateTo = paymentDueDateTo;
    }
}
