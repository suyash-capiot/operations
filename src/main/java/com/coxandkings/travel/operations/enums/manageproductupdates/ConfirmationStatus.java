package com.coxandkings.travel.operations.enums.manageproductupdates;

public enum ConfirmationStatus {

    CNF("Confirmed"),
    REJ("Rejected"),
    PENDING("Pending");


    private String confStatus;

    private ConfirmationStatus(String newStatus) {
        confStatus = newStatus;
    }

    public static ConfirmationStatus fromString(String newStatus) {
        ConfirmationStatus aStatus = null;
        if (newStatus == null || newStatus.isEmpty()) {
            return aStatus;
        }

        for (ConfirmationStatus tmpStatus : ConfirmationStatus.values()) {
            if (tmpStatus.getStatus().equalsIgnoreCase(newStatus)) {
                aStatus = tmpStatus;
                break;
            }
        }
        return aStatus;
    }

    public String getStatus() {
        return confStatus;
    }
}
