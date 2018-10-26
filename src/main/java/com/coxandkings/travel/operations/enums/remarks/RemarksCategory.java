package com.coxandkings.travel.operations.enums.remarks;

import org.springframework.util.StringUtils;

public enum RemarksCategory {
    FIRST_RESERVATION_CHECK("first_reservation_check"),
    BOOKING_REMARKS("booking_remarks");
    private String categoryType;

    RemarksCategory(String newCategory) {
        categoryType = newCategory;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public static RemarksCategory getRemarksCategory(String newCategory) {
        RemarksCategory remarksCategory = null;
        if(StringUtils.isEmpty(newCategory)) {
            return null;
        }

        for(RemarksCategory tempRemarksCategory: RemarksCategory.values()) {
            if(tempRemarksCategory.getCategoryType().equalsIgnoreCase(newCategory)) {
                remarksCategory = tempRemarksCategory;
                break;
            }
        }

        return remarksCategory;
    }
}
