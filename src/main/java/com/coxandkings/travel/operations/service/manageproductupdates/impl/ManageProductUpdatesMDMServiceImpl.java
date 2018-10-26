package com.coxandkings.travel.operations.service.manageproductupdates.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.manageproductupdates.CheaperPriceUpdatesFlightInfo;
import com.coxandkings.travel.operations.model.manageproductupdates.CheaperUpdatesHotelInfo;
import com.coxandkings.travel.operations.service.manageproductupdates.ManageProductUpdatesMDMService;
import org.springframework.stereotype.Service;

@Service
public class ManageProductUpdatesMDMServiceImpl implements ManageProductUpdatesMDMService {

    @Override
    public boolean isCheaperPriceAvailable(CheaperPriceUpdatesFlightInfo aFlightBooking) throws OperationException {
        //TODO - MDM Integeration
        return true;
    }

    @Override
    public boolean isCheaperPriceAvailable(CheaperUpdatesHotelInfo aFlightBooking) throws OperationException {
        //TODO - MDM Integeration
        return true;
    }
}
