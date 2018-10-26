package com.coxandkings.travel.operations.enums.refund;

public enum  RefundTypes {
    REFUND_AMOUNT("Refund Amount"),
    ACCOUNT_CREDIT("Account Credit"),
    REFUND_REDEEMABLE("Refund Redeemable");

    private String status;

    RefundTypes(String status) {
        this.status=status;

    }


    public static RefundTypes getRefundType(String status) {
        RefundTypes refundTypes = null;


        for(RefundTypes refundTypes1: RefundTypes.values()) {
            //System.out.println(refundTypes1.getStatus()+"="+status.replaceAll("\"",""));
            if(refundTypes1.getStatus().equalsIgnoreCase(status)) {
                refundTypes = refundTypes1;
                break;
            }
        }

        return refundTypes;
    }

    public String getStatus() {

        return this.status;

    }

}
