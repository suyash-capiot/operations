package com.coxandkings.travel.operations.resource.mailroomMaster;

import com.coxandkings.travel.operations.resource.BaseResource;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateSerializer;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;
import java.util.List;

public class PlanCollectionForOutboundIdResource extends BaseResource {
    private List<String> dispatchId;
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime planCollectionDate;
    private String planCollectionSlot;
    private String collectionEmployeeName;

    public List<String> getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(List<String> dispatchId) {
        this.dispatchId = dispatchId;
    }

    public ZonedDateTime getPlanCollectionDate() {
        return planCollectionDate;
    }

    public void setPlanCollectionDate(ZonedDateTime planCollectionDate) {
        this.planCollectionDate = planCollectionDate;
    }

    public String getPlanCollectionSlot() {
        return planCollectionSlot;
    }

    public void setPlanCollectionSlot(String planCollectionSlot) {
        this.planCollectionSlot = planCollectionSlot;
    }

    public String getCollectionEmployeeName() {
        return collectionEmployeeName;
    }

    public void setCollectionEmployeeName(String collectionEmployeeName) {
        this.collectionEmployeeName = collectionEmployeeName;
    }
}
