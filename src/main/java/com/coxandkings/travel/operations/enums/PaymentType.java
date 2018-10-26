package com.coxandkings.travel.operations.enums;

import org.springframework.util.StringUtils;

public enum PaymentType {
    FULL_PAYMENT("Full"),
    PART_PAYMENT("Part"),
    NO_PAYMENT("No Payment");

    private String type;

    PaymentType(String newType) {
        type = newType;
    }

    public static PaymentType getPaymentType(String newType) {
        PaymentType paymentType = null;
        if (StringUtils.isEmpty(newType)) {
            return null;
        }

        for (PaymentType tempPaymentType : PaymentType.values()) {
            if (tempPaymentType.getType().equalsIgnoreCase(newType)) {
                paymentType = tempPaymentType;
                break;
            }
        }

        return paymentType;
    }

    public String getType() {
        return type;
    }



}
