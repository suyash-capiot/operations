package com.coxandkings.travel.operations.resource.manageofflinebooking;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "date",
    "amount",
    "flightRefNumberRphList",
    "serviceQuantity",
    "destinationLocation",
    "type",
    "originLocation",
    "ssrCode",
    "flightNumber",
    "number",
    "servicePrice",
    "currencyCode",
    "status",
    "categoryCode",
    "description",
    "airlineCode",
    "companyShortName",
    "taxes"
})
public class SpecialRequestInfo {

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
    private Object categoryCode;
    @JsonProperty("description")
    private Object description;
    @JsonProperty("airlineCode")
    private Object airlineCode;
    @JsonProperty("companyShortName")
    private Object companyShortName;
    @JsonProperty("taxes")
    private Object taxes;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("flightRefNumberRphList")
    public String getFlightRefNumberRphList() {
        return flightRefNumberRphList;
    }

    @JsonProperty("flightRefNumberRphList")
    public void setFlightRefNumberRphList(String flightRefNumberRphList) {
        this.flightRefNumberRphList = flightRefNumberRphList;
    }

    @JsonProperty("serviceQuantity")
    public String getServiceQuantity() {
        return serviceQuantity;
    }

    @JsonProperty("serviceQuantity")
    public void setServiceQuantity(String serviceQuantity) {
        this.serviceQuantity = serviceQuantity;
    }

    @JsonProperty("destinationLocation")
    public String getDestinationLocation() {
        return destinationLocation;
    }

    @JsonProperty("destinationLocation")
    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("originLocation")
    public String getOriginLocation() {
        return originLocation;
    }

    @JsonProperty("originLocation")
    public void setOriginLocation(String originLocation) {
        this.originLocation = originLocation;
    }

    @JsonProperty("ssrCode")
    public String getSsrCode() {
        return ssrCode;
    }

    @JsonProperty("ssrCode")
    public void setSsrCode(String ssrCode) {
        this.ssrCode = ssrCode;
    }

    @JsonProperty("flightNumber")
    public String getFlightNumber() {
        return flightNumber;
    }

    @JsonProperty("flightNumber")
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    @JsonProperty("number")
    public String getNumber() {
        return number;
    }

    @JsonProperty("number")
    public void setNumber(String number) {
        this.number = number;
    }

    @JsonProperty("servicePrice")
    public ServicePrice getServicePrice() {
        return servicePrice;
    }

    @JsonProperty("servicePrice")
    public void setServicePrice(ServicePrice servicePrice) {
        this.servicePrice = servicePrice;
    }

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("categoryCode")
    public Object getCategoryCode() {
        return categoryCode;
    }

    @JsonProperty("categoryCode")
    public void setCategoryCode(Object categoryCode) {
        this.categoryCode = categoryCode;
    }

    @JsonProperty("description")
    public Object getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(Object description) {
        this.description = description;
    }

    @JsonProperty("airlineCode")
    public Object getAirlineCode() {
        return airlineCode;
    }

    @JsonProperty("airlineCode")
    public void setAirlineCode(Object airlineCode) {
        this.airlineCode = airlineCode;
    }

    @JsonProperty("companyShortName")
    public Object getCompanyShortName() {
        return companyShortName;
    }

    @JsonProperty("companyShortName")
    public void setCompanyShortName(Object companyShortName) {
        this.companyShortName = companyShortName;
    }

    @JsonProperty("taxes")
    public Object getTaxes() {
        return taxes;
    }

    @JsonProperty("taxes")
    public void setTaxes(Object taxes) {
        this.taxes = taxes;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
