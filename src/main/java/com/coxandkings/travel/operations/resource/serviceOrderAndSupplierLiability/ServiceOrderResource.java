package com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.enums.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityType;
import com.coxandkings.travel.operations.enums.supplierBillPassing.StopPaymentStatus;
import com.coxandkings.travel.operations.enums.supplierBillPassing.StopPaymentUntil;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.Status;
import com.coxandkings.travel.operations.resource.supplierbillpassing.StopPaymentResource;
import com.coxandkings.travel.operations.resource.supplierbillpassing.SupplierBillPassingResource;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateSerializer;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

public class ServiceOrderResource {
    private String uniqueId;
    private Float versionNumber;
    private ServiceOrderAndSupplierLiabilityType type;
    private String companyMarketId;
    private String bookingRefNo;
    private String orderId;
    private String productCategoryId;
    private String productCategorySubTypeId;
    private String productNameId;
    private String productNameSubTypeOrProductFlavorName;
    private String supplierReferenceNumber;
    private String supplierId;
    private String supplierName;
    private String supplierCurrency;
    private BigDecimal netPayableToSupplier;
    private String supplierSettlementStatus;
    private String supplierBillPassingStatus;
    private String paymentAdviceId;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime dateOfGeneration;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime paymentDueDate;
    private Status status;
    private String serviceOrderStatus;
    private SupplierPricingResource supplierPricingResource;
    private String finalServiceOrderID;
    private String provisionalSupplierLiabilityID;
    private String finalSupplierLiabilityID;
    private String provisionalServiceOrderID;
    private Float linkedVersion;
    private String creditOrDebitNoteNumber;
    private String paymentAdviceStatus;
    private String productCategory;
    private String productCategorySubType;
    private String productNameOrFlavourName;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime travelCompletionDate;
    private BigDecimal diffAmount;
    private String createdByUserId;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime createdTime;
    private String lastModifiedByUserId;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime lastModifiedTime;
    private StopPaymentResource stopPaymentResource;
    private SupplierBillPassingResource supplierBillPassingResource;
    private BigDecimal diffInGst;
    private BigDecimal totalDiffAmount;
    private Boolean isGeneralInvoice;
    private String invoiceId;
    private String tripIndicator;
    private Set<PaymentAdvice> paymentAdviceSet = new HashSet<>();
    private BigDecimal attachedNetPayableToSupplier;
    private StopPaymentUntil stopPaymentTill;
    private ZonedDateTime stopPaymentTillDate;
    private String reason;
    private StopPaymentStatus stopPaymentStatus;
    private String supplierCredentialName;

//    private String paymentAdviceDate;

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

    public ServiceOrderAndSupplierLiabilityType getType() {
        return type;
    }

