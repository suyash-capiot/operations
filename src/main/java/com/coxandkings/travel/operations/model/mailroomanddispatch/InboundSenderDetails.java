package com.coxandkings.travel.operations.model.mailroomanddispatch;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="inbound_sender_details")
public class InboundSenderDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="inbound_sender_id")
    private String id;
    private String senderName;
    private String address;
    private String country;
    private String city;
    private String postalCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InboundSenderDetails that = (InboundSenderDetails) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(senderName, that.senderName) &&
                Objects.equals(address, that.address) &&
                Objects.equals(country, that.country) &&
                Objects.equals(city, that.city) &&
                Objects.equals(postalCode, that.postalCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, senderName, address, country, city, postalCode);
    }

    @Override
    public String toString() {
        return "InboundSenderDetails{" +
                "id='" + id + '\'' +
                ", senderName='" + senderName + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
