package com.coxandkings.travel.operations.resource.managedocumentation;

import java.util.List;

public class PaxDocInfo {
    private List<BookingDocumentDetailsResource> documentInfo;

    public List<BookingDocumentDetailsResource> getDocumentInfo() {
        return documentInfo;
    }

    public void setDocumentInfo(List<BookingDocumentDetailsResource> documentInfo) {
        this.documentInfo = documentInfo;
    }
}
