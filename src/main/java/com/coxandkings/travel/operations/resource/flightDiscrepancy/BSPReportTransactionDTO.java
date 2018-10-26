package com.coxandkings.travel.operations.resource.flightDiscrepancy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BSPReportTransactionDTO {
    private String id;
   private String iataNumber;
   private String airlineCode;
   private String airlineName;
   private String transactionType;
   private String ticketNumber;
   private String supplierName;
   private String issueDate;
   private String invoiceNumber;
   private BigDecimal ticketFare;
   private BigDecimal tax;
   private BigDecimal bspCommission;
   private BigDecimal totalCommission;
   private BigDecimal baseFare;
   private BigDecimal yq;
   private String source;
   private String raNumber;
   private String destination;
   private String xxiAmt;
   private String orAmt;

    public String getXxiAmt() {
        return xxiAmt;
    }

    public void setXxiAmt(String xxiAmt) {
        this.xxiAmt = xxiAmt;
    }

    public String getOrAmt() {
        return orAmt;
    }

    public void setOrAmt(String orAmt) {
        this.orAmt = orAmt;
    }

    public String getRaNumber() {
        return raNumber;
    }

    public void setRaNumber(String raNumber) {
        this.raNumber = raNumber;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIataNumber() {
        return iataNumber;
    }

    public void setIataNumber(String iataNumber) {
        this.iataNumber = iataNumber;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getTicketFare() {
        return ticketFare;
    }

    public void setTicketFare(BigDecimal ticketFare) {
        this.ticketFare = ticketFare;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getBspCommission() {
        return bspCommission;
    }

    public void setBspCommission(BigDecimal bspCommission) {
        this.bspCommission = bspCommission;
    }

    public BigDecimal getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(BigDecimal totalCommission) {
        this.totalCommission = totalCommission;
    }

    public BigDecimal getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(BigDecimal baseFare) {
        this.baseFare = baseFare;
    }

    public BigDecimal getYq() {
        return yq;
    }

    public void setYq(BigDecimal yq) {
        this.yq = yq;
    }

    public String getAirlineName() { return airlineName; }

    public void setAirlineName(String airlineName) { this.airlineName = airlineName; }
}
