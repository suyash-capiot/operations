package com.coxandkings.travel.operations.criteria.fileprofitability;

import com.coxandkings.travel.operations.enums.FileProfitability.FileProfTypes;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateSerializer;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;

public class FileProfSearchReportCriteria {
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

    private FileProfTypes fileProfTypes;
    private String departureLocation;
    private String destinationLocation;
    private boolean isBookRandomEntry;

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

    public boolean isPassengerwise() {
        return isPassengerwise;
    }

    public void setPassengerwise(boolean passengerwise) {
        isPassengerwise = passengerwise;
    }

    public boolean isRoomwise() {
        return isRoomwise;
    }

    public void setRoomwise(boolean roomwise) {
        isRoomwise = roomwise;
    }

    public boolean isTransportation() {
        return isTransportation;
    }

    public void setTransportation(boolean transportation) {
        isTransportation = transportation;
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

    public FileProfTypes getFileProfTypes() {
        return fileProfTypes;
    }

    public void setFileProfTypes(FileProfTypes fileProfTypes) {
        this.fileProfTypes = fileProfTypes;
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

    public boolean isBookRandomEntry() {
        return isBookRandomEntry;
    }

    public void setBookRandomEntry(boolean bookRandomEntry) {
        isBookRandomEntry = bookRandomEntry;
    }
}
