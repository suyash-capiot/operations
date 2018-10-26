package com.coxandkings.travel.operations.utils.supplier;

public enum CommunicationType {

    EMAIL("EMAIL"),
    FAX("FAX"),
    PHONE("PHONE");

    private String communicationType;

    public String getCommunicationType() {
        return communicationType;
    }

    public void setCommunicationType(String communicationType) {
        this.communicationType = communicationType;
    }

    CommunicationType(String communicationType) {
        this.communicationType = communicationType;
    }

    public static CommunicationType fromString(String aCommunicationType) {

        for (CommunicationType eCommunicationType : CommunicationType.values()) {
            if (eCommunicationType.getCommunicationType().equalsIgnoreCase(aCommunicationType)) {
                return eCommunicationType;
            }
        }
        return null;
    }
}
