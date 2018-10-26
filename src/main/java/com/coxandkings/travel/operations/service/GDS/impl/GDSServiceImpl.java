package com.coxandkings.travel.operations.service.GDS.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsFlightPaxInfo;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.service.GDS.GDSService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.utils.EmailUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GDSServiceImpl implements GDSService {

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private EmailUtils emailUtils;


    @Value("${gds-queue-management.template_config.function}")
    private String function;

    @Value("${gds-queue-management.template_config.scenario}")
    private String scenario;

    @Value("${gds-queue-management.template_config.subject}")
    private String subject;

    @Override
    public String sendCommunication(JSONObject reqJSON) throws OperationException {

        String bookId = reqJSON.optString("travelogixxBookID");
        String orderID = reqJSON.optString("travelogixxOrderID");
        OpsBooking opsBooking = opsBookingService.getBooking(bookId);

        List<OpsProduct> products = opsBooking.getProducts();
        OpsProduct product = products.stream().filter(opsProduct -> opsProduct.getOrderID().equalsIgnoreCase(orderID)).findFirst().get();
        List<OpsFlightPaxInfo> opsPaxFlightInfo = product.getOrderDetails().getFlightDetails().getPaxInfo();
        String email = opsPaxFlightInfo.stream().filter(x -> x.getLeadPax()).findFirst().get().getContactDetails().get(0).getContactInfo().getEmail();

        StringBuilder operatingAirLineDetails = getOperatingAirlineDetails(reqJSON);
        Map<String, String> dynamicValuesMap = getDynamicMap(reqJSON, product, operatingAirLineDetails);

        emailUtils.buildClientMail(function, scenario, email, subject + product.getSupplierRefNumber(), dynamicValuesMap, null, null);
        JSONObject successObject = new JSONObject();
        successObject.put("status", "SUCCESS");
        return successObject.toString();
    }

    private StringBuilder getOperatingAirlineDetails(JSONObject reqJSON) {
        StringBuilder operatingAirLineDetails = new StringBuilder();
        JSONArray flightDetails = reqJSON.getJSONArray("operatingAirline");
        for (int count = 0; count < flightDetails.length(); count++) {
            JSONObject flightDetailsJSONObject = flightDetails.getJSONObject(count);
            operatingAirLineDetails.append(flightDetailsJSONObject.getString("airlineCode") + "-" + flightDetailsJSONObject.getString("flightNumber") + ",");
        }
        return operatingAirLineDetails;
    }

    private Map<String, String> getDynamicMap(JSONObject reqJSON, OpsProduct product,
                                              StringBuilder operatingAirLineDetails) {
        Map<String,String> dynamicValuesMap = new HashMap<String, String>();
        dynamicValuesMap.put("name", product.getOrderDetails().getFlightDetails().getPaxInfo().get(0).getFirstName());
        dynamicValuesMap.put("bookId", reqJSON.optString("travelogixxBookID"));
        dynamicValuesMap.put("orderId", reqJSON.optString("travelogixxOrderID"));
        dynamicValuesMap.put("supplierPNR", reqJSON.optString("airlinepnr"));
        dynamicValuesMap.put("flightNumber", operatingAirLineDetails.substring(0,operatingAirLineDetails.length()-1));
        dynamicValuesMap.put("startDate", product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().get(0).getFlightSegment().get(0).getDepartureDateZDT().toString());
        dynamicValuesMap.put("paxCount", product.getOrderDetails().getFlightDetails().getPaxInfo().size()+"");
        return dynamicValuesMap;
    }




}
