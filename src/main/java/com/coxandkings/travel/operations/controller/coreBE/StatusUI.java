package com.coxandkings.travel.operations.controller.coreBE;

public class StatusUI {
    private String label;
    private String value;

    public StatusUI() {
    }

    public StatusUI(String label, String value) {
        this.label = label;
        this.value = value;
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
}
