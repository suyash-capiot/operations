package com.coxandkings.travel.operations.model.core;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsPaxParticular implements Serializable  {

    @JsonProperty("paxID")
    private String paxID;

    @JsonProperty("specialRequest")
    private OpsSpecialRequest specialRequest;
    
    @JsonProperty("passengerName")
    private String passengerName;
    
    @JsonProperty("dateOfBirth")
    private String dateOfBirth;

    @JsonProperty("paxKey")
    private String paxKey;

    @JsonProperty("seatnumber")
    private String seatnumber;


    @JsonProperty("isLeadPax")
    private Boolean isLeadPax;

    @JsonProperty("addressDetails")
    private OpsAddressDetails addressDetails;
    @JsonProperty("title")
    private String title;
    @JsonProperty("birthDate")
    private String birthDate;

    @JsonProperty("paxType")
    private String paxType;

    @JsonProperty("contactDetails")
    private List<OpsContactDetails> contactDetails = new ArrayList<OpsContactDetails>();


    private final static long serialVersionUID = 6441051655583572667L;

    private OpsSpecialRequestInfo mealInfo;

    public OpsPaxParticular() {
    }

    
    public String getPassengerName() {
		return passengerName;
	}


	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}


	public String getDateOfBirth() {
		return dateOfBirth;
	}


	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}


	public OpsSpecialRequest getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(OpsSpecialRequest specialRequest) {
        this.specialRequest = specialRequest;
    }

    public String getPaxKey() {
        return paxKey;
    }

    public void setPaxKey(String paxKey) {
        this.paxKey = paxKey;
    }

    public String getSeatnumber() {
        return seatnumber;
    }

    public void setSeatnumber(String seatnumber) {
        this.seatnumber = seatnumber;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public OpsSpecialRequestInfo getMealInfo() {
        return mealInfo;
    }

    public void setMealInfo(OpsSpecialRequestInfo mealInfo) {
        this.mealInfo = mealInfo;
    }

    public Boolean getLeadPax() {
        return isLeadPax;
    }

    public void setLeadPax(Boolean leadPax) {
        isLeadPax = leadPax;
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
}
