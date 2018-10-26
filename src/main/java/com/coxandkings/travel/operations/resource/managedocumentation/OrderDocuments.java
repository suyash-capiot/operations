package com.coxandkings.travel.operations.resource.managedocumentation;

import java.util.List;

public class OrderDocuments {

    private List<BookingDocumentDetailsResource> documentInfo;
    private String orderId;

    public List<BookingDocumentDetailsResource> getDocumentInfo() {
        return documentInfo;
    }

    public void setDocumentInfo(List<BookingDocumentDetailsResource> documentInfo) {
        this.documentInfo = documentInfo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
