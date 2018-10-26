package com.coxandkings.travel.operations.resource.refund;

import com.coxandkings.travel.operations.enums.refund.ApprovalStatus;

public class ApprovalResource {
    private String claimNo;
    private ApprovalStatus approvalStatus;
    private String approvalRemark;

    public String getClaimNo() {
        return claimNo;
    }

    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovalRemark() {
        return approvalRemark;
    }

    public void setApprovalRemark(String approvalRemark) {
        this.approvalRemark = approvalRemark;
    }

    @Override
    public String toString() {
        return "ApprovalResource{" +
                "claimNo='" + claimNo + '\'' +
                ", approvalStatus=" + approvalStatus +
                ", approvalRemark='" + approvalRemark + '\'' +
                '}';
    }
}
