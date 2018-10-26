package com.coxandkings.travel.operations.enums.managedocumentation;

public enum DocumentCriteria {

    CRITERIA_PAYMENT_TYPE("Payment Type"),
    CRITERIA_BOOKING_STATUS("Booking Status"),
    CRITERIA_ENQUIRY("Enquiry"),
    CRITERIA_QUOTATION("Quotation"),
    CRITERIA_NO_OF_NIGHTS("No of Nights");

    private String criteria;

    DocumentCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getCriteria() {
        return this.criteria;
    }

    public static DocumentCriteria fromString(String value) {
        DocumentCriteria documentCriteria = null;
        if(value != null && value.trim().length() > 0 ) {
            for(DocumentCriteria criteria : DocumentCriteria.values()) {
                if(criteria.getCriteria().equals(value)) {
                    documentCriteria = criteria;
                    break;
                }
            }
        }
        return documentCriteria;
    }

}
