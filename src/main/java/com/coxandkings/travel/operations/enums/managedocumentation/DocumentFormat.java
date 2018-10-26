package com.coxandkings.travel.operations.enums.managedocumentation;

public enum DocumentFormat {

    DOCUMENT_FORMAT_ORIGINALS("Originals"),
    DOCUMENT_FORMAT_PDF("PDF"),
    DOCUMENT_FORMAT_SCAN_COPY("Scan Copy"),
    DOCUMENT_FORMAT_E_DOCUMENT("E-document"),
    DOCUMENT_FORMAT_XEROX_COPY("Xerox Copy");

    private String docFormat;

    private DocumentFormat(String newFormat) {
        docFormat = newFormat;
    }

    public static DocumentFormat fromString(String newFormat) {
        DocumentFormat aDocFormat = null;
        if (newFormat != null && newFormat.trim().length() > 0) {
            for (DocumentFormat aDocumentFormat : DocumentFormat.values()) {
                if (aDocumentFormat.getDocFormat().equals(newFormat)) {
                    aDocFormat = aDocumentFormat;
                    break;
                }
            }
        }
        return aDocFormat;
    }

    public String getDocFormat() {
        return docFormat;
    }
}
