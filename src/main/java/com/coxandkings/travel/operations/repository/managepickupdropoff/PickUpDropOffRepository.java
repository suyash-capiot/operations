package com.coxandkings.travel.operations.repository.managepickupdropoff;

import com.coxandkings.travel.operations.model.managepickupdropoff.PickUpDropOff;

public interface PickUpDropOffRepository {

    public PickUpDropOff savePickUpDropOffDetails(PickUpDropOff pickUpDropOff);

    public PickUpDropOff searchPickUpDropOff(PickUpDropOff pickUpDropOff);
}
