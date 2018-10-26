package com.coxandkings.travel.operations.criteria.jbpm;

import com.coxandkings.travel.operations.criteria.BaseCriteria;
import com.coxandkings.travel.operations.enums.WorkFlow;

public class InstanceBookMappingCriteria extends BaseCriteria {
    private WorkFlow workFlow;
    private String userTaskName;
    private String entityRefId;

    public String getUserTaskName() {
        return userTaskName;
    }

    public void setUserTaskName(String userTaskName) {
        this.userTaskName = userTaskName;
    }

    public String getEntityRefId() {
        return entityRefId;
    }

    public void setEntityRefId(String entityRefId) {
        this.entityRefId = entityRefId;
    }

    public WorkFlow getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(WorkFlow workFlow) {
        this.workFlow = workFlow;
    }
}
