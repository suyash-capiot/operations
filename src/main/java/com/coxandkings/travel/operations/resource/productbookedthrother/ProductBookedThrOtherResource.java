package com.coxandkings.travel.operations.resource.productbookedthrother;

import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;

public class ProductBookedThrOtherResource {

    private String id;
    private String productCategorySubTypeValue;
    private String bookingRefId;
    private String orderId;

    //For Flights
    private String airlineName;
    private String flightNo;
    private String terminalNo;


    //For Train
    private String trainName;
    private String trainNo;
    private String platformNo;

    //For Bus
    private String serviceProvider;
    private String pickupPointName;
    private String dropoffPointName;

    //For Accomodation
    private String accomodationName;
    private String address;
    private String postalCode;
    private String country;
    private String city;
    private String location;

    //From CommanAttributes
    private String fromCity;
    private String toCity;

    @JsonProperty(value = "arrivalDateTime")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime arrivalDateTime;

    @JsonProperty(value = "departureDateTime")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime departureDateTime;

    @JsonIgnoreProperties
    private String arrivalDate;
    @JsonIgnoreProperties
    private String arrivalTime;
    @JsonIgnoreProperties
    private String departureDate;
    @JsonIgnoreProperties
    private String departureTime;


    private boolean linkSent = false;


    public boolean getLinkSent() {
        return linkSent;
    }

    public void setLinkSent(boolean linkSent) {
        this.linkSent = linkSent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductCategorySubTypeValue() {
        return productCategorySubTypeValue;
    }

    public void setProductCategorySubTypeValue(String productCategorySubTypeValue) {
        this.productCategorySubTypeValue = productCategorySubTypeValue;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public String getPlatformNo() {
        return platformNo;
    }

    public void setPlatformNo(String platformNo) {
        this.platformNo = platformNo;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getPickupPointName() {
        return pickupPointName;
    }

    public void setPickupPointName(String pickupPointName) {
        this.pickupPointName = pickupPointName;
    }

    public String getDropoffPointName() {
        return dropoffPointName;
    }

    public void setDropoffPointName(String dropoffPointName) {
        this.dropoffPointName = dropoffPointName;
    }

    public String getAccomodationName() {
        return accomodationName;
    }

    public void setAccomodationName(String accomodationName) {
        this.accomodationName = accomodationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public ZonedDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(ZonedDateTime arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public ZonedDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(ZonedDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
