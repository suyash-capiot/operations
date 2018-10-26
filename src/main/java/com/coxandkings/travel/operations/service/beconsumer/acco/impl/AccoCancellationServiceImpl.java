package com.coxandkings.travel.operations.service.beconsumer.acco.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.service.beconsumer.acco.AccoCancellationService;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service("accoCancellationService")
public class AccoCancellationServiceImpl implements AccoCancellationService {


    @Value(value = "${booking-engine-core-services.acco.modify}")
    private String accoCancellationURL;


    private static final org.apache.log4j.Logger logger = org.apache.log4j.LogManager.getLogger(AccoCancellationServiceImpl.class);


    @Override
    public JSONObject getRoomInfo(OpsRoom opsRoom, OpsHotelDetails opsHotelDetails) {

        try {
            JSONArray references = new JSONArray();
            JSONObject roomInfo = new JSONObject();
            OpsMealInfo mealInfo = opsRoom.getMealInfo();
            OpsRatePlanInfo ratePlanInfo = opsRoom.getRatePlanInfo();
            OpsRoomTypeInfo roomTypeInfo = opsRoom.getRoomTypeInfo();

            roomInfo.put("mealInfo", mealInfo(mealInfo));
            roomInfo.put("hotelInfo", hotelInfo(opsHotelDetails));
            roomInfo.put("references", references);
            roomInfo.put("roomTypeInfo", roomTypeInfo(roomTypeInfo));
            roomInfo.put("ratePlanInfo", ratePlanInfo(ratePlanInfo));
            roomInfo.put("availabilityStatus", "");// todo
            roomInfo.put("requestedRoomIndex", 1);// todo : not sure
            return roomInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject roomTypeInfo(OpsRoomTypeInfo roomTypeInfoNew) {
        try {
            JSONObject roomTypeInfo = new JSONObject();
            roomTypeInfo.put("roomTypeCode", roomTypeInfoNew.getRoomTypeCode());
            roomTypeInfo.put("roomCategoryCode", roomTypeInfoNew.getRoomCategoryID());
            roomTypeInfo.put("roomRef", roomTypeInfoNew.getRoomRef());
            roomTypeInfo.put("roomTypeName", roomTypeInfoNew.getRoomRef());
            roomTypeInfo.put("roomCategoryName", roomTypeInfoNew.getRoomCategoryName());
            return roomTypeInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject ratePlanInfo(OpsRatePlanInfo ratePlanInfoNew) {
        try {
            JSONObject ratePlanInfo = new JSONObject();
            ratePlanInfo.put("ratePlanName", ratePlanInfoNew.getRatePlanname());
            ratePlanInfo.put("ratePlanRef", ratePlanInfoNew.getRatePlanRef());
            ratePlanInfo.put("ratePlanCode", ratePlanInfoNew.getRatePlanCode());
            ratePlanInfo.put("bookingRef", ratePlanInfoNew.getBookingRef());
            return ratePlanInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject hotelInfo(OpsHotelDetails opsHotelDetails) {
        try {
            JSONObject hotelInfo = new JSONObject();
            hotelInfo.put("hotelCode", opsHotelDetails.getHotelCode());
            hotelInfo.put("hotelName", opsHotelDetails.getHotelName());
            hotelInfo.put("hotelRef", opsHotelDetails.getHotelCode());  // todo: not sure
            return hotelInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject mealInfo(OpsMealInfo mealInfoNew) {
        try {
            JSONObject mealInfo = new JSONObject();
            mealInfo.put("mealName", mealInfoNew.getMealName());
            mealInfo.put("mealCode", mealInfoNew.getMealID());
            return mealInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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

    private JSONArray getPaxInfoAcco(OpsProduct opsProduct) {
        List<OpsAccommodationPaxInfo> opsAccommodationPaxInfos = opsProduct.getOrderDetails().getHotelDetails().getRooms().get(0).getPaxInfo();

        JSONArray paxInfoArray = new JSONArray();
        for (OpsAccommodationPaxInfo opsAccommodationPaxInfo : opsAccommodationPaxInfos) {
            JSONObject paxInfo = new JSONObject();
            paxInfo.put("isLeadPax", opsAccommodationPaxInfo.getLeadPax());
            paxInfo.put("firstName", opsAccommodationPaxInfo.getFirstName());
            paxInfo.put("paxType", opsAccommodationPaxInfo.getPaxType());
            paxInfo.put("surname", opsAccommodationPaxInfo.getLastName());
            paxInfo.put("dob", opsAccommodationPaxInfo.getBirthDate());
            paxInfo.put("middleName", opsAccommodationPaxInfo.getMiddleName());
            paxInfo.put("title", opsAccommodationPaxInfo.getTitle());

            JSONArray contactDetailsList = new JSONArray();
            for (OpsContactDetails opsContactDetails : opsAccommodationPaxInfo.getContactDetails()) {

                JSONObject contactDetails = new JSONObject();

                JSONObject contactInfo = new JSONObject();
                contactInfo.put("countryCode", opsContactDetails.getContactInfo().getCountryCode());
                contactInfo.put("contactType", opsContactDetails.getContactInfo().getContactType());
                contactInfo.put("mobileNo", opsContactDetails.getContactInfo().getMobileNo());
                contactInfo.put("email", opsContactDetails.getContactInfo().getEmail());

                contactDetails.put("contactInfo", contactInfo);
                contactDetailsList.put(contactDetails);
            }
            paxInfo.put("contactDetails", contactDetailsList);

            JSONObject addressDetails = new JSONObject();
            addressDetails.put("zip", opsAccommodationPaxInfo.getAddressDetails().getZipCode());
            addressDetails.put("country", opsAccommodationPaxInfo.getAddressDetails().getCountryName());
            addressDetails.put("city", opsAccommodationPaxInfo.getAddressDetails().getCityName());
            addressDetails.put("addrLine1", opsAccommodationPaxInfo.getAddressDetails().getAddressLines().get(0));
            addressDetails.put("addrLine2", opsAccommodationPaxInfo.getAddressDetails().getAddressLines().get(1));
            addressDetails.put("state", opsAccommodationPaxInfo.getAddressDetails().getState());
            paxInfo.put("addressDetails", addressDetails);
            paxInfoArray.put(paxInfo);
        }
        return paxInfoArray;
    }


    private JSONObject accoCancellationRequest(OpsBooking opsBooking, OpsProduct opsProduct) {
        JSONObject requestHeader = getRequestHeader(opsBooking, true);
        JSONArray accommodationInfo = new JSONArray();
        List<OpsRoom> opsRooms = opsProduct.getOrderDetails().getHotelDetails().getRooms();
        for (OpsRoom opsRoom : opsRooms) {
            JSONObject accoInfo = new JSONObject();
            accoInfo.put("accommodationSubType", opsProduct.getProductSubCategory());
            accoInfo.put("orderID", opsProduct.getOrderID());
            accoInfo.put("checkIn", opsRoom.getCheckIn());
            accoInfo.put("supplierRef", opsProduct.getSupplierID());
            accoInfo.put("countryCode", opsProduct.getOrderDetails().getHotelDetails().getCountryCode());
            accoInfo.put("cityCode", opsProduct.getOrderDetails().getHotelDetails().getCityCode());
            accoInfo.put("checkOut", opsRoom.getCheckOut());
            accoInfo.put("modificationType", "FULLCANCELLATION"); //for cancellation of product
            accoInfo.put("supplierReferenceId", opsProduct.getSupplierReferenceId());
            accoInfo.put("clientReferenceId", opsProduct.getClientReferenceId());
            accoInfo.put("supplierCancellationId", opsProduct.getSupplierCancellationId());
            accoInfo.put("supplierReservationId", opsProduct.getSupplierReservationId());
            accoInfo.put("supplierRoomIndex", opsRoom.getSupplierRoomIndex());
            JSONArray paxInfoArray = new JSONArray();
            for (OpsAccommodationPaxInfo accommodationPaxInfo : opsProduct.getOrderDetails().getHotelDetails().getRooms().get(0).getPaxInfo()) {
                JSONObject paxInfo = new JSONObject(accommodationPaxInfo);
                paxInfoArray.put(paxInfo);
            }
            try {
                accoInfo.put("paxInfo", getPaxInfoAcco(opsProduct));
            } catch (Exception e) {
                e.printStackTrace();
            }
            accoInfo.put("roomInfo", getRoomInfo(opsRoom, opsProduct.getOrderDetails().getHotelDetails()));
            accommodationInfo.put(accoInfo);
        }
        JSONObject requestBody = new JSONObject();
        requestBody.put("accommodationInfo", accommodationInfo);
        requestBody.put("bookID", opsBooking.getBookID());
        JSONObject request = new JSONObject();
        request.put("requestHeader", requestHeader);
        request.put("requestBody", requestBody);
        return request;
    }

    @Override
    public String processCancellation(OpsBooking opsBooking, OpsProduct opsProduct) throws OperationException {
        JSONObject accoCancelRequest = accoCancellationRequest(opsBooking, opsProduct);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity<>(accoCancelRequest.toString(), headers);
        ResponseEntity<String> accoCancelResponse = null;
        try {
            logger.info("****Cancel Request***");
            logger.info(accoCancelRequest.toString());
            accoCancelResponse = RestUtils.exchange(accoCancellationURL, HttpMethod.POST, httpEntity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Cancellation of acco product is not successful");
            throw new OperationException("Cancellation is not successful in book id " + opsBooking.getBookID() + "for the order id " + opsProduct.getOrderID());
        }
        return accoCancelResponse.getBody();
    }


}