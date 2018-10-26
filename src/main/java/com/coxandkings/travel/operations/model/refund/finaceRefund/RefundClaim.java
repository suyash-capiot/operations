package com.coxandkings.travel.operations.model.refund.finaceRefund;

import com.coxandkings.travel.operations.enums.refund.RefundStatus;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;


@JsonIgnoreProperties(ignoreUnknown = true)
public class RefundClaim {

    private UUID id;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime claimCreationDate;
    private String claimNumber;
    private String bookingReferenceNumber;
    private String bookingStatus;
    /*
     * private long bookingDate; private long travelDate;
     */
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime createdOn;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime lastUpdatedOn;

    private String createdBy;
    private String lastUpdatedBy;
    private SearchBy searchBy;
    /*private RefunVoucherDTO refundVoucher;*/
    private String generatedCreditNote;
    private String redeemableCreditNote;
    private String generatedGiftVoucher;
    public RefundStatus refundUserStatus;
    private String userId;
    private boolean locked;
    private String bspRaNumber;
    private Long generatedClientAccountEntryId;
    private Long generatedCompanyAccountEntryId;
    private boolean pendingForRoeChangeApproval;
    private boolean pendingForPayModeChangeApproval;
    private boolean pendingForTypeChangeApproval;
    private boolean pendingForApprovalWithTreasuryUser;
    private RequestInitiator roeChangeInitiator;
    private RequestInitiator payModeChangeInitiator;
    private String reasonOfPendingApproval;
    private CompanyDetail companyDetail;
    private ClientDetail clientDetail;
    private Set<CollectionDetail> collectionDetail;
    private ProductDetail productDetail;
    private RefundDetail refundDetail;
    private Set<InvoiceDetail> invoiceDetail;
    private boolean reduceAccountCredit = false;
    private boolean requestAfterTypeChange = false;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ZonedDateTime getClaimCreationDate() {
        return claimCreationDate;
    }

