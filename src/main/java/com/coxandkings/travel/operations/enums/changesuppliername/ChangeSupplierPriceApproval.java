package com.coxandkings.travel.operations.enums.changesuppliername;

import org.springframework.util.StringUtils;

public enum ChangeSupplierPriceApproval {
    APPROVED("approved"),
    PENDING("pending"),
    REJECTED("rejected");

    private String category;

    ChangeSupplierPriceApproval(String newStatus) {
        category = newStatus;
    }

    public static ChangeSupplierPriceApproval getApprovalStatus(String newStatus) {
        ChangeSupplierPriceApproval approvalStatus = null;
        if (StringUtils.isEmpty(newStatus)) {
            return null;
        }

        for (ChangeSupplierPriceApproval tempApprovalStatus : ChangeSupplierPriceApproval.values()) {
            if (tempApprovalStatus.getCategory().equalsIgnoreCase(newStatus)) {
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
