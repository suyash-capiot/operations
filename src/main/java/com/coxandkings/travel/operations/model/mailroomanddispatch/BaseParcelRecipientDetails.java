package com.coxandkings.travel.operations.model.mailroomanddispatch;

import com.coxandkings.travel.operations.enums.mailroomanddispatch.RecipientDetailUserType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@JsonIgnoreProperties( {"hibernateLazyInitializer" , "handler"} )
@Entity
@JsonPropertyOrder( {"baseParcelRecipientDetails"} )
@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true )
@JsonSubTypes( {
        @JsonSubTypes.Type(value = ParcelInternalRecipient.class, name = "INTERNAL"),
        @JsonSubTypes.Type(value = ParcelExternalRecipient.class, name = "EXTERNAL"),

} )
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
public class BaseParcelRecipientDetails implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="base_parcel_recipient_id")
    private String id;
    @Enumerated
    private RecipientDetailUserType type;
    private String emailId;
    private String country;
    private String city;
    private String postalCode;
    private String address;
    private String landmark;
    private String extensionNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RecipientDetailUserType getType() {
        return type;
    }

    public void setType(RecipientDetailUserType type) {
        this.type = type;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getExtensionNo() {
        return extensionNo;
    }

    public void setExtensionNo(String extensionNo) {
        this.extensionNo = extensionNo;
    }
}
