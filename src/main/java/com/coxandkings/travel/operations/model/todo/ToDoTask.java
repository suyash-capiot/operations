package com.coxandkings.travel.operations.model.todo;

import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.model.BaseModel;
import com.coxandkings.travel.operations.model.companycommercial.JSONUserType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;
import org.json.JSONObject;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(indexes = {@Index(name = "IDX_MYIDX1", columnList = "assigned_by,file_handler_id")})
@TypeDefs({@TypeDef(name = "StringJsonObject", typeClass = JSONUserType.class)})
public class ToDoTask extends BaseModel {

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "booking_ref_id")
    private String bookingRefId;

    @Column(name = "assigned_by")
    private String assignedBy;

//    @Column(name = "assigned_to")
//    private String assignedTo;

    @Column(name = "product_id")
    // TODO : put @NotNull back when data is sanitised from other services
    private String productId;

    @Transient
    private JSONObject request;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "product_sub_category")
    private String productSubCategory;

    @Column(name = "client_category_id")
    private String clientCategoryId;

    @Column(name = "client_sub_category_id")
    private String clientSubCategoryId;

    @Column(name = "client_type_id")
    // @NotNull
    // TODO : put @NotNull back when data is sanitised from other services
    private String clientTypeId;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_Name")
    private String clientName;

    @Column(name = "company_id")
    // TODO : put @NotNull back when data is sanitised from other services
    private String companyId;

    @Column(name = "company_market_id")
    private String companyMarketId;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", name = "due_on", nullable = false)
    private ZonedDateTime dueOn;

    @Enumerated(EnumType.STRING)
    private ToDoTaskNameValues taskName;

    @Enumerated(EnumType.STRING)
    private ToDoTaskTypeValues taskType;

    @Column(name = "task_subtype_id")
    @Enumerated(EnumType.STRING)
    private ToDoTaskSubTypeValues taskSubType;

    @Column(name = "request_body")
    @Type(type = "StringJsonObject", parameters = {@org.hibernate.annotations.Parameter(name = "classType", value = "java.lang.String")})
    private String requestBody;

    @Column(name = "task_oriented_id")
    @Enumerated(EnumType.STRING)
    private ToDoTaskOrientedValues taskOrientedType;

    @Column(name = "task_subtype_desc")
    private String taskSubTypeDesc;

    @Enumerated(EnumType.STRING)
    private ToDoFunctionalAreaValues taskFunctionalArea;

    @Enumerated(EnumType.STRING)
    private ToDoTaskStatusValues taskStatus;

    @Enumerated(EnumType.STRING)
    //@NotNull
    //TODO : put @NotNull back when data is sanitised from other services - May be not null
    private ToDoTaskPriorityValues taskPriority;

    @Enumerated(EnumType.STRING)
    private ToDoTaskGeneratedTypeValues taskGeneratedType;


    @Column(name = "file_handler_id")
    // TODO : put @NotNull back when data is sanitised from other services - May be not null
    private String fileHandlerId;

    @Column(name = "secondary_file_handler_id")
    private String secondaryFileHandlerId;

    @Column
    private Boolean overdue;

    @Column(name = "note")
    private String note;

    @Column(name = "suggested_actions")
    private String suggestedActions;

    @Enumerated(EnumType.STRING)
    private ToDoTaskStatusValues mainTaskStatus;

    @Column
    private String remark;

    @OneToMany(mappedBy = "toDoTask")
    @JsonBackReference
    private List<ToDoCheckListItem> checkListItems;

    //Mapping with parents and followees
    @OneToOne
    @JoinColumn(name = "main_task_id")
    @JsonIgnore
    private ToDoTask parentToDoTask;

    @OneToMany(mappedBy = "parentToDoTask")
    @Fetch(FetchMode.JOIN)
    private List<ToDoTask> childToDoTasks;

    //About the record
    @OneToOne(cascade = CascadeType.ALL)
    @NotNull
    private ToDoTaskDetails toDoTaskDetails;

    @Column(name = "alert_notified")
    private Boolean alertNotified;

    @Enumerated(EnumType.STRING)
    private ToDoTaskNameValues mainTaskNameValue;

    public ToDoTask() {
    }

    public String getTaskSubTypeDesc() {
        return taskSubTypeDesc;
    }

    public void setTaskSubTypeDesc(String taskSubTypeDesc) {
        this.taskSubTypeDesc = taskSubTypeDesc;
    }

    public ToDoTask(ToDoTaskStatusValues taskStatus) {
        this.taskStatus = taskStatus;
    }

    private String url;

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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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

    public ZonedDateTime getDueOn() {
        return dueOn;
    }

    public void setDueOn(ZonedDateTime dueOn) {
        this.dueOn = dueOn;
    }

    public ToDoTaskNameValues getTaskName() {
        return taskName;
    }

    public void setTaskName(ToDoTaskNameValues taskName) {
        this.taskName = taskName;
    }

    public ToDoTaskTypeValues getTaskType() {
        return taskType;
    }

    public void setTaskType(ToDoTaskTypeValues taskType) {
        this.taskType = taskType;
    }

    public ToDoTaskSubTypeValues getTaskSubType() {
        return taskSubType;
    }

    public void setTaskSubType(ToDoTaskSubTypeValues taskSubType) {
        this.taskSubType = taskSubType;
    }

    public ToDoFunctionalAreaValues getTaskFunctionalArea() {
        return taskFunctionalArea;
    }

    public void setTaskFunctionalArea(ToDoFunctionalAreaValues taskFunctionalArea) {
        this.taskFunctionalArea = taskFunctionalArea;
    }

    public ToDoTaskStatusValues getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(ToDoTaskStatusValues taskStatus) {
        this.taskStatus = taskStatus;
    }

    public ToDoTaskPriorityValues getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(ToDoTaskPriorityValues taskPriority) {
        this.taskPriority = taskPriority;
    }

    public ToDoTaskGeneratedTypeValues getTaskGeneratedType() {
        return taskGeneratedType;
    }

    public void setTaskGeneratedType(ToDoTaskGeneratedTypeValues taskGeneratedType) {
        this.taskGeneratedType = taskGeneratedType;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public void setOverdue(Boolean overdue) {
        this.overdue = overdue;
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

    public boolean getOverdue() {
        return overdue;
    }

    public void setOverdue(boolean overdue) {
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

    public Boolean getAlertNotified() {
        return alertNotified;
    }

    public void setAlertNotified(Boolean alertNotified) {
        this.alertNotified = alertNotified;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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

    public ToDoTaskOrientedValues getTaskOrientedType() {
        return taskOrientedType;
    }

    public void setTaskOrientedType(ToDoTaskOrientedValues taskOrientedType) {
        this.taskOrientedType = taskOrientedType;
    }

    public JSONObject getRequest() {
        return request;
    }

    public void setRequest(JSONObject request) {
        this.request = request;
    }

    public ToDoTaskNameValues getMainTaskNameValue() {
        return mainTaskNameValue;
    }

    public void setMainTaskNameValue(ToDoTaskNameValues mainTaskNameValue) {
        this.mainTaskNameValue = mainTaskNameValue;
    }
}

