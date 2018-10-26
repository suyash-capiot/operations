package com.coxandkings.travel.operations.enums;

public enum WorkFlow {
    ADD_DRIVER_DETAILS("AddDriverDetails"),
    CHANGE_SUPPLIER_PRICE("ChangeSupplierPrice"),
    CHANGE_SUPPLIER_NAME("ChangeSupplier"),
    MAIN_PROCESS("MainProcess"),
    MANAGE_PICKUP_DROPOFF("ManagePickUpAndDropOffDetails"),

    ABSORB("Absorb"),
    CHARGE("Charge"),
    ABSORB_OR_CHARGE("AbsorbOrCharge"),
    RETAIN_OR_REFUND("RetainOrRefund"),
    CHEAPER_PRICE_UPDATES("CheaperPriceUpdates"),

    Change_REFUND_TYPE("ChangeRefundType"),
    MANAGE_REFUNDS("ManageRefunds"),
    UPDATE_SELLING_PRICE_RECORD("UpdateSellingPriceRecord"),
    FullCancellationPaymentSection("FullCancellationPaymentSection"),
    MANAGE_TIME_LIMIT_BOOKING(" ManageTimeLimitBooking"),
    BOOKING_CONFIRMATION("BookingConfirmation"),
    FULL_CANCELLATION_PROCESS("FullCancellationProcess"),
    PROCESS2("Process2");

    private String workFlowName;
    WorkFlow(String workFlowName) {
        this.workFlowName = workFlowName;
    }

    public String getWorkFlowName() {
        return this.workFlowName;
    }
}
