package com.coxandkings.travel.operations.service.qcmanagement.impl;

import com.coxandkings.travel.operations.enums.amendclientcommercials.BEInboundOperation;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEOperationId;
import com.coxandkings.travel.operations.enums.qcmanagement.CancelAmendTypes;
import com.coxandkings.travel.operations.enums.qcmanagement.KibanaRequestParameters;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.service.qcmanagement.QcCancellationService;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.coxandkings.travel.operations.utils.xml.XMLTransformer;
import com.coxandkings.travel.operations.utils.xml.XMLUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class QcCancellationServiceImpl implements QcCancellationService {

    @Value("${elastic-search.booking-engine}")
    private String bookingEngineIndexUrl;

    @Override
    public boolean doQcLevelCheck(OpsProduct opsProduct, CancelAmendTypes cancelAmendTypes) {

        boolean checkForCancellations = false;
        try {
            checkForCancellations = doQcCheckForCancellations(opsProduct.getSupplierReservationId(), opsProduct, cancelAmendTypes);
        } catch (OperationException e) {
            e.printStackTrace();
        }
        //amendments
        return false;
    }


    private boolean doQcCheckForAmendments(String supplierReservationId) {
        return false;
    }

    private boolean doQcCheckForCancellations(String supplierReservationId, OpsProduct opsProduct, CancelAmendTypes cancelAmendTypes) throws OperationException {
        JSONObject jsonRequest = createJsonRequest(BEInboundOperation.MODIFY.getInboundOperation(), BEOperationId.SUPPLIER_INTEGRATION_RQ.getOperationid(), cancelAmendTypes.getStatus(), supplierReservationId);
        JSONArray searchResultArray = getSearchResultArray(jsonRequest, supplierReservationId);
        loop:
        for (int i = 0; i < searchResultArray.length(); i++) {
            JSONObject hits = searchResultArray.getJSONObject(i);
            Element logMessage = this.getLogMessage(hits);
            if (isXmlCriteriaMatch(logMessage, opsProduct)) {
                //get sessionID , transactionID,userId
                String sessionId = hits.getString("sessionid");
                String userId = hits.getString("userid");
                String transactionId = hits.getString("transactionid");
                //fetch SUPPL_RS

                //check the charges that are leived for cancellations
            } else
                continue loop;
        }
        return false;
    }

    private boolean isXmlCriteriaMatch(Element logMessage, OpsProduct opsProduct) {
        if (logMessage != null) {
            Element[] resWrapperElems = XMLUtils.getElementsAtXPath(logMessage, "./accoi:RequestBody/acco:OTA_HotelResModifyRQWrapper");
            for (Element resWrapperElem : resWrapperElems) {
                String supplierId = XMLUtils.getValueAtXPath(resWrapperElem, "./acco:SupplierID");
                Element[] hotelResModifyRQ = XMLUtils.getElementsAtXPath(resWrapperElem, "./ota:OTA_HotelResModifyRQ");
                for (Element aHotelResModifyRQ : hotelResModifyRQ) {
                    String uniq = null;
                    Element[] elementsAtXPath = XMLUtils.getElementsAtXPath(aHotelResModifyRQ, "./ota:HotelResModifies/ota:HotelResModify/ota:UniqueID");
                    for (Element element : elementsAtXPath) {
                        if (element.getAttribute("Type").equalsIgnoreCase("14")) {
                            uniq = element.getAttribute("ID");
                            break;
                        }
                    }
                    if (opsProduct.getSupplierReservationId().equalsIgnoreCase(uniq)
                            && opsProduct.getSupplierID().equalsIgnoreCase(supplierId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Element getLogMessage(JSONObject input) {
        String logMessage = null;
        if (input.has(KibanaRequestParameters.SOURCE.getStatus())) {
            if (input.getJSONObject(KibanaRequestParameters.SOURCE.getStatus()).has(KibanaRequestParameters.LOGMESSAGE.getStatus()))
                logMessage = input.getJSONObject(KibanaRequestParameters.SOURCE.getStatus()).getString(KibanaRequestParameters.LOGMESSAGE.getStatus());
        }
        if (logMessage != null) {
            return XMLTransformer.fromEscapedString(logMessage);
        } else
            return null;
    }

    private JSONArray getSearchResultArray(JSONObject jsonRequest, String supplierReservationID) throws OperationException {
        ResponseEntity<String> searchResult = RestUtils.postForEntity(bookingEngineIndexUrl, jsonRequest.toString(), String.class);
        JSONObject searchResJSON = new JSONObject(searchResult.getBody());
        JSONArray searchResArr = searchResJSON.getJSONObject("hits").getJSONArray("hits");
        if (searchResArr.length() == 0) {
            throw new OperationException(String.format("No Matching Message Found for userId <%s>, transactionId <%s>, sessionId <%s>,  inboundOperation<%s>, operationId<%s>, service<%s>",
                    KibanaRequestParameters.INBOUND_OPERATIONS.getStatus(), KibanaRequestParameters.OPERATIONSID.getStatus(), supplierReservationID));
        }
        return searchResArr;
    }

    private JSONObject createJsonRequest(String inbound_operation, String operationid, String modificationType, String supplierReservationId) {
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
        matchFour.put(KibanaRequestParameters.MESSAGE.getStatus(), supplierReservationId);

        JSONObject matchFive = new JSONObject();
        matchFive.put(KibanaRequestParameters.MESSAGE.getStatus(), "xml");

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

        JSONObject innerMatchFive = new JSONObject();
        innerMatchFive.put("match", matchFive);

        JSONArray shouldJsonArray = new JSONArray();
        shouldJsonArray.put(innerMatchOne);
        shouldJsonArray.put(innerMatchTwo);
        if (innerMatchThree != null) {
            shouldJsonArray.put(innerMatchThree);
        }
        shouldJsonArray.put(innerMatchFour);
        shouldJsonArray.put(innerMatchFive);

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
        sourceList.put(KibanaRequestParameters.LOGMESSAGE.getStatus());
        sourceList.put(KibanaRequestParameters.INBOUND_OPERATIONS.getStatus());
        sourceList.put(KibanaRequestParameters.SESSIONID.getStatus());
        sourceList.put(KibanaRequestParameters.USERID.getStatus());
        sourceList.put(KibanaRequestParameters.OPERATIONSID.getStatus());
        sourceList.put(KibanaRequestParameters.TRANSACTIONID.getStatus());

        JSONObject requestJsonObject = new JSONObject();
        requestJsonObject.put("query", boolObject);
        requestJsonObject.put("_source", sourceList);
        requestJsonObject.put("size", 50);

        return requestJsonObject;
    }
}
