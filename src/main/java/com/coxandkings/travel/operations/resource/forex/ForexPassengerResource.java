package com.coxandkings.travel.operations.resource.forex;

import com.coxandkings.travel.operations.model.core.OpsAddressDetails;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.Set;

public class ForexPassengerResource {

    private String id;
    private String createdByUserId;
    private String lastModifiedByUserId;
    private Boolean leadPassenger;
    private String salutation;
    private String passengerType;
    private String firstName;
    private String middleName;
    private String lastName;
    private Boolean isForexRequired;
    private String dob;
    private OpsAddressDetails addressDetails;
    private String forexStatus;

    @JsonBackReference
    private ForexBookingResource forex;
    //travel details
    private String airLine;
    private Long departureDate;
    private String ticketNumber;

    private PassportDetailsResource passportDetails;
    private Set<TourCostResource> tourCostDetails;
    private PersonalExpenseResource personalExpenseDetails;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    public void setLastModifiedByUserId(String lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }

    public ForexBookingResource getForex() {
        return forex;
    }

    public void setForex(ForexBookingResource forex) {
        this.forex = forex;
    }

    public Boolean getLeadPassenger() {
        return leadPassenger;
    }

    public void setLeadPassenger(Boolean leadPassenger) {
        this.leadPassenger = leadPassenger;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }


    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public OpsAddressDetails getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(OpsAddressDetails addressDetails) {
        this.addressDetails = addressDetails;
    }

    public Set<TourCostResource> getTourCostDetails() {
        return tourCostDetails;
    }

    public void setTourCostDetails(Set<TourCostResource> tourCostDetails) {
        this.tourCostDetails = tourCostDetails;
    }

    public PersonalExpenseResource getPersonalExpenseDetails() {
        return personalExpenseDetails;
    }

    public void setPersonalExpenseDetails(PersonalExpenseResource personalExpenseDetails) {
        this.personalExpenseDetails = personalExpenseDetails;
    }

    public String getAirLine() {
        return airLine;
    }

    public void setAirLine(String airLine) {
        this.airLine = airLine;
    }

    public Long getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Long departureDate) {
        this.departureDate = departureDate;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getForexStatus() {
        return forexStatus;
    }

    public void setForexStatus(String forexStatus) {
        this.forexStatus = forexStatus;
    }

    public PassportDetailsResource getPassportDetails() {
        return passportDetails;
    }

    public void setPassportDetails(PassportDetailsResource passportDetails) {
        this.passportDetails = passportDetails;
    }

    public Boolean getForexRequired() {
        return isForexRequired;
    }

    public void setForexRequired(Boolean forexRequired) {
        isForexRequired = forexRequired;
    }
}
