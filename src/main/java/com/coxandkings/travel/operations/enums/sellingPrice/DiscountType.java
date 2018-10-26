package com.coxandkings.travel.operations.enums.sellingPrice;

import org.springframework.util.StringUtils;

public enum DiscountType {
    AMOUNT("amount"),
    PERCENTAGE("percentage");

    private String name;

    DiscountType(String aName) {
        name = aName;
    }

    public static DiscountType getDiscountType(String aName) {
        DiscountType discountType = null;
        if(StringUtils.isEmpty(aName)) {
            return null;
        }

        for(DiscountType tmpDiscountType: DiscountType.values()) {
            if(tmpDiscountType.getName().equalsIgnoreCase(aName)) {
                discountType = tmpDiscountType;
            }
        }

        return discountType;
    }

    private String getName() {
        return name;
    }
}
