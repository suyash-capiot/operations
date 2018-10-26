package com.coxandkings.travel.operations.model.managedocumentation;

public class CommunicationType {

    private boolean sendDocument;
    private String sendDocCommType;
    private boolean receiveDocument;
    private String receiveDocCommType;

    public boolean isSendDocument() {
        return sendDocument;
    }

    public void setSendDocument(boolean sendDocument) {
        this.sendDocument = sendDocument;
    }

    public String getSendDocCommType() {
        return sendDocCommType;
    }

    public void setSendDocCommType(String sendDocCommType) {
        this.sendDocCommType = sendDocCommType;
    }

    public boolean isReceiveDocument() {
        return receiveDocument;
    }

    public void setReceiveDocument(boolean receiveDocument) {
        this.receiveDocument = receiveDocument;
    }

    public String getReceiveDocCommType() {
        return receiveDocCommType;
    }

    public void setReceiveDocCommType(String receiveDocCommType) {
        this.receiveDocCommType = receiveDocCommType;
    }

}
