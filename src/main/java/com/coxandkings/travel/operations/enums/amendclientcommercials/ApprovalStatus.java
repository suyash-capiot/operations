package com.coxandkings.travel.operations.enums.amendclientcommercials;

import org.springframework.util.StringUtils;

public enum ApprovalStatus {
    APPROVED("approved"),
    PENDING("pending"),
    REJECTED("rejected");

    private String category;

    ApprovalStatus(String newStatus )   {
        category = newStatus;
    }

    public static ApprovalStatus getApprovalStatus(String newStatus) {
        ApprovalStatus approvalStatus = null;
        if(StringUtils.isEmpty(newStatus)) {
            return null;
        }

        for(ApprovalStatus tempApprovalStatus: ApprovalStatus.values()) {
            if(tempApprovalStatus.getCategory().equalsIgnoreCase(newStatus)) {
                approvalStatus = tempApprovalStatus;
                break;
            }
        }

        return approvalStatus;
    }

    public String getCategory() {
        return category;
    }
}
