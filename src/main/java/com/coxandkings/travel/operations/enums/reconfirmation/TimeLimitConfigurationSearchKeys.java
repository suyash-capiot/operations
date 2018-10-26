package com.coxandkings.travel.operations.enums.reconfirmation;

public enum TimeLimitConfigurationSearchKeys {

    tlmId("tlmId"),
    companyMarket("companyMarket"),
    tlmEntityType("tlmEntityType"),
    tlmEntityName("tlmEntityName"),
    entityId("entityId");


    private String value;

    TimeLimitConfigurationSearchKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}

