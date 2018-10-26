package com.coxandkings.travel.operations.resource.managedocumentation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingDocumentDetailsResource {

    Map<String, String> additionalAttributes;
    private String documentID;
    private String documentType;
    private String documentName;
    private String bookingReferenceNo;
    private String orderId;
    private String productName;
    private String documentBy;
    private String documentFileName;
    private Boolean active;
    private String documentPageNumber;
    private String documentExtension;
    private String documentAsPer;
    private String documentFormat;
    private String receivedStatus;
    private String sendStatus;
    private List<String> communicationIds;
    private String paxName;
    private List<String> modeOfCommunication;
    private String copyTo;
    private String documentReceivedDate;
    private String cutOffDate;
    private String documentStatus;
    private String remarks;
    private PassportResource passport;
    private VisaDocumentResource visa;
    private String documentSettingId;
    private String paxId;
    private String roomId;
    private String reason;
    private Boolean isOrderLevel;
    private boolean visaDocsUploaded;
    private boolean passportDocsUploaded;
    private String disqualifyDocumentId;
    private String disqualifyDocumentPageNo;
    private Boolean visaAndPassportDetailsRequired;
    private String copiedDocumentId;

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getBookingReferenceNo() {
        return bookingReferenceNo;
    }

    public void setBookingReferenceNo(String bookingReferenceNo) {
        this.bookingReferenceNo = bookingReferenceNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDocumentBy() {
        return documentBy;
    }

    public void setDocumentBy(String documentBy) {
        this.documentBy = documentBy;
    }

    public String getDocumentFileName() {
        return documentFileName;
    }

    public void setDocumentFileName(String documentFileName) {
        this.documentFileName = documentFileName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDocumentPageNumber() {
        return documentPageNumber;
    }

    public void setDocumentPageNumber(String documentPageNumber) {
        this.documentPageNumber = documentPageNumber;
    }

    public String getDocumentAsPer() {
        return documentAsPer;
    }

    public void setDocumentAsPer(String documentAsPer) {
        this.documentAsPer = documentAsPer;
    }

    public String getDocumentFormat() {
        return documentFormat;
    }

    public void setDocumentFormat(String documentFormat) {
        this.documentFormat = documentFormat;
    }

    public String getReceivedStatus() {
        return receivedStatus;
    }

    public void setReceivedStatus(String receivedStatus) {
        this.receivedStatus = receivedStatus;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public List<String> getCommunicationIds() {
        return communicationIds;
    }

    public void setCommunicationIds(List<String> communicationIds) {
        this.communicationIds = communicationIds;
    }

    public String getPaxName() {
        return paxName;
    }

    public void setPaxName(String paxName) {
        this.paxName = paxName;
    }

    public List<String> getModeOfCommunication() {
        return modeOfCommunication;
    }

    public void setModeOfCommunication(List<String> modeOfCommunication) {
        this.modeOfCommunication = modeOfCommunication;
    }

    public String getCopyTo() {
        return copyTo;
    }

    public void setCopyTo(String copyTo) {
        this.copyTo = copyTo;
    }

    public String getDocumentReceivedDate() {
        return documentReceivedDate;
    }

    public void setDocumentReceivedDate(String documentReceivedDate) {
        this.documentReceivedDate = documentReceivedDate;
    }

    public String getCutOffDate() {
        return cutOffDate;
    }

    public void setCutOffDate(String cutOffDate) {
        this.cutOffDate = cutOffDate;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getDocumentSettingId() {
        return documentSettingId;
    }

    public void setDocumentSettingId(String documentSettingId) {
        this.documentSettingId = documentSettingId;
    }

    public String getPaxId() {
        return paxId;
    }

    public void setPaxId(String paxId) {
        this.paxId = paxId;
    }

    public Map<String, String> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(Map<String, String> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
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

    public Boolean getOrderLevel() {
        return isOrderLevel;
    }

    public void setOrderLevel(Boolean orderLevel) {
        isOrderLevel = orderLevel;
    }

    public boolean isVisaDocsUploaded() {
        return visaDocsUploaded;
    }

    public void setVisaDocsUploaded(boolean visaDocsUploaded) {
        this.visaDocsUploaded = visaDocsUploaded;
    }

    public boolean isPassportDocsUploaded() {
        return passportDocsUploaded;
    }

    public void setPassportDocsUploaded(boolean passportDocsUploaded) {
        this.passportDocsUploaded = passportDocsUploaded;
    }

    public String getDisqualifyDocumentId() {
        return disqualifyDocumentId;
    }

    public void setDisqualifyDocumentId(String disqualifyDocumentId) {
        this.disqualifyDocumentId = disqualifyDocumentId;
    }

    public String getDisqualifyDocumentPageNo() {
        return disqualifyDocumentPageNo;
    }

    public void setDisqualifyDocumentPageNo(String disqualifyDocumentPageNo) {
        this.disqualifyDocumentPageNo = disqualifyDocumentPageNo;
    }

    public Boolean getVisaAndPassportDetailsRequired() {
        return visaAndPassportDetailsRequired;
    }

    public void setVisaAndPassportDetailsRequired(Boolean visaAndPassportDetailsRequired) {
        this.visaAndPassportDetailsRequired = visaAndPassportDetailsRequired;
    }

    public String getDocumentExtension() {
        return documentExtension;
    }

    public void setDocumentExtension(String documentExtension) {
        this.documentExtension = documentExtension;
    }

    public String getCopiedDocumentId() {
        return copiedDocumentId;
    }

    public void setCopiedDocumentId(String copiedDocumentId) {
        this.copiedDocumentId = copiedDocumentId;
    }
}
