package com.coxandkings.travel.operations.resource.booking;

import com.coxandkings.travel.operations.resource.BaseResource;

public class FavProductDetailsResource extends BaseResource {

    private String favId;
    private String productCategoryId;
    private String productCategorySubTypeId;
    private Long travelFromDate;
    private Long travelToDate;
    private String gsdPnr;
    private String airlineName;
    private String suppierName;
    private String airlinePNR;
    private String ticketNumber;
    private String detailProductSummary;

    public String getFavId() {
        return favId;
    }

    public void setFavId(String favId) {
        this.favId = favId;
    }

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

    public Long getTravelFromDate() {
        return travelFromDate;
    }

    public void setTravelFromDate(Long travelFromDate) {
        this.travelFromDate = travelFromDate;
    }

    public Long getTravelToDate() {
        return travelToDate;
    }

    public void setTravelToDate(Long travelToDate) {
        this.travelToDate = travelToDate;
    }

    public String getGsdPnr() {
        return gsdPnr;
    }

    public void setGsdPnr(String gsdPnr) {
        this.gsdPnr = gsdPnr;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getSuppierName() {
        return suppierName;
    }

    public void setSuppierName(String suppierName) {
        this.suppierName = suppierName;
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

    public String getDetailProductSummary() {
        return detailProductSummary;
    }

    public void setDetailProductSummary(String detailProductSummary) {
        this.detailProductSummary = detailProductSummary;
    }
}
