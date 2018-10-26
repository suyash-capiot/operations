package com.coxandkings.travel.operations.helper.workflow;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "response")
public class Response implements Serializable {

    private KieContainers kieContainers;

    @XmlElement(name = "kie-containers")
    public KieContainers getKieContainers() {
        return kieContainers;
    }

    public void setKieContainers(KieContainers kieContainers) {
        this.kieContainers = kieContainers;
    }
}
