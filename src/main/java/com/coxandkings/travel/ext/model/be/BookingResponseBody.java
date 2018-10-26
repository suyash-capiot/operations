
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "clientID",
        "clientLanguage",
        "clientCurrency",
        "sessionID",
        "userID",
        "transactionID",
        "bookID",
        "products",
        "clientType",
        "IsHolidayResponseBody",
        "clientMarket",
        "ResponseBodyDate",
        "paymentInfo",
        "status"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingResponseBody implements Serializable
{

    @JsonProperty("clientID")
    private String clientID;
    @JsonProperty("clientLanguage")
    private String clientLanguage;
    @JsonProperty("clientCurrency")
    private String clientCurrency;
    @JsonProperty("sessionID")
    private String sessionID;
    @JsonProperty("userID")
    private String userID;
    @JsonProperty("transactionID")
    private String transactionID;
    @JsonProperty("bookID")
    private String bookID;
    @JsonProperty("products")
    private List<Product> products = new ArrayList<Product>();
    @JsonProperty("clientType")
    private String clientType;

    @JsonProperty("IsHolidayResponseBody")
    private boolean isHolidayResponseBody;

    @JsonProperty("clientMarket")
    private String clientMarket;
    @JsonProperty("ResponseBodyDate")
    private String ResponseBodyDate;
    @JsonProperty("paymentInfo")
    private List<PaymentInfo> paymentInfo = new ArrayList<PaymentInfo>();
    @JsonProperty("status")
    private String status;

    @JsonProperty("bookingDate")
    private String bookingDate;

    @JsonProperty("staffID")
    private String staffID;

    @JsonProperty("IsHolidayBooking")
    private boolean IsHolidayBooking;

    @JsonProperty("QCStatus")
    private String qcStatus;

    @JsonProperty("bookingType")
    private String bookingType;

    @JsonProperty("containsProducts")
    private List<String> containsProducts;


    private final static long serialVersionUID = -3089727563804417876L;

    /**
     * No args constructor for use in serialization
     *
     */
    public BookingResponseBody() {
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    @JsonProperty("clientID")
    public String getClientID() {
        return clientID;
    }

    @JsonProperty("clientID")
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    @JsonProperty("clientLanguage")
    public String getClientLanguage() {
        return clientLanguage;
    }

    @JsonProperty("clientLanguage")
    public void setClientLanguage(String clientLanguage) {
        this.clientLanguage = clientLanguage;
    }

    @JsonProperty("clientCurrency")
    public String getClientCurrency() {
        return clientCurrency;
    }

    @JsonProperty("clientCurrency")
    public void setClientCurrency(String clientCurrency) {
        this.clientCurrency = clientCurrency;
    }

    @JsonProperty("sessionID")
    public String getSessionID() {
        return sessionID;
    }

    @JsonProperty("sessionID")
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    @JsonProperty("userID")
    public String getUserID() {
        return userID;
    }

    @JsonProperty("userID")
    public void setUserID(String userID) {
        this.userID = userID;
    }

    @JsonProperty("transactionID")
    public String getTransactionID() {
        return transactionID;
    }

    @JsonProperty("transactionID")
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    @JsonProperty("bookID")
    public String getBookID() {
        return bookID;
    }

    @JsonProperty("bookID")
    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    @JsonProperty("products")
    public List<Product> getProducts() {
        return products;
    }

    @JsonProperty("products")
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @JsonProperty("clientType")
    public String getClientType() {
        return clientType;
    }

    @JsonProperty("clientType")
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @JsonProperty("IsHolidayResponseBody")
    public boolean getIsHolidayResponseBody() {
        return isHolidayResponseBody;
    }

    @JsonProperty("clientMarket")
    public String getClientMarket() {
        return clientMarket;
    }

    @JsonProperty("clientMarket")
    public void setClientMarket(String clientMarket) {
        this.clientMarket = clientMarket;
    }

    @JsonProperty("ResponseBodyDate")
    public String getResponseBodyDate() {
        return ResponseBodyDate;
    }

    @JsonProperty("ResponseBodyDate")
    public void setResponseBodyDate(String ResponseBodyDate) {
        this.ResponseBodyDate = ResponseBodyDate;
    }

    @JsonProperty("paymentInfo")
    public List<PaymentInfo> getPaymentInfo() {
        return paymentInfo;
    }

    @JsonProperty("paymentInfo")
    public void setPaymentInfo(List<PaymentInfo> paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    @JsonProperty("IsHolidayResponseBody")
    public void setIsHolidayResponseBody(boolean isHolidayResponseBody) {
        this.isHolidayResponseBody = isHolidayResponseBody;
    }

    public boolean isHolidayResponseBody() {
        return isHolidayResponseBody;
    }

    public void setHolidayResponseBody(boolean holidayResponseBody) {
        isHolidayResponseBody = holidayResponseBody;
    }

    public boolean getIsHolidayBooking() {
        return IsHolidayBooking;
    }

    public void setHolidayBooking(boolean holidayBooking) {
        IsHolidayBooking = holidayBooking;
    }

    public String getQcStatus() {
        return qcStatus;
    }

    public void setQcStatus(String qcStatus) {
        this.qcStatus = qcStatus;
    }

    public List<String> getContainsProducts() {
        return containsProducts;
    }

    public void setContainsProducts(List<String> containsProducts) {
        this.containsProducts = containsProducts;
    }
}
