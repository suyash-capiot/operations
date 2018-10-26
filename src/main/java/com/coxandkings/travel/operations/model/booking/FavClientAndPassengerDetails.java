package com.coxandkings.travel.operations.model.booking;

import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class FavClientAndPassengerDetails extends BaseModel {

    @Column(name = "fav_id")
    private String favId;

    @Column(name = "client_type")
    private String clientType;

    @Column(name = "client_category_id")
    private String clientCategoryId;

    @Column(name = "client_sub_category_id")
    private String clientSubCategoryId;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "passenger_name")
    private String passengerName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email_id")
    private String emailId;

    public String getFavId() {
        return favId;
    }

    public void setFavId(String favId) {
        this.favId = favId;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientCategoryId() {
        return clientCategoryId;
    }

    public void setClientCategoryId(String clientCategoryId) {
        this.clientCategoryId = clientCategoryId;
    }

    public String getClientSubCategoryId() {
        return clientSubCategoryId;
    }

    public void setClientSubCategoryId(String clientSubCategoryId) {
        this.clientSubCategoryId = clientSubCategoryId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
