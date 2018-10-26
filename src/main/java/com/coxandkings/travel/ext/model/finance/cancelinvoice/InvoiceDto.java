package com.coxandkings.travel.ext.model.finance.cancelinvoice;

import com.coxandkings.travel.ext.model.finance.invoice.Office;
import com.coxandkings.travel.ext.model.finance.invoice.PassengerDetails;
import com.coxandkings.travel.ext.model.finance.invoice.TaxDetailsEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class InvoiceDto {
    private UUID id;
    private IconFileUploadDetailsEntity companyLogoFile;

    private String createdOn;

    private Client client;
    private Boolean isHolidayBooking; //
    private Set<InvoiceParticularsDto> invoiceParticularsDto = new HashSet<InvoiceParticularsDto>();
    private Double totalCost;
    private String totalCostWords;
    private String sbu;
    private Double outStandingAmount;
    private Office salesOffice;
    private String invoiceCurrency;
    private String packages;
    private String division;
    private Long invoiceMonth;
    private Long invoiceYear;
    private String businessUnit;
    private String paymentStatus;
    private String invoiceType;
    private String invoiceStatus;
    private Boolean receiptApplied;
    private Double receiptAmountApplied;
    private String invoiceNumber;
    private Integer version;
    private Double roe;
    private Set<PassengerDetails> allPassenegers = new HashSet<>(); // Only for UI purposes
    private Set<TaxDetailsEntity> allProductsTax = new HashSet<>(); // For Template purposes

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public IconFileUploadDetailsEntity getCompanyLogoFile() {
        return companyLogoFile;
    }

    public void setCompanyLogoFile(IconFileUploadDetailsEntity companyLogoFile) {
        this.companyLogoFile = companyLogoFile;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Boolean getIsHolidayBooking() {
        return isHolidayBooking;
    }

    public void setIsHolidayBooking(Boolean holidayBooking) {
        isHolidayBooking = holidayBooking;
    }

    public Set<InvoiceParticularsDto> getInvoiceParticularsDto() {
        return invoiceParticularsDto;
    }

    public void setInvoiceParticularsDto(Set<InvoiceParticularsDto> invoiceParticularsDto) {
        this.invoiceParticularsDto = invoiceParticularsDto;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public String getTotalCostWords() {
        return totalCostWords;
    }

    public void setTotalCostWords(String totalCostWords) {
        this.totalCostWords = totalCostWords;
    }

    public String getSbu() {
        return sbu;
    }

    public void setSbu(String sbu) {
        this.sbu = sbu;
    }

    public Double getOutStandingAmount() {
        return outStandingAmount;
    }

    public void setOutStandingAmount(Double outStandingAmount) {
        this.outStandingAmount = outStandingAmount;
    }

    public Office getSalesOffice() {
        return salesOffice;
    }

    public void setSalesOffice(Office salesOffice) {
        this.salesOffice = salesOffice;
    }

    public String getInvoiceCurrency() {
        return invoiceCurrency;
    }

    public void setInvoiceCurrency(String invoiceCurrency) {
        this.invoiceCurrency = invoiceCurrency;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public Long getInvoiceMonth() {
        return invoiceMonth;
    }

    public void setInvoiceMonth(Long invoiceMonth) {
        this.invoiceMonth = invoiceMonth;
    }

    public Long getInvoiceYear() {
        return invoiceYear;
    }

    public void setInvoiceYear(Long invoiceYear) {
        this.invoiceYear = invoiceYear;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public Boolean getReceiptApplied() {
        return receiptApplied;
    }

    public void setReceiptApplied(Boolean receiptApplied) {
        this.receiptApplied = receiptApplied;
    }

    public Double getReceiptAmountApplied() {
        return receiptAmountApplied;
    }

    public void setReceiptAmountApplied(Double receiptAmountApplied) {
        this.receiptAmountApplied = receiptAmountApplied;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Double getRoe() {
        return roe;
    }

    public void setRoe(Double roe) {
        this.roe = roe;
    }

    public Set<PassengerDetails> getAllPassenegers() {
        return allPassenegers;
    }

    public void setAllPassenegers(Set<PassengerDetails> allPassenegers) {
        this.allPassenegers = allPassenegers;
    }

    public Set<TaxDetailsEntity> getAllProductsTax() {
        return allProductsTax;
    }

    public void setAllProductsTax(Set<TaxDetailsEntity> allProductsTax) {
        this.allProductsTax = allProductsTax;
    }
}
