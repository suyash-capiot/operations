package com.coxandkings.travel.operations.model.managedocumentation;

import com.coxandkings.travel.operations.enums.managedocumentation.DocumentEntityReference;
import com.coxandkings.travel.operations.enums.managedocumentation.DocumentType;

import java.util.ArrayList;
import java.util.List;

public class DocumentRowItem {

    private String bookId;

    private String orderId;

    private String productName;

    private boolean isOrderLevel;

    private String documentName; //this is a category classifier

    private CommunicationType communicationType;

    private String documentID;

    private DocumentEntityReference documentWise;

    private String docWise;

    private String documentFormat;

    private String cutOffDueDate;

    private String documentSettingId;

    private String productStatus;

    private DocumentType documentType;

    private boolean disableHandoverDocActions;

    private ModeOfCommunication modeOfCommunication;

    private List<Document> documents = new ArrayList<>();

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
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

    public boolean isOrderLevel() {
        return isOrderLevel;
    }

    public void setOrderLevel(boolean orderLevel) {
        isOrderLevel = orderLevel;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public CommunicationType getCommunicationType() {
        return communicationType;
    }

    public void setCommunicationType(CommunicationType communicationType) {
        this.communicationType = communicationType;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public DocumentEntityReference getDocumentWise() {
        return documentWise;
    }

    public void setDocumentWise(DocumentEntityReference documentWise) {
        this.documentWise = documentWise;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentFormat() {
        return documentFormat;
    }

    public void setDocumentFormat(String documentFormat) {
        this.documentFormat = documentFormat;
    }

    public String getCutOffDueDate() {
        return cutOffDueDate;
    }

    public void setCutOffDueDate(String cutOffDueDate) {
        this.cutOffDueDate = cutOffDueDate;
    }

    public String getDocumentSettingId() {
        return documentSettingId;
    }

    public void setDocumentSettingId(String documentSettingId) {
        this.documentSettingId = documentSettingId;
    }

    public ModeOfCommunication getModeOfCommunication() {
        return modeOfCommunication;
    }

    public void setModeOfCommunication(ModeOfCommunication modeOfCommunication) {
        this.modeOfCommunication = modeOfCommunication;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getDocWise() {
        return docWise;
    }

    public void setDocWise(String docWise) {
        this.docWise = docWise;
    }

    public boolean isDisableHandoverDocActions() {
        return disableHandoverDocActions;
    }

    public void setDisableHandoverDocActions(boolean disableHandoverDocActions) {
        this.disableHandoverDocActions = disableHandoverDocActions;
    }
}
