package com.coxandkings.travel.operations.reconfirmation.common;

import java.math.BigDecimal;

public enum CommonEnums {

    PAGE( "123" ),
    SIZE( "10" ),
    ORDER_ASC( "asc" ),
    order_desc( "desc" ),
    sort( "xxxxxx" );

    private String value;

    CommonEnums( String value ) {
        this.value = value;
    }

    public String getValue( ) {
        return value;
    }

    public void setValue( String value ) {
        this.value = value;
    }

    public Long getLongValue( ) {
        return Long.parseLong( value );
    }

    public Integer getIntegerValue( ) {
        return Integer.parseInt( value );
    }

    public BigDecimal getBigDecimalValue( ) {
        return new BigDecimal( value );
    }
}
