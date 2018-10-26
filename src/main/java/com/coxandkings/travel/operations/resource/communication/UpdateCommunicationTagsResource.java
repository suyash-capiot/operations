package com.coxandkings.travel.operations.resource.communication;

public class UpdateCommunicationTagsResource {
    private String id;
    private CommunicationTagResource communicationTags;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CommunicationTagResource getCommunicationTags() {
        return communicationTags;
    }

    public void setCommunicationTags(CommunicationTagResource communicationTags) {
        this.communicationTags = communicationTags;
    }
}
