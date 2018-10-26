package com.coxandkings.travel.operations.enums.flightDiscrepancy;

public enum FilterByName {
    SOURCE("Source"),
    AIRLINENAME("AirLineName"),
    IATA("IataNumber"),
    SUPPLIERNAME("supplierName");
    private String value;
    FilterByName(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
