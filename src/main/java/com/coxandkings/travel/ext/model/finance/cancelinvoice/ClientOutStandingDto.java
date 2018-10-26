package com.coxandkings.travel.ext.model.finance.cancelinvoice;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClientOutStandingDto {

    private String clientId;
    private String clientType;
    private String clientName;
    private String clientGSTIN;
    private String clientAddress;
    private String clientState;
    private String invoiceNumber;
    private String companyGSTIN;
    private String officeName;
    private String officeAddress;
    private String officeCity;
    private String officePincode;
    private String officeState;
    private Set<ProductOutStandingEntity> productSet;
    private Double dueAmount;
    private String currencyOfDueAmount;
    private Long applicableDueDate;
    private Double rateOfInterest;
    private Long interestPeriod;
    private String businessUnit;
    private Double calculatedInterest;
    private String typeOfInvoice = "TAX_INVOICE";
    private List<String> bookingReferenceNumbers = new ArrayList<>();

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Double getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(Double dueAmount) {
        this.dueAmount = dueAmount;
    }

    public String getCurrencyOfDueAmount() {
        return currencyOfDueAmount;
    }

    public void setCurrencyOfDueAmount(String currencyOfDueAmount) {
        this.currencyOfDueAmount = currencyOfDueAmount;
    }

    public Long getApplicableDueDate() {
        return applicableDueDate;
    }

    public void setApplicableDueDate(Long applicableDueDate) {
        this.applicableDueDate = applicableDueDate;
    }

    public Double getRateOfInterest() {
        return rateOfInterest;
    }

    public void setRateOfInterest(Double rateOfInterest) {
        this.rateOfInterest = rateOfInterest;
    }

    public Long getInterestPeriod() {
        return interestPeriod;
    }

    public void setInterestPeriod(Long interestPeriod) {
        this.interestPeriod = interestPeriod;
    }

    public Double getCalculatedInterest() {
        return calculatedInterest;
    }

    public void setCalculatedInterest(Double calculatedInterest) {
        this.calculatedInterest = calculatedInterest;
    }

    public String getTypeOfInvoice() {
        return typeOfInvoice;
    }

    public void setTypeOfInvoice(String typeOfInvoice) {
        this.typeOfInvoice = typeOfInvoice;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientGSTIN() {
        return clientGSTIN;
    }

    public void setClientGSTIN(String clientGSTIN) {
        this.clientGSTIN = clientGSTIN;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getClientState() {
        return clientState;
    }

    public void setClientState(String clientState) {
        this.clientState = clientState;
    }

    public String getCompanyGSTIN() {
        return companyGSTIN;
    }

    public void setCompanyGSTIN(String companyGSTIN) {
        this.companyGSTIN = companyGSTIN;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getOfficeCity() {
        return officeCity;
    }

    public void setOfficeCity(String officeCity) {
        this.officeCity = officeCity;
    }

    public String getOfficePincode() {
        return officePincode;
    }

    public void setOfficePincode(String officePincode) {
        this.officePincode = officePincode;
    }

    public String getOfficeState() {
        return officeState;
    }

    public void setOfficeState(String officeState) {
        this.officeState = officeState;
    }

    public Set<ProductOutStandingEntity> getProductSet() {
        return productSet;
    }

    public void setProductSet(Set<ProductOutStandingEntity> productSet) {
        this.productSet = productSet;
    }

    public List<String> getBookingReferenceNumbers() {
        return bookingReferenceNumbers;
    }

    public void setBookingReferenceNumbers(List<String> bookingReferanceNumbers) {
        this.bookingReferenceNumbers = bookingReferanceNumbers;
    }

}

