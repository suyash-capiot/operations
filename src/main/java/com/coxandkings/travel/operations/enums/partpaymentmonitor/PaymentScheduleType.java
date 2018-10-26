package com.coxandkings.travel.operations.enums.partpaymentmonitor;

import org.springframework.util.StringUtils;

public enum PaymentScheduleType {
    BALANCE_PAYMENT("Balance Payment"),
    INITAL_DEPOSITE_VISA("Inital deposite visa fees"),
    INITIAL_DEPOSITE_BOOKING_AMOUNT("Initial deposite-Booking amount"),
    INITIAL_DEPOSITE_DOCUMENT_AMOUNT("Intial deposite-documentation amount"),
    PERCENT_PRODUCT_PRICE("Percent Product Price");

    private String type;

    PaymentScheduleType(String newType) {
        type = newType;
    }

    public static PaymentScheduleType getPaymentScheduleType(String newType) {
        PaymentScheduleType paymentScheduleType = null;
        if (StringUtils.isEmpty(newType)) {
            return null;
        }

        for (PaymentScheduleType tempPaymentScheduleType : PaymentScheduleType.values()) {
            if (tempPaymentScheduleType.getType().equalsIgnoreCase(newType)) {
                paymentScheduleType = tempPaymentScheduleType;
                break;
            }
        }

        return paymentScheduleType;
    }

    public String getType() {
        return type;
    }

}
