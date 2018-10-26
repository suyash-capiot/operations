package com.coxandkings.travel.operations.enums.sellingPrice;

import org.springframework.util.StringUtils;

public enum ApprovalStatus {
    APPROVED("approvePaymentAdvise"),
    PENDING("pending"),
    REJECTED("rejected");

    private String statusName;

    ApprovalStatus(String aName) {
        statusName = aName;
    }

    public static ApprovalStatus getApprovalStatus(String anApprovalStatus) {
        ApprovalStatus approvalStatus = null;
        if(StringUtils.isEmpty(anApprovalStatus)) {
            return null;
        }

        for(ApprovalStatus tmpApprovalStatus: ApprovalStatus.values()) {
            if(tmpApprovalStatus.getStatusName().equalsIgnoreCase(anApprovalStatus)) {
                approvalStatus = tmpApprovalStatus;
            }
        }

        return approvalStatus;
    }

    public String getStatusName() {
        return statusName;
    }
}
