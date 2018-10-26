package com.coxandkings.travel.operations.resource.todo;

import com.coxandkings.travel.operations.enums.todo.ToDoTaskNameValues;
import com.coxandkings.travel.operations.resource.BaseResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

import java.time.ZonedDateTime;

public class ToDoTaskResource extends BaseResource {

    private String referenceId;//M
    private String bookingRefId;
    private String assignedBy;//Don't send
    private String productId;//O - Add Product name here
    private String productCategory;
    private String productSubCategory;
    private String clientCategoryId;//O
    private String clientSubCategoryId;//O
    private String clientTypeId;//M
    private String companyId;//M
    private String clientId;//M
    private String companyMarketId;//M
    @JsonProperty("dueOn")
    private ZonedDateTime dueOnDate;//M
    private JSONObject requestBody;

    private String taskNameId;//M
    private String taskTypeId;//M //must be from task types defined
    private String taskSubTypeId;//M
    private String taskSubTypeDesc;
    private String taskFunctionalAreaId;//M //must be from functional area Id defined
    private String taskStatusId;//Don't send //must be one of the status Ids defined
    private String taskPriorityId;//O //must be one of the defined status Ids
    private String taskGeneratedTypeId;//Don't send
    private String taskOrientedTypeId;

    private String fileHandlerId;//O but recommended
    private String secondaryFileHandlerId;//O

    private String note;//O
    private String suggestedActions;//O
    private String remark;//O

    private String mainTaskId;
    private String mainTaskName;

    private String mainTaskStatusTriggerId;//M in case of following tasks

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getClientCategoryId() {
        return clientCategoryId;
    }

    public void setClientCategoryId(String clientCategoryId) {
        this.clientCategoryId = clientCategoryId;
    }

    public String getClientSubCategoryId() {
        return clientSubCategoryId;
    }

    public void setClientSubCategoryId(String clientSubCategoryId) {
        this.clientSubCategoryId = clientSubCategoryId;
    }

    public String getClientTypeId() {
        return clientTypeId;
    }

    public void setClientTypeId(String clientTypeId) {
        this.clientTypeId = clientTypeId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCompanyMarketId() {
        return companyMarketId;
    }

    public void setCompanyMarketId(String companyMarketId) {
        this.companyMarketId = companyMarketId;
    }

    @Override
    public String getBookingRefId() {
        return bookingRefId;
    }

    @Override
    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }


    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public ZonedDateTime getDueOnDate() {
        return dueOnDate;
    }

    public void setDueOnDate(ZonedDateTime dueOnDate) {
        this.dueOnDate = dueOnDate;
    }

    public String getTaskNameId() {
        return taskNameId;
    }

    public void setTaskNameId(String taskNameId) {
        this.taskNameId = taskNameId;
    }

    public String getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(String taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public String getTaskSubTypeId() {
        return taskSubTypeId;
    }

    public void setTaskSubTypeId(String taskSubTypeId) {
        this.taskSubTypeId = taskSubTypeId;
    }

    public String getTaskFunctionalAreaId() {
        return taskFunctionalAreaId;
    }

    public void setTaskFunctionalAreaId(String taskFunctionalAreaId) {
        this.taskFunctionalAreaId = taskFunctionalAreaId;
    }

    public String getTaskStatusId() {
        return taskStatusId;
    }

    public void setTaskStatusId(String taskStatusId) {
        this.taskStatusId = taskStatusId;
    }

    public String getTaskPriorityId() {
        return taskPriorityId;
    }

    public void setTaskPriorityId(String taskPriorityId) {
        this.taskPriorityId = taskPriorityId;
    }

    public String getTaskGeneratedTypeId() {
        return taskGeneratedTypeId;
    }

    public void setTaskGeneratedTypeId(String taskGeneratedTypeId) {
        this.taskGeneratedTypeId = taskGeneratedTypeId;
    }

    public String getFileHandlerId() {
        return fileHandlerId;
    }

    public void setFileHandlerId(String fileHandlerId) {
        this.fileHandlerId = fileHandlerId;
    }

    public String getSecondaryFileHandlerId() {
        return secondaryFileHandlerId;
    }

    public void setSecondaryFileHandlerId(String secondaryFileHandlerId) {
        this.secondaryFileHandlerId = secondaryFileHandlerId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSuggestedActions() {
        return suggestedActions;
    }

    public void setSuggestedActions(String suggestedActions) {
        this.suggestedActions = suggestedActions;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMainTaskId() {
        return mainTaskId;
    }

    public void setMainTaskId(String mainTaskId) {
        this.mainTaskId = mainTaskId;
    }

    public String getMainTaskStatusTriggerId() {
        return mainTaskStatusTriggerId;
    }

    public void setMainTaskStatusTriggerId(String mainTaskStatusTriggerId) {
        this.mainTaskStatusTriggerId = mainTaskStatusTriggerId;
    }

    public String getTaskSubTypeDesc() {
        return taskSubTypeDesc;
    }

    public void setTaskSubTypeDesc(String taskSubTypeDesc) {
        this.taskSubTypeDesc = taskSubTypeDesc;
    }

    public String getTaskOrientedTypeId() {
        return taskOrientedTypeId;
    }

    public void setTaskOrientedTypeId(String taskOrientedTypeId) {
        this.taskOrientedTypeId = taskOrientedTypeId;
    }

    public JSONObject getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(JSONObject requestBody) {
        this.requestBody = requestBody;
    }

    public String getMainTaskName() {
        return mainTaskName;
    }

    public void setMainTaskName(String mainTaskName) {
        this.mainTaskName = mainTaskName;
    }

    @Override
    public String toString() {
        return "ToDoTaskResource{" +
                "referenceId='" + referenceId + '\'' +
                ", bookingRefId='" + bookingRefId + '\'' +
                ", assignedBy='" + assignedBy + '\'' +
                ", productId='" + productId + '\'' +
                ", productCategory='" + productCategory + '\'' +
                ", productSubCategory='" + productSubCategory + '\'' +
                ", clientCategoryId='" + clientCategoryId + '\'' +
                ", clientSubCategoryId='" + clientSubCategoryId + '\'' +
                ", clientTypeId='" + clientTypeId + '\'' +
                ", companyId='" + companyId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", companyMarketId='" + companyMarketId + '\'' +
                ", dueOnDate=" + dueOnDate +
                ", taskNameId='" + taskNameId + '\'' +
                ", taskTypeId='" + taskTypeId + '\'' +
                ", taskSubTypeId='" + taskSubTypeId + '\'' +
                ", taskOrientedTypeId='" + taskOrientedTypeId + '\'' +
                ", taskFunctionalAreaId='" + taskFunctionalAreaId + '\'' +
                ", taskStatusId='" + taskStatusId + '\'' +
                ", taskPriorityId='" + taskPriorityId + '\'' +
                ", taskGeneratedTypeId='" + taskGeneratedTypeId + '\'' +
                ", fileHandlerId='" + fileHandlerId + '\'' +
                ", secondaryFileHandlerId='" + secondaryFileHandlerId + '\'' +
                ", note='" + note + '\'' +
                ", suggestedActions='" + suggestedActions + '\'' +
                ", remark='" + remark + '\'' +
                ", mainTaskId='" + mainTaskId + '\'' +
                ", mainTaskStatusTriggerId='" + mainTaskStatusTriggerId + '\'' +
                '}';
    }
}