package com.coxandkings.travel.operations.service.beconsumer.holidays.helper;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.coxandkings.travel.operations.model.core.OpsBooking;
@Component
public class HolidaysBookingEngineConsumptionServiceHelper {

	public JSONObject getRequestHeader(OpsBooking opsBooking) {
        JSONObject requestHeader = new JSONObject();

        requestHeader.put("sessionID", opsBooking.getSessionID());
        requestHeader.put("transactionID", opsBooking.getTransactionID());
        requestHeader.put("userID", opsBooking.getUserID());
        requestHeader.put("clientContext", getClientContextJson(opsBooking));
        return requestHeader;
    }

	 private JSONObject getClientContextJson(OpsBooking opsBooking) throws JSONException {
	        JSONObject clientContext = new JSONObject();
	        clientContext.put("clientID", opsBooking.getClientID());
	        clientContext.put("clientMarket", opsBooking.getClientMarket());
	        clientContext.put("clientType", opsBooking.getClientType());
	        clientContext.put("clientLanguage", opsBooking.getClientLanguage());
	        clientContext.put("clientIATANumber", opsBooking.getClientIATANumber());
	        clientContext.put("pointOfSale", opsBooking.getPointOfSale());

	        clientContext.put("clientNationality", ""); // todo be not sending currently
	        clientContext.put("clientCallbackAddress", "");// todo be not sending currently
	        clientContext.put("clientCurrency", opsBooking.getClientCurrency());
	        //clientContext.put("company", "BookEngCNKIndia");
	        //clientContext.put("websiteUrl", "http://localhost:8089/AccoService/search");
	        return clientContext;
	    }
}
