package com.coxandkings.travel.operations.resource.alert;

import com.coxandkings.travel.operations.enums.AlertType;
import com.coxandkings.travel.operations.enums.WorkFlow;
import com.coxandkings.travel.operations.resource.BaseResource;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;

import java.util.Map;

public class AlertResource extends BaseResource {
    private AlertType alertType;
    private WorkFlow workFlow;
    private String procInstanceId;
    private Map<String, String> params;
    private ToDoTaskResource toDoTaskResource;
    private String entityRefId;
    private String userTaskName;
    private Boolean createMappingInstance;
    private String productId;


    public ToDoTaskResource getToDoTaskResource() {
        return toDoTaskResource;
    }

    public void setToDoTaskResource(ToDoTaskResource toDoTaskResource) {
        this.toDoTaskResource = toDoTaskResource;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public WorkFlow getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(WorkFlow workFlow) {
        this.workFlow = workFlow;
    }

    public String getProcInstanceId() {
        return procInstanceId;
    }

    public void setProcInstanceId(String procInstanceId) {
        this.procInstanceId = procInstanceId;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
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

    public Boolean getCreateMappingInstance() {
        return createMappingInstance;
    }

    public void setCreateMappingInstance(Boolean createMappingInstance) {
        this.createMappingInstance = createMappingInstance;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

}
