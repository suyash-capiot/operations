package com.coxandkings.travel.operations.criteria.fileprofitability;

import com.coxandkings.travel.operations.enums.FileProfitability.FileProfTypes;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateSerializer;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class FileProfSearchCriteria implements Serializable {
    @JsonProperty("travelDateFrom")

    @JsonSerialize(using = ZonedDateSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime travelDateFrom;

    @JsonSerialize(using = ZonedDateSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime travelDateTo;
    private String prodcutCategory;
    private String productSubCategory;
    private String productName;
    private String bookingRefNumber;
    private String leadPassengerName;
    private boolean isBookingRefNumbWise;
    private boolean isPassengerwise;
    private boolean isRoomwise;
    private boolean isTransportation;
    private boolean isAccomodation;
    private String orderId;
    private Integer pageSize;
    private Integer pageNumber;
    private FileProfTypes fileProfTypes;
    private String departureLocation;
    private String destinationLocation;
    private boolean isBookRandomEntry;

    public boolean getIsBookRandomEntry() {
        return isBookRandomEntry;
    }

    public void setIsBookRandomEntry(boolean bookRandomEntry) {
        isBookRandomEntry = bookRandomEntry;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public FileProfTypes getFileProfTypes() {
        return fileProfTypes;
    }

    public void setFileProfTypes(FileProfTypes fileProfTypes) {
        this.fileProfTypes = fileProfTypes;
    }

    public boolean getIsBookingRefNumbWise() {
        return isBookingRefNumbWise;
    }

    public void setIsBookingRefNumbWise(boolean bookingRefNumbWise) {
        isBookingRefNumbWise = bookingRefNumbWise;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public boolean getIsTransportation() {
        return isTransportation;
    }

    public void setIsTransportation(boolean transportation) {
        isTransportation = transportation;
    }

    public boolean getIsAccomodation() {
        return isAccomodation;
    }

    public void setIsAccomodation(boolean accomodation) {
        isAccomodation = accomodation;
    }

    public boolean getIsPassengerwise() {
        return isPassengerwise;
    }

    public void setIsPassengerwise(boolean passengerwise) {
        isPassengerwise = passengerwise;
    }

    public boolean getIsRoomwise() {
        return isRoomwise;
    }

    public void setIsRoomwise(boolean roomwise) {
        isRoomwise = roomwise;
    }

    public ZonedDateTime getTravelDateFrom() {
        return travelDateFrom;
    }

    public void setTravelDateFrom(ZonedDateTime travelDateFrom) {
        this.travelDateFrom = travelDateFrom;
    }

    public ZonedDateTime getTravelDateTo() {
        return travelDateTo;
    }

    public void setTravelDateTo(ZonedDateTime travelDateTo) {
        this.travelDateTo = travelDateTo;
    }

    public boolean isRoomwise() {
        return isRoomwise;
    }

    public void setRoomwise(boolean roomwise) {
        isRoomwise = roomwise;
    }

    public String getProdcutCategory() {
        return prodcutCategory;
    }

    public void setProdcutCategory(String prodcutCategory) {
        this.prodcutCategory = prodcutCategory;
    }

    public String getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBookingRefNumber() {
        return bookingRefNumber;
    }

    public void setBookingRefNumber(String bookingRefNumber) {
        this.bookingRefNumber = bookingRefNumber;
    }

    public String getLeadPassengerName() {
        return leadPassengerName;
    }

    public void setLeadPassengerName(String leadPassengerName) {
        this.leadPassengerName = leadPassengerName;
    }

    public boolean isBookingRefNumbWise() {
        return isBookingRefNumbWise;
    }

    public void setBookingRefNumbWise(boolean bookingRefNumbWise) {
        isBookingRefNumbWise = bookingRefNumbWise;
    }

    public void setPassengerwise(boolean passengerwise) {
        isPassengerwise = passengerwise;
    }

    public boolean isAccomodation() {
        return isAccomodation;
    }

    public void setAccomodation(boolean accomodation) {
        isAccomodation = accomodation;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
