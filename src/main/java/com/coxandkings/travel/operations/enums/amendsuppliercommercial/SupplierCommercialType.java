package com.coxandkings.travel.operations.enums.amendsuppliercommercial;

public enum SupplierCommercialType {
    PAYABLE("payable"),
    RECEIVABLE("Receivable");
    private String type;

    SupplierCommercialType(String type) {
        this.type = type;
    }

    public String getCommercialType() {
        return type;
    }
}
