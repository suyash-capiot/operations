package com.coxandkings.travel.operations.service.beconsumer.acco.helper;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/*
    Use This class to make request for following
    1. search
    2. Book
    3. Price
    4. Reprice

 */
@Component
public class AccoBookingEngineConsumptionHelper {

    private RestTemplate restTemplate;
    @Value("${booking_engine.get.documents}")
    private String docUrl;

    public JSONObject getRequestHeader(OpsBooking opsBooking) throws JSONException {
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

    public JSONObject getRoomInfo(OpsRoom opsRoom, OpsHotelDetails opsHotelDetails) {
        JSONObject roomInfo = new JSONObject();
        ;
        try {

            JSONArray references = new JSONArray();

            OpsMealInfo mealInfo = opsRoom.getMealInfo();
            OpsRatePlanInfo ratePlanInfo = opsRoom.getRatePlanInfo();
            OpsRoomTypeInfo roomTypeInfo = opsRoom.getRoomTypeInfo();

            roomInfo.put("mealInfo", mealInfo(mealInfo));
            roomInfo.put("hotelInfo", hotelInfo(opsHotelDetails));
            roomInfo.put("references", references);
            roomInfo.put("roomTypeInfo", roomTypeInfo(roomTypeInfo));
            roomInfo.put("ratePlanInfo", ratePlanInfo(ratePlanInfo));
            roomInfo.put("availabilityStatus", ""); // todo
            roomInfo.put("requestedRoomIndex", 1); //todo
            return roomInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return roomInfo;

    }

    public JSONObject mealInfo(OpsMealInfo mealInfoNew) {
        JSONObject mealInfo = null;
        try {
            mealInfo = new JSONObject();
            mealInfo.put("mealName", mealInfoNew.getMealName());
            mealInfo.put("mealCode", mealInfoNew.getMealID());
            return mealInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mealInfo;
    }

    public JSONObject hotelInfo(OpsHotelDetails opsHotelDetails) {
        JSONObject hotelInfo = null;
        try {
            hotelInfo = new JSONObject();
            hotelInfo.put("hotelCode", opsHotelDetails.getHotelCode());
            hotelInfo.put("hotelName", opsHotelDetails.getHotelName());
            hotelInfo.put("hotelRef", opsHotelDetails.getAccommodationReferenceNumber());
            return hotelInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hotelInfo;
    }

    public JSONObject roomTypeInfo(OpsRoomTypeInfo roomTypeInfoNew) {
        JSONObject roomTypeInfo = null;
        try {

            roomTypeInfo = new JSONObject();
            roomTypeInfo.put("roomTypeCode", roomTypeInfoNew.getRoomTypeCode());
            roomTypeInfo.put("roomCategoryCode", roomTypeInfoNew.getRoomCategoryID());
            roomTypeInfo.put("roomRef", roomTypeInfoNew.getRoomRef());
            roomTypeInfo.put("roomTypeName", roomTypeInfoNew.getRoomRef());
            roomTypeInfo.put("roomCategoryName", roomTypeInfoNew.getRoomCategoryName());
            return roomTypeInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return roomTypeInfo;
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

    public List<JSONObject> getAccommodationInfoForBookRequest(OpsProduct opsProduct, String supplierId, String modification) throws JSONException {
        List<JSONObject> accommodationList = new ArrayList<>();
        OpsHotelDetails hotelDetails = opsProduct.getOrderDetails().getHotelDetails();
        JSONObject accommodation = new JSONObject();
        String docDetails;
        //get document details of pax from Booking engine db
        JSONArray documentDetails = getDocumentDetails(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory(), opsProduct.getOrderID());
        accommodation.put("accommodationSubType", opsProduct.getProductSubCategory());
        OpsRoom opsRoom = hotelDetails.getRooms().get(0);  // todo ask ananth do all room contails same checkin and checkout date
        accommodation.put("checkIn", opsRoom.getCheckIn());
        accommodation.put("checkOut", opsRoom.getCheckOut());
        accommodation.put("countryCode", hotelDetails.getCountryCode());
        accommodation.put("cityCode", hotelDetails.getCityCode());
        accommodation.put("supplierRef", supplierId);
        accommodation.put("paxInfo", getRoomConfigs(hotelDetails, documentDetails));
        accommodationList.add(accommodation);

        return accommodationList;
    }

    private JSONArray getDocumentDetails(String productSubCategory, String orderID) throws JSONException {
        JSONObject resource = new JSONObject();
        resource.put("productSubCategory", productSubCategory);
        resource.put("orderID", orderID);
        HttpEntity<String> httpEntity = new HttpEntity<>(resource.toString(), null);
        restTemplate = RestUtils.getTemplate();
        try {
            ResponseEntity<JSONArray> docDetailsEntity = restTemplate.exchange(this.docUrl, HttpMethod.POST, httpEntity, JSONArray.class);
            return docDetailsEntity.getBody();
        } catch (Exception e) {

            e.printStackTrace();

        }
        return null;
    }

    public List<JSONObject> getRoomConfigs(OpsHotelDetails hotelDetails, JSONArray docDetails) throws JSONException {
        List<JSONObject> roomConfigs = new ArrayList<>();

        for (OpsRoom opsRoom : hotelDetails.getRooms()) {
            JSONObject roomConfig = new JSONObject();
            roomConfig.put("roomInfo", getRoomInfo(opsRoom, hotelDetails));
            roomConfig.put("paxInfo", getPaxInfo(opsRoom.getPaxInfo(), docDetails));
            roomConfigs.add(roomConfig);
        }

        return roomConfigs;
    }

    public JSONArray getPaxInfo(List<OpsAccommodationPaxInfo> paxInfoNew, JSONArray paxDox) {
        try {
            JSONArray paxInfo = new JSONArray();
            for (OpsAccommodationPaxInfo opsAccommodationPaxInfo : paxInfoNew) {
                JSONArray getPaxContactDetails = new JSONArray();
                JSONObject contactInfoParentObj = new JSONObject();
                contactInfoParentObj.put("contactInfo", getPaxContactInfo(opsAccommodationPaxInfo));
                getPaxContactDetails.put(contactInfoParentObj);
                JSONObject paxInfoDetails = new JSONObject();
                paxInfoDetails.put("isLeadPax", opsAccommodationPaxInfo.getLeadPax());
                paxInfoDetails.put("firstName", opsAccommodationPaxInfo.getFirstName());
                paxInfoDetails.put("paxType", opsAccommodationPaxInfo.getPaxType());
                paxInfoDetails.put("surname", opsAccommodationPaxInfo.getLastName());
                paxInfoDetails.put("dob", opsAccommodationPaxInfo.getBirthDate());
                paxInfoDetails.put("middleName", opsAccommodationPaxInfo.getMiddleName());
                paxInfoDetails.put("title", opsAccommodationPaxInfo.getTitle());
                paxInfoDetails.put("contactDetails", getPaxContactDetails);
                JSONObject paxDocInfo = new JSONObject(paxDox);
                paxInfoDetails.put("documentDetails", paxDocInfo);
                paxInfoDetails.put("addressDetails", getPaxAddressDetails(opsAccommodationPaxInfo.getAddressDetails()));
                paxInfo.put(paxInfoDetails);
            }
            return paxInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getPaxContactInfo(OpsAccommodationPaxInfo opsAccommodationPaxInfo) {
        try {
            JSONObject contactInfo = new JSONObject();
            OpsContactDetails contactDetails = opsAccommodationPaxInfo.getContactDetails().get(0);
            OpsContactInfo contactInfo1 = contactDetails.getContactInfo();
            contactInfo.put("countryCode", contactInfo1.getCountryCode());
            contactInfo.put("contactType", contactInfo1.getContactType());
            contactInfo.put("mobileNo", contactInfo1.getMobileNo());
            contactInfo.put("email", contactInfo1.getEmail());
            return contactInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public JSONObject getPaxAddressDetails(OpsAddressDetails addressDetailsNew) {
        try {
            JSONObject addressDetails = new JSONObject();
            addressDetails.put("zip", addressDetailsNew.getZipCode());
            addressDetails.put("country", addressDetailsNew.getCityName());
            addressDetails.put("city", addressDetailsNew.getCityName());
            addressDetails.put("addrLine2", addressDetailsNew.getAddressLines().get(0));
            addressDetails.put("addrLine1", addressDetailsNew.getAddressLines().get(1));
            addressDetails.put("state", addressDetailsNew.getState());
            return addressDetails;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public JSONArray supplierBookReference() {

        try {
            JSONObject supplierBookReference = new JSONObject();
            supplierBookReference.put("refValue", "");// todo
            supplierBookReference.put("refCode", "");// todo

            JSONArray supplierBookReferences = new JSONArray();
            supplierBookReferences.put(supplierBookReference);
            return supplierBookReferences;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public JSONArray getAccoInfoForCancellation(OpsProduct opsProduct) {
        try {
            JSONArray documentDetails = getDocumentDetails(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory(), opsProduct.getOrderID());
            OpsOrderDetails orderDetails = opsProduct.getOrderDetails();
            OpsHotelDetails hotelDetails = orderDetails.getHotelDetails();
            List<OpsRoom> rooms = hotelDetails.getRooms();
            JSONArray listOfAccoInfo = new JSONArray();
            for (OpsRoom opsRoom : rooms) {
                List<OpsAccommodationPaxInfo> paxInfo = opsRoom.getPaxInfo();

                JSONObject accomodationInfo = new JSONObject();
                accomodationInfo.put("accomodationSubType", opsProduct.getProductSubCategory());
                accomodationInfo.put("orderID", opsProduct.getOrderID());
                accomodationInfo.put("checkIn", opsRoom.getCheckIn());
                accomodationInfo.put("supplierRef", opsProduct.getSupplierRefNumber());
                accomodationInfo.put("countryCode", "");// todo
                accomodationInfo.put("cityCode", "G1011");// todo
                accomodationInfo.put("checkOut", opsRoom.getCheckOut());
                accomodationInfo.put("modificationType", "UPDATEPASSENGER");
                accomodationInfo.put("supplierRoomIndex", "110089368");// todo
                accomodationInfo.put("roomID", opsRoom.getRoomID());
                accomodationInfo.put("paxInfo", getPaxInfo(paxInfo, documentDetails));
                accomodationInfo.put("supplierBookReferences", supplierBookReference());
                accomodationInfo.put("roomInfo", getRoomInfo(opsRoom, hotelDetails));
                listOfAccoInfo.put(accomodationInfo);
            }


            return listOfAccoInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getCompleteJsonForAccoCancellation(JSONObject requestHeader, JSONArray accomodationInfo) {
        try {
            JSONObject parentObject = new JSONObject();
            JSONObject requestBody = new JSONObject();
            requestBody.put("accommodationInfo", accomodationInfo);
            parentObject.put("requestHeader", requestHeader);
            parentObject.put("requestBody", requestBody);
            return parentObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}