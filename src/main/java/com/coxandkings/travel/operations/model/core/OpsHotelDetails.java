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
public class OpsHotelDetails implements Serializable {

    @JsonProperty("rooms")
    private List<OpsRoom> rooms = new ArrayList<OpsRoom>();

    @JsonProperty("countryCode")
    private String countryCode;

    @JsonProperty("cityCode")
    private String cityCode;

    @JsonProperty("hotelCode")
    private String hotelCode;

    @JsonProperty("hotelName")
    private String hotelName;

//    @JsonProperty("supplierRefNo")
//    private String supplierRefNo;

    @JsonProperty("orderSupplierPriceInfo")
    private OpsAccoOrderSupplierPriceInfo opsAccoOrderSupplierPriceInfo;

    @JsonProperty("orderClientTotalPriceInfo")
    private OpsAccommodationTotalPriceInfo opsAccommodationTotalPriceInfo;

    private String accommodationReferenceNumber;

    private String cityName;

    private String countryName;

    @JsonProperty("supplierReconfirmationDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime supplierReconfirmationDateZDT;

    @JsonProperty("clientReconfirmationDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime clientReconfirmationDateZDT;

    @JsonProperty("timeLimitExpiryDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime timeLimitExpiryDate;

    @JsonProperty("credentialsName")
    private String credentialsName;

    @JsonProperty("ticketingPCC")
    private String ticketingPCC;

    @JsonProperty("prePaymentRequest")
    private Boolean prePaymentRequest;

    @JsonProperty("firstReservationCheck")
    private boolean firstReservationCheck;

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

    public OpsHotelDetails() {
    }

    public boolean isFirstReservationCheck() {
        return firstReservationCheck;
    }

    public void setFirstReservationCheck(boolean firstReservationCheck) {
        this.firstReservationCheck = firstReservationCheck;
    }

    public String getTicketingPCC() {
        return ticketingPCC;
    }

    public void setTicketingPCC(String ticketingPCC) {
        this.ticketingPCC = ticketingPCC;
    }

    public String getCredentialsName() {
        return credentialsName;
    }

    public void setCredentialsName(String credentialsName) {
        this.credentialsName = credentialsName;
    }

    public List<OpsRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<OpsRoom> rooms) {
        this.rooms = rooms;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }


/*
    // Following code is not used; use from OpsProduct; kept it for reference
    public String getSupplierRefNo() {
        return supplierRefNo;
    }

    public void setSupplierRefNo(String supplierRefNo) {
        this.supplierRefNo = supplierRefNo;
    }
*/

    public OpsAccoOrderSupplierPriceInfo getOpsAccoOrderSupplierPriceInfo() {
        return opsAccoOrderSupplierPriceInfo;
    }

    public void setOpsAccoOrderSupplierPriceInfo(OpsAccoOrderSupplierPriceInfo opsAccoOrderSupplierPriceInfo) {
        this.opsAccoOrderSupplierPriceInfo = opsAccoOrderSupplierPriceInfo;
    }

    public OpsAccommodationTotalPriceInfo getOpsAccommodationTotalPriceInfo() {
        return opsAccommodationTotalPriceInfo;
    }

    public void setOpsAccommodationTotalPriceInfo(OpsAccommodationTotalPriceInfo opsAccommodationTotalPriceInfo) {
        this.opsAccommodationTotalPriceInfo = opsAccommodationTotalPriceInfo;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getAccommodationReferenceNumber() {
        return accommodationReferenceNumber;
    }

    public void setAccommodationReferenceNumber(String accommodationReferenceNumber) {
        this.accommodationReferenceNumber = accommodationReferenceNumber;
    }

    public ZonedDateTime getSupplierReconfirmationDateZDT() {
        return supplierReconfirmationDateZDT;
    }

    public void setSupplierReconfirmationDateZDT(ZonedDateTime supplierReconfirmationDateZDT) {
        this.supplierReconfirmationDateZDT = supplierReconfirmationDateZDT;
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

    public Boolean getPrePaymentRequest() {
        return prePaymentRequest;
    }

    public void setPrePaymentRequest(Boolean prePaymentRequest) {
        this.prePaymentRequest = prePaymentRequest;
    }
}
