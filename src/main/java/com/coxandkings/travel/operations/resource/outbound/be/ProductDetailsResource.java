package com.coxandkings.travel.operations.resource.outbound.be;

public class ProductDetailsResource {
//modide
    private  String productCategoryId;
    private  String productCategorySubTypeId;
    private  Long travelFromDate;
    private  String airlineName;
    private  String supplierName;
    private  String gsdPnr;
    private  Long travelToDate;
    private  String airlinePNR;
    private  String ticketNumber;

    public String getProductCategoryId( ) {
        return productCategoryId;
    }

    public void setProductCategoryId( String productCategoryId ) {
        this.productCategoryId = productCategoryId;
    }

    public String getAirlineName( ) {
        return airlineName;
    }

    public void setAirlineName( String airlineName ) {
        this.airlineName = airlineName;
    }

    public String getProductCategorySubTypeId( ) {
        return productCategorySubTypeId;
    }

    public void setProductCategorySubTypeId( String productCategorySubTypeId ) {
        this.productCategorySubTypeId = productCategorySubTypeId;
    }

    public String getSupplierName( ) {
        return supplierName;
    }

    public void setSupplierName( String supplierName ) {
        this.supplierName = supplierName;
    }

    public Long getTravelFromDate( ) {
        return travelFromDate;
    }

    public void setTravelFromDate( Long travelFromDate ) {
        this.travelFromDate = travelFromDate;
    }

    public String getGsdPnr( ) {
        return gsdPnr;
    }

    public void setGsdPnr( String gsdPnr ) {
        this.gsdPnr = gsdPnr;
    }

    public Long getTravelToDate( ) {
        return travelToDate;
    }

    public void setTravelToDate( Long travelToDate ) {
        this.travelToDate = travelToDate;
    }

    public String getAirlinePNR( ) {
        return airlinePNR;
    }

    public void setAirlinePNR( String airlinePNR ) {
        this.airlinePNR = airlinePNR;
    }

    public String getTicketNumber( ) {
        return ticketNumber;
    }

    public void setTicketNumber( String ticketNumber ) {
        this.ticketNumber = ticketNumber;
    }
}
