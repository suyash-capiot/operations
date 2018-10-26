package com.coxandkings.travel.operations.model.refund;

import com.coxandkings.travel.operations.enums.refund.ReasonForRequest;
import com.coxandkings.travel.operations.enums.refund.RefundStatus;
import com.coxandkings.travel.operations.enums.refund.RefundTypes;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.coxandkings.travel.operations.model.refund.finaceRefund.ClientType;
import com.coxandkings.travel.operations.model.refund.finaceRefund.InvoiceDetail;
import com.coxandkings.travel.operations.model.refund.finaceRefund.ProductDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;


public class Refund {
    private String claimNo;
    private String clientName;
    private RefundTypes refundType;

    @JsonProperty("dueOn")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime dueOn;// Due on is same refund cut of date
    private ClientType clientType;
    private String clientId;
    private BigDecimal refundAmount;
    private String claimCurrency;
    private RefundStatus refundStatus;

    @JsonProperty("refundProcessedDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime refundProcessedDate;

    private String creditNoteNo;
    private String defaultModeOfPayment;
    private String requestedModeOfPayment;
    private ReasonForRequest reasonForRequest;
    private BigDecimal roeAsInClaim;
    private BigDecimal roeRequested;
    private BigDecimal netAmountPayable;
    private BigDecimal netAmountRequest;
    private String opsRemarks;
    private String approvalRemarks;
    private String bookingReferenceNo;
    private ProductDetail productDetail;
    private Set<InvoiceDetail> invoiceDetail;
    private String bspRaNumber;
    private boolean isFromUI=false;

    //getter and setter
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

    public void setRefundStatus(RefundStatus refundStatus) {
        this.refundStatus = refundStatus;
    }

    public ZonedDateTime getDueOn() {
        return dueOn;
    }

    public void setDueOn(ZonedDateTime dueOn) {
        this.dueOn = dueOn;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public String getClientId() {
        return clientId;
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

    public ZonedDateTime getRefundProcessedDate() {
        return refundProcessedDate;
    }

    public void setRefundProcessedDate(ZonedDateTime refundProcessedDate) {
        this.refundProcessedDate = refundProcessedDate;
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

    public String getOpsRemarks() {
        return opsRemarks;
    }

    public void setOpsRemarks(String opsRemarks) {
        this.opsRemarks = opsRemarks;
    }

    public String getApprovalRemarks() {
        return approvalRemarks;
    }

    public void setApprovalRemarks(String approvalRemarks) {
        this.approvalRemarks = approvalRemarks;
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

    public Set<InvoiceDetail> getInvoiceDetail() {
        return invoiceDetail;
    }

    public void setInvoiceDetail(Set<InvoiceDetail> invoiceDetail) {
        this.invoiceDetail = invoiceDetail;
    }

    public RefundStatus getRefundStatus() {
        return refundStatus;
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
    public String toString() {
        return "Refund{" +
                "claimNo='" + claimNo + '\'' +
                ", clientName='" + clientName + '\'' +
                ", refundType=" + refundType +
                ", dueOn=" + dueOn +
                ", clientType=" + clientType +
                ", clientId='" + clientId + '\'' +
                ", refundAmount=" + refundAmount +
                ", claimCurrency='" + claimCurrency + '\'' +
                ", refundStatus=" + refundStatus +
                ", refundProcessedDate=" + refundProcessedDate +
                ", creditNoteNo='" + creditNoteNo + '\'' +
                ", defaultModeOfPayment='" + defaultModeOfPayment + '\'' +
                ", requestedModeOfPayment='" + requestedModeOfPayment + '\'' +
                ", reasonForRequest=" + reasonForRequest +
                ", roeAsInClaim=" + roeAsInClaim +
                ", roeRequested=" + roeRequested +
                ", netAmountPayable=" + netAmountPayable +
                ", netAmountRequest=" + netAmountRequest +
                ", opsRemarks='" + opsRemarks + '\'' +
                ", approvalRemarks='" + approvalRemarks + '\'' +
                ", bookingReferenceNo='" + bookingReferenceNo + '\'' +
                ", productDetail=" + productDetail +
                ", invoiceDetail=" + invoiceDetail +
                ", bspRaNumber='" + bspRaNumber + '\'' +
                ", isFromUI='" + isFromUI + '\'' +
                '}';
    }
}

