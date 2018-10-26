
package com.coxandkings.travel.ext.model.be;

import java.util.List;

public class PaxDetail {

    private String title;
    private String firstName;
    private String middleName;
    private String surname;
    private String gender;
    private String dob;
    private String paxType;
    private List<ContactDetail> contactDetails = null;
    private AddressDetails addressDetails;
    private DocumentDetails documentDetails;
    private SpecialRequests specialRequests;
    private AncillaryServices ancillaryServices;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPaxType() {
        return paxType;
    }

    public void setPaxType(String paxType) {
        this.paxType = paxType;
    }

    public List<ContactDetail> getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(List<ContactDetail> contactDetails) {
        this.contactDetails = contactDetails;
    }

    public AddressDetails getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(AddressDetails addressDetails) {
        this.addressDetails = addressDetails;
    }

    public DocumentDetails getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(DocumentDetails documentDetails) {
        this.documentDetails = documentDetails;
    }

    public SpecialRequests getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(SpecialRequests specialRequests) {
        this.specialRequests = specialRequests;
    }

    public AncillaryServices getAncillaryServices() {
        return ancillaryServices;
    }

    public void setAncillaryServices(AncillaryServices ancillaryServices) {
        this.ancillaryServices = ancillaryServices;
    }

}
