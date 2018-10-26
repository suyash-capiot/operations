package com.coxandkings.travel.operations.model.mailroomanddispatch;


import com.coxandkings.travel.operations.enums.mailroomanddispatch.DispatchAgainst;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="outbound_dispatch")
public class OutboundDispatch {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="outbound_dispatch_id")
    private String id;
    private String dispatchId;
    @ManyToOne
    @JoinColumn(name = "Outbound_Parcel")
    private Parcel parcel;
    private String linkInBoudNumber;
    @Enumerated
    private DispatchAgainst dispatchAgainst;
    private String referenceNumber;
    private String leadPassengerName;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id")
    private List<ContentDetails> contactList;
    @OneToOne (cascade=CascadeType.ALL)
    private BaseRecipientDetails recipientDetailsList;
    @OneToOne (cascade=CascadeType.ALL)
    private DispatchSenderDetails dispatchSenderDetails;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "plan_collection_id")
    private PlanCollection planCollection;
    /*@JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime dispatchDeliveryDate;
    private String passportNumber;
    private String consigneeName;
    private String consigneeLocation;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Tracker> tracker;
    private String barCode;
    @OneToOne (cascade=CascadeType.ALL)
    private BarCode barcodes;*/
    @OneToOne(cascade = CascadeType.ALL)
    private OutboundDispatchStatus status;
    @OneToOne(cascade = CascadeType.ALL)
    private DispatchCostDetails dispatchCostDetails;

    //------------------WORKFLOW-----------------------
    @OneToOne(mappedBy = "outboundDispatch", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private RequestLockObject lock;

    @Column
    private String lastModifiedByUserId;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "createdOn", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdOn;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "lastModifiedOn", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime lastModifiedOn;

    public RequestLockObject getLock() {
        return lock;
    }

    public void setLock(RequestLockObject lock) {
        this.lock = lock;
    }

    public String getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    public void setLastModifiedByUserId(String lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public ZonedDateTime getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(ZonedDateTime lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public OutboundDispatchStatus getStatus() {
        return status;
    }

    public void setStatus(OutboundDispatchStatus status) {
        this.status = status;
    }

    private String passportNo;

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
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

    public DispatchCostDetails getDispatchCostDetails() {
        return dispatchCostDetails;
    }

    public void setDispatchCostDetails(DispatchCostDetails dispatchCostDetails) {
        this.dispatchCostDetails = dispatchCostDetails;
    }

    public DispatchSenderDetails getDispatchSenderDetails() {
        return dispatchSenderDetails;
    }

    public void setDispatchSenderDetails(DispatchSenderDetails dispatchSenderDetails) {
        this.dispatchSenderDetails = dispatchSenderDetails;
    }

   /* public ZonedDateTime getDispatchDeliveryDate() {
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

    public PlanCollection getPlanCollection() {
        return planCollection;
    }

    public void setPlanCollection(PlanCollection planCollection) {
        this.planCollection = planCollection;
    }

    /* public String getDispatchAgainst() {
         return dispatchAgainst;
     }

     public void setDispatchAgainst(String dispatchAgainst) {
         this.dispatchAgainst = dispatchAgainst;
     }
 */
    /*public String getConsigneeLocation() {
        return consigneeLocation;
    }

    public void setConsigneeLocation(String consigneeLocation) {
        this.consigneeLocation = consigneeLocation;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }*/
    /*public OutboundStatus getOutboundStatus() {
        return outboundStatus;
    }

    public void setOutboundStatus(OutboundStatus outboundStatus) {
        this.outboundStatus = outboundStatus;
    }
*/

    public Parcel getParcel() {
        return parcel;
    }

    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }

    /*public Set<Tracker> getTracker() {
        return tracker;
    }

    public void setTracker(Set<Tracker> tracker) {
        this.tracker = tracker;
    }*/

   /* public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public BarCode getBarcodes() {
        return barcodes;
    }

    public void setBarcodes(BarCode barcodes) {
        this.barcodes = barcodes;
    }*/

    public DispatchAgainst getDispatchAgainst() {
        return dispatchAgainst;
    }

    public void setDispatchAgainst(DispatchAgainst dispatchAgainst) {
        this.dispatchAgainst = dispatchAgainst;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OutboundDispatch that = (OutboundDispatch) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(parcel, that.parcel) &&
                Objects.equals(linkInBoudNumber, that.linkInBoudNumber) &&
                Objects.equals(referenceNumber, that.referenceNumber) &&
                Objects.equals(leadPassengerName, that.leadPassengerName) &&
                Objects.equals(contactList, that.contactList) &&
                Objects.equals(recipientDetailsList, that.recipientDetailsList) &&
                Objects.equals(dispatchSenderDetails, that.dispatchSenderDetails) &&
                Objects.equals(planCollection, that.planCollection) &&
                Objects.equals(dispatchAgainst, that.dispatchAgainst) &&
                /*Objects.equals(consigneeName, that.consigneeName) &&
                Objects.equals(consigneeLocation, that.consigneeLocation) &&
                Objects.equals(dispatchDeliveryDate, that.dispatchDeliveryDate) &&*/
               // Objects.equals(passportNumber, that.passportNumber) &&
               // Objects.equals(outboundStatus, that.outboundStatus) &&
                Objects.equals(status, that.status) ;
              //  Objects.equals(tracker, that.tracker) ;
               // Objects.equals(barCode, that.barCode) &&
               // Objects.equals(barcodes, that.barcodes);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, parcel, linkInBoudNumber, referenceNumber, leadPassengerName, contactList, recipientDetailsList, dispatchSenderDetails, planCollection,status);
    }

    @Override
    public String toString() {
        return "OutboundDispatch{" +
                "id='" + id + '\'' +
                ", parcel=" + parcel +
                ", linkInBoudNumber=" + linkInBoudNumber +
                ", referenceNumber='" + referenceNumber + '\'' +
                ", leadPassengerName='" + leadPassengerName + '\'' +
                ", contactList=" + contactList +
                ", recipientDetailsList=" + recipientDetailsList +
                ", dispatchSenderDetails=" + dispatchSenderDetails +
                ", planCollection=" + planCollection +
                //", dispatchAgainst='" + dispatchAgainst + '\'' +
               /* ", consigneeName=" + consigneeName +
                ", consigneeLocation=" + consigneeLocation +
                ", dispatchDeliveryDate=" + dispatchDeliveryDate +*/
               // ", passportNumber='" + passportNumber + '\'' +
                ", status=" + status +
               // ", tracker=" + tracker +
               // ", barCode='" + barCode + '\'' +
               // ", barcodes=" + barcodes +
                '}';
    }
}
