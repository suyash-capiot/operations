package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpecialRequestInfo implements Serializable {

    @JsonProperty("date")
    private String date;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("flightRefNumberRphList")
    private String flightRefNumberRphList;
    @JsonProperty("serviceQuantity")
    private String serviceQuantity;
    @JsonProperty("destinationLocation")
    private String destinationLocation;
    @JsonProperty("type")
    private String type;
    @JsonProperty("originLocation")
    private String originLocation;

    @JsonProperty("ssrCode")
    private String ssrCode;
    @JsonProperty("flightNumber")
    private String flightNumber;
    @JsonProperty("number")
    private String number;
    @JsonProperty("servicePrice")
    private ServicePrice servicePrice;

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("status")
    private String status;

    @JsonProperty("categoryCode")
    private String categoryCode;

    @JsonProperty("description")
    private String description;

    @JsonProperty("airlineCode")
    private String airlineCode;

    @JsonProperty("companyShortName")
    private String companyShortName;

    @JsonProperty("taxes")
    private SpecialServiceTaxes taxes;

    private final static long serialVersionUID = -6689402931569299025L;

    /**
     * No args constructor for use in serialization
     *
     */
    public SpecialRequestInfo() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFlightRefNumberRphList() {
        return flightRefNumberRphList;
    }

    public void setFlightRefNumberRphList(String flightRefNumberRphList) {
        this.flightRefNumberRphList = flightRefNumberRphList;
    }

    public String getServiceQuantity() {
        return serviceQuantity;
    }

    public void setServiceQuantity(String serviceQuantity) {
        this.serviceQuantity = serviceQuantity;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(String originLocation) {
        this.originLocation = originLocation;
    }

    public String getSsrCode() {
        return ssrCode;
    }

    public void setSsrCode(String ssrCode) {
        this.ssrCode = ssrCode;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ServicePrice getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(ServicePrice servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName;
    }

    public SpecialServiceTaxes getTaxes() {
        return taxes;
    }

    public void setTaxes(SpecialServiceTaxes taxes) {
        this.taxes = taxes;
    }
}
