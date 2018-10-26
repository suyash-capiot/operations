package com.coxandkings.travel.operations.service.qcmanagement.impl;

import com.coxandkings.travel.operations.enums.amendclientcommercials.BEInboundOperation;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEOperationId;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEServiceUri;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsOrderClientCommercial;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.resource.qcmanagement.SupplierTypeWiseInfo;
import com.coxandkings.travel.operations.resource.qcmanagement.client.*;
import com.coxandkings.travel.operations.service.qcmanagement.QcClientCommercialService;
import com.coxandkings.travel.operations.service.qcmanagement.QcUtilService;
import com.coxandkings.travel.operations.utils.BookingEngineElasticData;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QcClientCommercialServiceImpl implements QcClientCommercialService {
    private static final Logger logger = LogManager.getLogger(QcClientCommercialServiceImpl.class);

    @Autowired
    QcUtilService qcUtilService;
    @Value(value = "${qc_management.brms.air-client-commercial-url}")
    private String airClientCommercialBrmsUrl;

    @Value(value = "${qc_management.jsonExpression.client-air-response}")
    private String clientAirResponseJsonPath;
    @Value(value = "${qc_management.brms.acco-client-commercial-url}")
    private String accoClientCommercialBrmsUrl;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private BookingEngineElasticData bookingEngineElasticData;

    static private Map<String, Map<String, Integer>> getBrmsEntityWiseCommercialsForAir(List<BrmsClientPassengerDetail> passengerDetails) {
        //fix if multiple clients
        Map<String, Map<String, Integer>> calculatedCommercialByEntityName = new HashMap<>();
        Map<String, Integer> commercialByEntityName = new HashMap<>();
        String entityName = null;
        for (BrmsClientPassengerDetail brmsClientPassengerDetail : passengerDetails) {

            List<BrmsClientEntityCommercial> entityCommercials = brmsClientPassengerDetail.getEntityCommercials();
            if (entityCommercials.size() > 0) {
                for (BrmsClientEntityCommercial brmsClientEntityCommercial : entityCommercials) {
                    entityName = brmsClientEntityCommercial.getEntityName();
                    List<AdditionalCommercialDetail> additionalCommercialDetails = brmsClientEntityCommercial.getAdditionalCommercialDetails();

                    //AdditionalCommercial total
                    for (AdditionalCommercialDetail additionalCommercialDetail : additionalCommercialDetails) {
                        String commercialName = additionalCommercialDetail.getCommercialName();
                        Integer entityValue = commercialByEntityName.get(commercialName);
                        if (entityValue == null) {
                            commercialByEntityName.put(commercialName, additionalCommercialDetail.getCommercialAmount());
                        } else {
                            commercialByEntityName.put(commercialName, entityValue + additionalCommercialDetail.getCommercialAmount());
                        }
                    }

                    //MarUpCommercial total
                    MarkUpCommercialDetails markUpCommercialDetails = brmsClientEntityCommercial.getMarkUpCommercialDetails();
                    String markUpName = markUpCommercialDetails.getCommercialName();
                    Integer markUpEntityValue = commercialByEntityName.get(markUpName);
                    if (markUpEntityValue == null)
                        commercialByEntityName.put(markUpName, markUpCommercialDetails.getCommercialAmount());
                    else
                        commercialByEntityName.put(markUpName, markUpEntityValue + markUpCommercialDetails.getCommercialAmount());
                }
            }
        }
        calculatedCommercialByEntityName.put(entityName, commercialByEntityName);
        return calculatedCommercialByEntityName;
    }

    @Override
    public Boolean qcCheckForAirClientCommercial(OpsBooking opsBooking) throws JSONException {

        //getting SI response in json
        JSONObject airClietnCommercialResp = this.consumeAirClientCommercials(opsBooking);
        Boolean qcStatus = false;
        if (airClietnCommercialResp != null) {
            HttpEntity<String> requestEntity = qcUtilService.createHeader(airClietnCommercialResp.toString());
            ResponseEntity<String> clientCommercial = null;
            //getting calculated values from BRMS
            try {
                clientCommercial = RestUtils.exchange(airClientCommercialBrmsUrl, HttpMethod.POST, requestEntity, String.class);
            } catch (Exception e) {
                logger.error("Error While Retreiving client Commercial From BRMS " + e);
                e.printStackTrace();
            }

            Map<SupplierTypeWiseInfo, Map<String, Map<String, Integer>>> supplierTypeWiseInfoMap = new HashMap<>();
            List<BRMSClientResponse> brmsClientResponseList = jsonObjectProvider.getChildrenCollection(clientCommercial.getBody(), clientAirResponseJsonPath, BRMSClientResponse.class);
            Map<String, Map<String, Integer>> calculatedCommercialByEntityName = null;
            for (BRMSClientResponse aBrmsClientResponse : brmsClientResponseList) {

                String brmsSupplierId = aBrmsClientResponse.getAdvancedDefinition().getConnectivitySupplier();
                String brmsSupplierName = aBrmsClientResponse.getAdvancedDefinition().getCredentialsName();

                String brmsBookingType = aBrmsClientResponse.getAdvancedDefinition().getBookingType();
                if ("online".equalsIgnoreCase(brmsBookingType)) {
                    for (BrmsClientJourneyDetail aBrmsClientJourneyDetail : aBrmsClientResponse.getJourneyDetails()) {
                        Integer flightNumber = aBrmsClientJourneyDetail.getFlightDetails().stream().map(BrmsClientFlightDetail::getFlightNumber).findFirst().get();
                        calculatedCommercialByEntityName = getBrmsEntityWiseCommercialsForAir(aBrmsClientJourneyDetail.getPassengerDetails());

                        SupplierTypeWiseInfo supplierTypeWiseInfo = new SupplierTypeWiseInfo();
                        supplierTypeWiseInfo.setSupplierId(brmsSupplierId);
                        supplierTypeWiseInfo.setSupplierName(brmsSupplierName);
                        supplierTypeWiseInfo.setFlightNumber(flightNumber.toString());

                        supplierTypeWiseInfoMap.put(supplierTypeWiseInfo, calculatedCommercialByEntityName);
                    }
                }
            }

            Map<SupplierTypeWiseInfo, Map<String, Map<String, Integer>>> opsOrderWiseInfoMap = getOpsOrderClientCommercialByEntityForAir(opsBooking);

            //compare supplierTypeWiseInfoMap opsOrderWiseInfoMap properly///test TOdo
            qcStatus = supplierTypeWiseInfoMap.entrySet().stream().filter(value -> opsOrderWiseInfoMap.entrySet().stream().anyMatch(value1 -> (value1.getKey() == value.getKey() && value1.getValue() == value.getValue()))).findAny().isPresent();
        }
        return qcStatus;
    }

    private Map<SupplierTypeWiseInfo, Map<String, Map<String, Integer>>> getOpsOrderClientCommercialByEntityForAir(OpsBooking opsBooking) {
        Map<SupplierTypeWiseInfo, Map<String, Map<String, Integer>>> typeWiseInfoMap = new HashMap<>();
        for (OpsProduct opsProduct : opsBooking.getProducts()) {

            if (opsProduct.getOpsProductSubCategory().getSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
                Map<String, Map<String, Integer>> entityWiseDetails = new HashMap<>();
                String opsSupplierId = opsProduct.getSupplierID();
                String opsSupplierName = opsProduct.getSourceSupplierName();
                String opsFlightNumber = opsProduct.getOrderDetails().getFlightDetails().getOriginDestinationOptions().get(0).getFlightSegment().get(0).getMarketingAirline().getFlightNumber();

                Map<String, Integer> opsClientCommercialDetails = new HashMap<>();
                String entityName = null;
                List<OpsOrderClientCommercial> opsOrderClientCommercialList = opsProduct.getOrderDetails().getClientCommercials();
                for (OpsOrderClientCommercial opsOrderClientCommercial : opsOrderClientCommercialList) {
                    opsClientCommercialDetails.put(opsOrderClientCommercial.getCommercialName(), Integer.parseInt(opsOrderClientCommercial.getCommercialAmount()));
                    entityName = opsOrderClientCommercial.getCommercialEntityID();//bug
                }
                entityWiseDetails.put(entityName, opsClientCommercialDetails);

                SupplierTypeWiseInfo supplierTypeWiseInfo = new SupplierTypeWiseInfo();
                supplierTypeWiseInfo.setSupplierId(opsSupplierId);
                supplierTypeWiseInfo.setSupplierName(opsSupplierName);
                supplierTypeWiseInfo.setFlightNumber(opsFlightNumber);

                typeWiseInfoMap.put(supplierTypeWiseInfo, entityWiseDetails);
            }

        }
        return typeWiseInfoMap;
    }

    @Override
    public Boolean qcCheckForAccoClientCommercial(OpsBooking opsBooking) throws JSONException {
        //getting SI response in json
        JSONObject accoClientCommercialsResp = consumeAccoClientCommercials(opsBooking);
        Boolean qcStatus = false;
        if (accoClientCommercialsResp != null) {
            HttpEntity<String> requestEntity = qcUtilService.createHeader(accoClientCommercialsResp.toString());
            ResponseEntity<String> clientCommercial = null;
            //getting calculated values from BRMS
            try {
                clientCommercial = RestUtils.exchange(accoClientCommercialBrmsUrl, HttpMethod.POST, requestEntity, String.class);
            } catch (Exception e) {
                logger.error("Error While Retreiving client Commercial From BRMS " + e);
                e.printStackTrace();
            }

            ObjectMapper mapper = new ObjectMapper();
            List<JsonNode> commonList = null;
            try {
                commonList = mapper.readTree(clientCommercial.getBody()).findValues("businessRuleIntake");
            } catch (IOException e) {
                e.printStackTrace();
            }

            Map<String, Map<String, Map<String, Integer>>> brmsCalculatedCommercial = new HashMap<>();
            for (int i = 0; i < commonList.size(); i++) {
                JsonNode jsonNode = commonList.get(i);
                //ToDo check online or offline
                String bookingType = jsonNode.findPath("advancedDefinition").path("bookingType").toString();
                String supplierId = jsonNode.findPath("commonElements").path("supplier").toString();

                //calculated commercial by enity name
                Map<String, Map<String, Integer>> calculatedCommercialByEntityName = new HashMap<>();
                List<JsonNode> hotelDetailsList = jsonNode.findValues("hotelDetails");
                for (JsonNode aHotelDetail : hotelDetailsList) {
                    //Todo in case of two different hotels if entity names are same so it will override the all values with new one
                    //take some unique feild from hotel detail.
                    String hotelDetailsJson = aHotelDetail.toString();
                    JsonNode roomDetails = null;
                    try {
                        roomDetails = mapper.readTree(hotelDetailsJson).findValue("roomDetails");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //calculated commercial map
                    Map<String, Integer> commercialByCommercialName = new HashMap<>();
                    String entityName = null;
                    List<JsonNode> roomDetailsList = roomDetails.findParents("entityCommercials");
                    for (JsonNode aRoomDetail : roomDetailsList) {
                        //check if any commercials are applied or not
                        String commercialsApplied = aRoomDetail.path("commercialsApplied").get(0).toString();
                        if (!StringUtils.isEmpty(commercialsApplied)) {
                            //for every single entity commercial it will return an object in array so used get(0).
                            String entityCommercials = aRoomDetail.path("entityCommercials").toString();
                            BrmsClientEntityCommercial entityCommercial = (BrmsClientEntityCommercial) jsonObjectProvider.getChildrenCollection(entityCommercials, "$.*", BrmsClientEntityCommercial.class).get(0);
                            //brmsTotalEntityWiseCommercial = getCalculatedEntityWiseCommercialsForAcco(entityCommercial);
                            if (entityCommercial != null) {
                                entityName = entityCommercial.getEntityName();
                                List<AdditionalCommercialDetail> additionalCommercialDetails = entityCommercial.getAdditionalCommercialDetails();
                                //AdditionalCommercial total
                                for (AdditionalCommercialDetail additionalCommercialDetail : additionalCommercialDetails) {
                                    String commercialName = additionalCommercialDetail.getCommercialName();
                                    Integer entityValue = commercialByCommercialName.get(commercialName);
                                    if (entityValue == null) {
                                        commercialByCommercialName.put(commercialName, additionalCommercialDetail.getCommercialAmount());
                                    } else {
                                        commercialByCommercialName.put(commercialName, entityValue + additionalCommercialDetail.getCommercialAmount());
                                    }
                                }
                                //MarUpCommercial total
                                MarkUpCommercialDetails markUpCommercialDetails = entityCommercial.getMarkUpCommercialDetails();
                                String markUpName = markUpCommercialDetails.getCommercialName();
                                Integer markUpEntityValue = commercialByCommercialName.get(markUpName);
                                if (markUpEntityValue == null)
                                    commercialByCommercialName.put(markUpName, markUpCommercialDetails.getCommercialAmount());
                                else
                                    commercialByCommercialName.put(markUpName, markUpEntityValue + markUpCommercialDetails.getCommercialAmount());
                            }
                        }
                    }
                    calculatedCommercialByEntityName.put(entityName, commercialByCommercialName);
                }
                brmsCalculatedCommercial.put(supplierId, calculatedCommercialByEntityName);
            }

            //ops details
            Map<String, Map<String, Map<String, Integer>>> opsCalculatedTotalCommercial = getOpsOrderClientCommercialByEntityForAcco(opsBooking);
            //compare//todo not working
            qcStatus = brmsCalculatedCommercial.entrySet().stream().filter(value -> opsCalculatedTotalCommercial.entrySet().stream().anyMatch(value1 -> (value1.getKey() == value.getKey() && value1.getValue() == value.getValue()))).findAny().isPresent();
        }
        return qcStatus;
    }

    private Map<String, Map<String, Map<String, Integer>>> getOpsOrderClientCommercialByEntityForAcco(OpsBooking opsBooking) {
        Map<String, Map<String, Map<String, Integer>>> typeWiseInfoMap = new HashMap<>();
        for (OpsProduct opsProduct : opsBooking.getProducts()) {
            if ("online".equalsIgnoreCase(onlineOrOfflineOrder(opsProduct))) {
                if (opsProduct.getOpsProductSubCategory().getSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {
                    Map<String, Map<String, Integer>> entityWiseDetails = new HashMap<>();
                    String opsSupplierId = opsProduct.getSupplierID();

                    Map<String, Integer> opsClientCommercialDetails = new HashMap<>();
                    String entityName = null;
                    List<OpsOrderClientCommercial> opsOrderClientCommercialList = opsProduct.getOrderDetails().getClientCommercials();
                    for (OpsOrderClientCommercial opsOrderClientCommercial : opsOrderClientCommercialList) {
                        opsClientCommercialDetails.put(opsOrderClientCommercial.getCommercialName(), Integer.parseInt(opsOrderClientCommercial.getCommercialAmount()));
                        entityName = opsOrderClientCommercial.getCommercialEntityID();//bug
                    }
                    entityWiseDetails.put(entityName, opsClientCommercialDetails);

                    typeWiseInfoMap.put(opsSupplierId, entityWiseDetails);
                }
            }
        }
        return typeWiseInfoMap;
    }


    private String onlineOrOfflineOrder(OpsProduct opsProduct) {
        return opsProduct.getOrderDetails().getSupplierType().getSupplierType();
    }

    private JSONObject consumeAirClientCommercials(OpsBooking opsBooking) {
        JSONObject jsonOfSupplierCommercials = null;
        try {
            jsonOfSupplierCommercials = bookingEngineElasticData.getJSONData(
                    opsBooking.getUserID(),
                    opsBooking.getTransactionID(),
                    opsBooking.getSessionID(),
                    BEInboundOperation.REPRICE,
                    BEOperationId.CLIENT_COMMERCIAL_RQ,
                    BEServiceUri.FLIGHT);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (OperationException e) {
            e.printStackTrace();
        }
        return jsonOfSupplierCommercials;
    }

    private JSONObject consumeAccoClientCommercials(OpsBooking opsBooking) {
        JSONObject jsonOfSupplierCommercials = null;
        try {
            jsonOfSupplierCommercials = bookingEngineElasticData.getJSONData(
                    opsBooking.getUserID(),
                    opsBooking.getTransactionID(),
                    opsBooking.getSessionID(),
                    BEInboundOperation.REPRICE,
                    BEOperationId.CLIENT_COMMERCIAL_RQ,
                    BEServiceUri.HOTEL);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (OperationException e) {
            e.printStackTrace();
        }
        return jsonOfSupplierCommercials;
    }
}
