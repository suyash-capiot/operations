package com.coxandkings.travel.operations.model.core;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OpsActionItemDetails {

    @JsonProperty("orderActionItems")
    private OpsOrderLevelActionItems opsActionItemsMaster;

    @JsonProperty("roomActionItems")
    @JsonInclude (JsonInclude.Include.NON_NULL)
    private OpsHotelRoomLevelActionItems roomActionItems = null;

    @JsonProperty("flightActionItems")
    @JsonInclude (JsonInclude.Include.NON_NULL)
    private OpsFlightPaxLevelActionItems flightActionItems = null;

    @JsonProperty("holidaysActionItems")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OpsHolidaysPaxLevelActionItems holidaysActionItems = null;

    public OpsActionItemDetails( OpsProductSubCategory newProductSubCategory )   {
        opsActionItemsMaster = new OpsOrderLevelActionItems( newProductSubCategory );

        switch ( newProductSubCategory )    {
            case PRODUCT_SUB_CATEGORY_FLIGHT: {
                flightActionItems = new OpsFlightPaxLevelActionItems();
            }
            break;

            case PRODUCT_SUB_CATEGORY_HOTELS:   {
                roomActionItems = new OpsHotelRoomLevelActionItems();
            }
            break;

            case PRODUCT_SUB_CATEGORY_HOLIDAYS: {
                holidaysActionItems = new OpsHolidaysPaxLevelActionItems();
            }
            break;
        }
    }

    public OpsOrderLevelActionItems getOpsActionItemsMaster() {
        return opsActionItemsMaster;
    }

    public void setOpsActionItemsMaster(OpsOrderLevelActionItems opsActionItemsMaster) {
        this.opsActionItemsMaster = opsActionItemsMaster;
    }

    public OpsHotelRoomLevelActionItems getRoomActionItems() {
        return roomActionItems;
    }

    public void setRoomActionItems(OpsHotelRoomLevelActionItems roomActionItems) {
        this.roomActionItems = roomActionItems;
    }

    public OpsFlightPaxLevelActionItems getFlightActionItems() {
        return flightActionItems;
    }

    public void setFlightActionItems(OpsFlightPaxLevelActionItems flightActionItems) {
        this.flightActionItems = flightActionItems;
    }

    public OpsHolidaysPaxLevelActionItems getHolidaysActionItems() {
        return holidaysActionItems;
    }

    public void setHolidaysActionItems(OpsHolidaysPaxLevelActionItems holidaysActionItems) {
        this.holidaysActionItems = holidaysActionItems;
    }
}
