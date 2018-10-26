package com.coxandkings.travel.operations.service.reconfirmation.common;

public enum ClientSupplierCommercialCommonEnums {

    CLIENT_BY_ID_MDM_URL("reconfirmation.mdm.client"),
    SUPPLIER_BY_ID_MDM_URL("reconfirmation.mdm.supplier"),
    SUPPLIER("supplier"),
    CLIENT_PROFILE("clientProfile"),
    ASC("ASC"),
    DESC("DESC"),
    TOKEN_URL("reconfirmation.mdm.usermgmt"),
    TOKEN("token"),
    PASSWORD("reconfirmation.mdm.usermgmt.password"),
    USERNAME("reconfirmation.mdm.usermgmt.username"),
    COLON(":"),
    COMMA(","),
    DOUBLE_QUITS("\""),
    ZERO("'"),
    PRIOR_TO_TRAVEL_DATE("Prior to Travel Date"),
    FROM_BOOKING_DATE("From Booking Date"),



    RECONFIRMATION_CONFIG_SEARCH_URL("reconfirmation.mdm.reconfirmation-config-search"),
    RECONFIRMATION_CONFIG_SINGLE_RECORD_URL("reconfirmation.mdm.reconfirmation-config-single-record"),
    TIME_LIMIT_CONFIG_SINGLE_RECORD_URL("reconfirmation.mdm.time-limit-config-single-record"),
    TIME_LIMIT_CONFIG_SEARCH_URL("reconfirmation.mdm.time-limit-config-search"),
    BOOKING_DETAILS_URL("reconfirmation.mdm.booking-details-single-record"),

    ;

    private String value;

    ClientSupplierCommercialCommonEnums(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
