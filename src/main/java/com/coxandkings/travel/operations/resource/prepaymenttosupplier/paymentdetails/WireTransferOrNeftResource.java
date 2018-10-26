package com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentdetails;

public class WireTransferOrNeftResource {

    private String id;
    private String fromBankName;
    private String toBankName;
    private String accountNumber;
    private String swiftIfscCode;
    private String intermediaryBankSwiftOrIfscCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromBankName() {
        return fromBankName;
    }

    public void setFromBankName(String fromBankName) {
        this.fromBankName = fromBankName;
    }

    public String getToBankName() {
        return toBankName;
    }

    public void setToBankName(String toBankName) {
        this.toBankName = toBankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSwiftIfscCode() {
        return swiftIfscCode;
    }

    public void setSwiftIfscCode(String swiftIfscCode) {
        this.swiftIfscCode = swiftIfscCode;
    }

    public String getIntermediaryBankSwiftOrIfscCode() {
        return intermediaryBankSwiftOrIfscCode;
    }

    public void setIntermediaryBankSwiftOrIfscCode(String intermediaryBankSwiftOrIfscCode) {
        this.intermediaryBankSwiftOrIfscCode = intermediaryBankSwiftOrIfscCode;
    }
}
