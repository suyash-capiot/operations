package com.coxandkings.travel.operations.resource.managedocumentation;

public class PaxDocuments {

    private PaxDocInfo documentInfo;
    private String paxID;

    public PaxDocInfo getDocumentInfo() {
        return documentInfo;
    }

    public void setDocumentInfo(PaxDocInfo documentInfo) {
        this.documentInfo = documentInfo;
    }

    public String getPaxID() {
        return paxID;
    }

    public void setPaxID(String paxID) {
        this.paxID = paxID;
    }
}
