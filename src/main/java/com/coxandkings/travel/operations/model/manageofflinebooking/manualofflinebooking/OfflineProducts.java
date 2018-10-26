package com.coxandkings.travel.operations.model.manageofflinebooking.manualofflinebooking;

import com.coxandkings.travel.operations.model.BaseModel;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@TypeDefs({@TypeDef(name = "StringJsonObject", typeClass = StringJsonUserType.class)})
public class OfflineProducts extends BaseModel implements Serializable{

    @Column
    @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "java.lang.String")})
    private String clientDetails;

    @Column
    @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "java.lang.String")})
    private String paymentDetails;

    @Column
    @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "java.lang.String")})
    private String productDetails;

    @Column
    @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "java.lang.String")})
    private String travelAndPassengerDetails;

    @Column
    @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "java.lang.String")})
    private String lockDetails;

    public String getLockDetails() {
        return lockDetails;
    }

    public void setLockDetails(String lockDetails) {
        this.lockDetails = lockDetails;
    }

    //    @OneToOne
//    private OfflineSearch offlineSearch;

//    @OneToOne(mappedBy = "offlineProducts", cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY, optional = false)
//    private OfflineSearch offlineSearch;
//
//    public OfflineSearch getOfflineSearch() {
//        return offlineSearch;
//    }
//
//    public void setOfflineSearch(OfflineSearch offlineSearch) {
//        this.offlineSearch = offlineSearch;
//    }

    @Column(name = "bookRefNumber")
    private String bookRefNumber;

    @Column(name = "bookingStatus")
    private String bookingStatus;

    @Column(name = "isDeleted")
    private Boolean isDeleted;

    @Transient
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getBookRefNumber() {
        return bookRefNumber;
    }

    public void setBookRefNumber(String bookRefNumber) {
        this.bookRefNumber = bookRefNumber;
    }

//    public OfflineSearch getOfflineSearch() {
//        return offlineSearch;
//    }
//
//    public void setOfflineSearch(OfflineSearch offlineSearch) {
//        this.offlineSearch = offlineSearch;
//    }

//

    public String getClientDetails() {
        return clientDetails;
    }

    public void setClientDetails(String clientDetails) {
        this.clientDetails = clientDetails;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public String getTravelAndPassengerDetails() {
        return travelAndPassengerDetails;
    }

    public void setTravelAndPassengerDetails(String travelAndPassengerDetails) {
        this.travelAndPassengerDetails = travelAndPassengerDetails;
    }
}
