package com.coxandkings.travel.operations.utils.taxengine;

import com.coxandkings.travel.operations.enums.prodreview.ClientType;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.taxengine.TaxEngineOperations;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class TaxEngineUtils {

    private Logger logger = LogManager.getLogger(TaxEngineUtils.class);

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Value("${mdm.common.client.b2b_client}")
    private String clientB2BUrl;

    @Value(value = "${tax_engine.clientType}")
    private String clientTypeUrl;

    @Value(value = "${tax_engine.clientLocation}")
    private String clientLocationUrl;

    @Value("${mdm.common.supplier.supplier-by-id}")
    private String suppByIdUrl;

    @Value("${brms.username}")
    private String brmsUserName;

    @Value("${brms.password}")
    private String brmsPassword;

    @Value("${tax_engine.base_url}")
    private String taxEngineUrl;

    @Value("${mdm.common.client.b2c_client}")
    private String clientB2CUrl;

    @Value("${mdm.company}")
    private String orgB2BUrl;

    @Value("${mdm.organization}market")
    private String mrktB2BUrl;

    public JSONObject getGSTDetails(String bookingNo, String orderID, Double amount, Boolean isAmendmentCharges) throws OperationException {

        JSONObject gstReqObj = new JSONObject();
        try {
            OpsBooking opsBooking = opsBookingService.getBooking(bookingNo);
            if (null != opsBooking) {
                gstReqObj.put("lookup", "taxcalculationsession");
                gstReqObj.put("commands", getCommandsArr(opsBooking, orderID, amount, isAmendmentCharges));
            }
        } catch (Exception e) {
            logger.error("Exception is : ", e);
            throw new OperationException(Constants.OPS_TE_ERR_001);
        }
        return getTaxEngineResJSON(gstReqObj);
    }

    private JSONArray getCommandsArr(OpsBooking opsBooking, String orderID, Double amount, Boolean isAmendmentCharges) throws OperationException {
        JSONArray commandsArr = new JSONArray();
        JSONObject fireAllRules = new JSONObject();
        try {
            JSONObject commandEle = new JSONObject();
            commandsArr.put(commandEle);

            fireAllRules.put("fire-all-rules", new JSONObject());
            commandsArr.put(fireAllRules);

            JSONObject insertEle = new JSONObject();
            commandEle.put("insert", insertEle);
            insertEle.put("out-identifier", "Response");

            JSONObject object = new JSONObject();
            insertEle.put("object", object);

            JSONObject root = new JSONObject();
            object.put("cnk.taxcalculation.Root", root);

            JSONObject header = new JSONObject();
            root.put("header", header);
            header.put("transactionID", UUID.randomUUID().toString());
            header.put("sessionID", UUID.randomUUID().toString());
            header.put("operationName", isAmendmentCharges ? TaxEngineOperations.AMEND.getOperName() : TaxEngineOperations.SEARCH.getOperName());
            header.put("userID", userService.getLoggedInUserId());

            root.put("gstCalculationIntake", getGstCalcIntake(opsBooking, orderID, amount, isAmendmentCharges));
        } catch (Exception e) {
            logger.error("Exception is : ", e);
            throw new OperationException(Constants.OPS_TE_ERR_002);
        }

        return commandsArr;
    }

    private JSONObject getGstCalcIntake(OpsBooking opsBooking, String orderID, Double amount, Boolean isAmendmentCharges) throws OperationException {
        JSONObject gstCalcIntake = new JSONObject();
        try {
            String cliDetStr = null, clientName = null, companyId = null, cliLocDet = null, compMrktId = null;

            if (opsBooking.getClientType().equalsIgnoreCase(ClientType.B2B.toString())) {
                cliDetStr = mdmRestUtils.getResponseJSON(clientB2BUrl + opsBooking.getClientID());
                cliLocDet = mdmRestUtils.getResponseJSON(clientLocationUrl + jsonObjectProvider.getAttributeValue(cliDetStr, "$.locationDetails[0]", String.class));

                gstCalcIntake.put("country", jsonObjectProvider.getAttributeValue(cliLocDet, "$.addressDetails.country", String.class));
                gstCalcIntake.put("state", jsonObjectProvider.getAttributeValue(cliLocDet, "$.addressDetails.state", String.class));
                gstCalcIntake.put("clientCategory", jsonObjectProvider.getAttributeValue(cliDetStr, "$.clientProfile.clientDetails.clientCategory", String.class));
                gstCalcIntake.put("clientSubCategory", jsonObjectProvider.getAttributeValue(cliDetStr, "$.clientProfile.clientDetails.clientSubCategory", String.class));

                clientName = jsonObjectProvider.getAttributeValue(cliDetStr, "$.clientProfile.clientDetails.clientName", String.class);

                companyId = jsonObjectProvider.getAttributeValue(cliDetStr, "$.clientProfile.orgHierarchy.companyId", String.class);
                JSONObject companyFilter = new JSONObject();
                companyFilter.put("_id", companyId);
                String compDetails = mdmRestUtils.exchange(UriComponentsBuilder.fromUriString(orgB2BUrl + "?filter=" + companyFilter.toString()).build().encode().toUri(), HttpMethod.GET, String.class);
                gstCalcIntake.put("companyName", new JSONObject(compDetails).getJSONArray("data").getJSONObject(0).getString("name"));

                compMrktId = jsonObjectProvider.getAttributeValue(cliDetStr, "$.clientProfile.orgHierarchy.companyMarkets[0].marketId", String.class);
                JSONObject mrktFilter = new JSONObject();
                mrktFilter.put("_id", compMrktId);
                String mrktDetails = mdmRestUtils.exchange(UriComponentsBuilder.fromUriString(mrktB2BUrl + "?filter=" + mrktFilter.toString()).build().encode().toUri(), HttpMethod.GET, String.class);
                gstCalcIntake.put("companyMarket", new JSONObject(mrktDetails).getJSONArray("data").getJSONObject(0).getString("name"));

            } else if (opsBooking.getClientType().equalsIgnoreCase(ClientType.B2C.toString())) {
                JSONObject jsonFilter = new JSONObject();
                jsonFilter.put("clientDetails.clientId", opsBooking.getClientID());
                cliDetStr = mdmRestUtils.getResponseJSON(clientB2CUrl + "?filter=" + jsonFilter.toString());
                gstCalcIntake.put("country", jsonObjectProvider.getAttributeValue(cliDetStr, "$.data[0].corporateAddress.country", String.class));
                gstCalcIntake.put("state", jsonObjectProvider.getAttributeValue(cliDetStr, "$.data[0].corporateAddress.state", String.class));
                gstCalcIntake.put("clientCategory", jsonObjectProvider.getAttributeValue(cliDetStr, "$.data[0].clientDetails.clientCategory", String.class));
                clientName = jsonObjectProvider.getAttributeValue(cliDetStr, "$.data[0].clientDetails.clientName", String.class);

                //INFO: Below details are not available in MDM for B2C
                gstCalcIntake.put("clientSubCategory", "");
                gstCalcIntake.put("companyName", "");
                gstCalcIntake.put("companyMarket", "");
            }

            gstCalcIntake.put("clientType", opsBooking.getClientType());
            gstCalcIntake.put("validity", new SimpleDateFormat(Constants.TE_DATE_FORMAT).format(Date.from(opsBooking.getBookingDateZDT().toInstant())));
            gstCalcIntake.put("supplierPricingDetails", getSuppPriDetArr(opsBooking, orderID, clientName, amount, isAmendmentCharges));
        } catch (Exception e) {
            logger.error("Exception is : ", e);
            throw new OperationException(Constants.OPS_TE_ERR_003);
        }
        return gstCalcIntake;
    }

    private JSONArray getSuppPriDetArr(OpsBooking opsBooking, String orderID, String clientName, Double amount, Boolean isAmendmentCharges) throws OperationException {
        JSONArray suppPriDetArr = new JSONArray();
        JSONObject suppPriDetEle = null;
        String suppDetailsStr = null;

        try {
            OpsProduct opsProduct = opsBooking.getProducts().stream().filter(obj -> obj.getOrderID().equalsIgnoreCase(orderID)).findFirst().orElse(null);
            suppPriDetEle = new JSONObject();
            suppPriDetArr.put(suppPriDetEle);

            suppDetailsStr = mdmRestUtils.getResponseJSON(suppByIdUrl + opsProduct.getSupplierID());
            suppPriDetEle.put("supplierName", jsonObjectProvider.getAttributeValue(suppDetailsStr, "$.supplier.name", String.class));
            suppPriDetEle.put("clientName", jsonObjectProvider.getAttributeValue(clientName, "$.clientProfile.clientDetails.clientName", String.class));
            suppPriDetEle.put("isThirdParty", false);
            suppPriDetEle.put("rateFor", opsProduct.getProductSubCategory());
            suppPriDetEle.put("lor", "");// TODO: Need to find logic to get value
            suppPriDetEle.put("pos", "");// TODO: Need to find logic to get value
            suppPriDetEle.put("travelDetails", getTravelDetailsArr(opsProduct, amount, isAmendmentCharges));
        } catch (Exception e) {
            logger.error("Exception is : ", e);
            throw new OperationException(Constants.OPS_TE_ERR_004);
        }
        return suppPriDetArr;
    }

    private JSONArray getTravelDetailsArr(OpsProduct opsProduct, Double amount, Boolean isAmendmentCharges) throws OperationException {
        JSONArray travelDetailsArr = new JSONArray(), fareDetailsArr = null;
        JSONObject travelDetEle = new JSONObject(), fareDetEle = null;

        try {
            travelDetailsArr.put(travelDetEle);

            if (opsProduct.getOpsProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION)) {
                /**
                 *  INFO: Below details are not available in BE AIR booking response.
                 */
                travelDetEle.put("travellingCountry", "");
                travelDetEle.put("travellingState", "");
                travelDetEle.put("travellingCity", "");

                fareDetailsArr = new JSONArray();
                travelDetEle.put("fareDetails", fareDetailsArr);
                fareDetEle = new JSONObject();
                fareDetailsArr.put(fareDetEle);

                OpsFlightDetails opsFlightDetails = opsProduct.getOrderDetails().getFlightDetails();

                //Added as is was giving null pointer exception
                Double taxAmount = opsFlightDetails.getTotalPriceInfo().getTaxes().getAmount()== null? 0: opsFlightDetails.getTotalPriceInfo().getTaxes().getAmount();
                Double feeAmount = opsFlightDetails.getTotalPriceInfo().getFees().getTotal() == null ? 0 : opsFlightDetails.getTotalPriceInfo().getFees().getTotal();

                fareDetEle.put("totalFare", opsFlightDetails.getTotalPriceInfo().getBaseFare().getAmount() + feeAmount + taxAmount);
                fareDetEle.put("fareBreakUp", getAirFareBreakUpArr(opsFlightDetails.getTotalPriceInfo()));

                fareDetEle.put("isMarkedUp", false);//INFO: Need logic to find value
                fareDetEle.put("additionalCharges", "");
                fareDetEle.put("sellingPrice", opsFlightDetails.getTotalPriceInfo().getTotalPrice());
                fareDetEle.put("product", opsProduct.getProductCategory());
                fareDetEle.put("subType", opsProduct.getProductSubCategory());
                fareDetEle.put("starCategory", 5);//TODO: Need logic to find value
                fareDetEle.put("cabinClass", "");
                fareDetEle.put("flightType", "");
                fareDetEle.put("amendmentCharges", isAmendmentCharges ? amount : 0);
                fareDetEle.put("cancellationCharges", 0);// TODO: Need to check whether it's company/supplier cancellation charges
                fareDetEle.put("mealPlan", "");//TODO: Need logic to find value
                fareDetEle.put("supplierRateType", "");//TODO: Need logic to find value
                fareDetEle.put("supplierRateCode", "");//TODO: Need logic to find value
                fareDetEle.put("isPackage", false);//TODO: Need logic to find value

            } else if (opsProduct.getOpsProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION)) {
                travelDetEle.put("travellingCountry", opsProduct.getOrderDetails().getHotelDetails().getCountryName());
                travelDetEle.put("travellingState", "");
                travelDetEle.put("travellingCity", opsProduct.getOrderDetails().getHotelDetails().getCityName());

                fareDetailsArr = new JSONArray();
                travelDetEle.put("fareDetails", fareDetailsArr);

                for (OpsRoom opsRoom : opsProduct.getOrderDetails().getHotelDetails().getRooms()) {
                    fareDetEle = new JSONObject();
                    fareDetailsArr.put(fareDetEle);

                    if (opsRoom.getRoomTotalPriceInfo() == null) {
                        if (opsRoom.getRoomTotalPriceInfo().getReceivables() != null && !opsRoom.getRoomTotalPriceInfo().getReceivables().getAmount().equals("0")) {
                            fareDetEle.put("totalFare", opsRoom.getRoomTotalPriceInfo().getReceivables().getAmount());
                        }
                    } else if (opsRoom.getRoomTotalPriceInfo().getReceivables() == null) {
                        if (opsRoom.getRoomTotalPriceInfo() != null && !opsRoom.getRoomTotalPriceInfo().getRoomTotalPrice().equals("0")) {
                            fareDetEle.put("totalFare", opsRoom.getRoomTotalPriceInfo().getRoomTotalPrice());
                        }
                    } else {
                        if (opsRoom.getRoomTotalPriceInfo().getReceivables().getAmount() != null) {
                            fareDetEle.put("totalFare", Double.parseDouble(opsRoom.getRoomTotalPriceInfo().getRoomTotalPrice()) - opsRoom.getRoomTotalPriceInfo().getReceivables().getAmount());
                        } else {
                            fareDetEle.put("totalFare", Double.parseDouble(opsRoom.getRoomTotalPriceInfo().getRoomTotalPrice()));
                        }
                    }

                    fareDetEle.put("fareBreakUp", getAccoFareBreakUpArr(opsRoom));
                    fareDetEle.put("isMarkedUp", false);//INFO: Need logic to find value
                    fareDetEle.put("additionalCharges", "");
                    fareDetEle.put("sellingPrice", opsRoom.getRoomTotalPriceInfo().getRoomTotalPrice());
                    fareDetEle.put("product", opsProduct.getProductCategory());
                    fareDetEle.put("subType", opsProduct.getProductSubCategory());
                    fareDetEle.put("starCategory", 5);//TODO: Need logic to find value
                    fareDetEle.put("cabinClass", "");
                    fareDetEle.put("flightType", "");
                    fareDetEle.put("amendmentCharges", isAmendmentCharges ? amount : 0);
                    fareDetEle.put("cancellationCharges", 0);// TODO: Need to check whether it's company/supplier cancellation charges
                    fareDetEle.put("mealPlan", opsRoom.getMealInfo().getMealID());
                    fareDetEle.put("supplierRateType", "");//TODO: Need logic to find value
                    fareDetEle.put("supplierRateCode", "");//TODO: Need logic to find value
                    fareDetEle.put("isPackage", false);//TODO: Need logic to find value
                }
            }
        } catch (Exception e) {
            logger.error("Exception is : ", e);
            throw new OperationException(Constants.OPS_TE_ERR_005);
        }
        return travelDetailsArr;
    }

    private JSONArray getAccoFareBreakUpArr(OpsRoom opsRoom) throws OperationException {
        JSONArray fareBreakUpArr = new JSONArray();
        JSONObject fareBreakUpEle = new JSONObject();

        try {
            fareBreakUpEle.put("name", "Base");
            if (opsRoom.getRoomTotalPriceInfo() == null) {
                if (opsRoom.getRoomTotalPriceInfo().getOpsTaxes() != null && !opsRoom.getRoomTotalPriceInfo().getOpsTaxes().getAmount().equals("0")) {
                    fareBreakUpEle.put("value", opsRoom.getRoomTotalPriceInfo().getOpsTaxes().getAmount());
                }
            } else if (opsRoom.getRoomTotalPriceInfo().getOpsTaxes() == null) {
                if (opsRoom.getRoomTotalPriceInfo() != null && !opsRoom.getRoomTotalPriceInfo().getRoomTotalPrice().equals("0")) {
                    fareBreakUpEle.put("value", opsRoom.getRoomTotalPriceInfo().getRoomTotalPrice());
                }
            } else {
                if (opsRoom.getRoomTotalPriceInfo().getOpsTaxes().getAmount() != null) {
                    fareBreakUpEle.put("value", Double.parseDouble(opsRoom.getRoomTotalPriceInfo().getRoomTotalPrice()) - opsRoom.getRoomTotalPriceInfo().getOpsTaxes().getAmount());
                } else {
                    fareBreakUpEle.put("value", Double.parseDouble(opsRoom.getRoomTotalPriceInfo().getRoomTotalPrice()));
                }
            }

            fareBreakUpArr.put(fareBreakUpEle);

            // INFO: Adding individual taxes
            for (OpsTax opsTax : opsRoom.getRoomTotalPriceInfo().getOpsTaxes().getTax()) {
                fareBreakUpEle = new JSONObject();
                fareBreakUpEle.put("name", opsTax.getTaxCode());
                fareBreakUpEle.put("value", opsTax.getAmount());
                fareBreakUpArr.put(fareBreakUpEle);
            }
        } catch (Exception e) {
            logger.error("Exception is : ", e);
            throw new OperationException(Constants.OPS_TE_ERR_006);
        }
        return fareBreakUpArr;
    }

    private JSONArray getAirFareBreakUpArr(OpsFlightTotalPriceInfo totalPriceInfo) throws OperationException {
        JSONArray fareBreakUpArr = new JSONArray();
        JSONObject fareBreakUpEle = new JSONObject();

        try {
            // INFO: Adding base price
            fareBreakUpEle.put("name", "Base");
            fareBreakUpEle.put("value", totalPriceInfo.getBaseFare().getAmount());
            fareBreakUpArr.put(fareBreakUpEle);

            // INFO: Adding individual fee
            for (OpsFee opsFee : totalPriceInfo.getFees().getFee()) {
                fareBreakUpEle = new JSONObject();
                fareBreakUpEle.put("name", opsFee.getFeeCode());
                fareBreakUpEle.put("value", opsFee.getAmount());
                fareBreakUpArr.put(fareBreakUpEle);
            }

            // INFO: Adding individual receivables
            if(totalPriceInfo.getReceivables() != null) {
                for (OpsReceivable opsReceivable : totalPriceInfo.getReceivables().getReceivable()) {
                    fareBreakUpEle = new JSONObject();
                    fareBreakUpEle.put("name", opsReceivable.getCode());
                    fareBreakUpEle.put("value", opsReceivable.getAmount());
                    fareBreakUpArr.put(fareBreakUpEle);
                }
            }

            // INFO: Adding individual taxes
            if(totalPriceInfo.getTaxes().getTax().size() > 0) {
                for (OpsTax opsTax : totalPriceInfo.getTaxes().getTax()) {
                    fareBreakUpEle = new JSONObject();
                    fareBreakUpEle.put("name", opsTax.getTaxCode());
                    fareBreakUpEle.put("value", opsTax.getAmount());
                    fareBreakUpArr.put(fareBreakUpEle);
                }
            }
        } catch (Exception e) {
            logger.error("Exception is : ", e);
            throw new OperationException(Constants.OPS_TE_ERR_007);
        }
        return fareBreakUpArr;
    }

    private JSONObject getTaxEngineResJSON(JSONObject taxEngineRQJSON) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(taxEngineUrl);

        String plainCreds = brmsUserName + ":" + brmsPassword;
        String base64Creds = new String(Base64.encodeBase64(plainCreds.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<String>(taxEngineRQJSON.toString(), headers);
        return new JSONObject(RestUtils.getTemplate().postForObject(uriComponentsBuilder.build().encode().toUri(), httpEntity, String.class));
    }
}