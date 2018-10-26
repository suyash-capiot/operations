package com.coxandkings.travel.operations.criteria.forex;

import com.coxandkings.travel.operations.criteria.BaseCriteria;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.coxandkings.travel.operations.resource.forex.PassengerNameResource;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;

public class ForexCriteria extends BaseCriteria {


    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime fromDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime toDate;

    private String reqOrIndentId;
    private String bookRefNo;
    private String travelCountry;
    private String currency;
    private String supplierName;

    private PassengerNameResource passengerName;
    private String clientName;

    private String indentStatus;
    private String requestStatus;
    private String disbursementStatus;

    public ZonedDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(ZonedDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public ZonedDateTime getToDate() {
        return toDate;
    }

    public void setToDate(ZonedDateTime toDate) {
        this.toDate = toDate;
    }

    public String getReqOrIndentId() {
        return reqOrIndentId;
    }

    public void setReqOrIndentId(String reqOrIndentId) {
        this.reqOrIndentId = reqOrIndentId;
    }

    public String getBookRefNo() {
        return bookRefNo;
    }

    public void setBookRefNo(String bookRefNo) {
        this.bookRefNo = bookRefNo;
    }

    public String getTravelCountry() {
        return travelCountry;
    }

    public void setTravelCountry(String travelCountry) {
        this.travelCountry = travelCountry;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public PassengerNameResource getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(PassengerNameResource passengerName) {
        this.passengerName = passengerName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getIndentStatus() {
        return indentStatus;
    }

    public void setIndentStatus(String indentStatus) {
        this.indentStatus = indentStatus;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getDisbursementStatus() {
        return disbursementStatus;
    }

    public void setDisbursementStatus(String disbursementStatus) {
        this.disbursementStatus = disbursementStatus;
    }
}
