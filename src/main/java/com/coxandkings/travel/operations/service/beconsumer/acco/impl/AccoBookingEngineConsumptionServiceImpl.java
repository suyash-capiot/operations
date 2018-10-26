package com.coxandkings.travel.operations.service.beconsumer.acco.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.service.beconsumer.acco.AccoBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.beconsumer.acco.helper.AccoBookingEngineConsumptionHelper;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
class AccoBookingEngineConsumptionServiceImpl implements AccoBookingEngineConsumptionService {
    @Autowired
    private AccoBookingEngineConsumptionHelper accoBookingEngineConsumptionHelper;
    private RestTemplate restTemplate;
    private static Logger logger = LogManager.getLogger(AccoBookingEngineConsumptionServiceImpl.class);
    @Value("${booking-engine-core-services.acco.search}")
    private String accoSearchURL;
    @Value("${booking-engine-core-services.acco.price}")
    private String accoPriceURL;
    @Value("${booking-engine-core-services.acco.reprice}")
    private String accoRePriceURL;
    private String accoSupplierRedisKey;

    @Value("${booking-engine-core-services.acco.modify}")
    private String accoModifyURL;
    private static final String PRODUCT_NAME = "ACCO";

    @Override
    public String search(OpsBooking opsBooking, OpsProduct opsProduct) {
        JSONObject searchRequest = new JSONObject();
        JSONObject requestBody = new JSONObject();
        JSONObject requestHeader = null;
        try {
            requestHeader = accoBookingEngineConsumptionHelper.getRequestHeader(opsBooking);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<JSONObject> accommodationInfoList = new ArrayList<>();
        JSONObject accommodationInfo = new JSONObject();
        try {

            OpsOrderDetails orderDetails = opsProduct.getOrderDetails();
            OpsHotelDetails hotelDetails = orderDetails.getHotelDetails();
            List<OpsRoom> rooms = hotelDetails.getRooms();
            requestBody.put("countryCode", hotelDetails.getCountryCode());
            requestBody.put("cityCode", hotelDetails.getCityCode());
            requestBody.put("hotelCode", hotelDetails.getHotelCode());
            requestBody.put("accommodationSubTypes", new JSONArray().put(opsProduct.getProductSubCategory()));
            requestBody.put("paxNationality", "IN");// todo
            List<JSONObject> roomConfigList = new ArrayList<>();
            for (OpsRoom aopsRoom : rooms) {
                List<OpsAccommodationPaxInfo> paxInfo = aopsRoom.getPaxInfo();
                JSONObject roomConfig = new JSONObject();
                requestBody.put("checkIn", aopsRoom.getCheckIn());
                requestBody.put("checkOut", aopsRoom.getCheckOut());
                Integer paxCount = 0;
                List<Integer> childAges = new ArrayList<>();
                ;
                roomConfig.put("adultCount", setPaxCountAndChildAges(paxCount, childAges, paxInfo));
                roomConfig.put("childAges", childAges);
                roomConfigList.add(roomConfig);
            }
            accommodationInfoList.add(accommodationInfo);
            requestBody.put("roomConfig", roomConfigList);
            searchRequest.put("requestHeader", requestHeader);
            searchRequest.put("requestBody", requestBody);
            restTemplate = RestUtils.getTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<>(searchRequest.toString(), headers);
            ResponseEntity<String> searchResponseEntity = null;

            searchResponseEntity =
                    restTemplate.exchange(this.accoSearchURL, HttpMethod.POST, httpEntity, String.class);

            return searchResponseEntity.getBody();

        } catch (HttpClientErrorException httpClientErrorException) {
            httpClientErrorException.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Integer setPaxCountAndChildAges(Integer paxCount, List<Integer> childAges, List<OpsAccommodationPaxInfo> paxInfo) {
        for (OpsAccommodationPaxInfo opsAccommodationPaxInfo : paxInfo) {
            if (opsAccommodationPaxInfo.getPaxType().equalsIgnoreCase("ADT")) {
                paxCount++;
            }
            if (opsAccommodationPaxInfo.getPaxType().equalsIgnoreCase("CHD")) {
                // CALCULATE CHILD AGE AND SET IN REQUEST
                String dob = opsAccommodationPaxInfo.getBirthDate();
                childAges.add(calculateAge(dob));

            }
        }
        return paxCount;
    }

    @Override
    public String getPrice(OpsBooking opsBooking, OpsProduct opsProduct, String supplierId) {
        JSONObject requestResource = getPriceOrRepriceResource(opsBooking, opsProduct, supplierId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestResource.toString(), headers);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(this.accoPriceURL, HttpMethod.POST, httpEntity, String.class);
        } catch (Exception ex) {

        }
        return responseEntity.getBody();
    }

    @Override
    public String getReprice(OpsBooking opsBooking, OpsProduct opsProduct, String supplierId) throws JSONException {
        JSONObject requestResource = getPriceOrRepriceResource(opsBooking, opsProduct, supplierId);
        logger.info("Calling getRedisKeyForReq method");
        //    this.accoSupplierRedisKey = SupplierDetailAccessor.getRedisKeyForReq(requestResource.getJSONObject("accommodationInfo"));
        //  logger.info("supplierKey-->" + this.accoSupplierRedisKey);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestResource.toString(), headers);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(this.accoRePriceURL, HttpMethod.POST, httpEntity, String.class);
        } catch (Exception ex) {

        }
        return responseEntity.getBody();
    }

    @Override
    public String book(OpsBooking opsBooking, OpsProduct opsProduct, String supplierId) throws JSONException {
        JSONObject requestHeader = accoBookingEngineConsumptionHelper.getRequestHeader(opsBooking);
        JSONObject requestBody = new JSONObject();
        requestBody.put("product", PRODUCT_NAME);
        requestBody.put("bookID", opsBooking.getBookID());
        requestBody.put("accommodationInfo", accoBookingEngineConsumptionHelper.getAccommodationInfoForBookRequest(opsProduct, supplierId, null));
        requestBody.put("paymentInfo", getPaymentInfo(opsBooking.getPaymentInfo()));


        return null;
    }

    @Override
    public String bookFromExistingBooking(OpsBooking opsBooking, OpsProduct opsProduct, String supplierId) throws JSONException, OperationException {
        JSONObject requestResource = new JSONObject();
        JSONObject requestHeader = accoBookingEngineConsumptionHelper.getRequestHeader(opsBooking);
        JSONObject requestBody = new JSONObject();
        requestBody.put("product", PRODUCT_NAME);
        requestBody.put("bookID", opsBooking.getBookID());
        requestBody.put("accommodationInfo", accoBookingEngineConsumptionHelper.getAccommodationInfoForBookRequest(opsProduct, supplierId, null));
        requestBody.put("paymentInfo", new JSONArray());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        requestResource.put("requestHeader", requestHeader);
        requestResource.put("requestBody", requestBody);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestResource.toString(), headers);
        ResponseEntity<String> responseEntity = null;
        String result = null;
        try {
            responseEntity = restTemplate.exchange(this.accoRePriceURL, HttpMethod.POST, httpEntity, String.class);
            result = responseEntity.getBody();

        } catch (Throwable ex) {
            ex.printStackTrace();
            throw new OperationException(Constants.FAILED_TO_BOOK);
        }

        return result;
    }

