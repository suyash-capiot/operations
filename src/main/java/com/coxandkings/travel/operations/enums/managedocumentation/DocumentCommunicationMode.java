package com.coxandkings.travel.operations.enums.managedocumentation;

public enum DocumentCommunicationMode {

    COMMUNICATION_MODE_EMAIL("email", "Email"),
    COMMUNICATION_MODE_SMS("sms", "SMS"),
    COMMUNICATION_MODE_COURIER("courier", "Courier");

    private String commMode;
    private String displayValue;

    DocumentCommunicationMode(String newCommMode, String displayValue) {
        commMode = newCommMode;
        this.displayValue = displayValue;
    }

    public static DocumentCommunicationMode fromString(String newCommMode) {
        DocumentCommunicationMode aDocCommMode = null;
        if (newCommMode != null && newCommMode.trim().length() > 0) {
            for (DocumentCommunicationMode aDocumentCommMode : DocumentCommunicationMode.values()) {
                if (aDocumentCommMode.getCommMode().equals(newCommMode)) {
                    aDocCommMode = aDocumentCommMode;
                    break;
                }
            }
        }
        return aDocCommMode;
    }

    public String getCommMode() {
        return commMode;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
