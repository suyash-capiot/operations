package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingClientContext implements Serializable {

    @JsonProperty("clientID")
    private String clientID;

    @JsonProperty("clientType")
    private String clientType;

    @JsonProperty("clientLanguage")
    private String clientLanguage;

    @JsonProperty("clientMarket")
    private String clientMarket;

    @JsonProperty("clientCurrency")
    private String clientCurrency;

    @JsonProperty("pointOfSale")
    private String pointOfSale;

    @JsonProperty("company")
    private String company;

    @JsonProperty("sbu")
    private String sbu;

    @JsonProperty("bu")
    private String bu;

    @JsonProperty("groupCompanyID")
    private String groupCompanyID;

    @JsonProperty("groupOfCompaniesID")
    private String groupOfCompaniesID;

    @JsonProperty("companyID")
    private String companyID;

    @JsonProperty("clientCategory")
    private String clientCategory;

    @JsonProperty("clientSubCategory")
    private String clientSubCategory;

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

    public String getClientLanguage() {
        return clientLanguage;
    }

    public void setClientLanguage(String clientLanguage) {
        this.clientLanguage = clientLanguage;
    }

    public String getClientMarket() {
        return clientMarket;
    }

    public void setClientMarket(String clientMarket) {
        this.clientMarket = clientMarket;
    }

    public String getClientCurrency() {
        return clientCurrency;
    }

    public void setClientCurrency(String clientCurrency) {
        this.clientCurrency = clientCurrency;
    }

    public String getPointOfSale() {
        return pointOfSale;
    }

    public void setPointOfSale(String pointOfSale) {
        this.pointOfSale = pointOfSale;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

}
