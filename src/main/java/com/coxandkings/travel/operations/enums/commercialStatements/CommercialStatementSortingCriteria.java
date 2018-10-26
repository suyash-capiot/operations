package com.coxandkings.travel.operations.enums.commercialStatements;

public enum CommercialStatementSortingCriteria {

    StatementId("statementId"),
    CommercialHead("commercialHead"),
    ProductCategory("productCategory"),
    ProductCategorySubType("productCategorySubType"),
    ProductNameSubType("productNameSubType"),
    ProductFlavourName("productFlavourName"),
    ProductName("productName"),
    BookingDate("bookingPeriodFrom"),
    TravelDate("travelDateFrom"),
    SupplierORClientName("supplierOrClientName"),
    StatementName("statementName"),
    StatementDueDate("settlementDueDate"),
    SettlementStatus("settlementStatus");

    private String sortByValue;

    CommercialStatementSortingCriteria(String sortByValue){
        this.sortByValue=sortByValue;
    }

    public String getSortByValue() {
        return sortByValue;
    }
}
