package com.coxandkings.travel.operations.model.accountsummary;

public class InvoiceCreditDebitNoteInfo {

    private String creditDebitNoteNumber;

    private String noteType;

    public InvoiceCreditDebitNoteInfo() {
    }

    public String getCreditDebitNoteNumber() {
        return creditDebitNoteNumber;
    }

    public void setCreditDebitNoteNumber(String creditDebitNoteNumber) {
        this.creditDebitNoteNumber = creditDebitNoteNumber;
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }
}
