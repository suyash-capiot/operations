package com.coxandkings.travel.operations.model.reconfirmation.supplier;


import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationBookingAttribute;
import com.coxandkings.travel.operations.enums.reconfirmation.SupplierReconfirmationStatus;
import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "Supplier_Reconfirmation_details")
public class SupplierReconfirmationDetails extends BaseModel implements Serializable {

//    @Id
//    @GenericGenerator( name = "idGen", strategy = "com.coxandkings.travel.operations.model.reconfirmation.ReferenceCodeGenerator" )
//    @GeneratedValue( generator = "idGen" )
//    @Column( name = "id" )
//    private String id;

    @Column(name = "bookRefNo")
    private String bookRefNo;

    @Column(name = "supplierId")
    private String supplierId;

    //TODO - Use OrderID instead of ProductResource ID //done
    @Column(name = "orderID")
    private String orderID;

    //TODO: BR 322 - If user triggers the sending of the reconfirmation to client /supplier
    // TODO : before the cut off date then sys should not auto send the reconfirmation  when the cut off dat is reached.

    @Column(name = "reconfirmationSentToServiceProvider")
    private boolean reconfirmationSentToServiceProvider;

    @Column(name = "reconfirmationSentToSupplier")
    private boolean reconfirmationSentToSupplier;

    @Column(name = "clientReconfirmationID")
    private String clientReconfirmationID;

    @Column(name = "reconfirmedBy")
    private String reconfirmedBy;

    @Column(name = "supplierReconfirmationDate")
    private ZonedDateTime supplierReconfirmationDate;

    @Column(name = "supplierName")
    private String supplierName;

    @Column(name = "productName")
    private String productName;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "bookingAttribute")
    @Enumerated(EnumType.STRING)
    private ReconfirmationBookingAttribute bookingAttribute;

    @Enumerated(EnumType.STRING)
    private SupplierReconfirmationStatus supplierReconfirmationStatus;

    @Column(name = "rejectedDueToNoResponse")
    private boolean rejectedDueToNoResponse;

    @Column(name = "reconfirmationOnHoldUntilDate")
    private ZonedDateTime reconfirmationOnHoldUntilDate;

    @Column(name = "remarks")
    private String remarks;

//    @Column(name = "isRejectedBySupplier")
//    private boolean isRejectedBySupplier;

    @Column(name = "productConfirmationNumber")
    private String productConfirmationNumber;

    @Column(name = "supplierEmailId")
    private String supplierEmailId;

    @Column(name = "clientEmailId")
    private String clientEmailId;

    @Column(name = "template")
    private String template;

    @Column(name = "isReconfirmationSent")
    private boolean reconfirmationSent;

    @Column(name = "numberOfTimesReconfirmationSent")
    private Integer numberOfTimesReconfirmationSent;

    @Column(name = "reconfirmationCutOffDate")
    private ZonedDateTime reconfirmationCutOffDate;

    @Column(name = "travelDate")
    private ZonedDateTime travelDate;

    @Column(name = "clientReconfirmationDate")
    private ZonedDateTime clientReconfirmationDate;

    @Column(name = "toDoTaskAlternateOptionID")
    private String toDoTaskAlternateOptionID;

    @Column(name = "toDoTaskCancellationID")
    private String toDoTaskCancellationID;


