package com.coxandkings.travel.operations.enums.managedocumentation;

public enum DocumentCopy {
    DOCUMENT_COPY_CUSTOMER("Customer"),
    DOCUMENT_COPY_SUPPLIER("Supplier"),
    DOCUMENT_COPY_TOUR_MANAGER("Tour Manager"),
    DOCUMENT_COPY_MAN_ON_SPOT("Man on Spot");

    private String documentCopy;

    DocumentCopy(String documentCopy) {
        this.documentCopy = documentCopy;
    }

    public static DocumentCopy fromString(String newValue) {
        DocumentCopy aDocCopy = null;
        if (newValue != null && newValue.trim().length() > 0) {
            for (DocumentCopy aDocumentCopy : DocumentCopy.values()) {
                if (newValue.equals(aDocumentCopy.getDocumentCopy())) {
                    aDocCopy = aDocumentCopy;
                    break;
                }
            }
        }
        return aDocCopy;
    }

    public String getDocumentCopy() {
        return documentCopy;
    }
}
