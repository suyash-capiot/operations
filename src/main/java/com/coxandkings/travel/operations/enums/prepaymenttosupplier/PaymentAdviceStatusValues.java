package com.coxandkings.travel.operations.enums.prepaymenttosupplier;

import org.springframework.util.StringUtils;

public enum PaymentAdviceStatusValues {
    //GENERATED,
    APPROVAL_PENDING ("Pending for Approval"),
    APPROVED ( "Approved" ),
    REJECTED ( "Rejected" );

    private String paymentAdviseStatus;

    PaymentAdviceStatusValues( String newStatus )   {
        this.paymentAdviseStatus = newStatus;
    }

    public String getPaymentAdviseStatus()  {
        return paymentAdviseStatus;
    }

    public static PaymentAdviceStatusValues fromString( String newStatus ) {
        PaymentAdviceStatusValues aPaymentAdviseStatus = null;
        if(StringUtils.isEmpty(newStatus)) {
            return null;
        }

        for(PaymentAdviceStatusValues tmpStatus: PaymentAdviceStatusValues.values()) {
            if(tmpStatus.getPaymentAdviseStatus().equalsIgnoreCase(newStatus)) {
                aPaymentAdviseStatus = tmpStatus;
                break;
            }
        }

        return aPaymentAdviseStatus;
    }
}
