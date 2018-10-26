package com.coxandkings.travel.operations.model.managedocumentation;

import com.coxandkings.travel.operations.enums.managedocumentation.DocumentCopy;
import com.coxandkings.travel.operations.enums.managedocumentation.DocumentStatus;
import com.coxandkings.travel.operations.enums.managedocumentation.DocumentType;

import java.util.List;

public class DocumentVersion {

    private String documentId;
    private String fileName;
    private String documentPageNumber;
    private DocumentType docType;
    private DocumentStatus status;
    private DocumentCopy copyTo;
    private String extension;
    private String roomId;
    private String reason;
    private String remarks;
    private List<String> commIds;

    public DocumentCopy getCopyTo() {
        return copyTo;
    }

    public void setCopyTo(DocumentCopy copyTo) {
        this.copyTo = copyTo;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentPageNumber() {
        return documentPageNumber;
    }

    public void setDocumentPageNumber(String documentPageNumber) {
        this.documentPageNumber = documentPageNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public DocumentType getDocType() {
        return docType;
    }

    public void setDocType(DocumentType docType) {
        this.docType = docType;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<String> getCommIds() {
        return commIds;
    }

    public void setCommIds(List<String> commIds) {
        this.commIds = commIds;
    }
}
