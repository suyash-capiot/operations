package com.coxandkings.travel.operations.resource.outbound.be;

import java.util.LinkedList;
import java.util.List;

public class BookingDetailsResponse {

    private String bookingDateAndTime;
    private String bookingRefNumber;
    private String clientId;
    private String clientTypeId;
    private String pointOfSale;// website
    private String categorySubTypeId;
    private String travelDate;
    private String detailsSummery;
    private String paymentStatus;
    private List<ProductResponse> productResponses;
    private List<String> fileHandlers;
    private String bookingStatus;

    //company details
    private String companyId;
//    private String groupOfCompaniesId;
//    private String groupCompanyId;
//    private String companyMarketId;
//    private String sbuId;
//    private String buId;
//    private String assignTo;


    public String getBookingDateAndTime() {
        return bookingDateAndTime;
    }

    public void setBookingDateAndTime(String bookingDateAndTime) {
        this.bookingDateAndTime = bookingDateAndTime;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public BookingDetailsResponse() {
        productResponses = new LinkedList<>();
    }

    public String getClientTypeId() {
        return clientTypeId;
    }

    public void setClientTypeId(String clientTypeId) {
        this.clientTypeId = clientTypeId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public List<ProductResponse> getProductResponses() {
        return productResponses;
    }

    public void setProductResponses(List<ProductResponse> productResponses) {
        this.productResponses = productResponses;
    }

    public String getBookingRefNumber( ) {
        return bookingRefNumber;
    }

    public void setBookingRefNumber( String bookingRefNumber ) {
        this.bookingRefNumber = bookingRefNumber;
    }

    public String getClientId( ) {
        return clientId;
    }

    public void setClientId( String clientId ) {
        this.clientId = clientId;
    }

    public String getPointOfSale( ) {
        return pointOfSale;
    }

    public void setPointOfSale( String pointOfSale ) {
        this.pointOfSale = pointOfSale;
    }

    public String getCategorySubTypeId( ) {
        return categorySubTypeId;
    }

    public void setCategorySubTypeId( String categorySubTypeId ) {
        this.categorySubTypeId = categorySubTypeId;
    }

    public String getDetailsSummery( ) {
        return detailsSummery;
    }

    public void setDetailsSummery( String detailsSummery ) {
        this.detailsSummery = detailsSummery;
    }

    public String getPaymentStatus( ) {
        return paymentStatus;
    }

    public void setPaymentStatus( String paymentStatus ) {
        this.paymentStatus = paymentStatus;
    }

    public List<String> getFileHandlers() {
        return fileHandlers;
    }

    public void setFileHandlers(List<String> fileHandlers) {
        this.fileHandlers = fileHandlers;
    }

    //    public String getFileHandlerId() {
//        return fileHandlerId;
//    }
//
//    public void setFileHandlerId(String fileHandlerId) {
//        this.fileHandlerId = fileHandlerId;
//    }
//
//    public int getId( ) {
//        return id;
//    }
//
//    public void setId( int id ) {
//        this.id = id;
//    }
//
//    public String getGroupOfCompaniesId( ) {
//        return groupOfCompaniesId;
//    }
//
//    public void setGroupOfCompaniesId( String groupOfCompaniesId ) {
//        this.groupOfCompaniesId = groupOfCompaniesId;
//    }
//
//    public String getGroupNameId( ) {
//        return groupCompanyId;
//    }
//
//    public void setGroupNameId( String groupCompanyId ) {
//        this.groupCompanyId = groupCompanyId;
//    }
//
//    public String getCompanyId( ) {
//        return companyId;
//    }
//
//    public void setCompanyId( String companyId ) {
//        this.companyId = companyId;
//    }
//
//    public String getCompanyMarketId( ) {
//        return companyMarketId;
//    }
//
//    public void setCompanyMarketId( String companyMarketId ) {
//        this.companyMarketId = companyMarketId;
//    }
//
//    public String getSbuId( ) {
//        return sbuId;
//    }
//
//    public void setSbuId( String sbuId ) {
//        this.sbuId = sbuId;
//    }
//
//    public String getBuId( ) {
//        return buId;
//    }
//
//    public void setBuId( String buId ) {
//        this.buId = buId;
//    }

   /* @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !( o instanceof BookingDetailsResponse ) ) return false;
        BookingDetailsResponse response = ( BookingDetailsResponse ) o;
        return getId( ) == response.getId( ) &&
                Objects.equals( getBookingDateAndTime( ) , response.getBookingDateAndTime( ) ) &&
                Objects.equals( getBookingRefNumber( ) , response.getBookingRefNumber( ) ) &&
                Objects.equals( getClientId( ) , response.getClientId( ) ) &&
                Objects.equals( getPointOfSale( ) , response.getPointOfSale( ) ) &&
                Objects.equals( getPassengerName( ) , response.getPassengerName( ) ) &&
                Objects.equals( getCategorySubTypeId( ) , response.getCategorySubTypeId( ) ) &&
                Objects.equals( getProductName( ) , response.getProductName( ) ) &&
                Objects.equals( getTravelDate( ) , response.getTravelDate( ) ) &&
                Objects.equals( getDetailsSummery( ) , response.getDetailsSummery( ) ) &&
                Objects.equals( getSupplierName( ) , response.getSupplierName( ) ) &&
                Objects.equals( getProductStatus( ) , response.getProductStatus( ) ) &&
                Objects.equals( getPaymentStatus( ) , response.getPaymentStatus( ) ) &&
                Objects.equals( getFileHandlerName( ) , response.getFileHandlerName( ) ) &&
                Objects.equals( getGroupOfCompaniesId( ) , response.getGroupOfCompaniesId( ) ) &&
                Objects.equals( getGroupNameId( ) , response.getGroupNameId( ) ) &&
                Objects.equals( getCompanyId( ) , response.getCompanyId( ) ) &&
                Objects.equals( getCompanyMarketId( ) , response.getCompanyMarketId( ) ) &&
                Objects.equals( getSbuId( ) , response.getSbuId( ) ) &&
                Objects.equals( getBuId( ) , response.getBuId( ) );
    }

    @Override
    public int hashCode( ) {

        return Objects.hash( getBookingDateAndTime( ) , getBookingRefNumber( ) , getClientId( ) , getPointOfSale( ) , getPassengerName( ) , getCategorySubTypeId( ) , getProductName( ) , getTravelDate( ) , getDetailsSummery( ) , getSupplierName( ) , getProductStatus( ) , getPaymentStatus( ) , getFileHandlerName( ) , getId( ) , getGroupOfCompaniesId( ) , getGroupNameId( ) , getCompanyId( ) , getCompanyMarketId( ) , getSbuId( ) , getBuId( ) );
    }*/
}
