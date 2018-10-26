package com.coxandkings.travel.operations.enums.managedocumentation;

public enum DocumentStatus {
    PENDING("Pending"),
    REJECTED("Rejected"),
    VERIFIED("Verified"),
    RECEIVED("Received"),
    VERIFICATION_PENDING("Verification Pending"),
    PARTIALLY_UPLOADED("Partially Uploaded"),
    SENT("Sent"),
    GENERATED("Generated"),
    PENDING_TO_BE_SENT_TO_CUSTOMER("Pending to be sent to Customer"),
    SENT_TO_CUSTOMER("Sent to Customer"),
    PENDING_FROM_SUPPLIER("Pending from Supplier"),
    RECEIVED_FROM_SUPPLIER_PENDING_TO_BE_SENT_TO_CUSTOMER("Received from Supplier/Pending to be sent to Supplier");

    private String value;

    DocumentStatus(String value) {
        this.value = value;
    }

    public static DocumentStatus fromString(String newValue) {
        DocumentStatus aDocStatus = null;
        if (newValue != null && newValue.trim().length() > 0) {
            for (DocumentStatus aDocumentStatus : DocumentStatus.values()) {
                if (newValue.equals(aDocumentStatus.getValue())) {
                    aDocStatus = aDocumentStatus;
                    break;
                }
            }
        }
        return aDocStatus;
    }

    public String getValue() {
        return value;
    }
}
