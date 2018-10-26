package com.coxandkings.travel.operations.resource.managedocumentation;

import com.coxandkings.travel.operations.enums.managedocumentation.DocumentStatus;

public class DocStatusResource {

    private String name;
    private DocumentStatus value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DocumentStatus getValue() {
        return value;
    }

    public void setValue(DocumentStatus value) {
        this.value = value;
    }
}
