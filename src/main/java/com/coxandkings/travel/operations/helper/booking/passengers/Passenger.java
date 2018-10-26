package com.coxandkings.travel.operations.helper.booking.passengers;

import com.coxandkings.travel.operations.helper.booking.product.Product;
import com.coxandkings.travel.operations.helper.booking.product.Status;
import com.coxandkings.travel.operations.helper.enums.PassengerTypeValues;

import java.util.Map;

public class Passenger {
    Integer id;
    private Boolean leadPassenger;
    private String salutation;
    private String passengerName;
    private String phoneNumber;
    private String email;
    private Long dob;
    private String mealPreference;
    private String specialReq;
    private boolean refundable;
    private Long ammendCancelDateTime;
    private String inventory;
    private PassengerTypeValues passengerType;
    private Map<String, String> attributes;
    private Map<String, String> otherDetails;
    private PriceDetails priceDetails;
    private Status passengerStatus;
    private Product product;
    private String createdByUserId;
    private Long createdTime;
    private String lastModifiedByUserId;
    private Long lastModifiedTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Long getDob() {
        return dob;
    }

    public void setDob(Long dob) {
        this.dob = dob;
    }

    public String getMealPreference() {
        return mealPreference;
    }

    public void setMealPreference(String mealPreference) {
        this.mealPreference = mealPreference;
    }

    public String getSpecialReq() {
        return specialReq;
    }

    public void setSpecialReq(String specialReq) {
        this.specialReq = specialReq;
    }

    public boolean isRefundable() {
        return refundable;
    }

    public void setRefundable(boolean refundable) {
        this.refundable = refundable;
    }

    public Long getAmmendCancelDateTime() {
        return ammendCancelDateTime;
    }

    public void setAmmendCancelDateTime(Long ammendCancelDateTime) {
        this.ammendCancelDateTime = ammendCancelDateTime;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public PassengerTypeValues getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(PassengerTypeValues passengerType) {
        this.passengerType = passengerType;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Map<String, String> getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(Map<String, String> otherDetails) {
        this.otherDetails = otherDetails;
    }

    public PriceDetails getPriceDetails() {
        return priceDetails;
    }

    public void setPriceDetails(PriceDetails priceDetails) {
        this.priceDetails = priceDetails;
    }

    public Status getPassengerStatus() {
        return passengerStatus;
    }

    public void setPassengerStatus(Status passengerStatus) {
        this.passengerStatus = passengerStatus;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    public void setLastModifiedByUserId(String lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
}
