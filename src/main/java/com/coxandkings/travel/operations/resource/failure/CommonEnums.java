package com.coxandkings.travel.operations.resource.failure;

public enum CommonEnums {

    ORDER_ASC( "asc" ),
    ORDER_DESC( "desc" );

    private String value;

    CommonEnums(String value ) {
        this.value = value;
    }

    public String getValue( ) {
        return value;
    }

    public void setValue( String value ) {
        this.value = value;
    }

}
