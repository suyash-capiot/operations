package com.coxandkings.travel.operations.service.specialrequest.impl;

import com.coxandkings.travel.ext.model.be.OriginDestinationOption;
import com.coxandkings.travel.operations.beconsumer.BEConstants;
import com.coxandkings.travel.operations.controller.coreBE.SpecialRequestInfoController;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.service.beconsumer.air.AirBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.beconsumer.air.helper.AirBookingEngineConsumptionHelper;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.specialrequest.SpecialRequestService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.jayway.jsonpath.JsonPath;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpecialRequestServiceImpl implements SpecialRequestService, BEConstants {

    private static Logger logger = LogManager.getLogger(SpecialRequestInfoController.class);

    @Autowired
    private AirBookingEngineConsumptionHelper airBookingEngineConsumptionHelper;

    @Autowired
    private OpsBookingService opsBookingService;

    @Value("${booking-engine-core-services.air.search}")
    private String airSearchURL;

    @Value("${booking-engine-core-services.air.getssr}")
    private String airSpecialRequestURL;

    @Value("${booking_engine.update.air.ssr}")
    private String updateSSRURL;

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private JsonObjectProvider filter;

    @Autowired
    private AirBookingEngineConsumptionService airBookingEngineConsumptionService;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private UserService userService;

    @Override
    public String getAllRequestTypes(String bookingId, String orderId) throws OperationException {
        OpsBooking aOpsBooking = opsBookingService.getBooking(bookingId);
        OpsProduct aOpsProduct = opsBookingService.getOpsProduct(aOpsBooking, orderId);
        String supplierName = aOpsProduct.getSupplierID();

        //TODO: Add a check on departure date.
        List<OpsOriginDestinationOption> odos
                = aOpsProduct.getOrderDetails().getFlightDetails().getOriginDestinationOptions();

        for(OpsOriginDestinationOption aOdo : odos){
             Optional<OpsFlightSegment> optionalFlightSegment = aOdo.getFlightSegment().stream().
                    filter(aFlightSegment -> aFlightSegment.getDepartureDateZDT().isBefore(ZonedDateTime.now()))
                     .findFirst();

             if(optionalFlightSegment.isPresent()){
                 throw new OperationException(Constants.OPS_ERR_11301);
             }
        }

        String beSearchResponse = airBookingEngineConsumptionService.search(aOpsBooking, aOpsProduct);
        if(StringUtils.isEmpty(beSearchResponse)){
            logger.info("---booking engine search returned empty response----");
            throw new OperationException(Constants.OPS_ERR_11302);
        }

        JSONObject matchedPricedItinerary = getMatchedPricedItineraryInfo(aOpsProduct,beSearchResponse);

        if(matchedPricedItinerary != null) {
            JSONObject specialRequestResource = new JSONObject();

            JSONObject requestHeader = airBookingEngineConsumptionHelper.getRequestHeader(aOpsBooking);
            JSONObject requestBody = airBookingEngineConsumptionHelper.getSpecialRequestAirRequestBodyJson
                    (aOpsProduct,supplierName,matchedPricedItinerary);

            specialRequestResource.put("requestHeader", requestHeader);
            specialRequestResource.put("requestBody", requestBody);
            String beSSRResponse = null;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<>(specialRequestResource.toString(), headers);
            try {
                ResponseEntity<String> ratesResponseEntity = restUtils.exchange(this.airSpecialRequestURL,
                        HttpMethod.POST, httpEntity, String.class);
                logger.info("after calling getssr of BE");
                beSSRResponse = ratesResponseEntity.getBody();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            JSONObject specialRequests = new JSONObject();
            JSONArray mealRequestInfo = new JSONArray();
            JSONArray specialRequestInfo = new JSONArray();


            //TODO : Confirm with BE team weather pricedItinerary should be a JSON Object as only one value is possible??
            List<String> childSSRs = filter.getChildrenCollection(beSSRResponse,
                    "$.responseBody.pricedItinerary[0].specialServiceRequests", String.class);

            for (String aSSR : childSSRs) {
                String ssrCode = filter.getAttributeValue(aSSR, "$.ssrCode", String.class);
                if (ssrCode != null && ssrCode.trim().length() > 0) {
                    if (ssrCode.toUpperCase().endsWith("ML")) {
                        mealRequestInfo.put(new JSONObject(aSSR));
                    } else {
                        specialRequestInfo.put(new JSONObject(aSSR));
                    }
                }
            }

            specialRequests.put("mealRequestInfo", mealRequestInfo);
            specialRequests.put("specialRequestInfo", specialRequestInfo);

            return specialRequests.toString();
        }

        return null;
    }


    public JSONObject getMatchedPricedItineraryInfo(OpsProduct aOpsProduct, String searchResults) {
        JSONObject matchedPricedItineraryInfo = null;
        try {
            List<OpsOriginDestinationOption> productOdos = aOpsProduct.getOrderDetails().getFlightDetails().getOriginDestinationOptions();
            JSONObject parentObject = new JSONObject(searchResults);
            JSONObject responseBody = parentObject.getJSONObject("responseBody");

            JSONArray pricedItinerarys = responseBody.getJSONArray("pricedItinerary");

            Integer countOdoMatch = 0;
            for (int i = 0; i < pricedItinerarys.length(); i++) {
                JSONObject searchResPricedItinerary = pricedItinerarys.getJSONObject(i);
                JSONObject searchAirItinerary = searchResPricedItinerary.getJSONObject("airItinerary");
                JSONArray searchResODOs = searchAirItinerary.getJSONArray("originDestinationOptions");


                for (int j = 0; j < searchResODOs.length(); j++) {

                    JSONObject jsonObject = searchResODOs.getJSONObject(j);
                    JSONArray flightSegments = jsonObject.getJSONArray("flightSegment");

                    for (OpsOriginDestinationOption aProductOdo : productOdos) {

                        Integer countMatchFlightSegmentMatch = 0;

                        for (int k = 0; k < flightSegments.length(); k++) {
                            JSONObject flightSegment = flightSegments.getJSONObject(k);
                            String arrivalDate = flightSegment.getString("arrivalDate");
                            JSONObject operatingAirline = flightSegment.getJSONObject("operatingAirline");
                            String airlineCode = operatingAirline.getString("airlineCode");
                            String flightNumber = operatingAirline.getString("flightNumber");

                            Optional<OpsFlightSegment> aFlightSegment = aProductOdo.getFlightSegment().stream().filter(
                                aflightsegment -> aflightsegment.getArrivalDateZDT().toString().equals(arrivalDate)
                                && aflightsegment.getOperatingAirline().getAirlineCode().equalsIgnoreCase(airlineCode)
                                && aflightsegment.getOperatingAirline().getFlightNumber().equalsIgnoreCase(flightNumber))
                                .findFirst();

                            if (!(aFlightSegment.isPresent())) {
                                countMatchFlightSegmentMatch++;
                                break;
                            }
                        }
                        if(countMatchFlightSegmentMatch == aProductOdo.getFlightSegment().size()){
                            countOdoMatch++;
                            break;
                        }
                    }
                }
                if(countOdoMatch == searchResODOs.length()){
                    matchedPricedItineraryInfo = searchResPricedItinerary;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Error occured while compareWithCheaperPriceUpdatesFlightInfo");
        }
        return matchedPricedItineraryInfo;
    }


    @Override
    public MessageResource updateSSR(JSONObject updateSsrReqJson) {

        JSONObject specialRequests =  updateSsrReqJson.getJSONObject(JSON_PROP_SPECIALREQUESTS);
        JSONArray specialRequestInfo = concatArray(specialRequests.getJSONArray(JSON_PROP_SPECIALREQUESTINFO),
                specialRequests.getJSONArray("mealRequestInfo"));

        JSONObject newSpecialRequests = new JSONObject();
        newSpecialRequests.put(JSON_PROP_SPECIALREQUESTINFO,specialRequestInfo);
        updateSsrReqJson.remove(JSON_PROP_SPECIALREQUESTS);
        updateSsrReqJson.put(JSON_PROP_SPECIALREQUESTS, newSpecialRequests);
        updateSsrReqJson.put(JSON_PROP_USERID, userService.getLoggedInUserId());


        String beSSRResponse =  null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(updateSsrReqJson.toString(), headers);
        try {
            ResponseEntity<String> ratesResponseEntity = restUtils.exchange(this.updateSSRURL,
                    HttpMethod.PUT, httpEntity, String.class);
            logger.info("after calling update ssr of BE");
            beSSRResponse = ratesResponseEntity.getBody();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        MessageResource messageResource = new MessageResource();
        messageResource.setMessage(beSSRResponse);

        return messageResource;
    }

    private JSONArray concatArray(JSONArray... arrs)
            throws JSONException {
        JSONArray result = new JSONArray();
        for (JSONArray arr : arrs) {
            for (int i = 0; i < arr.length(); i++) {
                result.put(arr.get(i));
            }
        }
        return result;
    }
}
