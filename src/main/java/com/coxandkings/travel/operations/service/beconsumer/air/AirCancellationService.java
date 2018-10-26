package com.coxandkings.travel.operations.service.beconsumer.air;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;

public interface AirCancellationService {

    public String processCancellation(OpsBooking opsBooking, OpsProduct opsProduct) throws OperationException;
}
