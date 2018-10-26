package com.coxandkings.travel.operations.model.managedocumentation;

import com.coxandkings.travel.operations.model.template.request.TemplateInfo;

import java.util.List;
import java.util.Map;

public class DocumentInfoForMails {

    private String emailId;
    private List<DocumentVersion> documentVersions;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public List<DocumentVersion> getDocumentVersions() {
        return documentVersions;
    }

    public void setDocumentVersions(List<DocumentVersion> documentVersions) {
        this.documentVersions = documentVersions;
    }
}
