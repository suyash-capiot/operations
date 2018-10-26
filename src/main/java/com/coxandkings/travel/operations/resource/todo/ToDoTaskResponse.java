package com.coxandkings.travel.operations.resource.todo;

import com.coxandkings.travel.operations.enums.todo.ToDoTaskGeneratedTypeValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskStatusValues;
import com.coxandkings.travel.operations.model.todo.ToDoCheckListItem;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.model.todo.ToDoTaskDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToDoTaskResponse {
    private String id;
    private String referenceId;
    private String bookingRefId;
    private ToDoUserResource assignedByUser;

    @JsonProperty("productId")
    private String productName;
    private String productCategory;
    private String productSubCategory;

    private JSONObject request;

    private ToDoClientResource client;
    private ToDoCompanyResource company;
    private ToDoCompanyMarketResource companyMarket;
    private String dueOn;

    private String taskNameValue;
    private String taskTypeValue;
    private String taskSubType;
    private String taskFunctionalAreaValue;
    private String taskStatusValue;
    private String taskPriorityValue;
    private String taskOrientedValue;
    private ToDoTaskGeneratedTypeValues taskGeneratedType;
    private ToDoUserResource fileHandler;
    private ToDoUserResource secondaryFileHandler;
    private Boolean overdue;
    private String note;
    private String suggestedActions;
    private ToDoTaskStatusValues mainTaskStatus;
    private String remark;
    private List<ToDoCheckListItem> checkListItems;
    private ToDoTask parentToDoTask;
    private List<ToDoTask> childToDoTasks;
    private ToDoTaskDetails toDoTaskDetails;
    private String url;

    private boolean isRead;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public ToDoClientResource getClient() {
        return client;
    }

    public void setClient(ToDoClientResource client) {
        this.client = client;
    }

    public ToDoCompanyResource getCompany() {
        return company;
    }

    public void setCompany(ToDoCompanyResource company) {
        this.company = company;
    }

    public ToDoCompanyMarketResource getCompanyMarket() {
        return companyMarket;
    }

    public void setCompanyMarket(ToDoCompanyMarketResource companyMarket) {
        this.companyMarket = companyMarket;
    }

    public String getDueOn() {
        return dueOn;
    }

    public void setDueOn(String dueOn) {
        this.dueOn = dueOn;
    }

    public String getTaskNameValue() {
        return taskNameValue;
    }

    public void setTaskNameValue(String taskNameValue) {
        this.taskNameValue = taskNameValue;
    }

    public String getTaskTypeValue() {
        return taskTypeValue;
    }

    public void setTaskTypeValue(String taskTypeValue) {
        this.taskTypeValue = taskTypeValue;
    }

    public String getTaskSubType() {
        return taskSubType;
    }

    public void setTaskSubType(String taskSubType) {
        this.taskSubType = taskSubType;
    }

    public String getTaskFunctionalAreaValue() {
        return taskFunctionalAreaValue;
    }

    public void setTaskFunctionalAreaValue(String taskFunctionalAreaValue) {
        this.taskFunctionalAreaValue = taskFunctionalAreaValue;
    }

    public String getTaskStatusValue() {
        return taskStatusValue;
    }

    public void setTaskStatusValue(String taskStatusValue) {
        this.taskStatusValue = taskStatusValue;
    }

    public String getTaskPriorityValue() {
        return taskPriorityValue;
    }

    public void setTaskPriorityValue(String taskPriorityValue) {
        this.taskPriorityValue = taskPriorityValue;
    }

    public ToDoTaskGeneratedTypeValues getTaskGeneratedType() {
        return taskGeneratedType;
    }

    public void setTaskGeneratedType(ToDoTaskGeneratedTypeValues taskGeneratedType) {
        this.taskGeneratedType = taskGeneratedType;
    }

    public ToDoUserResource getAssignedByUser() {
        return assignedByUser;
    }

    public void setAssignedByUser(ToDoUserResource assignedByUser) {
        this.assignedByUser = assignedByUser;
    }

    public ToDoUserResource getFileHandler() {
        return fileHandler;
    }

    public void setFileHandler(ToDoUserResource fileHandler) {
        this.fileHandler = fileHandler;
    }

    public ToDoUserResource getSecondaryFileHandler() {
        return secondaryFileHandler;
    }

    public void setSecondaryFileHandler(ToDoUserResource secondaryFileHandler) {
        this.secondaryFileHandler = secondaryFileHandler;
    }

    public Boolean getOverdue() {
        return overdue;
    }

    public void setOverdue(Boolean overdue) {
        this.overdue = overdue;
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

    public ToDoTaskStatusValues getMainTaskStatus() {
        return mainTaskStatus;
    }

    public void setMainTaskStatus(ToDoTaskStatusValues mainTaskStatus) {
        this.mainTaskStatus = mainTaskStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ToDoCheckListItem> getCheckListItems() {
        return checkListItems;
    }

    public void setCheckListItems(List<ToDoCheckListItem> checkListItems) {
        this.checkListItems = checkListItems;
    }

    public ToDoTask getParentToDoTask() {
        return parentToDoTask;
    }

    public void setParentToDoTask(ToDoTask parentToDoTask) {
        this.parentToDoTask = parentToDoTask;
    }

    public List<ToDoTask> getChildToDoTasks() {
        return childToDoTasks;
    }

    public void setChildToDoTasks(List<ToDoTask> childToDoTasks) {
        this.childToDoTasks = childToDoTasks;
    }

    public ToDoTaskDetails getToDoTaskDetails() {
        return toDoTaskDetails;
    }

    public void setToDoTaskDetails(ToDoTaskDetails toDoTaskDetails) {
        this.toDoTaskDetails = toDoTaskDetails;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getTaskOrientedValue() {
        return taskOrientedValue;
    }

    public void setTaskOrientedValue(String taskOrientedValue) {
        this.taskOrientedValue = taskOrientedValue;
    }

    public JSONObject getRequest() {
        return request;
    }

    public void setRequest(JSONObject request) {
        this.request = request;
    }
}
