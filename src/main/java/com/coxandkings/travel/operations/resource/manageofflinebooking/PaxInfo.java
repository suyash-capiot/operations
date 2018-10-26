package com.coxandkings.travel.operations.resource.manageofflinebooking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "amendmentChargesDetails",
    "cancellationChargesDetails",
    "isLeadPax",
    "firstName",
    "lastName",
    "paxType",
    "specialRequests",
    "passengerID",
    "middleName",
    "addressDetails",
    "ancillaryServices",
    "title",
    "birthDate",
    "contactDetails",
    "status",
    "ticketNumber",
    "seatMap"
})
public class PaxInfo {

    @JsonProperty("amendmentChargesDetails")
    private List<AmendmentChargesDetail> amendmentChargesDetails = null;
    @JsonProperty("cancellationChargesDetails")
    private List<CancellationChargesDetail> cancellationChargesDetails = null;
    @JsonProperty("isLeadPax")
    private Boolean isLeadPax;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("paxType")
    private String paxType;
    @JsonProperty("specialRequests")
    private SpecialRequests specialRequests;
    @JsonProperty("passengerID")
    private String passengerID;
    @JsonProperty("middleName")
    private String middleName;
    @JsonProperty("addressDetails")
    private AddressDetails addressDetails;
    @JsonProperty("ancillaryServices")
    private AncillaryServices ancillaryServices;
    @JsonProperty("title")
    private String title;
    @JsonProperty("birthDate")
    private String birthDate;
    @JsonProperty("contactDetails")
    private List<ContactDetail> contactDetails = null;
    @JsonProperty("status")
    private String status;
    @JsonProperty("ticketNumber")
    private String ticketNumber;
    @JsonProperty("seatMap")
    private List<Object> seatMap = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("amendmentChargesDetails")
    public List<AmendmentChargesDetail> getAmendmentChargesDetails() {
        return amendmentChargesDetails;
    }

    @JsonProperty("amendmentChargesDetails")
    public void setAmendmentChargesDetails(List<AmendmentChargesDetail> amendmentChargesDetails) {
        this.amendmentChargesDetails = amendmentChargesDetails;
    }

    @JsonProperty("cancellationChargesDetails")
    public List<CancellationChargesDetail> getCancellationChargesDetails() {
        return cancellationChargesDetails;
    }

    @JsonProperty("cancellationChargesDetails")
    public void setCancellationChargesDetails(List<CancellationChargesDetail> cancellationChargesDetails) {
        this.cancellationChargesDetails = cancellationChargesDetails;
    }

    @JsonProperty("isLeadPax")
    public Boolean getIsLeadPax() {
        return isLeadPax;
    }

    @JsonProperty("isLeadPax")
    public void setIsLeadPax(Boolean isLeadPax) {
        this.isLeadPax = isLeadPax;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("paxType")
    public String getPaxType() {
        return paxType;
    }

    @JsonProperty("paxType")
    public void setPaxType(String paxType) {
        this.paxType = paxType;
    }

    @JsonProperty("specialRequests")
    public SpecialRequests getSpecialRequests() {
        return specialRequests;
    }

    @JsonProperty("specialRequests")
    public void setSpecialRequests(SpecialRequests specialRequests) {
        this.specialRequests = specialRequests;
    }

    @JsonProperty("passengerID")
    public String getPassengerID() {
        return passengerID;
    }

    @JsonProperty("passengerID")
    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    @JsonProperty("middleName")
    public String getMiddleName() {
        return middleName;
    }

    @JsonProperty("middleName")
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @JsonProperty("addressDetails")
    public AddressDetails getAddressDetails() {
        return addressDetails;
    }

    @JsonProperty("addressDetails")
    public void setAddressDetails(AddressDetails addressDetails) {
        this.addressDetails = addressDetails;
    }

    @JsonProperty("ancillaryServices")
    public AncillaryServices getAncillaryServices() {
        return ancillaryServices;
    }

    @JsonProperty("ancillaryServices")
    public void setAncillaryServices(AncillaryServices ancillaryServices) {
        this.ancillaryServices = ancillaryServices;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("birthDate")
    public String getBirthDate() {
        return birthDate;
    }

    @JsonProperty("birthDate")
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    @JsonProperty("contactDetails")
    public List<ContactDetail> getContactDetails() {
        return contactDetails;
    }

    @JsonProperty("contactDetails")
    public void setContactDetails(List<ContactDetail> contactDetails) {
        this.contactDetails = contactDetails;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("ticketNumber")
    public String getTicketNumber() {
        return ticketNumber;
    }

    @JsonProperty("ticketNumber")
    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    @JsonProperty("seatMap")
    public List<Object> getSeatMap() {
        return seatMap;
    }

    @JsonProperty("seatMap")
    public void setSeatMap(List<Object> seatMap) {
        this.seatMap = seatMap;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}