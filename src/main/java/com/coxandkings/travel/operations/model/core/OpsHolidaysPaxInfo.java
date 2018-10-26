package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsHolidaysPaxInfo {
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
    @JsonProperty("resGuestRPH")
    private String resGuestRPH;
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
    @JsonProperty("mealInfo")
    private OpsMealInfo mealInfo;
    @JsonProperty("ancillaryServices")
    private OpsAncillaryServices ancillaryServices;
    @JsonProperty("priceDetails")
    private OpsHolidaysPriceDetails priceDetails;
    //Added for forex
    @JsonProperty("isForexRequired")
    private Boolean isForexRequired;


    public OpsHolidaysPaxInfo() {
    }

    public Boolean getIsLeadPax() {
        return isLeadPax;
    }

    public void setIsLeadPax(Boolean isLeadPax) {
        this.isLeadPax = isLeadPax;
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

    public OpsMealInfo getMealInfo() {
        return mealInfo;
    }

    public void setMealInfo(OpsMealInfo mealInfo) {
        this.mealInfo = mealInfo;
    }

    public OpsHolidaysPriceDetails getPriceDetails() {
        return priceDetails;
    }

    public void setPriceDetails(OpsHolidaysPriceDetails priceDetails) {
        this.priceDetails = priceDetails;
    }

    public OpsAncillaryServices getAncillaryServices() {
        return ancillaryServices;
    }

    public void setAncillaryServices(OpsAncillaryServices ancillaryServices) {
        this.ancillaryServices = ancillaryServices;
    }

    public String getResGuestRPH() {
        return resGuestRPH;
    }

    public void setResGuestRPH(String resGuestRPH) {
        this.resGuestRPH = resGuestRPH;
    }

    public Boolean getForexRequired() {
        return isForexRequired;
    }

    public void setForexRequired(Boolean forexRequired) {
        isForexRequired = forexRequired;
    }

}
