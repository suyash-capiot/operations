package com.coxandkings.travel.operations.criteria.todo;

import com.coxandkings.travel.operations.criteria.BaseCriteria;
import com.coxandkings.travel.operations.enums.user.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ToDoCriteria extends BaseCriteria {
    private String referenceId;
    private String bookingRefId;
    private String assignedBy;
    private String productId;
    private String clientCategoryId;
    private String clientSubCategoryId;
    private String clientTypeId;
    private String clientName;
    private String companyId;
    private String companyMarketId;
    private Long dueOn;
    private String taskNameId;
    private String taskTypeId;
    private String taskSubTypeId;
    private String taskFunctionalAreaId;
    private String taskOrientedTypeId;
    private String[] taskStatusIds;
    private String taskPriorityId;
    private String fileHandlerId;
    private String[] fileHandlerIds;
    private String secondaryFileHandlerId;
    private String suggestedActions;
    private String mainTaskId;
    private String mainTaskStatusTriggerId;
    private String lockedBy;
    private Boolean overdue;

    private String businessProcess;
    private String taskArea;

    private String productCategory;
    private String productSubCategory;

    @JsonIgnore
    private UserType userType;

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getBusinessProcess() {
        return businessProcess;
    }

    public void setBusinessProcess(String businessProcess) {
        this.businessProcess = businessProcess;
    }

    public String getTaskArea() {
        return taskArea;
    }

    public void setTaskArea(String taskArea) {
        this.taskArea = taskArea;
    }

    public String getFileHandlerId() {
        return fileHandlerId;
    }

    public void setFileHandlerId(String fileHandlerId) {
        this.fileHandlerId = fileHandlerId;
    }

    public String[] getTaskStatusIds() {
        return taskStatusIds;
    }

    public void setTaskStatusIds(String[] taskStatusIds) {
        this.taskStatusIds = taskStatusIds;
    }

    public String[] getFileHandlerIds() {
        return fileHandlerIds;
    }

    public void setFileHandlerIds(String[] fileHandlerIds) {
        this.fileHandlerIds = fileHandlerIds;
    }

    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

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

    public String getCompanyMarketId() {
        return companyMarketId;
    }

    public void setCompanyMarketId(String companyMarketId) {
        this.companyMarketId = companyMarketId;
    }

    public Long getDueOn() {
        return dueOn;
    }

    public void setDueOn(Long dueOn) {
        this.dueOn = dueOn;
    }

    public String getSecondaryFileHandlerId() {
        return secondaryFileHandlerId;
    }

    public void setSecondaryFileHandlerId(String secondaryFileHandlerId) {
        this.secondaryFileHandlerId = secondaryFileHandlerId;
    }

    public String getSuggestedActions() {
        return suggestedActions;
    }

    public void setSuggestedActions(String suggestedActions) {
        this.suggestedActions = suggestedActions;
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

    public String getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public Boolean getOverdue() {
        return overdue;
    }

    public void setOverdue(Boolean overdue) {
        this.overdue = overdue;
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

    public void setTaskFunctionalAreaId(String taskFunctionalAreaId) {
        this.taskFunctionalAreaId = taskFunctionalAreaId;
    }

    public void setTaskPriorityId(String taskPriorityId) {
        this.taskPriorityId = taskPriorityId;
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

    public String getTaskPriorityId() {
        return taskPriorityId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTaskOrientedTypeId() {
        return taskOrientedTypeId;
    }

    public void setTaskOrientedTypeId(String taskOrientedTypeId) {
        this.taskOrientedTypeId = taskOrientedTypeId;
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
}
