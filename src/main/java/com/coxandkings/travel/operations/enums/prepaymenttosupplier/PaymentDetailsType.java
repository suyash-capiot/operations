package com.coxandkings.travel.operations.enums.prepaymenttosupplier;

import org.springframework.util.StringUtils;

public enum PaymentDetailsType {

    GUARANTEE_TO_SUPPLIER("Guarantee To Supplier"),
    PAY_TO_SUPPLIER("Pay To Supplier"),
    GUARANTEE_TO_SUPPLIER_PLUS_PAY_TO_SUPPLIER("Guarantee To Supplier + Pay To Supplier");

    private String paymentType;

    PaymentDetailsType(String type) {
        paymentType = type;
    }

    public static PaymentDetailsType fromString(String type) {
        PaymentDetailsType paymentDetailsType = null;
        if (StringUtils.isEmpty(type)) {
            return null;
        }

        for (PaymentDetailsType tmpType : PaymentDetailsType.values()) {
            if (tmpType.getPaymentType().equalsIgnoreCase(type)) {
                paymentDetailsType = tmpType;
                break;
            }
        }
        return paymentDetailsType;
    }

    public String getPaymentType() {
        return paymentType;
    }
}
