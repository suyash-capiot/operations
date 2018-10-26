package com.coxandkings.travel.operations.service.beconsumer.pnrstatus;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import org.json.JSONArray;

public interface RetrievePnrStatusService {
    String retrievePnrStatusFromPnr(OpsBooking opsBooking, OpsProduct product) throws OperationException;
}
