package com.coxandkings.travel.operations.service.manageofflinebooking.manualofflinebooking.impl.acco;


import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;

@Service
public class ClientCommercials implements AccoConstants {
    private static final Logger logger = LogManager.getLogger(ClientCommercials.class);

    @Value("${offline-booking.acco.client-commercial}")
    private String strClientCommercialsUrl;

    @Value("${brms.username}")
    private String strUserName;

    @Value("${brms.password}")
    private String strPassword;


    public JSONObject getClientCommercials(JSONObject reqJson, JSONObject suppCommRes, JSONObject mdmDataJSon) {

        //ServiceConfig commTypeConfig = AccoConfig.getCommercialTypeConfig(CommercialsType.COMMERCIAL_CLIENT_TRANSACTIONAL);
        JSONObject breClientReqJson = new JSONObject(new JSONTokener("{\"lookup\" : \"clienttransactionalsession\",\"commands\" : [{\"insert\": {\"out-identifier\": \"BusinessRuleIntake\",\"object\": {\"cnk.acco_commercialscalculationengine.clienttransactionalrules.Root\": {\"transactional\":true}}}},{\"fire-all-rules\": {}}]}"));
        JSONObject breClientReqRoot = breClientReqJson.getJSONArray("commands").getJSONObject(0).getJSONObject("insert").getJSONObject("object").getJSONObject(JSON_PROP_CLIENTTRANRULES);

        JSONObject suppCommResRoot = suppCommRes.getJSONObject(JSON_PROP_RESULT).getJSONObject(JSON_PROP_EXECUTIONRES).getJSONArray(JSON_PROP_RESULTS).getJSONObject(0).getJSONObject(JSON_PROP_VALUE).getJSONObject(JSON_PROP_SUPPTRANRULES);
        JSONObject suppCommResHdr = suppCommResRoot.getJSONObject(JSON_PROP_HEADER);
        breClientReqRoot.put(JSON_PROP_HEADER, suppCommResHdr);

        //UserContext usrCtx = UserContext.getUserContextForSession(reqJson.getJSONObject(JSON_PROP_REQHEADER));

        JSONArray briJsonArr = new JSONArray();
        JSONArray suppCommResBRIArr = suppCommResRoot.getJSONArray(JSON_PROP_BUSSRULEINTAKE);

        //JSONArray entityDtlsJsonArr = new JSONArray();

        for (int i = 0; i < suppCommResBRIArr.length(); i++) {
            JSONObject currSuppCommResBRI = suppCommResBRIArr.getJSONObject(i);
            currSuppCommResBRI.remove("ruleFlowName");
            currSuppCommResBRI.remove("mdmruleId");

//			currSuppCommResBRI.put(JSON_PROP_ENTITYDETAILS,  usrCtx.getClientCommercialsHierarchy());
            if (mdmDataJSon.optJSONArray(JSON_PROP_ENTITYDETAILS) != null)
                currSuppCommResBRI.put(JSON_PROP_ENTITYDETAILS, mdmDataJSon.getJSONArray(JSON_PROP_ENTITYDETAILS)); //Operations Specific

            //Ops Amendments Block
            if (reqJson.getJSONObject(JSON_PROP_REQBODY).has("opsAmendments") && reqJson.getJSONObject(JSON_PROP_REQBODY).getJSONObject("opsAmendments")
                    .getString("actionItem").equalsIgnoreCase("amendEntityCommercials")) {
                JSONObject opsAmendmentJson = reqJson.getJSONObject("requestBody").getJSONObject("opsAmendments");
                if (currSuppCommResBRI.getJSONObject(JSON_PROP_COMMONELEMS).getString(JSON_PROP_SUPP).equals(opsAmendmentJson.getString("supplierId"))) {
                    JSONArray hotelDetailsJsonArr = currSuppCommResBRI.getJSONArray(JSON_PROP_HOTELDETAILS);
                    for (int hotelIdx = 0; hotelIdx < hotelDetailsJsonArr.length(); hotelIdx++) {
                        JSONObject hotelDetailsJson = hotelDetailsJsonArr.getJSONObject(hotelIdx);
                        if (hotelDetailsJson.getString("productName").equals(opsAmendmentJson.getString("hotelName"))) {
                            JSONArray roomDetailsJsonArr = hotelDetailsJson.getJSONArray(JSON_PROP_ROOMDETAILS);
                            for (int roomIdx = 0; roomIdx < roomDetailsJsonArr.length(); roomIdx++) {
                                JSONObject roomDetailsJson = roomDetailsJsonArr.getJSONObject(roomIdx);
                                if (roomDetailsJson.get("roomType").equals(opsAmendmentJson.getString(JSON_PROP_ROOMTYPENAME)) &&
                                        roomDetailsJson.get("roomCategory").equals(opsAmendmentJson.getString(JSON_PROP_ROOMCATEGNAME)) &&
                                        roomDetailsJson.get("rateType").equals(opsAmendmentJson.getString(JSON_PROP_RATEPLANNAME)) &&
                                        roomDetailsJson.get("rateCode").equals(opsAmendmentJson.getString(JSON_PROP_RATEPLANCODE))) {
                                    roomDetailsJson.put("bookingId", opsAmendmentJson.getString("bookingId"));
                                    roomDetailsJson.put("ineligibleCommercials", opsAmendmentJson.getJSONArray("ineligibleCommercials"));
                                }
                            }
                        }
                    }

                }
            }

            briJsonArr.put(currSuppCommResBRI);

        }
        breClientReqRoot.put(JSON_PROP_BUSSRULEINTAKE, briJsonArr);

        breClientReqRoot.put("businessRuleIntake", briJsonArr);
        JSONObject breClientResJson = null;
        try {
            //long start = System.currentTimeMillis();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<String>(breClientReqJson.toString(), headers);
            ResponseEntity<String> strResponse = RestUtils.postForEntity(strClientCommercialsUrl, httpEntity, String.class, new BasicAuthorizationInterceptor(strUserName, strPassword));
            breClientResJson = new JSONObject(strResponse.getBody());
            //logger.info(String.format("Time taken to get client commercials response : %s ms", System.currentTimeMillis()-start));
        } catch (Exception x) {
            logger.warn("An exception occurred when calling supplier commercials", x);
        }
        return breClientResJson;
    }
}
