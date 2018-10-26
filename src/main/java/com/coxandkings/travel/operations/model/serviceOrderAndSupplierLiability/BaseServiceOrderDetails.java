package com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.enums.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityType;
import com.coxandkings.travel.operations.enums.supplierBillPassing.StopPaymentStatus;
import com.coxandkings.travel.operations.enums.supplierBillPassing.StopPaymentUntil;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateSerializer;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Table(name = "baseServiceOrderDetails")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonPropertyOrder({"serviceOrderAndSupplierLiability"})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ProvisionalServiceOrder.class, name = "PSO"),
                @JsonSubTypes.Type(value = FinalServiceOrder.class, name = "FSO"),
                @JsonSubTypes.Type(value = ProvisionalSupplierLiability.class, name = "PSL"),
                @JsonSubTypes.Type(value = FinalSupplierLiability.class, name = "FSL")
        })
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@IdClass(VersionId.class)
@Audited
public abstract class BaseServiceOrderDetails implements Serializable {

    @Id
    @Column
    private String uniqueId;

//    @Id
    @Column
    private Float versionNumber;

    @Column
    @NotNull(message = "service order or supplier liability type should not be null")
    @Enumerated(EnumType.STRING)
    private ServiceOrderAndSupplierLiabilityType type;

    @Column
   // @NotNull(message = "company market id should not be null")
    private String companyMarketId;

    @Column
    private String companyName;
    @Column
    private String companyId;
    @Column
    private String companyGroupName;
    @Column
    private String companyGroupId;
    @Column
    private String groupOfCompanyName;
    @Column
    private String groupOfCompanyId;
    @Column
    private String branchName;
    @Column
    private String SBU;
    @Column
    private String BU;


    @Column
    @NotNull(message = "booking reference number should not be null")
    private String bookingRefNo;

    @Column
    @NotNull(message = "product id should not be null")
    private String orderId;

    @Column
    @NotNull(message = "product category id should not be null")
    private String productCategoryId;

    @Column
    @NotNull(message = "product category sub type id should not be null")
    private String productCategorySubTypeId;

    @Column
//    @NotNull(message = "product name or flavour name id should not be null")
    private String productNameId;

    @Column
    private String productNameSubTypeOrProductFlavorName;

    @Column
    private String supplierReferenceNumber;

    @Column
    @NotNull(message = "supplier id should not be null")
    private String supplierId;

    @Column
    private String supplierName;

    @Column
    @NotNull(message = "supplier currency should not be null")
    private String supplierCurrency;

    @Column
    private BigDecimal netPayableToSupplier;

    @Column
    private String supplierSettlementStatus;

    @Column
    @NotNull(message = "date of generation should not be null")
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime dateOfGeneration;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private String finalServiceOrderID;

    @Column
    private String provisionalSupplierLiabilityID;

    @Column
    private String finalSupplierLiabilityID;

    @Column
    private String provisionalServiceOrderID;

    @Column
    private Float linkedVersion;

    @OneToOne(cascade = CascadeType.ALL)
    @Audited
    @JsonManagedReference
    private SupplierPricing supplierPricing;

    @Column
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime travelCompletionDate;

    @Column(name = "created_by_user_id")
    private String createdByUserId;

    @Column(name = "created_time")
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime createdTime;

    @Column(name = "last_modified_by_user_id")
    private String lastModifiedByUserId;

    @Column(name = "last_modified_time")
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime lastModifiedTime;

    @Column
    private BigDecimal diffAmount;

    @Column
    private BigDecimal diffInGst;

    @Column
    private BigDecimal totalDiffAmount;

    @Column
    private String creditOrDebitNoteNumber;

    @ManyToMany
    @JsonIgnore
    @Audited(targetAuditMode = NOT_AUDITED)
    private Set<PaymentAdvice> paymentAdviceSet = new HashSet<>();


    @Column
    // @NotNull(message = "payment due date should not be null")
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime paymentDueDate;

    @Column
    private Boolean isGeneralInvoice;

    private BigDecimal attachedNetPayableToSupplier;

    @Column
    private String invoiceId;

    @Enumerated()
    private StopPaymentUntil stopPaymentTill;

    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime stopPaymentTillDate;

    private String reason;

