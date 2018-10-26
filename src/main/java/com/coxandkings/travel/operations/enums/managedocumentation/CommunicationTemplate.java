package com.coxandkings.travel.operations.enums.managedocumentation;

public enum  CommunicationTemplate {

    COMM_FWD_DOCS_RCVD("MNG DOC - FWD DOCS RCVD"),
    COMM_FWD_DOCS_SENT("MNG DOC - FWD DOCS TO BE SENT"),
    COMM_EMAIL_HNDOVR_DOCS("MNG DOC - EMAIL HNDOVR DOCS"),
    COMM_RSND_DOCS_TO_CUST("MNG DOC- RESEND DOCUMENTS"),
    COMM_SEND_DOCS_TO_SUPP("MNG DOC- EMAIL SPLR FOR SENDNG DOCS"),
    COMM_SEND_DOCS_TO_CUST("MNG DOC - SEND DOCS TO CUST"),
    COMM_SEND_HNDOVR_DOCS_TO_CUST("MNG DOC - SEND HNDOVR DOCS TO CUST"),
    COMM_SND_HNDOVR_DOCS_TO_SUPP("MNG DOC - SEND HNDOVR DOCS TO SPLR"),
    COMM_SND_HNDOVR_DOCS_TO_MOS("MNG DOC - SEND HNDOVR DOCS TO MOS"),
    COMM_SND_HNDOVR_DOCS_TO_TOURMNGR("MNG DOC - SEND HNDOVR DOCS TO TOURMNGR"),
    COMM_COPY_SEND("Copy");

    private String scenario;

    CommunicationTemplate(String scenario) {
        this.scenario = scenario;
    }

    public String getScenario(){
        return this.scenario;
    }

}
