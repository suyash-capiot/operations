package com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability;

public enum Status {
    UNSETTLED("Unsettled"),
    PARTIALLY_SETTLED("Partially Settled"),
    PARTIALLY_SETTLED_AGAINST_DEPOSIT("Partially Settled against Deposit"),
    PARTIALLY_SETTLED_AGAINST_PRE_PAYMENT("Partially Settled against Prepayment"),
    SETTLED("Settled"),
    SETTLED_AGAINST_DEPOSIT("Settled against Deposit"),
    SETTLED_AGAINST_PRE_PAYMENT("Settled against Prepayment"),
    PAYMENT_PENDING("Payment Pending"),
    PARTIALLY_PAID("Partially Paid"),
    PAID("Paid"),
    FINAL_SERVICE_ORDER_GENERATED("Final Service Order Generated"),
    PROVISIONAL_SERVICE_ORDER_GENERATED("Provisional Service Order Generated"),
    PROVISIONAL_SUPPLIER_LIABILITY_GENERATED("Provisional Supplier Liability Generated"),
    FINAL_SUPPLIER_LIABILITY_GENERATED("Final Supplier Liability Generated"),
    PROVISIONAL_SERVICE_ORDER_CANCELLED("Provisional Service Order Cancelled"),
    PROVISIONAL_SUPPLIER_LIABILITY_CANCELLED("Provisional Supplier Liability Cancelled"),
    FINAL_SERVICE_ORDER_CANCELLED("Final Service Order Cancelled");

    private String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static Status fromString(String value) {
        Status status = null;
        if (value != null && value.trim().length() > 0) {
            for (Status statusVal : Status.values()) {
                if (statusVal.getValue().equals(value)) {
                    status = statusVal;
                    break;
                }
            }
        }
        return status;
    }
}