package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OpsSupplierType {

    SUPPLIER_TYPE_ONLINE("online"), SUPPLIER_TYPE_OFFLINE("offline");

    private String supplierType = null;

    private OpsSupplierType( String newSupplierType )   {
        supplierType = newSupplierType;
    }

    public String getSupplierType() {
        return supplierType;
    }

    public OpsSupplierType fromString( String newSupplierType ) {
        OpsSupplierType aSupplierType = null;

        if( newSupplierType == null || newSupplierType.isEmpty()) {
            return aSupplierType;
        }

        for( OpsSupplierType tmpSupplierType : OpsSupplierType.values() )   {
            if( newSupplierType.equalsIgnoreCase( tmpSupplierType.getSupplierType() ))  {
                aSupplierType = tmpSupplierType;
                break;
            }
        }

        return aSupplierType;
    }

    @JsonValue
    public String toValue() {

        for( OpsSupplierType tmpSupplierType : OpsSupplierType.values() )   {
            if( tmpSupplierType == this )   {
                return getSupplierType();
            }
        }

        return null; // or fail
    }
}
