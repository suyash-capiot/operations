package com.coxandkings.travel.operations.service.productsharing;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsHotelDetails;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.core.OpsRoom;
import org.json.JSONObject;

/**
 *
 */
public interface UpdateRoomService {
    /**
     * @param opsRoom
     * @param opsHotelDetails
     * @return
     */
    JSONObject getRoomInfo(OpsRoom opsRoom, OpsHotelDetails opsHotelDetails);

    /**
     * @param opsBooking
     * @param opsProduct
     * @return
     * @throws OperationException
     */
    Object processRoomUpdate(OpsBooking opsBooking, OpsProduct opsProduct) throws OperationException;

}
