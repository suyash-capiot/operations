
package com.coxandkings.travel.ext.model.finance.invoice;

import com.fasterxml.jackson.annotation.*;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "bookingRefNumber",
    "orderID",
    "productName",
    "productCategory",
    "productCategorySubType",
    "productNameSubType",
    "currency",
    "supplierCost",
    "sellingPrice",
    "amendmentType",
    "travelDate",
    "amendmentCharges",
    "amendmentPrice",
    "totalTax",
    "totalCommercialCharges",
    "totalPax",
    "receiptAmountApplied",
    "revenueRecognitionDate",
    "bookingMonth",
    "bookingYear",
    "checkOutDate",
    "checkInDate",
    "bookingDate",
    "orderCommercials",
    "passengerDetails",
    "taxDetails"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceParticularsDto {

    @JsonProperty("bookingRefNumber")
    private String bookingRefNumber;
    @JsonProperty("orderID")
    private String orderID;
    @JsonProperty("productName")
    private String productName;
    @JsonProperty("productCategory")
    private String productCategory;
    @JsonProperty("productCategorySubType")
    private String productCategorySubType;
    @JsonProperty("productNameSubType")
    private String productNameSubType;
    @JsonProperty("isInternantional")
    private String isInternantional;
    @JsonProperty("supplierCurrency")
    private String supplierCurrency;
    @JsonProperty("supplierCost")
    private Double supplierCost;
    @JsonProperty("sellingPrice")
    private Double sellingPrice;
    @JsonProperty("amendmentType")
    private AmendmentType amendmentType;
    @JsonProperty("travelStartDate")
    private ZonedDateTime travelStartDate;
    @JsonProperty("travelEndDate")
    private ZonedDateTime travelEndDate;
    @JsonProperty("amendmentCharges")
    private Double amendmentCharges;
    @JsonProperty("amendmentPrice")
    private Double amendmentPrice;
    @JsonProperty("totalTax")
    private Double totalTax;
    @JsonProperty("totalCommercialCharges")
    private Double totalCommercialCharges;
    @JsonProperty("totalPax")
    private Integer totalPax;
    @JsonProperty("tourName")
    private String tourName;
    @JsonProperty("receiptAmountApplied")
    private Double receiptAmountApplied;
    @JsonProperty("revenueRecognitionDate")
    private ZonedDateTime revenueRecognitionDate;
    @JsonProperty("bookingMonth")
    private Long bookingMonth;
    @JsonProperty("bookingYear")
    private Long bookingYear;
    @JsonProperty("checkOutDate")
    private ZonedDateTime checkOutDate;
    @JsonProperty("checkInDate")
    private ZonedDateTime checkInDate;
    @JsonProperty("bookingDate")
    private ZonedDateTime bookingDate;

    @JsonProperty("orderCommercials")
    private List<CommercialEntity> orderCommercials = null;
    @JsonProperty("passengerDetails")
    private List<PassengerDetail> passengerDetails = null;
    @JsonProperty("taxDetails")
    private List<TaxDetail> taxDetails = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("bookingRefNumber")
    public String getBookingRefNumber() {
        return bookingRefNumber;
    }

    @JsonProperty("bookingRefNumber")
    public void setBookingRefNumber(String bookingRefNumber) {
        this.bookingRefNumber = bookingRefNumber;
    }

    @JsonProperty("orderID")
    public String getOrderID() {
        return orderID;
    }

    @JsonProperty("orderID")
    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    @JsonProperty("productName")
    public String getProductName() {
        return productName;
    }

    @JsonProperty("productName")
    public void setProductName(String productName) {
        this.productName = productName;
    }

    @JsonProperty("productCategory")
    public String getProductCategory() {
        return productCategory;
    }

    @JsonProperty("productCategory")
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    @JsonProperty("productCategorySubType")
    public String getProductCategorySubType() {
        return productCategorySubType;
    }

    @JsonProperty("productCategorySubType")
    public void setProductCategorySubType(String productCategorySubType) {
        this.productCategorySubType = productCategorySubType;
    }

    @JsonProperty("productNameSubType")
    public String getProductNameSubType() {
        return productNameSubType;
    }

    @JsonProperty("productNameSubType")
    public void setProductNameSubType(String productNameSubType) {
        this.productNameSubType = productNameSubType;
    }

    @JsonProperty("supplierCost")
    public Double getSupplierCost() {
        return supplierCost;
    }

    @JsonProperty("supplierCost")
    public void setSupplierCost(Double supplierCost) {
        this.supplierCost = supplierCost;
    }

    @JsonProperty("sellingPrice")
    public Double getSellingPrice() {
        return sellingPrice;
    }

    @JsonProperty("sellingPrice")
    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    @JsonProperty("amendmentType")
    public AmendmentType getAmendmentType() {
        return amendmentType;
    }

    @JsonProperty("amendmentType")
    public void setAmendmentType(AmendmentType amendmentType) {
        this.amendmentType = amendmentType;
    }

    @JsonProperty("amendmentCharges")
    public Double getAmendmentCharges() {
        return amendmentCharges;
    }

    @JsonProperty("amendmentCharges")
    public void setAmendmentCharges(Double amendmentCharges) {
        this.amendmentCharges = amendmentCharges;
    }

    @JsonProperty("amendmentPrice")
    public Double getAmendmentPrice() {
        return amendmentPrice;
    }

    @JsonProperty("amendmentPrice")
    public void setAmendmentPrice(Double amendmentPrice) {
        this.amendmentPrice = amendmentPrice;
    }

    @JsonProperty("totalTax")
    public Double getTotalTax() {
        return totalTax;
    }

    @JsonProperty("totalTax")
    public void setTotalTax(Double totalTax) {
        this.totalTax = totalTax;
    }

    @JsonProperty("totalCommercialCharges")
    public Double getTotalCommercialCharges() {
        return totalCommercialCharges;
    }

    @JsonProperty("totalCommercialCharges")
    public void setTotalCommercialCharges(Double totalCommercialCharges) {
        this.totalCommercialCharges = totalCommercialCharges;
    }

    @JsonProperty("totalPax")
    public Integer getTotalPax() {
        return totalPax;
    }

    @JsonProperty("totalPax")
    public void setTotalPax(Integer totalPax) {
        this.totalPax = totalPax;
    }

    @JsonProperty("receiptAmountApplied")
    public Double getReceiptAmountApplied() {
        return receiptAmountApplied;
    }

    @JsonProperty("receiptAmountApplied")
    public void setReceiptAmountApplied(Double receiptAmountApplied) {
        this.receiptAmountApplied = receiptAmountApplied;
    }

    @JsonProperty("revenueRecognitionDate")
    public ZonedDateTime getRevenueRecognitionDate() {
        return revenueRecognitionDate;
    }

    @JsonProperty("revenueRecognitionDate")
    public void setRevenueRecognitionDate(ZonedDateTime revenueRecognitionDate) {
        this.revenueRecognitionDate = revenueRecognitionDate;
    }

    @JsonProperty("bookingMonth")
    public Long getBookingMonth() {
        return bookingMonth;
    }

    @JsonProperty("bookingMonth")
    public void setBookingMonth(Long bookingMonth) {
        this.bookingMonth = bookingMonth;
    }

    @JsonProperty("bookingYear")
    public Long getBookingYear() {
        return bookingYear;
    }

    @JsonProperty("bookingYear")
    public void setBookingYear(Long bookingYear) {
        this.bookingYear = bookingYear;
    }

    @JsonProperty("checkOutDate")
    public Object getCheckOutDate() {
        return checkOutDate;
    }

    @JsonProperty("checkOutDate")
    public void setCheckOutDate(ZonedDateTime checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    @JsonProperty("checkInDate")
    public ZonedDateTime getCheckInDate() {
        return checkInDate;
    }

    @JsonProperty("checkInDate")
    public void setCheckInDate(ZonedDateTime checkInDate) {
        this.checkInDate = checkInDate;
    }

    @JsonProperty("bookingDate")
    public ZonedDateTime getBookingDate() {
        return bookingDate;
    }

    @JsonProperty("bookingDate")
    public void setBookingDate(ZonedDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    @JsonProperty("orderCommercials")
    public List<CommercialEntity> getOrderCommercials() {
        return orderCommercials;
    }

    @JsonProperty("orderCommercials")
    public void setOrderCommercials(List<CommercialEntity> orderCommercials) {
        this.orderCommercials = orderCommercials;
    }

    @JsonProperty("passengerDetails")
    public List<PassengerDetail> getPassengerDetails() {
        return passengerDetails;
    }

    @JsonProperty("passengerDetails")
    public void setPassengerDetails(List<PassengerDetail> passengerDetails) {
        this.passengerDetails = passengerDetails;
    }

    @JsonProperty("taxDetails")
    public List<TaxDetail> getTaxDetails() {
        return taxDetails;
    }

    @JsonProperty("taxDetails")
    public void setTaxDetails(List<TaxDetail> taxDetails) {
        this.taxDetails = taxDetails;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getIsInternantional() {
        return isInternantional;
    }

    public void setIsInternantional(String isInternantional) {
        this.isInternantional = isInternantional;
    }

    public String getSupplierCurrency() {
        return supplierCurrency;
    }

    public void setSupplierCurrency(String supplierCurrency) {
        this.supplierCurrency = supplierCurrency;
    }

    public ZonedDateTime getTravelStartDate() {
        return travelStartDate;
    }

    public void setTravelStartDate(ZonedDateTime travelStartDate) {
        this.travelStartDate = travelStartDate;
    }

    public ZonedDateTime getTravelEndDate() {
        return travelEndDate;
    }

    public void setTravelEndDate(ZonedDateTime travelEndDate) {
        this.travelEndDate = travelEndDate;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

}
