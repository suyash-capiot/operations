package com.coxandkings.travel.operations.helper.workflow;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "task-summary")
public class TaskSummary implements Serializable {
    private String taskId;
    private String taskStatus;
    private String taskActualOwner;
    private String containerId;

    @XmlElement(name = "task-id")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @XmlElement(name = "task-status")
    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    @XmlElement(name = "task-actual-owner")
    public String getTaskActualOwner() {
        return taskActualOwner;
    }

    public void setTaskActualOwner(String taskActualOwner) {
        this.taskActualOwner = taskActualOwner;
    }

    @XmlElement(name = "task-container-id")
    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }
}
