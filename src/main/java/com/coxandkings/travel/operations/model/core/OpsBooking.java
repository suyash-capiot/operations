package com.coxandkings.travel.operations.model.core;

import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsBooking implements Serializable {

    @JsonProperty("clientID")
    private String clientID;

    @JsonProperty("clientLanguage")
    private String clientLanguage;

    @JsonProperty("clientCurrency")
    private String clientCurrency;

    @JsonProperty("clientIATANumber")
    private String clientIATANumber;

    @JsonProperty("sessionID")
    private String sessionID;

    @JsonProperty("userID")
    private String userID;

    @JsonProperty("transactionID")
    private String transactionID;

    @JsonProperty("bookID")
    private String bookID;

    @JsonProperty("products")
    private List<OpsProduct> products = new ArrayList<OpsProduct>();

    @JsonProperty("clientType")
    private String clientType;

    @JsonProperty("IsHolidayBooking")
    private boolean isHolidayBooking;

    @JsonProperty("clientMarket")
    private String clientMarket;

    @JsonProperty("pointOfSale")
    private String pointOfSale;

    @JsonProperty("bookingDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime bookingDateZDT;


    @JsonProperty("paymentInfo")
    private List<OpsPaymentInfo> paymentInfo = new ArrayList<OpsPaymentInfo>();

    @JsonProperty("bookingStatus")
    private OpsBookingStatus status;

    @JsonProperty("companyId")
    private String companyId;

    @JsonProperty("staffId")
    private String staffId;

    @JsonProperty("QCStatus")
    private String qcStatus;

    @JsonProperty("sbu")
    private String sbu;

    @JsonProperty("bu")
    private String bu;

    @JsonProperty("groupCompanyID")
    private String groupCompanyID;

    @JsonProperty("groupOfCompaniesID")
    private String groupOfCompaniesID;

    @JsonProperty("clientCategory")
    private String clientCategory;

    @JsonProperty("clientSubCategory")
    private String clientSubCategory;

    @JsonProperty("companyMarket")
    private String companyMarket;

    @JsonProperty("bookingType")
    private String bookingType;

    @JsonProperty("containsProducts")
    private List<String> containsProducts;

    public OpsBooking() {
    }

    public List<String> getContainsProducts() {
        return containsProducts;
    }

    public void setContainsProducts(List<String> containsProducts) {
        this.containsProducts = containsProducts;
    }


    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getClientCategory() {
        return clientCategory;
    }

    public void setClientCategory(String clientCategory) {
        this.clientCategory = clientCategory;
    }

    public String getClientSubCategory() {
        return clientSubCategory;
    }

    public void setClientSubCategory(String clientSubCategory) {
        this.clientSubCategory = clientSubCategory;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public boolean isHolidayBooking() {
        return isHolidayBooking;
    }

    public void setHolidayBooking(boolean holidayBooking) {
        isHolidayBooking = holidayBooking;
    }

    public String getClientCurrency() {
        return clientCurrency;
    }

    public void setClientCurrency(String clientCurrency) {
        this.clientCurrency = clientCurrency;
    }

    public String getClientIATANumber() {
        return clientIATANumber;
    }

    public void setClientIATANumber(String clientIATANumber) {
        this.clientIATANumber = clientIATANumber;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public List<OpsPaymentInfo> getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(List<OpsPaymentInfo> paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public List<OpsProduct> getProducts() {
        return products;
    }

    public void setProducts(List<OpsProduct> products) {
        this.products = products;
    }

    public OpsBookingStatus getStatus() {
        return status;
    }

    public void setStatus(OpsBookingStatus status) {
        this.status = status;
    }

    public String getPointOfSale() {
        return pointOfSale;
    }

    public void setPointOfSale(String pointOfSale) {
        this.pointOfSale = pointOfSale;
    }


    public String getClientLanguage() {
        return clientLanguage;
    }

    public void setClientLanguage(String clientLanguage) {
        this.clientLanguage = clientLanguage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public String getClientMarket() {
        return clientMarket;
    }

    public void setClientMarket(String clientMarket) {
        this.clientMarket = clientMarket;
    }

    public ZonedDateTime getBookingDateZDT() {
        return bookingDateZDT;
    }

    public void setBookingDateZDT(ZonedDateTime bookingDateZDT) {
        this.bookingDateZDT = bookingDateZDT;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getQcStatus() {
        return qcStatus;
    }

    public void setQcStatus(String qcStatus) {
        this.qcStatus = qcStatus;
    }

    public String getSbu() {
        return sbu;
    }

    public void setSbu(String sbu) {
        this.sbu = sbu;
    }

    public String getBu() {
        return bu;
    }

    public void setBu(String bu) {
        this.bu = bu;
    }

    public String getGroupCompanyID() {
        return groupCompanyID;
    }

    public void setGroupCompanyID(String groupCompanyID) {
        this.groupCompanyID = groupCompanyID;
    }

    public String getGroupOfCompaniesID() {
        return groupOfCompaniesID;
    }

    public void setGroupOfCompaniesID(String groupOfCompaniesID) {
        this.groupOfCompaniesID = groupOfCompaniesID;
    }

    public String getCompanyMarket() {
        return companyMarket;
    }

    public void setCompanyMarket(String companyMarket) {
        this.companyMarket = companyMarket;
    }
}
