package com.coxandkings.travel.operations.model.managedocumentation;

import java.util.List;

public class BookingDocumentDetails {

    private String bookingID;

    private List<DocumentRowItem> documentRowItemsList;

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public List<DocumentRowItem> getDocumentRowItemsList() {
        return documentRowItemsList;
    }

    public void setDocumentRowItemsList(List<DocumentRowItem> documentRowItemsList) {
        this.documentRowItemsList = documentRowItemsList;
    }
}
