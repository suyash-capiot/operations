package com.coxandkings.travel.operations.service.beconsumer.pnrstatus.impl;

import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.service.beconsumer.pnrstatus.RetrievePnrStatusService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RetrievePnrStatusServiceServiceImpl implements RetrievePnrStatusService {

    @Value(value = "${booking-engine-core-services.air.retrieve_pnr_status}")
    private String retrieveAirServiceUrl;

    @Value(value = "${booking-engine-core-services.acco.retrieve_pnr_status}")
    private String retrieveAccoServiceUrl;

    @Autowired
    RestUtils restUtils;

    @Override
    public String retrievePnrStatusFromPnr(OpsBooking opsBooking, OpsProduct product) throws OperationException {
        JSONObject requestHeader = getRequestHeader(opsBooking, true);
        JSONArray response = new JSONArray();
        String pnrStatus = null;
        if (product.getOpsProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION) && product.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT)) {
            String airlinePnr = product.getOrderDetails().getFlightDetails().getAirlinePNR();
            String supplierRef = product.getSupplierID();
            response = checkSupplierAir(airlinePnr, supplierRef, requestHeader);
            //TODO BE yet to provide Order Level Status
            if(null == supplierRef){
                throw new OperationException(Constants.OPS_ERR_11259);
            }
            //TODO: Fix status after BE gives Proper Response
            pnrStatus = response.getJSONObject(0).getString("orderStatus");
        } else if (product.getOpsProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION) && product.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS)) {
            String supplierRef = product.getSupplierID();
            String supplierReservationId = product.getSupplierReservationId();
            String accommodationSubType = product.getProductSubCategory();
            response = checkSupplierAcco(accommodationSubType, supplierRef, supplierReservationId, requestHeader);
            if(null == supplierRef){
                throw new OperationException(Constants.OPS_ERR_11259);
            }
            pnrStatus = response.getJSONObject(0).getString("status");
        }

        return pnrStatus;
    }


    public JSONObject getRequestHeader(OpsBooking booking, Boolean transaction) throws JSONException {
        JSONObject requestHeader = new JSONObject();
        requestHeader.put("userID", booking.getUserID());
        Random random = new Random();
        requestHeader.put("sessionID", booking.getSessionID() + random.nextInt(100));
        if (transaction) {
            requestHeader.put("transactionID", booking.getTransactionID());
        } else {
            requestHeader.put("transactionID", "");
        }
        requestHeader.put("clientContext", getClientContextJson(booking));
        return requestHeader;
    }


    private JSONArray checkSupplierAir(String bookRefId, String supplierRef, JSONObject requestHeader) {
        JSONObject supplierBookReferences = new JSONObject();
        supplierBookReferences.put("supplierRef", supplierRef);
        supplierBookReferences.put("bookRefID", bookRefId);
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(supplierBookReferences);

        JSONObject requestBody = new JSONObject();
        requestBody.put("supplierBookReferences", jsonArray);

        JSONObject request = new JSONObject();
        request.put("requestHeader", requestHeader);
        request.put("requestBody", requestBody);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

        ResponseEntity<String> response = restUtils.postForEntity(this.retrieveAirServiceUrl, entity, String.class);
        JSONArray responseObject = (new JSONObject(response.getBody())).getJSONObject("responseBody").getJSONArray("pricedItinerary");
        return responseObject;
    }

    private JSONArray checkSupplierAcco(String accommodationSubType, String supplierRef, String supplierReservationId, JSONObject requestHeader) {
        JSONObject accommodationInfo = new JSONObject();
        accommodationInfo.put("supplierRef", supplierRef);
        accommodationInfo.put("supplierReservationId", supplierReservationId);
        accommodationInfo.put("accommodationSubType", accommodationSubType);

        JSONObject requestBody = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(accommodationInfo);
        requestBody.put("accommodationInfo", jsonArray);

        JSONObject request = new JSONObject();
        request.put("requestHeader", requestHeader);
        request.put("requestBody", requestBody);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

        //URI uri = URI.create(this.retrieveAccoServiceUrl);
        ResponseEntity<String> response = restUtils.postForEntity(this.retrieveAccoServiceUrl, entity, String.class);
        JSONArray responseObject = (new JSONObject(response.getBody())).getJSONObject("responseBody").getJSONArray("accommodationInfo");
        return responseObject;
    }

    private JSONObject getClientContextJson(OpsBooking opsBooking) {
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
