package com.coxandkings.travel.operations.service.beconsumer.acco;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsHotelDetails;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.core.OpsRoom;
import org.json.JSONObject;

public interface AccoCancellationService {

    String processCancellation(OpsBooking opsBooking, OpsProduct opsProduct) throws OperationException;

    JSONObject getRoomInfo(OpsRoom opsRoom, OpsHotelDetails opsHotelDetails);

}
