package com.coxandkings.travel.operations.enums.manageproductupdates;

public enum UpdatedFlightStatus {

    NEW("New"),
    UPDATED("Updated");

    private String status;

    UpdatedFlightStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UpdatedFlightStatus fromString(String newAttribute) {
        UpdatedFlightStatus aFlightStatus = null;
        if (newAttribute == null || newAttribute.isEmpty()) {
            return aFlightStatus;
        }

        for (UpdatedFlightStatus tmpFlightStatus : UpdatedFlightStatus.values()) {
            if (tmpFlightStatus.getStatus().equalsIgnoreCase(newAttribute)) {
                aFlightStatus = tmpFlightStatus;
                break;
            }
        }
        return aFlightStatus;
    }

}
