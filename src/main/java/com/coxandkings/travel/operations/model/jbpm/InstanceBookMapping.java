package com.coxandkings.travel.operations.model.jbpm;

import com.coxandkings.travel.operations.enums.Status;
import com.coxandkings.travel.operations.enums.WorkFlow;
import com.coxandkings.travel.operations.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class InstanceBookMapping extends BaseModel {


    @Column(name = "booking_ref_id")
    private String bookingRefId;

    @Column
    @Enumerated(EnumType.STRING)
    private WorkFlow workFlow;

    @Column(name = "instance_id")
    private String instanceId;

    @Column(name = "entity_Ref_Id")
    private String entityRefId;

    @Column(name = "userTaskName")
    private String userTaskName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public WorkFlow getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(WorkFlow workFlow) {
        this.workFlow = workFlow;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getEntityRefId() {
        return entityRefId;
    }

    public void setEntityRefId(String entityRefId) {
        this.entityRefId = entityRefId;
    }

    public String getUserTaskName() {
        return userTaskName;
    }

    public void setUserTaskName(String userTaskName) {
        this.userTaskName = userTaskName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
