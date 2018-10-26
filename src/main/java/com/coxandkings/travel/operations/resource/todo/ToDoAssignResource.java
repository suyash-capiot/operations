package com.coxandkings.travel.operations.resource.todo;

import java.util.List;

public class ToDoAssignResource {
    private String fileHandlerid;
    private String user;
    private List<String> taskIds;

    public ToDoAssignResource() {
    }

    public String getFileHandlerid() {
        return fileHandlerid;
    }

    public void setFileHandlerid(String fileHandlerid) {
        this.fileHandlerid = fileHandlerid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<String> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<String> taskIds) {
        this.taskIds = taskIds;
    }
}
