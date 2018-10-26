package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingResponseHeader implements Serializable {

    @JsonProperty("clientContext")
    private BookingClientContext clientContext;

    @JsonProperty("sessionID")
    private String sessionID;

    @JsonProperty("userID")
    private String userID;

    @JsonProperty("transactionID")
    private String transactionID;

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

    @JsonProperty("companyMarket")
    private String companyMarket;

    public String getCompanyMarket() {
        return companyMarket;
    }

    public void setCompanyMarket(String companyMarket) {
        this.companyMarket = companyMarket;
    }

    public BookingClientContext getClientContext() {
        return clientContext;
    }

    public void setClientContext(BookingClientContext clientContext) {
        this.clientContext = clientContext;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }
}
