package com.coxandkings.travel.operations.enums.amendsuppliercommercial;

import org.springframework.util.StringUtils;

public enum  SupplierCommercialApproval {
    APPROVED("approved"),
    PENDING("pending"),
    REJECTED("rejected");

    private String status;

    SupplierCommercialApproval(String newStatus )   {
        status = newStatus;
    }

    public static SupplierCommercialApproval getApprovalStatus(String newStatus) {
        SupplierCommercialApproval approvalStatus = null;
        if(StringUtils.isEmpty(newStatus)) {
            return null;
        }

        for(SupplierCommercialApproval tempApprovalStatus: SupplierCommercialApproval.values()) {
            if (tempApprovalStatus.getStatus().equalsIgnoreCase(newStatus)) {
                approvalStatus = tempApprovalStatus;
                break;
            }
        }

        return approvalStatus;
    }

    public String getStatus() {
        return status;
    }
}
