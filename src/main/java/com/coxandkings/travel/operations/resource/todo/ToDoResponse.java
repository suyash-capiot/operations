package com.coxandkings.travel.operations.resource.todo;

import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToDoResponse {
    private List<ToDoTask> content;
    private List<ToDoTaskResponse> data;
    private Integer pageNumber;
    private Integer pageSize;
    private Boolean isLast;
    private Long count;
    private Integer totalPages;

    private boolean canAssignTask;

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Boolean getLast() {
        return isLast;
    }

    public void setLast(Boolean last) {
        isLast = last;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<ToDoTask> getContent() {
        return content;
    }

    public void setContent(List<ToDoTask> content) {
        this.content = content;
    }

    public List<ToDoTaskResponse> getData() {
        return data;
    }

    public void setData(List<ToDoTaskResponse> data) {
        this.data = data;
    }

    public boolean isCanAssignTask() {
        return canAssignTask;
    }

    public void setCanAssignTask(boolean canAssignTask) {
        this.canAssignTask = canAssignTask;
    }
}
