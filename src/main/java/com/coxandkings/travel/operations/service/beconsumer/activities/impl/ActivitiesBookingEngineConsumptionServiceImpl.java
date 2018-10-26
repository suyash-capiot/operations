package com.coxandkings.travel.operations.service.beconsumer.activities.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.coxandkings.travel.operations.model.core.OpsAccommodationPaxInfo;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsActivitiesDetails;
import com.coxandkings.travel.operations.model.core.OpsOrderDetails;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.core.OpsRoom;
import com.coxandkings.travel.operations.service.beconsumer.acco.helper.AccoBookingEngineConsumptionHelper;
import com.coxandkings.travel.operations.service.beconsumer.activities.ActivitiesBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.beconsumer.activities.helper.ActivitiesBookingEngineConsumptionHelper;
import com.coxandkings.travel.operations.utils.RestUtils;
@Service
public class ActivitiesBookingEngineConsumptionServiceImpl implements ActivitiesBookingEngineConsumptionService{

	 @Autowired
	 private ActivitiesBookingEngineConsumptionHelper activitiesBookingEngineConsumptionHelper;
	 private RestTemplate restTemplate;
	 
	 @Value("${booking-engine-core-services.activity.search}")
	    private String activitiesSearchURL;
	 
	@Override
	public String search(OpsBooking opsBooking, OpsProduct opsProduct) {
	    JSONObject searchRequest = new JSONObject();
        JSONObject requestBody = new JSONObject();
        JSONObject requestHeader = null;
        try {
            requestHeader = activitiesBookingEngineConsumptionHelper.getRequestHeader(opsBooking);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {

            OpsOrderDetails orderDetails = opsProduct.getOrderDetails();
            OpsActivitiesDetails activitiesDetails = orderDetails.getActivityDetails();
//            activitiesDetails.get
            
            requestBody.put("countryCode", "IN");
            requestBody.put("cityCode", "G1011");
            
            JSONObject particiapntInfoObj = new JSONObject();
            particiapntInfoObj.put("adultCount", "");
            
            String[] childAges = new String[2];
            
            particiapntInfoObj.put("childAges", childAges);
            requestBody.put("participantInfo",particiapntInfoObj );
            
            searchRequest.put("requestHeader", requestHeader);
            searchRequest.put("requestBody", requestBody);
            restTemplate = RestUtils.getTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<>(searchRequest.toString(), headers);
            ResponseEntity<String> searchResponseEntity = null;

            searchResponseEntity =
                    restTemplate.exchange(this.activitiesSearchURL, HttpMethod.POST, httpEntity, String.class);

            return searchResponseEntity.getBody();

        } catch (HttpClientErrorException httpClientErrorException) {
            httpClientErrorException.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}

}
