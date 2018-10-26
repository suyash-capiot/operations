package com.coxandkings.travel.operations.model.core;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;

import java.util.ArrayList;

public class OpsOrderLevelActionItems {

    private ArrayList<OpsActionItem> actionItemsList = new ArrayList<>();

    public OpsOrderLevelActionItems( OpsProductSubCategory newProductSubCategory )    {
        switch( newProductSubCategory ) {
            case PRODUCT_SUB_CATEGORY_FLIGHT:   {
                OpsFlightOrderLevelActionItems flightActionItemsList = new OpsFlightOrderLevelActionItems();
                actionItemsList = flightActionItemsList.getFlightOrderActionItemsList();
            }
            break;

            case PRODUCT_SUB_CATEGORY_HOTELS:   {
                OpsHotelOrderLevelActionItems hotelActionItemsList = new OpsHotelOrderLevelActionItems();
                actionItemsList = hotelActionItemsList.getHotelOrderActionItemsList();
            }
            break;

            case PRODUCT_SUB_CATEGORY_HOLIDAYS: {
                OpsHolidaysOrderLevelActionItems holidaysActionItemsList = new OpsHolidaysOrderLevelActionItems();
                actionItemsList = holidaysActionItemsList.getHolidaysOrderActionItemsList();
            }
            break;
        }
    }

    public ArrayList<OpsActionItem> getActionItemsList() {
        return actionItemsList;
    }

    public void setActionItemsList(ArrayList<OpsActionItem> actionItemsList) {
        this.actionItemsList = actionItemsList;
    }
}


