package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsFlightPaxInfo implements Serializable {

    @JsonProperty("isLeadPax")
    private Boolean leadPax;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("paxType")
    private String paxType;

    @JsonProperty("specialRequests")
    private OpsSpecialRequest opsSpecialRequest;

    @JsonProperty("passengerID")
    private String passengerID;

    @JsonProperty("middleName")
    private String middleName;

    @JsonProperty("addressDetails")
    private OpsAddressDetails addressDetails;

    @JsonProperty("ancillaryServices")
    private OpsAncillaryServices ancillaryServices;

    @JsonProperty("title")
    private String title;

    @JsonProperty("birthDate")
    private String birthDate;

    @JsonProperty("contactDetails")
    private List<OpsContactDetails> contactDetails = new ArrayList<OpsContactDetails>();

    @JsonProperty("status")
    private String status;

    @JsonProperty("ticketNumber")
    private String ticketNumber;

    @JsonProperty("seatMap")
    private List<OpsSeatMap> seatMap = new ArrayList<>();

    private List<OpsAmendDetails> amendmentChargesDetails;

    private List<OpsCancDetails> cancellationChargesDetails;

    public OpsFlightPaxInfo() {
    }

    public Boolean getLeadPax() {
        return leadPax;
    }

    public void setLeadPax(Boolean leadPax) {
        this.leadPax = leadPax;
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

    public String getPaxType() {
        return paxType;
    }

    public void setPaxType(String paxType) {
        this.paxType = paxType;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public OpsAddressDetails getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(OpsAddressDetails addressDetails) {
        this.addressDetails = addressDetails;
    }

    public OpsAncillaryServices getAncillaryServices() {
        return ancillaryServices;
    }

    public void setAncillaryServices(OpsAncillaryServices ancillaryServices) {
        this.ancillaryServices = ancillaryServices;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public List<OpsContactDetails> getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(List<OpsContactDetails> contactDetails) {
        this.contactDetails = contactDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OpsSpecialRequest getOpsSpecialRequest() {
        return opsSpecialRequest;
    }

    public void setOpsSpecialRequest(OpsSpecialRequest opsSpecialRequest) {
        this.opsSpecialRequest = opsSpecialRequest;
    }

    public List<OpsAmendDetails> getAmendmentChargesDetails() {
        return amendmentChargesDetails;
    }

    public void setAmendmentChargesDetails(List<OpsAmendDetails> amendmentChargesDetails) {
        this.amendmentChargesDetails = amendmentChargesDetails;
    }

    public List<OpsCancDetails> getCancellationChargesDetails() {
        return cancellationChargesDetails;
    }

    public void setCancellationChargesDetails(List<OpsCancDetails> cancellationChargesDetails) {
        this.cancellationChargesDetails = cancellationChargesDetails;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public List<OpsSeatMap> getSeatMap() {
        return seatMap;
    }

    public void setSeatMap(List<OpsSeatMap> seatMap) {
        this.seatMap = seatMap;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsFlightPaxInfo paxInfo = (OpsFlightPaxInfo) o;
        return Objects.equals(leadPax, paxInfo.leadPax) &&
                Objects.equals(firstName, paxInfo.firstName) &&
                Objects.equals(lastName, paxInfo.lastName) &&
                Objects.equals(paxType, paxInfo.paxType) &&
                Objects.equals(opsSpecialRequest, paxInfo.opsSpecialRequest) &&
                Objects.equals(passengerID, paxInfo.passengerID) &&
                Objects.equals(middleName, paxInfo.middleName) &&
                Objects.equals(addressDetails, paxInfo.addressDetails) &&
                Objects.equals(ancillaryServices, paxInfo.ancillaryServices) &&
                Objects.equals(title, paxInfo.title) &&
                Objects.equals(birthDate, paxInfo.birthDate) &&
                Objects.equals(contactDetails, paxInfo.contactDetails) &&
                Objects.equals(status, paxInfo.status) &&
                Objects.equals(seatMap,paxInfo.seatMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leadPax, firstName, lastName, paxType, opsSpecialRequest, passengerID, middleName, addressDetails, ancillaryServices, title, birthDate, contactDetails, status,seatMap);
    }
}
