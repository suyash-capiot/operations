package com.coxandkings.travel.operations.service.beconsumer.acco;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import org.json.JSONException;

public interface AccoBookingEngineConsumptionService {
    String search(OpsBooking opsBooking, OpsProduct opsProduct);

    String getPrice(OpsBooking opsBooking, OpsProduct opsProduct, String supplierId);

    String getReprice(OpsBooking opsBooking, OpsProduct opsProduct, String supplierId) throws JSONException;

    String book(OpsBooking opsBooking, OpsProduct opsProduct,String supplierId) throws JSONException;

    String bookFromExistingBooking(OpsBooking opsBooking, OpsProduct opsProduct, String supplierId) throws JSONException, OperationException;

    Object cancelRoom(OpsBooking opsBooking, OpsProduct opsProduct, String roomId) throws OperationException;
}
