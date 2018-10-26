package com.coxandkings.travel.operations.resource.managedocumentation;

import com.coxandkings.travel.operations.enums.managedocumentation.CommunicationTemplate;
import com.coxandkings.travel.operations.enums.managedocumentation.DocumentEntityReference;

import java.util.List;

public class DocumentsResource {

    private String handoverDocGenId;
    private String paxId;
    private String roomId;
    private PassportResource passport;
    private VisaDocumentResource visa;
    private String orderId;
    private DocumentEntityReference documentWise;
    private String documentSettingId;
    private String bookID;
    private String fileType;
    private String fileName;
    private String documentId;
    private String extension;
    private String remarks;
    private String documentPageNumber;
    private Boolean isVisaDocument;
    private Boolean isPassportDocument;
    private Boolean isVerified;
    private Boolean disqualifyPreviousVersion;
    private String editOrReUploadDocumentId;
    private String editOrReUploadDocumentPageNo;
    private List<String> copyTo;
    List<String> documentIds;
    private Boolean kafkaGenerated;
    private String sendDocTo;
    private String communicationId;
    private CommunicationTemplate communicationTemplate;
    private String documentName;
    private String approvalRequestJustification;
    private String customerName;
    private String productName;
    private TotalRevenueAndGrossProfitResource revenueAndGrossProfitResource;
    private boolean sendDocsToSupplier;
    private String paxName;
    private boolean clientEmail;

    public String getPaxId() {
        return paxId;
    }

    public void setPaxId(String paxId) {
        this.paxId = paxId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public PassportResource getPassport() {
        return passport;
    }

    public void setPassport(PassportResource passport) {
        this.passport = passport;
    }

    public VisaDocumentResource getVisa() {
        return visa;
    }

    public void setVisa(VisaDocumentResource visa) {
        this.visa = visa;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public DocumentEntityReference getDocumentWise() {
        return documentWise;
    }

    public void setDocumentWise(DocumentEntityReference documentWise) {
        this.documentWise = documentWise;
    }

    public String getDocumentSettingId() {
        return documentSettingId;
    }

    public void setDocumentSettingId(String documentSettingId) {
        this.documentSettingId = documentSettingId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDocumentPageNumber() {
        return documentPageNumber;
    }

    public void setDocumentPageNumber(String documentPageNumber) {
        this.documentPageNumber = documentPageNumber;
    }

    public Boolean getVisaDocument() {
        return isVisaDocument;
    }

    public void setVisaDocument(Boolean visaDocument) {
        isVisaDocument = visaDocument;
    }

    public Boolean getPassportDocument() {
        return isPassportDocument;
    }

    public void setPassportDocument(Boolean passportDocument) {
        isPassportDocument = passportDocument;
    }

    public Boolean getDisqualifyPreviousVersion() {
        return disqualifyPreviousVersion;
    }

    public void setDisqualifyPreviousVersion(Boolean disqualifyPreviousVersion) {
        this.disqualifyPreviousVersion = disqualifyPreviousVersion;
    }

    public String getEditOrReUploadDocumentId() {
        return editOrReUploadDocumentId;
    }

    public void setEditOrReUploadDocumentId(String editOrReUploadDocumentId) {
        this.editOrReUploadDocumentId = editOrReUploadDocumentId;
    }

    public String getEditOrReUploadDocumentPageNo() {
        return editOrReUploadDocumentPageNo;
    }

    public void setEditOrReUploadDocumentPageNo(String editOrReUploadDocumentPageNo) {
        this.editOrReUploadDocumentPageNo = editOrReUploadDocumentPageNo;
    }

    public List<String> getCopyTo() {
        return copyTo;
    }

    public void setCopyTo(List<String> copyTo) {
        this.copyTo = copyTo;
    }

    public Boolean getKafkaGenerated() {
        return kafkaGenerated;
    }

    public void setKafkaGenerated(Boolean kafkaGenerated) {
        this.kafkaGenerated = kafkaGenerated;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public List<String> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<String> documentIds) {
        this.documentIds = documentIds;
    }

    public String getSendDocTo() {
        return sendDocTo;
    }

    public void setSendDocTo(String sendDocTo) {
        this.sendDocTo = sendDocTo;
    }

    public String getCommunicationId() {
        return communicationId;
    }

    public void setCommunicationId(String communicationId) {
        this.communicationId = communicationId;
    }

    public CommunicationTemplate getCommunicationTemplate() {
        return communicationTemplate;
    }

    public void setCommunicationTemplate(CommunicationTemplate communicationTemplate) {
        this.communicationTemplate = communicationTemplate;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getHandoverDocGenId() {
        return handoverDocGenId;
    }

    public void setHandoverDocGenId(String handoverDocGenId) {
        this.handoverDocGenId = handoverDocGenId;
    }

    public String getApprovalRequestJustification() {
        return approvalRequestJustification;
    }

    public void setApprovalRequestJustification(String approvalRequestJustification) {
        this.approvalRequestJustification = approvalRequestJustification;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public TotalRevenueAndGrossProfitResource getRevenueAndGrossProfitResource() {
        return revenueAndGrossProfitResource;
    }

    public void setRevenueAndGrossProfitResource(TotalRevenueAndGrossProfitResource revenueAndGrossProfitResource) {
        this.revenueAndGrossProfitResource = revenueAndGrossProfitResource;
    }

    public boolean isSendDocsToSupplier() {
        return sendDocsToSupplier;
    }

    public void setSendDocsToSupplier(boolean sendDocsToSupplier) {
        this.sendDocsToSupplier = sendDocsToSupplier;
    }

    public String getPaxName() {
        return paxName;
    }

    public void setPaxName(String paxName) {
        this.paxName = paxName;
    }

    public boolean isClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(boolean clientEmail) {
        this.clientEmail = clientEmail;
    }
}
