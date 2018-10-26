package com.coxandkings.travel.operations.model.forex;

import com.coxandkings.travel.operations.enums.forex.RequestStatus;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Set;

/*
Each Forex booking can have multiple passengers.
 */
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Table(name = "forex_booking_details")
public class ForexBooking {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Transient
    private String _id;

    @Transient
    private String stage;

    @Column(name = "created_by_user_id")
    @CreatedBy
    private String createdByUserId;

    @Column(name = "last_modified_by_user_id")
    @LastModifiedBy
    private String lastModifiedByUserId;

    @Column
    private String requestId;
    @Column
    private String enquiryId;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "booking_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime bookingDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "created_at_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAtTime;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "last_modified_at_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime lastModifiedAtTime;

    private String bookRefNo;

    private String travelCountry;
    private String travelStartDate;
    private String travelEndDate;

    private String clientName;

    @Column(name = "approverToDoTaskId")
    private String approverToDoTaskId;

    private String approverRemark;

    @OneToOne(mappedBy = "forexBooking", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private RequestLockObject lock;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "forex", orphanRemoval = true)
    @JsonManagedReference
    private Set<ForexPassenger> forexPassengerSet;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "forexBooking", orphanRemoval = true)
    private Set<ForexIndent> indentSet;

    //BTQ forms
    @Column(name = "document_ids")
    private ArrayList<String> documentIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    public void setLastModifiedByUserId(String lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }

    public String getBookRefNo() {
        return bookRefNo;
    }

    public void setBookRefNo(String bookRefNo) {
        this.bookRefNo = bookRefNo;
    }

    public String getTravelCountry() {
        return travelCountry;
    }

    public void setTravelCountry(String travelCountry) {
        this.travelCountry = travelCountry;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public ZonedDateTime getCreatedAtTime() {
        return createdAtTime;
    }

    public void setCreatedAtTime(ZonedDateTime createdAtTime) {
        this.createdAtTime = createdAtTime;
    }

    public ZonedDateTime getLastModifiedAtTime() {
        return lastModifiedAtTime;
    }

    public void setLastModifiedAtTime(ZonedDateTime lastModifiedAtTime) {
        this.lastModifiedAtTime = lastModifiedAtTime;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Set<ForexPassenger> getForexPassengerSet() {
        return forexPassengerSet;
    }

    public void setForexPassengerSet(Set<ForexPassenger> forexPassengerSet) {
        this.forexPassengerSet = forexPassengerSet;
    }

    public String getApproverRemark() {
        return approverRemark;
    }

    public void setApproverRemark(String approverRemark) {
        this.approverRemark = approverRemark;
    }

    public String getTravelStartDate() {
        return travelStartDate;
    }

    public void setTravelStartDate(String travelStartDate) {
        this.travelStartDate = travelStartDate;
    }

    public String getTravelEndDate() {
        return travelEndDate;
    }

    public void setTravelEndDate(String travelEndDate) {
        this.travelEndDate = travelEndDate;
    }

    public String getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(String enquiryId) {
        this.enquiryId = enquiryId;
    }

    public ZonedDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(ZonedDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Set<ForexIndent> getIndentSet() {
        return indentSet;
    }

    public void setIndentSet(Set<ForexIndent> indentSet) {
        this.indentSet = indentSet;
    }

    public ArrayList<String> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(ArrayList<String> documentIds) {
        this.documentIds = documentIds;
    }

    public String getApproverToDoTaskId() {
        return approverToDoTaskId;
    }

    public void setApproverToDoTaskId(String approverToDoTaskId) {
        this.approverToDoTaskId = approverToDoTaskId;
    }

    public RequestLockObject getLock() {
        return lock;
    }

    public void setLock(RequestLockObject lock) {
        this.lock = lock;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
}
