package com.coxandkings.travel.operations.enums.taxengine;

public enum TaxEngineOperations {
    SEARCH("Search"), AMEND("Amend");

    private String operName;

    TaxEngineOperations(String operName) {
        this.operName = operName;
    }

    public String getOperName() {
        return this.operName;
    }
}
