package com.coxandkings.travel.operations.enums.partpaymentmonitor;

import org.springframework.util.StringUtils;

public enum PaymentDueDateType {

    FROM_BOOKING_DATE("From Booking Date"),
    PRIOR_TO_TRAVEL_DATE("Prior to Travel date"),
    SUPPLIER_SETTLEMENT("Suplier Settlement");

    private String type;

    PaymentDueDateType(String newType) {
        type = newType;
    }

    public static PaymentDueDateType getPaymentDueDateType(String newType) {
        PaymentDueDateType paymentDueDateType = null;
        if (StringUtils.isEmpty(newType)) {
            return null;
        }

        for (PaymentDueDateType tempPaymentDueDateType : PaymentDueDateType.values()) {
            if (tempPaymentDueDateType.getType().equalsIgnoreCase(newType)) {
                paymentDueDateType = tempPaymentDueDateType;
                break;
            }
        }

        return paymentDueDateType;
    }

    public String getType() {
        return type;
    }

}
