
package com.coxandkings.travel.ext.model.finance.invoice;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "companyLogoFile",
    "createdOn",
    "client",
    "invoiceParticularsDto",
    "totalCost",
    "totalCostWords",
    "sbu",
    "balance",
    "ttAndCo",
    "gstn",
    "panNo",
    "office",
    "currency",
    "packages",
    "division",
    "invoiceMonth",
    "invoiceYear",
    "businessUnit",
    "paymentStatus",
    "invoiceType",
    "invoiceStatus",
    "receiptApplied",
    "receiptAmountApplied",
    "invoiceNumber",
    "version",
    "roe",
    "allPassenegers",
    "allProductsTax"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Invoice {

    @JsonProperty("id")
    private String id;
    @JsonProperty("companyLogoFile")
    private CompanyLogoFile companyLogoFile;
    @JsonProperty("createdOn")
    private String createdOn;
    @JsonProperty("client")
    private Client client;
    @JsonProperty("isHolidayBooking")
    private String isHolidayBooking;
    @JsonProperty("invoiceParticularsDto")
    private List<InvoiceParticularsDto> invoiceParticularsDto = null;
    @JsonProperty("totalCost")
    private Double totalCost;
    @JsonProperty("totalCostWords")
    private String totalCostWords;
    @JsonProperty("sbu")
    private Object sbu;
    @JsonProperty("outStandingAmount")
    private Double outStandingAmount;
    @JsonProperty("salesOffice")
    private SalesOffice salesOffice;
    @JsonProperty("invoiceCurrency")
    private String invoiceCurrency;
    @JsonProperty("balance")
    private Double balance;
    @JsonProperty("ttAndCo")
    private String ttAndCo;
    @JsonProperty("gstn")
    private String gstn;
    @JsonProperty("panNo")
    private String panNo;
    @JsonProperty("office")
    private Office office;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("packages")
    private String packages;
    @JsonProperty("division")
    private String division;
    @JsonProperty("invoiceMonth")
    private Integer invoiceMonth;
    @JsonProperty("invoiceYear")
    private Integer invoiceYear;
    @JsonProperty("businessUnit")
    private String businessUnit;
    @JsonProperty("paymentStatus")
    private String paymentStatus;
    @JsonProperty("invoiceType")
    private String invoiceType;
    @JsonProperty("invoiceStatus")
    private String invoiceStatus;
    @JsonProperty("receiptApplied")
    private Boolean receiptApplied;
    @JsonProperty("receiptAmountApplied")
    private Integer receiptAmountApplied;
    @JsonProperty("invoiceNumber")
    private String invoiceNumber;
    @JsonProperty("version")
    private Integer version;
    @JsonProperty("roe")
    private Integer roe;
    @JsonProperty("allPassenegers")
    private List<AllPasseneger> allPassenegers = null;
    @JsonProperty("allProductsTax")
    private List<AllProductsTax> allProductsTax = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("companyLogoFile")
    public CompanyLogoFile getCompanyLogoFile() {
        return companyLogoFile;
    }

    @JsonProperty("companyLogoFile")
    public void setCompanyLogoFile(CompanyLogoFile companyLogoFile) {
        this.companyLogoFile = companyLogoFile;
    }

    @JsonProperty("createdOn")
    public String getCreatedOn() {
        return createdOn;
    }

    @JsonProperty("createdOn")
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    @JsonProperty("client")
    public Client getClient() {
        return client;
    }

    @JsonProperty("client")
    public void setClient(Client client) {
        this.client = client;
    }

    @JsonProperty("invoiceParticularsDto")
    public List<InvoiceParticularsDto> getInvoiceParticularsDto() {
        return invoiceParticularsDto;
    }

    @JsonProperty("invoiceParticularsDto")
    public void setInvoiceParticularsDto(List<InvoiceParticularsDto> invoiceParticularsDto) {
        this.invoiceParticularsDto = invoiceParticularsDto;
    }

    @JsonProperty("totalCost")
    public Double getTotalCost() {
        return totalCost;
    }

    @JsonProperty("totalCost")
    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    @JsonProperty("totalCostWords")
    public String getTotalCostWords() {
        return totalCostWords;
    }

    @JsonProperty("totalCostWords")
    public void setTotalCostWords(String totalCostWords) {
        this.totalCostWords = totalCostWords;
    }

    @JsonProperty("sbu")
    public Object getSbu() {
        return sbu;
    }

    @JsonProperty("sbu")
    public void setSbu(Object sbu) {
        this.sbu = sbu;
    }

    @JsonProperty("balance")
    public Double getBalance() {
        return balance;
    }

    @JsonProperty("balance")
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @JsonProperty("ttAndCo")
    public String getTtAndCo() {
        return ttAndCo;
    }

    @JsonProperty("ttAndCo")
    public void setTtAndCo(String ttAndCo) {
        this.ttAndCo = ttAndCo;
    }

    @JsonProperty("gstn")
    public String getGstn() {
        return gstn;
    }

    @JsonProperty("gstn")
    public void setGstn(String gstn) {
        this.gstn = gstn;
    }

    @JsonProperty("panNo")
    public String getPanNo() {
        return panNo;
    }

    @JsonProperty("panNo")
    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    @JsonProperty("office")
    public Office getOffice() {
        return office;
    }

    @JsonProperty("office")
    public void setOffice(Office office) {
        this.office = office;
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("packages")
    public String getPackages() {
        return packages;
    }

    @JsonProperty("packages")
    public void setPackages(String packages) {
        this.packages = packages;
    }

    @JsonProperty("division")
    public String getDivision() {
        return division;
    }

    @JsonProperty("division")
    public void setDivision(String division) {
        this.division = division;
    }

    @JsonProperty("invoiceMonth")
    public Integer getInvoiceMonth() {
        return invoiceMonth;
    }

    @JsonProperty("invoiceMonth")
    public void setInvoiceMonth(Integer invoiceMonth) {
        this.invoiceMonth = invoiceMonth;
    }

    @JsonProperty("invoiceYear")
    public Integer getInvoiceYear() {
        return invoiceYear;
    }

    @JsonProperty("invoiceYear")
    public void setInvoiceYear(Integer invoiceYear) {
        this.invoiceYear = invoiceYear;
    }

    @JsonProperty("businessUnit")
    public String getBusinessUnit() {
        return businessUnit;
    }

    @JsonProperty("businessUnit")
    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    @JsonProperty("paymentStatus")
    public String getPaymentStatus() {
        return paymentStatus;
    }

    @JsonProperty("paymentStatus")
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @JsonProperty("invoiceType")
    public String getInvoiceType() {
        return invoiceType;
    }

    @JsonProperty("invoiceType")
    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    @JsonProperty("invoiceStatus")
    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    @JsonProperty("invoiceStatus")
    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    @JsonProperty("receiptApplied")
    public Boolean getReceiptApplied() {
        return receiptApplied;
    }

    @JsonProperty("receiptApplied")
    public void setReceiptApplied(Boolean receiptApplied) {
        this.receiptApplied = receiptApplied;
    }

    @JsonProperty("receiptAmountApplied")
    public Integer getReceiptAmountApplied() {
        return receiptAmountApplied;
    }

    @JsonProperty("receiptAmountApplied")
    public void setReceiptAmountApplied(Integer receiptAmountApplied) {
        this.receiptAmountApplied = receiptAmountApplied;
    }

    @JsonProperty("invoiceNumber")
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    @JsonProperty("invoiceNumber")
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    @JsonProperty("version")
    public Integer getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(Integer version) {
        this.version = version;
    }

    @JsonProperty("roe")
    public Integer getRoe() {
        return roe;
    }

    @JsonProperty("roe")
    public void setRoe(Integer roe) {
        this.roe = roe;
    }

    @JsonProperty("allPassenegers")
    public List<AllPasseneger> getAllPassenegers() {
        return allPassenegers;
    }

    @JsonProperty("allPassenegers")
    public void setAllPassenegers(List<AllPasseneger> allPassenegers) {
        this.allPassenegers = allPassenegers;
    }

    @JsonProperty("allProductsTax")
    public List<AllProductsTax> getAllProductsTax() {
        return allProductsTax;
    }

    @JsonProperty("allProductsTax")
    public void setAllProductsTax(List<AllProductsTax> allProductsTax) {
        this.allProductsTax = allProductsTax;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getIsHolidayBooking() {
        return isHolidayBooking;
    }

    public void setIsHolidayBooking(String isHolidayBooking) {
        this.isHolidayBooking = isHolidayBooking;
    }

    public Double getOutStandingAmount() {
        return outStandingAmount;
    }

    public void setOutStandingAmount(Double outStandingAmount) {
        this.outStandingAmount = outStandingAmount;
    }

    public SalesOffice getSalesOffice() {
        return salesOffice;
    }

    public void setSalesOffice(SalesOffice salesOffice) {
        this.salesOffice = salesOffice;
    }

    public String getInvoiceCurrency() {
        return invoiceCurrency;
    }

    public void setInvoiceCurrency(String invoiceCurrency) {
        this.invoiceCurrency = invoiceCurrency;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

}
