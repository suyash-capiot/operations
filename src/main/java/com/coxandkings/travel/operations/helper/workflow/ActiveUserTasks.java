package com.coxandkings.travel.operations.helper.workflow;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "active-user-tasks")
public class ActiveUserTasks implements Serializable {
    private List<TaskSummary> taskSummaries;

    @XmlElement(name = "task-summary")
    public List<TaskSummary> getTaskSummaries() {
        return taskSummaries;
    }

    public void setTaskSummaries(List<TaskSummary> taskSummaries) {
        this.taskSummaries = taskSummaries;
    }
}
