package com.coxandkings.travel.operations.resource.jbpm;

import com.coxandkings.travel.operations.enums.Status;
import com.coxandkings.travel.operations.enums.WorkFlow;
import com.coxandkings.travel.operations.resource.BaseResource;

public class InstanceBookMappingResource extends BaseResource {
    private WorkFlow workFlow;
    private String instanceId;
    private String entityRefId;
    private String userTaskName;
    private String entityType;
    private Status status;

    public String getEntityType() { return entityType; }

    public void setEntityType(String entityType) { this.entityType = entityType; }

    public WorkFlow getWorkFlow() { return workFlow; }

    public void setWorkFlow(WorkFlow workFlow) { this.workFlow = workFlow; }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getEntityRefId() { return entityRefId; }

    public void setEntityRefId(String entityRefId) { this.entityRefId = entityRefId; }

    public String getUserTaskName() { return userTaskName; }

    public void setUserTaskName(String userTaskName) { this.userTaskName = userTaskName; }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
