package com.coxandkings.travel.operations.enums;

public enum ProductActions {

    CHANGE_SUPPLIER_NAME("change_supplier_name"),
    UPDATE_TIME_LIMIT_EXPIRE("update_time_limit_expire"),
    AMEND_CLIENT_COMMERCIALS("amend_client_Commercials"),
    PRE_PAYMENT_TO_SUPPLIER("pre_payment_to_supplier"),
    ADD_DRIVER_DETAILS("add_driver_details");


    private String actionName;

    private ProductActions(String newActionName ) {
        actionName = newActionName;
    }

    public String getActionType()   {
        return actionName;
    }

    //ProductActions.HANGE_SUPPLIER_NAME.getActionType();

}