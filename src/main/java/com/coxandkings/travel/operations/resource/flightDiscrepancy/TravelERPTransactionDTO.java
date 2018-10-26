package com.coxandkings.travel.operations.resource.flightDiscrepancy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TravelERPTransactionDTO {
    private String id;
    private String airlineCode;
    private String airlineName;
    private String transactionType;
    private String date;
    private String ticketNumber;
    private String source;
    private String destination;
    private BigDecimal baseFare;
    private BigDecimal tax;
    private BigDecimal ticketFare;
    private BigDecimal bspCommission;
    private BigDecimal totalCommission;
    private String bspIata;
    private String raNumber;
    private BigDecimal xxlAmount;
    private BigDecimal oramount;
    private BigDecimal yq;
    private String orgTicketNumber;
    private String bookingRefNum;
    private Set<String> creditDebitNoteNumber;
    private String fsoNumber;
    private String xxiAmount;
    private String orAmt;

    public String getXxiAmount() {
        return xxiAmount;
    }

    public void setXxiAmount(String xxiAmount) {
        this.xxiAmount = xxiAmount;
    }

    public String getAirlineName() { return airlineName; }

    public void setAirlineName(String airlineName) { this.airlineName = airlineName; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public BigDecimal getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(BigDecimal baseFare) {
        this.baseFare = baseFare;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTicketFare() {
        return ticketFare;
    }

    public void setTicketFare(BigDecimal ticketFare) {
        this.ticketFare = ticketFare;
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

    public String getBspIata() {
        return bspIata;
    }

    public void setBspIata(String bspIata) {
        this.bspIata = bspIata;
    }

    public String getRaNumber() {
        return raNumber;
    }

    public void setRaNumber(String raNumber) {
        this.raNumber = raNumber;
    }

    public String getOrgTicketNumber() {
        return orgTicketNumber;
    }

    public void setOrgTicketNumber(String orgTicketNumber) {
        this.orgTicketNumber = orgTicketNumber;
    }

    public String getBookingRefNum() {
        return bookingRefNum;
    }

    public void setBookingRefNum(String bookingRefNum) {
        this.bookingRefNum = bookingRefNum;
    }

    public Set<String> getCreditDebitNoteNumber() {
        return creditDebitNoteNumber;
    }

    public void setCreditDebitNoteNumber(Set<String> creditDebitNoteNumber) {
        this.creditDebitNoteNumber = creditDebitNoteNumber;
    }

    public BigDecimal getXxlAmount() {
        return xxlAmount;
    }

    public void setXxlAmount(BigDecimal xxlAmount) {
        this.xxlAmount = xxlAmount;
    }

    public BigDecimal getOramount() {
        return oramount;
    }

    public void setOramount(BigDecimal oramount) {
        this.oramount = oramount;
    }

    public BigDecimal getYq() {
        return yq;
    }

    public void setYq(BigDecimal yq) {
        this.yq = yq;
    }

    public String getFsoNumber() {
        return fsoNumber;
    }

    public void setFsoNumber(String fsoNumber) {
        this.fsoNumber = fsoNumber;
    }
}
