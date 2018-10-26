package com.coxandkings.travel.operations.model.refund;

import com.coxandkings.travel.operations.enums.refund.ApprovalStatus;
import com.coxandkings.travel.operations.enums.refund.RefundTypes;

import javax.persistence.*;

@Entity
@Table(name = "refund_change_type")
public class ChangeType {
    @Id
    @Column(name = "claim_no")
    private String claimNo;

    @Column(name = "old_refund_type")
    @Enumerated(EnumType.STRING)
    private RefundTypes oldRefundType;

    @Column(name = "new_refund_type")
    @Enumerated(EnumType.STRING)
    private RefundTypes newRefundType;

    private String toDoTaskNo;


    @Column(name = "approval_status")
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus = ApprovalStatus.Pending;

    @Column(name = "ops_remark")
    private String opsRemark;

    @Column(name = "approval_remark")
    private String approvalRemark;


    public String getClaimNo() {
        return claimNo;
    }

    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }

    public RefundTypes getOldRefundType() {
        return oldRefundType;
    }

    public void setOldRefundType(RefundTypes oldRefundType) {
        this.oldRefundType = oldRefundType;
    }

    public RefundTypes getNewRefundType() {
        return newRefundType;
    }

    public void setNewRefundType(RefundTypes newRefundType) {
        this.newRefundType = newRefundType;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getOpsRemark() {
        return opsRemark;
    }

    public void setOpsRemark(String opsRemark) {
        this.opsRemark = opsRemark;
    }

    public String getApprovalRemark() {
        return approvalRemark;
    }

    public String getToDoTaskNo() {
        return toDoTaskNo;
    }

    public void setToDoTaskNo(String toDoTaskNo) {
        this.toDoTaskNo = toDoTaskNo;
    }

    public void setApprovalRemark(String approvalRemark) {
        this.approvalRemark = approvalRemark;
    }

    @Override
    public String toString() {
        return "ChangeType{" +
                "claimNo='" + claimNo + '\'' +
                ", oldRefundType=" + oldRefundType +
                ", newRefundType=" + newRefundType +
                ", approvalStatus=" + approvalStatus +
                ", opsRemark='" + opsRemark + '\'' +
                ", approvalRemark='" + approvalRemark + '\'' +
                '}';
    }
}
