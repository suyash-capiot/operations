package com.coxandkings.travel.operations.helper.workflow;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "kie-container")
public class KieContainer implements Serializable {
    private String containerId;
    private Messages messages;

    @XmlAttribute(name="container-id")
    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public Messages getMessages() {
        return messages;
    }

    public void setMessages(Messages messages) {
        this.messages = messages;
    }
}
