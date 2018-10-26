package com.coxandkings.travel.operations.service.manageofflinebooking.manualofflinebooking.impl.acco;


import com.coxandkings.travel.operations.enums.manageofflinebooking.CommercialsOperation;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.coxandkings.travel.operations.utils.manageOfflineBooking.MDMDataUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SupplierCommercials implements AccoConstants {

    private static final Logger logger = LogManager.getLogger(SupplierCommercials.class);

    @Value("${offline-booking.acco.supplier-commercial}")
    private String strSupplierCommercialsUrl;

    @Value("${brms.username}")
    private String strUserName;

    @Value("${brms.password}")
    private String strPassword;

    @Autowired
    private MDMDataUtils dataUtils;

    public JSONObject getSupplierCommercials(CommercialsOperation op, JSONObject req, JSONObject res, Map<Integer, String> SI2BRMSRoomMap, JSONObject mdmDataJSon) throws OperationException {
        //ServiceConfig commTypeConfig = AccoConfig.getCommercialTypeConfig(CommercialsType.COMMERCIAL_SUPPLIER_TRANSACTIONAL);
        JSONObject breSuppReqJson = new JSONObject("{\"lookup\": \"suppliertransactionalsession\",\"commands\": [{\"insert\": {\"out-identifier\": \"BusinessRuleIntake\",\"object\": {\"cnk.acco_commercialscalculationengine.suppliertransactionalrules.Root\": {\"transactional\":true}}}},{\"fire-all-rules\": {}}]}");

        JSONObject reqHeader = req.getJSONObject(JSON_PROP_REQHEADER);
        JSONObject reqBody = req.getJSONObject(JSON_PROP_REQBODY);
        JSONObject clntCtx = reqHeader.getJSONObject(JSON_PROP_CLIENTCONTEXT);

        JSONObject resHeader = res.getJSONObject(JSON_PROP_RESHEADER);
        JSONObject resBody = res.getJSONObject(JSON_PROP_RESBODY);

        JSONObject breHdrJson = new JSONObject();
        breHdrJson.put(JSON_PROP_SESSIONID, resHeader.getString(JSON_PROP_SESSIONID));
        breHdrJson.put(JSON_PROP_TRANSACTID, resHeader.getString(JSON_PROP_TRANSACTID));
        breHdrJson.put(JSON_PROP_USERID, resHeader.getString(JSON_PROP_USERID));
        breHdrJson.put(JSON_PROP_OPERATIONNAME, op.toString());

        JSONObject rootJson = breSuppReqJson.getJSONArray("commands").getJSONObject(0).getJSONObject("insert")
                .getJSONObject("object")
                .getJSONObject("cnk.acco_commercialscalculationengine.suppliertransactionalrules.Root");

        JSONArray briJsonArr = new JSONArray();
        rootJson.put(JSON_PROP_HEADER, breHdrJson);
        rootJson.put(JSON_PROP_BUSSRULEINTAKE, briJsonArr);

        int briIndex = 0, hotelIndex = 0;
        String prevSuppId, suppId, cityName, hotelCode;
        JSONObject briJson = null, commonElemsJson, advDefnJson, hotelDetailsJson, roomDetailsJson, subReqBody, accoInfoJson;
        Map<String, Object> cityAttrs;
        Map<String, Object> hotelAttrs;
        Map<String, JSONObject> hotelMap = null;
        Map<String, Integer> hotelIndexMap = null;
        JSONArray multiResArr = resBody.getJSONArray(JSON_PROP_ACCOMODATIONARR);
        int roomIdx = 0;

        for (int i = 0; i < multiResArr.length(); i++) {

            accoInfoJson = (JSONObject) multiResArr.get(i);
            subReqBody = reqBody.has(JSON_PROP_ACCOMODATIONARR) ? (JSONObject) reqBody.getJSONArray(JSON_PROP_ACCOMODATIONARR).get(i) : reqBody;
            //briIndex = briJsonArr.length();
            prevSuppId = DEF_SUPPID;

            for (Object roomStayJson : accoInfoJson.getJSONArray(JSON_PROP_ROOMSTAYARR)) {

                suppId = ((JSONObject) roomStayJson).getString(JSON_PROP_SUPPREF);
                hotelCode = ((JSONObject) ((JSONObject) ((JSONObject) roomStayJson).get(JSON_PROP_ROOMINFO)).get(JSON_PROP_HOTELINFO)).getString(JSON_PROP_HOTELCODE);

                if (!prevSuppId.equals(suppId)) {

                    prevSuppId = suppId;
                    hotelMap = new HashMap<String, JSONObject>();
                    hotelIndexMap = new HashMap<String, Integer>();
                    hotelIndex = 0;
                    briJson = new JSONObject();

                    commonElemsJson = new JSONObject();
                    commonElemsJson.put(JSON_PROP_SUPP, suppId);
                    // TODO: Supplier market is hard-coded below. Where will this come from?
                    commonElemsJson.put(JSON_PROP_SUPPMARKET, clntCtx.getString(JSON_PROP_CLIENTMARKET));
                    commonElemsJson.put("contractValidity", DATE_FORMAT.format(new Date()));
                    //TODO:get from enum
                    commonElemsJson.put(JSON_PROP_PRODCATEGSUBTYPE, ((JSONObject) roomStayJson).getString(JSON_PROP_ACCOSUBTYPE));
                    // TODO: Hard-coded value. This should be set from client context.Check if
                    // required in acco or no
                    commonElemsJson.put(JSON_PROP_CLIENTTYPE, clntCtx.getString(JSON_PROP_CLIENTTYPE));
                    // TODO: Properties for clientGroup, clientName, iatanumber are not yet set. Are
                    // these required for B2C? What will be BRMS behavior if these properties are  not sent.
                    if (mdmDataJSon.optJSONObject(MDM_PROP_ORGHIERARCHY) != null) {
                        JSONObject orgHierarchy = mdmDataJSon.getJSONObject(MDM_PROP_ORGHIERARCHY);
                        commonElemsJson.put("companyId", orgHierarchy.optString("company"));
                    }
                    advDefnJson = new JSONObject();
                    advDefnJson.put("travelCheckInDate", subReqBody.getString(JSON_PROP_CHKIN).concat("T00:00:00"));
                    advDefnJson.put("travelCheckOutDate", subReqBody.getString(JSON_PROP_CHKOUT).concat("T00:00:00"));
                    // TODO: Significance of this data in acco.Is it same as the check in date or
                    // date of search
                    advDefnJson.put("salesDate", DATE_FORMAT.format(new Date()));
                    // TODO: is this the default value for all.Which other values are supported and
                    // where will it come from
                    advDefnJson.put("bookingType", "Online");
                    // TODO: Is this the same as client type.If yes, set from client context
                    advDefnJson.put(JSON_PROP_CREDSNAME, reqHeader.getJSONObject(JSON_PROP_CLIENTCONTEXT).getString("clientType"));
                    // TODO: Is this expected from req? This data is also needed by some supplier?
                    // Will it be provided by wem
                    advDefnJson.put(JSON_PROP_NATIONALITY, "Indian");
                    // TODO: city country continent and state mapping should be there iin adv def

                    //TODO:is empty default requied?data should be present in redis
                    cityName = reqBody.getString("city");//RedisHotelData.getHotelInfo(hotelCode, JSON_PROP_CITY);//Operations Specific
                    cityAttrs = dataUtils.getCityInfo(cityName);////Operations Specific
                    advDefnJson.put(JSON_PROP_CONTINENT, cityAttrs.getOrDefault(JSON_PROP_CONTINENT, ""));
                    advDefnJson.put(JSON_PROP_COUNTRY, cityAttrs.getOrDefault(JSON_PROP_COUNTRY, ""));
                    advDefnJson.put(JSON_PROP_CITY, cityName);
                    advDefnJson.put(JSON_PROP_STATE, cityAttrs.getOrDefault(JSON_PROP_STATE, ""));

                    briJson.put(JSON_PROP_COMMONELEMS, commonElemsJson);
                    briJson.put(JSON_PROP_ADVANCEDDEF, advDefnJson);

                    briJsonArr.put(briIndex++, briJson);
                }

                hotelDetailsJson = hotelMap.get(hotelCode);
                if (hotelDetailsJson == null) {

                    hotelDetailsJson = new JSONObject();

                    hotelAttrs = reqBody.getJSONObject("hotelInfo").toMap();  ////Operations Specific
                    hotelDetailsJson.put("productName", hotelAttrs.getOrDefault("name", ""));
                    hotelDetailsJson.put("productBrand", hotelAttrs.getOrDefault("brand", ""));
                    hotelDetailsJson.put("productChain", hotelAttrs.getOrDefault("chain", ""));

                    briJson.append(JSON_PROP_HOTELDETAILS, hotelDetailsJson);
                    hotelMap.put(hotelCode, hotelDetailsJson);
                    hotelIndexMap.put(hotelCode, hotelIndex++);
                }

                roomDetailsJson = getBRMSRoomDetailsJSON((JSONObject) roomStayJson);
                hotelDetailsJson.append(JSON_PROP_ROOMDETAILS, roomDetailsJson);
                //this is done so that while calculating prices from client response finding a particular room becomes efficient
                SI2BRMSRoomMap.put(roomIdx++, String.format("%s%c%s%c%s", briIndex - 1, KEYSEPARATOR, hotelIndexMap.get(hotelCode), KEYSEPARATOR, hotelDetailsJson.getJSONArray("roomDetails").length() - 1));
            }
        }

        // Ops Amendments Block
        if (reqBody.has("opsAmendments") && reqBody.getJSONObject("opsAmendments").getString("actionItem")
                .equalsIgnoreCase("amendSupplierCommercials")) {
            JSONObject opsAmendmentJson = reqBody.getJSONObject("opsAmendments");
            for (int briIdx = 0; briIdx < briJsonArr.length(); briIdx++) {
                JSONObject bri = briJsonArr.getJSONObject(briIdx);
                if (bri.getJSONObject(JSON_PROP_COMMONELEMS).getString(JSON_PROP_SUPP)
                        .equals(opsAmendmentJson.getString("supplierId"))) {
                    JSONArray hotelDetailsJsonArr = bri.getJSONArray(JSON_PROP_HOTELDETAILS);
                    for (int hotelIdx = 0; hotelIdx < hotelDetailsJsonArr.length(); hotelIdx++) {
                        JSONObject hotelDetails = hotelDetailsJsonArr.getJSONObject(hotelIdx);
                        if (hotelDetails.getString("productName").equals(opsAmendmentJson.getString("hotelName"))) {
                            JSONArray roomDetailsJsonArr = hotelDetails.getJSONArray(JSON_PROP_ROOMDETAILS);
                            for (int Idx = 0; Idx < roomDetailsJsonArr.length(); Idx++) {
                                JSONObject roomDetails = roomDetailsJsonArr.getJSONObject(Idx);
                                if (roomDetails.get("roomType")
                                        .equals(opsAmendmentJson.getString(JSON_PROP_ROOMTYPENAME))
                                        && roomDetails.get("roomCategory")
                                        .equals(opsAmendmentJson.getString(JSON_PROP_ROOMCATEGNAME))
                                        && roomDetails.get("rateType")
                                        .equals(opsAmendmentJson.getString(JSON_PROP_RATEPLANNAME))
                                        && roomDetails.get("rateCode")
                                        .equals(opsAmendmentJson.getString(JSON_PROP_RATEPLANCODE))) {
                                    roomDetails.put("bookingId", opsAmendmentJson.getString("bookingId"));
                                    roomDetails.put("ineligibleCommercials", opsAmendmentJson.getJSONArray("ineligibleCommercials"));
                                }
                            }
                        }
                    }

                }
            }
        }


        JSONObject breSuppResJson = null;
        try {
            //long start = System.currentTimeMillis();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<String>(breSuppReqJson.toString(), headers);
            ResponseEntity<String> strResponse = RestUtils.postForEntity(strSupplierCommercialsUrl, httpEntity, String.class, new BasicAuthorizationInterceptor(strUserName, strPassword));
            breSuppResJson = new JSONObject(strResponse.getBody());
            //logger.info(String.format("Time taken to get supplier commercials response : %s ms", System.currentTimeMillis()-start));
        } catch (Exception x) {
            logger.warn("An exception occurred when calling supplier commercials", x);
        }

        return breSuppResJson;
    }

    public JSONObject getBRMSRoomDetailsJSON(JSONObject roomStayJson) {

        JSONObject roomDetailsJson = new JSONObject();
        JSONObject roomInfoJson = roomStayJson.getJSONObject(JSON_PROP_ROOMINFO);

        JSONObject roomTypeJson = roomInfoJson.getJSONObject(JSON_PROP_ROOMTYPEINFO);
        //TODO:should codes come here
        roomDetailsJson.put("roomType", roomTypeJson.get(JSON_PROP_ROOMTYPENAME));
        //TODO:this data is taken from si as it is not found in mat's system.Ask mdm if room name has single mapping to room category
        //If yes take from mdm
        roomDetailsJson.put("roomCategory", roomTypeJson.get(JSON_PROP_ROOMCATEGNAME));

        JSONObject ratePlanJson = roomInfoJson.getJSONObject(JSON_PROP_RATEPLANINFO);
        //TODO:Will SI lookup this?If not,where this data will come from
        roomDetailsJson.put("rateType", ratePlanJson.get(JSON_PROP_RATEPLANNAME));
        roomDetailsJson.put("rateCode", ratePlanJson.get(JSON_PROP_RATEPLANCODE));
        //TODO:This hack is rght?
        //roomDetailsJson.put("bookingEngineKey", roomIndex);

        // TODO:Yet to add passenger type.If passenger type is taken from req, how to handle multiroom
        // case? ask SI to map in response

        JSONObject totalPriceJson = (JSONObject) roomStayJson.get(JSON_PROP_ROOMPRICE);
        BigDecimal totalPrice = totalPriceJson.getBigDecimal(JSON_PROP_AMOUNT);
        roomDetailsJson.put("totalFare", String.valueOf(totalPrice));

        JSONObject taxesJson = totalPriceJson.optJSONObject(JSON_PROP_TOTALTAX);
        //farebreakup will not be a part of req if null
        if (taxesJson != null && taxesJson.has(JSON_PROP_AMOUNT)) {
            JSONObject fareBreakupJson = new JSONObject();
            fareBreakupJson.put("baseFare", totalPrice.subtract(taxesJson.getBigDecimal(JSON_PROP_AMOUNT)));
            JSONArray taxArr = taxesJson.optJSONArray(JSON_PROP_TAXBRKPARR);
            if (taxArr != null) {
                JSONArray taxDetailsArr = new JSONArray();
                JSONObject taxDetailJson;
                for (Object tax : taxArr) {
                    taxDetailJson = new JSONObject();
                    //TODO:Will SI standardized these codes
                    taxDetailJson.put("taxName", ((JSONObject) tax).getString(JSON_PROP_TAXCODE));
                    taxDetailJson.put("taxValue", ((JSONObject) tax).getBigDecimal(JSON_PROP_AMOUNT));
                    taxDetailsArr.put(taxDetailJson);
                }
                fareBreakupJson.put("taxDetails", taxDetailsArr);
            }
            roomDetailsJson.put("fareBreakUp", fareBreakupJson);
        }

        return roomDetailsJson;

    }

}
