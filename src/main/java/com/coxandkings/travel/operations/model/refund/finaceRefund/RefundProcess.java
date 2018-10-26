package com.coxandkings.travel.operations.model.refund.finaceRefund;

import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;


@JsonIgnoreProperties(ignoreUnknown = true)
public class RefundProcess {

    private String bookingReferenceNumber;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime createdOn;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime lastUpdatedOn;

    private String createdBy;
    private String lastUpdatedBy;
    private BookingStatus bookingStatus;
    private CompanyDetail companyDetail;
    private ClientDetail clientDetail;
    private ProductDetail productDetail;
    private RefundDetail refundDetail;
    private String redeemableCreditNote;
    private String bspRaNumber;
    private boolean isFromUI=false;
    private boolean requestAfterTypeChange;

    private boolean pendingForTypeChangeApproval;

    private Boolean reduceAccountCredit;


    public String getBspRaNumber() {
        return bspRaNumber;
    }

    public void setBspRaNumber(String bspRaNumber) {
        this.bspRaNumber = bspRaNumber;
    }

    public String getBookingReferenceNumber() {
        return bookingReferenceNumber;
    }

    public void setBookingReferenceNumber(String bookingReferenceNumber) {
        this.bookingReferenceNumber = bookingReferenceNumber;
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

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
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

    public void setRedeemableCreditNote(String redeemableCreditNote) {
        this.redeemableCreditNote = redeemableCreditNote;
    }

    public String getRedeemableCreditNote() {
        return redeemableCreditNote;
    }

    public boolean getIsFromUI() {
        return isFromUI;
    }

    public void setIsFromUI(boolean fromUI) {
        isFromUI = fromUI;
    }

    public boolean isRequestAfterTypeChange() {
        return requestAfterTypeChange;
    }

    public void setRequestAfterTypeChange(boolean requestAfterTypeChange) {
        this.requestAfterTypeChange = requestAfterTypeChange;
    }

    public boolean isPendingForTypeChangeApproval() {
        return pendingForTypeChangeApproval;
    }

    public void setPendingForTypeChangeApproval(boolean pendingForTypeChangeApproval) {
        this.pendingForTypeChangeApproval = pendingForTypeChangeApproval;
    }

    public Boolean getReduceAccountCredit() {
        return reduceAccountCredit;
    }

    public void setReduceAccountCredit(Boolean reduceAccountCredit) {
        this.reduceAccountCredit = reduceAccountCredit;
    }
}
