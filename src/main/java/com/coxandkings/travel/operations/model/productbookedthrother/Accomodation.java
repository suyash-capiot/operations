package com.coxandkings.travel.operations.model.productbookedthrother;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "other_product_info_Accomodation")
public class Accomodation extends Attribute
{

    private String accomodationName;
    private String address;
    private String postalCode;
    private String country;
    private String city;
    private String location;


    public String getAccomodationName() {
        return accomodationName;
    }

    public void setAccomodationName(String accomodationName) {
        this.accomodationName = accomodationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
