package com.coxandkings.travel.operations.model.doTicketing;

import com.coxandkings.travel.operations.enums.doTicketing.AbsorbRetainScenarios;
import com.coxandkings.travel.operations.enums.doTicketing.ApprovalTypeValues;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;

@Audited
@Entity
public class DoTicketing {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column
    private String id;
    @Column
    private String bookId;
    @Column
    private String orderId;
    @Column
    private String status;
    @Column
    private String ticketingCredential;
    @Column
    private BigDecimal oldAmount;
    @Column
    private String oldAmountCcy;
    @Transient
    private String fareComparator;
    @Column
    private BigDecimal newAmount;
    @Column
    private String newAmountCcy;
    @Column
    private BigDecimal difference;
    @Column
    private String differenceCcy;
    @Column
    @Enumerated(EnumType.STRING)
    private AbsorbRetainScenarios scenario;
    @Column
    private String creditNo;
    @Column
    private BigDecimal companyAmount;
    @Column
    private BigDecimal customerAmount;
    @Column
    @Enumerated(EnumType.STRING)
    private ApprovalTypeValues opsApprovalType;
    @Column
    private String approverRemark;
    @Column
    private String approverTodoTaskId;
    @Column
    private String reasonForRequest;
    @Column
    private String opsApprovalStatus;
    @Column
    private String clientApprovalStatus;

    public String getId() {
        return id;
    }

    public String getTicketingCredential() {
        return ticketingCredential;
    }

    public void setTicketingCredential(String ticketingCredential) {
        this.ticketingCredential = ticketingCredential;
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

    public AbsorbRetainScenarios getScenario() {
        return scenario;
    }

    public void setScenario(AbsorbRetainScenarios scenario) {
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

    public ApprovalTypeValues getOpsApprovalType() {
        return opsApprovalType;
    }

    public void setOpsApprovalType(ApprovalTypeValues opsApprovalType) {
        this.opsApprovalType = opsApprovalType;
    }

    public String getReasonForRequest() {
        return reasonForRequest;
    }

    public void setReasonForRequest(String reasonForRequest) {
        this.reasonForRequest = reasonForRequest;
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

    public String getFareComparator() {
        return fareComparator;
    }

    public void setFareComparator(String fareComparator) {
        this.fareComparator = fareComparator;
    }

    public String getCreditNo() {
        return creditNo;
    }

    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }
}
