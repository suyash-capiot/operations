package com.coxandkings.travel.operations.criteria.booking.becriteria;
public class ProductDetailsFilter {

    private String productCategoryId; // productCategory - Done

    private String productCategorySubTypeId; //productSubCategory - Done

    private String supplierName; // source supplier name - DONE

    private String travelFromDate; // travelFromDate - flights

    private String  travelToDate; //travelToDate - flights

    private String airlinePNR; // airlinePNR - Done

    private String gsdPnr; //GdsPNR - fights - Done

    private String ticketNumber; //ticketingpnr - flights - Done

    private String country; // countrycode - hotel - done

    private String city; // citycode - hotel - done

    private String SupplierReferenceNumber; // supplierreferenceid - hotel - done

    private String productName;

    private String airlineName;

    public String getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(String productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductCategorySubTypeId() {
        return productCategorySubTypeId;
    }

    public void setProductCategorySubTypeId(String productCategorySubTypeId) {
        this.productCategorySubTypeId = productCategorySubTypeId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getAirlinePNR() {
        return airlinePNR;
    }

    public void setAirlinePNR(String airlinePNR) {
        this.airlinePNR = airlinePNR;
    }

    public String getTravelFromDate() {
        return travelFromDate;
    }

    public void setTravelFromDate(String travelFromDate) {
        this.travelFromDate = travelFromDate;
    }

    public String getTravelToDate() {
        return travelToDate;
    }

    public void setTravelToDate(String travelToDate) {
        this.travelToDate = travelToDate;
    }

    public String getGsdPnr() {
        return gsdPnr;
    }

    public void setGsdPnr(String gsdPnr) {
        this.gsdPnr = gsdPnr;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
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

    public String getSupplierReferenceNumber() {
        return SupplierReferenceNumber;
    }

    public void setSupplierReferenceNumber(String supplierReferenceNumber) {
        SupplierReferenceNumber = supplierReferenceNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }
}
