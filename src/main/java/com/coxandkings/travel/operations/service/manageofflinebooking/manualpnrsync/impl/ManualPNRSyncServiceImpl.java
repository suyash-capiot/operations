package com.coxandkings.travel.operations.service.manageofflinebooking.manualpnrsync.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.ErrorResponseResource;
import com.coxandkings.travel.operations.resource.manageofflinebooking.*;
import com.coxandkings.travel.operations.service.manageofflinebooking.manualpnrsync.ManualPNRSyncService;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManualPNRSyncServiceImpl implements ManualPNRSyncService {

    @Value("${offline-booking.BE-retrieveairbookingno}")
    private String retrieveAirBookingNo;

    private String bookingURL = null;

    @Override
    public OpsResponse retrieveBooking(ManualPNRSyncResource manualPNRSyncResource) throws OperationException {
        OpsResponse os = null;
        try {
            String retrieveRequest = createRetrieveRequest(manualPNRSyncResource);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<>(retrieveRequest, headers);
            //Retrieve booking from B.E
            RestTemplate restTemplate = RestUtils.getTemplate();
            ResponseEntity<String> responseBE = restTemplate.exchange(bookingURL, HttpMethod.POST, httpEntity, String.class);

            os = createRetrieveResponse(responseBE.toString());


        } catch (Exception e) {
            Map<String, String> entity = new HashMap<>();
            entity.put("Error", String.format("Mandatory details are not entered :" + e.getMessage()));
            throw new OperationException(entity);
        }
        return os;
    }

    public String createRetrieveRequest(ManualPNRSyncResource manualPNRSyncResource) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Map retrieveRequest = new HashMap();
        retrieveRequest.put("requestHeader", createRetrieveHeader());
        retrieveRequest.put("requestBody", createRetrieveBody(manualPNRSyncResource));

        return mapper.writeValueAsString(retrieveRequest).toString();
    }

    public OpsRequestHeader createRetrieveHeader() {
        OpsRequestHeader requestHeader = new OpsRequestHeader();

        OpsClientContext clientContext = new OpsClientContext();
        clientContext.setPointOfSale("Website   ");
        clientContext.setClientID("");
        clientContext.setClientLanguage("English");
        clientContext.setClientType("B2C");
        clientContext.setClientCallbackAddress("");
        clientContext.setClientMarket("India");
        clientContext.setClientCurrency("INR");
        clientContext.setClientNationality("");
        clientContext.setClientIATANumber("1234");

        requestHeader.setSessionID("00GAL55");
        requestHeader.setUserID("CORPTRL42");
        requestHeader.setTransactionID("");

        requestHeader.setClientContext(clientContext);

        return requestHeader;
    }

    public Map createRetrieveBody(ManualPNRSyncResource manualPNRSyncResource) {
        Map mapsupplierBookReferences = new HashMap();
        mapsupplierBookReferences.put("supplierRef", manualPNRSyncResource.getSupplierName());
        mapsupplierBookReferences.put("bookRefID", manualPNRSyncResource.getBookRefID());
        mapsupplierBookReferences.put("gdsPNR", manualPNRSyncResource.getGdsPNR());

        List<Map> lstSupplierBookReferences = new ArrayList<>();
        lstSupplierBookReferences.add(mapsupplierBookReferences);

        Map mapOriginDestinationInfo = new HashMap();
        mapOriginDestinationInfo.put("departureDate","2018-04-09");
        mapOriginDestinationInfo.put("originLocation", "BOM");
        mapOriginDestinationInfo.put("destinationLocation","DEL");

        Map mapOriginDestinationInfo2 = new HashMap();
        mapOriginDestinationInfo2.put("departureDate","2018-04-13");
        mapOriginDestinationInfo2.put("originLocation", "DEL");
        mapOriginDestinationInfo2.put("destinationLocation","BOM");

        List<Map> lstOriginDestinationInfo = new ArrayList<>();
        lstOriginDestinationInfo.add(mapOriginDestinationInfo);
        lstOriginDestinationInfo.add(mapOriginDestinationInfo2);

        Map mapPaxInfo = new HashMap();
        mapPaxInfo.put("paxType","ADT");
        mapPaxInfo.put("quantity", 2);

        Map mapPaxInfo2 = new HashMap();
        mapPaxInfo2.put("paxType","CHD");
        mapPaxInfo2.put("quantity", 2);

        Map mapPaxInfo3 = new HashMap();
        mapPaxInfo3.put("paxType","INF");
        mapPaxInfo3.put("quantity", 2);

        List<Map> lstPaxInfo = new ArrayList<>();
        lstPaxInfo.add(mapPaxInfo);
        lstPaxInfo.add(mapPaxInfo2);
        lstPaxInfo.add(mapPaxInfo3);

        Map mapRequestBody = new HashMap();
        mapRequestBody.put("supplierBookReferences", lstSupplierBookReferences);
        mapRequestBody.put("cabinType", "Economy");
        mapRequestBody.put("tripType", "Return");
        mapRequestBody.put("originDestinationInfo", lstOriginDestinationInfo);
        mapRequestBody.put("paxInfo", lstPaxInfo);

        return mapRequestBody;
    }

    public ErrorResponseResource getMessageToUser(String errorStr, HttpStatus httpStatus) {
        ErrorResponseResource resource = new ErrorResponseResource();
        resource.setMessage(errorStr);
        resource.setStatus(httpStatus);
        return resource;
    }

    public OpsResponse  createRetrieveResponse(String pBEResponse ){
        OpsResponse pOpsResponse = new OpsResponse();

        pOpsResponse.setOpsProductCategory("PRODUCT_CATEGORY_TRANSPORTATION");
        pOpsResponse.setOpsProductSubCategory("PRODUCT_SUB_CATEGORY_FLIGHT");
        pOpsResponse.setProductLevelActions( new ProductLevelActions());
        pOpsResponse.setAmendmentChargesDetails(new ArrayList());
        pOpsResponse.setCancellationChargesDetails(new ArrayList());
        pOpsResponse.setAveragePriced(true);
        pOpsResponse.setActualMarginAmount(1000);
        pOpsResponse.setChecked(true);
        pOpsResponse.setBookingId("BookingID");
        pOpsResponse.setSupplierID("supplierID");
        pOpsResponse.setOrderID( "orderID");
        pOpsResponse.setInventory("inventory");
        pOpsResponse.setProductCategory("productCategory");
        pOpsResponse.setOrderDetails(new OrderDetails());
        pOpsResponse.setProductSubCategory("Flight");
        pOpsResponse.setFileHandlerUserID("fileHandlerUserID");
        pOpsResponse.setFileHandlerUserName("fileHandlerUserName");
        pOpsResponse.setRoe(74);

        return pOpsResponse;
    }

    public void processBooking(ManualPNRSyncResource manualPNRSyncResource){
        try
        {
            String retrieveRequest = createRetrieveRequest(manualPNRSyncResource);
            JSONObject jsObjProcessRequest = new JSONObject(retrieveRequest);


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<>(retrieveRequest, headers);
            RestTemplate restTemplate = RestUtils.getTemplate();
            ResponseEntity<String> responseBE = restTemplate.exchange(bookingURL, HttpMethod.POST, httpEntity, String.class);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
