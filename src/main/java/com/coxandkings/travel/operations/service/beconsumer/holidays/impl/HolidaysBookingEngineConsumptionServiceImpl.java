package com.coxandkings.travel.operations.service.beconsumer.holidays.impl;

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

import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsHolidaysDetails;
import com.coxandkings.travel.operations.model.core.OpsOrderDetails;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.service.beconsumer.holidays.HolidaysBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.beconsumer.holidays.helper.HolidaysBookingEngineConsumptionServiceHelper;
import com.coxandkings.travel.operations.utils.RestUtils;
@Service
public class HolidaysBookingEngineConsumptionServiceImpl implements HolidaysBookingEngineConsumptionService{

	
    @Autowired
    private HolidaysBookingEngineConsumptionServiceHelper holidaysBookingEngineConsumptionServiceHelper;
    
    
    @Value("${booking-engine-core-services.holidays.search}")
    private String holidaysSearchURL;
    
    private RestTemplate restTemplate;
    
    
	@Override
	public String search(OpsBooking opsBooking, OpsProduct opsProduct) {

        JSONObject searchRequest = new JSONObject();
        JSONObject requestBody = new JSONObject();
        JSONObject requestHeader = null;
        try {
            requestHeader = holidaysBookingEngineConsumptionServiceHelper.getRequestHeader(opsBooking);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {

            OpsOrderDetails orderDetails = opsProduct.getOrderDetails();
            OpsHolidaysDetails holidaysDetails = orderDetails.getPackageDetails();
            
            requestBody.put("destinationLocation", "LON");
            requestBody.put("brandName", "COSMOS");
            requestBody.put("tourCode", "4160");
            requestBody.put("subTourCode", "81014");
            requestBody.put("supplierID", "COSMOSANDGLOBUS");// todo
          
            searchRequest.put("requestHeader", requestHeader);
            searchRequest.put("requestBody", requestBody);
            restTemplate = RestUtils.getTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<>(searchRequest.toString(), headers);
            ResponseEntity<String> searchResponseEntity = null;

            searchResponseEntity =
                    restTemplate.exchange(this.holidaysSearchURL, HttpMethod.POST, httpEntity, String.class);

            return searchResponseEntity.getBody();

        } catch (HttpClientErrorException httpClientErrorException) {
            httpClientErrorException.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    
	}

}
