package com.coxandkings.travel.operations.helper.workflow;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "process-definitions")
public class ProcessDefinitions implements Serializable {
    private List<Process> processes;

    public List<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }
}
