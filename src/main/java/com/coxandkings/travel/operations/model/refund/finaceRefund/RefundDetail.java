package com.coxandkings.travel.operations.model.refund.finaceRefund;

import com.coxandkings.travel.operations.enums.refund.ClaimStatus;
import com.coxandkings.travel.operations.enums.refund.RefundTypes;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;


public class RefundDetail {

    private UUID id;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime dueOn;

    private RefundReason refundReason;
    private RefundTypes refundType;
    private BigDecimal refundAmount;
    private BigDecimal claimROE;
    private BigDecimal requestedROE;
    private BigDecimal netAmountPayable;
    /* private Long createdOn;
     private Long lastUpdatedOn;
     private String createdBy;
     private String lastUpdatedBy;*/
    private ClaimStatus status;
    private String defaultModeOfPayment;
    private String requestedModeOfPayment;
    private String currentModeOfPayment;
    private String refundClaimRaisedBy;
    private String refundClaimApprovedBy;
    private String refundProcessedBy;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)

    private ZonedDateTime refundProcessedDate;

    private String claimCurrency;
    private String processCurrency;
    private String remarks;

    private String opsRemarks;
    private String approvalRemarks;



    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ZonedDateTime getDueOn() {
        return dueOn;
    }

    public void setDueOn(ZonedDateTime dueOn) {
        this.dueOn = dueOn;
    }

    public RefundReason getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(RefundReason refundReason) {
        this.refundReason = refundReason;
    }

    public RefundTypes getRefundType() {

        return refundType;
    }

    public void setRefundType(RefundTypes refundType) {
        this.refundType = refundType;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public BigDecimal getClaimROE() {
        return claimROE;
    }

    public void setClaimROE(BigDecimal claimROE) {
        this.claimROE = claimROE;
    }

    public BigDecimal getRequestedROE() {
        return requestedROE;
    }

    public void setRequestedROE(BigDecimal requestedROE) {
        this.requestedROE = requestedROE;
    }

    public BigDecimal getNetAmountPayable() {
        return netAmountPayable;
    }

    public void setNetAmountPayable(BigDecimal netAmountPayable) {
        this.netAmountPayable = netAmountPayable;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
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

    public String getCurrentModeOfPayment() {
        return currentModeOfPayment;
    }

    public void setCurrentModeOfPayment(String currentModeOfPayment) {
        this.currentModeOfPayment = currentModeOfPayment;
    }

    public String getRefundClaimRaisedBy() {
        return refundClaimRaisedBy;
    }

    public void setRefundClaimRaisedBy(String refundClaimRaisedBy) {
        this.refundClaimRaisedBy = refundClaimRaisedBy;
    }

    public String getRefundClaimApprovedBy() {
        return refundClaimApprovedBy;
    }

    public void setRefundClaimApprovedBy(String refundClaimApprovedBy) {
        this.refundClaimApprovedBy = refundClaimApprovedBy;
    }

    public String getRefundProcessedBy() {
        return refundProcessedBy;
    }

    public void setRefundProcessedBy(String refundProcessedBy) {
        this.refundProcessedBy = refundProcessedBy;
    }

    public ZonedDateTime getRefundProcessedDate() {
        return refundProcessedDate;
    }

    public void setRefundProcessedDate(ZonedDateTime refundProcessedDate) {
        this.refundProcessedDate = refundProcessedDate;
    }

    public String getClaimCurrency() {
        return claimCurrency;
    }

    public void setClaimCurrency(String claimCurrency) {
        this.claimCurrency = claimCurrency;
    }

    public String getProcessCurrency() {
        return processCurrency;
    }

    public void setProcessCurrency(String processCurrency) {
        this.processCurrency = processCurrency;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
}
