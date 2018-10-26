package com.coxandkings.travel.operations.model.reconfirmation.client;


import com.coxandkings.travel.operations.config.ZonedDateTimeConverter;
import com.coxandkings.travel.operations.enums.reconfirmation.ClientReconfirmationStatus;
import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationBookingAttribute;
import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table( name = "ClientReconfirmation_details" )
public class ClientReconfirmationDetails extends BaseModel implements Serializable {

//    @Id
//    @GenericGenerator( name = "idGen", strategy = "com.coxandkings.travel.operations.model.reconfirmation.ReferenceCodeGenerator" )
//    @GeneratedValue( generator = "idGen" )
//    @Column( name = "id" )
//    private String id;

    //TODO: BR 322 - If user triggers the sending of the reconfirmation to client /supplier
    // TODO : before the cut off date then sys should not auto send the reconfirmation  when the cut off dat is reached.
    @Column( name = "isReconfirmationSentToCustomer" )
    private boolean isReconfirmationSentToCustomer;

    @Column( name = "bookRefNo" )
    private String bookRefNo;

    @Column( name = "orderID" )
    private String orderID;

    @Column( name = "isReconfirmationSentToClient" )
    private boolean isReconfirmationSentToClient;

    @Column( name = "productName" )
    private String productName;

    @Convert( converter = ZonedDateTimeConverter.class )
    @Column( name = "clientOrCustomerReconfirmationDate" )
    private ZonedDateTime clientOrCustomerReconfirmationDate;

    @Column( name = "bookingAttribute" )
    @Enumerated( EnumType.STRING )
    private ReconfirmationBookingAttribute bookingAttribute;

    @Column( name = "rejectedDueToNoResponse" )
    private boolean rejectedDueToNoResponse;

    @Convert( converter = ZonedDateTimeConverter.class )
    @Column( name = "reconfirmationOnHoldUntilDate" )
    private ZonedDateTime reconfirmationOnHoldUntilDate;

    @Column( name = "remarks" )
    private String remarks;


    @Column( name = "isRejectedByCustomer" )
    private boolean isRejectedByCustomer;

    @Enumerated( EnumType.STRING )
    private ClientReconfirmationStatus clientReconfirmationStatus;

    @Column( name = "supplierEmailId" )
    private String supplierEmailId;

    @Column( name = "clientEmailId" )
    private String clientEmailId;

    @Column( name = "template" )
    private String template;

    @Column( name = "numberOfTimesReconfirmationSent" )
    private Integer numberOfTimesReconfirmationSent;

    @Convert( converter = ZonedDateTimeConverter.class )
    @Column( name = "reconfirmationCutOffDate" )
    private ZonedDateTime reconfirmationCutOffDate;

    @Convert( converter = ZonedDateTimeConverter.class )
    @Column( name = "travelDate" )
    private ZonedDateTime travelDate;

    @Column( name = "reconfirmedBy" )
    private String reconfirmedBy;

    @Column( name = "city" )
    private String city;

    @Column( name = "country" )
    private String country;

    @Column( name = "toDoTaskAlternateOptionID" )
    private String toDoTaskAlternateOptionID;

    @Column( name = "toDoTaskCancellationID" )
    private String toDoTaskCancellationID;

    @Column( name = "handoverDocumentID" )
    private String handoverDocumentID;

    //TODO USE THIS ATTRIBUTE WHEN IMPLEMENTING THE BATCH JOB, IF IT IS FALSE THEN DO BATCH JOB OTHEWISE DONT DO reconfiratmions.

    @Column( name = "autoReconfirmation" )
    private boolean autoReconfirmation;

    @Column( name = "hash" )
    private String hash;

    @Column( name = "clientReplyDate" )
    private ZonedDateTime clientReplyDate;

    @Column( name = "expiredLink" )
    private boolean expiredLink;

    public boolean isAutoReconfirmation( ) {
        return autoReconfirmation;
    }

    public void setAutoReconfirmation( boolean autoReconfirmation ) {
        this.autoReconfirmation = autoReconfirmation;
    }

    public String getHandoverDocumentID( ) {
        return handoverDocumentID;
    }

    public void setHandoverDocumentID( String handoverDocumentID ) {
        this.handoverDocumentID = handoverDocumentID;
    }

    public String getToDoTaskAlternateOptionID( ) {
        return toDoTaskAlternateOptionID;
    }

    public void setToDoTaskAlternateOptionID( String toDoTaskAlternateOptionID ) {
        this.toDoTaskAlternateOptionID = toDoTaskAlternateOptionID;
    }

    public String getToDoTaskCancellationID( ) {
        return toDoTaskCancellationID;
    }

    public void setToDoTaskCancellationID( String toDoTaskCancellationID ) {
        this.toDoTaskCancellationID = toDoTaskCancellationID;
    }

    public ZonedDateTime getTravelDate( ) {
        return travelDate;
    }

