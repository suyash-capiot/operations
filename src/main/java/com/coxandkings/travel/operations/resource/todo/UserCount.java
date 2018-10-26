package com.coxandkings.travel.operations.resource.todo;

public class UserCount {
    private ToDoUserResource toDoUserResource;
//    private Boolean overdue;
    private Long overdueCount;
    private Long notOverdueCount;

    public UserCount() {
    }

    public UserCount(ToDoUserResource toDoUserResource, Long overdueCount, Long notOverdueCount) {
        this.toDoUserResource = toDoUserResource;
        this.overdueCount = overdueCount;
        this.notOverdueCount = notOverdueCount;
    }

    public ToDoUserResource getToDoUserResource() {
        return toDoUserResource;
    }

    public void setToDoUserResource(ToDoUserResource toDoUserResource) {
        this.toDoUserResource = toDoUserResource;
    }

    public Long getOverdueCount() {
        return overdueCount;
    }

    public void setOverdueCount(Long overdueCount) {
        this.overdueCount = overdueCount;
    }

    public Long getNotOverdueCount() {
        return notOverdueCount;
    }

    public void setNotOverdueCount(Long notOverdueCount) {
        this.notOverdueCount = notOverdueCount;
    }
}
