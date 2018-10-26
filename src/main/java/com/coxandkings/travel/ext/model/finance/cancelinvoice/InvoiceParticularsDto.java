package com.coxandkings.travel.ext.model.finance.cancelinvoice;

import com.coxandkings.travel.ext.model.finance.invoice.AmendmentType;
import com.coxandkings.travel.ext.model.finance.invoice.PassengerDetails;
import com.coxandkings.travel.ext.model.finance.invoice.TaxDetailsEntity;

import java.util.HashSet;
import java.util.Set;


public class InvoiceParticularsDto {
    private String bookingRefNumber;
    private String orderID;
    private String productName;
    private String productCategory;
    private String productCategorySubType;
    private String productNameSubType;
    private Boolean isInternantional; //
    private String supplierCurrency;
    private Double supplierCost;
    private Double sellingPrice;
    private AmendmentType amendmentType;

    private String travelStartDate; //

    private String travelEndDate; //
    private Double amendmentCharges;
    private Double amendmentPrice;
    private Double totalTax;
    private Double totalCommercialCharges;
    private Long totalPax;
    private String tourName;
    private Double receiptAmountApplied;

    private String revenueRecognitionDate;
    private Long bookingMonth;
    private Long bookingYear;

    private String checkOutDate;

    private String checkInDate;

    private String bookingDate;
    private Set<CommercialEntity> orderCommercials = new HashSet<>();
    private Set<PassengerDetails> passengerDetails = new HashSet<>();
    private Set<TaxDetailsEntity> taxDetails = new HashSet<>();

    public String getBookingRefNumber() {
        return bookingRefNumber;
    }

    public void setBookingRefNumber(String bookingRefNumber) {
        this.bookingRefNumber = bookingRefNumber;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

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

    public String getProductNameSubType() {
        return productNameSubType;
    }

    public void setProductNameSubType(String productNameSubType) {
        this.productNameSubType = productNameSubType;
    }

    public Boolean getIsInternantional() {
        return isInternantional;
    }

    public void setIsInternantional(Boolean internantional) {
        isInternantional = internantional;
    }

    public String getSupplierCurrency() {
        return supplierCurrency;
    }

    public void setSupplierCurrency(String supplierCurrency) {
        this.supplierCurrency = supplierCurrency;
    }

    public Double getSupplierCost() {
        return supplierCost;
    }

    public void setSupplierCost(Double supplierCost) {
        this.supplierCost = supplierCost;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public AmendmentType getAmendmentType() {
        return amendmentType;
    }

    public void setAmendmentType(AmendmentType amendmentType) {
        this.amendmentType = amendmentType;
    }

    public String getTravelStartDate() {
        return travelStartDate;
    }

    public void setTravelStartDate(String travelStartDate) {
        this.travelStartDate = travelStartDate;
    }

    public String getTravelEndDate() {
        return travelEndDate;
    }

    public void setTravelEndDate(String travelEndDate) {
        this.travelEndDate = travelEndDate;
    }

    public Double getAmendmentCharges() {
        return amendmentCharges;
    }

    public void setAmendmentCharges(Double amendmentCharges) {
        this.amendmentCharges = amendmentCharges;
    }

    public Double getAmendmentPrice() {
        return amendmentPrice;
    }

    public void setAmendmentPrice(Double amendmentPrice) {
        this.amendmentPrice = amendmentPrice;
    }

    public Double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(Double totalTax) {
        this.totalTax = totalTax;
    }

    public Double getTotalCommercialCharges() {
        return totalCommercialCharges;
    }

    public void setTotalCommercialCharges(Double totalCommercialCharges) {
        this.totalCommercialCharges = totalCommercialCharges;
    }

    public Long getTotalPax() {
        return totalPax;
    }

    public void setTotalPax(Long totalPax) {
        this.totalPax = totalPax;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public Double getReceiptAmountApplied() {
        return receiptAmountApplied;
    }

    public void setReceiptAmountApplied(Double receiptAmountApplied) {
        this.receiptAmountApplied = receiptAmountApplied;
    }

    public String getRevenueRecognitionDate() {
        return revenueRecognitionDate;
    }

    public void setRevenueRecognitionDate(String revenueRecognitionDate) {
        this.revenueRecognitionDate = revenueRecognitionDate;
    }

    public Long getBookingMonth() {
        return bookingMonth;
    }

    public void setBookingMonth(Long bookingMonth) {
        this.bookingMonth = bookingMonth;
    }

    public Long getBookingYear() {
        return bookingYear;
    }

    public void setBookingYear(Long bookingYear) {
        this.bookingYear = bookingYear;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Set<CommercialEntity> getOrderCommercials() {
        return orderCommercials;
    }

    public void setOrderCommercials(Set<CommercialEntity> orderCommercials) {
        this.orderCommercials = orderCommercials;
    }

    public Set<PassengerDetails> getPassengerDetails() {
        return passengerDetails;
    }

    public void setPassengerDetails(Set<PassengerDetails> passengerDetails) {
        this.passengerDetails = passengerDetails;
    }

    public Set<TaxDetailsEntity> getTaxDetails() {
        return taxDetails;
    }

    public void setTaxDetails(Set<TaxDetailsEntity> taxDetails) {
        this.taxDetails = taxDetails;
    }
}