    public void setTravelDate( ZonedDateTime travelDate ) {
        this.travelDate = travelDate;
    }


//    public String getId( ) {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public boolean isReconfirmationSentToCustomer( ) {
        return isReconfirmationSentToCustomer;
    }

    public void setReconfirmationSentToCustomer( boolean reconfirmationSentToCustomer ) {
        isReconfirmationSentToCustomer = reconfirmationSentToCustomer;
    }

    public boolean isReconfirmationSentToClient( ) {
        return isReconfirmationSentToClient;
    }

    public void setReconfirmationSentToClient( boolean reconfirmationSentToClient ) {
        isReconfirmationSentToClient = reconfirmationSentToClient;
    }

    public String getProductName( ) {
        return productName;
    }

    public void setProductName( String productName ) {
        this.productName = productName;
    }

    public ReconfirmationBookingAttribute getBookingAttribute( ) {
        return bookingAttribute;
    }

    public void setBookingAttribute( ReconfirmationBookingAttribute bookingAttribute ) {
        this.bookingAttribute = bookingAttribute;
    }

    public boolean isRejectedDueToNoResponse( ) {
        return rejectedDueToNoResponse;
    }

    public void setRejectedDueToNoResponse( boolean rejectedDueToNoResponse ) {
        this.rejectedDueToNoResponse = rejectedDueToNoResponse;
    }

    public String getRemarks( ) {
        return remarks;
    }

    public void setRemarks( String remarks ) {
        this.remarks = remarks;
    }

    public String getBookRefNo( ) {
        return bookRefNo;
    }

    public void setBookRefNo( String bookRefNo ) {
        this.bookRefNo = bookRefNo;
    }

    public String getOrderID( ) {
        return orderID;
    }

    public void setOrderID( String orderID ) {
        this.orderID = orderID;
    }

    public boolean isRejectedByCustomer( ) {
        return isRejectedByCustomer;
    }

    public void setRejectedByCustomer( boolean rejectedByCustomer ) {
        isRejectedByCustomer = rejectedByCustomer;
    }

    public ClientReconfirmationStatus getClientReconfirmationStatus( ) {
        return clientReconfirmationStatus;
    }

    public void setClientReconfirmationStatus( ClientReconfirmationStatus clientReconfirmationStatus ) {
        this.clientReconfirmationStatus = clientReconfirmationStatus;
    }

    public String getSupplierEmailId( ) {
        return supplierEmailId;
    }

    public void setSupplierEmailId( String supplierEmailId ) {
        this.supplierEmailId = supplierEmailId;
    }

    public String getClientEmailId( ) {
        return clientEmailId;
    }

    public void setClientEmailId( String clientEmailId ) {
        this.clientEmailId = clientEmailId;
    }

    public String getTemplate( ) {
        return template;
    }

    public void setTemplate( String template ) {
        this.template = template;
    }

    public ZonedDateTime getClientOrCustomerReconfirmationDate( ) {
        return clientOrCustomerReconfirmationDate;
    }

    public void setClientOrCustomerReconfirmationDate( ZonedDateTime clientOrCustomerReconfirmationDate ) {
        this.clientOrCustomerReconfirmationDate = clientOrCustomerReconfirmationDate;
    }

    public ZonedDateTime getReconfirmationOnHoldUntilDate( ) {
        return reconfirmationOnHoldUntilDate;
    }

    public void setReconfirmationOnHoldUntilDate( ZonedDateTime reconfirmationOnHoldUntilDate ) {
        this.reconfirmationOnHoldUntilDate = reconfirmationOnHoldUntilDate;
    }

    public Integer getNumberOfTimesReconfirmationSent( ) {
        return numberOfTimesReconfirmationSent;
    }

    public void setNumberOfTimesReconfirmationSent( Integer numberOfTimesReconfirmationSent ) {
        this.numberOfTimesReconfirmationSent = numberOfTimesReconfirmationSent;
    }

    public ZonedDateTime getReconfirmationCutOffDate( ) {
        return reconfirmationCutOffDate;
    }

    public void setReconfirmationCutOffDate( ZonedDateTime reconfirmationCutOffDate ) {
        this.reconfirmationCutOffDate = reconfirmationCutOffDate;
    }

    public String getReconfirmedBy( ) {
        return reconfirmedBy;
    }

    public void setReconfirmedBy( String reconfirmedBy ) {
        this.reconfirmedBy = reconfirmedBy;
    }

    public String getCity( ) {
        return city;
    }

    public void setCity( String city ) {
        this.city = city;
    }

    public String getCountry( ) {
        return country;
    }

    public void setCountry( String country ) {
        this.country = country;
    }

    public String getHash( ) {
        return hash;
    }

    public void setHash( String hash ) {
        this.hash = hash;
    }

    public ZonedDateTime getClientReplyDate( ) {
        return clientReplyDate;
    }

    public void setClientReplyDate( ZonedDateTime clientReplyDate ) {
        this.clientReplyDate = clientReplyDate;
    }

    public boolean isExpiredLink( ) {
        return expiredLink;
    }

    public void setExpiredLink( boolean expiredLink ) {
        this.expiredLink = expiredLink;
    }
}