//    @Column(name = "toDoTaskFollowUpID")
//    private String toDoTaskFollowUpID;

    @Column(name = "handoverDocumentID")
    private String handoverDocumentID;

    @Column(name = "autoReconfirmation")
    private boolean autoReconfirmation;

    @Column(name = "reconfirmationConfiguredFor")
    private String reconfirmationConfiguredFor;

    @Column(name = "hash")
    private String hash;

    @Column(name = "clientReplyDate")
    private ZonedDateTime clientReplyDate;

    @Column(name = "supplierReplyDate")
    private ZonedDateTime supplierReplyDate;

    @Column(name = "expiredLink")
    private boolean expiredLink;


    public String getReconfirmationConfiguredFor() {
        return reconfirmationConfiguredFor;
    }

    public void setReconfirmationConfiguredFor(String reconfirmationConfiguredFor) {
        this.reconfirmationConfiguredFor = reconfirmationConfiguredFor;
    }

    public boolean isAutoReconfirmation() {
        return autoReconfirmation;
    }

    public void setAutoReconfirmation(boolean autoReconfirmation) {
        this.autoReconfirmation = autoReconfirmation;
    }

    public String getHandoverDocumentID() {
        return handoverDocumentID;
    }

    public void setHandoverDocumentID(String handoverDocumentID) {
        this.handoverDocumentID = handoverDocumentID;
    }

    public String getToDoTaskAlternateOptionID() {
        return toDoTaskAlternateOptionID;
    }

    public void setToDoTaskAlternateOptionID(String toDoTaskAlternateOptionID) {
        this.toDoTaskAlternateOptionID = toDoTaskAlternateOptionID;
    }

    public String getToDoTaskCancellationID() {
        return toDoTaskCancellationID;
    }

    public void setToDoTaskCancellationID(String toDoTaskCancellationID) {
        this.toDoTaskCancellationID = toDoTaskCancellationID;
    }

    public ZonedDateTime getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(ZonedDateTime travelDate) {
        this.travelDate = travelDate;
    }

    public ZonedDateTime getClientReconfirmationDate() {
        return clientReconfirmationDate;
    }

    public void setClientReconfirmationDate(ZonedDateTime clientReconfirmationDate) {
        this.clientReconfirmationDate = clientReconfirmationDate;
    }

    public ZonedDateTime getReconfirmationCutOffDate() {
        return reconfirmationCutOffDate;
    }

    public void setReconfirmationCutOffDate(ZonedDateTime reconfirmationCutOffDate) {
        this.reconfirmationCutOffDate = reconfirmationCutOffDate;
    }

    public String getClientReconfirmationID() {
        return clientReconfirmationID;
    }

    public void setClientReconfirmationID(String clientReconfirmationID) {
        this.clientReconfirmationID = clientReconfirmationID;
    }

    public boolean isReconfirmationSent() {
        return reconfirmationSent;
    }

    public void setReconfirmationSent(boolean reconfirmationSent) {
        this.reconfirmationSent = reconfirmationSent;
    }

    public String getSupplierEmailId() {
        return supplierEmailId;
    }

    public void setSupplierEmailId(String supplierEmailId) {
        this.supplierEmailId = supplierEmailId;
    }

    public String getReconfirmedBy() {
        return reconfirmedBy;
    }

    public void setReconfirmedBy(String reconfirmedBy) {
        this.reconfirmedBy = reconfirmedBy;
    }


    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ReconfirmationBookingAttribute getBookingAttribute() {
        return bookingAttribute;
    }

    public void setBookingAttribute(ReconfirmationBookingAttribute bookingAttribute) {
        this.bookingAttribute = bookingAttribute;
    }

    public boolean isRejectedDueToNoResponse() {
        return rejectedDueToNoResponse;
    }

    public void setRejectedDueToNoResponse(boolean rejectedDueToNoResponse) {
        this.rejectedDueToNoResponse = rejectedDueToNoResponse;
    }

    public ZonedDateTime getSupplierReconfirmationDate() {
        return supplierReconfirmationDate;
    }

    public void setSupplierReconfirmationDate(ZonedDateTime supplierReconfirmationDate) {
        this.supplierReconfirmationDate = supplierReconfirmationDate;
    }

    public ZonedDateTime getReconfirmationOnHoldUntilDate() {
        return reconfirmationOnHoldUntilDate;
    }

    public void setReconfirmationOnHoldUntilDate(ZonedDateTime reconfirmationOnHoldUntilDate) {
        this.reconfirmationOnHoldUntilDate = reconfirmationOnHoldUntilDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getBookRefNo() {
        return bookRefNo;
    }

    public void setBookRefNo(String bookRefNo) {
        this.bookRefNo = bookRefNo;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getProductConfirmationNumber() {
        return productConfirmationNumber;
    }

    public void setProductConfirmationNumber(String productConfirmationNumber) {
        this.productConfirmationNumber = productConfirmationNumber;
    }

    public SupplierReconfirmationStatus getSupplierReconfirmationStatus() {
        return supplierReconfirmationStatus;
    }

    public void setSupplierReconfirmationStatus(SupplierReconfirmationStatus supplierReconfirmationStatus) {
        this.supplierReconfirmationStatus = supplierReconfirmationStatus;
    }

    public String getClientEmailId() {
        return clientEmailId;
    }

    public void setClientEmailId(String clientEmailId) {
        this.clientEmailId = clientEmailId;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Integer getNumberOfTimesReconfirmationSent() {
        return numberOfTimesReconfirmationSent;
    }

    public void setNumberOfTimesReconfirmationSent(Integer numberOfTimesReconfirmationSent) {
        this.numberOfTimesReconfirmationSent = numberOfTimesReconfirmationSent;
    }

    public boolean isReconfirmationSentToSupplier() {
        return reconfirmationSentToSupplier;
    }

    public void setReconfirmationSentToSupplier(boolean reconfirmationSentToSupplier) {
        this.reconfirmationSentToSupplier = reconfirmationSentToSupplier;
    }

    public boolean isReconfirmationSentToServiceProvider() {
        return reconfirmationSentToServiceProvider;
    }

    public void setReconfirmationSentToServiceProvider(boolean reconfirmationSentToServiceProvider) {
        this.reconfirmationSentToServiceProvider = reconfirmationSentToServiceProvider;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public ZonedDateTime getClientReplyDate() {
        return clientReplyDate;
    }

    public void setClientReplyDate(ZonedDateTime clientReplyDate) {
        this.clientReplyDate = clientReplyDate;
    }

    public ZonedDateTime getSupplierReplyDate() {
        return supplierReplyDate;
    }

    public void setSupplierReplyDate(ZonedDateTime supplierReplyDate) {
        this.supplierReplyDate = supplierReplyDate;
    }

    public boolean isExpiredLink() {
        return expiredLink;
    }

    public void setExpiredLink(boolean expiredLink) {
        this.expiredLink = expiredLink;
    }

//    public String getToDoTaskFollowUpID() {
//        return toDoTaskFollowUpID;
//    }
//
//    public void setToDoTaskFollowUpID(String toDoTaskFollowUpID) {
//        this.toDoTaskFollowUpID = toDoTaskFollowUpID;
//    }
}
