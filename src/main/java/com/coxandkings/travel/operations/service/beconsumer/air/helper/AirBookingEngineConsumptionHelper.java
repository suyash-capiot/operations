package com.coxandkings.travel.operations.service.beconsumer.air.helper;

import com.coxandkings.travel.ext.model.be.SpecialServiceRequest;
import com.coxandkings.travel.operations.model.core.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AirBookingEngineConsumptionHelper {
    private static Logger logger = LogManager.getLogger(AirBookingEngineConsumptionHelper.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static ObjectMapper objectMapper = new ObjectMapper();

    static{
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public JSONObject getRequestHeader(OpsBooking opsBooking) throws JSONException {
        JSONObject requestHeader = new JSONObject();

        requestHeader.put("sessionID", opsBooking.getSessionID());
        requestHeader.put("transactionID", opsBooking.getTransactionID());
        requestHeader.put("userID", opsBooking.getUserID());
        requestHeader.put("clientContext", getClientContextJson(opsBooking));
        return requestHeader;
    }

    private JSONObject getClientContextJson(OpsBooking opsBooking) throws JSONException {
        JSONObject clientContext = new JSONObject();
        clientContext.put("clientID", opsBooking.getClientID());
        clientContext.put("clientMarket", opsBooking.getClientMarket());
        clientContext.put("clientType", opsBooking.getClientType());
        clientContext.put("clientLanguage", opsBooking.getClientLanguage());
        clientContext.put("clientIATANumber", "1234");
        clientContext.put("pointOfSale", opsBooking.getPointOfSale());
        clientContext.put("clientNationality", ""); // todo be not sending currently
        clientContext.put("clientCallbackAddress", "");// todo be not sending currently
        clientContext.put("clientCurrency", opsBooking.getClientCurrency());
        return clientContext;
    }

    private List<JSONObject> getPricedItineraryListJson(OpsProduct opsProduct, String supplierName) throws JSONException {
        OpsFlightDetails flightDetails = opsProduct.getOrderDetails().getFlightDetails();
        List<JSONObject> pricedItineraryList = new ArrayList<>();
        JSONObject pricedItinerary = new JSONObject();
        //TODO: remove this hardcoding, getBooking doesn't have this.
        pricedItinerary.put("isReturnJourneyCombined", false);
        JSONObject airItinerary = new JSONObject();
        List<JSONObject> originDestinationOptions = new ArrayList<>();
        List<OpsOriginDestinationOption> opsOriginDestinationOptions = flightDetails.getOriginDestinationOptions();

        if (opsOriginDestinationOptions != null) {
            for (OpsOriginDestinationOption opsOriginDestinationOption : opsOriginDestinationOptions) {
                JSONObject originDestinationOption = new JSONObject();
                List<JSONObject> flightSegmentList = new ArrayList<>();
                List<OpsFlightSegment> opsFlightSegments = opsOriginDestinationOption.getFlightSegment();
                if (opsFlightSegments != null) {
                    for (OpsFlightSegment opsFlightSegment : opsFlightSegments) {
                        JSONObject flightSegment = new JSONObject();
                        flightSegment.put("arrivalTerminal", opsFlightSegment.getArrivalTerminal());
                        flightSegment.put("availableCount", opsFlightSegment.getAvailableCount());
                        flightSegment.put("cabinType", opsFlightSegment.getCabinType());
                        flightSegment.put("connectionType", opsFlightSegment.getConnectionType());
                        //flightSegment.0(opsFlightSegment.getArrivalDateZDT()); todo
                        //Time in format - dd:MM:yyyyTHH:mm:ss
                        String date = dateTimeFormatter.format(opsFlightSegment.getArrivalDateZDT());
                        flightSegment.put("arrivalDate", date);
                        date = dateTimeFormatter.format(opsFlightSegment.getDepartureDateZDT());
                        flightSegment.put("departureDate", date);
                        flightSegment.put("cabinType", opsFlightSegment.getCabinType());
                        flightSegment.put("departureTerminal", opsFlightSegment.getDepartureTerminal());
                        flightSegment.put("destinationLocation", opsFlightSegment.getDestinationLocation());
                        flightSegment.put("extendedRPH", opsFlightSegment.getExtendedRPH());
                        flightSegment.put("journeyDuration", opsFlightSegment.getJourneyDuration().intValue());
                        flightSegment.put("refundableIndicator", opsFlightSegment.getRefundableIndicator());
                        flightSegment.put("resBookDesigCode", opsFlightSegment.getResBookDesigCode());
                        flightSegment.put("fareInfo", getFareInfo(opsFlightSegment.getFareInfo()));
                        flightSegment.put("rph", "");
                        flightSegment.put("status", opsFlightSegment.getStatus());

                        JSONObject marketingAirline = new JSONObject();
                        if (opsFlightSegment.getMarketingAirline() != null) {
                            marketingAirline.put("airlineCode", opsFlightSegment.getMarketingAirline().getAirlineCode());
                            marketingAirline.put("flightNumber", opsFlightSegment.getMarketingAirline().getFlightNumber());
                        }
                        flightSegment.put("marketingAirline", marketingAirline);
                        OpsOperatingAirline opsOperatingAirline = opsFlightSegment.getOperatingAirline();
                        JSONObject operatingAirline = new JSONObject();
                        if (opsOperatingAirline != null) {
                            operatingAirline.put("airlineCode", opsOperatingAirline.getAirlineCode());
                            operatingAirline.put("flightNumber", opsOperatingAirline.getFlightNumber());
                            flightSegment.put("operatingAirline", operatingAirline);
                        }
                        flightSegment.put("originLocation", opsFlightSegment.getOriginLocation());
                        flightSegment.put("quoteID", opsFlightSegment.getQuoteID());
                        flightSegmentList.add(flightSegment);
                    }
                    originDestinationOption.put("flightSegment", flightSegmentList);
                }
                originDestinationOptions.add(originDestinationOption);
            }
        }
        airItinerary.put("originDestinationOptions", originDestinationOptions);
        pricedItinerary.put("airItinerary", airItinerary);
        if (!StringUtils.isEmpty(supplierName)) {
            pricedItinerary.put("supplierRef", supplierName);
        } else {
            pricedItinerary.put("supplierRef", opsProduct.getSupplierID());
        }
        pricedItineraryList.add(pricedItinerary);
        return pricedItineraryList;
    }

    private JSONArray getFareInfo(List<OpsFareInfo_> fareInfoList) {

        JSONArray fareInfos = new JSONArray();
        for(OpsFareInfo_ fareInfo : fareInfoList){
            JSONObject fareInfoObject = new JSONObject();
            fareInfoObject.put("paxType", fareInfo.getPaxType());
            fareInfoObject.put("fareBasisCode", fareInfo.getFareBasisCode());
            fareInfoObject.put("fareReference", fareInfo.getFareReference());
            fareInfos.put(fareInfoObject);
        }
        return fareInfos;
    }

    public List<JSONObject> getOriginDestinationOptions(List<OpsOriginDestinationOption> originDestinationOptions) throws JSONException {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (OpsOriginDestinationOption opsOriginDestinationOption : originDestinationOptions) {

            List<OpsFlightSegment> flightSegment = opsOriginDestinationOption.getFlightSegment();
            for (OpsFlightSegment opsFlightSegment : flightSegment) {
                JSONObject jsonObject = new JSONObject();
                String departureDate = opsFlightSegment.getDepartureDateZDT().toOffsetDateTime().toString();
                logger.info("departureDate->" + departureDate.substring(0,departureDate.indexOf("T")));
                jsonObject.put("departureDate", departureDate.substring(0,departureDate.indexOf("T")));
                logger.info("departureDate->" + opsFlightSegment.getDepartureDateZDT().toOffsetDateTime().toString());
                jsonObject.put("departureDate", opsFlightSegment.getDepartureDateZDT().toLocalDate().toString());
                jsonObject.put("originLocation", opsFlightSegment.getOriginLocation());
                jsonObject.put("destinationLocation", opsFlightSegment.getDestinationLocation());
                jsonObjectList.add(jsonObject);
            }
        }
        return jsonObjectList;
    }

    public JSONObject getRePriceAirRequestBodyJson(OpsProduct opsProduct, String supplierName) throws JSONException {
        String tripType = opsProduct.getOrderDetails().getFlightDetails().getTripType();
        JSONObject requestBody = new JSONObject();
        requestBody.put("tripType", tripType);
        requestBody.put("tripIndicator", opsProduct.getOrderDetails().getFlightDetails().getTripIndicator());
        requestBody.put("paxInfo", getPaxInfoListJson(opsProduct)); //completed
        requestBody.put("pricedItinerary", getPricedItineraryListJson(opsProduct, supplierName)); //completed
        requestBody.put("paxDetails", getPaxDetailsJson(opsProduct));
        return requestBody;
    }

    public JSONObject getBookAirRequestBodyJson(OpsBooking opsBooking, OpsProduct opsProduct) {

        String tripType = opsProduct.getOrderDetails().getFlightDetails().getTripType();
        JSONObject requestBody = new JSONObject();
        requestBody.put("tripType", tripType);
        requestBody.put("bookID", opsBooking.getBookID());
//        requestBody.put("tripIndicator", opsProduct.getOrderDetails().getFlightDetails().getTripIndicator());
        requestBody.put("paxInfo", getPaxInfoListJson(opsProduct)); //completed
        requestBody.put("pricedItinerary", getPricedItineraryListForBookJson(opsProduct)); //completed
        requestBody.put("paxDetails", getPaxDetailsJson(opsProduct));
        requestBody.put("paymentInfo", getPaymentInfoJson(opsBooking));
        return requestBody;
    }

    private List<JSONObject> getPricedItineraryListForBookJson(OpsProduct opsProduct) {

        List<JSONObject> pricedItineraryList = getPricedItineraryListJson(opsProduct, opsProduct.getSupplierID());
        //Add airItineraryPricingInfo
        JSONObject pricedItinerary = pricedItineraryList.get(0);
        pricedItinerary.put("airItineraryPricingInfo", getAirItineraryPricingInfo(opsProduct));
        return pricedItineraryList;
    }

    private JSONObject getAirItineraryPricingInfo(OpsProduct opsProduct) {

        JSONObject airItineraryPricingInfo = new JSONObject();
        OpsFlightTotalPriceInfo opsFlightTotalPriceInfo = opsProduct.getOrderDetails().getFlightDetails().getTotalPriceInfo();
        List<SpecialServiceRequest> specialServiceRequests = opsFlightTotalPriceInfo.getSpecialServiceRequests();
        if(specialServiceRequests!=null)
            try {
                String temp = objectMapper.writeValueAsString(specialServiceRequests);
                airItineraryPricingInfo.put("specialServiceRequests", new JSONArray(temp));
            } catch (JsonProcessingException e) {}

        airItineraryPricingInfo.put("paxTypeFares", getPaxTypeFareJson(opsFlightTotalPriceInfo.getPaxTypeFares()));
        airItineraryPricingInfo.put("itinTotalFare", getItinTotalFare(opsFlightTotalPriceInfo));
        //TODO: Populate incentives
        airItineraryPricingInfo.put("incentives", JSONObject.NULL);

        return airItineraryPricingInfo;
    }

    private JSONArray getPaxTypeFareJson(List<OpsPaxTypeFareFlightClient> paxTypeFares) {

        if(paxTypeFares == null)
            return null;

        JSONArray paxTypeFaresArr;
        try{
            String temp = objectMapper.writeValueAsString(paxTypeFares);
            paxTypeFaresArr = new JSONArray(temp);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        for(int i=0;i<paxTypeFaresArr.length();i++){

            JSONObject paxTypeFare = paxTypeFaresArr.getJSONObject(i);
            JSONArray receivablesArr = new JSONArray();
            JSONObject receivables = new JSONObject();
            BigDecimal sum = BigDecimal.ZERO;
            String ccyCode = null;
            JSONArray opsClientEntityCommercial = paxTypeFare.optJSONArray("clientEntityCommercial");
            for(int j=0;opsClientEntityCommercial!=null && j<opsClientEntityCommercial.length();j++){
                JSONArray clientCommercials = opsClientEntityCommercial.getJSONObject(j).optJSONArray("clientCommercials");
                for(int k=0;clientCommercials!=null && k<clientCommercials.length();k++){
                    JSONObject clientCommercial = clientCommercials.getJSONObject(k);
                    if("Receivable".equalsIgnoreCase(clientCommercial.optString("commericalType"))){
                        JSONObject receivable = new JSONObject();
                        receivable.put("code", clientCommercial.optString("commercialName"));
                        receivable.put("amount", clientCommercial.opt("commercialAmount"));
                        sum = sum.add(clientCommercial.optBigDecimal("commercialAmount", BigDecimal.ZERO));
                        ccyCode = clientCommercial.optString("commercialCurrency");
                        receivable.put("currencyCode", clientCommercial.optString("commercialCurrency"));
                        receivablesArr.put(receivable);
                    }
                }
            }
            receivables.put("amount", sum);
            receivables.put("currencyCode", ccyCode);
            receivables.put("receivable", receivablesArr);
            paxTypeFare.put("receivables", receivables);
            paxTypeFare.remove("clientEntityCommercial");
        }
        return paxTypeFaresArr;
    }

    private JSONObject getItinTotalFare(OpsFlightTotalPriceInfo opsFlightTotalPriceInfo) {

        JSONObject itinTotalFare = new JSONObject();
        itinTotalFare.put("amount", new BigDecimal(opsFlightTotalPriceInfo.getTotalPrice()));
        itinTotalFare.put("currencyCode", opsFlightTotalPriceInfo.getCurrencyCode());

        try {
            String temp = objectMapper.writeValueAsString(opsFlightTotalPriceInfo.getBaseFare());
            itinTotalFare.put("baseFare", new JSONObject(new JSONTokener(temp)));
        } catch (JsonProcessingException e) {}

        if(opsFlightTotalPriceInfo.getTaxes()!=null)
            try {
                String temp = objectMapper.writeValueAsString(opsFlightTotalPriceInfo.getTaxes());
                itinTotalFare.put("taxes", new JSONObject(new JSONTokener(temp)));
            } catch (JsonProcessingException e) {}

        if(opsFlightTotalPriceInfo.getCompanyTaxes()!=null)
            try {
                String temp = objectMapper.writeValueAsString(opsFlightTotalPriceInfo.getCompanyTaxes());
                itinTotalFare.put("companyTaxes", new JSONObject(new JSONTokener(temp)));
            } catch (JsonProcessingException e) {}

        if(opsFlightTotalPriceInfo.getReceivables()!=null)
            try {
                String temp = objectMapper.writeValueAsString(opsFlightTotalPriceInfo.getReceivables());
                itinTotalFare.put("receivables", new JSONObject(new JSONTokener(temp)));
            } catch (JsonProcessingException e) {}

        return itinTotalFare;
    }

    private List<JSONObject> getSpecialServiceRequestJson(List<SpecialServiceRequest> specialServiceRequests) {

        List<JSONObject> specialServiceList = new ArrayList<>();
        for(SpecialServiceRequest serviceRequest : specialServiceRequests){
            JSONObject specialServiceJson;
            try {
                String res = objectMapper.writeValueAsString(serviceRequest);
                specialServiceJson = new JSONObject(new JSONTokener(res));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                continue;
            }
            specialServiceList.add(specialServiceJson);
        }
        return specialServiceList;
    }

    public JSONArray getPaymentInfoJson(OpsBooking opsBooking) {

        String res = "[]";
        try{
            res = objectMapper.writeValueAsString(opsBooking.getPaymentInfo());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new JSONArray(res);
    }

    public List<JSONObject> getPaxInfoListJson(OpsProduct opsProduct) throws JSONException {
        List<OpsFlightPaxInfo> paxInfos = opsProduct.getOrderDetails().getFlightDetails().getPaxInfo();
        List<JSONObject> paxInfoList = new ArrayList<>();


            Map<String, Integer> paxMap = new HashMap<>();
            for (OpsFlightPaxInfo opsAccommodationPaxInfo : paxInfos) {
                Integer count = paxMap.get(opsAccommodationPaxInfo.getPaxType());
                if (count != null) {
                    paxMap.put(opsAccommodationPaxInfo.getPaxType(), ++count);
                } else {
                    paxMap.put(opsAccommodationPaxInfo.getPaxType(), 1);
                }
            }
            for (Map.Entry<String, Integer> entry : paxMap.entrySet()) {
                JSONObject paxInfo = new JSONObject();
                paxInfo.put("paxType", entry.getKey());
                paxInfo.put("quantity", entry.getValue());
                paxInfoList.add(paxInfo);
            }

        return paxInfoList;
    }

    public List<JSONObject> getPaxDetailsJson(OpsProduct opsProduct) throws JSONException {
        List<JSONObject> paxDetails = new ArrayList<>();
        List<OpsFlightPaxInfo> paxInfos = opsProduct.getOrderDetails().getFlightDetails().getPaxInfo();
        if (paxInfos != null) {
            for (OpsFlightPaxInfo opsFlightPaxInfo : paxInfos) {
                JSONObject paxDetail = new JSONObject();
                paxDetail.put("dob", opsFlightPaxInfo.getBirthDate());
                paxDetail.put("firstName", opsFlightPaxInfo.getFirstName());
                paxDetail.put("middleName", opsFlightPaxInfo.getFirstName());
                paxDetail.put("paxType", opsFlightPaxInfo.getPaxType());
                paxDetail.put("title", opsFlightPaxInfo.getTitle());
                if(opsFlightPaxInfo.getTitle()!=null && opsFlightPaxInfo.getTitle().equalsIgnoreCase("Mr"))
                    paxDetail.put("gender", "Male");
                else if(opsFlightPaxInfo.getTitle()!=null && opsFlightPaxInfo.getTitle().equalsIgnoreCase("Mrs"))
                    paxDetail.put("gender", "Female");
                else
                    paxDetail.put("gender", "Male");
                //TODO: No Gender in getBooking From BE.
//                paxDetail.put("gender", opsFlightPaxInfo.getG);
                paxDetail.put("surname", opsFlightPaxInfo.getLastName());

                JSONObject addressDetails = getAddressDetailsJson(opsFlightPaxInfo.getAddressDetails());
                JSONObject ancillaryServices = getAncillaryServicesJson(opsFlightPaxInfo.getAncillaryServices());
                JSONObject specialRequests = getSpecialRequestsJson(opsFlightPaxInfo.getOpsSpecialRequest());
                List<JSONObject> contactDetails = getContactDetailsJson(opsFlightPaxInfo.getContactDetails());
             //   paxDetail.put("documentDetails",getDocumentDetailsJson());// todo
                paxDetail.put("addressDetails", addressDetails);
                paxDetail.put("contactDetails", contactDetails);
                if(specialRequests!=null && specialRequests.length()!=0) {
                    paxDetail.put("specialRequests", specialRequests);
                }
                if(ancillaryServices.getJSONArray("ancillaryInfo")!=null && ancillaryServices.getJSONArray("ancillaryInfo").length()!=0)
                    paxDetail.put("ancillaryServices", ancillaryServices);
                paxDetails.add(paxDetail);
            }
        }

        return paxDetails;
    }

    public JSONObject getAddressDetailsJson(OpsAddressDetails addressDetails) throws JSONException {
        JSONObject addressDetail = new JSONObject();
        if (addressDetails != null) {
            addressDetail.put("city", addressDetails.getCityName());
            addressDetail.put("country", addressDetails.getCountryName());
            addressDetail.put("state", addressDetails.getState());
            addressDetail.put("zip", addressDetails.getZipCode());
            if (addressDetails.getAddressLines().size() >= 2)
                addressDetail.put("addrLine1", addressDetails.getAddressLines().get(0));
            addressDetail.put("addrLine2", addressDetails.getAddressLines().get(1));

        }
        return addressDetail;
    }

    public JSONObject getAncillaryServicesJson(OpsAncillaryServices ancillaryServices) throws JSONException {

        List<JSONObject> ancillaryInfo = new ArrayList<>();
        JSONObject ancillaryJosn = new JSONObject();
        for (OpsAncillaryInfo opsAncillaryInfo : ancillaryServices.getAncillaryInfo()) {
            JSONObject ancillaryJson = new JSONObject();
            ancillaryJson.put("amount", opsAncillaryInfo.getAmount());
            ancillaryJson.put("description", opsAncillaryInfo.getDescription());
            ancillaryJson.put("quantity", opsAncillaryInfo.getQuantity());
            ancillaryJson.put("type", opsAncillaryInfo.getType());
            ancillaryJson.put("unit", opsAncillaryInfo.getUnit());
            ancillaryInfo.add(ancillaryJson);
        }
        ancillaryJosn.put("ancillaryInfo", ancillaryInfo);
        return ancillaryJosn;
    }

    public JSONObject getSpecialRequestsJson(OpsSpecialRequest opsSpecialRequest) throws JSONException {
        JSONObject specialPriceRequestJson = new JSONObject();
        List<JSONObject> specialRequestInfo = new ArrayList<>();
        if(opsSpecialRequest!=null) {
            List<OpsSpecialRequestInfo> specialRequestInfos = opsSpecialRequest.getSpecialRequestInfo();
            for (OpsSpecialRequestInfo specialRequestInfo1 : specialRequestInfos) {
                JSONObject specialPriceJson = new JSONObject();
                specialPriceJson.put("flightRefNumberRphList", specialRequestInfo1.getFlightRefNumberRphList());
                specialPriceJson.put("amount", specialRequestInfo1.getAmount());
                specialPriceJson.put("serviceQuantity", specialRequestInfo1.getServiceQuantity());
                specialPriceJson.put("destinationLocation", specialRequestInfo1.getDestinationLocation());
                specialPriceJson.put("originLocation", specialRequestInfo1.getOriginLocation());
                specialPriceJson.put("ssrCode", specialRequestInfo1.getSsrCode());
                specialPriceJson.put("flightNumber", specialRequestInfo1.getFlightNumber());
                specialPriceJson.put("number", specialRequestInfo1.getNumber());
                specialPriceJson.put("type", specialRequestInfo1.getType());
                specialPriceJson.put("categoryCode", specialRequestInfo1.getCategoryCode());
                specialPriceJson.put("description", specialRequestInfo1.getDescription());
                specialPriceJson.put("airlineCode", specialRequestInfo1.getAirlineCode());
                if(specialRequestInfo1.getServicePrice()!=null) {
                    JSONObject servicePrice = new JSONObject();
                    servicePrice.put("basePrice", specialRequestInfo1.getServicePrice().getBasePrice());//todo
                    specialPriceJson.put("servicePrice", servicePrice);
                }
                if(specialRequestInfo1.getTaxes()!=null) {
                    JSONObject taxes = new JSONObject();
                    taxes.put("amount", specialRequestInfo1.getTaxes().getAmount());//todo
                    specialPriceJson.put("taxes", taxes);
                }
                specialPriceJson.put("companyShortName", specialRequestInfo1.getCompanyShortName());

                specialPriceJson.put("currencyCode", specialRequestInfo1.getCurrencyCode());
                specialPriceJson.put("status", specialRequestInfo1.getStatus());
                specialRequestInfo.add(specialPriceJson);
                specialPriceRequestJson.put("specialRequestInfo", specialRequestInfo);
            }
        }
        return specialPriceRequestJson;
    }

    public List<JSONObject> getContactDetailsJson(List<OpsContactDetails> contactDetails) throws JSONException {
        List<JSONObject> contactDetailsJson = new ArrayList<>();
        if (contactDetails != null) {
            for (OpsContactDetails opsContactDetails : contactDetails) {
                JSONObject contactDetail = new JSONObject();
                JSONObject contactInfo = new JSONObject(opsContactDetails.getContactInfo());
                contactDetail.put("contactInfo", contactInfo);
                contactDetailsJson.add(contactDetail);
            }
        }
        return contactDetailsJson;
    }

    public JSONObject getPriceAirRequestBodyJson(OpsProduct opsProduct, String supplierName) throws JSONException {
        JSONObject requestBody = new JSONObject();
        String tripType = opsProduct.getOrderDetails().getFlightDetails().getTripType();
        requestBody.put("tripType", tripType);
        requestBody.put("paxInfo", getPaxInfoListJson(opsProduct)); //completed
        requestBody.put("pricedItinerary", getPricedItineraryListJson(opsProduct, supplierName)); //completed
        return requestBody;
    }

    public JSONObject getSpecialRequestAirRequestBodyJson(OpsProduct opsProduct, String supplierName, JSONObject matchedPricedItinerary) throws JSONException {
        JSONObject requestBody = new JSONObject();
        String tripType = opsProduct.getOrderDetails().getFlightDetails().getTripType();
        String tripIndicator = opsProduct.getOrderDetails().getFlightDetails().getTripIndicator();
        requestBody.put("tripType", tripType);
        requestBody.put("tripIndicator",  tripIndicator);
        requestBody.put("paxInfo", getPaxInfoListJson(opsProduct)); //completed

        JSONArray pricedItineraryArray = new JSONArray();
        pricedItineraryArray.put(matchedPricedItinerary);

        requestBody.put("pricedItinerary", pricedItineraryArray); //completed
        return requestBody;
    }


    public JSONObject getIssueTicketAirRequestBodyJson(OpsBooking opsBooking, OpsProduct opsProduct) {

        JSONObject requestBody = new JSONObject();

        requestBody.put("bookID", opsBooking.getBookID());
        JSONArray suppBookReferncesArr = new JSONArray();
        JSONObject suppbookReference = new JSONObject();
        suppbookReference.put("supplierRef", opsProduct.getSupplierID());
        suppbookReference.put("orderID", opsProduct.getOrderID());
        suppbookReference.put("bookRefID", opsProduct.getOrderDetails().getFlightDetails().getAirlinePNR());
        suppBookReferncesArr.put(suppbookReference);

        requestBody.put("supplierBookReferences", suppBookReferncesArr);
        return requestBody;
    }
}
