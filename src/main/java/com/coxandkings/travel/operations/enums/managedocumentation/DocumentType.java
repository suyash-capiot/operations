package com.coxandkings.travel.operations.enums.managedocumentation;

public enum DocumentType {

    //MDM
    HANDOVER_DOCUMENTS("Handover Documents"),
    DOCUMENT_FROM_CUSTOMER("Document from customer"),
    NICE_TRIP_COMMUNICATION("Nice Trip Communication"),
    QUOTATION_PROPOSAL("Quotation Proposal"),

    SEND_DOCUMENT("Document to be sent"),
    RECEIVE_DOCUMENT("Document to be received");

    private String value;

    DocumentType(String value) {
        this.value = value;
    }

    public static DocumentType fromString(String value) {
        DocumentType documentType = null;
        if (value != null && value.trim().length() > 0) {
            for (DocumentType docType : DocumentType.values()) {
                if (docType.getValue().equals(value)) {
                    documentType = docType;
                    break;
                }
            }
        }
        return documentType;
    }

    public String getValue() {
        return this.value;
    }
}
