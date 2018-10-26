package com.coxandkings.travel.operations.service.managepickupdropoff;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.managepickupdropoff.PickUpDropOff;
import com.coxandkings.travel.operations.resource.managepickupdropoff.PickUpDropOffResource;

public interface PickUpDropOffService {

    public PickUpDropOff savePickUpDropOff(PickUpDropOffResource resource) throws OperationException;

    public PickUpDropOff updatePickUpDropOff(PickUpDropOffResource resource) throws OperationException;

    public PickUpDropOff searchPickUpDropOff(String bookingRefId, String orderId) throws OperationException;
}
