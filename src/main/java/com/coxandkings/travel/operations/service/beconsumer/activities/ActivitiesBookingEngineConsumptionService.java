package com.coxandkings.travel.operations.service.beconsumer.activities;

import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;

public interface ActivitiesBookingEngineConsumptionService {

	String search(OpsBooking opsBooking, OpsProduct product);

}
