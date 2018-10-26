package com.coxandkings.travel.operations.criteria.workflow;

public class WorkFlowFilter {

    private String _id;
    private String workFlowOperationType;
    private String createdBy;
    private String type;
    private String serviceName;
    private Boolean deleted;
    private String status;

    public WorkFlowFilter() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getWorkFlowOperationType() {
        return workFlowOperationType;
    }

    public void setWorkFlowOperationType(String workFlowOperationType) {
        this.workFlowOperationType = workFlowOperationType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
