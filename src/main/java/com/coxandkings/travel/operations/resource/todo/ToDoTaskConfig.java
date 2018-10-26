package com.coxandkings.travel.operations.resource.todo;

public class ToDoTaskConfig {
    private String taskName;
    private String url;

    public ToDoTaskConfig() {
    }

    public ToDoTaskConfig(String taskName, String url) {
        this.taskName = taskName;
        this.url = url;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
