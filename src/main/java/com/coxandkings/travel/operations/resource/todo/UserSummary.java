package com.coxandkings.travel.operations.resource.todo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class UserSummary {
    private ToDoUserResource userId;
    private List<StatusCount> statusCounts;
    private Long total;

    @JsonIgnore
    private String fileHandlerID;

    public UserSummary() {
        statusCounts = new LinkedList<>();
        total = 0L;
    }

    public ToDoUserResource getUserId() {
        return userId;
    }

    public void incrementStatusCount(String status) {
        if (statusCounts == null || statusCounts.size() == 0) {
            StatusCount newStatusCount = new StatusCount();
            newStatusCount.setToDoStatus(status);
            newStatusCount.setCount(1L);
            statusCounts.add(newStatusCount);
        } else if (statusCounts != null && statusCounts.size() > 0) {
            StatusCount tmpCount = new StatusCount();
            tmpCount.setToDoStatus(status);
            if (statusCounts.contains(tmpCount)) {
                int existingStatusCount = statusCounts.indexOf(tmpCount);
                StatusCount aStatusCount = statusCounts.get(existingStatusCount);
                Long existingTaskCount = aStatusCount.getCount();
                existingTaskCount = existingTaskCount + 1;
                aStatusCount.setCount(existingTaskCount);
            } else {
                StatusCount newStatusCount = new StatusCount();
                newStatusCount.setToDoStatus(status);
                newStatusCount.setCount(1L);
                statusCounts.add(newStatusCount);
            }
        }
    }


    public void setUserId(ToDoUserResource userId) {
        this.userId = userId;
    }

    public List<StatusCount> getStatusCounts() {
        return statusCounts;
    }

    public void setStatusCounts(List<StatusCount> statusCounts) {
        this.statusCounts = statusCounts;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getFileHandlerID() {
        return fileHandlerID;
    }

    public void setFileHandlerID(String fileHandlerID) {
        this.fileHandlerID = fileHandlerID;
    }
}
