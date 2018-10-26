package com.coxandkings.travel.operations.service.booking.impl;

import com.coxandkings.travel.operations.enums.amendclientcommercials.BEInboundOperation;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEOperationId;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEServiceUri;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.qcmanagement.CancelAmendTypes;
import com.coxandkings.travel.operations.enums.qcmanagement.KibanaRequestParameters;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.booking.BookingHistoryItem;
import com.coxandkings.travel.operations.service.booking.BookingHistoryAccoService;
import com.coxandkings.travel.operations.utils.BookingEngineElasticData;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class BookingHistoryAccoServiceImpl implements BookingHistoryAccoService {
    private static final Logger logger = LogManager.getLogger(BookingHistoryAccoServiceImpl.class);

    @Value("${elastic-search.booking-engine}")
    private String bookingEngineIndexUrl;

    @Autowired
    private BookingEngineElasticData bookingEngineElasticData;

    @Override
    public List<BookingHistoryItem> getAccoHistory(String bookID, List<String> paxIDs) throws OperationException {
        List<BookingHistoryItem> bookingHistoryInfoList = new ArrayList<>();
        bookingHistoryInfoList.addAll(this.getCancellationHistory(bookID, paxIDs));
        bookingHistoryInfoList.addAll(this.getAmendmentsHistory(bookID));
        return bookingHistoryInfoList;
    }

    private List<BookingHistoryItem> getCancellationHistory(String bookID, List<String> paxIDs) throws OperationException {
        List<BookingHistoryItem> list = new ArrayList<>();
        list.addAll(this.checkType(bookID, CancelAmendTypes.ACCO_CANCELTYPE_CANCELPAX, paxIDs));
        list.addAll(this.checkType(bookID, CancelAmendTypes.ACCO_CANCELTYPE_FULLCANCEL, paxIDs));
        list.addAll(this.checkType(bookID, CancelAmendTypes.ACCO_CANCELTYPE_CANCELROOM, paxIDs));
        return list;
    }

    private List<BookingHistoryItem> getAmendmentsHistory(String bookID) throws OperationException {
        List<BookingHistoryItem> listOfHistory = new ArrayList<>();
        listOfHistory.addAll(this.checkType(bookID, CancelAmendTypes.ACCO_AMENDTYPE_UPDATEROOM, null));
        listOfHistory.addAll(this.checkType(bookID, CancelAmendTypes.ACCO_AMENDTYPE_CHANGEPERIODOFSTAY, null));
        listOfHistory.addAll(this.checkType(bookID, CancelAmendTypes.ACCO_AMENDTYPE_ADDPAX, null));
        listOfHistory.addAll(this.checkType(bookID, CancelAmendTypes.ACCO_AMENDTYPE_UPDATEPAX, null));
        return listOfHistory;
    }

    private List<BookingHistoryItem> checkType(String bookID, CancelAmendTypes cancelAmendTypes, List<String> paxIDs) throws OperationException {
        List<BookingHistoryItem> list = new ArrayList<>();
        switch (cancelAmendTypes) {
            case ACCO_CANCELTYPE_CANCELPAX:
                list = this.getHistory(bookID, cancelAmendTypes, paxIDs);
                break;
            case ACCO_CANCELTYPE_FULLCANCEL:
                list = this.getHistory(bookID, cancelAmendTypes, null);
                break;
            case ACCO_CANCELTYPE_CANCELROOM:
                list = this.getHistory(bookID, cancelAmendTypes, null);
                break;
            case ACCO_AMENDTYPE_UPDATEROOM:
                list = this.getHistory(bookID, cancelAmendTypes, null);
                break;
            case ACCO_AMENDTYPE_CHANGEPERIODOFSTAY:
                list = this.getHistory(bookID, cancelAmendTypes, null);
                break;
            case ACCO_AMENDTYPE_ADDPAX:
                list = this.getHistory(bookID, cancelAmendTypes, null);
                break;
            case ACCO_AMENDTYPE_UPDATEPAX:
                list = this.getHistory(bookID, cancelAmendTypes, null);
        }
        return list;
    }

    private List<BookingHistoryItem> getHistory(String bookID, CancelAmendTypes cancelAmendTypes, List<String> paxIDs) throws OperationException {
        List<BookingHistoryItem> list = new ArrayList<>();
        JSONObject jsonRequest = this.createJsonRequest(BEInboundOperation.MODIFY.getInboundOperation(), BEOperationId.BOOKING_ENGINE_RQ.getOperationid(), cancelAmendTypes.getStatus(), bookID);
        JSONArray searchResArr = this.getSearchResultArray(jsonRequest, bookID);
        loop:
        for (int i = 0; i < searchResArr.length(); i++) {
            String modificationType = null;
            JSONObject accomodationInfo = null;
            String requestBookID = null;
            JSONObject source = searchResArr.getJSONObject(i).getJSONObject("_source");
            String timeStamp = searchResArr.getJSONObject(i).getJSONObject("_source").getString("timestamp");
            //retreiving BKNGENG_RQ
            String messageJson = source.getString("message");
            if (messageJson.contains("xml")) {
                continue loop;
            }
            JSONObject bookingEngineRequest = this.getRequestJson(messageJson);
            try {
                accomodationInfo = bookingEngineRequest.getJSONObject("requestBody").getJSONArray("accommodationInfo").getJSONObject(0);
                requestBookID = bookingEngineRequest.getJSONObject("requestBody").getString("bookID");
            } catch (Exception e) {
                continue loop;
            }
            if (accomodationInfo == null && requestBookID == null) {
                continue loop;
            }
            //filtering request
            if (BEOperationId.BOOKING_ENGINE_RQ.getOperationid().equalsIgnoreCase(source.getString(KibanaRequestParameters.OPERATIONSID.getStatus()))
                    && BEInboundOperation.MODIFY.getInboundOperation().equalsIgnoreCase(source.getString(KibanaRequestParameters.INBOUND_OPERATIONS.getStatus()).trim())
                    && cancelAmendTypes.getStatus().equalsIgnoreCase(accomodationInfo.getString("modificationType"))
                    && bookID.equalsIgnoreCase(requestBookID)) {
                //retreving booking response : inputs BKNGENG_RQ (sessionID,userID,transactionID)
                JSONObject bookingEngineResponse = consumeBookingResponseForAcco(bookingEngineRequest);
                //in some cases response : {"status":"ERROR"} so checking response contains responseBody or not to avoid exception
                if (bookingEngineResponse != null) {
                    if (bookingEngineResponse.toString().contains("responseBody")) {
                        //checking booking response to cross check wheather booking is cancelled or not based on Status
                        if (bookingEngineResponse.getJSONObject("responseBody") != null) {
                            String responseStatus = null;
                            try {
                                responseStatus = bookingEngineResponse.getJSONObject("responseBody").getJSONArray("accommodationInfo").getJSONObject(0).getString("status");
                                if ("SUCCESS".equalsIgnoreCase(responseStatus)) {
                                    BookingHistoryItem bookingHistoryItem = new BookingHistoryItem();
                                    bookingHistoryItem.setBookID(requestBookID);
                                    bookingHistoryItem.setOrderID(accomodationInfo.getString("orderID"));
                                    bookingHistoryItem.setProductCategory(OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION.getCategory());
                                    bookingHistoryItem.setProductSubCategory(accomodationInfo.getString("accommodationSubType"));
                                    bookingHistoryItem.setAction(accomodationInfo.getString("modificationType"));
                                    if (cancelAmendTypes.getStatus().equalsIgnoreCase(CancelAmendTypes.ACCO_CANCELTYPE_CANCELPAX.getStatus())) {
                                        //TODO check paxids are correct, not tested with success response ..logs not there
                                        List<String> cancelledPaxIds = getCancelledPaxID(accomodationInfo, paxIDs);
                                        if (cancelledPaxIds.size() > 0) {
                                            bookingHistoryItem.setDescription("Cancelled paxIds:- " + Arrays.toString(cancelledPaxIds.toArray()) + ",charges:- " + bookingEngineResponse.getJSONObject("responseBody").getJSONArray("accommodationInfo").getJSONObject(0).getInt("companyCharges"));
                                        }
                                    } else if (cancelAmendTypes.getStatus().equalsIgnoreCase(CancelAmendTypes.ACCO_CANCELTYPE_FULLCANCEL.getStatus())) {
                                        bookingHistoryItem.setDescription("company cancellation charges :- " + bookingEngineResponse.getJSONObject("responseBody").getJSONArray("accommodationInfo").getJSONObject(0).getInt("companyCharges"));
                                    } else if (cancelAmendTypes.getStatus().equalsIgnoreCase(CancelAmendTypes.ACCO_CANCELTYPE_CANCELROOM.getStatus())) {
                                        //todo not tested with success reponse ..logs are not available
                                        if (accomodationInfo.has("roomID")) {
                                            bookingHistoryItem.setDescription("RoomID: " + accomodationInfo.getString("roomID") + "charges :- " + bookingEngineResponse.getJSONObject("responseBody").getJSONArray("accommodationInfo").getJSONObject(0).getInt("companyCharges"));
                                        }
                                    } else if (cancelAmendTypes.getStatus().equalsIgnoreCase(CancelAmendTypes.ACCO_AMENDTYPE_UPDATEROOM.getStatus())) {
                                        //Todo which room is updates? need details from audit logs
                                        //Todo not tested with success response
                                        if (accomodationInfo.has("roomID")) {
                                            bookingHistoryItem.setDescription("roomID: " + accomodationInfo.getString("roomID") + ",Check-out date:" + accomodationInfo.getString("checkOut") + ",company charges :- " + bookingEngineResponse.getJSONObject("responseBody").getJSONArray("accommodationInfo").getJSONObject(0).getInt("companyCharges"));
                                        }
                                    } else if (cancelAmendTypes.getStatus().equalsIgnoreCase(CancelAmendTypes.ACCO_AMENDTYPE_CHANGEPERIODOFSTAY.getStatus())) {
                                        //Todo which period(check-in and check-out) is updated ?  and not tested
                                        if (accomodationInfo.has("checkIn") && accomodationInfo.has("checkOut")) {
                                            bookingHistoryItem.setDescription("CheckIn date : " + accomodationInfo.getString("checkIn") + ",Check-out date:" + accomodationInfo.getString("checkOut") + ",company charges :- " + bookingEngineResponse.getJSONObject("responseBody").getJSONArray("accommodationInfo").getJSONObject(0).getInt("companyCharges"));
                                        }
                                    } else if (cancelAmendTypes.getStatus().equalsIgnoreCase(CancelAmendTypes.ACCO_AMENDTYPE_ADDPAX.getStatus())) {
                                        String paxName = getPaxNameOfAddedPassenger(accomodationInfo);
                                        bookingHistoryItem.setDescription("Passenger Name : " + paxName + " ,company charges :- " + bookingEngineResponse.getJSONObject("responseBody").getJSONArray("accommodationInfo").getJSONObject(0).getInt("companyCharges"));
                                    } else if (cancelAmendTypes.getStatus().equalsIgnoreCase(CancelAmendTypes.ACCO_AMENDTYPE_UPDATEPAX.getStatus())) {
                                        //Todo which passenger is updated ? need old details from db audit ..not tested with success response
                                        String paxName = getPaxNameOfUpdatedPassenger(accomodationInfo);
                                        if (paxName != null) {
                                            bookingHistoryItem.setDescription("Updated Passenger Name: " + paxName + ",charges :- " + bookingEngineResponse.getJSONObject("responseBody").getJSONArray("accommodationInfo").getJSONObject(0).getInt("companyCharges"));
                                        }
                                    }
                                    bookingHistoryItem.setStatus("SUCCESS");
                                    bookingHistoryItem.setTimestamp(timeStamp);
                                    list.add(bookingHistoryItem);
                                }
                            } catch (Exception e) {
                                try {
                                    responseStatus = bookingEngineResponse.getJSONObject("responseBody").getString("status");
                                    if ("ERROR".equalsIgnoreCase(responseStatus) || "FAILURE".equalsIgnoreCase(responseStatus)) {
                                        BookingHistoryItem bookingHistoryItem = this.getFailureResponse(requestBookID, accomodationInfo, bookingEngineResponse, responseStatus, timeStamp);
                                        list.add(bookingHistoryItem);
                                    }
                                } catch (Exception e2) {
                                    logger.error("Invalid Response Structure from BE" + e2);
                                }
                            }
                        }
                    } else if ("ERROR".equalsIgnoreCase(bookingEngineResponse.getString("status"))) {
                        BookingHistoryItem bookingHistoryItem = this.getErrorResponse(requestBookID, accomodationInfo, bookingEngineResponse, timeStamp);
                        list.add(bookingHistoryItem);
                    }
                }
            }
        }
        return list;
    }


    private List<String> getCancelledPaxID(JSONObject accomodationInfo, List<String> paxIDs) {
        List<String> cancelledPaxIDList = new ArrayList<>();
        if (accomodationInfo.has("paxID") && !accomodationInfo.isNull("paxID")) {
            String availablePaxID = accomodationInfo.getString("paxID");
            String remainPaxId;
            Iterator<String> iterator = paxIDs.iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                if (next != availablePaxID) {
                    cancelledPaxIDList.add(next);
                }
            }
        }
        return cancelledPaxIDList;
    }

    private String getPaxNameOfAddedPassenger(JSONObject accomodationInfo) {
        String paxName = "";
        JSONArray paxInfos = accomodationInfo.getJSONArray("paxInfo");
        for (int j = 0; j < paxInfos.length(); j++) {
            JSONObject pax = paxInfos.getJSONObject(j);
            try {
                if (!pax.has("paxID")) {
                    paxName = pax.getString("firstName") + " " + pax.getString("middleName") + " " + pax.getString("surname");
                }
            } catch (Exception e) {
                logger.error("pax details are not available in booking request" + e);
            }
        }
        return paxName;
    }

    private String getPaxNameOfUpdatedPassenger(JSONObject accomodationInfo) {
        String paxName = "";
        JSONArray paxInfos = accomodationInfo.getJSONArray("paxInfo");
        for (int j = 0; j < paxInfos.length(); j++) {
            JSONObject pax = paxInfos.getJSONObject(j);
            try {
                if (pax.has("paxID")) {
                    paxName = pax.getString("firstName") + " " + pax.getString("middleName") + " " + pax.getString("surname");
                }
            } catch (Exception e) {
                logger.error("pax details are not available in booking request" + e);
            }
        }
        return paxName;
    }

    private JSONObject getRequestJson(String messageJson) {
        int endWith = messageJson.indexOf("{");
        String replaceString = messageJson.substring(0, endWith);
        String message = messageJson.replace(replaceString, "");
        return new JSONObject(new JSONTokener(message));
    }

    private BookingHistoryItem getFailureResponse(String requestBookID, JSONObject accomodationInfo, JSONObject bookingEngineResponse, String responseStatus, String timeStamp) {
        String errorMsg = null;
        BookingHistoryItem bookingHistoryItem = new BookingHistoryItem();
        bookingHistoryItem.setBookID(requestBookID);
        bookingHistoryItem.setOrderID(accomodationInfo.getString("orderID"));
        bookingHistoryItem.setProductCategory(OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION.getCategory());
        bookingHistoryItem.setProductSubCategory(accomodationInfo.getString("accommodationSubType"));
        bookingHistoryItem.setAction(accomodationInfo.getString("modificationType"));
        try {
            errorMsg = bookingEngineResponse.getJSONObject("responseBody").getString("errorMsg");
            bookingHistoryItem.setDescription("Error message: " + errorMsg);
        } catch (Exception e1) {
            bookingHistoryItem.setDescription("Error message: " + errorMsg);
        }
        bookingHistoryItem.setStatus(responseStatus);
        bookingHistoryItem.setTimestamp(timeStamp);
        return bookingHistoryItem;
    }

    private BookingHistoryItem getErrorResponse(String requestBookID, JSONObject accomodationInfo, JSONObject bookingEngineResponse, String timeStamp) {
        String errorCode = null;
        BookingHistoryItem bookingHistoryItem = new BookingHistoryItem();
        bookingHistoryItem.setBookID(requestBookID);
        bookingHistoryItem.setOrderID(accomodationInfo.getString("orderID"));
        bookingHistoryItem.setProductCategory(OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION.getCategory());
        bookingHistoryItem.setProductSubCategory(accomodationInfo.getString("accommodationSubType"));
        bookingHistoryItem.setAction(accomodationInfo.getString("modificationType"));
        try {
            errorCode = bookingEngineResponse.getString("errorCode");
            bookingHistoryItem.setDescription("Error code " + errorCode);
        } catch (Exception e) {
            bookingHistoryItem.setDescription("Error code " + errorCode);
        }
        bookingHistoryItem.setStatus(bookingEngineResponse.getString("status"));
        bookingHistoryItem.setTimestamp(timeStamp);
        return bookingHistoryItem;
    }

    private JSONArray getSearchResultArray(JSONObject jsonRequest, String bookID) throws OperationException {
        ResponseEntity<String> searchResult = RestUtils.postForEntity(bookingEngineIndexUrl, jsonRequest.toString(), String.class);
        JSONObject searchResJSON = new JSONObject(searchResult.getBody());
        JSONArray searchResArr = searchResJSON.getJSONObject("hits").getJSONArray("hits");
        if (searchResArr.length() == 0) {
            throw new OperationException(String.format("No Matching Message Found for userId <%s>, transactionId <%s>, sessionId <%s>,  inboundOperation<%s>, operationId<%s>, service<%s>",
                    KibanaRequestParameters.INBOUND_OPERATIONS.getStatus(), KibanaRequestParameters.OPERATIONSID.getStatus(), bookID));
        }
        return searchResArr;
    }

    private JSONObject consumeBookingResponseForAcco(JSONObject jsonObject) {
        JSONObject bookingResponse = null;
        try {
            bookingResponse = bookingEngineElasticData.getJSONData(
                    jsonObject.getJSONObject("requestHeader").getString("userID"),
                    jsonObject.getJSONObject("requestHeader").getString("transactionID"),
                    jsonObject.getJSONObject("requestHeader").getString("sessionID"),
                    BEInboundOperation.MODIFY,
                    BEOperationId.BOOKING_ENGINE_RS,
                    BEServiceUri.HOTEL);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (OperationException e) {
            e.printStackTrace();
        }
        return bookingResponse;
    }

    private JSONObject createJsonRequest(String inbound_operation, String operationid, String modificationType, String bookID) {
        JSONObject matchOne = new JSONObject();
        matchOne.put(KibanaRequestParameters.INBOUND_OPERATIONS.getStatus(), inbound_operation);
        JSONObject matchTwo = new JSONObject();
        matchTwo.put(KibanaRequestParameters.OPERATIONSID.getStatus(), operationid);
        JSONObject matchThree = null;
        if (modificationType != null) {
            matchThree = new JSONObject();
            matchThree.put(KibanaRequestParameters.MESSAGE.getStatus(), modificationType);
        }
        JSONObject matchFour = new JSONObject();
        matchFour.put(KibanaRequestParameters.MESSAGE.getStatus(), bookID);

        JSONObject innerMatchOne = new JSONObject();
        innerMatchOne.put("match", matchOne);
        JSONObject innerMatchTwo = new JSONObject();
        innerMatchTwo.put("match", matchTwo);
        JSONObject innerMatchThree = null;
        if (matchThree != null) {
            innerMatchThree = new JSONObject();
            innerMatchThree.put("match", matchThree);
        }
        JSONObject innerMatchFour = new JSONObject();
        innerMatchFour.put("match", matchFour);

        JSONArray shouldJsonArray = new JSONArray();
        shouldJsonArray.put(innerMatchOne);
        shouldJsonArray.put(innerMatchTwo);
        if (innerMatchThree != null) {
            shouldJsonArray.put(innerMatchThree);
        }
        shouldJsonArray.put(innerMatchFour);

        JSONObject shouldObject = new JSONObject();
        shouldObject.put("should", shouldJsonArray);

        JSONObject innerBoolObject = new JSONObject();
        innerBoolObject.put("bool", shouldObject);

        JSONObject mustObject = new JSONObject();
        mustObject.put("must", innerBoolObject);

        JSONObject boolObject = new JSONObject();
        boolObject.put("bool", mustObject);

        //source
        JSONArray sourceList = new JSONArray();
        sourceList.put(KibanaRequestParameters.MESSAGE.getStatus());
        sourceList.put(KibanaRequestParameters.INBOUND_OPERATIONS.getStatus());
        sourceList.put(KibanaRequestParameters.SESSIONID.getStatus());
        sourceList.put(KibanaRequestParameters.USERID.getStatus());
        sourceList.put(KibanaRequestParameters.OPERATIONSID.getStatus());
        sourceList.put(KibanaRequestParameters.TRANSACTIONID.getStatus());
        sourceList.put(KibanaRequestParameters.TIMESTAMP.getStatus());

        JSONObject requestJsonObject = new JSONObject();
        requestJsonObject.put("query", boolObject);
        requestJsonObject.put("_source", sourceList);
        requestJsonObject.put("size", 50);

        return requestJsonObject;
    }
}