    @Override
    public Object cancelRoom(OpsBooking opsBooking, OpsProduct opsProduct, String roomId) throws OperationException {

        JSONObject resource = new JSONObject();
        JSONObject requestHeader = accoBookingEngineConsumptionHelper.getRequestHeader(opsBooking);
        JSONObject requestBody = new JSONObject();


        List<JSONObject> accommodationList = new ArrayList<>();
        OpsHotelDetails hotelDetails = opsProduct.getOrderDetails().getHotelDetails();
        JSONObject accommodation = new JSONObject();
        accommodation.put("accommodationSubType", opsProduct.getProductSubCategory());
        accommodation.put("roomId", roomId);
        accommodation.put("modificationType", "CANCELROOM");
        OpsRoom opsRoom = null;
        Optional<OpsRoom> optOpsRoom = opsProduct.getOrderDetails().getHotelDetails().getRooms().stream()
                .filter(room -> room.getRoomID().equalsIgnoreCase(roomId)).findAny();// todo ask ananth do all room contails same checkin and checkout date
        if (!optOpsRoom.isPresent()) {
            throw new OperationException("There is no Room Available for roomId=" + roomId);
        }

        opsRoom = optOpsRoom.get();
        accommodation.put("checkIn", opsRoom.getCheckIn());
        accommodation.put("checkOut", opsRoom.getCheckOut());
        accommodation.put("countryCode", hotelDetails.getCountryCode());
        accommodation.put("cityCode", hotelDetails.getCityCode());
        accommodation.put("supplierCancellationId", opsProduct.getSupplierCancellationId()); // todo
        accommodation.put("supplierRoomIndex", ""); // todo
        accommodation.put("supplierReservationId", opsProduct.getSupplierReservationId());
        accommodation.put("clientReferenceId", opsProduct.getClientReferenceId());


        if (opsProduct.getSupplierID() == null) {
            accommodation.put("supplierRef", opsProduct.getSourceSupplierName());
        } else {
            accommodation.put("supplierRef", opsProduct.getSupplierID());
        }
        accommodation.put("paxInfo", accoBookingEngineConsumptionHelper.getPaxInfo(opsRoom.getPaxInfo(), null));
        accommodation.put("roomInfo", accoBookingEngineConsumptionHelper.getRoomInfo(opsRoom, hotelDetails));
        accommodationList.add(accommodation);
        requestBody.put("accommodationInfo", accommodationList);
        restTemplate = RestUtils.getTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        resource.put("requestHeader", requestHeader);
        resource.put("requestBody", requestBody);

        HttpEntity<String> httpEntity = new HttpEntity<>(resource.toString(), headers);
        ResponseEntity<String> responseEntity = null;
        String result = null;
        try {
            responseEntity = restTemplate.exchange(accoModifyURL, HttpMethod.PUT, httpEntity, String.class);
            result = responseEntity.getBody();
        } catch (Throwable throwable) {

            throw new OperationException(Constants.FAILED_TO_CANCEL_BOOKING);
        }
        return result;
    }


