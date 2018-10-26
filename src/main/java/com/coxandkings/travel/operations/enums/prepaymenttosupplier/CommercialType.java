package com.coxandkings.travel.operations.enums.prepaymenttosupplier;

import org.springframework.util.StringUtils;

public enum CommercialType {
    RECEIVABLE( "Receivable" ),
    PAYABLE ( "Payable" );

    private String commercialType;

    CommercialType(String type) {
        this.commercialType=type;
    }

    public String getCommercialType()
    {
        return commercialType;
    }


    public static CommercialType fromString( String newtype ) {
        CommercialType type = null;
        if(StringUtils.isEmpty(newtype)) {
            return null;
        }

        for(CommercialType tmpType: CommercialType.values()) {
            if(tmpType.getCommercialType().equalsIgnoreCase(newtype)) {
                type = tmpType;
                break;
            }
        }

        return type;
    }

}
