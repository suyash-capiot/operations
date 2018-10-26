package com.coxandkings.travel.operations.model.booking;

import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class FavProductDetails extends BaseModel {

    @Column(name = "fav_id")
    private String favId;

    @Column(name = "product_category_id")
    private String productCategoryId;

    @Column(name = "product_category_subType_id")
    private String productCategorySubTypeId;

    @Column(name = "travel_from_date")
    private Long travelFromDate;

    @Column(name = "travel_to_date")
    private Long travelToDate;

    @Column(name = "gsd_pnr")
    private String gsdPnr;

    @Column(name = "airline_name")
    private String airlineName;

    @Column(name = "suppler_name")
    private String suppierName;

    @Column(name = "airline_pnr")
    private String airlinePNR;

    @Column(name = "ticket_number")
    private String ticketNumber;

    @Column(name = "detaile_product_summary")
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
