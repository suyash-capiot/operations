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
public class OpsFlightDetails implements Serializable {

    @JsonProperty("airlinePNR")
    private String airlinePNR;

    @JsonProperty("orderSupplierPriceInfo")
    private OpsFlightSupplierPriceInfo opsFlightSupplierPriceInfo;

    @JsonProperty("orderClientTotalPriceInfo")
    private OpsFlightTotalPriceInfo totalPriceInfo;

    @JsonProperty("ticketNumber")
    private String ticketNumber;

    @JsonProperty("tripIndicator")
    private String tripIndicator;

    @JsonProperty("tripType")
    private String tripType;

    @JsonProperty("GDSPNR")
    private String gDSPNR;

    @JsonProperty("ticketingPCC")
    private String ticketingPCC;

    @JsonProperty("bookingPCC")
    private String bookingPCC;

    @JsonProperty("originDestinationOptions")
    private List<OpsOriginDestinationOption> originDestinationOptions = new ArrayList<OpsOriginDestinationOption>();

    @JsonProperty("paxInfo")
    private List<OpsFlightPaxInfo> paxInfo = new ArrayList<OpsFlightPaxInfo>();

    @JsonProperty("ticketPNR")
    private String ticketPNR;

    //TODO : Booking2 Engine not giving these params
    @JsonProperty("supplierReconfirmationDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime supplierReconfirmationDateZDT;

    @JsonProperty("clientReconfirmationDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime clientReconfirmationDateZDT;

    @JsonProperty("clientreconfirmationStatus")
    private String clientreconfirmationStatus;

    @JsonProperty("suppReconfirmationStatus")
    private String suppReconfirmationStatus;

    public String getClientreconfirmationStatus() {
        return clientreconfirmationStatus;
    }

    public void setClientreconfirmationStatus(String clientreconfirmationStatus) {
        this.clientreconfirmationStatus = clientreconfirmationStatus;
    }

    public String getSuppReconfirmationStatus() {
        return suppReconfirmationStatus;
    }

    public void setSuppReconfirmationStatus(String suppReconfirmationStatus) {
        this.suppReconfirmationStatus = suppReconfirmationStatus;
    }

    @JsonProperty("timeLimitExpiryDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime timeLimitExpiryDate;

    @JsonProperty("ticketIssueDate")
    private String ticketIssueDate;

    @JsonProperty("credentialsName")
    private String credentialsName;

    @JsonProperty("prePaymentRequest")
    private boolean prePaymentRequest;

    @JsonProperty("firstReservationCheck")
    private boolean firstReservationCheck;

    public OpsFlightDetails() {
    }

    public boolean isPrePaymentRequest() {
        return prePaymentRequest;
    }

    public boolean isFirstReservationCheck() {
        return firstReservationCheck;
    }

    public void setFirstReservationCheck(boolean firstReservationCheck) {
        this.firstReservationCheck = firstReservationCheck;
    }

    public String getCredentialsName() {
        return credentialsName;
    }

    public void setCredentialsName(String credentialsName) {
        this.credentialsName = credentialsName;
    }

    public OpsFlightSupplierPriceInfo getOpsFlightSupplierPriceInfo() {
        return opsFlightSupplierPriceInfo;
    }

    public void setOpsFlightSupplierPriceInfo(OpsFlightSupplierPriceInfo opsFlightSupplierPriceInfo) {
        this.opsFlightSupplierPriceInfo = opsFlightSupplierPriceInfo;
    }

    public OpsFlightTotalPriceInfo getTotalPriceInfo() {
        return totalPriceInfo;
    }

    public void setTotalPriceInfo(OpsFlightTotalPriceInfo totalPriceInfo) {
        this.totalPriceInfo = totalPriceInfo;
    }

    public String getAirlinePNR() {
        return airlinePNR;
    }

    public void setAirlinePNR(String airlinePNR) {
        this.airlinePNR = airlinePNR;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getTripIndicator() {
        return tripIndicator;
    }

    public void setTripIndicator(String tripIndicator) {
        this.tripIndicator = tripIndicator;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getgDSPNR() {
        return gDSPNR;
    }

    public void setgDSPNR(String gDSPNR) {
        this.gDSPNR = gDSPNR;
    }

    public String getTicketingPCC() {
        return ticketingPCC;
    }

    public void setTicketingPCC(String ticketingPCC) {
        this.ticketingPCC = ticketingPCC;
    }

    public List<OpsOriginDestinationOption> getOriginDestinationOptions() {
        return originDestinationOptions;
    }

    public void setOriginDestinationOptions(List<OpsOriginDestinationOption> originDestinationOptions) {
        this.originDestinationOptions = originDestinationOptions;
    }

    public List<OpsFlightPaxInfo> getPaxInfo() {
        return paxInfo;
    }

    public void setPaxInfo(List<OpsFlightPaxInfo> paxInfo) {
        this.paxInfo = paxInfo;
    }

    public ZonedDateTime getClientReconfirmationDateZDT() {
        return clientReconfirmationDateZDT;
    }

    public void setClientReconfirmationDateZDT(ZonedDateTime clientReconfirmationDateZDT) {
        this.clientReconfirmationDateZDT = clientReconfirmationDateZDT;
    }

    public ZonedDateTime getTimeLimitExpiryDate() {
        return timeLimitExpiryDate;
    }

    public void setTimeLimitExpiryDate(ZonedDateTime timeLimitExpiryDate) {
        this.timeLimitExpiryDate = timeLimitExpiryDate;
    }

    public ZonedDateTime getSupplierReconfirmationDateZDT() {
        return supplierReconfirmationDateZDT;
    }

    public void setSupplierReconfirmationDateZDT(ZonedDateTime supplierReconfirmationDateZDT) {
        this.supplierReconfirmationDateZDT = supplierReconfirmationDateZDT;
    }

    public String getTicketPNR() {
        return ticketPNR;
    }

    public void setTicketPNR(String ticketPNR) {
        this.ticketPNR = ticketPNR;
    }

    public String getTicketIssueDate() {
        return ticketIssueDate;
    }

    public void setTicketIssueDate(String ticketIssueDate) {
        this.ticketIssueDate = ticketIssueDate;
    }

    public String getBookingPCC() {
        return bookingPCC;
    }

    public void setBookingPCC(String bookingPCC) {
        this.bookingPCC = bookingPCC;
    }

    public boolean getPrePaymentRequest() {
        return prePaymentRequest;
    }

    public void setPrePaymentRequest(boolean prePaymentRequest) {
        this.prePaymentRequest = prePaymentRequest;
    }

}
