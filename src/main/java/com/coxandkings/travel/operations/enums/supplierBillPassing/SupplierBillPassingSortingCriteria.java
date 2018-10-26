package com.coxandkings.travel.operations.enums.supplierBillPassing;

public enum SupplierBillPassingSortingCriteria {

    SupplierName("supplierName"),
    ProvisionalServiceOrderID("provisionalServiceOrderID"),
    FinalServiceOrderID("finalServiceOrderID"),
    ProvisionalSupplierLiabilityID("provisionalSupplierLiabilityID"),
    FinalSupplierLiabilityID("finalSupplierLiabilityID"),
    PaymentAdviceStatus("paymentAdviceStatus"),
    SupplierSettlementStatus("supplierSettlementStatus"),
    SupplierBillPassingStatus("supplierBillPassingStatus");

    private String sortByValue;

    SupplierBillPassingSortingCriteria(String sortByValue){
        this.sortByValue=sortByValue;
    }

    public static SupplierBillPassingSortingCriteria forString(String psgrTypeStr) {
        SupplierBillPassingSortingCriteria[] psgrTypes = SupplierBillPassingSortingCriteria.values();
        for (SupplierBillPassingSortingCriteria psgrType : psgrTypes) {
            if (psgrType.toString().equals(psgrTypeStr)) {
                return psgrType;
            }
        }
        return null;
    }


    public String getSortByValue() {
        return sortByValue;
    }
}
