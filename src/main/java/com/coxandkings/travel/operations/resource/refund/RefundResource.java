package com.coxandkings.travel.operations.resource.refund;


import com.coxandkings.travel.operations.enums.refund.ReasonForRequest;
import com.coxandkings.travel.operations.enums.refund.RefundTypes;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.coxandkings.travel.operations.model.refund.finaceRefund.ClientType;
import com.coxandkings.travel.operations.model.refund.finaceRefund.ProductDetail;
import com.coxandkings.travel.operations.resource.BaseResource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RefundResource extends BaseResource {
    private String claimNo;
    private String clientName;

    @Enumerated(EnumType.STRING)
    private RefundTypes refundType;

    @JsonProperty("dueOn")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime dueOn;
    private String clientId;

    @JsonProperty("clientType")
    @Enumerated(EnumType.STRING)
    private ClientType clientType;
    private BigDecimal refundAmount;
    private String claimCurrency;
    private String refundStatus;

    @JsonProperty("refundProcessedDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime refundProcessedDate;
    private String creditNoteNo;
    private String redeemableCreditNote;
    private String defaultModeOfPayment;
    private String requestedModeOfPayment;
    @Enumerated(EnumType.STRING)
    private ReasonForRequest reasonForRequest;

    @JsonProperty("refundCutOff")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime refundCutOff;
    private BigDecimal roeAsInClaim;
    private BigDecimal roeRequested;
    private BigDecimal netAmountPayable;
    private BigDecimal netAmountRequest;

    private String bookingReferenceNo;
    private ProductDetail productDetail;
    // In case of flight booking we required this number
    private String bspRaNumber;
    private boolean isFromUI=false;


    public String getClaimNo() {
        return claimNo;
    }

    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public RefundTypes getRefundType() {
        return refundType;
    }

    public void setRefundType(RefundTypes refundType) {
        this.refundType = refundType;
    }

    public ZonedDateTime getDueOn() {
        return dueOn;
    }

    public void setDueOn(ZonedDateTime dueOn) {
        this.dueOn = dueOn;
    }

    public String getClientId() {
        return clientId;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {

        this.clientType = ClientType.getClientType(clientType);
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getClaimCurrency() {
        return claimCurrency;
    }

    public void setClaimCurrency(String claimCurrency) {
        this.claimCurrency = claimCurrency;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public ZonedDateTime getRefundProcessedDate() {
        return refundProcessedDate;
    }

    public void setRefundProcessedDate(ZonedDateTime refundProcessedDate) {
        this.refundProcessedDate = refundProcessedDate;
    }

    public String getRedeemableCreditNote() {
        return redeemableCreditNote;
    }

    public void setRedeemableCreditNote(String redeemableCreditNote) {
        this.redeemableCreditNote = redeemableCreditNote;
    }

    public String getCreditNoteNo() {
        return creditNoteNo;
    }

    public void setCreditNoteNo(String creditNoteNo) {
        this.creditNoteNo = creditNoteNo;
    }

    public String getDefaultModeOfPayment() {
        return defaultModeOfPayment;
    }

    public void setDefaultModeOfPayment(String defaultModeOfPayment) {
        this.defaultModeOfPayment = defaultModeOfPayment;
    }

    public String getRequestedModeOfPayment() {
        return requestedModeOfPayment;
    }

    public void setRequestedModeOfPayment(String requestedModeOfPayment) {
        this.requestedModeOfPayment = requestedModeOfPayment;
    }

    public ReasonForRequest getReasonForRequest() {
        return reasonForRequest;
    }

    public void setReasonForRequest(ReasonForRequest reasonForRequest) {
        this.reasonForRequest = reasonForRequest;
    }

    public ZonedDateTime getRefundCutOff() {
        return refundCutOff;
    }

    public void setRefundCutOff(ZonedDateTime refundCutOff) {
        this.refundCutOff = refundCutOff;
    }

    public BigDecimal getRoeAsInClaim() {
        return roeAsInClaim;
    }

    public void setRoeAsInClaim(BigDecimal roeAsInClaim) {
        this.roeAsInClaim = roeAsInClaim;
    }

    public BigDecimal getRoeRequested() {
        return roeRequested;
    }

    public void setRoeRequested(BigDecimal roeRequested) {
        this.roeRequested = roeRequested;
    }

    public BigDecimal getNetAmountPayable() {
        return netAmountPayable;
    }

    public void setNetAmountPayable(BigDecimal netAmountPayable) {
        this.netAmountPayable = netAmountPayable;
    }

    public BigDecimal getNetAmountRequest() {
        return netAmountRequest;
    }

    public void setNetAmountRequest(BigDecimal netAmountRequest) {
        this.netAmountRequest = netAmountRequest;
    }


    public String getBookingReferenceNo() {
        return bookingReferenceNo;
    }

    public void setBookingReferenceNo(String bookingReferenceNo) {
        this.bookingReferenceNo = bookingReferenceNo;
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
    }

    public String getBspRaNumber() {
        return bspRaNumber;
    }

    public void setBspRaNumber(String bspRaNumber) {
        this.bspRaNumber = bspRaNumber;
    }

    public boolean getIsFromUI() {
        return isFromUI;
    }

    public void setIsFromUI(boolean fromUI) {
        isFromUI = fromUI;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefundResource that = (RefundResource) o;
        return Objects.equals(claimNo, that.claimNo) &&
                Objects.equals(clientName, that.clientName) &&
                refundType == that.refundType &&
                Objects.equals(dueOn, that.dueOn) &&
                Objects.equals(clientId, that.clientId) &&
                Objects.equals(clientType, that.clientType) &&
                Objects.equals(refundAmount, that.refundAmount) &&
                Objects.equals(claimCurrency, that.claimCurrency) &&
                Objects.equals(refundStatus, that.refundStatus) &&
                Objects.equals(refundProcessedDate, that.refundProcessedDate) &&
                Objects.equals(creditNoteNo, that.creditNoteNo) &&
                Objects.equals(defaultModeOfPayment, that.defaultModeOfPayment) &&
                Objects.equals(requestedModeOfPayment, that.requestedModeOfPayment) &&
                reasonForRequest == that.reasonForRequest &&
                Objects.equals(refundCutOff, that.refundCutOff) &&
                Objects.equals(roeAsInClaim, that.roeAsInClaim) &&
                Objects.equals(roeRequested, that.roeRequested) &&
                Objects.equals(netAmountPayable, that.netAmountPayable) &&
                Objects.equals(netAmountRequest, that.netAmountRequest) &&
                Objects.equals(bookingReferenceNo, that.bookingReferenceNo) &&
                Objects.equals(productDetail, that.productDetail) &&
                Objects.equals(bspRaNumber, that.bspRaNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(claimNo, clientName, refundType, dueOn, clientId, clientType, refundAmount, claimCurrency, refundStatus, refundProcessedDate, creditNoteNo, defaultModeOfPayment, requestedModeOfPayment, reasonForRequest, refundCutOff, roeAsInClaim, roeRequested, netAmountPayable, netAmountRequest, bookingReferenceNo, productDetail, bspRaNumber);
    }

    @Override
    public String toString() {
        return "RefundResource{" +
                "claimNo='" + claimNo + '\'' +
                ", clientName='" + clientName + '\'' +
                ", refundType=" + refundType +
                ", dueOn=" + dueOn +
                ", clientId='" + clientId + '\'' +
                ", clientType='" + clientType + '\'' +
                ", refundAmount=" + refundAmount +
                ", claimCurrency='" + claimCurrency + '\'' +
                ", refundStatus='" + refundStatus + '\'' +
                ", refundProcessedDate=" + refundProcessedDate +
                ", creditNoteNo='" + creditNoteNo + '\'' +
                ", defaultModeOfPayment='" + defaultModeOfPayment + '\'' +
                ", requestedModeOfPayment='" + requestedModeOfPayment + '\'' +
                ", reasonForRequest=" + reasonForRequest +
                ", refundCutOff=" + refundCutOff +
                ", roeAsInClaim=" + roeAsInClaim +
                ", roeRequested=" + roeRequested +
                ", netAmountPayable=" + netAmountPayable +
                ", netAmountRequest=" + netAmountRequest +
                ", bookingReferenceNo='" + bookingReferenceNo + '\'' +
                ", productDetail=" + productDetail +
                ", bspRaNumber='" + bspRaNumber + '\'' +
                '}';
    }
}
