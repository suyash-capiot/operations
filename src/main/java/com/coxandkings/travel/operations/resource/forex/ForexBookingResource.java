package com.coxandkings.travel.operations.resource.forex;

import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.coxandkings.travel.operations.model.forex.RequestLockObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ForexBookingResource {

    private String id;
    private String _id;
    private String stage;
    private String createdByUserId;
    private String lastModifiedByUserId;
    private String bookRefNo;
    private String requestId;
    private String enquiryId;
    private String requestStatus;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime bookingDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime createdAtTime;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime lastModifiedAtTime;

    private String clientName;

    private String travelCountry;
    private String travelStartDate;
    private String travelEndDate;

    private String approverToDoTaskId;
    private String approverRemark;

    private RequestLockObject lock;

    @JsonManagedReference
    private Set<ForexPassengerResource> forexPassengerSet;
    private Set<ForexIndentResource> indentSet;

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

    public Set<ForexPassengerResource> getForexPassengerSet() {
        return forexPassengerSet;
    }

    public void setForexPassengerSet(Set<ForexPassengerResource> forexPassengerSet) {
        this.forexPassengerSet = forexPassengerSet;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(String enquiryId) {
        this.enquiryId = enquiryId;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public ZonedDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(ZonedDateTime bookingDate) {
        this.bookingDate = bookingDate;
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

    public String getApproverRemark() {
        return approverRemark;
    }

    public void setApproverRemark(String approverRemark) {
        this.approverRemark = approverRemark;
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

    public Set<ForexIndentResource> getIndentSet() {
        return indentSet;
    }

    public void setIndentSet(Set<ForexIndentResource> indentSet) {
        this.indentSet = indentSet;
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
