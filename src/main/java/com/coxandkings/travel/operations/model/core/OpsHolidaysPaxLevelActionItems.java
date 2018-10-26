package com.coxandkings.travel.operations.model.core;

import com.coxandkings.travel.operations.enums.ActionItemStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpsHolidaysPaxLevelActionItems {

    @JsonIgnore
    String[] actionItemNames = {"updateClientReconfirmationDate", "updateSupplierReconfirmationDate",
            "viewClientCustomerReconfirmationFlags", "viewSupplierReconfirmationFlags", "viewSharedBookingDetails",
            "discountOnSellingPrice", "discountOnSupplierPrice", "supplementToSupplierPrice", "amendSupplierCommercials"
            , "amendClientCommercials", "amendCompanyCommercials"};
    @JsonIgnore
    String[] actionItemLabels = {"Update Client Reconfirmation Date", "Update Supplier Reconfirmation Date"
            , "View Client Customer Reconfirmation Flags", "View Supplier Reconfirmation Flags", "View Shared Booking Details",
            "Discount on Selling Price", "Discount on Supplier Price", "Supplement to Supplier Price", "Amend�Supplier Commercials"
            , "Amend�Client Commercials", "Amend�Company Commercials"};
    @JsonIgnore
    Boolean[] isInlineActionItem = {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE,
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE
            , Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
    //To confirm if it is to be added for holidays
    //It is present for flights
    @JsonIgnore
    String functionName[] = {"", "", "", "", "", "", "Discount on Supplier Price", "Supplement to Supplier Price", "", "", ""};
    @JsonIgnore
    private Map<String, OpsActionItem> actionItemsMap = new HashMap<String, OpsActionItem>();
    private List<OpsActionItem> actionItemList = new ArrayList<OpsActionItem>();


    public OpsHolidaysPaxLevelActionItems() {
        super();
        init();

        System.out.println("Enterted OpsHolidaysPaxLevelActionItems");
    }

    public void init() {
        actionItemsMap.clear();
        actionItemList.clear();
        int index = 0;
        for (String anActionItemName : actionItemNames) {
            OpsActionItem anActionItem = new OpsActionItem(actionItemLabels[index],
                    anActionItemName, ActionItemStatus.ACTIVE.getActionItemStatus(), isInlineActionItem[index], functionName[index]);
            actionItemList.add(anActionItem);
            index++;
        }
    }

    public List<OpsActionItem> getActionItemList() {
//    init();
        return actionItemList;
    }

    public void setActionItemList(List<OpsActionItem> actionItemList) {
        this.actionItemList = actionItemList;
    }

    public void setActionItemsMap(Map<String, OpsActionItem> newProperties) {
        actionItemsMap = newProperties;
    }

}