    public void setType(ServiceOrderAndSupplierLiabilityType type) {
        this.type = type;
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

    public String getSupplierSettlementStatus() {
        return supplierSettlementStatus;
    }

    public void setSupplierSettlementStatus(String supplierSettlementStatus) {
        this.supplierSettlementStatus = supplierSettlementStatus;
    }

    public String getPaymentAdviceId() {
        return paymentAdviceId;
    }

    public void setPaymentAdviceId(String paymentAdviceId) {
        this.paymentAdviceId = paymentAdviceId;
    }

    public SupplierPricingResource getSupplierPricingResource() {
        return supplierPricingResource;
    }

    public void setSupplierPricingResource(SupplierPricingResource supplierPricingResource) {
        this.supplierPricingResource = supplierPricingResource;
    }

    public ZonedDateTime getDateOfGeneration() {
        return dateOfGeneration;
    }

    public void setDateOfGeneration(ZonedDateTime dateOfGeneration) {
        this.dateOfGeneration = dateOfGeneration;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public String getProvisionalServiceOrderID() {
        return provisionalServiceOrderID;
    }

    public void setProvisionalServiceOrderID(String provisionalServiceOrderID) {
        this.provisionalServiceOrderID = provisionalServiceOrderID;
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getPaymentAdviceStatus() {
        return paymentAdviceStatus;
    }

    public void setPaymentAdviceStatus(String paymentAdviceStatus) {
        this.paymentAdviceStatus = paymentAdviceStatus;
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

    public String getProductNameOrFlavourName() {
        return productNameOrFlavourName;
    }

    public void setProductNameOrFlavourName(String productNameOrFlavourName) {
        this.productNameOrFlavourName = productNameOrFlavourName;
    }

    public ZonedDateTime getTravelCompletionDate() {
        return travelCompletionDate;
    }

    public void setTravelCompletionDate(ZonedDateTime travelCompletionDate) {
        this.travelCompletionDate = travelCompletionDate;
    }

    public BigDecimal getDiffAmount() {
        return diffAmount;
    }

    public void setDiffAmount(BigDecimal diffAmount) {
        this.diffAmount = diffAmount;
    }

    public String getSupplierBillPassingStatus() {
        return supplierBillPassingStatus;
    }

    public void setSupplierBillPassingStatus(String supplierBillPassingStatus) {
        this.supplierBillPassingStatus = supplierBillPassingStatus;
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

    public StopPaymentResource getStopPaymentResource() {
        return stopPaymentResource;
    }

    public void setStopPaymentResource(StopPaymentResource stopPaymentResource) {
        this.stopPaymentResource = stopPaymentResource;
    }

    public SupplierBillPassingResource getSupplierBillPassingResource() {
        return supplierBillPassingResource;
    }

    public void setSupplierBillPassingResource(SupplierBillPassingResource supplierBillPassingResource) {
        this.supplierBillPassingResource = supplierBillPassingResource;
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

    public ZonedDateTime getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(ZonedDateTime paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
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

    public String getTripIndicator() {
        return tripIndicator;
    }

    public void setTripIndicator(String tripIndicator) {
        this.tripIndicator = tripIndicator;
    }

    public Set<PaymentAdvice> getPaymentAdviceSet() {
        return paymentAdviceSet;
    }

    public void setPaymentAdviceSet(Set<PaymentAdvice> paymentAdviceSet) {
        this.paymentAdviceSet = paymentAdviceSet;
    }

    public String getProductNameSubTypeOrProductFlavorName() {
        return productNameSubTypeOrProductFlavorName;
    }

    public void setProductNameSubTypeOrProductFlavorName(String productNameSubTypeOrProductFlavorName) {
        this.productNameSubTypeOrProductFlavorName = productNameSubTypeOrProductFlavorName;
    }

    public String getSupplierReferenceNumber() {
        return supplierReferenceNumber;
    }

    public void setSupplierReferenceNumber(String supplierReferenceNumber) {
        this.supplierReferenceNumber = supplierReferenceNumber;
    }

    public String getServiceOrderStatus() {
        return serviceOrderStatus;
    }

    public void setServiceOrderStatus(String serviceOrderStatus) {
        this.serviceOrderStatus = serviceOrderStatus;
    }

    public BigDecimal getAttachedNetPayableToSupplier() {
        return attachedNetPayableToSupplier;
    }

    public void setAttachedNetPayableToSupplier(BigDecimal attachedNetPayableToSupplier) {
        this.attachedNetPayableToSupplier = attachedNetPayableToSupplier;
    }

    public StopPaymentUntil getStopPaymentTill() {
        return stopPaymentTill;
    }

    public void setStopPaymentTill(StopPaymentUntil stopPaymentTill) {
        this.stopPaymentTill = stopPaymentTill;
    }

    public ZonedDateTime getStopPaymentTillDate() {
        return stopPaymentTillDate;
    }

    public void setStopPaymentTillDate(ZonedDateTime stopPaymentTillDate) {
        this.stopPaymentTillDate = stopPaymentTillDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public StopPaymentStatus getStopPaymentStatus() {
        return stopPaymentStatus;
    }

    public void setStopPaymentStatus(StopPaymentStatus stopPaymentStatus) {
        this.stopPaymentStatus = stopPaymentStatus;
    }

    public String getSupplierCredentialName() {
        return supplierCredentialName;
    }

    public void setSupplierCredentialName(String supplierCredentialName) {
        this.supplierCredentialName = supplierCredentialName;
    }

    public BigDecimal getNetPayableToSupplier() {
        return netPayableToSupplier;
    }

    public void setNetPayableToSupplier(BigDecimal netPayableToSupplier) {
        this.netPayableToSupplier = netPayableToSupplier;
    }
}
