package com.coxandkings.travel.operations.model.mailroomanddispatch;

import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateSerializer;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="inbound_log_entry")
public class InboundEntry {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="inbound_entry_id")
    private String id;//inBoundNumber
    private String inboundNo;
    private String mode;
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime receivedDate;
    private String awbNo;
    private Integer piecesRecived;
    private String remarks;
    private String supplierName;
    private String linkToDispatchId;
    @OneToOne (cascade=CascadeType.ALL)
    private InboundSenderDetails inboundSenderDetails;
    @OneToOne (cascade=CascadeType.ALL)
    private InboundRecipientDetails inboundRecipientDetails;
   // @OneToOne (cascade=CascadeType.ALL)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "plan_delivery_id")
    private PlanDelivery planDelivery;
    /*@OneToOne (cascade=CascadeType.ALL)
    private CommonDelivery commonDelivery;*/
    @OneToMany(mappedBy = "inboundEntry",cascade=CascadeType.ALL)
    private List<PackageDetails> packageList;
    @ManyToMany
    @JoinTable(name="inbound_logstatus", joinColumns=@JoinColumn(name="inbound_entry_id"), inverseJoinColumns=@JoinColumn(name="inbound_status_id"))
    private Set<InboundLogEntryStatus> inboundLogEntryStatus;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "master_room_id")
    private MailRoomMaster mailRoomMaster;

    private String modeSub;
    private Integer actualPieces;

    //WORKFLOW Implementations
    @OneToOne(mappedBy = "mailRoomMaster", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
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

    public String getModeSub() {
        return modeSub;
    }

    public void setModeSub(String modeSub) {
        this.modeSub = modeSub;
    }

    public Integer getActualPieces() {
        return actualPieces;
    }

    public void setActualPieces(Integer actualPieces) {
        this.actualPieces = actualPieces;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInboundNo() {
        return inboundNo;
    }

    public void setInboundNo(String inboundNo) {
        this.inboundNo = inboundNo;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public ZonedDateTime getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(ZonedDateTime receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getAwbNo() {
        return awbNo;
    }

    public void setAwbNo(String awbNo) {
        this.awbNo = awbNo;
    }

    public Integer getPiecesRecived() {
        return piecesRecived;
    }

    public void setPiecesRecived(Integer piecesRecived) {
        this.piecesRecived = piecesRecived;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getLinkToDispatchId() {
        return linkToDispatchId;
    }

    public void setLinkToDispatchId(String linkToDispatchId) {
        this.linkToDispatchId = linkToDispatchId;
    }

    public InboundSenderDetails getInboundSenderDetails() {
        return inboundSenderDetails;
    }

    public void setInboundSenderDetails(InboundSenderDetails inboundSenderDetails) {
        this.inboundSenderDetails = inboundSenderDetails;
    }


    public List<PackageDetails> getPackageList() {
        return packageList;
    }

    public void setPackageList(List<PackageDetails> packageList) {
        this.packageList = packageList;
    }

    public InboundRecipientDetails getInboundRecipientDetails() {
        return inboundRecipientDetails;
    }

    public void setInboundRecipientDetails(InboundRecipientDetails inboundRecipientDetails) {
        this.inboundRecipientDetails = inboundRecipientDetails;
    }

    public PlanDelivery getPlanDelivery() {
        return planDelivery;
    }

    public void setPlanDelivery(PlanDelivery planDelivery) {
        this.planDelivery = planDelivery;
    }

   /* public String getProofOfDelivery() {
        return proofOfDelivery;
    }

    public void setProofOfDelivery(String proofOfDelivery) {
        this.proofOfDelivery = proofOfDelivery;
    }*/

    public Set<InboundLogEntryStatus> getInboundLogEntryStatus() {
        return inboundLogEntryStatus;
    }

    public void setInboundLogEntryStatus(Set<InboundLogEntryStatus> inboundLogEntryStatus) {
        this.inboundLogEntryStatus = inboundLogEntryStatus;
    }

    public MailRoomMaster getMailRoomMaster() {
        return mailRoomMaster;
    }

    public void setMailRoomMaster(MailRoomMaster mailRoomMaster) {
        this.mailRoomMaster = mailRoomMaster;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InboundEntry that = (InboundEntry) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(mode, that.mode) &&
                Objects.equals(receivedDate, that.receivedDate) &&
                Objects.equals(awbNo, that.awbNo) &&
                Objects.equals(piecesRecived, that.piecesRecived) &&
                Objects.equals(remarks, that.remarks) &&
                Objects.equals(supplierName, that.supplierName) &&
                Objects.equals(linkToDispatchId, that.linkToDispatchId) &&
                Objects.equals(inboundSenderDetails, that.inboundSenderDetails) &&
                Objects.equals(inboundRecipientDetails, that.inboundRecipientDetails) &&
                Objects.equals(planDelivery, that.planDelivery) &&
               // Objects.equals(commonDelivery, that.commonDelivery) &&
                Objects.equals(packageList, that.packageList) &&
                Objects.equals(inboundLogEntryStatus, that.inboundLogEntryStatus) &&
                Objects.equals(mailRoomMaster, that.mailRoomMaster);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, mode, receivedDate, awbNo, piecesRecived, remarks, supplierName, linkToDispatchId, inboundSenderDetails, inboundRecipientDetails, planDelivery,packageList,mailRoomMaster,inboundLogEntryStatus);
    }

}
