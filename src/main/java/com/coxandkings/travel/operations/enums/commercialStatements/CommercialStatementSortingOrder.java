package com.coxandkings.travel.operations.enums.commercialStatements;

public enum CommercialStatementSortingOrder {
    ASC("Ascending"),
    DESC("Descending");

    private String sortingOrder;

    public String getSortingOrder() {
        return sortingOrder;
    }

    CommercialStatementSortingOrder(String value){
        this.sortingOrder=value;
    }
}
