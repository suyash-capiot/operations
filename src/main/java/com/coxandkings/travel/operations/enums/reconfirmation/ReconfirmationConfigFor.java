package com.coxandkings.travel.operations.enums.reconfirmation;

public enum ReconfirmationConfigFor {

    SERVICE_PROVIDER("SERVICE PROVIDER"),
    SUPPLIER("supplier"),
    CUSTOMER("CUSTOMER"),
    SUPPLIER_AND_CUSTOMER("supplier"),
    CLIENT_AND_SUPPLIER("client&supplier"),
    CLIENT("client");

    private String value;

    ReconfirmationConfigFor(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }


    public static ReconfirmationConfigFor fromString(String value) {
        for (ReconfirmationConfigFor reconfirmation : ReconfirmationConfigFor.values()) {
            if (reconfirmation.getValue().equalsIgnoreCase(value)) {
                return reconfirmation;
            }
        }
        return null;
    }
}
