package com.coxandkings.travel.operations.enums.doTicketing;

import org.springframework.util.StringUtils;

public enum ApprovalTypeValues {

    PAYMENT_PENDING("Payment Pending"),
    PAYMENT_PENDING_AND_BOOKING_NOT_CONFIRMED("Payment Pending and Booking not Confirmed"),
    BOOKING_NOT_CONFIRMED("Booking not Confirmed"),
    REFUND_TO_CLIENT("Refund to Client"),
    ABSORB_BY_COMPANY("Absorb by Company");

    private String type;

    ApprovalTypeValues(String value) {this.type = value; }

    public String getValue() {
        return type;
    }

    public static ApprovalTypeValues fromString(String type) {
        ApprovalTypeValues approvalTypeValue = null;

        if(StringUtils.isEmpty(type)) {
            return approvalTypeValue;
        }

        for(ApprovalTypeValues approvalTypeValuesTemp : ApprovalTypeValues.values()) {
            if(type.equalsIgnoreCase(approvalTypeValuesTemp.getValue())) {
                approvalTypeValue = approvalTypeValuesTemp;
            }
        }
        return approvalTypeValue;
    }

}
