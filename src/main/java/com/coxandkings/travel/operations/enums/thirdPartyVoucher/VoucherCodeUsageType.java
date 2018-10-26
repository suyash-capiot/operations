package com.coxandkings.travel.operations.enums.thirdPartyVoucher;

public enum VoucherCodeUsageType {

    FIXED("Fixed"),
    MULTIPLE("Multiple");

    String value;

    VoucherCodeUsageType(String value) {
        this.value = value;
    }

    public static VoucherCodeUsageType getEnum(String value){
        VoucherCodeUsageType voucherCodeUsageType=null;
        for (VoucherCodeUsageType vc:VoucherCodeUsageType.values()){
            if (vc.value.equalsIgnoreCase(value)){
                voucherCodeUsageType=vc;
            }
        }
        return voucherCodeUsageType;
    }
}
