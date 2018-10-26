package com.coxandkings.travel.operations.service.beconsumer.air.impl;

import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.resource.doTicketing.DoTicketingResource;
import com.coxandkings.travel.operations.service.beconsumer.air.AirBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.beconsumer.air.helper.AirBookingEngineConsumptionHelper;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Service
public class AirBookingEngineConsumptionServiceImpl implements AirBookingEngineConsumptionService {

    @Autowired
    private AirBookingEngineConsumptionHelper airBookingEngineConsumptionHelper;

    private RestTemplate restTemplate;

    private static Logger logger = LogManager.getLogger(AirBookingEngineConsumptionServiceImpl.class);

    @Value("${booking-engine-core-services.air.search}")
    private String airSearchURL;

    @Value("${booking-engine-core-services.air.price}")
    private String airPriceURL;

    @Value("${booking-engine-core-services.air.reprice}")
    private String airRePriceURL;

    @Value("${booking-engine-core-services.air.issueTicket}")
    private String airIssueTicketURL;

    @Override
    public String getPriceForAirJson(OpsBooking opsBooking, OpsProduct opsProduct, String supplierName) throws JSONException, ParseException, IOException {
        logger.info("Inside the getPriceForAirJson ");
        JSONObject airRePriceResource = new JSONObject();
        JSONObject requestHeader = airBookingEngineConsumptionHelper.getRequestHeader(opsBooking);
        JSONObject requestBody = airBookingEngineConsumptionHelper.getPriceAirRequestBodyJson(opsProduct, supplierName);
        airRePriceResource.put("requestHeader", requestHeader);
        airRePriceResource.put("requestBody", requestBody);
        System.out.println(airRePriceResource);
        String rates = null;

        restTemplate = RestUtils.getTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(airRePriceResource.toString(), null);
        try {
            ResponseEntity<String> ratesResponseEntity = restTemplate.exchange(this.airPriceURL,
                    HttpMethod.POST, httpEntity, String.class);
            logger.info("after calling getPrice of BE");
            rates = ratesResponseEntity.getBody();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return rates;

    }

    @Override
    public String search(OpsBooking opsBooking, OpsProduct opsProduct) throws JSONException {
        JSONObject searchRequest = new JSONObject();
        ResponseEntity<String> ratesResponseEntity = null;
        JSONObject requestBody = new JSONObject();
        JSONObject requestHeader = airBookingEngineConsumptionHelper.getRequestHeader(opsBooking);
        List<JSONObject> paxInfoListJson = airBookingEngineConsumptionHelper.getPaxInfoListJson(opsProduct);
        List<JSONObject> originDestinationOptions = airBookingEngineConsumptionHelper.getOriginDestinationOptions(opsProduct.getOrderDetails().getFlightDetails().getOriginDestinationOptions());
        String tripType = opsProduct.getOrderDetails().getFlightDetails().getTripType();
        String tripIndicator = opsProduct.getOrderDetails().getFlightDetails().getTripIndicator();
        requestBody.put("tripType", tripType);
        requestBody.put("tripIndicator", tripIndicator);
        requestBody.put("originDestinationInfo", originDestinationOptions);
        requestBody.put("paxInfo", paxInfoListJson);
        requestBody.put("cabinType", "Economy"); // todo
        searchRequest.put("requestHeader", requestHeader);
        searchRequest.put("requestBody", requestBody);
        restTemplate = RestUtils.getTemplate();

//        SimpleClientHttpRequestFactory factory = (SimpleClientHttpRequestFactory)restTemplate.getRequestFactory();
//        factory.setConnectTimeout(requestTimeout);
//        factory.setReadTimeout(requestTimeout);
//        restTemplate.setRequestFactory(factory);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(searchRequest.toString(), headers);
        try {
            ratesResponseEntity =
                    restTemplate.exchange(this.airSearchURL, HttpMethod.POST, httpEntity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String response = null;
        if(ratesResponseEntity != null)
            response = ratesResponseEntity.getBody();

        return response;
    }

    @Override
    public String getRePriceForAirJson(OpsBooking opsBooking, OpsProduct opsProduct, String supplierName) throws JSONException, ParseException, IOException {
        JSONObject airRePriceResource;
        airRePriceResource = getRePriceRequestForAirJson(opsBooking ,opsProduct);
        JSONObject repriceRs = getRePriceResponseForAirJson(airRePriceResource);
        return repriceRs!=null ? repriceRs.toString() : null;
    }

    @Override
    public JSONObject getRePriceResponseForAirJson(JSONObject rePriceRequest) throws JSONException, ParseException, IOException {
        String repriceRs = null;
        restTemplate = RestUtils.getTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(rePriceRequest.toString(), null);
        try {
            ResponseEntity<String> ratesResponseEntity = restTemplate.exchange(this.airRePriceURL,
                    HttpMethod.POST, httpEntity, String.class);
            logger.info("after calling getPrice of BE");
            repriceRs = ratesResponseEntity.getBody();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        if(repriceRs==null)
            return null;

        JSONObject response = new JSONObject(new JSONTokener(repriceRs));
        return response;
    }

    @Override
    public JSONObject getRePriceRequestForAirJson(OpsBooking opsBooking, OpsProduct opsProduct) throws JSONException, ParseException, IOException {
        JSONObject airRePriceResource = new JSONObject();
        JSONObject requestHeader = airBookingEngineConsumptionHelper.getRequestHeader(opsBooking);
        JSONObject requestBody = airBookingEngineConsumptionHelper.getRePriceAirRequestBodyJson(opsProduct, opsProduct.getSupplierID());
        airRePriceResource.put("requestHeader", requestHeader);
        airRePriceResource.put("requestBody", requestBody);

        logger.info(String.format("BE Reprice Request for Air bookId: %s is - %s", opsBooking.getBookID(), airRePriceResource.toString()));
        return airRePriceResource;
    }

    @Override
    public JSONObject getBookRequestForAirJson(OpsBooking opsBooking, OpsProduct opsProduct, String supplierID) {

        JSONObject airBookResource = new JSONObject();
        JSONObject requestHeader = airBookingEngineConsumptionHelper.getRequestHeader(opsBooking);
        JSONObject requestBody = airBookingEngineConsumptionHelper.getBookAirRequestBodyJson(opsBooking, opsProduct);
        airBookResource.put("requestHeader", requestHeader);
        airBookResource.put("requestBody", requestBody);

        logger.info(String.format("BE Book Request for Air bookId: %s is - %s", opsBooking.getBookID(), airBookResource.toString()));
        return airBookResource;
    }

    @Override
    public JSONObject createBEAirBookRQFromRepriceRS(JSONObject repriceRs, OpsBooking opsBooking, OpsProduct opsProduct) {

        if(repriceRs==null || repriceRs.length()==0)
            return null;

        String tripType = opsProduct.getOrderDetails().getFlightDetails().getTripType();
        JSONObject responseHeader = repriceRs.getJSONObject("responseHeader");
        JSONObject responseBody = repriceRs.getJSONObject("responseBody");
        JSONObject bookRequest = new JSONObject();
        JSONObject requestBody = new JSONObject();
        bookRequest.put("requestHeader", responseHeader);
        bookRequest.put("requestBody", requestBody);

        requestBody.put("pricedItinerary", responseBody.getJSONArray("pricedItinerary"));
        requestBody.put("tripType", tripType);
        requestBody.put("bookID", opsBooking.getBookID());
        requestBody.put("tripIndicator", opsProduct.getOrderDetails().getFlightDetails().getTripIndicator());
        requestBody.put("paxInfo", airBookingEngineConsumptionHelper.getPaxInfoListJson(opsProduct)); //completed
        requestBody.put("paxDetails", airBookingEngineConsumptionHelper.getPaxDetailsJson(opsProduct));
        requestBody.put("paymentInfo", airBookingEngineConsumptionHelper.getPaymentInfoJson(opsBooking));

        return bookRequest;
    }

    @Override
    public JSONObject getIssueTicketRequestForAirJson(OpsBooking opsBooking, OpsProduct opsProduct, DoTicketingResource doTicketingResource) {

        String credentialName = "";
        if(doTicketingResource!=null)
            credentialName = doTicketingResource.getTicketingCredential();
        JSONObject airIssueTicketResource = new JSONObject();
        JSONObject requestHeader = airBookingEngineConsumptionHelper.getRequestHeader(opsBooking);
        JSONObject requestBody = airBookingEngineConsumptionHelper.getIssueTicketAirRequestBodyJson(opsBooking, opsProduct);
        airIssueTicketResource.put("requestHeader", requestHeader);
        airIssueTicketResource.put("requestBody", requestBody);
        requestBody.put("credentialName", credentialName);

        logger.info(String.format("BE Issue Ticket Request for Air bookId: %s is - %s", opsBooking.getBookID(), airIssueTicketResource.toString()));
        return airIssueTicketResource;

    }

    @Override
    public JSONObject getIssueTicketResponseAir(OpsBooking opsBooking, OpsProduct opsProduct, DoTicketingResource doTicketingResource)  {

        JSONObject issueTicketRq = null;
        String res = null;
        try {
            issueTicketRq = getIssueTicketRequestForAirJson(opsBooking, opsProduct, doTicketingResource);
        }catch(Exception e){
            logger.error("Unable to create Air BE-Issue Ticket request for BookId: " + opsBooking.getBookID());
            return null;
        }
        logger.info("Issue Ticket Request is : " + issueTicketRq.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(issueTicketRq.toString(), headers);
        try {
            ResponseEntity<String> ratesResponseEntity = RestUtils.exchange(this.airIssueTicketURL, HttpMethod.POST, httpEntity, String.class);
            res = ratesResponseEntity.getBody();
        } catch (Exception e) {
           logger.warn("Exception occured while issuing Ticket " +e.getMessage());
        }
        if(res==null)
            return null;

        return new JSONObject(new JSONTokener(res));
    }

}
