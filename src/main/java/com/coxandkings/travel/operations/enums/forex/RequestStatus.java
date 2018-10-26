package com.coxandkings.travel.operations.enums.forex;

import org.springframework.util.StringUtils;

public enum RequestStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    REJECTED("Rejected");

    String requestStatus;

    RequestStatus(String newStatus) {
        this.requestStatus = newStatus;
    }

    public static RequestStatus fromString(String newStatus) {
        RequestStatus requestStatus = null;
        if (StringUtils.isEmpty(newStatus)) {
            return null;
        }

        for (RequestStatus tmpStatus : RequestStatus.values()) {
            if (tmpStatus.getRequestStatus().equalsIgnoreCase(newStatus)) {
                requestStatus = tmpStatus;
                break;
            }
        }

        return requestStatus;
    }

    public String getRequestStatus() {
        return requestStatus;
    }
}
