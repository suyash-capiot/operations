package com.coxandkings.travel.operations.resource.managedocumentation;

import java.util.List;

public class RoomDocumentsRequest {

    private String roomID;
    private List<BookingDocumentDetailsResource> roomDocuments;
    private String userID;

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public List<BookingDocumentDetailsResource> getRoomDocuments() {
        return roomDocuments;
    }

    public void setRoomDocuments(List<BookingDocumentDetailsResource> roomDocuments) {
        this.roomDocuments = roomDocuments;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
