package com.coxandkings.travel.operations.enums.qcmanagement;

import org.springframework.util.StringUtils;

public enum QcStatus {
    QC_COMPLETED("Completed"),
    QC_ERROR("Error"),
    QC_PENDING("Pending"),
    QC_IN_PROGRESS("In Progress");
    private String status;

    QcStatus(String newStatus) {
        status = newStatus;
    }

    public static QcStatus getQcStatus(String newStatus) {
        QcStatus qcStatus = null;
        if (StringUtils.isEmpty(newStatus)) {
            return null;
        }
        for (QcStatus tempStatus : QcStatus.values()) {
            if (tempStatus.getStatus().equalsIgnoreCase(newStatus)) {
                qcStatus = tempStatus;
                break;
            }
        }
        return qcStatus;
    }

    public String getStatus() {
        return status;
    }
}
