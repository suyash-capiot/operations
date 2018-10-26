package com.coxandkings.travel.operations.resource.managedocumentation;

import java.util.List;

public class RoomDocuments {

    private List<BookingDocumentDetailsResource> documentInfo;
    private String roomID;

    public List<BookingDocumentDetailsResource> getDocumentInfo() {
        return documentInfo;
    }

    public void setDocumentInfo(List<BookingDocumentDetailsResource> documentInfo) {
        this.documentInfo = documentInfo;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }
}
