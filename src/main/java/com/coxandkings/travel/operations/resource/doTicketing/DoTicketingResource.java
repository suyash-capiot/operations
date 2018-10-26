package com.coxandkings.travel.operations.resource.doTicketing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DoTicketingResource {

    private String id;
    private String bookId;
    private String orderId;
    private String status;
    private String ticketingCredential;
    private BigDecimal oldAmount;
    private String oldAmountCcy;
    private BigDecimal newAmount;
    private String newAmountCcy;
    private BigDecimal difference;
    private String differenceCcy;
    private String scenario;
    private String creditNo;
    private BigDecimal companyAmount;
    private BigDecimal customerAmount;
    private String opsApprovalType;
    private String approverRemark;
    private String approverTodoTaskId;
    private String reasonForRequest;
    private String opsApprovalStatus;
    private String clientApprovalStatus;
    private String fareComparator;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTicketingCredential() {
        return ticketingCredential;
    }

    public void setTicketingCredential(String ticketingCredential) {
        this.ticketingCredential = ticketingCredential;
    }

    public BigDecimal getOldAmount() {
        return oldAmount;
    }

    public void setOldAmount(BigDecimal oldAmount) {
        this.oldAmount = oldAmount;
    }

    public String getOldAmountCcy() {
        return oldAmountCcy;
    }

    public void setOldAmountCcy(String oldAmountCcy) {
        this.oldAmountCcy = oldAmountCcy;
    }

    public BigDecimal getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(BigDecimal newAmount) {
        this.newAmount = newAmount;
    }

    public String getNewAmountCcy() {
        return newAmountCcy;
    }

    public void setNewAmountCcy(String newAmountCcy) {
        this.newAmountCcy = newAmountCcy;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public String getDifferenceCcy() {
        return differenceCcy;
    }

    public void setDifferenceCcy(String differenceCcy) {
        this.differenceCcy = differenceCcy;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public BigDecimal getCompanyAmount() {
        return companyAmount;
    }

    public void setCompanyAmount(BigDecimal companyAmount) {
        this.companyAmount = companyAmount;
    }

    public BigDecimal getCustomerAmount() {
        return customerAmount;
    }

    public void setCustomerAmount(BigDecimal customerAmount) {
        this.customerAmount = customerAmount;
    }

    public String getOpsApprovalType() {
        return opsApprovalType;
    }

    public void setOpsApprovalType(String opsApprovalType) {
        this.opsApprovalType = opsApprovalType;
    }

    public String getApproverRemark() {
        return approverRemark;
    }

    public void setApproverRemark(String approverRemark) {
        this.approverRemark = approverRemark;
    }

    public String getApproverTodoTaskId() {
        return approverTodoTaskId;
    }

    public void setApproverTodoTaskId(String approverTodoTaskId) {
        this.approverTodoTaskId = approverTodoTaskId;
    }

    public String getReasonForRequest() {
        return reasonForRequest;
    }

    public void setReasonForRequest(String reasonForRequest) {
        this.reasonForRequest = reasonForRequest;
    }

    public String getCreditNo() {
        return creditNo;
    }

    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }

    public String getOpsApprovalStatus() {
        return opsApprovalStatus;
    }

    public void setOpsApprovalStatus(String opsApprovalStatus) {
        this.opsApprovalStatus = opsApprovalStatus;
    }

    public String getClientApprovalStatus() {
        return clientApprovalStatus;
    }

    public void setClientApprovalStatus(String clientApprovalStatus) {
        this.clientApprovalStatus = clientApprovalStatus;
    }

    public String getFareComparator() {
        return fareComparator;
    }

    public void setFareComparator(String fareComparator) {
        this.fareComparator = fareComparator;
    }
}
