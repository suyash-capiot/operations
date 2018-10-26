package com.coxandkings.travel.operations.utils;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.model.core.*;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;

@Component
public class ActionItemsHelper {

    public void updateActionItemsForTimeLimitBooking(OpsBooking aBooking, OpsProduct aProduct,
                                                     OpsActionItemDetails actionItems, OpsProductSubCategory newProductSubCategory) {

        final String timeLimitExpiryConstant = "timeLimitExpiry";
        final String convertTimeLimitToPaid = "convertTimeLimitToPaid";

        OpsOrderDetails anOrder = aProduct.getOrderDetails();
        ZonedDateTime tlExpiryDateTime = null;

        switch (newProductSubCategory) {
            case PRODUCT_SUB_CATEGORY_FLIGHT: {
                OpsFlightDetails aFlightOrder = anOrder.getFlightDetails();
                tlExpiryDateTime = aFlightOrder.getTimeLimitExpiryDate();
            }
            break;

            case PRODUCT_SUB_CATEGORY_HOTELS: {
                OpsHotelDetails aHotelOrder = anOrder.getHotelDetails();
                tlExpiryDateTime = aHotelOrder.getTimeLimitExpiryDate();
            }
            break;
        }
        OpsOrderLevelActionItems orderLevelItems = actionItems.getOpsActionItemsMaster();
        List<OpsActionItem> orderActionItems = orderLevelItems.getActionItemsList();

        if (tlExpiryDateTime == null) {
            if (orderActionItems != null && orderActionItems.size() > 0) {
                Iterator<OpsActionItem> actionItemsItr = orderActionItems.iterator();

                while (actionItemsItr.hasNext()) {
                    OpsActionItem anActionItem = actionItemsItr.next();
                    String itemType = anActionItem.getValue();
                    if (itemType.equalsIgnoreCase(timeLimitExpiryConstant)) {
                        actionItemsItr.remove();
                    } else if (itemType.equalsIgnoreCase(convertTimeLimitToPaid)) {
                        actionItemsItr.remove();
                    }
                }
            }
        }
    }
}

