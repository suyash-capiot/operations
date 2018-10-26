package com.coxandkings.travel.operations.service.booking.impl;

import com.coxandkings.travel.ext.model.be.*;
import com.coxandkings.travel.operations.beconsumer.BEConstants;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.FlightPaxLevelUpdateResource;
import com.coxandkings.travel.operations.model.core.PaxLevelStatus;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.booking.UpdateAccommodationDetailResource;
import com.coxandkings.travel.operations.resource.booking.UpdateFlightDetailResource;
import com.coxandkings.travel.operations.resource.booking.UpdatePaxInfoResource;
import com.coxandkings.travel.operations.service.booking.ManageBookingStatusService;
import com.coxandkings.travel.operations.service.booking.UpdateBookingDetailsService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UpdateBookingDetailsServiceImpl implements BEConstants, UpdateBookingDetailsService {
    private static Logger logger = LogManager.getLogger(UpdateBookingDetailsServiceImpl.class);
    @Autowired
    private UserService userService;

    @Autowired
    private ManageBookingStatusService manageBookingStatus;

    @Value(value = "${booking_engine.update.air.paxInfo}")
    private String updatePaxInfoAir;

    @Value(value = "${booking_engine.update.acco.paxInfo}")
    private String updatePaxInfoAcco;

    @Value(value = "${booking_engine.get.booking}")
    private String getBookingUrl;

    @Value(value = "${booking_engine.update.air.details}")
    private String updateFlightDetails;


    @Value(value = "${booking_engine.update.acco.stayDates}")
    private String updateStayDates;

    @Value( value = "${acco_update.update_supplier_ref}")
    private String updateSupplierReferenceNumberAPIURL;

    @Value( value = "${acco_update.update_acco_ref}")
    private String updateAccoReferenceNumberAPIURL;

    // Constants used by Booking Engine
    public static final String JSON_PROP_SUPPLIERREFERENCEID = "supplierReferenceId";
    public static final String JSON_PROP_ACCO_REFERENCENO = "accoRefNumber";
    public static final String JSON_PROP_ORDERID = "orderID";
    public static final String JSON_PROP_USERID = "userID";

    //===================Update Pax Info==========================//
    @Override
    public MessageResource updatePaxInfo(UpdatePaxInfoResource updatePaxInfoResource) throws OperationException {

        String bookingId = updatePaxInfoResource.getBookId();
        String parameterizedGetBooking = getBookingUrl + bookingId;

        String bookingDetails = RestUtils.getForObject(parameterizedGetBooking, String.class);

        //TODO: FIX this with error message properties
        if (bookingDetails.contains("BE_ERR_001")) {
            throw new OperationException("Failed to get booking for booking id : " + bookingId);
        }

        ObjectMapper objMapper = new ObjectMapper();
        objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Booking booking = null;
        try {
            booking = objMapper.readValue(bookingDetails, Booking.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String orderId = updatePaxInfoResource.getOrderId();
        Optional<Product> matchProduct = booking.getBookingResponseBody().getProducts().stream().
                filter(aProduct -> aProduct.getOrderID().equals(orderId)).findFirst();

        if (!(matchProduct.isPresent())) {
            throw new OperationException("Invalid Order Id: " + orderId);
        }

        Product aMatchProduct = matchProduct.get();
        String productCategory = aMatchProduct.getProductCategory();
        String productSubCategory = aMatchProduct.getProductSubCategory();
        OpsProductCategory opsProductCategory = OpsProductCategory.getProductCategory(
                productCategory);
        if (opsProductCategory.equals(OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION)) {

            OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(
                    OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION, productSubCategory);

            if (opsProductSubCategory.equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS)) {
                return updateAccommodationPaxInfo(aMatchProduct, updatePaxInfoResource);
            }
        }

        if (opsProductCategory.equals(OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION)) {

            OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(
                    OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION, productSubCategory);

            if (opsProductSubCategory.equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT)) {
                return updateFlightPaxInfo(aMatchProduct, updatePaxInfoResource);
            }

        }

        MessageResource messageResource = new MessageResource();

        messageResource.setCode("");
        messageResource.setMessage("Update Failed");

        return messageResource;
    }

    private MessageResource updateFlightPaxInfo(Product aMatchProduct, UpdatePaxInfoResource updatePaxInfoResource)
            throws OperationException {

        if (updatePaxInfoResource.getPaxDetails() != null) {
            try {
                Gson gson = new Gson();
                String json = gson.toJson(updatePaxInfoResource.getPaxDetails(), ArrayList.class);


                JSONArray paxInfoArray = new JSONArray(json);

                for (int i = 0; i < paxInfoArray.length(); i++) {

                    String jsonPaxDetails = paxInfoArray.get(i).toString();
                    JSONObject aOpsPaxDetails = new JSONObject(jsonPaxDetails);

                    JSONArray opsPaxList = aOpsPaxDetails.getJSONArray("paxInfo");

                    int len = opsPaxList.length();
                    int index = 0;
                    List<PaxInfo> paxInfoList = aMatchProduct.getOrderDetails().getPaxInfo();
                    for (int j = 0; j < len; j++) {
                        PaxInfo aPaxInfo = null;
                        JSONObject aOpsPax = (JSONObject) opsPaxList.get(j);

                        if (!(StringUtils.isEmpty(aOpsPax.getString("passengerID")))) {
                            String paxID = aOpsPax.getString("passengerID");
                            Optional<PaxInfo> paxInfoOptional = paxInfoList.stream().filter(aPax -> aPax.getPassengerID().
                                    equalsIgnoreCase(paxID)).findFirst();

                            if (paxInfoOptional.isPresent()) {
                                aPaxInfo = paxInfoOptional.get();

                                if (!(StringUtils.isEmpty(aOpsPax.getString("firstName")))) {
                                    aPaxInfo.setFirstName(aOpsPax.getString("firstName"));
                                }
                                if (!(StringUtils.isEmpty(aOpsPax.getString("lastName")))) {
                                    aPaxInfo.setLastName(aOpsPax.getString("lastName"));
                                }
                                if (!(StringUtils.isEmpty(aOpsPax.getString("title")))) {
                                    aPaxInfo.setTitle(aOpsPax.getString("title"));
                                }
                                if (!(StringUtils.isEmpty(aOpsPax.getString("birthDate")))) {
                                    aPaxInfo.setBirthDate(aOpsPax.getString("birthDate"));
                                }
                            }

                            aPaxInfo.setUserID(userService.getLoggedInUserId());

                            try {
                                RestUtils.put(updatePaxInfoAir, aPaxInfo, String.class);
                            } catch (RestClientException e) {
                                throw new OperationException(String.format("Unable to update PaxInfo for PaxId <%s> ", paxID));
                            }
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                MessageResource messageResource = new MessageResource();
                messageResource.setMessage("Update Failed");
                return messageResource;
            }
        }
        MessageResource messageResource = new MessageResource();
        messageResource.setMessage("Update Successful");
        return messageResource;
    }


    private MessageResource updateAccommodationPaxInfo(Product aMatchProduct, UpdatePaxInfoResource updatePaxInfoResource)
            throws OperationException {

        if (updatePaxInfoResource.getPaxDetails() != null) {

            Gson gson = new Gson();
            String json = gson.toJson(updatePaxInfoResource.getPaxDetails(), ArrayList.class);
            try {
                JSONArray paxInfoArray = new JSONArray(json);

                for (int ptrPaxDetails = 0; ptrPaxDetails < paxInfoArray.length(); ptrPaxDetails++) {
                    JSONObject jsonObject = new JSONObject(paxInfoArray.get(ptrPaxDetails));

                    String jsonPaxDetails = paxInfoArray.get(ptrPaxDetails).toString();
                    JSONObject aOpsPaxDetails = new JSONObject(jsonPaxDetails);

                    String roomId = aOpsPaxDetails.getString("roomId");
                    Optional<Room> matchRoom = aMatchProduct.getOrderDetails().getHotelDetails().getRooms().stream().
                            filter(aRoom -> aRoom.getRoomID().equals(roomId)).findFirst();

                    if (!(matchRoom.isPresent())) {
                        throw new OperationException("Invalid room Id: " + roomId);
                    }

                    JSONArray opsPaxList = aOpsPaxDetails.getJSONArray("paxInfo");

                    int len = opsPaxList.length();
                    int index = 0;
                    List<PaxInfo> paxInfoList = matchRoom.get().getPaxInfo();
                    for (int ptrPaxInfo = 0; ptrPaxInfo < len; ptrPaxInfo++) {
                        JSONObject aOpsPax = (JSONObject) opsPaxList.get(ptrPaxInfo);
                        PaxInfo aPaxInfo = null;

                        if (!(StringUtils.isEmpty(aOpsPax.getString("paxID")))) {
                            String paxID = aOpsPax.getString("paxID");
                            Optional<PaxInfo> paxInfoOptional = paxInfoList.stream().filter(aPax -> aPax.getPaxID().
                                    equalsIgnoreCase(paxID)).findFirst();
                            if (paxInfoOptional.isPresent()) {
                                aPaxInfo = paxInfoOptional.get();

                                if (!(StringUtils.isEmpty(aOpsPax.getString("firstName")))) {
                                    aPaxInfo.setFirstName(aOpsPax.getString("firstName"));
                                }
                                if (!(StringUtils.isEmpty(aOpsPax.getString("lastName")))) {
                                    aPaxInfo.setLastName(aOpsPax.getString("lastName"));
                                }
                                if (!(StringUtils.isEmpty(aOpsPax.getString("title")))) {
                                    aPaxInfo.setTitle(aOpsPax.getString("title"));
                                }
                                if (!(StringUtils.isEmpty(aOpsPax.getString("birthDate")))) {
                                    aPaxInfo.setBirthDate(aOpsPax.getString("birthDate"));
                                }

                                aPaxInfo.setUserID(userService.getLoggedInUserId());

                                try {
                                    RestUtils.put(updatePaxInfoAcco, aPaxInfo, String.class);
                                } catch (RestClientException e) {
                                    throw new OperationException(String.format("Unable to update booking status for PaxId <%s> ", paxID));
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                MessageResource messageResource = new MessageResource();
                messageResource.setMessage("Update Failed");
                return messageResource;
            }
        }
        //TODO : Call Booking Engine Service to update pax Info
        MessageResource messageResource = new MessageResource();
        messageResource.setMessage("Update Sucessfull");
        return messageResource;

    }
    //===================End of Update Pax Info for Air and Acco========================//


    @Override
    public MessageResource updateOriginDestinationInformation(UpdateFlightDetailResource updateFlightDetailResource)
            throws OperationException {

        String bookingId = updateFlightDetailResource.getBookID();

        String parameterizedGetBooking = getBookingUrl + bookingId;

        String bookingDetails = RestUtils.getForObject(parameterizedGetBooking, String.class);

        //TODO: Remove the contains comparision after defining Error message
        if (bookingDetails.contains("BE_ERR_001")) {
            throw new OperationException("Failed to get booking for booking id : " + bookingId);
        }

        ObjectMapper objMapper = new ObjectMapper();
        objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Booking booking = null;
        try {
            booking = objMapper.readValue(bookingDetails, Booking.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (booking == null) {
            throw new OperationException("Failed to get booking for booking id : " + bookingId);
        }

        String orderId = updateFlightDetailResource.getOrderID();
        Optional<Product> matchProduct = booking.getBookingResponseBody().getProducts().stream().
                filter(aProduct -> aProduct.getOrderID().equals(orderId)).findFirst();

        if (!(matchProduct.isPresent())) {
            throw new OperationException("Invalid Order Id: " + orderId);
        }

        FlightDetails flightDetails = matchProduct.get().getOrderDetails().getFlightDetails();
        List<OriginDestinationOption> BEOriginDestinationOptions = flightDetails.getOriginDestinationOptions();

        DateFormat opsFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat beFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        //TODO: Assumption that the order of list will match
        BEOriginDestinationOptions.forEach(aOriginDestinationOption -> {
            aOriginDestinationOption.getFlightSegment().forEach(aFlightSegment -> {
                if (!(StringUtils.isEmpty(updateFlightDetailResource.getArrivalDate()))) {
                    Date date = null;
                    try {
                        date = opsFormat.parse(updateFlightDetailResource.getArrivalDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String beDate = beFormat.format(date);
                    aFlightSegment.setArrivalDate(beDate);
                }
                if (!(StringUtils.isEmpty(updateFlightDetailResource.getDepartureDate()))) {
                    Date date = null;
                    try {
                        date = opsFormat.parse(updateFlightDetailResource.getDepartureDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String beDate = beFormat.format(date);
                    aFlightSegment.setDepartureDate(beDate);
                }
                if (!(StringUtils.isEmpty(updateFlightDetailResource.getAirlineCode()))) {
                    aFlightSegment.getOperatingAirline().setAirlineCode(updateFlightDetailResource.getAirlineCode());
                }
                if (!(StringUtils.isEmpty(updateFlightDetailResource.getFlightNumber()))) {
                    aFlightSegment.getOperatingAirline().setFlightNumber(updateFlightDetailResource.getFlightNumber());
                }
            });
        });

        try {
            JSONObject updateFlightDetailsReq = new JSONObject();
            updateFlightDetailsReq.put(JSON_PROP_ORDERID, orderId);

            ObjectWriter ow = new ObjectMapper().writer();
            JSONObject flightDetailsJSON = new JSONObject(ow.writeValueAsString(flightDetails));

            updateFlightDetailsReq.put(JSON_PROP_AIR_FLIGHTDETAILS, flightDetailsJSON);
            updateFlightDetailsReq.put(JSON_PROP_USERID, userService.getLoggedInUserId());
            logger.info("Flight Details Json : " + flightDetailsJSON);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity httpEntity = new HttpEntity(updateFlightDetailsReq.toString(), headers);
            RestTemplate restTemplate = RestUtils.getTemplate();

            ResponseEntity<String> exchange = restTemplate.exchange(updateFlightDetails,
                    HttpMethod.PUT, httpEntity, String.class);
            String response = exchange.getBody();
            if (response == null) {
                MessageResource messageResource = new MessageResource();
                messageResource.setMessage("Update failed from booking engine");
                return messageResource;
            } else {
                MessageResource messageResource = new MessageResource();
                messageResource.setMessage("Update Successful");
                return messageResource;
            }
        } catch (RestClientException e) {
            throw new OperationException(String.format("Unable to update FlightDetails for orderId <%s> ", orderId));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException(String.format("Unable to update FlightDetails for orderId <%s> ", orderId));
        }

        MessageResource messageResource = new MessageResource();
        messageResource.setMessage(String.format("Unable to update FlightDetails for orderId <%s> ", orderId));
        return messageResource;
    }

    @Override
    public MessageResource updateAccommodationDetails(UpdateAccommodationDetailResource
                                                              updateAccommodationDetailResource) throws OperationException {

        String bookingId = updateAccommodationDetailResource.getBookId();

        String parameterizedGetBooking = getBookingUrl + bookingId;

        String bookingDetails = RestUtils.getForObject(parameterizedGetBooking, String.class);

        if (bookingDetails.contains("BE_ERR_001")) {
            throw new OperationException("Failed to get booking for booking id : " + bookingId);
        }

        ObjectMapper objMapper = new ObjectMapper();
        objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Booking booking = null;
        try {
            booking = objMapper.readValue(bookingDetails, Booking.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (booking == null) {
            throw new OperationException("Failed to map booking to object for booking id : " + bookingId);
        }

        String orderId = updateAccommodationDetailResource.getOrderId();

        Optional<Product> matchProduct = booking.getBookingResponseBody().getProducts().stream().
                filter(aProduct -> aProduct.getOrderID().equals(orderId)).findFirst();

        if (!(matchProduct.isPresent())) {
            throw new OperationException("Invalid Order Id: " + orderId);
        }

        String roomId = updateAccommodationDetailResource.getRoomId();
        String existingSupplierRefNumber = matchProduct.get().getSupplierReferenceId();

        Optional<Room> matchRoom = matchProduct.get().getOrderDetails().getHotelDetails().getRooms().stream().
                filter(aRoom -> aRoom.getRoomID().equals(roomId)).findFirst();

        if (!(matchRoom.isPresent())) {
            throw new OperationException("Invalid room Id: " + orderId);
        }

        //TODO: Extract room details not required for this scenario, remove it
        Room aRoom = matchRoom.get();
        aRoom.setCheckIn(updateAccommodationDetailResource.getCheckInDate());
        aRoom.setCheckOut(updateAccommodationDetailResource.getCheckOutDate());

        try {
            JSONObject updateRoomStayDatesReq = new JSONObject();
            updateRoomStayDatesReq.put(JSON_PROP_ACCO_ROOMID, updateAccommodationDetailResource.getRoomId());
            updateRoomStayDatesReq.put(JSON_PROP_ACCO_CHKIN, updateAccommodationDetailResource.getCheckInDate());
            updateRoomStayDatesReq.put(JSON_PROP_ACCO_CHKOUT,
                    updateAccommodationDetailResource.getCheckOutDate());
            updateRoomStayDatesReq.put(JSON_PROP_USERID, userService.getLoggedInUserId());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity httpEntity = new HttpEntity(updateRoomStayDatesReq.toString(), headers);
            RestTemplate restTemplate = RestUtils.getTemplate();

            restTemplate.exchange(updateStayDates, HttpMethod.PUT, httpEntity, String.class);

        } catch (RestClientException e) {
            throw new OperationException(String.format("Unable to update Accommodation Stay dates for roomId <%s> ", aRoom.getRoomID()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (updateAccommodationDetailResource.getRoomStatus() != null) {
            manageBookingStatus.updateRoomStatus(roomId, updateAccommodationDetailResource.getRoomStatus());
        }

        try {
            if( existingSupplierRefNumber != null && updateAccommodationDetailResource.getSupplierRefNo() != null ) {
                if (!existingSupplierRefNumber.equalsIgnoreCase(updateAccommodationDetailResource.getSupplierRefNo())) {
                    JSONObject updateSupplierRefNoReq = new JSONObject();
                    updateSupplierRefNoReq.put(JSON_PROP_SUPPLIERREFERENCEID, updateAccommodationDetailResource.getSupplierRefNo());
                    updateSupplierRefNoReq.put(JSON_PROP_USERID, userService.getLoggedInUserId());
                    updateSupplierRefNoReq.put(JSON_PROP_ORDERID, updateAccommodationDetailResource.getOrderId());

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity httpEntity = new HttpEntity(updateSupplierRefNoReq.toString(), headers);
                    RestTemplate restTemplate = RestUtils.getTemplate();

                    restTemplate.exchange(updateSupplierReferenceNumberAPIURL, HttpMethod.PUT, httpEntity, String.class);
                }
            }
        }
        catch( Exception e )    {
            throw new OperationException( "Unable to Supplier Reference number " );
        }

        try {
            String newAccoRefNumber = updateAccommodationDetailResource.getAccomodationRefNo();
            if( newAccoRefNumber == null || newAccoRefNumber.trim().length() == 0 )  {
                newAccoRefNumber = ""; //used by clients to reset existing ref number
            }
            JSONObject updateSupplierRefNoReq = new JSONObject();
            updateSupplierRefNoReq.put( JSON_PROP_ACCO_REFERENCENO, newAccoRefNumber );
            updateSupplierRefNoReq.put(JSON_PROP_USERID, userService.getLoggedInUserId());
            updateSupplierRefNoReq.put(JSON_PROP_ORDERID, updateAccommodationDetailResource.getOrderId());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity httpEntity = new HttpEntity(updateSupplierRefNoReq.toString(), headers);
            RestTemplate restTemplate = RestUtils.getTemplate();

            restTemplate.exchange( updateAccoReferenceNumberAPIURL, HttpMethod.PUT, httpEntity, String.class );
        }
        catch( Exception e )    {
            throw new OperationException( "Unable to Accommodation Reference number " );
        }

        MessageResource messageResource = new MessageResource();
        messageResource.setMessage("Accommodation details are updated successfully");
        return messageResource;
    }

    @Override
    public String updateFlightConfirmationDetails(UpdateFlightDetailResource flightDetails) throws OperationException
    {
        UpdateFlightDetailResource flight = new UpdateFlightDetailResource();
        List<FlightPaxLevelUpdateResource> flightPaxUpdateResourceList = null;
        FlightPaxLevelUpdateResource flightPaxResource = null;
        if (flightDetails!=null)
        {
            if (!StringUtils.isEmpty(flightDetails.getBookID()))
            {
                flight.setBookID(flightDetails.getBookID());
            }
            if (!StringUtils.isEmpty(flightDetails.getOrderID())) {
                flight.setOrderID(flightDetails.getOrderID());
            }
            if (!StringUtils.isEmpty(flightDetails.getGdsPNR()))
            {
                flight.setGdsPNR(flightDetails.getGdsPNR());
            }
            if (!StringUtils.isEmpty(flightDetails.getAirlinePNR()))
            {
                flight.setAirlinePNR(flightDetails.getAirlinePNR());
            }
            if (!StringUtils.isEmpty(flightDetails.getSupplierRefNumber())) {
                flight.setSupplierRefNumber(flightDetails.getSupplierRefNumber());
            }
            if (!StringUtils.isEmpty(flightDetails.getPolicyNumber())) {
                flight.setPolicyNumber(flightDetails.getPolicyNumber());
            }

            List<FlightPaxLevelUpdateResource> paxInfo = flightDetails.getPaxInfo();
            if (paxInfo!=null && paxInfo.size()>0)
            {
                flightPaxUpdateResourceList = new ArrayList<>();
                for (FlightPaxLevelUpdateResource resource: paxInfo)
                {
                    flightPaxResource = new FlightPaxLevelUpdateResource();
                    if (!StringUtils.isEmpty(resource.getPassengerID()))
                    {
                        flightPaxResource.setPassengerID(resource.getPassengerID());
                    }
                    if (!StringUtils.isEmpty(resource.getSeatNumber()))
                    {
                        flightPaxResource.setSeatNumber(resource.getSeatNumber());
                    }
                    if (!StringUtils.isEmpty(resource.getStatus()))
                    {
                        flightPaxResource.setStatus(resource.getStatus());
                    }
                    if(!StringUtils.isEmpty(resource.getTicketNumber()))
                    {
                        flightPaxResource.setTicketNumber(resource.getTicketNumber());
                    }
                    flightPaxUpdateResourceList.add(flightPaxResource);
                }

            }
            if (flightPaxUpdateResourceList!=null && flightPaxUpdateResourceList.size()>0)
            {
                flight.setPaxInfo(flightPaxUpdateResourceList);
            }

        }
        String s = manageBookingStatus.updateConfirmationDetails(flight);
        if (s==null)
        {
            throw new OperationException(Constants.OPS_ERR_53000);
        }
        return s;
    }

    @Override
    public List<String> getPaxStatus() {
        List<String> paxStatus = new ArrayList<>();
        for (PaxLevelStatus status: PaxLevelStatus.values())
        {
            paxStatus.add(status.getProductStatus());
        }

        return paxStatus;
    }
}
