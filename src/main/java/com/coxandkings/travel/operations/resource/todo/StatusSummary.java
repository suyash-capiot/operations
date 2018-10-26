package com.coxandkings.travel.operations.resource.todo;

import java.util.LinkedList;
import java.util.List;

public class StatusSummary {
    private String toDoStatus;
    private List<UserCount> userSummaries;
    private Long totalOverdue;
    private Long totalNotOverdue;

    public StatusSummary() {
        userSummaries = new LinkedList<>();
        totalOverdue = 0L;
        totalNotOverdue = 0L;
    }

    public String getToDoStatus() {
        return toDoStatus;
    }

    public void setToDoStatus(String toDoStatus) {
        this.toDoStatus = toDoStatus;
    }

    public List<UserCount> getUserSummaries() {
        return userSummaries;
    }

    public void setUserSummaries(List<UserCount> userSummaries) {
        this.userSummaries = userSummaries;
    }

    public Long getTotalOverdue() {
        return totalOverdue;
    }

    public void setTotalOverdue(Long totalOverdue) {
        this.totalOverdue = totalOverdue;
    }

    public Long getTotalNotOverdue() {
        return totalNotOverdue;
    }

    public void setTotalNotOverdue(Long totalNotOverdue) {
        this.totalNotOverdue = totalNotOverdue;
    }
}
