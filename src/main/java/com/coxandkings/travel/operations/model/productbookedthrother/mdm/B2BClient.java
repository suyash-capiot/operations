package com.coxandkings.travel.operations.model.productbookedthrother.mdm;

import java.util.List;

public class B2BClient {
    private String clientID;
    private String clientName;
    private String clientGroup;
    private String status;
    private String clientMarket;
    private String clientCategory;
    private String clientSubCategory;
    private String language;
    private String companyId;
    private String SBU;
    private String BU;
    private List<String> email;
    private String firstName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClientMarket() {
        return clientMarket;
    }

    public void setClientMarket(String clientMarket) {
        this.clientMarket = clientMarket;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getSBU() {
        return SBU;
    }

    public void setSBU(String SBU) {
        this.SBU = SBU;
    }

    public String getBU() {
        return BU;
    }

    public void setBU(String BU) {
        this.BU = BU;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public String getClientGroup() {
        return clientGroup;
    }

    public void setClientGroup(String clientGroup) {
        this.clientGroup = clientGroup;
    }
}
