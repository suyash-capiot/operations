package com.coxandkings.travel.operations.service.manageproductupdates.impl;

import com.coxandkings.travel.operations.criteria.booking.becriteria.*;
import com.coxandkings.travel.operations.criteria.manageproductupdates.FlightUpdateSearchCriteria;
import com.coxandkings.travel.operations.enums.common.MDMClientType;
import com.coxandkings.travel.operations.enums.manageproductupdates.ChangeType;
import com.coxandkings.travel.operations.enums.manageproductupdates.ConfirmationStatus;
import com.coxandkings.travel.operations.enums.manageproductupdates.UpdatedFlightStatus;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.manageproductupdates.CheaperPriceUpdatesFlightInfo;
import com.coxandkings.travel.operations.model.manageproductupdates.CheaperUpdatesHotelInfo;
import com.coxandkings.travel.operations.model.manageproductupdates.ProductUpdatesFlightSegmentInfo;
import com.coxandkings.travel.operations.model.manageproductupdates.UpdatedFlightInfo;
import com.coxandkings.travel.operations.model.todo.ToDoSubType;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.repository.manageproductupdates.ManageProductUpdatesToDoTaskRepository;
import com.coxandkings.travel.operations.repository.manageproductupdates.UpdatedFlightInfoRepository;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.booking.UpdateFlightDetailResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.manageproductupdates.CustomerOrClientEmailResource;
import com.coxandkings.travel.operations.resource.manageproductupdates.EticketResource;
import com.coxandkings.travel.operations.resource.manageproductupdates.FlightProductSearchResult;
import com.coxandkings.travel.operations.resource.manageproductupdates.ProductUpdateFlightResource;
import com.coxandkings.travel.operations.resource.notification.NotificationResource;
import com.coxandkings.travel.operations.resource.searchviewfilter.BookingSearchResponseItem;
import com.coxandkings.travel.operations.resource.todo.ToDoStatus;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.resource.user.MdmUserInfo;
import com.coxandkings.travel.operations.resource.user.OpsUser;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.beconsumer.air.AirBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.booking.ManageBookingStatusService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.booking.UpdateBookingDetailsService;
import com.coxandkings.travel.operations.service.manageproductupdates.ManageProductUpdatesBEService;
import com.coxandkings.travel.operations.service.manageproductupdates.ManageProductUpdatesMDMService;
import com.coxandkings.travel.operations.service.manageproductupdates.ManageProductUpdatesService;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.CopyUtils;
import com.coxandkings.travel.operations.utils.EmailUtils;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.jayway.jsonpath.JsonPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class ManageProductUpdatesServiceImpl implements ManageProductUpdatesService {
    private static Logger logger = LogManager.getLogger(ManageProductUpdatesServiceImpl.class);

    @Value(value = "${updatesToProductMaster.get-flight-products-by-criteria}")
    private String urlToGetflightProducts;

    @Value(value = "${updatesToProductMaster.function}")
    private String functionName;

    @Value(value = "${updatesToProductMaster.newDetails.scenario}")
    private String newDetailsScenario;

    @Value(value = "${updatesToProductMaster.newDetails.subject}")
    private String newDetailsSubject;

    @Value(value = "${manage-product-updates.mdm.product-details-acco}")
    private String productDetailsURLForAcco;

    @Value(value = "${manage-product-updates.mdm.alertConfig.businessProcess}")
    private String businessProcess;

    @Value(value = "${manage-product-updates.mdm.alertConfig.function}")
    private String function;

    @Value(value = "${manage-product-updates.mdm.alertConfig.alertName}")
    private String alertName;

    @Value(value = "${updatesToProductMaster.updatedMdmDetails.scenario}")
    private String updatedMdmScenario;

    @Value(value = "${updatesToProductMaster.updatedMdmDetails.subject}")
    private String updatedMdmSubject;

    @Value(value = "${manage-product-updates.eticket.generate}")
    private String eticketUrl;

    @Value(value = "${manage-product-updates.eticket.download}")
    private String downloadUrl;

    @Value(value = "${updatesToProductMaster.eticket.scenario}")
    private String eticketScenario;

    @Value(value = "${updatesToProductMaster.eticket.subject}")
    private String eticketSubject;

    @Autowired
    private ManageBookingStatusService manageBookingStatusService;

    @Autowired
    private ManageProductUpdatesBEService beDataLoader;

    @Autowired
    private ManageProductUpdatesToDoTaskRepository toDoTaskRepository;

    @Autowired
    private ManageProductUpdatesMDMService mdmService;

    @Autowired
    private UserService userService;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    @Qualifier(value = "mDMToken")
    MDMToken mdmToken; //Token used by System to connect to MDM

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private UpdateBookingDetailsService updateBookingDetailsService;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private ClientMasterDataService clientMasterDataService;

    @Autowired
    private AirBookingEngineConsumptionService airBookingEngineConsumptionService;

    @Autowired
    private UpdatedFlightInfoRepository updatedFlightInfoRepository;

    @Autowired
    private AlertService alertService;


    @Override
    public void handleProductUpdates() throws OperationException {
        handleProductUpdatesForFlights();
        handleProductUpdatesForHotels();
    }


    /**
     * Purpose of this method is to get the flight product details
     * based on the criteria.
     *
     * @return
     */
    @Override
    public FlightProductSearchResult getFlightProductsByCriteria(FlightUpdateSearchCriteria criteria) {
        OpsUser loggedInUser = userService.getLoggedInUser();
        criteria.setCompanyId(loggedInUser.getCompanyId());
        ResponseEntity<FlightProductSearchResult> result = null;
        HttpEntity<FlightUpdateSearchCriteria> requestEntity = new HttpEntity<>(criteria);
        try {
            result = RestUtils.exchange(urlToGetflightProducts, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<FlightProductSearchResult>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Error while retrieving flight products by criteria from BE" + e);
        }
        return result.getBody();
    }


    /**
     * Purpose of this method is to send email to all the customers.
     *
     * @param emailResourceList
     * @return
     */
    public Map<String, List<String>> sendEmailToClientOrCustomer(List<CustomerOrClientEmailResource> emailResourceList) throws OperationException {
        List<String> failedList = new ArrayList<>();
        List<String> passedList = new ArrayList<>();
        Map<String, List<String>> resultedListMap = new HashMap<>();
        EmailResponse emailResponse = null;
        for (CustomerOrClientEmailResource aResource : emailResourceList) {
            OpsBooking opsBooking = opsBookingService.getBooking(aResource.getBookId());
            if (opsBooking != null && opsBooking.getClientID() != null && opsBooking.getClientType() != null) {
                String clientEmailId = clientMasterDataService.getClientEmailId(opsBooking.getClientID(), MDMClientType.fromString(opsBooking.getClientType()));
                if (clientEmailId != null) {
                    Map<String, String> dynamicVariables = new HashMap<>();
                    dynamicVariables.put("booking_ref_number", aResource.getBookId());
                    dynamicVariables.put("order_id", aResource.getOrderId());
                    dynamicVariables.put("flight_number", aResource.getFlightNumber());
                    dynamicVariables.put("departure_date", aResource.getDepartureDateAndTime());
                    dynamicVariables.put("arrival_date", aResource.getArrivalDateAndTime());
                    try {
                        emailResponse = emailUtils.buildClientMail(functionName,
                                newDetailsScenario,
                                clientEmailId,
                                newDetailsSubject,
                                dynamicVariables, null, null);
                        passedList.add(clientEmailId);
                        resultedListMap.put("passed", passedList);
                    } catch (Exception e) {
                        logger.info("Email is not sent to customer/client " + clientEmailId);
                        failedList.add(clientEmailId);
                        resultedListMap.put("failed", failedList);
                    }
                }
            }
        }
        return resultedListMap;
    }

    /**
     * Purpose of this method is to update the flight details based on below conditions.
     * 1)When Flight Number changes
     * 2)When Flight Time Changes
     * 3)When Flight Number And Flight Time changes
     *
     * @param resourceList
     * @return
     * @throws OperationException
     */
    @Override
    public Map<String, List<String>> updateFlightInfo(List<ProductUpdateFlightResource> resourceList) throws OperationException {
        List<UpdateFlightDetailResource> failedList = new ArrayList<>();
        List<UpdateFlightDetailResource> passedList = new ArrayList<>();
        Map<String, List<String>> resultedListMap = new HashMap<>();
        List<String> resultPassesList = new ArrayList<>();
        List<String> resultFailedList = new ArrayList<>();
        for (ProductUpdateFlightResource aProductUpdateResource : resourceList) {
            if (aProductUpdateResource.getConfirmationStatus().contains("Pending")) {
                UpdatedFlightInfo updatedFlightInfo = updatedFlightInfoRepository.getUpdatedFlightInfoByCriteria(aProductUpdateResource.getBookingID(), aProductUpdateResource.getOrderID());
                if (updatedFlightInfo == null) {
                    updatedFlightInfo = updatedFlightInfoRepository.getById(aProductUpdateResource.getId());
                }
                updatedFlightInfo.setConfirmationStatus("Pending");
                updatedFlightInfoRepository.saveUpdatedFlightInfo(updatedFlightInfo);
            }
            if (aProductUpdateResource.getConfirmationStatus().contains("Rejected")) {
                UpdatedFlightInfo updatedFlightInfo = updatedFlightInfoRepository.getUpdatedFlightInfoByCriteria(aProductUpdateResource.getBookingID(), aProductUpdateResource.getOrderID());
                if (updatedFlightInfo == null) {
                    updatedFlightInfo = updatedFlightInfoRepository.getById(aProductUpdateResource.getId());
                }
                updatedFlightInfo.setConfirmationStatus("Rejected");
                updatedFlightInfoRepository.saveUpdatedFlightInfo(updatedFlightInfo);
            }
            if (aProductUpdateResource.getConfirmationStatus().contains("Confirmed")) {
                //update when flight number changes
                if (aProductUpdateResource.getChangeType().toUpperCase().equals(String.valueOf(ChangeType.FLIGHT_NUMBER))) {
                    ChangeType.valueOf(aProductUpdateResource.getChangeType().toUpperCase()).process(aProductUpdateResource);
                    UpdateFlightDetailResource updateFlightDetailResource = new UpdateFlightDetailResource();
                    updateFlightDetailResource.setBookID(aProductUpdateResource.getBookingID());
                    updateFlightDetailResource.setOrderID(aProductUpdateResource.getOrderID());
                    updateFlightDetailResource.setFlightNumber(aProductUpdateResource.getFlightNumber());
                    try {
                        MessageResource messageResource = updateBookingDetailsService.updateOriginDestinationInformation(updateFlightDetailResource);
                        updateFlightDetailResource.setId(aProductUpdateResource.getId());
                        passedList.add(updateFlightDetailResource);
                        resultPassesList.add(updateFlightDetailResource.getBookID());
                        resultedListMap.put("passed", resultPassesList);
                    } catch (OperationException ex) {
                        failedList.add(updateFlightDetailResource);
                        resultFailedList.add(updateFlightDetailResource.getBookID());
                        resultedListMap.put("failed", resultFailedList);
                    }
                }
                //update when flight time changes
                else if (aProductUpdateResource.getChangeType().toUpperCase().equals(String.valueOf(ChangeType.FLIGHT_TIME))) {
                    ChangeType.valueOf(aProductUpdateResource.getChangeType().toUpperCase()).process(aProductUpdateResource);
                    UpdateFlightDetailResource updateFlightDetailResource = new UpdateFlightDetailResource();
                    updateFlightDetailResource.setBookID(aProductUpdateResource.getBookingID());
                    updateFlightDetailResource.setOrderID(aProductUpdateResource.getOrderID());
                    updateFlightDetailResource.setDepartureDate(aProductUpdateResource.getDepartureDateAndTime());
                    updateFlightDetailResource.setArrivalDate(aProductUpdateResource.getArrivalDateAndTime());
                    try {
                        updateBookingDetailsService.updateOriginDestinationInformation(updateFlightDetailResource);
                        updateFlightDetailResource.setId(aProductUpdateResource.getId());
                        passedList.add(updateFlightDetailResource);
                        resultPassesList.add(updateFlightDetailResource.getBookID());
                        resultedListMap.put("passed", resultPassesList);
                    } catch (OperationException ex) {
                        failedList.add(updateFlightDetailResource);
                        resultFailedList.add(updateFlightDetailResource.getBookID());
                        resultedListMap.put("failed", resultFailedList);
                    }
                }
                //update when flight number and time changes
                else if (aProductUpdateResource.getChangeType().toUpperCase().equals(String.valueOf(ChangeType.FLIGHT_NUMBER_AND_TIME))) {
                    ChangeType.valueOf(aProductUpdateResource.getChangeType().toUpperCase()).process(aProductUpdateResource);
                    UpdateFlightDetailResource updateFlightDetailResource = new UpdateFlightDetailResource();
                    updateFlightDetailResource.setBookID(aProductUpdateResource.getBookingID());
                    updateFlightDetailResource.setOrderID(aProductUpdateResource.getOrderID());
                    updateFlightDetailResource.setFlightNumber(aProductUpdateResource.getFlightNumber());
                    logger.info("UI departure date: " + aProductUpdateResource.getDepartureDateAndTime());
                    updateFlightDetailResource.setDepartureDate(aProductUpdateResource.getDepartureDateAndTime());
                    logger.info("UI Arrival date: " + aProductUpdateResource.getArrivalDateAndTime());
                    updateFlightDetailResource.setArrivalDate(aProductUpdateResource.getArrivalDateAndTime());
                    try {
                        MessageResource messageResource = updateBookingDetailsService.updateOriginDestinationInformation(updateFlightDetailResource);
                        updateFlightDetailResource.setId(aProductUpdateResource.getId());
                        passedList.add(updateFlightDetailResource);
                        resultPassesList.add(updateFlightDetailResource.getBookID());
                        resultedListMap.put("passed", resultPassesList);
                    } catch (OperationException ex) {
                        updateFlightDetailResource.setId(aProductUpdateResource.getId());
                        failedList.add(updateFlightDetailResource);
                        resultFailedList.add(updateFlightDetailResource.getBookID());
                        resultedListMap.put("failed", resultFailedList);
                    }
                } else {
                    throw new OperationException("Criteria Did Not Match");
                }
            }

        }
        if (passedList.size() > 0) {
            List<UpdatedFlightInfo> updatedInfoList = this.updateFlightInfoStatus(passedList);
            for (UpdatedFlightInfo updatedFlightInfo : updatedInfoList) {
                updatedFlightInfo.setUpdatedFlightStatus(UpdatedFlightStatus.UPDATED.getStatus());
                updatedFlightInfo.setConfirmationStatus("Confirmed");
                updatedFlightInfoRepository.saveUpdatedFlightInfo(updatedFlightInfo);
            }
        }

        return resultedListMap;
    }

    private List<UpdatedFlightInfo> updateFlightInfoStatus(List<UpdateFlightDetailResource> passedList) {
        List<UpdatedFlightInfo> updatedInfoList = new ArrayList<>();
        for (UpdateFlightDetailResource aResource : passedList) {
            UpdatedFlightInfo updatedInfo = updatedFlightInfoRepository.getUpdatedFlightInfoByCriteria(aResource.getBookID(), aResource.getOrderID());
            if (updatedInfo == null) {
                updatedInfo = updatedFlightInfoRepository.getById(aResource.getId());
            }
            updatedInfoList.add(updatedInfo);
        }
        return updatedInfoList;
    }

    /**
     * @param aFlightBookingInfo
     * @param searchResults
     * @return
     */
    public boolean compareWithCheaperPriceUpdatesFlightInfo(CheaperPriceUpdatesFlightInfo aFlightBookingInfo, String searchResults) {
        try {
            boolean isMatchedCriteria = false;
            boolean isMatchAmtAndCurrency = false;
            String supplierCurrency = aFlightBookingInfo.getSupplierCurrency();
            Double supplierPrice = aFlightBookingInfo.getSupplierPrice();
            List<ProductUpdatesFlightSegmentInfo> flightSegts = aFlightBookingInfo.getFlightSegments();
            for (ProductUpdatesFlightSegmentInfo flightSegmentsInfo : flightSegts) {
                JSONObject parentObject = new JSONObject(searchResults);
                JSONObject responseBody = parentObject.getJSONObject("responseBody");

                JSONArray pricedItinerarys = responseBody.getJSONArray("pricedItinerary");

                pricedItinerarys:
                for (int i = 0; i < pricedItinerarys.length(); i++) {

                    JSONObject pricedItinerary = pricedItinerarys.getJSONObject(i);
                    JSONObject airItinerary = pricedItinerary.getJSONObject("airItinerary");
                    JSONArray originDestinationOptions = airItinerary.getJSONArray("originDestinationOptions");
                    JSONObject airItineraryPricingInfo = pricedItinerary.getJSONObject("airItineraryPricingInfo");
                    JSONObject itinTotalFare = airItineraryPricingInfo.getJSONObject("itinTotalFare");

                    Double amount = itinTotalFare.getDouble("amount");
                    String currencyCode = itinTotalFare.getString("currencyCode");

                    originDestinationOptions:
                    for (int j = 0; j < originDestinationOptions.length(); j++) {

                        JSONObject jsonObject = originDestinationOptions.getJSONObject(j);
                        JSONArray flightSegments = jsonObject.getJSONArray("flightSegment");

                        flightSegments:
                        for (int k = 0; k < flightSegments.length(); k++) {
                            JSONObject flightSegment = flightSegments.getJSONObject(k);
                            String cabinTypeF = flightSegment.getString("cabinType");
                            String departureDateF = flightSegment.getString("departureDate");
                            String originLocationF = flightSegment.getString("originLocation");
                            String destinationLocationF = flightSegment.getString("destinationLocation");

                            String cabinType = flightSegmentsInfo.getCabintype();
                            String departureDate = flightSegmentsInfo.getDepartureDate();
                            String destinationLocation = flightSegmentsInfo.getDestinationLocation();
                            String originLocation = flightSegmentsInfo.getOriginLocation();

                            if (cabinType.equalsIgnoreCase(cabinTypeF) && departureDate.equalsIgnoreCase(departureDateF)
                                    && destinationLocation.equalsIgnoreCase(destinationLocationF) && originLocation.equalsIgnoreCase(originLocationF)) {
                                isMatchedCriteria = true;
                                break;
                            }
                        }
                        break;
                    }
                    if (amount < supplierPrice && currencyCode.equalsIgnoreCase(supplierCurrency)) {
                        isMatchAmtAndCurrency = true;
                    }

                    if (isMatchedCriteria && isMatchAmtAndCurrency) {
                        return true;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Error occured while compareWithCheaperPriceUpdatesFlightInfo");
            return false;
        }
        return false;
    }

    /**
     * The purpose of this method is to do the following:
     * 1) Call BE's APIs to get the List of Flight Booking2 info to check for AbstractProductFactory updates
     * 2) Using the Booking2 Information details, go to MDM and whether cheaper prices are available from offline Supplier
     * 3) If available, call ToDoTask repository to check if existing ToDOTask is created already for the Booking2
     * 4) If its not created, create a new ToDOTask for OpsUser
     * 5) If its created, simply return
     *
     * @throws OperationException
     */

    public void handleProductUpdatesForFlights() throws OperationException {
        try {
            // Step 1: Get Bookings from BE
            List<CheaperPriceUpdatesFlightInfo> flightBookings = beDataLoader.getFlightBookingsForProductUpdates();

            // Step 2: Do we have Bookings list > 0
            if (flightBookings == null || flightBookings.size() == 0) {

            } else {
                // Step 3:
                // Call BE.search() API using ClientID from YML file
                // Get the Search Results
                // Compare prices of Search Result with CheaperPriceUpdatesFlightInfo
                // If Search Result entry is cheaper, then create To Do task (done below)
                // Go to ToDoTask and fetch existing ToDoTasks (for that Booking2 & Supplier) which are NOT in CLOSED state for Operations &&
                // are of sub type AbstractProductFactory Updates
                ToDoSubType subType = new ToDoSubType();
                subType.setId("CHEAPER_PRICE_UPDATES");
                ToDoStatus aToDoStatus = new ToDoStatus();
                aToDoStatus.setId("Closed");
                List<String> alreadyAvailableFollowupTasksList = null;
                try {
                    // Fetch from ToDoTask Table
                    alreadyAvailableFollowupTasksList = toDoTaskRepository.getToDoTasksBySubType(subType, aToDoStatus);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Get User Info for system user from MDM Token
                MdmUserInfo mdmUser = userService.createUserDetailsFromToken(mdmToken.getToken());
                OpsUser aOpsUser = userService.getOpsUser(mdmUser);
                String userID = aOpsUser.getUserID();

                // Step 4: Check whether a Booking2's supplier price is cheaper in MDM
                for (CheaperPriceUpdatesFlightInfo aFlightBookingInfo : flightBookings) {

                    OpsBooking aBooking = opsBookingService.getBooking(aFlightBookingInfo.getBookID());
                    OpsProduct aProduct = opsBookingService.getProduct(aFlightBookingInfo.getBookID(), aFlightBookingInfo.getOrderID());

                    String searchResults = null;
                    try {
                        searchResults = airBookingEngineConsumptionService.search(aBooking, aProduct);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("unable to get airBookingEngineConsumptionService.search results ", e);
                    }
                    boolean status = false;
                    if (searchResults != null) {
                        status = this.compareWithCheaperPriceUpdatesFlightInfo(aFlightBookingInfo, searchResults);
                    }
                    boolean isCheaperPriceAvailable = mdmService.isCheaperPriceAvailable(aFlightBookingInfo);
                    // Step 4: If its cheaper, then create a ToDoTask (if its not created already for sub type CHEAPER_PRICE_UPDATES)
                    if (status) {
                        // Check a ToDoTask is arelady created, in that case, do nothing
                        // Otherwise, create a new ToDoTask for OpsUser
                        String bookID = aFlightBookingInfo.getBookID();
                        if (!alreadyAvailableFollowupTasksList.contains(bookID)) {

                            // Create a ToDoTask
                            ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
                            toDoTaskResource.setBookingRefId(aBooking.getBookID());
                            try {
                                toDoTaskResource.setCreatedByUserId(this.userService.getLoggedInUserId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //Its not possible to use specific Product/Order as payment is made for Booking2
                            toDoTaskResource.setProductId(aProduct.getOrderID());
                            toDoTaskResource.setReferenceId("213847323");
                            //toDoTaskResource.setClientTypeId(aBooking.getClientType());
                            //toDoTaskResource.setCompanyId(aBooking.getCompanyId());
                            //toDoTaskResource.setClientId(aBooking.getClientID());
                            //toDoTaskResource.setCompanyMarketId("");

                            toDoTaskResource.setAssignedBy(userID);
                            toDoTaskResource.setClientCategoryId("");
                            toDoTaskResource.setClientSubCategoryId("");
                            toDoTaskResource.setTaskNameId(ToDoTaskNameValues.UPDATE.getValue());
                            toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
                            toDoTaskResource.setTaskPriorityId(ToDoTaskPriorityValues.MEDIUM.getValue());
                            toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.CHEAPER_PRICE_UPDATES.toString()); //PART_PAYMENT_BOOKING
                            toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue()); // OPERATIONS or FINANCE
                            //toDoTaskResource.setTaskStatusId(ToDoTaskStatusValues.ASSIGNED.getValue());
                            toDoTaskResource.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.name());
                            //TODO : This should be from KPI Master
                            ZonedDateTime dueDateTime = java.time.ZonedDateTime.now().plusDays(5);
                            String dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSz";
                            //toDoTaskResource.setDueOnDate(DateTimeFormatter.ofPattern(dateTimePattern).format(dueDateTime));
                            try {
                                toDoTaskService.save(toDoTaskResource);
                            } catch (Exception e) {
                                e.printStackTrace();
                                logger.error("unable to create to do task", e);
                            }
                        }
                    } else {
                        // return doing nothing!
                        logger.info("Cheaper price for Booking2 ID: " + aFlightBookingInfo.getBookID() + " and Order ID: " +
                                aFlightBookingInfo.getOrderID() + " is unavailable");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred while processing Manage Updates Batch Job for Flights", e);
            e.printStackTrace();
            //TODO use appropriate error code to log
            throw new OperationException();
        }
    }

    @Override
    public List<UpdatedFlightInfo> saveUpdatedFlightInfo(List<ProductUpdateFlightResource> resourceList) {
        List<UpdatedFlightInfo> updatedFlightInfos = new ArrayList<>();
        List<CustomerOrClientEmailResource> emailResourceList = new ArrayList<>();
        for (ProductUpdateFlightResource productUpdateFlightResource : resourceList) {
            UpdatedFlightInfo updatedFlightInfo = new UpdatedFlightInfo();
            CopyUtils.copy(productUpdateFlightResource, updatedFlightInfo);
            updatedFlightInfo.setUpdatedFlightStatus(UpdatedFlightStatus.NEW.getStatus());
            updatedFlightInfo.setConfirmationStatus(ConfirmationStatus.PENDING.getStatus());
            updatedFlightInfo = updatedFlightInfoRepository.saveUpdatedFlightInfo(updatedFlightInfo);
            updatedFlightInfos.add(updatedFlightInfo);

            //auto send email
            CustomerOrClientEmailResource emailResource = new CustomerOrClientEmailResource();
            emailResource.setBookId(productUpdateFlightResource.getBookingID());
            emailResource.setOrderId(productUpdateFlightResource.getOrderID());
            emailResource.setArrivalDateAndTime(productUpdateFlightResource.getArrivalDateAndTime());
            emailResource.setDepartureDateAndTime(productUpdateFlightResource.getDepartureDateAndTime());
            emailResource.setFlightNumber(productUpdateFlightResource.getFlightNumber());
            emailResourceList.add(emailResource);
        }
        try {
            sendEmailToClientOrCustomer(emailResourceList);
        } catch (OperationException e) {
            logger.info("Error while sending mail" + e);
        }
        return updatedFlightInfos;
    }

    @Override
    public Map<String, Object> getUpdatedFlightInfo(Integer size, Integer page) {
        size = (size == null) ? 10 : size;
        page = (page == null) ? 1 : page;
        return updatedFlightInfoRepository.getUpdatedFlightInfo(size, page);
    }

    @Override
    public Map<String, List<String>> generateEticket(List<EticketResource> eticketResource) throws OperationException {
        List<String> failedList = new ArrayList<>();
        List<String> passedList = new ArrayList<>();
        Map<String, List<String>> resultedListMap = new HashMap<>();

        MessageResource messageResource = new MessageResource();
        for (EticketResource resource : eticketResource) {
            //download eticket
            URI uri = null;
            try {
                uri = new URI(downloadUrl + resource.getBookID());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_PDF));
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

            ResponseEntity<String> eticketResponse = null;
            ResponseEntity<byte[]> downloadResponse = null;
            try {
                //e-ticket generated
                eticketResponse = RestUtils.exchange(eticketUrl + resource.getBookID(), HttpMethod.GET, null, String.class);
                //download e-ticket
                downloadResponse = mdmRestUtils.exchange(uri, HttpMethod.GET, entity, byte[].class);
                passedList.add(resource.getBookID());
                resultedListMap.put("passed", passedList);
            } catch (Exception e) {
                logger.error("Error while generating pdf: " + e);
                failedList.add(resource.getBookID());
                resultedListMap.put("failed", failedList);
                e.printStackTrace();
            }
            byte[] body = downloadResponse.getBody();

            OpsBooking opsBooking = opsBookingService.getBooking(resource.getBookID());
            if (opsBooking != null) {
                //send email to customer
                String clientEmailId = clientMasterDataService.getClientEmailId(opsBooking.getClientID(), MDMClientType.fromString(opsBooking.getClientType()));
                if (clientEmailId != null) {
                    try {
                        //todo email not working fix tommarrow
                        emailUtils.buildClientMail(functionName, eticketScenario, clientEmailId, eticketSubject, null, "eTicket.pdf", body);
                    } catch (Exception e) {
                        logger.error("Error while sending eticket to customer :" + clientEmailId);
                        e.printStackTrace();
                    }
                }
            }
        }
        return resultedListMap;
    }

    /**
     * The purpose of this method is to do the following:
     * 1) Call BE's APIs to get the List of Flight Booking2 info to check for Product updates
     * 2) Using the Booking2 Information details, go to MDM and whether cheaper prices are available from offline Supplier
     * 3) If available, call ToDoTask repository to check if existing ToDOTask is created already for the Booking2
     * 4) If its not created, create a new ToDOTask for OpsUser
     * 5) If its created, simply return
     *
     * @throws OperationException
     */
    private void handleProductUpdatesForHotels() throws OperationException {
        try {
            // Step 1: Get Bookings from BE
            List<CheaperUpdatesHotelInfo> hotelBookings = beDataLoader.getHotelBookingsForProductUpdates();

            // Step 2: Do we have Bookings list > 0
            if (hotelBookings == null || hotelBookings.size() == 0) {

            } else {
                // Step 3:
                // Go to ToDoTask and fetch existing ToDoTasks (for that Booking2 & Supplier) which are NOT in CLOSED state for Operations &&
                // are of sub type Product Updates
                ToDoSubType subType = new ToDoSubType();
                subType.setId("CHEAPER_PRICE_UPDATES");

                ToDoStatus aToDoStatus = new ToDoStatus();
                aToDoStatus.setId("Closed");

                // Fetch from ToDoTask Table
                List<String> alreadyAvailableFollowupTasksList = toDoTaskRepository.getToDoTasksBySubType(subType, aToDoStatus);

                // Get User Info for system user from MDM Token
                MdmUserInfo mdmUser = userService.createUserDetailsFromToken(mdmToken.getToken());
                OpsUser aOpsUser = userService.getOpsUser(mdmUser);
                String userID = aOpsUser.getUserID();

                // Step 4: Check whether a Booking2's supplier price is cheaper in MDM
                for (CheaperUpdatesHotelInfo aHotelBookingInfo : hotelBookings) {

                    boolean isCheaperPriceAvailable = mdmService.isCheaperPriceAvailable(aHotelBookingInfo);

                    // Step 4: If its cheaper, then create a ToDoTask (if its not created already for sub type CHEAPER_PRICE_UPDATES)
                    if (isCheaperPriceAvailable) {
                        // Check a ToDoTask is arelady created, in that case, do nothing
                        // Otherwise, create a new ToDoTask for OpsUser
                        String bookID = aHotelBookingInfo.getBookID();
                        if (!alreadyAvailableFollowupTasksList.contains(bookID)) {
                            // Create a ToDoTask

                            ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
                            toDoTaskResource.setBookingRefId(bookID);
                            try {
                                toDoTaskResource.setCreatedByUserId(this.userService.getLoggedInUserId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            toDoTaskResource.setProductId(aHotelBookingInfo.getOrderID());
                            toDoTaskResource.setReferenceId("2345");
                            //toDoTaskResource.setClientTypeId("");
                            //toDoTaskResource.setCompanyId("");
                            //toDoTaskResource.setCompanyMarketId("");
                            toDoTaskResource.setTaskNameId(ToDoTaskNameValues.UPDATE.getValue());
                            toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
                            toDoTaskResource.setTaskPriorityId(ToDoTaskPriorityValues.MEDIUM.getValue());
                            toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.CHEAPER_PRICE_UPDATES.toString());
                            toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
                            toDoTaskResource.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());

                            //TODO : This should be from KPI Master
                            ZonedDateTime dueDateTime = java.time.ZonedDateTime.now().plusDays(5);
                            String dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSz";
                            //toDoTaskResource.setDueOnDate(DateTimeFormatter.ofPattern(dateTimePattern).format(dueDateTime));
                            toDoTaskService.save(toDoTaskResource);

                        }
                    } else {
                        // return doing nothing!
                        logger.info("Cheaper price for Booking2 ID: " + aHotelBookingInfo.getBookID() + " and Order ID: " +
                                aHotelBookingInfo.getOrderID() + " is unavailable");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred while processing Manage Updates Batch Job for Flights", e);
            e.printStackTrace();
            //TODO use appropriate error code to log
            throw new OperationException();
        }
    }

    private ToDoTask createToDo(OpsBooking opsBooking, OpsProduct opsProduct) {
        ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
        toDoTaskResource.setReferenceId(opsBooking.getBookID());
        toDoTaskResource.setCreatedByUserId(opsBooking.getUserID());
        toDoTaskResource.setAssignedBy(opsBooking.getUserID());

        toDoTaskResource.setClientCategoryId("");
        toDoTaskResource.setProductId(opsProduct.getOrderID());
        toDoTaskResource.setClientSubCategoryId("");
        toDoTaskResource.setClientTypeId(opsBooking.getClientID());
        toDoTaskResource.setCompanyId(opsBooking.getCompanyId());
        toDoTaskResource.setCompanyMarketId("");

        toDoTaskResource.setTaskNameId("UPDATE");
        toDoTaskResource.setTaskTypeId("Main");
        toDoTaskResource.setTaskSubTypeId("UPDATES_IN_PRODUCT_MASTER");
        toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
        toDoTaskResource.setTaskStatusId(ToDoTaskStatusValues.ASSIGNED.getValue());
        toDoTaskResource.setTaskPriorityId("High");
        toDoTaskResource.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
        toDoTaskResource.setFileHandlerId("UPDATES_IN_PRODUCT_MASTER");// TODO

        toDoTaskResource.setSuggestedActions("");//sent the rest end point for now
        //TODO : This should be from KPI Master
        toDoTaskResource.setDueOnDate(ZonedDateTime.now());
        try {
            ToDoTask toDoDetails = toDoTaskService.save(toDoTaskResource);
            return toDoDetails;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error occurred while processing createToDo", e);
        }
        return null;
    }

    private void action(String accommodationDetails, OpsBooking opsBooking, OpsProduct opsProduct, String emailId, String mobileNo) {
        Boolean sendUpdates = JsonPath.parse(accommodationDetails).read("$.updates[0].sendUpdates", Boolean.class);//boolean
        if (sendUpdates != null && sendUpdates.equals(Boolean.TRUE)) {
            Boolean email = JsonPath.parse(accommodationDetails).read("$.updates[0].modeOfCommunication.email", Boolean.class);//boolean

            if (email != null && email.equals(Boolean.TRUE))
                this.sendCommunicationViaEmail(opsBooking, opsProduct, emailId, accommodationDetails);

//                    if (email != null && email.equals(Boolean.FALSE))
//                        createAlertForOpsUser(opsBooking);

            Boolean phone = JsonPath.parse(accommodationDetails).read("$.updates[0].modeOfCommunication.phone", Boolean.class);//boolean
            if (phone != null && phone.equals(Boolean.TRUE))
                this.sendCommunicationViaSMS(opsBooking, opsProduct, mobileNo);

//                    if (phone != null && phone.equals(Boolean.FALSE))
//                        createAlertForOpsUser(opsBooking);

            //todo :Need to think about it
            Boolean external = JsonPath.parse(accommodationDetails).read("$.updates[0].descriptionType.external", Boolean.class);//boolean
            Boolean internal = JsonPath.parse(accommodationDetails).read("$.updates[0].descriptionType.internal", Boolean.class);//boolean

        } else {
            createAlertForOpsUser(opsBooking);
        }
    }

    private void sendCommunicationViaEmail(OpsBooking opsBooking, OpsProduct opsProduct, String email, String accommodationDetails) {
        String productCategory = JsonPath.parse(accommodationDetails).read("$.accomodationInfo.productCatSubType", String.class);//boolean
        Map<String, String> dynamicVariables = new HashMap<>();
        dynamicVariables.put("bookID", opsBooking.getBookID());
        dynamicVariables.put("companyID", opsBooking.getCompanyId());
        dynamicVariables.put("productCategory", productCategory);
        try {
            emailUtils.buildClientMail(functionName,
                    updatedMdmScenario,
                    email,
                    updatedMdmSubject,
                    dynamicVariables, null, null);
        } catch (OperationException e) {
            logger.error("Error while sending mail " + email);
        }

        this.createAlertForOpsUser(opsBooking);
    }

    private void sendCommunicationViaSMS(OpsBooking opsBooking, OpsProduct opsProduct, String mobileNo) {
        this.createAlertForOpsUser(opsBooking);
    }

    //TODO : ACCORDING TO DOCUMENT THIS IS ONLY APPLICABLE FOR ACCO , CRUISE AND ACTIVITIES
    @Override
    public void alertManageProductUpdates(String payload) {
        try {
            JSONObject payloadJson = new JSONObject(payload);
            String accoDetails = payloadJson.getJSONObject("data").getJSONObject("accomodationData").toString();
            accoAction(payload, accoDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param payload
     * @param accommodationDetails
     */
    private void accoAction(String payload, String accommodationDetails) {
        String productCatSubType = JsonPath.parse(payload).read("$.data.accomodationData.accomodationInfo.productCatSubType", String.class);
        String companyId = JsonPath.parse(payload).read("$.data.accomodationData.accomodationInfo.companyId", String.class);
        String companyName = JsonPath.parse(payload).read("$.data.accomodationData.accomodationInfo.companyName", String.class);

        BookingSearchCriteria bookingSearchCriteria = new BookingSearchCriteria();
        bookingSearchCriteria.setSize(Integer.MAX_VALUE);
        bookingSearchCriteria.setPage(1);
        BookingDetailsFilter bookingDetailsFilter = new BookingDetailsFilter();

        String country = JsonPath.parse(payload).read("$.data.accomodationData.accomodationInfo.address.country", String.class);
        String state = JsonPath.parse(payload).read("$.data.accomodationData.accomodationInfo.address.state", String.class);
        String city = JsonPath.parse(payload).read("$.data.accomodationData.accomodationInfo.address.city", String.class);
        ProductDetailsFilter productDetailsFilter = new ProductDetailsFilter();
        productDetailsFilter.setProductCategorySubTypeId(productCatSubType);
        productDetailsFilter.setCity(city);
        productDetailsFilter.setCountry(country);

        ClientAndPassengerBasedFilter clientAndPassengerBasedFilter = new ClientAndPassengerBasedFilter();
        clientAndPassengerBasedFilter.setClientId("");

        bookingSearchCriteria.setBookingBasedFilter(bookingDetailsFilter);
        bookingSearchCriteria.setClientPxBasedFilter(clientAndPassengerBasedFilter);
        bookingSearchCriteria.setProductBasedFilter(productDetailsFilter);

        CompanyDetailsFilter companyDetailsFilter = new CompanyDetailsFilter();
        companyDetailsFilter.setCompanyId(companyId);
        bookingSearchCriteria.setCompanyBasedFilter(companyDetailsFilter);

        List<BookingSearchResponseItem> bookingSearchResponseItems = opsBookingService.searchBookings(bookingSearchCriteria);
        for (BookingSearchResponseItem responseItem : bookingSearchResponseItems) {
            String bookID = responseItem.getBookID();
            try {
                OpsBooking booking = opsBookingService.getBooking(bookID);
                List<OpsProduct> products = booking.getProducts();
                for (OpsProduct product : products) {
                    if (product.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS)) {
                        List<OpsRoom> rooms = product.getOrderDetails().getHotelDetails().getRooms();
                        for (OpsRoom opsRoom : rooms) {
                            List<OpsAccommodationPaxInfo> paxInfo = opsRoom.getPaxInfo();
                            for (OpsAccommodationPaxInfo opsAccommodationPaxInfo : paxInfo) {
                                if (opsAccommodationPaxInfo.getLeadPax() != null && opsAccommodationPaxInfo.getLeadPax()) {
                                    List<OpsContactDetails> contactDetails = opsAccommodationPaxInfo.getContactDetails();
                                    for (OpsContactDetails opsContactDetail : contactDetails) {
                                        String email = null;
                                        String mobileNo = null;
                                        if (opsContactDetail.getContactInfo() != null) {

                                            if (opsContactDetail.getContactInfo().getEmail() != null) {
                                                email = opsContactDetail.getContactInfo().getEmail();
                                            }
                                            if (opsContactDetail.getContactInfo().getMobileNo() != null) {
                                                mobileNo = opsContactDetail.getContactInfo().getMobileNo();
                                            }
                                        }
                                        action(accommodationDetails, booking, product, email, mobileNo);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param productId
     * @return
     */
    public String getUpdateProductDetails(String productId) {
        String results = null;
        try {
            String url = productDetailsURLForAcco + productId;
            HttpEntity httpEntity = new HttpEntity(new String());
            ResponseEntity<String> response = RestUtils.getTemplate().exchange(url, HttpMethod.POST, httpEntity, String.class);
            results = response.getBody();
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * @param opsBooking
     */
    private void createAlertForOpsUser(OpsBooking opsBooking) {
        NotificationResource notificationResource = null;
        try {
            notificationResource = alertService.createAlert(businessProcess, function, opsBooking.getCompanyId(), alertName, userService.getLoggedInUserId(), "Product master is updated for company Id :" + opsBooking.getCompanyId());
        } catch (OperationException e) {
            logger.error("Error while sending alerts to whome you are assigned" + e);
        }
    }

}