    public void setClaimCreationDate(ZonedDateTime claimCreationDate) {
        this.claimCreationDate = claimCreationDate;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getBookingReferenceNumber() {
        return bookingReferenceNumber;
    }

    public void setBookingReferenceNumber(String bookingReferenceNumber) {
        this.bookingReferenceNumber = bookingReferenceNumber;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getRedeemableCreditNote() {
        return redeemableCreditNote;
    }

    public void setRedeemableCreditNote(String redeemableCreditNote) {
        this.redeemableCreditNote = redeemableCreditNote;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public ZonedDateTime getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(ZonedDateTime lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
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

    public SearchBy getSearchBy() {
        return searchBy;
    }

    public void setSearchBy(SearchBy searchBy) {
        this.searchBy = searchBy;
    }

    public String getGeneratedCreditNote() {
        return generatedCreditNote;
    }

    public void setGeneratedCreditNote(String generatedCreditNote) {
        this.generatedCreditNote = generatedCreditNote;
    }

    public String getGeneratedGiftVoucher() {
        return generatedGiftVoucher;
    }

    public void setGeneratedGiftVoucher(String generatedGiftVoucher) {
        this.generatedGiftVoucher = generatedGiftVoucher;
    }

    public RefundStatus getRefundUserStatus() {
        return refundUserStatus;
    }

    public void setRefundUserStatus(RefundStatus refundUserStatus) {
        this.refundUserStatus = refundUserStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getBspRaNumber() {
        return bspRaNumber;
    }

    public void setBspRaNumber(String bspRaNumber) {
        this.bspRaNumber = bspRaNumber;
    }

    public Long getGeneratedClientAccountEntryId() {
        return generatedClientAccountEntryId;
    }

    public void setGeneratedClientAccountEntryId(Long generatedClientAccountEntryId) {
        this.generatedClientAccountEntryId = generatedClientAccountEntryId;
    }

    public Long getGeneratedCompanyAccountEntryId() {
        return generatedCompanyAccountEntryId;
    }

    public void setGeneratedCompanyAccountEntryId(Long generatedCompanyAccountEntryId) {
        this.generatedCompanyAccountEntryId = generatedCompanyAccountEntryId;
    }

    public boolean isPendingForRoeChangeApproval() {
        return pendingForRoeChangeApproval;
    }

    public void setPendingForRoeChangeApproval(boolean pendingForRoeChangeApproval) {
        this.pendingForRoeChangeApproval = pendingForRoeChangeApproval;
    }

    public boolean isPendingForPayModeChangeApproval() {
        return pendingForPayModeChangeApproval;
    }

    public void setPendingForPayModeChangeApproval(boolean pendingForPayModeChangeApproval) {
        this.pendingForPayModeChangeApproval = pendingForPayModeChangeApproval;
    }

    public boolean isPendingForTypeChangeApproval() {
        return pendingForTypeChangeApproval;
    }

    public void setPendingForTypeChangeApproval(boolean pendingForTypeChangeApproval) {
        this.pendingForTypeChangeApproval = pendingForTypeChangeApproval;
    }

    public boolean isPendingForApprovalWithTreasuryUser() {
        return pendingForApprovalWithTreasuryUser;
    }

    public void setPendingForApprovalWithTreasuryUser(boolean pendingForApprovalWithTreasuryUser) {
        this.pendingForApprovalWithTreasuryUser = pendingForApprovalWithTreasuryUser;
    }

    public RequestInitiator getRoeChangeInitiator() {
        return roeChangeInitiator;
    }

    public void setRoeChangeInitiator(RequestInitiator roeChangeInitiator) {
        this.roeChangeInitiator = roeChangeInitiator;
    }

    public RequestInitiator getPayModeChangeInitiator() {
        return payModeChangeInitiator;
    }

    public void setPayModeChangeInitiator(RequestInitiator payModeChangeInitiator) {
        this.payModeChangeInitiator = payModeChangeInitiator;
    }

    public String getReasonOfPendingApproval() {
        return reasonOfPendingApproval;
    }

    public void setReasonOfPendingApproval(String reasonOfPendingApproval) {
        this.reasonOfPendingApproval = reasonOfPendingApproval;
    }

    public CompanyDetail getCompanyDetail() {
        return companyDetail;
    }

    public void setCompanyDetail(CompanyDetail companyDetail) {
        this.companyDetail = companyDetail;
    }

    public ClientDetail getClientDetail() {
        return clientDetail;
    }

    public void setClientDetail(ClientDetail clientDetail) {
        this.clientDetail = clientDetail;
    }

    public Set<CollectionDetail> getCollectionDetail() {
        return collectionDetail;
    }

    public void setCollectionDetail(Set<CollectionDetail> collectionDetail) {
        this.collectionDetail = collectionDetail;
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
    }

    public RefundDetail getRefundDetail() {
        return refundDetail;
    }

    public void setRefundDetail(RefundDetail refundDetail) {
        this.refundDetail = refundDetail;
    }

    public Set<InvoiceDetail> getInvoiceDetail() {
        return invoiceDetail;
    }

    public void setInvoiceDetail(Set<InvoiceDetail> invoiceDetail) {
        this.invoiceDetail = invoiceDetail;
    }

    public boolean isReduceAccountCredit() {
        return reduceAccountCredit;
    }

    public void setReduceAccountCredit(boolean reduceAccountCredit) {
        this.reduceAccountCredit = reduceAccountCredit;
    }

    public boolean isRequestAfterTypeChange() {
        return requestAfterTypeChange;
    }

    public void setRequestAfterTypeChange(boolean requestAfterTypeChange) {
        this.requestAfterTypeChange = requestAfterTypeChange;
    }

    @Override
    public String toString() {
        return "RefundClaim{" +
                "id=" + id +
                ", claimCreationDate=" + claimCreationDate +
                ", claimNumber='" + claimNumber + '\'' +
                ", bookingReferenceNumber='" + bookingReferenceNumber + '\'' +
                ", bookingStatus='" + bookingStatus + '\'' +
                ", createdOn=" + createdOn +
                ", lastUpdatedOn=" + lastUpdatedOn +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", searchBy=" + searchBy +
                ", generatedCreditNote='" + generatedCreditNote + '\'' +
                ", redeemableCreditNote='" + redeemableCreditNote + '\'' +
                ", generatedGiftVoucher='" + generatedGiftVoucher + '\'' +
                ", refundUserStatus=" + refundUserStatus +
                ", userId='" + userId + '\'' +
                ", locked=" + locked +
                ", bspRaNumber='" + bspRaNumber + '\'' +
                ", generatedClientAccountEntryId=" + generatedClientAccountEntryId +
                ", generatedCompanyAccountEntryId=" + generatedCompanyAccountEntryId +
                ", pendingForRoeChangeApproval=" + pendingForRoeChangeApproval +
                ", pendingForPayModeChangeApproval=" + pendingForPayModeChangeApproval +
                ", pendingForTypeChangeApproval=" + pendingForTypeChangeApproval +
                ", pendingForApprovalWithTreasuryUser=" + pendingForApprovalWithTreasuryUser +
                ", roeChangeInitiator=" + roeChangeInitiator +
                ", payModeChangeInitiator=" + payModeChangeInitiator +
                ", reasonOfPendingApproval='" + reasonOfPendingApproval + '\'' +
                ", companyDetail=" + companyDetail +
                ", clientDetail=" + clientDetail +
                ", collectionDetail=" + collectionDetail +
                ", productDetail=" + productDetail +
                ", refundDetail=" + refundDetail +
                ", invoiceDetail=" + invoiceDetail +
                ",reduceAccountCredit" + redeemableCreditNote +
                '}';
    }
}
