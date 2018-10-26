package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsAccommodationPaxInfo implements Serializable {

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

    @JsonProperty("addressDetails")
    private OpsAddressDetails addressDetails;

    @JsonProperty("title")
    private String title;

    @JsonProperty("birthDate")
    private String birthDate;

    @JsonProperty("contactDetails")
    private List<OpsContactDetails> contactDetails;

    @JsonProperty("companyCancelCharges")
    private String companyCancelCharges;

    @JsonProperty("supplierCancelCharges")
    private String supplierCancelCharges;

    @JsonProperty("companyCancelChargesCurrencyCode")
    private String companyCancelChargesCurrencyCode;

    @JsonProperty("supplierCancelChargesCurrencyCode")
    private String supplierCancelChargesCurrencyCode;

    @JsonProperty("companyAmendCharges")
    private String companyAmendCharges;

    @JsonProperty("supplierAmendCharges")
    private String supplierAmendCharges;

    @JsonProperty("companyAmendChargesCurrencyCode")
    private String companyAmendChargesCurrencyCode;

    @JsonProperty("supplierAmendChargesCurrencyCode")
    private String supplierAmendChargesCurrencyCode;

    @JsonProperty("status")
    private String status;

    public OpsAccommodationPaxInfo() {
    }

    public Boolean getLeadPax() {
        return isLeadPax;
    }

    public void setLeadPax(Boolean leadPax) {
        isLeadPax = leadPax;
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

    public String getPaxID() {
        return paxID;
    }

    public void setPaxID(String paxID) {
        this.paxID = paxID;
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

    public String getCompanyCancelCharges() {
        return companyCancelCharges;
    }

    public void setCompanyCancelCharges(String companyCancelCharges) {
        this.companyCancelCharges = companyCancelCharges;
    }

    public String getSupplierCancelCharges() {
        return supplierCancelCharges;
    }

    public void setSupplierCancelCharges(String supplierCancelCharges) {
        this.supplierCancelCharges = supplierCancelCharges;
    }

    public String getCompanyCancelChargesCurrencyCode() {
        return companyCancelChargesCurrencyCode;
    }

    public void setCompanyCancelChargesCurrencyCode(String companyCancelChargesCurrencyCode) {
        this.companyCancelChargesCurrencyCode = companyCancelChargesCurrencyCode;
    }

    public String getSupplierCancelChargesCurrencyCode() {
        return supplierCancelChargesCurrencyCode;
    }

    public void setSupplierCancelChargesCurrencyCode(String supplierCancelChargesCurrencyCode) {
        this.supplierCancelChargesCurrencyCode = supplierCancelChargesCurrencyCode;
    }

    public String getCompanyAmendCharges() {
        return companyAmendCharges;
    }

    public void setCompanyAmendCharges(String companyAmendCharges) {
        this.companyAmendCharges = companyAmendCharges;
    }

    public String getSupplierAmendCharges() {
        return supplierAmendCharges;
    }

    public void setSupplierAmendCharges(String supplierAmendCharges) {
        this.supplierAmendCharges = supplierAmendCharges;
    }

    public String getCompanyAmendChargesCurrencyCode() {
        return companyAmendChargesCurrencyCode;
    }

    public void setCompanyAmendChargesCurrencyCode(String companyAmendChargesCurrencyCode) {
        this.companyAmendChargesCurrencyCode = companyAmendChargesCurrencyCode;
    }

    public String getSupplierAmendChargesCurrencyCode() {
        return supplierAmendChargesCurrencyCode;
    }

    public void setSupplierAmendChargesCurrencyCode(String supplierAmendChargesCurrencyCode) {
        this.supplierAmendChargesCurrencyCode = supplierAmendChargesCurrencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsAccommodationPaxInfo paxInfo = (OpsAccommodationPaxInfo) o;
        return Objects.equals(isLeadPax, paxInfo.isLeadPax) &&
                Objects.equals(firstName, paxInfo.firstName) &&
                Objects.equals(lastName, paxInfo.lastName) &&
                Objects.equals(paxType, paxInfo.paxType) &&
                Objects.equals(paxID, paxInfo.paxID) &&
                Objects.equals(middleName, paxInfo.middleName) &&
                Objects.equals(addressDetails, paxInfo.addressDetails) &&
                Objects.equals(title, paxInfo.title) &&
                Objects.equals(birthDate, paxInfo.birthDate) &&
                Objects.equals(contactDetails, paxInfo.contactDetails);
    }

    @Override
    public int hashCode() {

        return Objects.hash(isLeadPax, firstName, lastName, paxType, paxID, middleName, addressDetails, title, birthDate, contactDetails);
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	


}
