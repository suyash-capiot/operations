package com.coxandkings.travel.operations.service.productsharing.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.service.productsharing.UpdateRoomService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service("updateRoomService")
public class UpdateRoomServiceImpl implements UpdateRoomService {


    @Value(value = "${product_sharing.be.acco_modify}")
    private String accoCancellationURL;

    @Autowired
    private RestUtils restUtils;

    /**
     * @param addressDetailsNew
     * @return
     */
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

    /**
     * @param opsAccommodationPaxInfo
     * @return
     */
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

    /**
     * @param paxInfoNew
     * @return
     */
    public JSONArray getPaxInfo(List<OpsAccommodationPaxInfo> paxInfoNew) {
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
                paxInfoDetails.put("paxID", opsAccommodationPaxInfo.getPaxID());
                paxInfoDetails.put("contactDetails", getPaxContactDetails);
                paxInfoDetails.put("documentDetails", getPaxDocInfo());
                paxInfoDetails.put("addressDetails", getPaxAddressDetails(opsAccommodationPaxInfo.getAddressDetails()));
                paxInfo.put(paxInfoDetails);
            }
            return paxInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param opsBooking
     * @return
     */
    public JSONObject getRequestHeader(OpsBooking opsBooking) {

        try {
            JSONObject requestHeader = new JSONObject();
            requestHeader.put("sessionID", opsBooking.getSessionID());
            requestHeader.put("transactionID", opsBooking.getTransactionID());
            requestHeader.put("userID", opsBooking.getUserID());
            requestHeader.put("clientContext", getClientContextJson(opsBooking));
            return requestHeader;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * @param opsBooking
     * @return
     * @throws JSONException
     */
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

    /**
     * @param opsBooking
     * @return
     */
    public JSONObject clientContext(OpsBooking opsBooking) {
        try {
            JSONObject clientContext = new JSONObject();
            clientContext.put("clientCurrency", opsBooking.getClientCurrency());
            clientContext.put("clientID", opsBooking.getClientID());
            clientContext.put("clientLanguage", opsBooking.getClientLanguage());
            clientContext.put("clientMarket", opsBooking.getClientMarket());
            clientContext.put("clientType", opsBooking.getClientType());
            clientContext.put("clientNationality", "");// todo
            clientContext.put("pointOfSale", opsBooking.getPointOfSale());
            clientContext.put("clientIATANumber", opsBooking.getClientIATANumber());
            clientContext.put("clientCallbackAddress", "");// todo
            return clientContext;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param ratePlanInfoNew
     * @return
     */
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

    /**
     * @param roomTypeInfoNew
     * @return
     */
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

    /**
     * @param opsHotelDetails
     * @return
     */
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

    /**
     * @param mealInfoNew
     * @return
     */
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

    /**
     * @param opsRoom
     * @param opsHotelDetails
     * @return
     */
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
            roomInfo.put("requestedRoomIndex", opsRoom.getRoomID());// todo : not sure
            return roomInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * @return
     */
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

    /**
     * @param opsProduct
     * @return
     */
    public JSONArray getAccoInfo(OpsProduct opsProduct) {
        try {

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
                accomodationInfo.put("cityCode", "");// todo
                accomodationInfo.put("checkOut", opsRoom.getCheckOut());
                accomodationInfo.put("modificationType", "UPDATEROOM");
                accomodationInfo.put("supplierRoomIndex", opsRoom.getSupplierRoomIndex());

                accomodationInfo.put("roomID", opsRoom.getRoomID());
                accomodationInfo.put("paxInfo", getPaxInfo(paxInfo));
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

    /**
     * @return
     */
    public JSONObject getPaxDocInfo() {
// todo set Document info
        try {
            JSONObject documentInfo = new JSONObject();
            documentInfo.put("expiryDate", "");// todo

            JSONArray documentInfoArray = new JSONArray();
            documentInfoArray.put(documentInfo);
            JSONObject documentDetails = new JSONObject();
            documentDetails.put("documentInfo", documentInfoArray);

            return documentDetails;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param requestHeader
     * @param accomodationInfo
     * @return
     */
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

    /**
     * @param opsBooking
     * @param opsProduct
     * @return
     * @throws OperationException
     */
    @Override
    public Object processRoomUpdate(OpsBooking opsBooking, OpsProduct opsProduct) throws OperationException {
        JSONArray accoInfo = getAccoInfo(opsProduct);
        JSONObject header = getRequestHeader(opsBooking);
        JSONObject completeJsonForAccoCancellation = getCompleteJsonForAccoCancellation(header, accoInfo);
        try {
            ResponseEntity<?> exchange = restUtils.postForEntity(accoCancellationURL, completeJsonForAccoCancellation, Object.class);
            Object body = exchange.getBody();
            return body;
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException(Constants.ER909);
        }
    }


}
