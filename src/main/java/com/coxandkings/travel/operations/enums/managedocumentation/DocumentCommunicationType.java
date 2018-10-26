package com.coxandkings.travel.operations.enums.managedocumentation;

public enum DocumentCommunicationType {
    SYSTEM_TO_CUSTOMER("System to Customer", "Sent - System to Customer"),
    SUPPLIER_TO_CUSTOMER("Supplier to Customer", "Sent - Supplier to Customer"),
    SUPPLIER_TO_COMPANY("Supplier to Company", "Received - Supplier to Company"),
    CUSTOMER_TO_COMPANY("Customer to Company", "Received - Customer to Company");

    private String communicationType;
    private String displayValue;

    DocumentCommunicationType(String communicationType, String displayValue) {
        this.communicationType = communicationType;
        this.displayValue = displayValue;
    }

    public static DocumentCommunicationType fromString(String newValue) {
        DocumentCommunicationType commType = null;

        if (newValue != null && newValue.trim().length() > 0) {
            for (DocumentCommunicationType aCommType : DocumentCommunicationType.values()) {
                if (aCommType.getCommunicationType().equalsIgnoreCase(newValue)) {
                    commType = aCommType;
                    break;
                }
            }
        }
        return commType;
    }

    public static DocumentCommunicationType fromDisplayValue(String newValue) {
        DocumentCommunicationType commType = null;

        if (newValue != null && newValue.trim().length() > 0) {
            for (DocumentCommunicationType aCommType : DocumentCommunicationType.values()) {
                if (aCommType.getDisplayValue().equalsIgnoreCase(newValue)) {
                    commType = aCommType;
                    break;
                }
            }
        }
        return commType;
    }

    public String getCommunicationType() {
        return communicationType;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
