package com.coxandkings.travel.operations.model.managedocumentation;

import java.util.ArrayList;
import java.util.List;

public class RoomWiseDocuments {

    private String roomId;
    private List<DocumentVersion> documentVersions = new ArrayList<>();

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<DocumentVersion> getDocumentVersions() {
        return documentVersions;
    }

    public void setDocumentVersions(List<DocumentVersion> documentVersions) {
        this.documentVersions = documentVersions;
    }
}