    @Enumerated(EnumType.STRING)
    private StopPaymentStatus stopPaymentStatus;

    private String tripIndicator;

    @Column
    private String supplierCredentialName;

    public BigDecimal getDiffAmount() {
        return diffAmount;
    }

    public void setDiffAmount(BigDecimal diffAmount) {
        this.diffAmount = diffAmount;
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

    public String getCompanyMarketId() {
        return companyMarketId;
    }

    public void setCompanyMarketId(String companyMarketId) {
        this.companyMarketId = companyMarketId;
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

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
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

    public String getBookingRefNo() {
        return bookingRefNo;
    }

    public void setBookingRefNo(String bookingRefNo) {
        this.bookingRefNo = bookingRefNo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ServiceOrderAndSupplierLiabilityType getType() {
        return type;
    }

    public void setType(ServiceOrderAndSupplierLiabilityType type) {
        this.type = type;
    }

    public String getProductNameId() {
        return productNameId;
    }

    public void setProductNameId(String productNameId) {
        this.productNameId = productNameId;
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

    public ZonedDateTime getDateOfGeneration() {
        return dateOfGeneration;
    }

    public void setDateOfGeneration(ZonedDateTime dateOfGeneration) {
        this.dateOfGeneration = dateOfGeneration;
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

    public SupplierPricing getSupplierPricing() {
        return supplierPricing;
    }

    public void setSupplierPricing(SupplierPricing supplierPricing) {
        this.supplierPricing = supplierPricing;
    }

    public Float getLinkedVersion() {
        return linkedVersion;
    }

    public void setLinkedVersion(Float linkedVersion) {
        this.linkedVersion = linkedVersion;
    }

    public ZonedDateTime getTravelCompletionDate() {
        return travelCompletionDate;
    }

    public void setTravelCompletionDate(ZonedDateTime travelCompletionDate) {
        this.travelCompletionDate = travelCompletionDate;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    public void setLastModifiedByUserId(String lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(ZonedDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getCreditOrDebitNoteNumber() {
        return creditOrDebitNoteNumber;
    }

    public void setCreditOrDebitNoteNumber(String creditOrDebitNoteNumber) {
        this.creditOrDebitNoteNumber = creditOrDebitNoteNumber;
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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

    public BigDecimal getAttachedNetPayableToSupplier() {
        return attachedNetPayableToSupplier;
    }

    public void setAttachedNetPayableToSupplier(BigDecimal attachedNetPayableToSupplier) {
        this.attachedNetPayableToSupplier = attachedNetPayableToSupplier;
    }

    public String getTripIndicator() {
        return tripIndicator;
    }

    public void setTripIndicator(String tripIndicator) {
        this.tripIndicator = tripIndicator;
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

    public Set<PaymentAdvice> getPaymentAdviceSet() {
        return paymentAdviceSet;
    }

    public void setPaymentAdviceSet(Set<PaymentAdvice> paymentAdviceSet) {
        this.paymentAdviceSet = paymentAdviceSet;
    }

    public String getSupplierCredentialName() {
        return supplierCredentialName;
    }

    public void setSupplierCredentialName(String supplierCredentialName) {
        this.supplierCredentialName = supplierCredentialName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;

    }

    public BigDecimal getNetPayableToSupplier() {
        return netPayableToSupplier;
    }

    public void setNetPayableToSupplier(BigDecimal netPayableToSupplier) {
        this.netPayableToSupplier = netPayableToSupplier;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyGroupName() {
        return companyGroupName;
    }

    public void setCompanyGroupName(String companyGroupName) {
        this.companyGroupName = companyGroupName;
    }

    public String getCompanyGroupId() {
        return companyGroupId;
    }

    public void setCompanyGroupId(String companyGroupId) {
        this.companyGroupId = companyGroupId;
    }

    public String getGroupOfCompanyName() {
        return groupOfCompanyName;
    }

    public void setGroupOfCompanyName(String groupOfCompanyName) {
        this.groupOfCompanyName = groupOfCompanyName;
    }

    public String getGroupOfCompanyId() {
        return groupOfCompanyId;
    }

    public void setGroupOfCompanyId(String groupOfCompanyId) {
        this.groupOfCompanyId = groupOfCompanyId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getSBU() {
        return SBU;
    }

    public void setSBU(String SBU) {
        this.SBU = SBU;
    }

    public String getBU() {
        return BU;
    }

    public void setBU(String BU) {
        this.BU = BU;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseServiceOrderDetails that = (BaseServiceOrderDetails) o;
        return Objects.equals(uniqueId, that.uniqueId) &&
                Objects.equals(versionNumber, that.versionNumber) &&
                type == that.type &&
                Objects.equals(companyMarketId, that.companyMarketId) &&
                Objects.equals(bookingRefNo, that.bookingRefNo) &&
                Objects.equals(orderId, that.orderId) &&
                Objects.equals(productCategoryId, that.productCategoryId) &&
                Objects.equals(productCategorySubTypeId, that.productCategorySubTypeId) &&
                Objects.equals(productNameId, that.productNameId) &&
                Objects.equals(productNameSubTypeOrProductFlavorName, that.productNameSubTypeOrProductFlavorName) &&
                Objects.equals(supplierReferenceNumber, that.supplierReferenceNumber) &&
                Objects.equals(supplierId, that.supplierId) &&
                Objects.equals(supplierName, that.supplierName) &&
                Objects.equals(supplierCurrency, that.supplierCurrency) &&
                Objects.equals(supplierSettlementStatus, that.supplierSettlementStatus) &&
                Objects.equals(dateOfGeneration, that.dateOfGeneration) &&
                status == that.status &&
                Objects.equals(finalServiceOrderID, that.finalServiceOrderID) &&
                Objects.equals(provisionalSupplierLiabilityID, that.provisionalSupplierLiabilityID) &&
                Objects.equals(finalSupplierLiabilityID, that.finalSupplierLiabilityID) &&
                Objects.equals(provisionalServiceOrderID, that.provisionalServiceOrderID) &&
                Objects.equals(linkedVersion, that.linkedVersion) &&
                Objects.equals(supplierPricing, that.supplierPricing) &&
                Objects.equals(travelCompletionDate, that.travelCompletionDate) &&
                Objects.equals(createdByUserId, that.createdByUserId) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(lastModifiedByUserId, that.lastModifiedByUserId) &&
                Objects.equals(lastModifiedTime, that.lastModifiedTime) &&
                Objects.equals(diffAmount, that.diffAmount) &&
                Objects.equals(diffInGst, that.diffInGst) &&
                Objects.equals(totalDiffAmount, that.totalDiffAmount) &&
                Objects.equals(creditOrDebitNoteNumber, that.creditOrDebitNoteNumber) &&
                Objects.equals(paymentAdviceSet, that.paymentAdviceSet) &&
                Objects.equals(paymentDueDate, that.paymentDueDate) &&
                Objects.equals(isGeneralInvoice, that.isGeneralInvoice) &&
                Objects.equals(attachedNetPayableToSupplier, that.attachedNetPayableToSupplier) &&
                Objects.equals(invoiceId, that.invoiceId) &&
                stopPaymentTill == that.stopPaymentTill &&
                Objects.equals(stopPaymentTillDate, that.stopPaymentTillDate) &&
                Objects.equals(reason, that.reason) &&
                stopPaymentStatus == that.stopPaymentStatus &&
                Objects.equals(tripIndicator, that.tripIndicator) &&
                Objects.equals(supplierCredentialName, that.supplierCredentialName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uniqueId, versionNumber, type, companyMarketId, bookingRefNo, orderId, productCategoryId, productCategorySubTypeId, productNameId, productNameSubTypeOrProductFlavorName, supplierReferenceNumber, supplierId, supplierName, supplierCurrency, supplierSettlementStatus, dateOfGeneration, status, finalServiceOrderID, provisionalSupplierLiabilityID, finalSupplierLiabilityID, provisionalServiceOrderID, linkedVersion, supplierPricing, travelCompletionDate, createdByUserId, createdTime, lastModifiedByUserId, lastModifiedTime, diffAmount, diffInGst, totalDiffAmount, creditOrDebitNoteNumber, paymentAdviceSet, paymentDueDate, isGeneralInvoice, attachedNetPayableToSupplier, invoiceId, stopPaymentTill, stopPaymentTillDate, reason, stopPaymentStatus, tripIndicator, supplierCredentialName);
    }
}
