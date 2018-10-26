package com.coxandkings.travel.operations.resource.mailroomMaster;


import com.coxandkings.travel.operations.enums.mailroomanddispatch.DispatchAgainst;
import com.coxandkings.travel.operations.enums.mailroomanddispatch.WorkflowEnums;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.coxandkings.travel.operations.model.mailroomanddispatch.*;
import com.coxandkings.travel.operations.resource.BaseResource;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;
import java.util.List;

public class OutboundDispatchResource extends BaseResource {

    private String id;
    private String dispatchId;
    private String linkInBoudNumber;
    private DispatchAgainst dispatchAgainst;
    private String referenceNumber;
    private String leadPassengerName;
   // private Parcel parcel;
    private List<ContentDetails> contactList;
    private BaseRecipientDetails recipientDetailsList;
    private DispatchSenderDetails dispatchSenderDetails;
   // private PlanCollection planCollection;
   /* @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime dispatchDeliveryDate;
    private String passportNumber;
    private String consigneeName;
    private String consigneeLocation;
    private String statusId;
    private Set<Tracker> tracker;*/
    /*private String barCode;
    private BarCode barcodes;*/
    private OutboundDispatchStatus dispatchStatus;
    private DispatchCostDetails dispatchCostDetails;

    //WorkFlow Implementations
    private String action;
    private WorkflowEnums status;
    private String lastModifiedByUserId;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private ZonedDateTime lastModifiedOn;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private ZonedDateTime createdOn;
    private String _id;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public WorkflowEnums getStatus() {
        return status;
    }

    public void setStatus(WorkflowEnums status) {
        this.status = status;
    }

    public String getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    public void setLastModifiedByUserId(String lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }

    public ZonedDateTime getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(ZonedDateTime lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }

    public DispatchCostDetails getDispatchCostDetails() {
        return dispatchCostDetails;
    }

    public void setDispatchCostDetails(DispatchCostDetails dispatchCostDetails) {
        this.dispatchCostDetails = dispatchCostDetails;
    }

    public String getLinkInBoudNumber() {
        return linkInBoudNumber;
    }

    public void setLinkInBoudNumber(String linkInBoudNumber) {
        this.linkInBoudNumber = linkInBoudNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getLeadPassengerName() {
        return leadPassengerName;
    }

    public void setLeadPassengerName(String leadPassengerName) {
        this.leadPassengerName = leadPassengerName;
    }

    public List<ContentDetails> getContactList() {
        return contactList;
    }

    public void setContactList(List<ContentDetails> contactList) {
        this.contactList = contactList;
    }

    public BaseRecipientDetails getRecipientDetailsList() {
        return recipientDetailsList;
    }

    public void setRecipientDetailsList(BaseRecipientDetails recipientDetailsList) {
        this.recipientDetailsList = recipientDetailsList;
    }

    public DispatchSenderDetails getDispatchSenderDetails() {
        return dispatchSenderDetails;
    }

    public void setDispatchSenderDetails(DispatchSenderDetails dispatchSenderDetails) {
        this.dispatchSenderDetails = dispatchSenderDetails;
    }

    /*public ZonedDateTime getDispatchDeliveryDate() {
        return dispatchDeliveryDate;
    }

    public void setDispatchDeliveryDate(ZonedDateTime dispatchDeliveryDate) {
        this.dispatchDeliveryDate = dispatchDeliveryDate;
    }*/

   /*public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }*/

   /* public PlanCollection getPlanCollection() {
        return planCollection;
    }

    public void setPlanCollection(PlanCollection planCollection) {
        this.planCollection = planCollection;
    }*/

   /* public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeLocation() {
        return consigneeLocation;
    }

    public void setConsigneeLocation(String consigneeLocation) {
        this.consigneeLocation = consigneeLocation;
    }*/

   /* public BarCode getBarcodes() {
        return barcodes;
    }

    public void setBarcodes(BarCode barcodes) {
        this.barcodes = barcodes;
    }*/

    /*public String getDispatchAgainst() {
        return dispatchAgainst;
    }

    public void setDispatchAgainst(String dispatchAgainst) {
        this.dispatchAgainst = dispatchAgainst;
    }*/

    /*public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }*/

    /*public Parcel getParcel() {
        return parcel;
    }

    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }*/

    public DispatchAgainst getDispatchAgainst() {
        return dispatchAgainst;
    }

    public void setDispatchAgainst(DispatchAgainst dispatchAgainst) {
        this.dispatchAgainst = dispatchAgainst;
    }

    public OutboundDispatchStatus getDispatchStatus() {
        return dispatchStatus;
    }

    public void setDispatchStatus(OutboundDispatchStatus dispatchStatus) {
        this.dispatchStatus = dispatchStatus;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

   /* public Set<Tracker> getTracker() {
        return tracker;
    }

    public void setTracker(Set<Tracker> tracker) {
        this.tracker = tracker;
    }*/

    /*public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }*/

    @Override
    public String toString() {
        return "OutboundDispatchResource{" +
                "id='" + id + '\'' +
                ", linkInBoudNumber=" + linkInBoudNumber +
                ", referenceNumber='" + referenceNumber + '\'' +
                ", leadPassengerName='" + leadPassengerName + '\'' +
               // ", parcel=" + parcel +
                ", contactList=" + contactList +
                ", recipientDetailsList=" + recipientDetailsList +
                ", dispatchSenderDetails=" + dispatchSenderDetails +
               // ", planCollection=" + planCollection +
               // ", consigneeName=" + consigneeName +
                //", consigneeLocation=" + consigneeLocation +
                /*", dispatchAgainst='" + dispatchAgainst + '\'' +*/
                //", dispatchDeliveryDate=" + dispatchDeliveryDate +
               // ", passportNumber='" + passportNumber + '\'' +
                //", statusId='" + statusId + '\'' +
                //", tracker=" + tracker +
               // ", barCode='" + barCode + '\'' +
                //", barcodes=" + barcodes +
                '}';
    }
}
