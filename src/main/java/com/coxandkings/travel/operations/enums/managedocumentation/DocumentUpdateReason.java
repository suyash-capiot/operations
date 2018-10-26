package com.coxandkings.travel.operations.enums.managedocumentation;

public enum DocumentUpdateReason {

    DOCUMENT_EDITED("Document Edited"),
    DOCUMENT_REUPLOADED("Document Reuploaded"),
    DOCUMENT_REVOKED("Document Revoked"),
    DOCUMENT_REJECTED("Document Rejected"),
    DOCUMENT_COPIED_TO_SUPPLIER("Document copied to Supplier"),
    DOCUMENT_COPIED_TO_TOUR_MANAGER("Document copied to Tour Manager"),
    DOCUMENT_COPIED_TO_MAN_ON_SPOT("Document copied to Man on Spot");

    private String value;

    DocumentUpdateReason(String value) {
        this.value = value;
    }

    public static DocumentUpdateReason fromString(String value) {
        DocumentUpdateReason documentUpdateReason = null;
        if (value != null && value.trim().length() > 0) {
            for (DocumentUpdateReason documentUpdate : DocumentUpdateReason.values()) {
                if (documentUpdate.getValue().equals(value)) {
                    documentUpdateReason = documentUpdate;
                    break;
                }
            }
        }
        return documentUpdateReason;
    }

    public String getValue() {
        return this.value;
    }
}
