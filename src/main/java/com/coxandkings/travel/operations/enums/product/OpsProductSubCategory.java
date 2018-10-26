package com.coxandkings.travel.operations.enums.product;

public enum OpsProductSubCategory {

    PRODUCT_SUB_CATEGORY_FLIGHT( OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION, "Flight" ),
    PRODUCT_SUB_CATEGORY_BUS( OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION, "Bus" ),
    PRODUCT_SUB_CATEGORY_RAIL( OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION, "Rail" ),
    PRODUCT_SUB_CATEGORY_INDIAN_RAIL( OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION, "Indian Rail" ),
    PRODUCT_SUB_CATEGORY_CAR( OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION, "Car" ),
    PRODUCT_SUB_CATEGORY_HOTELS( OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION, "Hotel" ),
    PRODUCT_SUB_CATEGORY_EVENTS(OpsProductCategory.PRODUCT_CATEGORY_ACTIVITIES, "Events"),
    PRODUCT_SUB_CATEGORY_HOLIDAYS(OpsProductCategory.PRODUCT_CATEGORY_HOLIDAYS, "Holidays"),
    PRODUCT_SUB_CATEGORY_EXTRAS(OpsProductCategory.PRODUCT_CATEGORY_HOLIDAYS, "Extras"),
    PRODUCT_SUB_CATEGORY_EXTENSIONNIGHTS(OpsProductCategory.PRODUCT_CATEGORY_HOLIDAYS, "Extension Nights"),
    PRODUCT_SUB_CATEGORY_TRANSFER(OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION, "Transfer"),
    PRODUCT_SUB_CATEGORY_INSURANCE(OpsProductCategory.PRODUCT_CATEGORY_OTHERPRODUCTS, "Insurance");

    private OpsProductCategory category;

    private String subCategory;

    OpsProductSubCategory( OpsProductCategory parentCategory, String newSubCategory )     {
        category = parentCategory;
        subCategory = newSubCategory;
    }

    public static OpsProductSubCategory fromString( String newSubCategory )  {
        OpsProductSubCategory aProductSubCategory = null;
        for( OpsProductSubCategory aTmpProductSubCategory : OpsProductSubCategory.values() )    {
            if( aTmpProductSubCategory.getSubCategory().equalsIgnoreCase( newSubCategory ))   {
                aProductSubCategory = aTmpProductSubCategory;
                break;
            }
        }

        return aProductSubCategory;
    }

    public static OpsProductSubCategory getProductSubCategory(OpsProductCategory aProductCategory, String aSubCategory )  {
        OpsProductSubCategory aProductSubCategory = null;
        if( aProductCategory == null || (aSubCategory == null || aSubCategory.trim().length() == 0 ) )  {
            return null;
        }

        for( OpsProductCategory aTmpProductCategory : OpsProductCategory.values() ) {
            if( aProductCategory.equals( aTmpProductCategory )) {
                for( OpsProductSubCategory aTmpProductSubCategory : OpsProductSubCategory.values() )    {
                    if( aTmpProductSubCategory.getSubCategory().equalsIgnoreCase( aSubCategory ))   {
                        aProductSubCategory = aTmpProductSubCategory;
                        break;
                    }
                }
            }
        }

        return aProductSubCategory;
    }

    public OpsProductCategory getCategory() {
        return category;
    }

    public String getSubCategory()  {
        return subCategory;
    }
}
