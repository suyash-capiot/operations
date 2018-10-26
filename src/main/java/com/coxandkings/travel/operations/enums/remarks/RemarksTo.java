package com.coxandkings.travel.operations.enums.remarks;

import org.springframework.util.StringUtils;

public enum RemarksTo {

    CUSTOMER("customer"),
    SUPPLIER("supplier");
    private String toType;

    RemarksTo(String newToType) {
        toType = newToType;
    }

    public static RemarksTo getRemarksTo(String newToType) {
        RemarksTo remarksTo = null;
        if(StringUtils.isEmpty(newToType)) {
            return null;
        }

        for(RemarksTo tempRemarksTo: RemarksTo.values()) {
            if(tempRemarksTo.getToType().equalsIgnoreCase(newToType)) {
                remarksTo = tempRemarksTo;
                break;
            }
        }

        return remarksTo;
    }

    public String getToType() {
        return toType;
    }


}
