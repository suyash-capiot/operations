package com.coxandkings.travel.operations.resource.failure;

public class PaymentAttributesResource {

    private String bankName;

    private String toBankName;

    private String cardType;

    private String nameOnCard;

    private String bankAccount;

    private String depositRefNumber;

    private String noOfProducts;

    private String ifscOrSwiftCode;

    private String intermediaryIfscOrSwiftCode;

    private String ivrDetails;

    private String contactPerson;

    private String address;

    private String mobileNumber;

    private String chequeOrDDNumber;

    private String merchantId;


    public PaymentAttributesResource() {
        this.bankName = "";
        this.toBankName = "";
        this.cardType = "";
        this.nameOnCard = "";
        this.bankAccount = "";
        this.depositRefNumber = "";
        this.noOfProducts = "";
        this.ifscOrSwiftCode = "";
        this.intermediaryIfscOrSwiftCode = "";
        this.ivrDetails = "";
        this.contactPerson = "";
        this.address = "";
        this.mobileNumber = "";
        this.chequeOrDDNumber = "";
        this.merchantId = "";
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getToBankName() {
        return toBankName;
    }

    public void setToBankName(String toBankName) {
        this.toBankName = toBankName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getDepositRefNumber() {
        return depositRefNumber;
    }

    public void setDepositRefNumber(String depositRefNumber) {
        this.depositRefNumber = depositRefNumber;
    }

    public String getNoOfProducts() {
        return noOfProducts;
    }

    public void setNoOfProducts(String noOfProducts) {
        this.noOfProducts = noOfProducts;
    }

    public String getIfscOrSwiftCode() {
        return ifscOrSwiftCode;
    }

    public void setIfscOrSwiftCode(String ifscOrSwiftCode) {
        this.ifscOrSwiftCode = ifscOrSwiftCode;
    }

    public String getIntermediaryIfscOrSwiftCode() {
        return intermediaryIfscOrSwiftCode;
    }

    public void setIntermediaryIfscOrSwiftCode(String intermediaryIfscOrSwiftCode) {
        this.intermediaryIfscOrSwiftCode = intermediaryIfscOrSwiftCode;
    }

    public String getIvrDetails() {
        return ivrDetails;
    }

    public void setIvrDetails(String ivrDetails) {
        this.ivrDetails = ivrDetails;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getChequeOrDDNumber() {
        return chequeOrDDNumber;
    }

    public void setChequeOrDDNumber(String chequeOrDDNumber) {
        this.chequeOrDDNumber = chequeOrDDNumber;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}
