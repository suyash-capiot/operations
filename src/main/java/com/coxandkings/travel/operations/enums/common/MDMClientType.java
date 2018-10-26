package com.coxandkings.travel.operations.enums.common;

public enum MDMClientType {

    B2B ( "B2B" ), B2C ( "B2C"), B2E ( "B2E" );

    private String clientType;

    private MDMClientType( String newClientType )   {
        clientType = newClientType;
    }

    public String getClientType()   {
        return clientType;
    }

    public static MDMClientType fromString( String newClientType )  {
        MDMClientType aClientType = null;
        if( newClientType == null || newClientType.trim().length() == 0 )   {
            return aClientType;
        }

        for( MDMClientType tmpClientType : MDMClientType.values())  {
            if( tmpClientType.getClientType().equalsIgnoreCase( newClientType ))   {
                aClientType = tmpClientType;
                break;
            }
        }
        return aClientType;
    }
}
