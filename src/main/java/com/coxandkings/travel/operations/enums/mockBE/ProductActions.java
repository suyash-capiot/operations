package com.coxandkings.travel.operations.enums.mockBE;

public enum ProductActions {

    CHANGE_SUPPLIER_NAME("change_supplier_name"),
    UPDATE_TIME_LIMIT_EXPIRE("update_time_limit_expire"),
    AMEND_CLIENT_COMMERCIALS("amend_client_Commercials"),
    MANAGE_RECONFIRMATION("manage_reconfirmation");


    private String actionName;

    private ProductActions(String newActionName ) {
        actionName = newActionName;
    }

    public String getActionType()   {
        return actionName;
    }

    //ProductActions.HANGE_SUPPLIER_NAME.getActionType();

}