    private JSONArray getPaymentInfo(List<OpsPaymentInfo> paymentInfoList) throws JSONException {
        JSONArray jsonPaymentInfos = new JSONArray();
        try {
            for (OpsPaymentInfo opsPaymentInfo : paymentInfoList) {
                JSONObject paymentInfo = new JSONObject();
                paymentInfo.put("paymentMethod", opsPaymentInfo.getPaymentMethod());
                paymentInfo.put("paymentAmount", opsPaymentInfo.getPaymentAmount());
                paymentInfo.put("amountCurrency", opsPaymentInfo.getAmountCurrency());
                paymentInfo.put("cardType", opsPaymentInfo.getCardType());
                paymentInfo.put("cardNumber", opsPaymentInfo.getCardNumber());
                paymentInfo.put("cardExpiry", opsPaymentInfo.getCardExpiry());
                paymentInfo.put("encryptionKey", opsPaymentInfo.getEncryptionKey());
                paymentInfo.put("token", opsPaymentInfo.getToken());
                paymentInfo.put("accountType", opsPaymentInfo.getAccountType());
                jsonPaymentInfos.put(paymentInfo);

            }
            return jsonPaymentInfos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonPaymentInfos;
    }

    private Integer calculateAge(String dob) {

        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.parse(dob);
        Period period = Period.between(birthday, today);
        System.out.println(period.getDays());
        System.out.println(period.getMonths());
        System.out.println(period.getYears());
        return period.getYears();
    }

    public JSONObject getPriceOrRepriceResource(OpsBooking opsBooking, OpsProduct opsProduct, String supplierId) {
        JSONObject searchRequest = new JSONObject();
        JSONObject requestHeader = null;
        JSONObject requestBody = new JSONObject();
        try {
            requestHeader = accoBookingEngineConsumptionHelper.getRequestHeader(opsBooking);
        } catch (JSONException e) {
            logger.error("Failed to make request Header");
            e.printStackTrace();
        }

        try {

            OpsOrderDetails orderDetails = opsProduct.getOrderDetails();
            OpsHotelDetails hotelDetails = orderDetails.getHotelDetails();
            List<OpsRoom> rooms = hotelDetails.getRooms();
            JSONArray accommodationList = new JSONArray();
            JSONObject accommodationInfo = new JSONObject();
            accommodationInfo.put("countryCode", hotelDetails.getCountryCode());
            accommodationInfo.put("cityCode", hotelDetails.getCityCode());
            accommodationInfo.put("hotelCode", hotelDetails.getHotelCode());
            accommodationInfo.put("accommodationSubType", opsProduct.getProductSubCategory());
            if (StringUtils.isEmpty(supplierId)) {
                //accommodationInfo.put("supplierRef", hotelDetails.getSupplierRefNo());
                accommodationInfo.put("supplierRef",  opsProduct.getSupplierRefNumber() );
            } else {
                accommodationInfo.put("supplierRef", supplierId);
            }
            accommodationInfo.put("paxNationality", "IN"); // todo

            List<JSONObject> roomConfigList = new ArrayList<>();
            for (OpsRoom aopsRoom : rooms) {
                List<OpsAccommodationPaxInfo> paxInfo = aopsRoom.getPaxInfo();
                JSONObject roomConfig = new JSONObject();
                accommodationInfo.put("checkIn", aopsRoom.getCheckIn());
                accommodationInfo.put("checkOut", aopsRoom.getCheckOut());
                // todo
                int paxCount = 0;
                List<Integer> childAges = new ArrayList<>();
                for (OpsAccommodationPaxInfo opsAccommodationPaxInfo : paxInfo) {
                    if (opsAccommodationPaxInfo.getPaxType().equalsIgnoreCase("ADT")) {
                        paxCount++;
                    }
                    if (opsAccommodationPaxInfo.getPaxType().equalsIgnoreCase("CHD")) {
                        // CALCULATE CHILD AGE AND SET IN REQUEST
                        String dob = opsAccommodationPaxInfo.getBirthDate();
                        childAges.add(calculateAge(dob));

                    }
                }
                roomConfig.put("adultCount", paxCount);
                roomConfig.put("childAges", childAges);
                roomConfig.put("roomInfo", accoBookingEngineConsumptionHelper.getRoomInfo(aopsRoom, hotelDetails));
                roomConfigList.add(roomConfig);
            }
            accommodationInfo.put("roomConfig", roomConfigList);
            accommodationList.put(accommodationInfo);
            requestBody.put("accommodationInfo", accommodationList);
            searchRequest.put("requestHeader", requestHeader);
            searchRequest.put("requestBody", requestBody);

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return searchRequest;
    }

    public Object processCancellation(OpsBooking opsBooking, OpsProduct opsProduct) throws OperationException {

        try {
            JSONArray accoInfo = accoBookingEngineConsumptionHelper.getAccoInfoForCancellation(opsProduct);
            JSONObject header = accoBookingEngineConsumptionHelper.getRequestHeader(opsBooking);
            JSONObject completeJsonForAccoCancellation = accoBookingEngineConsumptionHelper.getCompleteJsonForAccoCancellation(header, accoInfo);

            RestTemplate restTemplate = RestUtils.getTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<JSONObject> request = new HttpEntity<>(completeJsonForAccoCancellation, headers);
            ResponseEntity<?> exchange = restTemplate.exchange(accoModifyURL, HttpMethod.POST, request, Object.class);
            Object body = exchange.getBody();
            return body;
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException(Constants.ER909);
        }
    }
}