package com.coxandkings.travel.operations.enums.forex;

import org.springframework.util.StringUtils;

public enum IndentStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    REJECTED("Rejected");

    String indentStatus;

    IndentStatus(String newStatus) {
        this.indentStatus = newStatus;
    }

    public static IndentStatus fromString(String newStatus) {
        IndentStatus indentStatus = null;
        if (StringUtils.isEmpty(newStatus)) {
            return null;
        }

        for (IndentStatus tmpStatus : IndentStatus.values()) {
            if (tmpStatus.getIndentStatus().equalsIgnoreCase(newStatus)) {
                indentStatus = tmpStatus;
                break;
            }
        }

        return indentStatus;
    }

    public String getIndentStatus() {
        return indentStatus;
    }
}
