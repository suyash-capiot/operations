package com.coxandkings.travel.operations.service.qcmanagement.impl;

import com.coxandkings.travel.operations.enums.amendclientcommercials.BEInboundOperation;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEOperationId;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEServiceUri;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsOrderSupplierCommercial;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.resource.qcmanagement.CommonElementWiseInfo;
import com.coxandkings.travel.operations.resource.qcmanagement.supplier.*;
import com.coxandkings.travel.operations.service.qcmanagement.QcSupplierCommercialService;
import com.coxandkings.travel.operations.service.qcmanagement.QcUtilService;
import com.coxandkings.travel.operations.utils.BookingEngineElasticData;
import com.coxandkings.travel.operations.utils.CopyUtils;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class QcSupplierCommercialServiceImpl implements QcSupplierCommercialService {
    private static final Logger logger = LogManager.getLogger(QcSupplierCommercialServiceImpl.class);

    @Autowired
    QcUtilService qcUtilService;
    @Value(value = "${qc_management.brms.air-supplier-commercial-url}")
    private String supplierAirCommercialBrmsUrl;

    @Value(value = "${qc_management.jsonExpression.supplier-air-response}")
    private String supplierAirResponseJsonPath;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private BookingEngineElasticData bookingEngineElasticData;
    @Value(value = "${qc_management.brms.acco-supplierCommercial-url}")
    private String supplierAcooCommercialBrmsUrl;

    public Boolean qcCheckForOnlineAirSupplierCommercial(OpsBooking opsBooking) throws JSONException, OperationException {

        //getting SI response in json
        JSONObject airSupplierCommericalResJson = this.consumeAirSupplierCommercials(opsBooking);
        Boolean qcValue = false;
        if (airSupplierCommericalResJson != null) {
            HttpEntity<String> requestEntity = qcUtilService.createHeader(airSupplierCommericalResJson.toString());
            ResponseEntity<String> supplierCommercial = null;
            //getting calculated values from BRMS
            try {
                supplierCommercial = RestUtils.exchange(supplierAirCommercialBrmsUrl, HttpMethod.POST, requestEntity, String.class);
            } catch (Exception e) {
                logger.error("Error While Retreiving Supplier Commercial From BRMS " + e);
                e.printStackTrace();
            }

            Map<String, List<JSONObject>> brmsSupplierTransactionalResponse = new HashMap<>();
            List<BrmsSupplierResponse> brmsSupplierResponseList = jsonObjectProvider.getChildrenCollection(supplierCommercial.getBody(), supplierAirResponseJsonPath, BrmsSupplierResponse.class);
            if (brmsSupplierResponseList != null && brmsSupplierResponseList.size() > 0) {
                for (BrmsSupplierResponse aBrmsSupplierResponse : brmsSupplierResponseList) {
                    //get brms supplierType
                    String brmsSupplierType = aBrmsSupplierResponse.getAdvancedDefinition().getBookingType();
                    if ("online".equalsIgnoreCase(brmsSupplierType)) {
                        //get brms supplierID
                        String brmsSupplierId = aBrmsSupplierResponse.getAdvancedDefinition().getConnectivitySupplier();
                        List<JSONObject> listOfCommercials = new ArrayList<>();
                        for (BrmsSupplierJourneyDetail aBrmsSupplierJourneyDetail : aBrmsSupplierResponse.getBRMSJourneyDetails()) {
                            for (BrmsSupplierPassengerDetail aBrmsPassengerDetail : aBrmsSupplierJourneyDetail.getBRMSPassengerDetails()) {
                                String passengerType = aBrmsPassengerDetail.getPassengerType();
                                List<String> coomercialsApplied = aBrmsPassengerDetail.getCommercialsApplied();
                                if (coomercialsApplied != null && !coomercialsApplied.isEmpty()) {

                                    for (String appliedType : coomercialsApplied) {
                                        List<BrmsSupplierCommercialDetail> brmsSupplierCommercialList = aBrmsPassengerDetail.getCommercialDetails();
                                        for (BrmsSupplierCommercialDetail aSupplierCommercialDetail : brmsSupplierCommercialList) {
                                            if (aSupplierCommercialDetail.getCommercialName().equalsIgnoreCase(appliedType)) {
                                                //manage amount
                                                Double commercialAmount = aSupplierCommercialDetail.getCommercialAmount();
                                                JSONObject appliedCommercials = new JSONObject();
                                                appliedCommercials.put("paxType", passengerType);
                                                appliedCommercials.put("appliedType", appliedType);
                                                appliedCommercials.put("amount", commercialAmount);
                                                listOfCommercials.add(appliedCommercials);
                                            }
                                        }
                                    }
                                }
                            }
                            brmsSupplierTransactionalResponse.put(brmsSupplierId, listOfCommercials);
                        }
                    }
                }


                Map<String, Map<String, Double>> brmsTotalSupplierCommercials = new HashMap<>();
                Map<String, Double> appliedTypeMap = new HashMap<>();
                for (Map.Entry<String, List<JSONObject>> entry : brmsSupplierTransactionalResponse.entrySet()) {
                    Boolean flag = false;
                    String key = entry.getKey();
                    List<JSONObject> values = entry.getValue();
                    for (JSONObject value : values) {
                        String appliedType = value.getString("appliedType");
                        Double appliedAmount = appliedTypeMap.get(appliedType);
                        if (appliedAmount == null) {
                            appliedTypeMap.put(appliedType, value.getDouble("amount"));
                            flag = true;
                        } else {
                            appliedTypeMap.put(appliedType, appliedAmount + appliedTypeMap.get(appliedType));
                            flag = true;
                        }
                    }
                    if (flag)
                        brmsTotalSupplierCommercials.put(key, appliedTypeMap);
                }
                //bug //test TOdo
                Map<String, Map<String, Double>> opsTotalSupplierCommercials = new HashMap<>();
                for (OpsProduct opsProduct : opsBooking.getProducts()) {
                    if (opsProduct.getOpsProductSubCategory().getSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
                        String supplierType = onlineOrOfflineOrder(opsProduct);
                        Map<String, Double> map = new HashMap<>();
                        if ("online".equalsIgnoreCase(supplierType)) {
                            List<OpsOrderSupplierCommercial> opsOrderSupplierCommercialList = opsProduct.getOrderDetails().getSupplierCommercials();
                            for (OpsOrderSupplierCommercial opsOrderSupplierCommercial : opsOrderSupplierCommercialList) {
                                String commercialName = opsOrderSupplierCommercial.getCommercialName();
                                Double commercialAmount = Double.parseDouble(opsOrderSupplierCommercial.getCommercialAmount());
                                map.put(commercialName, commercialAmount);
                                opsTotalSupplierCommercials.put(opsProduct.getSupplierID(), map);
                            }
                        }
                    }
                }
                //bug //test TOdo
                qcValue = brmsTotalSupplierCommercials.entrySet().stream().filter(value -> opsTotalSupplierCommercials.entrySet().stream().anyMatch(value1 -> (value1.getKey() == value.getKey() && value1.getValue() == value.getValue()))).findAny().isPresent();
            } else
                throw new OperationException("Error while getting supplier commercial from Brsm");
        }
        return qcValue;
    }


    public Boolean qcCheckForOnlineAccoSupplierCommercial(OpsBooking opsBooking) throws JSONException, OperationException {

        //getting SI response in json
        JSONObject accoSupplierCommercialsResJson = consumeAccoSupplierCommercials(opsBooking);
        Boolean qcCheck = false;

        if (accoSupplierCommercialsResJson != null) {
            HttpEntity<String> requestEntity = qcUtilService.createHeader(accoSupplierCommercialsResJson.toString());
            ResponseEntity<String> supplierCommercial = null;
            //getting calculated values from BRMS
            try {
                supplierCommercial = RestUtils.exchange(supplierAcooCommercialBrmsUrl, HttpMethod.POST, requestEntity, String.class);
            } catch (Exception e) {
                logger.error("Error While Retreiving Supplier Commercial From BRMS " + e);
                e.printStackTrace();
            }
            //checking supplierCommercial null or not by taking result value
            JSONObject jsonObject = new JSONObject(supplierCommercial.getBody());
            String result = jsonObject.getString("result");


            if (!result.contains("null")) {
                String roomDetails = null;
                ObjectMapper mapper = new ObjectMapper();
                List<JsonNode> parentComercialList = new ArrayList<>();
                List<JsonNode> commercialsAppliedList = new ArrayList<>();
                String commonElementJson = null;

                try {
                    commonElementJson = mapper.readTree(supplierCommercial.getBody()).findPath("commonElements").toString();
                    roomDetails = mapper.readTree(supplierCommercial.getBody()).findPath("roomDetails").toString();
                    commercialsAppliedList = mapper.readTree(roomDetails).findValues("commercialsApplied");
                    parentComercialList = mapper.readTree(roomDetails).findValues("commercialDetails");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (commonElementJson != null && roomDetails != null && commercialsAppliedList.size() > 0 && parentComercialList.size() > 0) {
                    logger.error("found that for acco Supplier Commercials are not applied");


                    Map<CommonElementWiseInfo, Map<String, Double>> brmsCalculatedCommercialDetails =
                            this.getBrmsAccoCalculatedCommercialDetails(commonElementJson, parentComercialList, commercialsAppliedList);

                    Map<CommonElementWiseInfo, Map<String, Double>> opsrmsCalculatedCommercialDetails =
                            this.getOpsAccoOrderCommercialDetails(opsBooking);

                    qcCheck = brmsCalculatedCommercialDetails.entrySet().stream().filter(value -> opsrmsCalculatedCommercialDetails.entrySet().stream().anyMatch(value1 -> (value1.getKey() == value.getKey() && value1.getValue() == value.getValue()))).findAny().isPresent();
                }
            } else {
                logger.error("No Data Found While Getting Acco Supplier Commercials From Brms");
            }
        }
        return qcCheck;
    }

    private Map<CommonElementWiseInfo, Map<String, Double>> getBrmsAccoCalculatedCommercialDetails(String commonElementJson, List<JsonNode> parentComercialList, List<JsonNode> commercialsAppliedList) {
        Map<CommonElementWiseInfo, Map<String, Double>> filteredMap = new HashMap<>();
        BrmsSupplierCommonElements brmsSupplierCommonElements = (BrmsSupplierCommonElements) jsonObjectProvider.getChildObject(commonElementJson, "$", BrmsSupplierCommonElements.class);
        CommonElementWiseInfo commonElementWiseInfo = new CommonElementWiseInfo();
        CopyUtils.copy(brmsSupplierCommonElements, commonElementWiseInfo);
        //get the total from method also check common elements
        Map<String, Double> totalCommercial = new HashMap<>();
        List<BrmsSupplierCommercialDetail> list = jsonObjectProvider.getChildrenCollection(parentComercialList.toString(), "$.*.*", BrmsSupplierCommercialDetail.class);
        if (commercialsAppliedList.size() == list.size()) {
            for (BrmsSupplierCommercialDetail supplierCommercialDetail : list) {
                String commercialName = supplierCommercialDetail.getCommercialName();
                Double entityValue = totalCommercial.get(commercialName);
                if (entityValue == null) {
                    totalCommercial.put(commercialName, supplierCommercialDetail.getCommercialAmount());
                } else {
                    totalCommercial.put(commercialName, entityValue + supplierCommercialDetail.getCommercialAmount());
                }
            }
        }
        filteredMap.put(commonElementWiseInfo, totalCommercial);
        return filteredMap;
    }

    private Map<CommonElementWiseInfo, Map<String, Double>> getOpsAccoOrderCommercialDetails(OpsBooking opsBooking) {
        Map<CommonElementWiseInfo, Map<String, Double>> opsFilteredMap = new HashMap<>();
        String clientType = opsBooking.getClientType();
        for (OpsProduct opsProduct : opsBooking.getProducts()) {
            String opsProductSubCategory = opsProduct.getProductSubCategory();
            String opsSupplierId = opsProduct.getSupplierID();
            Map<String, Double> opsTotalCommercial = new HashMap<>();
            if (opsProduct.getOpsProductSubCategory().getSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {
                String supplierType = this.onlineOrOfflineOrder(opsProduct);
                if ("online".equalsIgnoreCase(supplierType)) {
                    List<OpsOrderSupplierCommercial> opsOrderSupplierCommercialList = opsProduct.getOrderDetails().getSupplierCommercials();
                    for (OpsOrderSupplierCommercial opsOrderSupplierCommercial : opsOrderSupplierCommercialList) {
                        opsTotalCommercial.put(opsOrderSupplierCommercial.getCommercialName(), Double.parseDouble(opsOrderSupplierCommercial.getCommercialAmount()));
                    }
                }
            }
            CommonElementWiseInfo commonElementWiseInfo = new CommonElementWiseInfo();
            commonElementWiseInfo.setSupplier(opsSupplierId);
            commonElementWiseInfo.setProductCategorySubType(opsProductSubCategory);
            commonElementWiseInfo.setClientType(clientType);
            opsFilteredMap.put(commonElementWiseInfo, opsTotalCommercial);
        }
        return opsFilteredMap;
    }

    private String onlineOrOfflineOrder(OpsProduct opsProduct) {
        return opsProduct.getOrderDetails().getSupplierType().getSupplierType();
    }

    private JSONObject consumeAirSupplierCommercials(OpsBooking opsBooking) {
        JSONObject jsonOfSupplierCommercials = null;
        try {
            jsonOfSupplierCommercials = bookingEngineElasticData.getJSONData(
                    opsBooking.getUserID(),
                    opsBooking.getTransactionID(),
                    opsBooking.getSessionID(),
                    BEInboundOperation.REPRICE,
                    BEOperationId.SUPPLIER_COMMERCIAL_RQ,
                    BEServiceUri.FLIGHT);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (OperationException e) {
            e.printStackTrace();
        }
        return jsonOfSupplierCommercials;
    }

    private JSONObject consumeAccoSupplierCommercials(OpsBooking opsBooking) {
        JSONObject jsonOfSupplierCommercials = null;
        try {
            jsonOfSupplierCommercials = bookingEngineElasticData.getJSONData(
                    opsBooking.getUserID(),
                    opsBooking.getTransactionID(),
                    opsBooking.getSessionID(),
                    BEInboundOperation.REPRICE,
                    BEOperationId.SUPPLIER_COMMERCIAL_RQ,
                    BEServiceUri.HOTEL);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (OperationException e) {
            e.printStackTrace();
        }
        return jsonOfSupplierCommercials;
    }
}
