package com.coxandkings.travel.operations.service.beconsumer.air;

import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.resource.doTicketing.DoTicketingResource;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;

public interface AirBookingEngineConsumptionService {
    String getPriceForAirJson(OpsBooking opsBooking, OpsProduct opsProduct, String supplierName) throws JSONException, ParseException, IOException;
    JSONObject getRePriceResponseForAirJson(JSONObject rePriceRequest) throws JSONException, ParseException, IOException;
    String search(OpsBooking opsBooking, OpsProduct opsProduct) throws JSONException;
    String getRePriceForAirJson(OpsBooking opsBooking, OpsProduct opsProduct, String supplierName) throws JSONException, ParseException, IOException;
    JSONObject getRePriceRequestForAirJson(OpsBooking opsBooking, OpsProduct opsProduct) throws JSONException, ParseException, IOException;

    JSONObject getBookRequestForAirJson(OpsBooking opsBooking, OpsProduct opsProduct, String supplierID);

    JSONObject getIssueTicketRequestForAirJson(OpsBooking opsBooking, OpsProduct opsProduct, DoTicketingResource doTicketingResource);

    JSONObject getIssueTicketResponseAir(OpsBooking opsBooking, OpsProduct opsProduct, DoTicketingResource doTicketingResource);
    JSONObject createBEAirBookRQFromRepriceRS(JSONObject repriceRs, OpsBooking opsBooking, OpsProduct opsProduct);
}
