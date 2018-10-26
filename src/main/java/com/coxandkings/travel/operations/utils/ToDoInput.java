package com.coxandkings.travel.operations.utils;

import com.coxandkings.travel.operations.enums.todo.*;

public class ToDoInput {
    ToDoTaskNameValues toDoTaskNameValues;
    ToDoTaskPriorityValues toDoTaskPriorityValues;
    ToDoTaskStatusValues toDoTaskStatusValues;
    ToDoTaskSubTypeValues toDoTaskSubTypeValues;
    ToDoTaskTypeValues toDoTaskTypeValues;
    ToDoFunctionalAreaValues toDoFunctionalAreaValues;
    String refId;
    String fileHandlerId;
    String secondaryFileHandlerId;
    Long dueDate;

    public ToDoTaskNameValues getToDoTaskNameValues() {
        return toDoTaskNameValues;
    }

    public void setToDoTaskNameValues(ToDoTaskNameValues toDoTaskNameValues) {
        this.toDoTaskNameValues = toDoTaskNameValues;
    }

    public ToDoTaskPriorityValues getToDoTaskPriorityValues() {
        return toDoTaskPriorityValues;
    }

    public void setToDoTaskPriorityValues(ToDoTaskPriorityValues toDoTaskPriorityValues) {
        this.toDoTaskPriorityValues = toDoTaskPriorityValues;
    }

    public ToDoTaskStatusValues getToDoTaskStatusValues() {
        return toDoTaskStatusValues;
    }

    public void setToDoTaskStatusValues(ToDoTaskStatusValues toDoTaskStatusValues) {
        this.toDoTaskStatusValues = toDoTaskStatusValues;
    }

    public ToDoTaskSubTypeValues getToDoTaskSubTypeValues() {
        return toDoTaskSubTypeValues;
    }

    public void setToDoTaskSubTypeValues(ToDoTaskSubTypeValues toDoTaskSubTypeValues) {
        this.toDoTaskSubTypeValues = toDoTaskSubTypeValues;
    }

    public ToDoTaskTypeValues getToDoTaskTypeValues() {
        return toDoTaskTypeValues;
    }

    public void setToDoTaskTypeValues(ToDoTaskTypeValues toDoTaskTypeValues) {
        this.toDoTaskTypeValues = toDoTaskTypeValues;
    }

    public ToDoFunctionalAreaValues getToDoFunctionalAreaValues() {
        return toDoFunctionalAreaValues;
    }

    public void setToDoFunctionalAreaValues(ToDoFunctionalAreaValues toDoFunctionalAreaValues) {
        this.toDoFunctionalAreaValues = toDoFunctionalAreaValues;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
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

    public Long getDueDate() {
        return dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }
}
