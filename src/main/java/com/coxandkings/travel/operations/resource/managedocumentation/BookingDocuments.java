package com.coxandkings.travel.operations.resource.managedocumentation;

import java.util.List;

public class BookingDocuments {

    private List<BookingDocumentDetailsResource> documentInfo;
    private String bookID;

    public List<BookingDocumentDetailsResource> getDocumentInfo() {
        return documentInfo;
    }

    public void setDocumentInfo(List<BookingDocumentDetailsResource> documentInfo) {
        this.documentInfo = documentInfo;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }
}
