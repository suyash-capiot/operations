
package com.coxandkings.travel.operations.resource.productsharing;

import java.io.Serializable;
import java.util.Objects;

public class ProductSharingMainResource implements Serializable {

    private String serialNumber;
    private String bookingReferenceNo;
    private String orderId;
    private String passengerName;
    private String firstName;
    private String lastName;
    private String title;
    private String genderPreferenceForSharing;
    private ProductSharingStatus status;
    private String passengerId;
    private String emailId;
    private AccommodationDetails accommodationDetails;

    //TODO : Need to complete after discussion
    private Object cruiseDetails;
    private Object transferDetailsPrivateOrShared;
    private Object holidays;
    private Object activitiesWithoutTransfer;

    private final static long serialVersionUID = 3540580182166037865L;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public ProductSharingMainResource withSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public String getBookingReferenceNo() {
        return bookingReferenceNo;
    }

    public void setBookingReferenceNo(String bookingReferenceNo) {
        this.bookingReferenceNo = bookingReferenceNo;
    }

    public ProductSharingMainResource withBookingReferenceNo(String bookingReferenceNo) {
        this.bookingReferenceNo = bookingReferenceNo;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ProductSharingMainResource withOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public ProductSharingMainResource withPassengerName(String passengerName) {
        this.passengerName = passengerName;
        return this;
    }

    public String getGenderPreferenceForSharing() {
        return genderPreferenceForSharing;
    }

    public void setGenderPreferenceForSharing(String genderPreferenceForSharing) {
        this.genderPreferenceForSharing = genderPreferenceForSharing;
    }

    public ProductSharingMainResource withGenderPreferenceForsharing(String genderPreferenceForsharing) {
        this.genderPreferenceForSharing = genderPreferenceForsharing;
        return this;
    }

    public AccommodationDetails getAccommodationDetails() {
        return accommodationDetails;
    }

    public void setAccommodationDetails(AccommodationDetails accommodationDetails) {
        this.accommodationDetails = accommodationDetails;
    }

    public ProductSharingMainResource withAccmmodationDetails(AccommodationDetails accommodationDetails) {
        this.accommodationDetails = accommodationDetails;
        return this;
    }

    public Object getCruiseDetails() {
        return cruiseDetails;
    }

    public void setCruiseDetails(Object cruiseDetails) {
        this.cruiseDetails = cruiseDetails;
    }

    public Object getTransferDetailsPrivateOrShared() {
        return transferDetailsPrivateOrShared;
    }

    public void setTransferDetailsPrivateOrShared(Object transferDetailsPrivateOrShared) {
        this.transferDetailsPrivateOrShared = transferDetailsPrivateOrShared;
    }

    public Object getHolidays() {
        return holidays;
    }

    public void setHolidays(Object holidays) {
        this.holidays = holidays;
    }

    public Object getActivitiesWithoutTransfer() {
        return activitiesWithoutTransfer;
    }

    public void setActivitiesWithoutTransfer(Object activitiesWithoutTransfer) {
        this.activitiesWithoutTransfer = activitiesWithoutTransfer;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ProductSharingStatus getStatus() {
        return status;
    }

    public void setStatus(ProductSharingStatus status) {
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductSharingMainResource)) return false;
        ProductSharingMainResource that = (ProductSharingMainResource) o;
        return Objects.equals(getSerialNumber(), that.getSerialNumber()) &&
                Objects.equals(getBookingReferenceNo(), that.getBookingReferenceNo()) &&
                Objects.equals(getOrderId(), that.getOrderId()) &&
                Objects.equals(getPassengerName(), that.getPassengerName()) &&
                Objects.equals(getFirstName(), that.getFirstName()) &&
                Objects.equals(getLastName(), that.getLastName()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getGenderPreferenceForSharing(), that.getGenderPreferenceForSharing()) &&
                getStatus() == that.getStatus() &&
                Objects.equals(getPassengerId(), that.getPassengerId()) &&
                Objects.equals(getEmailId(), that.getEmailId()) &&
                Objects.equals(getAccommodationDetails(), that.getAccommodationDetails()) &&
                Objects.equals(getCruiseDetails(), that.getCruiseDetails()) &&
                Objects.equals(getTransferDetailsPrivateOrShared(), that.getTransferDetailsPrivateOrShared()) &&
                Objects.equals(getHolidays(), that.getHolidays()) &&
                Objects.equals(getActivitiesWithoutTransfer(), that.getActivitiesWithoutTransfer());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getSerialNumber(), getBookingReferenceNo(), getOrderId(), getPassengerName(), getFirstName(), getLastName(), getTitle(), getGenderPreferenceForSharing(), getStatus(), getPassengerId(), getEmailId(), getAccommodationDetails(), getCruiseDetails(), getTransferDetailsPrivateOrShared(), getHolidays(), getActivitiesWithoutTransfer());
    }

    @Override
    public String toString() {
        return "ProductSharingMainResource{" +
                "serialNumber='" + serialNumber + '\'' +
                ", bookingReferenceNo='" + bookingReferenceNo + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
