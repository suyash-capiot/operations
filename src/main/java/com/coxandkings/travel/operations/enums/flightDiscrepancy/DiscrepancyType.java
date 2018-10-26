package com.coxandkings.travel.operations.enums.flightDiscrepancy;

public enum DiscrepancyType {
    BSP("BSP"),
    LCC("LCC"),
    NON_BSP("NONBsp");

    private String value;

    DiscrepancyType(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
