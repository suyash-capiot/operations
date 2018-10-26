package com.coxandkings.travel.operations.helper.workflow;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.List;

//@XmlRootElement(name = "kie-containers")
public class KieContainers implements Serializable {

    private List<KieContainer> kieContainer;

    @XmlElement(name = "kie-container")
    public List<KieContainer> getKieContainer() {
        return kieContainer;
    }

    public void setKieContainer(List<KieContainer> kieContainer) {
        this.kieContainer = kieContainer;
    }
}
