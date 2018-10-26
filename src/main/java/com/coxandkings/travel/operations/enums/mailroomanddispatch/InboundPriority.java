package com.coxandkings.travel.operations.enums.mailroomanddispatch;

public enum InboundPriority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");
    private String value;

    InboundPriority(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
