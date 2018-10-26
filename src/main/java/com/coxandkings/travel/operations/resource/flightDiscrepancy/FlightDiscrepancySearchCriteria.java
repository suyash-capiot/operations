package com.coxandkings.travel.operations.resource.flightDiscrepancy;

import java.util.List;

public class FlightDiscrepancySearchCriteria {
    private String dateTo;
    private String dateFrom;
    private String bookingRefNum;
    private String transactionType;
    private String airlineCode;
    private String type;
    private String company;
    private String iata;
    private String bspReferenceNumber;
    private String discrepancyRecordId;
    private Integer page;
    private Integer size;
    private String filterByName;
    private List<String> filterByValues;
    private String supplierName;

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public List<String> getFilterByValues() {
        return filterByValues;
    }

    public void setFilterByValues(List<String> filterByValues) {
        this.filterByValues = filterByValues;
    }

    public String getDateTo() { return dateTo; }

    public void setDateTo(String dateTo) { this.dateTo = dateTo; }

    public String getDateFrom() { return dateFrom; }

    public void setDateFrom(String dateFrom) { this.dateFrom = dateFrom; }

    public String getBookingRefNum() {
        return bookingRefNum;
    }

    public void setBookingRefNum(String bookingRefNum) {
        this.bookingRefNum = bookingRefNum;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public String getBspReferenceNumber() {
        return bspReferenceNumber;
    }

    public void setBspReferenceNumber(String bspReferenceNumber) {
        this.bspReferenceNumber = bspReferenceNumber;
    }

    public String getDiscrepancyRecordId() {
        return discrepancyRecordId;
    }

    public void setDiscrepancyRecordId(String discrepancyRecordId) {
        this.discrepancyRecordId = discrepancyRecordId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilterByName() {
        return filterByName;
    }

    public void setFilterByName(String filterByName) {
        this.filterByName = filterByName;
    }
}
