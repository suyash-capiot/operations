package com.coxandkings.travel.operations.enums.thirdPartyVoucher;

public enum VoucherCodeStatus {

    UNASSIGNED("UNASSIGNED"),
    ASSIGNED("Assigned"),
    SENT("Sent"),
    INVALID("Invalid"),
    ACTIVE("Active"),
    INACTIVE("Inactive");

    String value;

    VoucherCodeStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static VoucherCodeStatus getEnum(String value){
        VoucherCodeStatus voucherCodeStatus=null;
        for (VoucherCodeStatus vc:VoucherCodeStatus.values()){
            if (vc.getValue().equalsIgnoreCase(value)){
                voucherCodeStatus=vc;
                break;
            }
        }
        return voucherCodeStatus;
    }
}
