package com.coxandkings.travel.operations.resource.mailroomMaster;

import com.coxandkings.travel.operations.enums.mailroomanddispatch.WorkflowEnums;
import com.coxandkings.travel.operations.model.mailroomanddispatch.*;
import com.coxandkings.travel.operations.resource.BaseResource;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateSerializer;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class InboundEntryResource extends BaseResource {

    private String id;
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
    private InboundSenderDetails inboundSenderDetails;
    private InboundRecipientDetails inboundRecipientDetails;
    private PlanDelivery planDelivery;
    private List<PackageDetails> packageList;
    private Set<InboundLogEntryStatus> inboundLogEntryStatus;
    //private String proofOfDelivery;
    //private String statusId;
    //private MailRoomMaster mailRoomMaster;
    //private CommonDelivery commonDelivery;
    private String modeSub;
    private Integer actualPieces;

    //WorkFlow Implementations
    private String action;
    private WorkflowEnums status;
    private String lastModifiedByUserId;
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime lastModifiedOn;
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime createdOn;

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

    @Override
    public String getId() {
        return id;
    }

    public String getInboundNo() {
        return inboundNo;
    }

    public void setInboundNo(String inboundNo) {
        this.inboundNo = inboundNo;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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

/*    public String getProofOfDelivery() {
        return proofOfDelivery;
    }

    public void setProofOfDelivery(String proofOfDelivery) {
        this.proofOfDelivery = proofOfDelivery;
    }*/

   /* public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }
*/
    /*public MailRoomMaster getMailRoomMaster() {
        return mailRoomMaster;
    }

    public void setMailRoomMaster(MailRoomMaster mailRoomMaster) {
        this.mailRoomMaster = mailRoomMaster;
    }*/

    public Set<InboundLogEntryStatus> getInboundLogEntryStatus() {
        return inboundLogEntryStatus;
    }

    public void setInboundLogEntryStatus(Set<InboundLogEntryStatus> inboundLogEntryStatus) {
        this.inboundLogEntryStatus = inboundLogEntryStatus;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InboundEntryResource that = (InboundEntryResource) o;
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
                Objects.equals(packageList, that.packageList) &&
                Objects.equals(inboundLogEntryStatus, that.inboundLogEntryStatus);
                //Objects.equals(statusId, that.statusId) &&
               // Objects.equals(mailRoomMaster, that.mailRoomMaster) &&
              //  Objects.equals(commonDelivery, that.commonDelivery);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, mode, receivedDate, awbNo, piecesRecived, remarks, supplierName, linkToDispatchId, inboundSenderDetails, inboundRecipientDetails, planDelivery, packageList,inboundLogEntryStatus);
    }

}
