
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
    "isLeadPax",
    "firstName",
    "lastName",
    "paxType",
    "paxID",
    "middleName",
    "addressDetails",
    "title",
    "birthDate",
    "contactDetails"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaxInfo implements Serializable
{

    @JsonProperty("isLeadPax")
    private Boolean isLeadPax;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("paxType")
    private String paxType;
    @JsonProperty("paxID")
    private String paxID;
    @JsonProperty("middleName")
    private String middleName;

    @JsonProperty("ancillaryServices")
    private AncillaryServices ancillaryServices;

    @JsonProperty("addressDetails")
    private AddressDetails addressDetails;
    @JsonProperty("title")
    private String title;
    @JsonProperty("birthDate")
    private String birthDate;

    @JsonProperty("contactDetails")
    private List<ContactDetail> contactDetails = new ArrayList<ContactDetail>();

    @JsonProperty("passengerID")
    private String passengerID;

    @JsonProperty("status")
    private String status;

    @JsonProperty("specialRequests")
    private SpecialRequest specialRequests;
    @JsonProperty("quantity")
    private String quantity;

    //TODO: This is required for pax info update. Regenerate it.
    @JsonProperty("userID")
    private String userID;

    @JsonProperty("resGuestRPH")
    private String resGuestRPH;

    @JsonProperty("ticketNumber")
    private String ticketNumber;

    @JsonProperty("seatMap")
    private List<SeatMap> seatMap = new ArrayList<>();

    private final static long serialVersionUID = 8723595014321153927L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public PaxInfo() {
    }

    /**
     * 
     * @param isLeadPax
     * @param firstName
     * @param lastName
     * @param paxType
     * @param paxID
     * @param middleName
     * @param addressDetails
     * @param title
     * @param birthDate
     * @param contactDetails
     */
    public PaxInfo(Boolean isLeadPax, String firstName, String lastName, String paxType, String paxID, String middleName, AddressDetails addressDetails, String title, String birthDate, List<ContactDetail> contactDetails) {
        super();
        this.isLeadPax = isLeadPax;
        this.firstName = firstName;
        this.lastName = lastName;
        this.paxType = paxType;
        this.paxID = paxID;
        this.middleName = middleName;
        this.addressDetails = addressDetails;
        this.title = title;
        this.birthDate = birthDate;
        this.contactDetails = contactDetails;
    }

    public PaxInfo(Boolean isLeadPax, String firstName, String lastName, String paxType, String paxID, String middleName, AncillaryServices ancillaryServices, AddressDetails addressDetails, String title, String birthDate, List<ContactDetail> contactDetails, String passengerID, String status, SpecialRequest specialRequests, String quantity, String userID, String resGuestRPH, String ticketNumber, List<SeatMap> seatMap) {
        this.isLeadPax = isLeadPax;
        this.firstName = firstName;
        this.lastName = lastName;
        this.paxType = paxType;
        this.paxID = paxID;
        this.middleName = middleName;
        this.ancillaryServices = ancillaryServices;
        this.addressDetails = addressDetails;
        this.title = title;
        this.birthDate = birthDate;
        this.contactDetails = contactDetails;
        this.passengerID = passengerID;
        this.status = status;
        this.specialRequests = specialRequests;
        this.quantity = quantity;
        this.userID = userID;
        this.resGuestRPH = resGuestRPH;
        this.ticketNumber = ticketNumber;
        this.seatMap = seatMap;
    }

    @JsonProperty("seatMap")
    public List<SeatMap> getSeatMap() {
        return seatMap;
    }

    @JsonProperty("seatMap")
    public void setSeatMap(List<SeatMap> seatMap) {
        this.seatMap = seatMap;
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

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    @JsonProperty("paxID")
    public String getPaxID() {
        return paxID;
    }

    @JsonProperty("paxID")
    public void setPaxID(String paxID) {
        this.paxID = paxID;
    }

    @JsonProperty("middleName")
    public String getMiddleName() {
        return middleName;
    }

    @JsonProperty("middleName")
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public AncillaryServices getAncillaryServices() {
        return ancillaryServices;
    }

    public void setAncillaryServices(AncillaryServices ancillaryServices) {
        this.ancillaryServices = ancillaryServices;
    }

    @JsonProperty("addressDetails")
    public AddressDetails getAddressDetails() {
        return addressDetails;
    }

    @JsonProperty("addressDetails")
    public void setAddressDetails(AddressDetails addressDetails) {
        this.addressDetails = addressDetails;
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

    public SpecialRequest getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(SpecialRequest specialRequests) {
        this.specialRequests = specialRequests;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getResGuestRPH() {
        return resGuestRPH;
    }

    public void setResGuestRPH(String resGuestRPH) {
        this.resGuestRPH = resGuestRPH;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PaxInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("isLeadPax");
        sb.append('=');
        sb.append(((this.isLeadPax == null)?"<null>":this.isLeadPax));
        sb.append(',');
        sb.append("firstName");
        sb.append('=');
        sb.append(((this.firstName == null)?"<null>":this.firstName));
        sb.append(',');
        sb.append("lastName");
        sb.append('=');
        sb.append(((this.lastName == null)?"<null>":this.lastName));
        sb.append(',');
        sb.append("paxType");
        sb.append('=');
        sb.append(((this.paxType == null)?"<null>":this.paxType));
        sb.append(',');
        sb.append("paxID");
        sb.append('=');
        sb.append(((this.paxID == null)?"<null>":this.paxID));
        sb.append(',');
        sb.append("middleName");
        sb.append('=');
        sb.append(((this.middleName == null)?"<null>":this.middleName));
        sb.append(',');
        sb.append("addressDetails");
        sb.append('=');
        sb.append(((this.addressDetails == null)?"<null>":this.addressDetails));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null)?"<null>":this.title));
        sb.append(',');
        sb.append("birthDate");
        sb.append('=');
        sb.append(((this.birthDate == null)?"<null>":this.birthDate));
        sb.append(',');
        sb.append("contactDetails");
        sb.append('=');
        sb.append(((this.contactDetails == null)?"<null>":this.contactDetails));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.isLeadPax == null)? 0 :this.isLeadPax.hashCode()));
        result = ((result* 31)+((this.firstName == null)? 0 :this.firstName.hashCode()));
        result = ((result* 31)+((this.lastName == null)? 0 :this.lastName.hashCode()));
        result = ((result* 31)+((this.paxType == null)? 0 :this.paxType.hashCode()));
        result = ((result* 31)+((this.paxID == null)? 0 :this.paxID.hashCode()));
        result = ((result* 31)+((this.middleName == null)? 0 :this.middleName.hashCode()));
        result = ((result* 31)+((this.addressDetails == null)? 0 :this.addressDetails.hashCode()));
        result = ((result* 31)+((this.title == null)? 0 :this.title.hashCode()));
        result = ((result* 31)+((this.birthDate == null)? 0 :this.birthDate.hashCode()));
        result = ((result* 31)+((this.contactDetails == null)? 0 :this.contactDetails.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PaxInfo) == false) {
            return false;
        }
        PaxInfo rhs = ((PaxInfo) other);
        return (((((((((((this.isLeadPax == rhs.isLeadPax)||((this.isLeadPax!= null)&&this.isLeadPax.equals(rhs.isLeadPax)))&&((this.firstName == rhs.firstName)||((this.firstName!= null)&&this.firstName.equals(rhs.firstName))))&&((this.lastName == rhs.lastName)||((this.lastName!= null)&&this.lastName.equals(rhs.lastName))))&&((this.paxType == rhs.paxType)||((this.paxType!= null)&&this.paxType.equals(rhs.paxType))))&&((this.paxID == rhs.paxID)||((this.paxID!= null)&&this.paxID.equals(rhs.paxID))))&&((this.middleName == rhs.middleName)||((this.middleName!= null)&&this.middleName.equals(rhs.middleName))))&&((this.addressDetails == rhs.addressDetails)||((this.addressDetails!= null)&&this.addressDetails.equals(rhs.addressDetails))))&&((this.title == rhs.title)||((this.title!= null)&&this.title.equals(rhs.title))))&&((this.birthDate == rhs.birthDate)||((this.birthDate!= null)&&this.birthDate.equals(rhs.birthDate))))&&((this.contactDetails == rhs.contactDetails)||((this.contactDetails!= null)&&this.contactDetails.equals(rhs.contactDetails))));
    }

}
