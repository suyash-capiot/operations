package com.coxandkings.travel.operations.service.manageproductupdates;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.manageproductupdates.CheaperPriceUpdatesFlightInfo;
import com.coxandkings.travel.operations.model.manageproductupdates.CheaperUpdatesHotelInfo;

public interface ManageProductUpdatesMDMService {

    public boolean isCheaperPriceAvailable( CheaperPriceUpdatesFlightInfo aFlightBooking ) throws OperationException;

    public boolean isCheaperPriceAvailable( CheaperUpdatesHotelInfo aFlightBooking ) throws OperationException;
}
