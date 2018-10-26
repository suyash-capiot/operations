package com.coxandkings.travel.operations.resource.refund;

import com.coxandkings.travel.operations.enums.refund.RefundTypes;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class ChangeRefundTypeResponse {
    private String refundClaimNo;
    @Enumerated(EnumType.STRING)
    private RefundTypes refundTypes;
    private String opsRemark;

    public String getRefundClaimNo() {
        return refundClaimNo;
    }

    public void setRefundClaimNo(String refundClaimNo) {
        this.refundClaimNo = refundClaimNo;
    }

    public String getOpsRemark() {
        return opsRemark;
    }

    public void setOpsRemark(String opsRemark) {
        this.opsRemark = opsRemark;
    }

    public RefundTypes getRefundTypes() {
        return refundTypes;
    }

    public void setRefundTypes(RefundTypes refundTypes) {
        this.refundTypes = refundTypes;
    }

}
