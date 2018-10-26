package com.coxandkings.travel.operations.enums.supplierBillPassing;

public enum SupplierBillPassingSortingOrder {
    ASC("Ascending"),
    DESC("Descending");

    private String sortingOrder;

    public String getSortingOrder() {
        return sortingOrder;
    }

    SupplierBillPassingSortingOrder(String value){
        this.sortingOrder=value;
    }
}
