package com.coxandkings.travel.operations.model.managedocumentation;

import com.coxandkings.travel.operations.enums.managedocumentation.DocumentStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Document {

    private String paxId;
    private String passengerName;
    private String documentReceivedDate;
    private Boolean visaAndPassportDetailsRequired;
    //passport details
    private String passportNumber;
    private String dateOfIssue;
    private String passportExpiryDate;
    private String placeOfIssue;
    private String nationality;

    //visa details
    private String country;
    private String durationOfVisaDays;
    private String noOfEntries;
    private String typeOfVisa;
    private String purposeOfVisit;
    private String visaValidFromDate;
    private String visaValidTillDate;
    private String entryType;
    private String barcodeNo;
    private String citizenshipStatus;
    private String passportIssuedIn;

    private boolean visaDetailsEntered;
    private boolean passportDetailsEntered;

    private DocumentStatus documentStatus;
    private String remarks;
    private Boolean canEdit;
    private Boolean canUpload;
    private Boolean canCopy;
    private List<DocumentVersion> documentVersions = new ArrayList<>();
    private Set<String> communicationIds = new HashSet<>();
    private Set<RoomDetails> roomIds = new HashSet<>();
    private List<RoomWiseDocuments> roomWiseDocuments = new ArrayList<>();
    private Set<String> copiedTo = new HashSet<>();
    private List<DocumentVersion> sentDocuments = new ArrayList<>();
    private List<DocumentVersion> receivedDocuments = new ArrayList<>();
    private List<DocumentVersion> customerCopy = new ArrayList<>();
    private List<DocumentVersion> supplierCopy = new ArrayList<>();
    private List<DocumentVersion> mosCopy = new ArrayList<>();
    private List<DocumentVersion> tourManagerCopy = new ArrayList<>();
    private List<DocumentVersion> generatedDocuments = new ArrayList<>();
    private boolean approvalRequestSent;

    public String getPaxId() {
        return paxId;
    }

    public void setPaxId(String paxId) {
        this.paxId = paxId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getDocumentReceivedDate() {
        return documentReceivedDate;
    }

    public void setDocumentReceivedDate(String documentReceivedDate) {
        this.documentReceivedDate = documentReceivedDate;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public String getPassportExpiryDate() {
        return passportExpiryDate;
    }

    public void setPassportExpiryDate(String passportExpiryDate) {
        this.passportExpiryDate = passportExpiryDate;
    }

    public String getPlaceOfIssue() {
        return placeOfIssue;
    }

    public void setPlaceOfIssue(String placeOfIssue) {
        this.placeOfIssue = placeOfIssue;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDurationOfVisaDays() {
        return durationOfVisaDays;
    }

    public void setDurationOfVisaDays(String durationOfVisaDays) {
        this.durationOfVisaDays = durationOfVisaDays;
    }

    public String getNoOfEntries() {
        return noOfEntries;
    }

    public void setNoOfEntries(String noOfEntries) {
        this.noOfEntries = noOfEntries;
    }

    public String getTypeOfVisa() {
        return typeOfVisa;
    }

    public void setTypeOfVisa(String typeOfVisa) {
        this.typeOfVisa = typeOfVisa;
    }

    public String getPurposeOfVisit() {
        return purposeOfVisit;
    }

    public void setPurposeOfVisit(String purposeOfVisit) {
        this.purposeOfVisit = purposeOfVisit;
    }

    public DocumentStatus getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(DocumentStatus documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(Boolean canEdit) {
        this.canEdit = canEdit;
    }

    public Boolean getCanUpload() {
        return canUpload;
    }

    public void setCanUpload(Boolean canUpload) {
        this.canUpload = canUpload;
    }

    public Boolean getCanCopy() {
        return canCopy;
    }

    public void setCanCopy(Boolean canCopy) {
        this.canCopy = canCopy;
    }

    public List<DocumentVersion> getDocumentVersions() {
        return documentVersions;
    }

    public void setDocumentVersions(List<DocumentVersion> documentVersions) {
        this.documentVersions = documentVersions;
    }

    public Set<String> getCommunicationIds() {
        return communicationIds;
    }

    public void setCommunicationIds(Set<String> communicationIds) {
        this.communicationIds = communicationIds;
    }

    public Set<RoomDetails> getRoomIds() {
        return roomIds;
    }

    public void setRoomIds(Set<RoomDetails> roomIds) {
        this.roomIds = roomIds;
    }

    public Boolean getVisaAndPassportDetailsRequired() {
        return visaAndPassportDetailsRequired;
    }

    public void setVisaAndPassportDetailsRequired(Boolean visaAndPassportDetailsRequired) {
        this.visaAndPassportDetailsRequired = visaAndPassportDetailsRequired;
    }

    public String getVisaValidFromDate() {
        return visaValidFromDate;
    }

    public void setVisaValidFromDate(String visaValidFromDate) {
        this.visaValidFromDate = visaValidFromDate;
    }

    public String getVisaValidTillDate() {
        return visaValidTillDate;
    }

    public void setVisaValidTillDate(String visaValidTillDate) {
        this.visaValidTillDate = visaValidTillDate;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getBarcodeNo() {
        return barcodeNo;
    }

    public void setBarcodeNo(String barcodeNo) {
        this.barcodeNo = barcodeNo;
    }

    public String getCitizenshipStatus() {
        return citizenshipStatus;
    }

    public void setCitizenshipStatus(String citizenshipStatus) {
        this.citizenshipStatus = citizenshipStatus;
    }

    public String getPassportIssuedIn() {
        return passportIssuedIn;
    }

    public void setPassportIssuedIn(String passportIssuedIn) {
        this.passportIssuedIn = passportIssuedIn;
    }

    public List<RoomWiseDocuments> getRoomWiseDocuments() {
        return roomWiseDocuments;
    }

    public void setRoomWiseDocuments(List<RoomWiseDocuments> roomWiseDocuments) {
        this.roomWiseDocuments = roomWiseDocuments;
    }

    public Set<String> getCopiedTo() {
        return copiedTo;
    }

    public void setCopiedTo(Set<String> copiedTo) {
        this.copiedTo = copiedTo;
    }

    public List<DocumentVersion> getSentDocuments() {
        return sentDocuments;
    }

    public void setSentDocuments(List<DocumentVersion> sentDocuments) {
        this.sentDocuments = sentDocuments;
    }

    public List<DocumentVersion> getReceivedDocuments() {
        return receivedDocuments;
    }

    public void setReceivedDocuments(List<DocumentVersion> receivedDocuments) {
        this.receivedDocuments = receivedDocuments;
    }

    public List<DocumentVersion> getCustomerCopy() {
        return customerCopy;
    }

    public void setCustomerCopy(List<DocumentVersion> customerCopy) {
        this.customerCopy = customerCopy;
    }

    public List<DocumentVersion> getSupplierCopy() {
        return supplierCopy;
    }

    public void setSupplierCopy(List<DocumentVersion> supplierCopy) {
        this.supplierCopy = supplierCopy;
    }

    public List<DocumentVersion> getMosCopy() {
        return mosCopy;
    }

    public void setMosCopy(List<DocumentVersion> mosCopy) {
        this.mosCopy = mosCopy;
    }

    public List<DocumentVersion> getTourManagerCopy() {
        return tourManagerCopy;
    }

    public void setTourManagerCopy(List<DocumentVersion> tourManagerCopy) {
        this.tourManagerCopy = tourManagerCopy;
    }

    public List<DocumentVersion> getGeneratedDocuments() {
        return generatedDocuments;
    }

    public void setGeneratedDocuments(List<DocumentVersion> generatedDocuments) {
        this.generatedDocuments = generatedDocuments;
    }

    public boolean isVisaDetailsEntered() {
        return visaDetailsEntered;
    }

    public void setVisaDetailsEntered(boolean visaDetailsEntered) {
        this.visaDetailsEntered = visaDetailsEntered;
    }

    public boolean isPassportDetailsEntered() {
        return passportDetailsEntered;
    }

    public void setPassportDetailsEntered(boolean passportDetailsEntered) {
        this.passportDetailsEntered = passportDetailsEntered;
    }

    public boolean isApprovalRequestSent() {
        return approvalRequestSent;
    }

    public void setApprovalRequestSent(boolean approvalRequestSent) {
        this.approvalRequestSent = approvalRequestSent;
    }
}
