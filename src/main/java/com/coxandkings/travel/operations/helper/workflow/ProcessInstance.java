package com.coxandkings.travel.operations.helper.workflow;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "process-instance")
public class ProcessInstance implements Serializable {
    private String processInstanceId;
    private String processId;
    private ActiveUserTasks activeUserTasks;


    @XmlElement(name = "process-instance-id")
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @XmlElement(name = "process-id")
    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    @XmlElement(name = "active-user-tasks")
    public ActiveUserTasks getActiveUserTasks() {
        return activeUserTasks;
    }

    public void setActiveUserTasks(ActiveUserTasks activeUserTasks) {
        this.activeUserTasks = activeUserTasks;
    }
}
