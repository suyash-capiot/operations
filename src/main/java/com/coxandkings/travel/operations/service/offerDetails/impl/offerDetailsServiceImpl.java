package com.coxandkings.travel.operations.service.offerDetails.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.offerDetails.OfferDetailsService;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.coxandkings.travel.operations.utils.manageOfflineBooking.MDMDataUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class offerDetailsServiceImpl implements OfferDetailsService{

    private static Logger logger = LogManager.getLogger(offerDetailsServiceImpl.class);

    @Value("${offers.voucherDetails}")
    private String voucherDtlsUrl;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Override
    public JSONArray getVoucherCodeDetails(String offersCode)throws OperationException {

        try{
            JSONObject reqJson = new JSONObject();
            reqJson.put("offerCode",offersCode);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<String>(reqJson.toString(), headers);
            ResponseEntity<String> strResponse = RestUtils.postForEntity(voucherDtlsUrl, httpEntity, String.class);
            JSONArray voucherDetails = new JSONArray();
            if(strResponse!=null){
                voucherDetails = new JSONArray(strResponse.getBody());
            }
            return voucherDetails;
        } catch (Exception e){
            logger.info("Exception occurred while fetching voucher details");
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred while fetching voucher details" + e.getMessage()));
            throw new OperationException(entity);
        }
    }
}
