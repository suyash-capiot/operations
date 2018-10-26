package com.coxandkings.travel.operations.model.forex;

import com.coxandkings.travel.operations.model.core.OpsAddressDetails;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "forex_passenger_details")
public class ForexPassenger {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "created_by_user_id")
    @CreatedBy
    private String createdByUserId;

    @Column(name = "last_modified_by_user_id")
    @LastModifiedBy
    private String lastModifiedByUserId;

    private Boolean leadPassenger;
    private String salutation;
    private String firstName;
    private String middleName;
    private String lastName;
    private String passengerType;
    private String dob;
    private Boolean isForexRequired;
    private OpsAddressDetails addressDetails;
    private String forexStatus;

    //Travel details
    private String airLine;
    private Long departureDate;
    private String ticketNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forex_id")
    @JsonBackReference
    private ForexBooking forex; // Reference to Forex

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passport_id")
    private PassportDetails passportDetails;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "forexPassenger")
    private Set<TourCostDetails> tourCostDetails;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personal_expense_id")
    private PersonalExpenseDetails personalExpenseDetails;

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

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public ForexBooking getForex() {
        return forex;
    }

    public void setForex(ForexBooking forex) {
        this.forex = forex;
    }

    public PassportDetails getPassportDetails() {
        return passportDetails;
    }

    public void setPassportDetails(PassportDetails passportDetails) {
        this.passportDetails = passportDetails;
    }


    public OpsAddressDetails getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(OpsAddressDetails addressDetails) {
        this.addressDetails = addressDetails;
    }

    public Set<TourCostDetails> getTourCostDetails() {
        return tourCostDetails;
    }

    public void setTourCostDetails(Set<TourCostDetails> tourCostDetails) {
        this.tourCostDetails = tourCostDetails;
    }

    public PersonalExpenseDetails getPersonalExpenseDetails() {
        return personalExpenseDetails;
    }

    public void setPersonalExpenseDetails(PersonalExpenseDetails personalExpenseDetails) {
        this.personalExpenseDetails = personalExpenseDetails;
    }


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

    public Boolean getForexRequired() {
        return isForexRequired;
    }

    public void setForexRequired(Boolean forexRequired) {
        isForexRequired = forexRequired;
    }
}
