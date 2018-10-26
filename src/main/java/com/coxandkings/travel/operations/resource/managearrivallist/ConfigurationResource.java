package com.coxandkings.travel.operations.resource.managearrivallist;

import java.time.Duration;

// To GET data from MDM

public class ConfigurationResource
{
    //    private String nameOfList;
//    private String generatedBy;
    private Duration cutOffGeneration;
    private String productCategory;
    private String productCategorySubType;
    private String supplierName;
    private String clientType;
    private String clientGroup;
    private String clientName;
    private Boolean sendToSupplier;
    private String bookingDate;


    //Flight
    private String airlineName;
    private String fromCity;
    private String toCity;
    private String journeyType;


    //Acco
    private String continent;
    private String country;
    private String city;
    private String productName;
    private String chain;
    private String isMysteryProduct;





    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductCategorySubType() {
        return productCategorySubType;
    }

    public void setProductCategorySubType(String productCategorySubType) {
        this.productCategorySubType = productCategorySubType;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientGroup() {
        return clientGroup;
    }

    public void setClientGroup(String clientGroup) {
        this.clientGroup = clientGroup;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Boolean getSendToSupplier() {
        return sendToSupplier;
    }

    public void setSendToSupplier(Boolean sendToSupplier) {
        this.sendToSupplier = sendToSupplier;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public String getIsMysteryProduct() {
        return isMysteryProduct;
    }

    public void setIsMysteryProduct(String isMysteryProduct) {
        this.isMysteryProduct = isMysteryProduct;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
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

    public String getJourneyType() {
        return journeyType;
    }

    public void setJourneyType(String journeyType) {
        this.journeyType = journeyType;
    }

    public Duration getCutOffGeneration() {
        return cutOffGeneration;
    }

    public void setCutOffGeneration(Duration cutOffGeneration) {
        this.cutOffGeneration = cutOffGeneration;
    }


}
