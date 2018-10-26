package com.coxandkings.travel.operations.enums.doTicketing;

import org.springframework.util.StringUtils;

public enum FareAmountComparator {

    LOWER("Lower"),
    EQUAL("Equal"),
    HIGHER("Higher");

    private String value;

    FareAmountComparator(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static FareAmountComparator fromString(String fareAmount) {
        FareAmountComparator fareAmountComparatorType = null;

        if(StringUtils.isEmpty(fareAmount)) {
            return fareAmountComparatorType;
        }

        for(FareAmountComparator fareAmountComparatorTypeTemp : FareAmountComparator.values()) {
            if(fareAmount.equalsIgnoreCase(fareAmountComparatorTypeTemp.getValue())) {
                fareAmountComparatorType = fareAmountComparatorTypeTemp;
            }
        }

        return fareAmountComparatorType;
    }


}
