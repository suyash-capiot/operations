package com.coxandkings.travel.operations.service.beconsumer.holidays;

import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;

public interface HolidaysBookingEngineConsumptionService {

	String search(OpsBooking opsBooking, OpsProduct product);

}
