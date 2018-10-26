package com.coxandkings.travel.operations.resource.outbound.be;

import com.coxandkings.travel.operations.enums.mockBE.PassengerTypeValues;

public class PassengerResponse {
    private String salutation;
    private String name;
    private PassengerTypeValues type;
    private String phoneNumber;
    private String email;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PassengerTypeValues getType() {
        return type;
    }

    public void setType(PassengerTypeValues type) {
        this.type = type;
    }
}
