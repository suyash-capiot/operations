package com.coxandkings.travel.operations.service.booking.impl;

import com.coxandkings.travel.operations.enums.amendclientcommercials.BEInboundOperation;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEOperationId;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEServiceUri;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.qcmanagement.CancelAmendTypes;
import com.coxandkings.travel.operations.enums.qcmanagement.KibanaRequestParameters;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.booking.BookingHistoryItem;
import com.coxandkings.travel.operations.service.booking.BookingHistoryAirService;
import com.coxandkings.travel.operations.utils.BookingEngineElasticData;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookingHistoryAirServiceImpl implements BookingHistoryAirService {

    @Value("${elastic-search.booking-engine}")
    private String bookingEngineIndexUrl;

    @Autowired
    private BookingEngineElasticData bookingEngineElasticData;

    @Override
    public List<BookingHistoryItem> getAirHistory(String bookID) throws OperationException {
        return this.getAllHistory(bookID);
    }

    private List<BookingHistoryItem> getAllHistory(String bookID) throws OperationException {
        List<BookingHistoryItem> listOfHistory = new ArrayList<>();
        listOfHistory.addAll(this.getPIS(bookID, CancelAmendTypes.AIR_AMENDTYPE_PIS));
        listOfHistory.addAll(this.getFullCancellationHistory(bookID));
        listOfHistory.addAll(this.getREMHistory(bookID, CancelAmendTypes.AIR_AMENDTYPE_REM));
        listOfHistory.addAll(this.getSSRHistory(bookID, CancelAmendTypes.AIR_AMENDTYPE_SSR));
        return listOfHistory;
    }

    private List<BookingHistoryItem> getSSRHistory(String bookID, CancelAmendTypes cancelAmendTypes) throws OperationException {
        List<BookingHistoryItem> list = new ArrayList<>();
        JSONObject jsonRequest = this.createJsonRequest(BEInboundOperation.AMEND.getInboundOperation(), BEOperationId.BOOKING_ENGINE_RQ.getOperationid(), CancelAmendTypes.AIR_AMENDTYPE_SSR.getStatus(), bookID);
        JSONArray searchResArr = this.getSearchResultArray(jsonRequest, bookID);
        loop:
        for (int i = 0; i < searchResArr.length(); i++) {
            String requestBookID = null;
            String cancelType = null;
            String operationId = null;
            JSONObject source = null;
            String timeStamp = null;
            String messageJson = null;
            try {
                source = searchResArr.getJSONObject(i).getJSONObject("_source");
                timeStamp = searchResArr.getJSONObject(i).getJSONObject("_source").getString("timestamp");
                messageJson = source.getString("message");
                if (messageJson.contains("xml")) {
                    continue loop;
                }
            } catch (Exception e) {
                continue loop;
            }
            //BKNGENG_RQ
            JSONObject bookingEngineRequest = this.getRequestJson(messageJson);
            try {
                requestBookID = bookingEngineRequest.getJSONObject("requestBody").getString("bookID");
                cancelType = bookingEngineRequest.getJSONObject("requestBody").getString(BEInboundOperation.AMEND.getInboundOperation() + "Type");
                operationId = source.getString(KibanaRequestParameters.OPERATIONSID.getStatus());
            } catch (Exception e) {
                continue loop;
            }
            if (BEOperationId.BOOKING_ENGINE_RQ.getOperationid().equalsIgnoreCase(operationId)
                    && BEInboundOperation.AMEND.getInboundOperation().equalsIgnoreCase(source.getString(KibanaRequestParameters.INBOUND_OPERATIONS.getStatus()).trim())
                    && CancelAmendTypes.AIR_AMENDTYPE_SSR.getStatus().equalsIgnoreCase(cancelType)
                    && bookID.equalsIgnoreCase(requestBookID)) {

                if (bookingEngineRequest.has("requestBody") && bookingEngineRequest.has("requestHeader")) {
                    JSONObject requestBody = bookingEngineRequest.getJSONObject("requestBody");
                    JSONObject requestHeader = bookingEngineRequest.getJSONObject("requestHeader");

                    Map<String, String> indentifier = new HashMap<>();
                    List<Map<String, String>> listOfSupplierBookReference = new ArrayList<>();

                    if (requestBody.has("supplierBookReferences")) {
                        JSONArray supplierBookReferences = requestBody.getJSONArray("supplierBookReferences");
                        for (int j = 0; j < supplierBookReferences.length(); j++) {
                            JSONObject aSupplierBookReference = supplierBookReferences.getJSONObject(j);
                            if (aSupplierBookReference.has("supplierRef")) {
                                indentifier.put("orderID", aSupplierBookReference.getString("orderID"));
                                indentifier.put("supplierRef", aSupplierBookReference.getString("supplierRef"));
                                indentifier.put("ssr", this.getSSRPaxDetails(aSupplierBookReference));
                            }
                            listOfSupplierBookReference.add(indentifier);
                        }
                    }
                    //BKNGENG_RS
                    JSONObject bookingEngineResponse = consumeAmendBookingResponseForAir(requestHeader);
                    if (bookingEngineResponse == null) {
                        continue loop;
                    }
                    if (bookingEngineResponse.has("responseBody")) {
                        JSONObject responseBody = bookingEngineResponse.getJSONObject("responseBody");
                        if (responseBody.has("supplierBookReferences")) {
                            JSONArray supplierBookReferences = responseBody.getJSONArray("supplierBookReferences");
                            for (int k = 0; k < supplierBookReferences.length(); k++) {
                                JSONObject aResponseSupplierDetails = supplierBookReferences.getJSONObject(k);
                                String responseSupplierRef = aResponseSupplierDetails.getString("supplierRef");
                                String responseStatus = aResponseSupplierDetails.getString("status");
                                Integer companyCharges = aResponseSupplierDetails.getInt("companyCharges");

                                for (Map<String, String> map : listOfSupplierBookReference) {
                                    Set<Map.Entry<String, String>> entries = map.entrySet();
                                    String requestOrderID = null;
                                    String requestSupplierRef = null;
                                    String requestSsr = null;
                                    for (Map.Entry entry : entries) {
                                        if (entry.getKey().equals("orderID")) {
                                            requestOrderID = entry.getValue().toString();
                                        } else if (entry.getKey().equals("supplierRef")) {
                                            requestSupplierRef = entry.getValue().toString();
                                        } else if (entry.getKey().equals("ssr")) {
                                            requestSsr = entry.getValue().toString();
                                        }
                                    }
                                    if (requestSupplierRef.equalsIgnoreCase(responseSupplierRef)) {
                                        BookingHistoryItem bookingHistoryItem = new BookingHistoryItem();
                                        bookingHistoryItem.setBookID(requestBookID);
                                        bookingHistoryItem.setOrderID(requestOrderID);
                                        bookingHistoryItem.setProductCategory(OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION.getCategory());
                                        bookingHistoryItem.setProductSubCategory(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory());
                                        bookingHistoryItem.setAction(cancelType);
                                        bookingHistoryItem.setDescription("Updated SSR Details- PaxName: " + requestSsr + ",Compnay charges: " + companyCharges);
                                        bookingHistoryItem.setStatus(responseStatus);
                                        bookingHistoryItem.setTimestamp(timeStamp);
                                        list.add(bookingHistoryItem);
                                    }
                                }
                            }
                        }
                    } else if (bookingEngineResponse.has("errorCode")) {
                        BookingHistoryItem errorResponse = this.getErrorResponse(requestBookID, bookingEngineResponse, timeStamp, cancelType);
                        list.add(errorResponse);
                    }
                }
            }
        }
        return list;
    }

    private String getSSRPaxDetails(JSONObject aSupplierBookReference) {
        String ssrDetails = null;
        JSONArray paxDetails = aSupplierBookReference.getJSONArray("paxDetails");
        for (int j = 0; j < paxDetails.length(); j++) {
            JSONObject aPaxDetail = paxDetails.getJSONObject(j);
            List<String> specialRequestList = new ArrayList<>();
            String paxName = aPaxDetail.getString("firstName") + " " + aPaxDetail.getString("surname");
            JSONArray specialRequestInfo = aPaxDetail.getJSONArray("specialRequestInfo");
            for (int k = 0; k < specialRequestInfo.length(); k++) {
                JSONObject aSpecialRequestInfo = specialRequestInfo.getJSONObject(k);
                String specialRequest = "Service Quantity: " + aSpecialRequestInfo.getString("serviceQuantity")
                        + ",SSR Code: " + aSpecialRequestInfo.getString("ssrCode")
                        + "("
                        + aSpecialRequestInfo.getString("description")
                        + "),Type: " + aSpecialRequestInfo.getString("type");
                specialRequestList.add(specialRequest);
            }
            ssrDetails = paxName + Arrays.toString(specialRequestList.toArray());
        }
        return ssrDetails;
    }

    private List<BookingHistoryItem> getREMHistory(String bookID, CancelAmendTypes cancelAmendTypes) throws OperationException {
        List<BookingHistoryItem> list = new ArrayList<>();
        JSONObject jsonRequest = this.createJsonRequest(BEInboundOperation.AMEND.getInboundOperation(), BEOperationId.BOOKING_ENGINE_RQ.getOperationid(), CancelAmendTypes.AIR_AMENDTYPE_REM.getStatus(), bookID);
        JSONArray searchResArr = this.getSearchResultArray(jsonRequest, bookID);
        loop:
        for (int i = 0; i < searchResArr.length(); i++) {
            String requestBookID = null;
            String cancelType = null;
            String operationId = null;
            JSONObject source = null;
            String timeStamp = null;
            String messageJson = null;
            try {
                source = searchResArr.getJSONObject(i).getJSONObject("_source");
                timeStamp = searchResArr.getJSONObject(i).getJSONObject("_source").getString("timestamp");
                messageJson = source.getString("message");
                if (messageJson.contains("xml")) {
                    continue loop;
                }
            } catch (Exception e) {
                continue loop;
            }
            //BKNGENG_RQ
            JSONObject bookingEngineRequest = this.getRequestJson(messageJson);
            try {
                requestBookID = bookingEngineRequest.getJSONObject("requestBody").getString("bookID");
                cancelType = bookingEngineRequest.getJSONObject("requestBody").getString(BEInboundOperation.AMEND.getInboundOperation() + "Type");
                operationId = source.getString(KibanaRequestParameters.OPERATIONSID.getStatus());
            } catch (Exception e) {
                continue loop;
            }
            if (BEOperationId.BOOKING_ENGINE_RQ.getOperationid().equalsIgnoreCase(operationId)
                    && BEInboundOperation.AMEND.getInboundOperation().equalsIgnoreCase(source.getString(KibanaRequestParameters.INBOUND_OPERATIONS.getStatus()).trim())
                    && CancelAmendTypes.AIR_AMENDTYPE_REM.getStatus().equalsIgnoreCase(cancelType)
                    && bookID.equalsIgnoreCase(requestBookID)) {
                if (bookingEngineRequest.has("requestBody") && bookingEngineRequest.has("requestHeader")) {
                    JSONObject requestBody = bookingEngineRequest.getJSONObject("requestBody");
                    JSONObject requestHeader = bookingEngineRequest.getJSONObject("requestHeader");
                    Map<String, String> indentifier = new HashMap<>();
                    List<Map<String, String>> listOfSupplierBookReference = new ArrayList<>();

                    if (requestBody.has("supplierBookReferences")) {
                        JSONArray supplierBookReferences = requestBody.getJSONArray("supplierBookReferences");
                        for (int j = 0; j < supplierBookReferences.length(); j++) {
                            JSONObject aSupplierBookReference = supplierBookReferences.getJSONObject(j);
                            if (aSupplierBookReference.has("supplierRef")) {
                                indentifier.put("orderID", aSupplierBookReference.getString("orderID"));
                                indentifier.put("supplierRef", aSupplierBookReference.getString("supplierRef"));
                                indentifier.put("remarkText", Arrays.toString(getPaxRemarksInfo(aSupplierBookReference).toArray()));
                            }
                            listOfSupplierBookReference.add(indentifier);
                        }
                    }
                    //BKNGENG_RS
                    JSONObject bookingEngineResponse = consumeAmendBookingResponseForAir(requestHeader);
                    if (bookingEngineResponse == null) {
                        continue loop;
                    }
                    if (bookingEngineResponse.has("responseBody")) {
                        JSONObject responseBody = bookingEngineResponse.getJSONObject("responseBody");
                        if (responseBody.has("supplierBookReferences")) {
                            JSONArray supplierBookReferences = responseBody.getJSONArray("supplierBookReferences");
                            for (int k = 0; k < supplierBookReferences.length(); k++) {
                                JSONObject aResponseSupplierDetails = supplierBookReferences.getJSONObject(k);
                                String responseSupplierRef = aResponseSupplierDetails.getString("supplierRef");
                                String responseStatus = aResponseSupplierDetails.getString("status");
                                Integer companyCharges = aResponseSupplierDetails.getInt("companyCharges");
                                for (Map<String, String> map : listOfSupplierBookReference) {
                                    Set<Map.Entry<String, String>> entries = map.entrySet();
                                    String requestOrderID = null;
                                    String requestSupplierRef = null;
                                    String requestRemarks = null;
                                    for (Map.Entry entry : entries) {
                                        if (entry.getKey().equals("orderID")) {
                                            requestOrderID = entry.getValue().toString();
                                        } else if (entry.getKey().equals("supplierRef")) {
                                            requestSupplierRef = entry.getValue().toString();
                                        } else if (entry.getKey().equals("remarkText")) {
                                            requestRemarks = entry.getValue().toString();
                                        }
                                    }
                                    if (requestSupplierRef.equalsIgnoreCase(responseSupplierRef)) {
                                        BookingHistoryItem bookingHistoryItem = new BookingHistoryItem();
                                        bookingHistoryItem.setBookID(requestBookID);
                                        bookingHistoryItem.setOrderID(requestOrderID);
                                        bookingHistoryItem.setProductCategory(OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION.getCategory());
                                        bookingHistoryItem.setProductSubCategory(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory());
                                        bookingHistoryItem.setAction(cancelType);
                                        bookingHistoryItem.setDescription("Updated remarks: " + requestRemarks + ",Compnay charges: " + companyCharges);
                                        bookingHistoryItem.setStatus(responseStatus);
                                        bookingHistoryItem.setTimestamp(timeStamp);
                                        list.add(bookingHistoryItem);
                                    }
                                }
                            }
                        }
                    } else if (bookingEngineResponse.has("errorCode")) {
                        BookingHistoryItem errorResponse = this.getErrorResponse(requestBookID, bookingEngineResponse, timeStamp, cancelType);
                        list.add(errorResponse);
                    }
                }
            }
        }
        return list;
    }

    private List<String> getPaxRemarksInfo(JSONObject aSupplierBookReference) {
        List<String> listOfpaxRemarkText = new ArrayList<>();
        JSONArray paxRemarkInfo = aSupplierBookReference.getJSONArray("paxRemarkInfo");
        for (int i = 0; i < paxRemarkInfo.length(); i++) {
            JSONObject jsonObject = paxRemarkInfo.getJSONObject(i);
            if (jsonObject.has("paxRemarkText"))
                listOfpaxRemarkText.add(jsonObject.getString("paxRemarkText"));
        }
        return listOfpaxRemarkText;
    }

    private List<BookingHistoryItem> getPIS(String bookID, CancelAmendTypes cancelAmendTypes) throws OperationException {
        List<BookingHistoryItem> list = new ArrayList<>();
        JSONObject jsonRequest = this.createJsonRequest(BEInboundOperation.AMEND.getInboundOperation(), BEOperationId.BOOKING_ENGINE_RQ.getOperationid(), CancelAmendTypes.AIR_AMENDTYPE_PIS.getStatus(), bookID);
        JSONArray searchResArr = this.getSearchResultArray(jsonRequest, bookID);
        loop:
        for (int i = 0; i < searchResArr.length(); i++) {
            String requestBookID = null;
            String cancelType = null;
            String operationId = null;
            JSONObject source = null;
            String timeStamp = null;
            String messageJson = null;
            try {
                source = searchResArr.getJSONObject(i).getJSONObject("_source");
                timeStamp = searchResArr.getJSONObject(i).getJSONObject("_source").getString("timestamp");
                messageJson = source.getString("message");
                if (messageJson.contains("xml")) {
                    continue loop;
                }
            } catch (Exception e) {
                continue loop;
            }
            //BKNGENG_RQ
            JSONObject bookingEngineRequest = this.getRequestJson(messageJson);
            try {
                requestBookID = bookingEngineRequest.getJSONObject("requestBody").getString("bookID");
                cancelType = bookingEngineRequest.getJSONObject("requestBody").getString(BEInboundOperation.AMEND.getInboundOperation() + "Type");
                operationId = source.getString(KibanaRequestParameters.OPERATIONSID.getStatus());
            } catch (Exception e) {
                continue loop;
            }
            if (BEOperationId.BOOKING_ENGINE_RQ.getOperationid().equalsIgnoreCase(operationId)
                    && BEInboundOperation.AMEND.getInboundOperation().equalsIgnoreCase(source.getString(KibanaRequestParameters.INBOUND_OPERATIONS.getStatus()).trim())
                    && CancelAmendTypes.AIR_AMENDTYPE_PIS.getStatus().equalsIgnoreCase(cancelType)
                    && bookID.equalsIgnoreCase(requestBookID)) {
                if (bookingEngineRequest.has("requestBody") && bookingEngineRequest.has("requestHeader")) {
                    JSONObject requestBody = bookingEngineRequest.getJSONObject("requestBody");
                    JSONObject requestHeader = bookingEngineRequest.getJSONObject("requestHeader");

                    Map<String, String> indentifier = new HashMap<>();
                    List<Map<String, String>> listOfSupplierBookReference = new ArrayList<>();

                    if (requestBody.has("supplierBookReferences")) {
                        JSONArray supplierBookReferences = requestBody.getJSONArray("supplierBookReferences");
                        for (int j = 0; j < supplierBookReferences.length(); j++) {
                            JSONObject aSupplierBookReference = supplierBookReferences.getJSONObject(j);
                            if (aSupplierBookReference.has("supplierRef")) {
                                indentifier.put("orderID", aSupplierBookReference.getString("orderID"));
                                indentifier.put("supplierRef", aSupplierBookReference.getString("supplierRef"));
                                indentifier.put("paxDetails", Arrays.toString(this.getPaxDetails(aSupplierBookReference).toArray()));
                            }
                            listOfSupplierBookReference.add(indentifier);
                        }
                    }
                    //BKNGENG_RS
                    JSONObject bookingEngineResponse = consumeAmendBookingResponseForAir(requestHeader);
                    if (bookingEngineResponse == null) {
                        continue loop;
                    }
                    if (bookingEngineResponse.has("responseBody")) {
                        JSONObject responseBody = bookingEngineResponse.getJSONObject("responseBody");
                        if (responseBody.has("supplierBookReferences")) {
                            JSONArray supplierBookReferences = responseBody.getJSONArray("supplierBookReferences");
                            for (int k = 0; k < supplierBookReferences.length(); k++) {
                                JSONObject aResponseSupplierDetails = supplierBookReferences.getJSONObject(k);
                                String responseSupplierRef = aResponseSupplierDetails.getString("supplierRef");
                                String responseStatus = aResponseSupplierDetails.getString("status");
                                Integer companyCharges = aResponseSupplierDetails.getInt("companyCharges");
                                for (Map<String, String> map : listOfSupplierBookReference) {
                                    Set<Map.Entry<String, String>> entries = map.entrySet();
                                    String requestOrderID = null;
                                    String requestSupplierRef = null;
                                    String requestPaxDetails = null;
                                    for (Map.Entry entry : entries) {
                                        if (entry.getKey().equals("orderID")) {
                                            requestOrderID = entry.getValue().toString();
                                        } else if (entry.getKey().equals("supplierRef")) {
                                            requestSupplierRef = entry.getValue().toString();
                                        } else if (entry.getKey().equals("paxDetails")) {
                                            requestPaxDetails = entry.getValue().toString();
                                        }
                                    }
                                    if (requestSupplierRef.equalsIgnoreCase(responseSupplierRef)) {
                                        BookingHistoryItem bookingHistoryItem = new BookingHistoryItem();
                                        bookingHistoryItem.setBookID(requestBookID);
                                        bookingHistoryItem.setOrderID(requestOrderID);
                                        bookingHistoryItem.setProductCategory(OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION.getCategory());
                                        bookingHistoryItem.setProductSubCategory(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory());
                                        bookingHistoryItem.setAction(cancelType);
                                        bookingHistoryItem.setDescription("Updated pax details: " + requestPaxDetails + ",Compnay charges: " + companyCharges);
                                        bookingHistoryItem.setStatus(responseStatus);
                                        bookingHistoryItem.setTimestamp(timeStamp);
                                        list.add(bookingHistoryItem);
                                    }
                                }
                            }
                        }
                    } else if (bookingEngineResponse.has("errorCode")) {
                        BookingHistoryItem errorResponse = this.getErrorResponse(requestBookID, bookingEngineResponse, timeStamp, cancelType);
                        list.add(errorResponse);
                    }
                }
            }
        }
        return list;
    }

    private List<String> getPaxDetails(JSONObject aSupplierBookReference) {
        List<String> listOfPax = new ArrayList<>();
        Map<String, String> paxMap = new HashMap<>();
        JSONArray paxDetails = aSupplierBookReference.getJSONArray("paxDetails");
        for (int k = 0; k < paxDetails.length(); k++) {
            JSONObject aPax = paxDetails.getJSONObject(k);
            JSONObject paxDataAmendInfo = null;
            try {
                paxMap.put("name", aPax.getString("firstName") + " " + aPax.getString("surname"));
                paxDataAmendInfo = aPax.getJSONObject("paxDataAmendInfo");
                paxMap.put("emailID", paxDataAmendInfo.getJSONObject("emailInfo").getString("emailID"));
                paxMap.put("phoneNumber", paxDataAmendInfo.getJSONObject("telephoneInfo").getString("phoneNumber"));
                listOfPax.add(paxMap.toString());
            } catch (Exception e) {

            }
        }
        return listOfPax;
    }

    private List<BookingHistoryItem> getFullCancellationHistory(String bookID) throws OperationException {
        List<BookingHistoryItem> list = new ArrayList<>();
        JSONObject jsonRequest = this.createJsonRequest(BEInboundOperation.CANCEL.getInboundOperation(), BEOperationId.BOOKING_ENGINE_RQ.getOperationid(), CancelAmendTypes.AIR_CANCELTYPE_CANCELALL.getStatus(), bookID);
        JSONArray searchResArr = this.getSearchResultArray(jsonRequest, bookID);
        loop:
        for (int i = 0; i < searchResArr.length(); i++) {
            String requestBookID = null;
            String cancelType = null;
            String operationId = null;
            JSONObject source = searchResArr.getJSONObject(i).getJSONObject("_source");
            String timeStamp = searchResArr.getJSONObject(i).getJSONObject("_source").getString("timestamp");
            String messageJson = source.getString("message");
            if (messageJson.contains("xml")) {
                continue loop;
            }
            //BKNGENG_RQ
            JSONObject bookingEngineRequest = this.getRequestJson(messageJson);
            try {
                requestBookID = bookingEngineRequest.getJSONObject("requestBody").getString("bookID");
                cancelType = bookingEngineRequest.getJSONObject("requestBody").getString("cancelType");
                operationId = source.getString(KibanaRequestParameters.OPERATIONSID.getStatus());
            } catch (Exception e) {
                continue loop;
            }
            if (BEOperationId.BOOKING_ENGINE_RQ.getOperationid().equalsIgnoreCase(operationId)
                    && BEInboundOperation.CANCEL.getInboundOperation().equalsIgnoreCase(source.getString(KibanaRequestParameters.INBOUND_OPERATIONS.getStatus()).trim())
                    && CancelAmendTypes.AIR_CANCELTYPE_CANCELALL.getStatus().equalsIgnoreCase(cancelType)
                    && bookID.equalsIgnoreCase(requestBookID)) {
                if (bookingEngineRequest.has("requestBody") && bookingEngineRequest.has("requestHeader")) {
                    Map<String, String> indentifier = new HashMap<>();

                    JSONObject requestBody = bookingEngineRequest.getJSONObject("requestBody");
                    JSONObject requestHeader = bookingEngineRequest.getJSONObject("requestHeader");

                    if (requestBody.has("supplierBookReferences")) {
                        JSONArray supplierBookReferences = requestBody.getJSONArray("supplierBookReferences");
                        for (int j = 0; j < supplierBookReferences.length(); j++) {
                            JSONObject aSupplierBookReference = supplierBookReferences.getJSONObject(i);
                            if (aSupplierBookReference.has("orderID") && aSupplierBookReference.has("supplierRef")) {
                                indentifier.put(aSupplierBookReference.getString("orderID"), aSupplierBookReference.getString("supplierRef"));
                            }
                        }
                    }

                    //BKNGENG_RS
                    JSONObject bookingEngineResponse = consumeCancelBookingResponseForAir(requestHeader);
                    if (bookingEngineResponse.has("responseBody")) {
                        JSONObject responseBody = bookingEngineResponse.getJSONObject("responseBody");
                        if (responseBody.has("supplierBookReferences")) {
                            JSONArray supplierBookReferences = responseBody.getJSONArray("supplierBookReferences");
                            for (int k = 0; k < supplierBookReferences.length(); k++) {
                                JSONObject aResponseSupplierDetails = supplierBookReferences.getJSONObject(k);
                                String supplierRef = aResponseSupplierDetails.getString("supplierRef");
                                String responseStatus = aResponseSupplierDetails.getString("status");
                                Integer companyCharges = aResponseSupplierDetails.getInt("companyCharges");
                                Set<Map.Entry<String, String>> entries = indentifier.entrySet();
                                for (Map.Entry entry : entries) {
                                    if (supplierRef.equalsIgnoreCase(entry.getValue().toString())) {
                                        BookingHistoryItem bookingHistoryItem = new BookingHistoryItem();
                                        bookingHistoryItem.setBookID(requestBookID);
                                        bookingHistoryItem.setOrderID(entry.getKey().toString());
                                        bookingHistoryItem.setProductCategory(OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION.getCategory());
                                        bookingHistoryItem.setProductSubCategory(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory());
                                        bookingHistoryItem.setAction(cancelType);
                                        bookingHistoryItem.setDescription("Cancelled OrderID: " + entry.getKey() + ",Compnay charges: " + companyCharges);
                                        bookingHistoryItem.setStatus(responseStatus);
                                        bookingHistoryItem.setTimestamp(timeStamp);
                                        list.add(bookingHistoryItem);
                                    }
                                }
                            }
                        }
                    } else if (bookingEngineResponse.has("errorCode")) {
                        BookingHistoryItem errorResponse = this.getErrorResponse(requestBookID, bookingEngineResponse, timeStamp, cancelType);
                        list.add(errorResponse);
                    }
                }
            }
        }
        return list;
    }

    private BookingHistoryItem getErrorResponse(String requestBookID, JSONObject bookingEngineResponse, String
            timeStamp, String cancelType) {
        String errorMsg = null;
        String errorCode = null;
        BookingHistoryItem bookingHistoryItem = new BookingHistoryItem();
        bookingHistoryItem.setBookID(requestBookID);
        bookingHistoryItem.setProductCategory(OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION.getCategory());
        bookingHistoryItem.setProductSubCategory(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory());
        bookingHistoryItem.setAction(cancelType);
        try {
            errorMsg = bookingEngineResponse.getString("errorMessage");
            errorCode = bookingEngineResponse.getString("errorCode");
            bookingHistoryItem.setDescription("Error Message: " + errorMsg + ",Error Code: " + errorCode);
        } catch (Exception e) {
            bookingHistoryItem.setDescription("Error Message: " + errorMsg + ",Error Code: " + errorCode);
        }
        bookingHistoryItem.setStatus("ERROR");
        bookingHistoryItem.setTimestamp(timeStamp);
        return bookingHistoryItem;
    }

    private JSONObject getRequestJson(String messageJson) {
        int endWith = messageJson.indexOf("{");
        String replaceString = messageJson.substring(0, endWith);
        String message = messageJson.replace(replaceString, "");
        return new JSONObject(new JSONTokener(message));
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

    private JSONObject consumeCancelBookingResponseForAir(JSONObject jsonObject) {
        JSONObject bookingResponse = null;
        try {
            bookingResponse = bookingEngineElasticData.getJSONData(
                    jsonObject.getString("userID"),
                    jsonObject.getString("transactionID"),
                    jsonObject.getString("sessionID"),
                    BEInboundOperation.CANCEL,
                    BEOperationId.BOOKING_ENGINE_RS,
                    BEServiceUri.FLIGHT);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (OperationException e) {
            e.printStackTrace();
        }
        return bookingResponse;
    }

    private JSONObject consumeAmendBookingResponseForAir(JSONObject jsonObject) {
        JSONObject bookingResponse = null;
        try {
            bookingResponse = bookingEngineElasticData.getJSONData(
                    jsonObject.getString("userID"),
                    jsonObject.getString("transactionID"),
                    jsonObject.getString("sessionID"),
                    BEInboundOperation.AMEND,
                    BEOperationId.BOOKING_ENGINE_RS,
                    BEServiceUri.FLIGHT);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (OperationException e) {
            e.printStackTrace();
        }
        return bookingResponse;
    }

    private JSONObject createJsonRequest(String inbound_operation, String operationid, String
            modificationType, String bookID) {
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
        sourceList.put(KibanaRequestParameters.INBOUND_SERVICE_URI.getStatus());

        JSONObject requestJsonObject = new JSONObject();
        requestJsonObject.put("query", boolObject);
        requestJsonObject.put("_source", sourceList);
        requestJsonObject.put("size", 30);

        return requestJsonObject;
    }
}
