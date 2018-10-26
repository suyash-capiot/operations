package com.coxandkings.travel.operations.enums.qcmanagement;

import org.springframework.util.StringUtils;

public enum CancelAmendTypes {
    AIR_CANCELTYPE_CANCELPAX("CANCELPASSENGER"),
    AIR_CANCELTYPE_CANCELJOU("CANCELJOU"),
    AIR_CANCELTYPE_CANCELSSR("CANCELSSR"),
    AIR_CANCELTYPE_CANCELODO("CANCELODO"),
    AIR_AMENDTYPE_SSR("SSR"),
    AIR_AMENDTYPE_REM("REM"),
    AIR_AMENDTYPE_PIS("PIS"),
    AIR_AMENDTYPE_UPDATEPAX("UPDATEPASSENGER"),
    AIR_CANCELTYPE_CANCELALL("ALL"),

    ACCO_AMENDTYPE_ADDPAX("ADDPASSENGER"),
    ACCO_AMENDTYPE_UPDATEPAX("UPDATEPASSENGER"),
    ACCO_AMENDTYPE_UPDATESTAYDATES("UPDATESTAYDATES"),
    ACCO_AMENDTYPE_CHANGEPERIODOFSTAY("CHANGEPERIODOFSTAY"),
    ACCO_AMENDTYPE_UPDATEROOM("UPDATEROOM"),
    ACCO_CANCELTYPE_CANCELPAX("CANCELPASSENGER"),
    ACCO_CANCELTYPE_CANCELROOM("CANCELROOM"),
    ACCO_CANCELTYPE_FULLCANCEL("FULLCANCELLATION");

    private String cancelAmendType;

    CancelAmendTypes(String newCancelAmendType) {
        cancelAmendType = newCancelAmendType;
    }

    public static CancelAmendTypes getCancelAmendTypes(String newCancelAmendType) {
        CancelAmendTypes cancelAmendTypes = null;
        if (StringUtils.isEmpty(newCancelAmendType)) {
            return null;
        }
        for (CancelAmendTypes tempCancelAmendType : CancelAmendTypes.values()) {
            if (tempCancelAmendType.getStatus().equalsIgnoreCase(newCancelAmendType)) {
                cancelAmendTypes = tempCancelAmendType;
                break;
            }
        }
        return cancelAmendTypes;
    }

    public String getStatus() {
        return cancelAmendType;
    }
}