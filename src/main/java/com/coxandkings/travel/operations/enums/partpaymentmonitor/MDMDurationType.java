package com.coxandkings.travel.operations.enums.partpaymentmonitor;

import org.springframework.util.StringUtils;

public enum MDMDurationType {

    DAYS("days"),
    MONTHS("months");
    private String type;

    MDMDurationType(String newType) {
        type = newType;
    }

    public static MDMDurationType getMDMDurationType(String newType) {
        MDMDurationType mdmDurationType = null;
        if (StringUtils.isEmpty(newType)) {
            return null;
        }

        for (MDMDurationType tempMDMDurationType : MDMDurationType.values()) {
            if (tempMDMDurationType.getType().equalsIgnoreCase(newType)) {
                mdmDurationType = tempMDMDurationType;
                break;
            }
        }

        return mdmDurationType;
    }

    public String getType() {
        return type;
    }

}
