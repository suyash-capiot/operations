package com.coxandkings.travel.operations.service.manageproductupdates;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.manageproductupdates.CheaperPriceUpdatesFlightInfo;
import com.coxandkings.travel.operations.model.manageproductupdates.CheaperUpdatesHotelInfo;

import java.util.List;

public interface ManageProductUpdatesBEService {

    /**
     *  Get Bookings from Booking2 Engine:
     *  Exclude Criteria: Exclude Flights done using Online supplier, Exclude Flights which are in Ticketed status
     *  Include Criteria: Include all Non Air Products made with Online Suppliers, Include all Flight Bookings made with Offline Suppliers
     * @return
     */
    public List<CheaperPriceUpdatesFlightInfo> getFlightBookingsForProductUpdates() throws OperationException;

    /**
     *  Get Bookings from Booking2 Engine
     * @return
     */
    public List<CheaperUpdatesHotelInfo> getHotelBookingsForProductUpdates() throws OperationException;

}
