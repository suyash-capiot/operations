package com.coxandkings.travel.operations.service.beconsumer.air.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsPaymentInfo;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.service.beconsumer.air.AirCancellationService;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class AirCancellationServiceImpl implements AirCancellationService {
    private static final Logger logger = LogManager.getLogger(AirCancellationServiceImpl.class);

    @Value(value = "${booking-engine-core-services.air.cancel}")
    private String airCancelServiceUrl;


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
        return clientContext;
    }


    private JSONObject airCancelRequest(OpsBooking opsBooking, OpsProduct opsProduct) {
        JSONObject requestHeader = getRequestHeader(opsBooking, true);
        JSONObject requestBody = new JSONObject();
        requestBody.put("cancelType", "ALL"); //for cancellation of the product
        requestBody.put("bookID", opsBooking.getBookID());
        JSONArray supplierBookingReferences = new JSONArray();
        JSONObject supplierBookingReference = new JSONObject();
        supplierBookingReference.put("orderID", opsProduct.getOrderID());
        supplierBookingReference.put("supplierRef", opsProduct.getSupplierID());
        supplierBookingReference.put("bookRefID", opsProduct.getOrderDetails().getFlightDetails().getAirlinePNR());
        supplierBookingReferences.put(supplierBookingReference);
        requestBody.put("supplierBookReferences", supplierBookingReferences);
        requestBody.put("paymentInfo", getPaymentInfo(opsBooking.getPaymentInfo(), null));
        JSONObject request = new JSONObject();
        request.put("requestHeader", requestHeader);
        request.put("requestBody", requestBody);
        return request;
    }

    private JSONArray getPaymentInfo(List<OpsPaymentInfo> paymentInfos, Double amount) {
        JSONArray paymentInfoList = new JSONArray();
        for (OpsPaymentInfo opsPaymentInfo : paymentInfos) {
            JSONObject paymentInfo = new JSONObject();
            paymentInfo.put("paymentMethod", (opsPaymentInfo.getPaymentMethod() == null) ? "" : opsPaymentInfo.getPaymentMethod());

            if (amount != null) {
                paymentInfo.put("paymentAmount", amount);
            } else {
                paymentInfo.put("paymentAmount", (opsPaymentInfo.getPaymentAmount() == null) ? "" : opsPaymentInfo.getPaymentAmount());
            }
            paymentInfo.put("paymentType", (opsPaymentInfo.getPaymentType() == null) ? "" : opsPaymentInfo.getPaymentType());
            paymentInfo.put("amountCurrency", (opsPaymentInfo.getAmountCurrency() == null) ? "" : opsPaymentInfo.getAmountCurrency());
            paymentInfo.put("cardType", (opsPaymentInfo.getCardType() == null) ? "" : opsPaymentInfo.getCardType());
            paymentInfo.put("cardNumber", (opsPaymentInfo.getCardNumber() == null) ? "" : opsPaymentInfo.getCardNumber());
            paymentInfo.put("cardExpiry", (opsPaymentInfo.getCardType() == null) ? "" : opsPaymentInfo.getCardType());
            paymentInfo.put("encryptionKey", (opsPaymentInfo.getEncryptionKey() == null) ? "" : opsPaymentInfo.getEncryptionKey());
            paymentInfo.put("token", (opsPaymentInfo.getToken() == null) ? "" : opsPaymentInfo.getToken());
            paymentInfo.put("accountType", (opsPaymentInfo.getAccountType() == null) ? "" : opsPaymentInfo.getAccountType());
            paymentInfo.put("merchantId", "");
            paymentInfo.put("totalAmount", "");
            paymentInfo.put("merchantTxnId", "");
            paymentInfo.put("transactionDate", "");
            paymentInfo.put("amountPaid", "");
            paymentInfo.put("bankName", "");
            paymentInfo.put("nameOnCard", "");
            paymentInfo.put("paymentStatus", "");
            paymentInfo.put("bankAccount", "");
            paymentInfo.put("depositRefNumber", "");
            paymentInfo.put("noOfProducts", "");
            paymentInfoList.put(paymentInfo);
        }
        return paymentInfoList;
    }

    public String processCancellation(OpsBooking opsBooking, OpsProduct opsProduct) throws OperationException {
        JSONObject airCancelRequest = airCancelRequest(opsBooking, opsProduct);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JSONObject> httpEntity = new HttpEntity(airCancelRequest.toString(), headers);
        ResponseEntity<String> airCancelResponse = null;
        try {
            logger.info("****Cancel Request***");
            logger.info(airCancelRequest.toString());
            airCancelResponse = RestUtils.exchange(airCancelServiceUrl, HttpMethod.POST, httpEntity, String.class);
        } catch (Exception e) {
            logger.error("Cancellation of air product is not successful");
            e.printStackTrace();
            throw new OperationException("Cancellation is not successful in book id " + opsBooking.getBookID() + "for the order id " + opsProduct.getOrderID());
        }
        return airCancelResponse.getBody();
    }

}
