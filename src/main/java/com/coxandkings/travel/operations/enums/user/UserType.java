package com.coxandkings.travel.operations.enums.user;

public enum UserType {

    OPS_USER( "OpsUser" ), OPS_APPROVER_USER( "OpsApprovalUser" ), OPS_EMAIL_MONITOR_USER ("OpsEmailMonitorUser" ),
    OPS_SUPPLIER_EXTRANET( "OpsSupplierExtranet" ), OPS_MARKETING_USER( "Marketing User" ), OPS_PRODUCT_USER ( "AbstractProductFactory User" );

    private String userType;

    private UserType( String newUserType )  {
        userType = newUserType;
    }

    public String getUserType() {
        return userType;
    }

    public static UserType fromString( String newUserType ) {
        UserType aUsrType = null;

        if( newUserType != null && newUserType.trim().length() > 0 ) {
            for (UserType tmpUsrType : UserType.values()) {
                if (tmpUsrType.getUserType().equalsIgnoreCase(newUserType)) {
                    aUsrType = tmpUsrType;
                    break;
                }
            }
        }

        return aUsrType;
    }
}
