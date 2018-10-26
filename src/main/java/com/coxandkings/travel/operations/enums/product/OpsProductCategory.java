package com.coxandkings.travel.operations.enums.product;

import org.springframework.util.StringUtils;

public enum OpsProductCategory {

    PRODUCT_CATEGORY_TRANSPORTATION( "Transportation" ),
    //TODO : SPELLING MISTAKE FOR ACCO
    PRODUCT_CATEGORY_ACCOMMODATION( "Accommodation" ),
    PRODUCT_CATEGORY_ACTIVITIES("Activities"),
    PRODUCT_CATEGORY_HOLIDAYS("Holidays"),
    PRODUCT_CATEGORY_OTHERPRODUCTS("OtherProducts");

    private String category;

    OpsProductCategory(String newCategory )   {
        category = newCategory;
    }

    public static OpsProductCategory getProductCategory(String aCategory) {
        OpsProductCategory opsProductCategory = null;
        if(StringUtils.isEmpty(aCategory)) {
            return null;
        }

        for(OpsProductCategory opsTmpProductCategory: OpsProductCategory.values()) {
            if(opsTmpProductCategory.getCategory().equalsIgnoreCase(aCategory)) {
                opsProductCategory = opsTmpProductCategory;
                break;
            }
        }

        return opsProductCategory;
    }

    public String getCategory() {
        return category;
    }
}
