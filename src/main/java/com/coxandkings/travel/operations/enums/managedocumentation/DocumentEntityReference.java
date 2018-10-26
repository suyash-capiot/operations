package com.coxandkings.travel.operations.enums.managedocumentation;

public enum DocumentEntityReference {

    DOCUMENT_PAX_WISE("Passenger wise"),
    DOCUMENT_LEAD_PAX_WISE("Lead Passenger Only"),
    DOCUMENT_BOOKING_WISE("Booking Level"),
    DOCUMENT_ROOM_WISE("Room Type wise"),
    DOCUMENT_CABIN_WISE("Cabin Type wise"),
    DOCUMENT_ORDER_WISE("Order Level");

    private String entityReference;


    private DocumentEntityReference(String newEntityRef) {
        entityReference = newEntityRef;

    }

    public static DocumentEntityReference fromString(String newEntityRef) {
        DocumentEntityReference aDocEntityReference = DocumentEntityReference.DOCUMENT_BOOKING_WISE;

        if (newEntityRef != null && newEntityRef.trim().length() > 0) {
            for (DocumentEntityReference tmpDocEntityReference : DocumentEntityReference.values()) {
                if (tmpDocEntityReference.getEntityReference().equalsIgnoreCase(newEntityRef)) {
                    aDocEntityReference = tmpDocEntityReference;
                    break;
                }
            }
        }
        return aDocEntityReference;
    }

    public String getEntityReference() {
        return entityReference;
    }
}
