package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OpsActionItem {

    @JsonProperty("label")
    private String label;

    @JsonProperty("value")
    private String value;

    @JsonProperty("state")
    private String state;

    @JsonProperty("inLineEdit")
    private boolean inLineEdit;

    @JsonProperty("functionName")
    private String functionName;

    public OpsActionItem(String label, String value, String state, boolean inLineEdit, String functionName) {
        this.label = label;
        this.value = value;
        this.state = state;
        this.inLineEdit = inLineEdit;
        this.functionName = functionName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isInLineEdit() {
        return inLineEdit;
    }

    public void setInLineEdit(boolean inLineEdit) {
        this.inLineEdit = inLineEdit;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }
}
