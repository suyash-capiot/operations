package com.coxandkings.travel.operations.enums.thirdPartyVoucher;

public enum PaymentStatusToReleaseVoucher {

    FULL_PAYMENT("Full"),
    PART_PAYMENT("Part");

    String value;

    PaymentStatusToReleaseVoucher(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PaymentStatusToReleaseVoucher getEnum(String value){
        PaymentStatusToReleaseVoucher paymentStatusToReleaseVoucher=null;
        for (PaymentStatusToReleaseVoucher ps:PaymentStatusToReleaseVoucher.values()){
            if (ps.getValue().equalsIgnoreCase(value)){
                paymentStatusToReleaseVoucher=ps;
                break;
            }
        }
        return paymentStatusToReleaseVoucher;
    }
}
