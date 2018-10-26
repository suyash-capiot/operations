package com.coxandkings.travel.operations.service.bedbservice.air.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsOrderDetails;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.resource.bookingenginedb.air.AirOrderPriceDetail;
import com.coxandkings.travel.operations.service.bedbservice.air.AirBeDBUpdateService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.coxandkings.travel.operations.utils.adapter.BeBookingAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("airBeDBUpdateServiceImpl")
public class AirBeDBUpdateServiceImpl implements AirBeDBUpdateService {
	
	private static final Logger logger = LogManager.getLogger(AirBeDBUpdateServiceImpl.class);
	
    @Autowired
    private UserService userService;
    @Autowired
    private BeBookingAdapter beBookingAdapter;

    @Value("${booking_engine.update.air.order-price}")
    private String updateOrderLevelPriceUrl;
    private RestTemplate restTemplate;

    @Override
    public String updateOrderPrice(OpsProduct opsProduct) throws OperationException {
        AirOrderPriceDetail airOrderPriceDetail = new AirOrderPriceDetail();
        airOrderPriceDetail.setOrderID(opsProduct.getOrderID());
        
        String userId=userService.getLoggedInUserId();
        if(userId==null) {
        	userId="";
        }
        airOrderPriceDetail.setUserID(userId);
        OpsOrderDetails opsOrderDetails = opsProduct.getOrderDetails();
        // setting order client commercial
        airOrderPriceDetail.setOrderClientCommercials(beBookingAdapter.getOrderClientCommercials(opsOrderDetails.getClientCommercials()));
        // setting order supplier commercials
        airOrderPriceDetail.setOrderSupplierCommercials(beBookingAdapter.getOrderSupplierCommercials(opsOrderDetails.getSupplierCommercials()));
        airOrderPriceDetail.setOrderSupplierPriceInfo(beBookingAdapter.getOrderSupplierPriceInfo(opsOrderDetails.getFlightDetails().getOpsFlightSupplierPriceInfo()));
        airOrderPriceDetail.setOrderTotalPriceInfo(beBookingAdapter.getOrderTotalPriceInfo(opsOrderDetails.getFlightDetails().getTotalPriceInfo()));

        ObjectMapper objectMapper = new ObjectMapper();
        String request = null;
        try {
            request = objectMapper.writeValueAsString(airOrderPriceDetail);
            logger.info("Booking DB Update RQ " + request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException("Unable to parse the object to string");
        }
        restTemplate = RestUtils.getTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(request, headers);
        String result = null;
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(updateOrderLevelPriceUrl, HttpMethod.PUT, httpEntity, String.class);
            result = responseEntity.getBody();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
