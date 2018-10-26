package com.coxandkings.travel.operations.resource.alert;

import com.coxandkings.travel.operations.enums.WorkFlow;
import com.coxandkings.travel.operations.resource.BaseResource;

import java.util.Map;

public class WorkFlowResource extends BaseResource {
    private WorkFlow workFlow;
    private String entityRefId;
    private String userTaskName;
    private Map<String,Object> attributes;

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public WorkFlow getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(WorkFlow workFlow) {
        this.workFlow = workFlow;
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
}
