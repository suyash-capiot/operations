package com.coxandkings.travel.operations.model.bookstopenquiry;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "bookstop_enquiry")
public class BookstopEnquiry implements Serializable{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    @Column(name = "client_customer_id")
    private String client_customer_id ;
    @Column(name = "client_customer_name")
    private String client_customer_name ;
    @Column(name = "emailAddress")
    private String emailAddress ;
    @Column(name = "phoneNumber")
    private String phoneNumber ;
    @Column(name = "address1")
    private String address1 ;
    @Column(name = "address2")
    private String address2 ;
    @Column(name = "zipcode")
    private String zipcode ;
    @Column(name = "reason")
    private String reason ;
    @Column(name = "comments")
    private String comments ;
    @Column(name = "status")
    private String status ;
    @Column(name = "bookstop_id")
    private String bookstopId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient_customer_id() {
        return client_customer_id;
    }

    public void setClient_customer_id(String client_customer_id) {
        this.client_customer_id = client_customer_id;
    }

    public String getClient_customer_name() {
        return client_customer_name;
    }

    public void setClient_customer_name(String client_customer_name) {
        this.client_customer_name = client_customer_name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookstopId() {
        return bookstopId;
    }

    public void setBookstopId(String bookstopId) {
        this.bookstopId = bookstopId;
    }

    @Override
    public String toString() {
        return "BookstopEnquiry{" +
                "client_customer_id='" + client_customer_id + '\'' +
                ", client_customer_name='" + client_customer_name + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", reason='" + reason + '\'' +
                ", comments='" + comments + '\'' +
                ", status='" + status + '\'' +
                ", bookstopId='" + bookstopId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookstopEnquiry that = (BookstopEnquiry) o;
        return Objects.equals(client_customer_id, that.client_customer_id) &&
                Objects.equals(client_customer_name, that.client_customer_name) &&
                Objects.equals(emailAddress, that.emailAddress) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(address1, that.address1) &&
                Objects.equals(address2, that.address2) &&
                Objects.equals(zipcode, that.zipcode) &&
                Objects.equals(reason, that.reason) &&
                Objects.equals(comments, that.comments) &&
                Objects.equals(status, that.status) &&
                Objects.equals(bookstopId, that.bookstopId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(client_customer_id, client_customer_name, emailAddress, phoneNumber, address1, address2, zipcode, reason, comments, status, bookstopId);
    }
}
