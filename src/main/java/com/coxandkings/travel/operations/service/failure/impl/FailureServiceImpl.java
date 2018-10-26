package com.coxandkings.travel.operations.service.failure.impl;

import com.coxandkings.travel.operations.criteria.todo.ToDoCriteria;
import com.coxandkings.travel.operations.enums.common.MDMClientType;
import com.coxandkings.travel.operations.enums.failure.FailureActions;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.refund.RefundStatus;
import com.coxandkings.travel.operations.enums.todo.ToDoFunctionalAreaValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskNameValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskSubTypeValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskTypeValues;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.failure.FailureDetails;
import com.coxandkings.travel.operations.model.refund.Refund;
import com.coxandkings.travel.operations.model.refund.finaceRefund.ClientType;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.repository.failure.FailureRepository;
import com.coxandkings.travel.operations.resource.booking.AssignStaffResource;
import com.coxandkings.travel.operations.resource.changesuppliername.request.acco.RoomInfo;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.failure.*;
import com.coxandkings.travel.operations.resource.failure.beresponse.BeFlightSegment;
import com.coxandkings.travel.operations.resource.failure.beresponse.BeOriginDestinationOption;
import com.coxandkings.travel.operations.resource.failure.beresponse.BePricedItinerary;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.resource.todo.ToDoResponse;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResponse;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.beconsumer.acco.AccoBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.beconsumer.acco.AccoCancellationService;
import com.coxandkings.travel.operations.service.beconsumer.acco.helper.AccoBookingEngineConsumptionHelper;
import com.coxandkings.travel.operations.service.beconsumer.air.AirBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.beconsumer.air.AirCancellationService;
import com.coxandkings.travel.operations.service.beconsumer.air.helper.AirBookingEngineConsumptionHelper;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.failure.FailureService;
import com.coxandkings.travel.operations.service.failure.FailureThresholdConfigurationService;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.service.mdmservice.UserMasterDataService;
import com.coxandkings.travel.operations.service.mdmservice.impl.CompanyMasterDataServiceImpl;
import com.coxandkings.travel.operations.service.refund.RefundService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.*;
import com.coxandkings.travel.operations.utils.supplier.SupplierDetailsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jayway.jsonpath.JsonPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.modeshape.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service("failureService")
public class FailureServiceImpl implements FailureService {

    @Value("${booking_engine.get.documents}")
    private String docUrl;

    @Value(value = "${booking_engine.failed-bookings}")
    private String searchFailedBookingsUrl;

    @Value(value = "${booking_engine.check-duplicates}")
    private String checkDuplicatesUrl;

    @Value(value = "${failures.supplierReferenceNumber}")
    private String updateSupplierRefNumberUrl;

    @Value(value = "${failures.pnr}")
    private String updatePnrUrl;

    @Value(value = "${failures.paymentDetails}")
    private String updatePaymentDetailsUrl;

    @Value(value = "${failures.update-order-attribute}")
    private String updateOrderAttribute;

    //Air Service urls
    @Value(value = "${failures.air-service.retrieve}")
    private String airRetrieveServiceUrl;

    @Value(value = "${booking-engine-core-services.air.search}")
    private String airSearchServiceUrl;

    @Value(value = "${booking-engine-core-services.air.price}")
    private String airPriceUrl;

    @Value("${booking-engine-core-services.air.reprice}")
    private String airRePriceURL;

    @Value(value = "${booking-engine-core-services.air.book}")
    private String airBookServiceUrl;

    @Value(value = "${booking-engine-core-services.air.cancel}")
    private String airCancelServiceUrl;

    //Acco Service urls
    @Value(value = "${failures.acco-service.retrieve}")
    private String accoRetrieveServiceUrl;

    @Value(value = "${booking-engine-core-services.acco.search}")
    private String accoSearchUrl;

    @Value(value = "${booking-engine-core-services.acco.price}")
    private String accoPriceUrl;

    @Value(value = "${booking-engine-core-services.acco.reprice}")
    private String accoRePriceUrl;

    @Value(value = "${booking-engine-core-services.acco.book}")
    private String accoBookServiceUrl;

    @Value(value = "${booking-engine-core-services.acco.modify}")
    private String accoCancelUrl;
    @Autowired
    private CompanyMasterDataServiceImpl companyMasterDataService;

    //Email and Alerts
    @Value(value = "${failures.email.scenario}")
    private String scenario;

    @Value(value = "${failures.email.businessProcess}")
    private String businessProcess;

    @Value(value = "${failures.email.function}")
    private String function;

    @Value(value = "${failures.email.from}")
    private String from;

    @Value(value = "${failures.alert_ops.name}")
    private String opsAlertName;

    @Value(value = "${failures.alert_ops.alertType}")
    private String opsAlertType;

    @Value(value = "${failures.alert_tech.name}")
    private String techAlertName;

    @Value(value = "${failures.alert_tech.alertType}")
    private String techAlertType;

    @Value(value = "${failures.alert_ops_unsuccessful.name}")
    private String opsAlertUnsuccessfulBookingName;

    @Value(value = "${failures.alert_ops_unsuccessful.alertType}")
    private String opsAlertUnsuccessfulBookingType;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private AccoBookingEngineConsumptionService accoBookingEngineConsumptionService;

    @Autowired
    private AirBookingEngineConsumptionService airBookingEngineConsumptionService;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private AirCancellationService airCancellationService;

    @Autowired
    private AccoCancellationService accoCancellationService;

    @Autowired
    private AirBookingEngineConsumptionHelper airBookingEngineConsumptionHelper;

    @Autowired
    private AccoBookingEngineConsumptionHelper accoBookingEngineConsumptionHelper;

    @Autowired
    private FailureRepository failureRepository;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private ClientMasterDataService clientMasterDataService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private FailureThresholdConfigurationService failureThresholdConfigurationService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private SupplierDetailsService supplierDetailsService;


    @Autowired
    private UserMasterDataService userMasterService;

    @Autowired
    private UserService userService;


    private static final Logger logger = LogManager.getLogger(FailureServiceImpl.class);

    @Override
    public List<FailureDetailsResource> getFailures(FailureSearchCriteria failureSearchCriteria) throws OperationException {
        switch (failureSearchCriteria.getFailureFlag()) {
            case "Failure Transaction":
                failureSearchCriteria.setPaymentFailureFlag("FAILED");
                failureSearchCriteria.setFailureFlag(null);
                break;
            case "Failure":
                failureSearchCriteria.setFailureFlag("Failure Booking");
                failureSearchCriteria.setPaymentFailureFlag("FAILED");
                break;
            case "Failure Booking":
                failureSearchCriteria.setPaymentFailureFlag(null);
                break;
        }
        HttpEntity<FailureSearchCriteria> entity = new HttpEntity<>(failureSearchCriteria);
        ResponseEntity<List<FailureDetailsResource>> responseEntity = null;
        try {
            responseEntity = RestUtils.exchange(searchFailedBookingsUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<List<FailureDetailsResource>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("unable to get failed bookings");
        }
        List<FailureDetailsResource> failureDetailsResources = new ArrayList<>();
        if (responseEntity != null) {
            failureDetailsResources = responseEntity.getBody();
            if (failureDetailsResources != null && failureDetailsResources.size() > 0) {
                for (FailureDetailsResource failureDetailsResource : failureDetailsResources) {

                    if (failureDetailsResource.getPaymentStatus() != null && failureDetailsResource.getPaymentStatus().equalsIgnoreCase("FAILED")) {
                        failureDetailsResource.setFailureFlag("Failure Transaction");
                    }

                    List<RefundStatus> refundClaimIds = new ArrayList<>();
                    try {
                        refundClaimIds = refundService.refundClaimsByBookingReferenceNo(failureDetailsResource.getBookID()).stream().map(Refund::getRefundStatus).collect(Collectors.toList());
                    } catch (OperationException e) {
                        e.printStackTrace();
                        logger.error("Error while fetching refunds for the booking " + failureDetailsResource.getBookID());
                    }

                    Set<RefundStatus> refundStatuses = new HashSet<>(refundClaimIds);
                    if (refundStatuses.contains(RefundStatus.PENDING_WITH_FINANCE) || refundStatuses.contains(RefundStatus.PENDING_WITH_OPS)) {
                        failureDetailsResource.setRefundStatus("Refund Pending");
                    } else if (refundStatuses.containsAll(Collections.singleton(RefundStatus.PROCESSED))) {
                        failureDetailsResource.setRefundStatus("Processed");
                    } else {
                        failureDetailsResource.setRefundStatus("N/A");
                    }
                }
            } else {
                throw new OperationException(Constants.NO_RESULT_FOUND);
            }
        }


        return failureDetailsResources;
    }

    @Override
    public FailureDetails get(String id) {
        return failureRepository.getByBookID(id);
    }

    private void saveAndUpdate(FailureDetails details) {

        FailureDetails failureDetails = new FailureDetails();
        if (failureRepository.getExists(details.getBookID())) {
            failureDetails = failureRepository.getByBookID(details.getBookID());
            failureDetails.setOrderActions(details.getOrderActions());
            failureDetails.setCommunicationCount(details.getCommunicationCount());
        } else {
            CopyUtils.copy(details, failureDetails);
        }
        failureRepository.saveOrUpdate(failureDetails);
    }

    @Override
    public List<String> checkDuplicates(String bookID) {
        String response = "";
        try {
            response = RestUtils.getForObject(this.checkDuplicatesUrl + bookID, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error in checking the duplicate bookings");
        }
        return JsonPath.parse(response).read("$.duplicateBookIDs");
    }

    @Override
    public List<FailureDetailsResource> getDuplicates(DuplicateBookingsResource duplicateBookingsResource) throws OperationException {
        List<String> duplicates = checkDuplicates(duplicateBookingsResource.getBookId());
        List<FailureDetailsResource> failureDetailsResources = new ArrayList<>();
        for (String id : duplicates) {
            failureDetailsResources.add(convert(id));
        }

        // Default Sort by  booking date in Ascending order.
        if (StringUtils.isEmpty(duplicateBookingsResource.getSortCriteria())) {
            duplicateBookingsResource.setSortCriteria("BookingDateTime");
        }

        if (StringUtils.isEmpty(duplicateBookingsResource.getSortOrder())) {
            duplicateBookingsResource.setSortOrder(CommonEnums.ORDER_ASC.getValue());
        }

        if (duplicateBookingsResource.getSortOrder().equalsIgnoreCase(CommonEnums.ORDER_ASC.getValue())) {
            Collections.sort(failureDetailsResources, getComparator(duplicateBookingsResource.getSortCriteria()));
        } else {
            Collections.sort(failureDetailsResources, getComparator(duplicateBookingsResource.getSortCriteria()).reversed());
        }


        return failureDetailsResources;
    }


    public Comparator<FailureDetailsResource> getComparator(final String sortBy) {

        if ("BookingDateTime".equalsIgnoreCase(sortBy)) {
            return new Comparator<FailureDetailsResource>() {
                @Override
                public int compare(FailureDetailsResource f1, FailureDetailsResource f2) {

                    String f1BookingDate = f1.getBookingDate();
                    String f2BookingDate = f2.getBookingDate();
                    return ZonedDateTime.parse(f1BookingDate).
                            compareTo(ZonedDateTime.parse(f2BookingDate));
                }
            };
        } else if ("ClientName".equalsIgnoreCase(sortBy)) {
            return new Comparator<FailureDetailsResource>() {
                @Override
                public int compare(FailureDetailsResource f1, FailureDetailsResource f2) {
                    String f1ClientName = f1.getClientName();
                    String f2ClientName = f2.getClientName();

                    if (f1ClientName == null) {
                        f1ClientName = "";
                    }
                    if (f2ClientName == null) {
                        f2ClientName = "";
                    }

                    return f1ClientName.compareTo(f2ClientName);
                }
            };
        } else if ("ClientCategory".equalsIgnoreCase(sortBy)) {
            return new Comparator<FailureDetailsResource>() {
                @Override
                public int compare(FailureDetailsResource f1, FailureDetailsResource f2) {
                    String f1ClientCategory = f1.getClientCategory();
                    String f2ClientCategory = f2.getClientCategory();

                    if (f1ClientCategory == null) {
                        f1ClientCategory = "";
                    }
                    if (f2ClientCategory == null) {
                        f2ClientCategory = "";
                    }
                    return f1ClientCategory.compareTo(f2ClientCategory);
                }
            };
        } else if ("ClientSubCategory".equalsIgnoreCase(sortBy)) {
            return new Comparator<FailureDetailsResource>() {
                @Override
                public int compare(FailureDetailsResource f1, FailureDetailsResource f2) {
                    String f1ClientSubCategory = f1.getClientSubCategory();
                    String f2ClientSubCategory = f2.getClientSubCategory();

                    if (f1ClientSubCategory == null) {
                        f1ClientSubCategory = "";
                    }
                    if (f2ClientSubCategory == null) {
                        f2ClientSubCategory = "";
                    }

                    return f1ClientSubCategory.compareTo(f2ClientSubCategory);
                }
            };
        } else if ("PointOfSale".equalsIgnoreCase(sortBy)) {
            return new Comparator<FailureDetailsResource>() {
                @Override
                public int compare(FailureDetailsResource f1, FailureDetailsResource f2) {
                    String f1PointOfSale = f1.getPointOfSale();
                    String f2PointOfSale = f2.getPointOfSale();

                    if (f1PointOfSale == null) {
                        f1PointOfSale = "";
                    }

                    if (f2PointOfSale == null) {
                        f2PointOfSale = "";
                    }

                    return f1PointOfSale.compareTo(f2PointOfSale);
                }
            };
        } else if ("ProductSubCategory".equalsIgnoreCase(sortBy)) {
            return new Comparator<FailureDetailsResource>() {
                // As Dhananjay said to sort based on Search booking (Product Summary),
                // Same technique I am using to sort this product summary.
                // Defect Tracker #10583
                @Override
                public int compare(FailureDetailsResource f1, FailureDetailsResource f2) {
                    String f1ProductSubcategory = "";
                    String f2ProductSubcategory = "";
                    try {
                        f1ProductSubcategory = f1.getProductSummary().get(0).getProductSubCategory();
                        if(f1ProductSubcategory == null){
                            f1ProductSubcategory = "";
                        }
                    } catch (Exception e) {
                        logger.info("Comparator failed to fetch f1 ProductSubcategory");
                    }

                    try {
                        f2ProductSubcategory = f2.getProductSummary().get(0).getProductSubCategory();
                        if(f2ProductSubcategory == null){
                            f2ProductSubcategory = "";
                        }

                    } catch (Exception e) {
                        logger.info("Comparator failed to fetch f2 ProductSubcategory");
                    }

                    return f1ProductSubcategory.
                            compareTo(f2ProductSubcategory);
                }
            };
        } else if ("ProductName".equalsIgnoreCase(sortBy)) {
            return new Comparator<FailureDetailsResource>() {
                @Override
                public int compare(FailureDetailsResource f1, FailureDetailsResource f2) {
                    String f1productName = "";
                    String f2productName = "";

                    try {
                        f1productName = f1.getProductSummary().get(0).getOrders().get(0).getOrderSummary().get(0)
                                .getProductName();
                        if (f1productName == null) {
                            f1productName = "";
                        }
                    } catch (Exception e) {
                        logger.info("Comparator failed to fetch f1 product name");
                    }

                    try {
                        f2productName = f2.getProductSummary().get(0).getOrders().get(0).getOrderSummary().get(0)
                                .getProductName();
                        if (f2productName == null) {
                            f2productName = "";
                        }
                    } catch (Exception e) {
                        logger.info("Comparator failed to fetch f2 product name");
                    }

                    return f1productName.compareTo(f2productName);

                }
            };
        } else if ("TravelDate".equalsIgnoreCase(sortBy)) {
            return new Comparator<FailureDetailsResource>() {
                @Override
                public int compare(FailureDetailsResource f1, FailureDetailsResource f2) {
                    ZonedDateTime f1TravelDate = null;
                    ZonedDateTime f2TravelDate = null;
                    Instant minInstant = Instant.ofEpochMilli(Long.MIN_VALUE);
                    try {
                        f1TravelDate = ZonedDateTime.parse(f1.getProductSummary().get(0).getOrders().get(0).getOrderSummary()
                                .get(0).getTravelDate());
                        if (f1TravelDate == null) {
                            f1TravelDate = minInstant.atZone(ZoneOffset.UTC);
                        }
                    } catch (Exception e) {
                        logger.info("Comparator failed to fetch f1 TravelDate ");
                    }

                    try {
                        f2TravelDate = ZonedDateTime.parse(f1.getProductSummary().get(0).getOrders().get(0).getOrderSummary()
                                .get(0).getTravelDate());
                        if (f2TravelDate == null) {
                            f2TravelDate = minInstant.atZone(ZoneOffset.UTC);
                        }
                    } catch (Exception e) {
                        logger.info("Comparator failed to fetch f2 Travel Date ");
                    }

                    return f1TravelDate.compareTo(f2TravelDate);

                }
            };
        } else if ("ProductStatus".equalsIgnoreCase(sortBy)) {
            return new Comparator<FailureDetailsResource>() {
                @Override
                public int compare(FailureDetailsResource f1, FailureDetailsResource f2) {
                    String f1OrderStatus = "";
                    String f2OrderStatus = "";
                    try {
                        f1OrderStatus = f1.getProductSummary().get(0).getOrders().get(0).getOrderStatus();
                        if (f1OrderStatus == null) {
                            f1OrderStatus = "";
                        }
                    } catch (Exception e) {
                        logger.info("Comparator failed to fetch f1 OrderStatus ");
                    }
                    try {
                        f2OrderStatus = f2.getProductSummary().get(0).getOrders().get(0).getOrderStatus();
                        if (f2OrderStatus == null) {
                            f1OrderStatus = "";
                        }
                    } catch (Exception e) {
                        logger.info("Comparator failed to fetch f2 OrderStatus ");
                    }
                    return f1OrderStatus
                            .compareTo(f2OrderStatus);

                }
            };
        } else if ("RefundStatus".equalsIgnoreCase(sortBy)) {
            return new Comparator<FailureDetailsResource>() {
                @Override
                public int compare(FailureDetailsResource f1, FailureDetailsResource f2) {

                    String f1RefundStatus = f1.getRefundStatus();
                    String f2RefundStatus = f2.getRefundStatus();

                    if (f1RefundStatus == null) {
                        f1RefundStatus = "";
                    }

                    if (f2RefundStatus == null) {
                        f2RefundStatus = "";
                    }

                    return f1RefundStatus.compareTo(f2RefundStatus);
                }
            };
        } else if ("FileHandlerName".equalsIgnoreCase(sortBy)) {
            return new Comparator<FailureDetailsResource>() {
                @Override
                public int compare(FailureDetailsResource f1, FailureDetailsResource f2) {
                    String f1FileHandlerName = f1.getFileHandlerName();
                    String f2FileHandlerName = f2.getFileHandlerName();

                    if (f1FileHandlerName == null) {
                        f1FileHandlerName = "";
                    }

                    if (f2FileHandlerName == null) {
                        f2FileHandlerName = "";
                    }

                    return f1FileHandlerName.compareTo(f2FileHandlerName);
                }
            };
        } else {
            throw new IllegalArgumentException("Invalid Sort Field '" + sortBy + "'");
        }

    }

    @Override
    public void actionBookingFailure(OpsBooking opsBooking, OpsProduct opsProduct) throws OperationException {
        FailureDetails failureDetails = null;
        try {
            failureDetails = failureRepository.getByBookID(opsBooking.getBookID());
        } catch (Exception exception) {
            failureDetails = new FailureDetails();
            failureDetails.setBookID(opsBooking.getBookID());
            Map<String, FailureActions> map = new HashMap<>();
            List<String> bookIDs = checkDuplicates(opsBooking.getBookID());
            Integer value = 0;
            //checking if supplier is offline or online
            JSONArray supplierResponse = new JSONArray();
            if (opsProduct.getOrderDetails().getSupplierType().toString().equalsIgnoreCase("online")) {
                supplierResponse = checkSupplierSystem(opsBooking, opsProduct);
                try {
                    value = supplierResponse.length();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("cannot able to check supplier system, Booking is not confirmed with the supplier");
                }
            } else {
                logger.info("Unable to check supplier system as supplier is not online");
            }

            if (bookIDs.size() > 0) {
                if (value > 0 && checkSupplierStatus(opsProduct.getOpsProductSubCategory(), supplierResponse)) {
                    map.put(opsProduct.getOrderID(), FailureActions.CANCELLATION_AND_REFUND);
                    createTodo(null, opsBooking.getBookID(), opsProduct.getOrderID(), ToDoTaskNameValues.CANCEL_AND_REFUND, "Cancel the booking and initiate a refund", ToDoFunctionalAreaValues.OPERATIONS, MDMClientType.fromString(opsBooking.getClientType()));
                } else {
                    map.put(opsProduct.getOrderID(), FailureActions.REFUND);
                    createTodo(null, opsBooking.getBookID(), opsProduct.getOrderID(), ToDoTaskNameValues.REFUND, "Initiate a Refund for the current booking", ToDoFunctionalAreaValues.OPERATIONS, MDMClientType.fromString(opsBooking.getClientType()));
                }
            } else {
                if (value > 0 && checkSupplierStatus(opsProduct.getOpsProductSubCategory(), supplierResponse)) {
                    //Todo Try sync pnr
                    map.put(opsProduct.getOrderID(), FailureActions.UPDATE_PNR);
                    createTodo(null, opsBooking.getBookID(), opsProduct.getOrderID(), ToDoTaskNameValues.UPDATE_PNR, "Update pnr or supplier reference number", ToDoFunctionalAreaValues.OPERATIONS, MDMClientType.fromString(opsBooking.getClientType()));
                } else {
                    try {
                        searchToBook(opsBooking.getBookID(), opsProduct.getOrderID());
                    } catch (Exception e) {
                        map.put(opsProduct.getOrderID(), FailureActions.SEARCH_AND_BOOK);
                        createTodo(null, opsBooking.getBookID(), opsProduct.getOrderID(), ToDoTaskNameValues.SEARCH_AND_BOOK, "Search and book the product", ToDoFunctionalAreaValues.OPERATIONS, MDMClientType.fromString(opsBooking.getClientType()));
                    }
                }
            }
            failureDetails.setOrderActions(map);
            saveAndUpdate(failureDetails);
        }
    }


    @Override
    public JSONArray checkSupplierSystem(OpsBooking opsBooking, OpsProduct product) {
        JSONObject requestHeader = getRequestHeader(opsBooking, true);
        JSONArray response = new JSONArray();
        if (product.getOpsProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION) && product.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT)) {
            String airlinePnr = product.getOrderDetails().getFlightDetails().getAirlinePNR();
            String supplierRef = product.getSupplierID();
            if (!StringUtils.isEmpty(airlinePnr) && !StringUtils.isEmpty(supplierRef)) {
                response = checkSupplierAir(opsBooking, product, airlinePnr, supplierRef);
            } else {
                logger.info("The order is not booked with the supplier");
                logger.info(product.getOrderID());
            }
        } else if (product.getOpsProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION) && product.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS)) {
            String supplierRef = product.getSupplierID();
            String supplierReservationId = product.getSupplierReservationId();
            String accommodationSubType = product.getProductSubCategory();
            if (!StringUtils.isEmpty(supplierRef) && !StringUtils.isEmpty(supplierReservationId) && !StringUtils.isEmpty(accommodationSubType)) {
                response = checkSupplierAcco(accommodationSubType, supplierRef, supplierReservationId, requestHeader);
            } else {
                logger.info("The order is not booked with the supplier");
                logger.info(product.getOrderID());
            }
        }
        return response;
    }

    @Override
    public void actionPaymentFailure(OpsBooking opsBooking) throws OperationException {
        FailureDetails failureDetails = new FailureDetails();
        if (!failureRepository.getExists(opsBooking.getBookID())) {
            failureDetails.setBookID(opsBooking.getBookID());
            createTodo(null, opsBooking.getBookID(), null, ToDoTaskNameValues.ACTION, "manually handle the booking", ToDoFunctionalAreaValues.OPERATIONS, MDMClientType.fromString(opsBooking.getClientType()));
            failureRepository.saveOrUpdate(failureDetails);
        }
    }

    @Override
    public Map updatePnr(PnrResource pnrResource) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(this.updatePnrUrl);
        Map<String, String> map = new HashMap<>();
        String message = null;
        JSONObject request = new JSONObject();
        request.put("orderID", (pnrResource.getOrderID() == null) ? "" : pnrResource.getOrderID());
        request.put("userID", (pnrResource.getUserID() == null) ? "" : pnrResource.getUserID());
        request.put("airlinePNR", (pnrResource.getAirlinePnr() == null) ? "" : pnrResource.getAirlinePnr());
        request.put("GDSPNR", (pnrResource.getGdsPnr() == null) ? "" : pnrResource.getGdsPnr());
        request.put("ticketPNR", (pnrResource.getTicketPNR() == null) ? "" : pnrResource.getTicketPNR());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);
        try {
            ResponseEntity<String> responseEntity = RestUtils.exchange(builder.toUriString(), HttpMethod.PUT, entity, String.class);
            message = responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            message = "Unable to update PNR details";
            logger.error("update PNR is not successful");
        }
        map.put("message", message);
        return map;
    }

    @Override
    public Map updateSupplierRefNumber(SupplierRefResource supplierRefResource) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(this.updateSupplierRefNumberUrl);
        String message = "";
        JSONObject request = new JSONObject();
        request.put("orderID", (supplierRefResource.getOrderID() == null) ? "" : supplierRefResource.getOrderID());
        request.put("userID", (supplierRefResource.getUserID() == null) ? "" : supplierRefResource.getUserID());
        request.put("supplierReferenceId", (supplierRefResource.getSupplierReferenceId() == null) ? "" : supplierRefResource.getSupplierReferenceId());
        request.put("supplierReservationId", (supplierRefResource.getSupplierReservationId() == null) ? "" : supplierRefResource.getSupplierReservationId());
        request.put("clientReferenceId", (supplierRefResource.getClientReferenceId() == null) ? "" : supplierRefResource.getClientReferenceId());
        request.put("supplierCancellationId", (supplierRefResource.getSupplierCancellationId() == null) ? "" : supplierRefResource.getSupplierCancellationId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);
        try {
            ResponseEntity<String> responseEntity = RestUtils.exchange(builder.toUriString(), HttpMethod.PUT, entity, String.class);
            message = responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            message = "update supplier reference is not  successful";
            logger.error("update supplier reference is not  successful");
        }
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return map;
    }

    @Override
    public Map<String, String> updatePaymentDetails(PaymentInfoResource paymentInfoResource) {
        String message = "";
        ResponseEntity<String> responseEntity = null;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(this.updatePaymentDetailsUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject paymentDetails = new JSONObject();
        paymentDetails.put("userID", (paymentInfoResource.getUserID() == null) ? "" : paymentInfoResource.getUserID());
        paymentDetails.put("paymentID", (paymentInfoResource.getPaymentID() == null) ? "" : paymentInfoResource.getPaymentID());
        paymentDetails.put("totalAmount", (paymentInfoResource.getTotalAmount() == null) ? "" : paymentInfoResource.getTotalAmount());
        paymentDetails.put("amountCurrency", (paymentInfoResource.getAmountCurrency() == null) ? "" : paymentInfoResource.getAmountCurrency());
        paymentDetails.put("transactionRefNumber", (paymentInfoResource.getTransactionRefNumber() == null) ? "" : paymentInfoResource.getTransactionRefNumber());
        paymentDetails.put("transactionDate", (paymentInfoResource.getTransactionDate() == null) ? "" : paymentInfoResource.getTransactionDate());
        paymentDetails.put("amountPaid", (paymentInfoResource.getAmountPaid() == null) ? "" : paymentInfoResource.getAmountPaid());
        paymentDetails.put("paymentType", (paymentInfoResource.getPaymentType() == null) ? "" : paymentInfoResource.getPaymentType());
        paymentDetails.put("paymentMethod", (paymentInfoResource.getPaymentMethod() == null) ? "" : paymentInfoResource.getPaymentMethod());
        paymentDetails.put("paymentStatus", (paymentInfoResource.getPaymentStatus() == null) ? "" : paymentInfoResource.getPaymentStatus());

        JSONObject paymentAttributes = new JSONObject();
        paymentAttributes.put("bankName", (paymentInfoResource.getPaymentAttributesResource().getBankName() == null) ? "" : paymentInfoResource.getPaymentAttributesResource().getBankName());
        paymentAttributes.put("toBankName", (paymentInfoResource.getPaymentAttributesResource().getToBankName() == null) ? "" : paymentInfoResource.getPaymentAttributesResource().getToBankName());
        paymentAttributes.put("cardType", (paymentInfoResource.getPaymentAttributesResource().getCardType() == null) ? "" : paymentInfoResource.getPaymentAttributesResource().getCardType());
        paymentAttributes.put("nameOnCard", (paymentInfoResource.getPaymentAttributesResource().getNameOnCard() == null) ? "" : paymentInfoResource.getPaymentAttributesResource().getNameOnCard());
        paymentAttributes.put("bankAccount", (paymentInfoResource.getPaymentAttributesResource().getBankAccount() == null) ? "" : paymentInfoResource.getPaymentAttributesResource().getBankAccount());
        paymentAttributes.put("depositRefNumber", (paymentInfoResource.getPaymentAttributesResource().getDepositRefNumber() == null) ? "" : paymentInfoResource.getPaymentAttributesResource().getDepositRefNumber());
        paymentAttributes.put("noOdProducts", (paymentInfoResource.getPaymentAttributesResource().getNoOfProducts() == null) ? "" : paymentInfoResource.getPaymentAttributesResource().getNoOfProducts());
        paymentAttributes.put("Ifsc/SwiftCode", (paymentInfoResource.getPaymentAttributesResource().getIfscOrSwiftCode() == null) ? "" : paymentInfoResource.getPaymentAttributesResource().getIfscOrSwiftCode());
        paymentAttributes.put("IvrDetails", (paymentInfoResource.getPaymentAttributesResource().getIvrDetails() == null) ? "" : paymentInfoResource.getPaymentAttributesResource().getIvrDetails());
        paymentAttributes.put("IntermedaryBankIfsc/SwiftCode", (paymentInfoResource.getPaymentAttributesResource().getIntermediaryIfscOrSwiftCode() == null) ? "" : paymentInfoResource.getPaymentAttributesResource().getIntermediaryIfscOrSwiftCode());
        paymentAttributes.put("contactPerson", (paymentInfoResource.getPaymentAttributesResource().getContactPerson() == null) ? "" : paymentInfoResource.getPaymentAttributesResource().getContactPerson());
        paymentAttributes.put("Address", (paymentInfoResource.getPaymentAttributesResource().getAddress() == null) ? "" : paymentInfoResource.getPaymentAttributesResource().getAddress());
        paymentAttributes.put("mobileNumber", (paymentInfoResource.getPaymentAttributesResource().getMobileNumber() == null) ? "" : paymentInfoResource.getPaymentAttributesResource().getMobileNumber());
        paymentAttributes.put("cheque/DDNumber", (paymentInfoResource.getPaymentAttributesResource().getChequeOrDDNumber() == null) ? "" : paymentInfoResource.getPaymentAttributesResource().getChequeOrDDNumber());
        paymentAttributes.put("merchantId", (paymentInfoResource.getPaymentAttributesResource().getMerchantId() == null) ? "" : paymentInfoResource.getPaymentAttributesResource().getMerchantId());

        paymentDetails.put("paymentAttributes", paymentAttributes);
        HttpEntity<String> entity = new HttpEntity(paymentDetails, headers);
        try {
            responseEntity = RestUtils.exchange(builder.toUriString(), HttpMethod.PUT, entity, String.class);
            message = responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            message = "Unable to update payment details";
            logger.error("update payment details is not  successful");
        }
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return map;
    }

    @Override
    public Map cancel(String bookID) throws OperationException {
        OpsBooking opsBooking = opsBookingService.getBooking(bookID);
        String status = "SUCCESS";
        String message = "Cancellation is not successful";
        Map<String, String> map = new HashMap<>();
        for (OpsProduct opsProduct : opsBooking.getProducts()) {
            JSONArray supplierResponse = checkSupplierSystem(opsBooking, opsProduct);
            //checking the order status before doing the cancellation
            if (opsProduct.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT)) {
                //if (supplierResponse.length() > 0) {
                JSONObject airCancelRequest = airCancelRequest(opsBooking, opsProduct);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> httpEntity = new HttpEntity(airCancelRequest.toString(), headers);
                ResponseEntity<String> airCancelResponse = null;
                try {
                    logger.info("****Cancel Request***");
                    logger.info(airCancelRequest.toString());
                    airCancelResponse = RestUtils.exchange(airCancelServiceUrl, HttpMethod.POST, httpEntity, String.class);
                    message = jsonObjectProvider.getAttributeValue(airCancelResponse.getBody(), "$.responseBody.supplierBookReferences[0].status", String.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Cancellation of air product is not successful order is not confirmed with supplier full cancellation will be initiated");
                    message = "Cancellation of air product is not successful order is not confirmed with supplier full cancellation will be initiated";
                    //throw new OperationException("Cancellation is not successful in book id " + opsBooking.getBookID() + "for the order id " + opsProduct.getOrderID());
                }
                map.put("message", message);
            } else if (opsProduct.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS)) {
                //String accoCancelResponse = accoCancellationService.processCancellation(opsBooking, opsProduct);
                //if (supplierResponse.length() > 0 && checkSupplierStatus(opsProduct.getOpsProductSubCategory(), supplierResponse)) {
                JSONObject accoCancelRequest = accoCancellationRequest(opsBooking, opsProduct);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity httpEntity = new HttpEntity<>(accoCancelRequest.toString(), headers);
                ResponseEntity<String> accoCancelResponse = null;
                try {
                    logger.info("****Cancel Request***");
                    logger.info(accoCancelRequest.toString());
                    accoCancelResponse = RestUtils.exchange(accoCancelUrl, HttpMethod.POST, httpEntity, String.class);
                    try {
                        status = jsonObjectProvider.getAttributeValue(accoCancelResponse.getBody(), "$.responseBody.accommodationInfo[0].status", String.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                        status = jsonObjectProvider.getAttributeValue(accoCancelResponse.getBody(), "$.responseBody.status", String.class);
                    }
                    if (status.equals("SUCCESS")) {
                        message = "Accommodation product of book ID " + opsBooking.getBookID() + " with order id" + opsProduct.getOrderID() + " has been successfully cancelled";
                    } else if (status.equals("FAILURE")) {
                        message = jsonObjectProvider.getAttributeValue(accoCancelResponse.getBody(), "$.responseBody.errorMsg", String.class);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (accoCancelResponse != null) {
                        status = jsonObjectProvider.getAttributeValue(accoCancelResponse.getBody(), "$.responseBody.status", String.class);
                        message = jsonObjectProvider.getAttributeValue(accoCancelResponse.getBody(), "$.responseBody.errorMsg", String.class);
                        map.put("message", message);
                    }
                    logger.error("Cancellation of acco product is not successful order is not confirmed with supplier, full cancellation will be initiated");
                    //throw new OperationException("Cancellation is not successful for book id " + opsBooking.getBookID() + "for the order id " + opsProduct.getOrderID());
                }
                map.put("message", message);
            }
        }
        //map.put("status", status);
        return map;
    }

    @Override
    public Map<String, String> searchToBook(String bookID, String orderID) throws OperationException {
        logger.info("*** In Evoke Web Service ***");
        OpsBooking opsBooking = opsBookingService.getBooking(bookID);
        OpsProduct opsProduct = null;
        try {
            opsProduct = opsBooking.getProducts().stream().filter(opsProduct1 -> opsProduct1.getOrderID().equalsIgnoreCase(orderID)).findFirst().get();
        } catch (Exception e) {
            logger.error("There order id provided is not present in this booking");
            e.printStackTrace();
            //throw new OperationException("The order id is not matching with the booking");
        }
        String message = null;
        //need to see if i have to add more checks
        if (Objects.requireNonNull(opsProduct).getOrderDetails().getOpsBookingAttribute().toString().contains("BF") || opsProduct.getOrderDetails().getOpsOrderStatus().equals(OpsOrderStatus.RQ)) {
            if (opsProduct.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT)) {
                JSONObject airSearchRequest = getAirSearchRequest(opsBooking, opsProduct);
                String airSearchResponse = "";
                try {
                    logger.info("***In Air Search***");
                    ResponseEntity<String> response = null;
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> entity = new HttpEntity(airSearchRequest.toString(), headers);
                    ZonedDateTime time = ZonedDateTime.now();
                    response = RestUtils.exchange(airSearchServiceUrl, HttpMethod.POST, entity, String.class);
                    logger.info("*****************************************************");
                    logger.info(Duration.between(ZonedDateTime.now(), time));
                    logger.info("printing the time taken by search");
                    airSearchResponse = response.getBody();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Search is unsuccessful for the flight");
                    //throw new OperationException("search is unsuccessful");
                }
                JSONObject priceRequest = getPriceAirRequestBodyJson(opsProduct, airSearchResponse);
                JSONObject airPriceRequest = new JSONObject();
                JSONObject requestHeader = null;
                try {
                    requestHeader = new JSONObject(jsonObjectProvider.getChildJSON(airSearchResponse, "$.responseHeader"));
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("unable to get Request Header");
                }
                airPriceRequest.put("requestHeader", requestHeader);
                airPriceRequest.put("requestBody", priceRequest);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> httpEntity = new HttpEntity<>(airPriceRequest.toString().replace("\\", ""), headers);
                ResponseEntity<String> priceResponse = null;
                try {
                    logger.info("***In Air Price***");
                    priceResponse = RestUtils.exchange(this.airPriceUrl, HttpMethod.POST, httpEntity, String.class);
                    logger.info("after calling getPrice of BE");
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("air price is not successful");
                    //throw new OperationException("pricing is unsuccessful");
                }
                assert priceResponse != null;
                JSONObject rePriceAirRequestBodyJson = getRePriceAirRequestBodyJson(opsProduct, priceResponse.getBody());
                JSONObject airRepriceRequest = new JSONObject();
                airRepriceRequest.put("requestHeader", requestHeader);
                airRepriceRequest.put("requestBody", rePriceAirRequestBodyJson);
                String rePriceResponse = null;
                ResponseEntity<String> repriceResponseEntity = null;
                HttpEntity<String> entity = new HttpEntity<>(airRepriceRequest.toString(), headers);
                try {
                    logger.info("***In Air RePrice***");
                    repriceResponseEntity = RestUtils.exchange(this.airRePriceURL, HttpMethod.POST, entity, String.class);
                    logger.info("after calling getPrice of BE");
                    rePriceResponse = repriceResponseEntity.getBody();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("air reprice is not successful");
                    //throw new OperationException("Repricing is unsuccessful");
                }
                Double oldPrice = Double.parseDouble(opsProduct.getOrderDetails().getFlightDetails().getTotalPriceInfo().getTotalPrice());
                Double newPrice = getNewPrice(rePriceResponse, opsProduct);
                if (newPrice.equals(oldPrice)) {
                    message = bookAir(opsBooking, opsProduct, newPrice, new JSONObject(rePriceResponse));
                } else {
                    try {
                        message = priceNotEqual(opsBooking, opsProduct, newPrice, oldPrice, airRepriceRequest, null);
                        //message = "Booking is successful";
                    } catch (Exception e) {
                        e.printStackTrace();
                        createTodo(null, opsBooking.getBookID(), opsProduct.getOrderID(), ToDoTaskNameValues.SEARCH_AND_BOOK, "price did not match", ToDoFunctionalAreaValues.OPERATIONS, MDMClientType.fromString(opsBooking.getClientType()));
                        message = "Booking is not successful and todo task was created for Ops User to handle manually";
                    }
                }
            } else if (opsProduct.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS)) {
                ResponseEntity<String> accoSearchResponse = null;
                JSONObject accoSearchRequest = accoSearchRequest(opsBooking, opsProduct);
                try {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    logger.info("***In Acco Search***");
                    logger.info(accoSearchRequest.toString());
                    HttpEntity<String> entity = new HttpEntity(accoSearchRequest.toString(), headers);
                    accoSearchResponse = RestUtils.exchange(this.accoSearchUrl, HttpMethod.POST, entity, String.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Search for the product is not successful");
                    //throw new OperationException("Search is not successful");
                }
                JSONObject requestHeader = null;
                try {
                    requestHeader = new JSONObject(jsonObjectProvider.getChildJSON(accoSearchResponse.getBody(), "$.responseHeader"));
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("unable to get Request Header");
                }
                ResponseEntity<String> accoPriceResponse = null;
                JSONObject requestBody = null;
                try {
                    requestBody = getAccoPriceRequestBody(accoSearchResponse.getBody(), opsProduct);
                } catch (Exception e) {
                    logger.error("No matching results found for search");
                    e.printStackTrace();
                }
                JSONObject request = new JSONObject();
                request.put("requestHeader", requestHeader);
                request.put("requestBody", requestBody);
                try {
                    //accoPrice = accoBookingEngineConsumptionService.getPrice(opsBooking, opsProduct, opsProduct.getSourceSupplierName());
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> entity = new HttpEntity(request.toString(), headers);
                    accoPriceResponse = RestUtils.exchange(this.accoPriceUrl, HttpMethod.POST, entity, String.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Pricing for the product is not successful");
                    //throw new OperationException("Pricing is  not successful");
                }
                ResponseEntity<String> accoRepriceResponse = null;
                JSONObject accoRepriceRequest = new JSONObject();
                accoRepriceRequest.put("requestHeader", requestHeader);
                accoRepriceRequest.put("requestBody", getAccoPriceRequestBody(accoPriceResponse.getBody(), opsProduct));
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity(accoRepriceRequest.toString(), headers);
                try {
                    accoRepriceResponse = RestUtils.exchange(this.accoRePriceUrl, HttpMethod.POST, entity, String.class);
                    //accoRePrice = accoBookingEngineConsumptionService.getReprice(opsBooking, opsProduct, opsProduct.getSourceSupplierName());
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Reprice for the product is not successful");
                    //throw new OperationException("Reprice for the product is not successful");
                }
                Double oldPrice = Double.parseDouble(opsProduct.getOrderDetails().getHotelDetails().getOpsAccommodationTotalPriceInfo().getTotalPrice());
                Double newPrice = getAccoAmountAfterReprice(opsProduct, accoRepriceResponse.getBody());
                if (oldPrice.equals(newPrice)) {
                    message = bookAcco(opsBooking, accoRepriceResponse.getBody(), opsProduct, newPrice);
                } else {
                    try {
                        message = priceNotEqual(opsBooking, opsProduct, newPrice, oldPrice, null, accoRepriceResponse.getBody());
                        //message = "Booking is successful";
                    } catch (Exception e) {
                        e.printStackTrace();
                        createTodo(null, opsBooking.getBookID(), opsProduct.getOrderID(), ToDoTaskNameValues.SEARCH_AND_BOOK, "product with same price is not available", ToDoFunctionalAreaValues.OPERATIONS, MDMClientType.fromString(opsBooking.getClientType()));
                        message = "Booking is not successful and todo task was created for the Ops User to handle manually";
                    }
                }
            }

        } else {
            message = "The product is not a failure";
        }

        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return map;
    }

    //filtering the response to get the matching priced itinerary not the best possible way need to find a better solution
    private JSONArray getPricedItineraryListJson(String response, OpsProduct opsProduct) {
        ObjectMapper mapper = new ObjectMapper();
        List<BePricedItinerary> airResponses = jsonObjectProvider.getChildrenCollection(response, "$.responseBody.pricedItinerary[*]", BePricedItinerary.class);
        JSONArray array = new JSONArray();
        for (BePricedItinerary pricedItinerary : airResponses) {
            if (opsProduct.getSupplierID().equalsIgnoreCase(pricedItinerary.getSupplierRef())) {
                for (BeOriginDestinationOption originDestinationOption : pricedItinerary.getAirItinerary().getOriginDestinationOptions()) {
                    for (OpsOriginDestinationOption opsOriginDestinationOption : opsProduct.getOrderDetails().getFlightDetails().getOriginDestinationOptions()) {
                        for (BeFlightSegment flightSegment : originDestinationOption.getFlightSegment()) {
                            for (OpsFlightSegment opsFlightSegment : opsOriginDestinationOption.getFlightSegment()) {
                                if (flightSegment.getOriginLocation().equalsIgnoreCase(opsFlightSegment.getOriginLocation()) &&
                                        flightSegment.getDestinationLocation().equalsIgnoreCase(opsFlightSegment.getDestinationLocation()) &&
                                        flightSegment.getMarketingAirline().getAirlineCode().equals(opsFlightSegment.getMarketingAirline().getAirlineCode()) &&
                                        flightSegment.getMarketingAirline().getFlightNumber().equalsIgnoreCase(opsFlightSegment.getMarketingAirline().getFlightNumber()) &&
                                        flightSegment.getOperatingAirline().getFlightNumber().equalsIgnoreCase(opsFlightSegment.getOperatingAirline().getFlightNumber()) &&
                                        flightSegment.getOperatingAirline().getAirlineCode().equalsIgnoreCase(opsFlightSegment.getOperatingAirline().getAirlineCode())) {
                                    try {
                                        array.put((new JSONObject(mapper.writeValueAsString(pricedItinerary))));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        logger.error("unable to parse Air price response");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return array;
    }

    //checking the price from the air price response
    private Double getNewPrice(String airRepriceResponse, OpsProduct opsProduct) {
        Double amount = 0.0;
        List<BePricedItinerary> airPriceItineraries = new ArrayList<>();
        try {
            airPriceItineraries = jsonObjectProvider.getChildrenCollection(airRepriceResponse, "$.responseBody.pricedItinerary[*]", BePricedItinerary.class);
        } catch (Exception e) {
            logger.error("Unable to parse response json to check the price");
        }
        for (BePricedItinerary pricedItinerary : airPriceItineraries) {
            if (opsProduct.getSupplierID().equalsIgnoreCase(pricedItinerary.getSupplierRef())) {
                for (BeOriginDestinationOption originDestinationOption : pricedItinerary.getAirItinerary().getOriginDestinationOptions()) {
                    for (OpsOriginDestinationOption opsOriginDestinationOption : opsProduct.getOrderDetails().getFlightDetails().getOriginDestinationOptions()) {
                        for (BeFlightSegment flightSegment : originDestinationOption.getFlightSegment()) {
                            for (OpsFlightSegment opsFlightSegment : opsOriginDestinationOption.getFlightSegment()) {
                                if (flightSegment.getOriginLocation().equalsIgnoreCase(opsFlightSegment.getOriginLocation()) &&
                                        flightSegment.getDestinationLocation().equalsIgnoreCase(opsFlightSegment.getDestinationLocation()) &&
                                        flightSegment.getMarketingAirline().getAirlineCode().equals(opsFlightSegment.getMarketingAirline().getAirlineCode()) &&
                                        flightSegment.getMarketingAirline().getFlightNumber().equalsIgnoreCase(opsFlightSegment.getMarketingAirline().getFlightNumber()) &&
                                        flightSegment.getOperatingAirline().getFlightNumber().equalsIgnoreCase(opsFlightSegment.getOperatingAirline().getFlightNumber()) &&
                                        flightSegment.getOperatingAirline().getAirlineCode().equalsIgnoreCase(opsFlightSegment.getOperatingAirline().getAirlineCode())) {
                                    amount += pricedItinerary.getAirItineraryPricingInfo().getItinTotalFare().getAmount();
                                }
                            }
                        }
                    }
                }
            }
        }
        return amount;
    }

    private JSONObject getRePriceAirRequestBodyJson(OpsProduct opsProduct, String response) throws
            OperationException {
        String tripType = opsProduct.getOrderDetails().getFlightDetails().getTripType();
        JSONObject requestBody = new JSONObject();
        requestBody.put("tripType", tripType);
        requestBody.put("paxInfo", getPaxInfoListJsonAir(opsProduct));
        requestBody.put("pricedItinerary", getPricedItineraryListJson(response, opsProduct));
        requestBody.put("paxDetails", getPaxDetailsJsonAir(opsProduct));
        return requestBody;
    }

    private List<JSONObject> getPaxDetailsJsonAir(OpsProduct opsProduct) throws OperationException {
        List<JSONObject> paxDetails = new ArrayList<>();
        List<OpsFlightPaxInfo> paxInfos = opsProduct.getOrderDetails().getFlightDetails().getPaxInfo();
        JSONArray docDetails = getDocumentByOrder(opsProduct.getOrderID(), opsProduct.getProductSubCategory());
        if (paxInfos != null) {
            for (OpsFlightPaxInfo opsFlightPaxInfo : paxInfos) {
                JSONObject paxDetail = new JSONObject();
                paxDetail.put("dob", opsFlightPaxInfo.getBirthDate());
                paxDetail.put("firstName", opsFlightPaxInfo.getFirstName());
                paxDetail.put("middleName", opsFlightPaxInfo.getMiddleName());
                paxDetail.put("paxType", opsFlightPaxInfo.getPaxType());
                paxDetail.put("title", opsFlightPaxInfo.getTitle());
                //  paxDetail.put("gender",opsFlightPaxInfo.`); //todo gender missing
                paxDetail.put("gender", ""); //todo needs to be removed be is not sending this currently
                paxDetail.put("surname", opsFlightPaxInfo.getLastName());
                JSONObject addressDetails = null;
                JSONObject ancillaryServices = null;
                JSONObject specialRequests = null;
                addressDetails = airBookingEngineConsumptionHelper.getAddressDetailsJson(opsFlightPaxInfo.getAddressDetails());
                if (opsFlightPaxInfo.getAncillaryServices() != null) {
                    ancillaryServices = airBookingEngineConsumptionHelper.getAncillaryServicesJson(opsFlightPaxInfo.getAncillaryServices());
                }
                if (opsFlightPaxInfo.getOpsSpecialRequest() != null) {
                    specialRequests = airBookingEngineConsumptionHelper.getSpecialRequestsJson(opsFlightPaxInfo.getOpsSpecialRequest());
                }
                List<JSONObject> contactDetailsList = new ArrayList<>();
                for (OpsContactDetails opsContactDetails : opsFlightPaxInfo.getContactDetails()) {
                    JSONObject contactDetailsJson = new JSONObject();
                    JSONObject contactInfo = new JSONObject();
                    contactInfo.put("countryCode", opsContactDetails.getContactInfo().getCountryCode());
                    contactInfo.put("contactType", opsContactDetails.getContactInfo().getContactType());
                    contactInfo.put("mobileNo", opsContactDetails.getContactInfo().getMobileNo());
                    contactInfo.put("email", opsContactDetails.getContactInfo().getEmail());
                    contactDetailsJson.put("contactInfo", contactInfo);
                    contactDetailsList.add(contactDetailsJson);
                }
                if (docDetails != null) {
                    if (getDocumentsBypax(docDetails, opsFlightPaxInfo.getPassengerID()).length() != 0) {
                        paxDetail.put("documentDetails", getDocumentsBypax(docDetails, opsFlightPaxInfo.getPassengerID()));
                    }
                }
                paxDetail.put("addressDetails", (addressDetails == null) ? new JSONObject() : addressDetails);
                paxDetail.put("specialRequests", specialRequests);
                paxDetail.put("contactDetails", (contactDetailsList.isEmpty()) ? new JSONArray() : contactDetailsList);
                paxDetail.put("ancillaryServices", (ancillaryServices == null) ? new JSONObject() : ancillaryServices);
                paxDetails.add(paxDetail);
            }
        }
        return paxDetails;
    }

    private JSONObject getPriceAirRequestBodyJson(OpsProduct opsProduct, String airSearchResponse) throws
            OperationException {
        JSONObject requestBody = new JSONObject();
        String tripType = opsProduct.getOrderDetails().getFlightDetails().getTripType();
        requestBody.put("tripType", tripType);
        requestBody.put("paxInfo", getPaxInfoListJsonAir(opsProduct));
        requestBody.put("pricedItinerary", getPricedItineraryListJson(airSearchResponse, opsProduct));
        requestBody.put("tripIndicator", opsProduct.getOrderDetails().getFlightDetails().getTripIndicator());
        return requestBody;
    }

    private List<JSONObject> getPaxInfoListJsonAir(OpsProduct opsProduct) throws JSONException {
        List<OpsFlightPaxInfo> paxInfos = opsProduct.getOrderDetails().getFlightDetails().getPaxInfo();
        List<JSONObject> paxInfoList = new ArrayList<>();
        Map<String, Integer> paxMap = new HashMap<>();
        for (OpsFlightPaxInfo opsFlightPaxInfo : paxInfos) {
            Integer count = paxMap.get(opsFlightPaxInfo.getPaxType());
            if (count != null) {
                paxMap.put(opsFlightPaxInfo.getPaxType(), ++count);
            } else {
                paxMap.put(opsFlightPaxInfo.getPaxType(), 1);
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

    private String priceNotEqual(OpsBooking opsBooking, OpsProduct opsProduct, Double newPrice, Double
            oldPrice, JSONObject airRepriceResponse, String accoReprice) throws OperationException {
        String message = "";
        if (newPrice < oldPrice) {
            switch (opsProduct.getOpsProductSubCategory()) {
                case PRODUCT_SUB_CATEGORY_FLIGHT:
                    message = bookAir(opsBooking, opsProduct, newPrice, airRepriceResponse);
                case PRODUCT_SUB_CATEGORY_HOTELS:
                    message = bookAcco(opsBooking, accoReprice, opsProduct, oldPrice);
            }
            //todo retain amount as company markup
        } else if (newPrice > oldPrice) {
            if (checkThreshold(opsBooking, opsProduct, newPrice, oldPrice)) { //less than threshold or equals to threshold
                switch (opsProduct.getOpsProductSubCategory()) {
                    case PRODUCT_SUB_CATEGORY_FLIGHT:
                        message = bookAir(opsBooking, opsProduct, newPrice, airRepriceResponse);
                    case PRODUCT_SUB_CATEGORY_HOTELS:
                        message = bookAcco(opsBooking, accoReprice, opsProduct, oldPrice);
                }
            } else {
                createTodo(null, opsBooking.getBookID(), opsProduct.getOrderID(), ToDoTaskNameValues.SEARCH_AND_BOOK, "price is higher than threshold", ToDoFunctionalAreaValues.OPERATIONS, MDMClientType.fromString(opsBooking.getClientType()));
                message = "The new Price is higher than threshold, A todo task is created for the ops user";
            }
        }
        return message;
    }

    //threshold needs to be configured for all the combinations of product category and sub-category
    private Boolean checkThreshold(OpsBooking opsBooking, OpsProduct opsProduct, Double newPrice, Double oldPrice) {
        JSONObject thresholdSearchRequest = new JSONObject();
        thresholdSearchRequest.put("companyMarket", opsBooking.getClientMarket()); //TODO not sure
        thresholdSearchRequest.put("clientType", opsBooking.getClientType());
        thresholdSearchRequest.put("productCategory", opsProduct.getProductCategory());
        thresholdSearchRequest.put("productCategorySubType", opsProduct.getProductSubCategory());
        Integer numOfPax = 1;
        switch (opsProduct.getOpsProductSubCategory()) {
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                numOfPax = opsProduct.getOrderDetails().getFlightDetails().getPaxInfo().size();
            case PRODUCT_SUB_CATEGORY_HOTELS:
                numOfPax = opsProduct.getOrderDetails().getHotelDetails().getRooms().get(0).getPaxInfo().size();
        }
        Double priceDiff = 0.0;
        priceDiff = newPrice - oldPrice;
        JSONObject thresholdConfiguration = (JSONObject) failureThresholdConfigurationService.searchThresholdConfiguration(thresholdSearchRequest).get(0);
        Double maxThresholdValue = thresholdConfiguration.getDouble("maxThresholdValue");
        return priceDiff <= maxThresholdValue * numOfPax;
    }

    //getting price from reprice response to compare with the earlier price
    private Double getAccoAmountAfterReprice(OpsProduct opsProduct, String accoRePriceResponse) {
        Double amount = 0.0;
        for (OpsRoom opsRoom : opsProduct.getOrderDetails().getHotelDetails().getRooms()) {
            List<TotalAmountResource> amountResourceList = null;
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = null;
            String childJson = null;
            try {
                node = mapper.readTree(accoRePriceResponse).at("/responseBody/accommodationInfo");
                node = node.findValue("roomStay");
                childJson = node.toString();
                SimpleModule module = new SimpleModule();
                module.addDeserializer(TotalAmountResource.class, new TotalAmountDeserializer());
                mapper.registerModule(module);
                amountResourceList = mapper.readValue(childJson, new TypeReference<List<TotalAmountResource>>() {
                });
                for (TotalAmountResource totalAmountResource : amountResourceList) {
                    if (totalAmountResource.getRoomTypeName().equalsIgnoreCase(opsRoom.getRoomTypeInfo().getRoomTypeName())
                            && totalAmountResource.getRoomCategoryCode().equalsIgnoreCase(opsRoom.getRoomTypeInfo().getRoomCategoryID())
                            && totalAmountResource.getRoomCategoryName().equalsIgnoreCase(opsRoom.getRoomTypeInfo().getRoomCategoryName())
                            && totalAmountResource.getRoomRef().equalsIgnoreCase(opsRoom.getRoomTypeInfo().getRoomRef())
                            && totalAmountResource.getRoomTypeCode().equalsIgnoreCase(opsRoom.getRoomTypeInfo().getRoomTypeCode())) {
                        amount += totalAmountResource.getAmount();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                logger.error("unable to parse JSON to get total price");
            }
        }
        return amount;
    }

    private String bookAir(OpsBooking opsBooking, OpsProduct opsProduct, Double amount, JSONObject
            airRepriceResponse) throws OperationException {
        String message = "";

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(this.airBookServiceUrl);
        JSONObject pricedItinerary = (JSONObject) airRepriceResponse.get("responseBody");

        JSONArray array = getPaymentInfo(opsBooking.getPaymentInfo(), amount);
        JSONObject requestBody = new JSONObject();
        requestBody.put("bookID", opsBooking.getBookID() + "+" + opsProduct.getOrderID());
        //requestBody.put("bookID", opsBooking.getBookID());
        requestBody.put("tripType", opsProduct.getOrderDetails().getFlightDetails().getTripType());
        requestBody.put("pricedItinerary", pricedItinerary.get("pricedItinerary"));
        requestBody.put("paxInfo", getPaxInfoListJsonAir(opsProduct));
        requestBody.put("paxDetails", getPaxDetailsJsonAir(opsProduct));
        requestBody.put("paymentInfo", array);

        JSONObject request = new JSONObject();
        request.put("requestBody", requestBody);
        JSONObject requestHeader = null;
        try {
            requestHeader = new JSONObject(jsonObjectProvider.getChildJSON(String.valueOf(airRepriceResponse), "$.responseHeader"));
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("unable to get Request Header");
        }
        request.put("requestHeader", requestHeader);
        ResponseEntity<String> result = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(request.toString(), headers);
        try {
            logger.info("***In Air Book***");
            logger.info("Printing book request");
            logger.info(request.toString());
            result = RestUtils.exchange(uriComponentsBuilder.toUriString(), HttpMethod.POST, httpEntity, String.class);
            logger.info("Printing Book Response");
            logger.info(result.getBody());
            String responseValidation = "";
            try {
                responseValidation = jsonObjectProvider.getAttributeValue(result.getBody(), "$.responseBody.supplierBookReferences[0].status", String.class);
                logger.info("Book is not successful");
            } catch (Exception e) {
                logger.info("Booking is successful");
            }
            if (responseValidation == null) {
                responseValidation = jsonObjectProvider.getAttributeValue(result.getBody(), "$.responseBody.supplierBookReferences[0].airlinePNR", String.class);
            }
            if (responseValidation.equalsIgnoreCase("Failure")) {
                message = "Booking of Air product is not successful";
                sendAlert(opsBooking.getBookID(), opsAlertUnsuccessfulBookingName, opsAlertUnsuccessfulBookingType);
            } else if (!StringUtils.isEmpty(responseValidation)) {
                //sending alert to ops user
                sendAlert(opsBooking.getBookID(), opsAlertName, opsAlertType);
                //sending communication to client about the successful booking
                sendCommunication(opsBooking.getBookID());
                //updating order attribute removing failure flag
                removeFailureFlagFromOrder(opsBooking, opsProduct);
                message = "Booking is successful";

            } else {
                sendAlert(opsBooking.getBookID(), opsAlertUnsuccessfulBookingName, opsAlertUnsuccessfulBookingType);
                message = "Booking is un successful";
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendAlert(opsBooking.getBookID(), opsAlertUnsuccessfulBookingName, opsAlertUnsuccessfulBookingType);
            logger.info("booking for Air is unsuccessful");
        }
        return message;
    }


    //the fields are not finalized by BE and not sending to us properly needs to be changed
    private JSONArray getPaymentInfo(List<OpsPaymentInfo> opsPaymentInfos, Double amount) {
        JSONArray paymentInfoList = new JSONArray();
        for (OpsPaymentInfo opsPaymentInfo : opsPaymentInfos) {
            JSONObject paymentInfo = new JSONObject();
            paymentInfo.put("merchantId", "197");
            paymentInfo.put("amountCurrency", "");
            paymentInfo.put("amountPaid", "100.00");
            paymentInfo.put("bankAccount", "");
            paymentInfo.put("bankName", "");
            paymentInfo.put("cardType", "");
            paymentInfo.put("depositRefNumber", "");
            paymentInfo.put("merchantTxnId", "M123");
            paymentInfo.put("nameOnCard", "");
            paymentInfo.put("noOfProducts", "");
            paymentInfo.put("paymentMethod", "Credit Card");
            paymentInfo.put("paymentStatus", "success");
            paymentInfo.put("paymentType", "Full");
            paymentInfo.put("totalAmount", "");
            paymentInfo.put("transactionDate", "2018-05-09");
            paymentInfo.put("transactionRefNumber", "");
            paymentInfo.put("toBankName", "");
            paymentInfo.put("Ifsc/SwiftCode", "");
            paymentInfo.put("IntermedaryBankIfsc/SwiftCode", "");
            paymentInfo.put("IvrDetails", "");
            paymentInfo.put("contactPerson", "");
            paymentInfo.put("Address", "");
            paymentInfo.put("mobileNumber", "");
            paymentInfo.put("cheque/DDNumber", "");
            paymentInfo.put("giftVoucherNumber", "");
            paymentInfo.put("giftVoucherPin", "");
            paymentInfo.put("offerIDs", new JSONObject());
            paymentInfoList.put(paymentInfo);
            //paymentInfo.put("amountPaid", amount);
            //paymentInfo.put("transactionDate", (opsPaymentInfo.getTransactionDate() == null) ? "" : opsPaymentInfo.getTransactionDate());
            //paymentInfo.put("amountCurrency", (opsPaymentInfo.getAmountCurrency() == null) ? "INR" : opsPaymentInfo.getAmountCurrency());
            //paymentInfo.put("merchantTxnId", "");
            //paymentInfo.put("paymentMethod", (opsPaymentInfo.getPaymentMethod() == null) ? "" : opsPaymentInfo.getPaymentMethod());
            //paymentInfo.put("paymentStatus", (opsPaymentInfo.getPayStatus() == null) ? "" : opsPaymentInfo.getPayStatus());
            //paymentInfo.put("paymentType", (opsPaymentInfo.getPaymentType() == null) ? "" : opsPaymentInfo.getPaymentType());
            //paymentInfo.put("totalAmount", (opsPaymentInfo.getTotalAmount() == null) ? "" : opsPaymentInfo.getTotalAmount());
        }
        return paymentInfoList;
    }

    private String bookAcco(OpsBooking opsBooking, String accoRepriceResponse, OpsProduct opsProduct, Double amount) {
        ResponseEntity<String> result = null;

        String message = "";
        JSONObject requestHeader = null;
        try {
            requestHeader = new JSONObject(jsonObjectProvider.getChildJSON(accoRepriceResponse, "$.responseHeader"));
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("unable to get Request Header");
        }
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(this.accoBookServiceUrl);
        JSONObject accoBookRequest = new JSONObject();
        accoBookRequest.put("requestHeader", requestHeader);
        accoBookRequest.put("requestBody", getAccoBookRequest(opsBooking, accoRepriceResponse, opsProduct, amount));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity(accoBookRequest.toString().replace("\\", ""), headers);
        try {
            logger.info("**** The Book request ***");
            logger.info(accoBookRequest.toString().replace("\\", ""));
            result = RestUtils.exchange(uriComponentsBuilder.toUriString(), HttpMethod.POST, httpEntity, String.class);
            String status = jsonObjectProvider.getAttributeValue(result.getBody(), "$.responseBody.accommodationInfo[0].status", String.class);
            logger.info("***************************");
            logger.info("Printing book response");
            logger.info(result.getBody());
            if (status.equalsIgnoreCase("CONF")) {
                logger.info("Booking is successful for the book id" + opsBooking.getBookID());
                message = "The Booking is successful";
                removeFailureFlagFromOrder(opsBooking, opsProduct);
                sendAlert(opsBooking.getBookID(), opsAlertName, opsAlertType);
                sendCommunication(opsBooking.getBookID());
            } else if (status.equalsIgnoreCase("FAILED")) {
                logger.info("The Booking is not successful for the book id " + opsBooking.getBookID() + "and order id " + opsProduct.getOrderID());
                sendAlert(opsBooking.getBookID(), opsAlertUnsuccessfulBookingName, opsAlertUnsuccessfulBookingType);
                message = "The Booking is not successful for the book id " + opsBooking.getBookID() + "and order id " + opsProduct.getOrderID();
                createTodo(null, opsBooking.getBookID(), opsProduct.getOrderID(), ToDoTaskNameValues.SEARCH_AND_BOOK, "Search to book auto job failed handle this process manually", ToDoFunctionalAreaValues.OPERATIONS, MDMClientType.fromString(opsBooking.getClientType()));
            } else if (status.equalsIgnoreCase("RESERVED")) {
                logger.info("The Booking is in RESERVED status from the supplier");
                sendAlert(opsBooking.getBookID(), opsAlertUnsuccessfulBookingName, opsAlertUnsuccessfulBookingType);
                message = "The booking is in ON REQUEST from the supplier";
                //this means the status from supplier is not confirmed we need to see whats the scenario for this
                /*removeFailureFlagFromOrder(opsBooking, opsProduct);
                sendAlert(opsBooking.getBookID(), opsAlertName, opsAlertType);
                sendCommunication(opsBooking.getBookID(), opsBooking.getClientID(), opsBooking.getClientType());*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendAlert(opsBooking.getBookID(), opsAlertUnsuccessfulBookingName, opsAlertUnsuccessfulBookingType);
            logger.error("Booking is not successful");
        }
        return message;
    }

    @Override
    public String reportToTechnical(String bookID) throws OperationException {
        OpsBooking opsBooking = opsBookingService.getBooking(bookID);
        String message = null;
        try {
            message = createTodo(null, bookID, null, ToDoTaskNameValues.INVESTIGATE_AND_ACTION, "Investigate and action", ToDoFunctionalAreaValues.TECHNICAL_TEAM, MDMClientType.fromString(opsBooking.getClientType()));
            sendAlert(bookID, techAlertName, techAlertType);
            message = "Todo task was created successfully to technical team";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error while creating todo task");
        }
        return message;
    }

    @Override
    public EmailResponse sendCommunication(String bookID) throws OperationException {
        String clientMail = null;
        String clientName = null;
        EmailResponse emailResponse = null;
        OpsBooking opsBooking = opsBookingService.getBooking(bookID);
        String clientID = opsBooking.getClientID();
        String clientType = opsBooking.getClientType();
        if (!StringUtils.isEmpty(clientID) && !StringUtils.isEmpty(clientType)) {
            try {
                if (clientType.equalsIgnoreCase(MDMClientType.B2B.getClientType())) {
                    clientMail = clientMasterDataService.getClientEmailId(clientID, MDMClientType.fromString(clientType));
                    clientName = clientMasterDataService.getB2BClientNames(Collections.singletonList(clientID)).get(clientID);
                } else if (clientType.equalsIgnoreCase(MDMClientType.B2C.toString())) {
                    clientMail = clientMasterDataService.getClientEmailId(clientID, MDMClientType.fromString(clientID));
                    clientName = clientMasterDataService.getB2CClientNames(Collections.singletonList(clientID)).get(clientID);
                }
            } catch (Exception e) {
                logger.info("Error in fetching client details");
                e.printStackTrace();
            }
            Map<String, String> map = new HashMap<>();
            map.put("ClientName", clientName);
            map.put("BookID", bookID);
            map.put("status", String.valueOf(opsBooking != null && opsBooking.getStatus().getBookingStatus() != null ? opsBooking.getStatus().getBookingStatus() : " "));
            String subject = "Booking " + (opsBooking != null ? opsBooking.getStatus().getBookingStatus() : null);

            try {
                emailResponse = emailUtils.buildClientMail(function, scenario, clientMail, subject, map, null, null);
            } catch (Exception e) {
                logger.error("unable to send communication to client");
                throw new OperationException(Constants.OPS_ERR_20016);
            }
            if (emailResponse != null && emailResponse.getStatus().equals("SUCCESS")) {
                updateCommunicationCount(bookID);
            }
        } else {
            throw new OperationException("Client Id or Client Type is not defined for this booking");
        }

        return emailResponse;
    }

    private JSONObject getAccoBookRequest(OpsBooking opsBooking, String accoRepriceResponse, OpsProduct
            opsProduct, Double amount) {
        JSONObject request = new JSONObject();
        JSONObject accommodationInfo = new JSONObject();
        accommodationInfo.put("accommodationSubType", opsProduct.getProductSubCategory());
        OpsRoom opsRoom = opsProduct.getOrderDetails().getHotelDetails().getRooms().get(0);
        accommodationInfo.put("checkIn", opsRoom.getCheckIn());
        accommodationInfo.put("checkOut", opsRoom.getCheckOut());
        accommodationInfo.put("supplierRef", opsProduct.getSupplierID());
        accommodationInfo.put("countryCode", opsProduct.getOrderDetails().getHotelDetails().getCountryCode());
        accommodationInfo.put("cityCode", opsProduct.getOrderDetails().getHotelDetails().getCityCode());
        List<JSONObject> roomConfigs = new ArrayList<>();
        JSONObject roomConfig = new JSONObject();
        List<JSONObject> roomInfoList = jsonObjectProvider.getChildrenCollection(accoRepriceResponse, "$.responseBody.accommodationInfo[*].roomStay[*].roomInfo", RoomInfo.class);
        try {
            roomConfig.put("paxInfo", getPaxInfoAcco(opsProduct));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject roomInfoRequest = new JSONObject(roomInfoList.get(0));
        JSONObject mealInfoObject = roomInfoRequest.getJSONObject("mealInfo").put("mealCode", "");
        JSONObject roomInfo = roomInfoRequest.put("mealInfo", mealInfoObject);
        try {
            roomConfig.put("roomInfo", roomInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        roomConfigs.add(roomConfig);
        accommodationInfo.put("roomConfig", roomConfigs);
        JSONArray accommodationArray = new JSONArray();
        accommodationArray.put(accommodationInfo);
        request.put("accommodationInfo", accommodationArray);
        request.put("paymentInfo", getPaymentInfo(opsBooking.getPaymentInfo(), amount));
        request.put("bookID", opsBooking.getBookID() + "+" + opsProduct.getOrderID());
        //request.put("bookID", opsBooking.getBookID());
        return request;
    }

    private JSONArray getPaxInfoAcco(OpsProduct opsProduct) {
        List<OpsAccommodationPaxInfo> opsAccommodationPaxInfos = opsProduct.getOrderDetails().getHotelDetails().getRooms().get(0).getPaxInfo();
        JSONArray paxInfoArray = new JSONArray();
        JSONArray documents = null;
        try {
            documents = getDocumentByOrder(opsProduct.getOrderID(), opsProduct.getProductSubCategory());
        } catch (Exception e) {
            logger.error("error in fetching document details");
            e.printStackTrace();
        }
        for (OpsAccommodationPaxInfo opsAccommodationPaxInfo : opsAccommodationPaxInfos) {
            JSONObject paxInfo = new JSONObject();
            paxInfo.put("isLeadPax", opsAccommodationPaxInfo.getLeadPax());
            paxInfo.put("firstName", opsAccommodationPaxInfo.getFirstName());
            paxInfo.put("paxType", opsAccommodationPaxInfo.getPaxType());
            paxInfo.put("surname", opsAccommodationPaxInfo.getLastName());
            paxInfo.put("dob", opsAccommodationPaxInfo.getBirthDate());
            paxInfo.put("middleName", opsAccommodationPaxInfo.getMiddleName());
            paxInfo.put("title", opsAccommodationPaxInfo.getTitle());
            JSONArray contactDetailsList = new JSONArray();
            for (OpsContactDetails opsContactDetails : opsAccommodationPaxInfo.getContactDetails()) {
                JSONObject contactDetails = new JSONObject();
                JSONObject contactInfo = new JSONObject();
                contactInfo.put("countryCode", opsContactDetails.getContactInfo().getCountryCode());
                contactInfo.put("contactType", opsContactDetails.getContactInfo().getContactType());
                contactInfo.put("mobileNo", opsContactDetails.getContactInfo().getMobileNo());
                contactInfo.put("email", opsContactDetails.getContactInfo().getEmail());
                contactDetails.put("contactInfo", contactInfo);
                contactDetailsList.put(contactDetails);
            }
            paxInfo.put("contactDetails", contactDetailsList);
            JSONObject addressDetails = new JSONObject();
            addressDetails.put("zip", opsAccommodationPaxInfo.getAddressDetails().getZipCode());
            addressDetails.put("country", opsAccommodationPaxInfo.getAddressDetails().getCountryName());
            addressDetails.put("city", opsAccommodationPaxInfo.getAddressDetails().getCityName());
            addressDetails.put("addrLine1", opsAccommodationPaxInfo.getAddressDetails().getAddressLines().get(0));
            addressDetails.put("addrLine2", opsAccommodationPaxInfo.getAddressDetails().getAddressLines().get(1));
            addressDetails.put("state", opsAccommodationPaxInfo.getAddressDetails().getState());
            paxInfo.put("addressDetails", addressDetails);
            if (documents != null) {
                if (getDocumentsBypax(documents, opsAccommodationPaxInfo.getPaxID()).length() != 0) {
                    paxInfo.put("documentDetails", getDocumentsBypax(documents, opsAccommodationPaxInfo.getPaxID()));
                }
            }
            paxInfoArray.put(paxInfo);
        }
        return paxInfoArray;
    }

    private JSONArray checkSupplierAir(OpsBooking opsBooking, OpsProduct opsProduct, String bookRefId, String
            supplierRef) {
        JSONObject supplierBookReference = new JSONObject();
        supplierBookReference.put("supplierRef", supplierRef);
        supplierBookReference.put("bookRefID", bookRefId); //airlinePNR
        JSONArray supplierBookReferences = new JSONArray();
        supplierBookReferences.put(supplierBookReference);
        JSONObject requestBody = new JSONObject();
        requestBody.put("supplierBookReferences", supplierBookReferences);
        JSONObject request = new JSONObject();
        //for request header we need to do search and get the request header from there as per the BE API
        JSONObject airSearchRequest = getAirSearchRequest(opsBooking, opsProduct);
        ResponseEntity<String> airSearchResponse = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity(airSearchRequest.toString(), headers);
        ZonedDateTime time = ZonedDateTime.now();
        try {
            airSearchResponse = RestUtils.exchange(airSearchServiceUrl, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Search is unsuccessful for Air product");
        }
        logger.info("*****************************************************");
        logger.info(Duration.between(ZonedDateTime.now(), time));
        logger.info("printing the time taken by search");
        JSONObject searchRequestHeader = null;
        if (airSearchResponse != null) {
            searchRequestHeader = new JSONObject(jsonObjectProvider.getChildJSON(airSearchResponse.getBody(), "$.responseHeader"));
        }
        request.put("requestHeader", searchRequestHeader);
        request.put("requestBody", requestBody);
        HttpEntity<String> httpEntity = new HttpEntity<>(request.toString(), headers);
        ResponseEntity<String> response = null;
        try {
            response = RestUtils.postForEntity(airRetrieveServiceUrl, httpEntity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        if (response != null) {
            return new JSONArray(jsonObjectProvider.getAttributeValue(response.getBody(), "$.responseBody.pricedItinerary", String.class));
        }
        return new JSONArray();
    }

    private JSONArray checkSupplierAcco(String accommodationSubType, String supplierRef, String
            supplierReservationId, JSONObject requestHeader) {
        JSONObject accommodationInfo = new JSONObject();
        accommodationInfo.put("supplierRef", supplierRef);
        accommodationInfo.put("supplierReservationId", supplierReservationId);
        accommodationInfo.put("accommodationSubType", accommodationSubType);

        JSONObject requestBody = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(accommodationInfo);
        requestBody.put("accommodationInfo", jsonArray);

        JSONObject request = new JSONObject();
        request.put("requestHeader", requestHeader);
        request.put("requestBody", requestBody);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

        ResponseEntity<String> response = RestUtils.postForEntity(accoRetrieveServiceUrl, entity, String.class);
        return new JSONArray(jsonObjectProvider.getAttributeValue(response.getBody(), "$.responseBody.accommodationInfo", String.class));
    }

    //in some cases like search we don't have to send transaction id
    @Override
    public JSONObject getRequestHeader(OpsBooking booking, Boolean transaction) throws JSONException {
        JSONObject requestHeader = new JSONObject();
        requestHeader.put("userID", booking.getUserID());
        Random random = new Random();
        requestHeader.put("sessionID", booking.getSessionID() + random.nextInt(100)); // for generating random session id
        if (transaction) {
            requestHeader.put("transactionID", booking.getTransactionID());
        } else {
            requestHeader.put("transactionID", "");
        }
        requestHeader.put("clientContext", getClientContextJson(booking));
        return requestHeader;
    }

    private JSONObject getClientContextJson(OpsBooking opsBooking) {
        JSONObject clientContext = new JSONObject();
        clientContext.put("clientID", opsBooking.getClientID());
        clientContext.put("clientMarket", (opsBooking.getClientMarket() == null) ? "" : opsBooking.getClientMarket());
        clientContext.put("clientType", opsBooking.getClientType());
        clientContext.put("clientLanguage", (opsBooking.getClientLanguage() == null) ? "" : opsBooking.getClientLanguage());
        clientContext.put("clientIATANumber", (opsBooking.getClientIATANumber() == null) ? "" : opsBooking.getClientIATANumber());
        clientContext.put("pointOfSale", opsBooking.getPointOfSale());
        clientContext.put("clientCurrency", (opsBooking.getClientCurrency() == null) ? "" : opsBooking.getClientCurrency());
        clientContext.put("clientNationality", "");
        clientContext.put("clientCallbackAddress", "");
        return clientContext;
    }

    private String createTodo(AssignStaffResource assignStaffResource, String bookID, String
            orderId, ToDoTaskNameValues toDoTaskNameValues, String suggestedAction, ToDoFunctionalAreaValues
                                      toDoFunctionalArea, MDMClientType clientType) throws OperationException {
        String message = "";
        OpsBooking opsBooking = opsBookingService.getBooking(bookID);
        ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
        toDoTaskResource.setTaskFunctionalAreaId(toDoFunctionalArea.getValue());
        toDoTaskResource.setTaskNameId(toDoTaskNameValues.getValue());
        if (orderId != null) {
            toDoTaskResource.setProductId(opsBooking.getProducts().stream().filter(opsProduct -> opsProduct.getOrderID().equalsIgnoreCase(orderId)).findFirst().get().getProductSubCategory());
            toDoTaskResource.setReferenceId(orderId);
        } else {
            toDoTaskResource.setReferenceId(bookID);
            toDoTaskResource.setProductId("");
        }
        toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.MANAGE_FAILURE.toString());
        toDoTaskResource.setBookingRefId(bookID);
        toDoTaskResource.setSuggestedActions(suggestedAction);
        toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
        toDoTaskResource.setDueOnDate(ZonedDateTime.now().plusDays(5));
        toDoTaskResource.setCreatedByUserId("SYSTEM");
        toDoTaskResource.setClientTypeId(String.valueOf(clientType));
        toDoTaskResource.setTaskStatusId("Open");
        if (assignStaffResource != null) {
            String companyIdFromRQ = assignStaffResource.getCompanyID();
            if (companyIdFromRQ.isEmpty())
                throw new OperationException("Please provide companyId");
            if (companyIdFromRQ.equalsIgnoreCase(opsBooking.getCompanyId())) {
                toDoTaskResource.setCompanyId(opsBooking.getCompanyId());
            } else {
                throw new OperationException(String.format("booking with bookId %s is assigned to company having companyId %s. " +
                        "So we cannot assign this booking with another company having companyId %s. Please select the user of the same company", opsBooking.getBookID(), opsBooking.getCompanyId(), assignStaffResource.getCompanyID()));
            }
        }

        toDoTaskResource.setAssignedBy(userService.getLoggedInUserId());
        toDoTaskResource.setFileHandlerId(opsBooking.getStaffId());
        ToDoTask toDoTask = null;
        try {
            String checkDupplicates = checkDuplicateTodo(toDoTaskResource);
            if (checkDupplicates.equalsIgnoreCase("false")) {
                toDoTask = toDoTaskService.save(toDoTaskResource);
                logger.info("Todo task was created for " + bookID + " in Manage Failure");
                message = "Todo task was created successfully";
            } else {
                ToDoTaskResource toDoTaskResourceExisting = new ToDoTaskResource();
                ToDoTask existingTodo = toDoTaskService.getById(checkDupplicates);
                if (assignStaffResource != null) {
                    existingTodo.setFileHandlerId(opsBooking.getStaffId());
                }
                CopyUtils.copy(existingTodo, toDoTaskResourceExisting);
                toDoTaskResourceExisting.setTaskStatusId(existingTodo.getTaskStatus().getValue());

                toDoTaskService.save(toDoTaskResourceExisting);
                message = "A ToDo task updated for this process";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "Error while creating todo task";
            logger.error("error in creating todo task");
        }
        return message;
    }

    private String checkDuplicateTodo(ToDoTaskResource toDoTaskResource) {
        ToDoCriteria toDoCriteria = new ToDoCriteria();
        toDoCriteria.setReferenceId(toDoCriteria.getReferenceId());
        toDoCriteria.setTaskTypeId(toDoTaskResource.getTaskTypeId());
        toDoCriteria.setTaskSubTypeId(toDoTaskResource.getTaskSubTypeId());
        toDoCriteria.setTaskNameId(toDoTaskResource.getTaskNameId());
        toDoCriteria.setClientTypeId(toDoTaskResource.getClientTypeId());
        toDoCriteria.setProductId(toDoTaskResource.getProductId());
        toDoCriteria.setCompanyId(toDoTaskResource.getCompanyId());
        toDoCriteria.setBookingRefId(toDoTaskResource.getBookingRefId());
        ToDoResponse toDoResponse = null;
        try {
            toDoResponse = toDoTaskService.getByCriteria(toDoCriteria);
        } catch (OperationException e) {
            e.printStackTrace();
        }
//        return toDoResponse != null && toDoResponse.getContent() != null && toDoResponse.getContent().size() > 0;
        if (toDoResponse != null) {
            List<ToDoTaskResponse> todoResponses = toDoResponse.getData();
            StringBuilder todoIds = new StringBuilder();
            if(todoResponses != null && todoResponses.size() > 0 ) {
                todoIds.append(todoResponses.get(0).getId());
            }else {
                return "false";
            }

            //TODO: need to visit this again

            return todoIds.toString();
        } else {
            return "false";
        }
    }

    private String getDetailsSummaryAir(OpsFlightSegment opsFlightSegment) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return opsFlightSegment.getOriginLocation() + " to " + opsFlightSegment.getDestinationLocation() + " : " + opsFlightSegment.getOperatingAirline().getAirlineCode() +
                opsFlightSegment.getOperatingAirline().getFlightNumber() + " " + dtf.format(opsFlightSegment.getDepartureDateZDT()) + " / " + dtf.format(opsFlightSegment.getArrivalDateZDT());
    }

    private String getDetailsSummaryAcco(OpsRoom room) {
        return "Check In Date: " + room.getCheckIn() + " " + "Check out Date: " + room.getCheckOut();
    }

    //changing booking object to failureDetailsResource for UI in duplicates screen
    //not the efficient way, This should come from the duplicates search which would make it efficient
    @Override
    public FailureDetailsResource convert(String bookID) throws OperationException {
        OpsBooking opsBooking = opsBookingService.getBooking(bookID);
        FailureDetailsResource failureDetailsResource = new FailureDetailsResource();
        failureDetailsResource.setBookID(opsBooking.getBookID());
        failureDetailsResource.setBookingDate(String.valueOf(opsBooking.getBookingDateZDT().toOffsetDateTime()));
        failureDetailsResource.setClientID(opsBooking.getClientID());
        failureDetailsResource.setPaymentStatus(
                opsBooking.getPaymentInfo().stream().map(aPaymetInfo ->
                        aPaymetInfo.getPayStatus() != null ? aPaymetInfo.getPayStatus() : "")
                        .collect(Collectors.joining(",")));

        HashMap<String, String> userInfo = userMasterService.getUserInfo(Collections.singletonList(opsBooking.getStaffId()));

        if (!userInfo.isEmpty()) {
            failureDetailsResource.setFileHandlerName(userInfo.get(opsBooking.getStaffId()));
        }


        if (!StringUtils.isEmpty(opsBooking.getClientType())) {

            String clientDetails = null;

            try {
                if (opsBooking.getClientType().equalsIgnoreCase(ClientType.B2C.toString())) {
                    clientDetails = clientMasterDataService.getB2CClientDetails(opsBooking.getClientID());
                }

                if (opsBooking.getClientType().equalsIgnoreCase(ClientType.B2B.toString())) {
                    clientDetails = clientMasterDataService.getB2BClientDetails(opsBooking.getClientID());
                }
            } catch (Exception e) {
                logger.info("Failed to fetch client details for clint id " + opsBooking.getClientID(), e);
            }

            if (clientDetails != null) {
                String clientName = jsonObjectProvider.getAttributeValue(clientDetails,
                        "$.clientProfile.clientDetails.clientName", String.class);
                failureDetailsResource.setClientName(clientName);
            }

        }

        Optional<OpsPaymentInfo> aPaymentInfo =
                opsBooking.getPaymentInfo().stream().filter(opsPaymentInfo -> opsPaymentInfo.getPayStatus() != null &&
                        opsPaymentInfo.getPayStatus().equalsIgnoreCase("FAILED")).findFirst();

        if ( !StringUtils.isEmpty(opsBooking.getStatus()) &&
                ( opsBooking.getStatus().getBookingStatus().equalsIgnoreCase(OpsBookingStatus.CNF.toString())
                   || opsBooking.getStatus().getBookingStatus().equalsIgnoreCase(OpsBookingStatus.VCH.toString()) ) ) {
            failureDetailsResource.setFailureFlag("Successful Booking");
        } else if (aPaymentInfo.isPresent()) {
            failureDetailsResource.setFailureFlag("Payment Failure");
        } else {
            Optional<OpsProduct> aProduct
                    = opsBooking.getProducts().stream().filter(opsProduct ->
                    opsProduct.getOrderDetails().getOpsBookingAttribute() != null
                            && opsProduct.getOrderDetails().getOpsBookingAttribute().contains(OpsBookingAttribute.BF)).findFirst();

            if (aProduct.isPresent()) {
                failureDetailsResource.setFailureFlag("Failure Booking");
            }
        }

        failureDetailsResource.setClientType(opsBooking.getClientType());
        failureDetailsResource.setClientCategory(opsBooking.getClientCategory());
        failureDetailsResource.setClientSubCategory(opsBooking.getClientSubCategory());
        List<OpsProductSubCategory> summaryList = getProductSummary(opsBooking);
        List<ProductSummary> productSummaries = new ArrayList<>();
        for (OpsProductSubCategory summary : summaryList) {
            ProductSummary productSummary = new ProductSummary();
            productSummary.setProductSubCategory(summary.getSubCategory());
            List<OrderDetailsResource> orderDetailsResources = new ArrayList<>();
            for (OpsProduct opsProduct : opsBooking.getProducts()) {

                OrderDetailsResource orderDetailsResource = new OrderDetailsResource();
                orderDetailsResource.setOrderStatus(String.valueOf(opsProduct.getOrderDetails().getOpsOrderStatus()));
                orderDetailsResource.setOrderId(opsProduct.getOrderID());
                String supplierDetails = supplierDetailsService.getSupplierDetails(opsProduct.getSupplierID());
                JSONObject suppJsonObject = new JSONObject(supplierDetails);
                orderDetailsResource.setSupplierName(suppJsonObject.getJSONObject("supplier").getString("name"));
                Map<String, String> leadPaxName = getLeadPaxName(opsProduct);
                for (Map.Entry<String, String> entry : leadPaxName.entrySet()) {
                    if (entry.getKey().equalsIgnoreCase("LeadPaxName")) {
                        orderDetailsResource.setLeadPaxName(entry.getValue());
                    }

                    if (entry.getKey().equalsIgnoreCase("PaxCount")) {
                        orderDetailsResource.setPaxCount(Integer.parseInt(entry.getValue()));
                    }
                }


                List<OrderSummary> orderSummaries = new ArrayList<>();
                if (opsProduct.getOpsProductSubCategory().equals(summary)) {
                    if (opsProduct.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT)) {
                        for (OpsOriginDestinationOption opsOriginDestinationOption : opsProduct.getOrderDetails().getFlightDetails().getOriginDestinationOptions()) {
                            for (OpsFlightSegment opsFlightSegment : opsOriginDestinationOption.getFlightSegment()) {
                                OrderSummary orderSummary = new OrderSummary();
                                orderSummary.setTravelDate(String.valueOf(opsFlightSegment.getDepartureDateZDT()));
                                orderSummary.setDetailsSummary(getDetailsSummaryAir(opsFlightSegment));

                                if (!StringUtils.isEmpty(opsFlightSegment.getOperatingAirline())) {
                                    orderSummary.setProductName(opsFlightSegment.getOperatingAirline().getAirlineName());
                                }

                                orderSummaries.add(orderSummary);
                            }
                        }
                    } else if (opsProduct.getOpsProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS)) {
                        String hotelName = opsProduct.getOrderDetails().getHotelDetails().getHotelName();
                        for (OpsRoom opsRoom : opsProduct.getOrderDetails().getHotelDetails().getRooms()) {
                            OrderSummary orderSummary = new OrderSummary();
                            orderSummary.setDetailsSummary(getDetailsSummaryAcco(opsRoom));
                            orderSummary.setTravelDate(opsRoom.getCheckIn());
                            orderSummary.setProductName(hotelName);
                            orderSummaries.add(orderSummary);
                        }
                    }
                    orderDetailsResource.setOrderSummary(orderSummaries);
                    orderDetailsResources.add(orderDetailsResource);
                }
            }
            productSummary.setOrders(orderDetailsResources);
            productSummaries.add(productSummary);
        }
        StringBuilder companyName = new StringBuilder();
        companyName = companyName.append(companyMasterDataService.getCompanyNames(Collections.singletonList(opsBooking.getCompanyId())).get(opsBooking.getCompanyId())).append("\n")
                .append("Market -").append(" ").append(opsBooking.getCompanyMarket()).append(" ").append("\n")
                .append(opsBooking.getSbu()).append("/").append(opsBooking.getBu());
        failureDetailsResource.setCompanyDetails(companyName.toString());
        failureDetailsResource.setProductSummary(productSummaries);
        return failureDetailsResource;
    }

    private List<OpsProductSubCategory> getProductSummary(OpsBooking opsBooking) {
        List<OpsProductSubCategory> productSummaryList = new ArrayList<>();
        for (OpsProduct opsProduct : opsBooking.getProducts()) {
            productSummaryList.add(opsProduct.getOpsProductSubCategory());
        }
        return Lists.newArrayList(Sets.newHashSet(productSummaryList));
    }

    private void sendAlert(String bookID, String alertName, String alertType) {
        InlineMessageResource inlineMessageResource = new InlineMessageResource();
        inlineMessageResource.setAlertName(alertName);
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("bookId", bookID);
        inlineMessageResource.setDynamicVariables(map);
        inlineMessageResource.setNotificationType(alertType);
        try {
            alertService.sendInlineMessageAlert(inlineMessageResource);
            logger.info("Alert was sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("There was an error in sending alert");
            //throw new OperationException("There was an error in sending alert");
        }
    }


    private void updateCommunicationCount(String bookID) {
        FailureDetails failureDetails = null;
        try {
            if (failureRepository.getExists(bookID)) {
                failureDetails = failureRepository.getByBookID(bookID);
                failureDetails.setCommunicationCount(failureDetails.getCommunicationCount() + 1);
            } else {
                failureDetails = new FailureDetails();
                failureDetails.setBookID(bookID);
                failureDetails.setCommunicationCount(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("no object found with the book ID " + bookID);
            //throw new OperationException("no object found with the book ID " + bookID);
        }
        failureRepository.saveOrUpdate(failureDetails);
    }

    public Integer getCommunicationCount(String bookID) {
        FailureDetails failureDetails = null;
        try {
            failureDetails = failureRepository.getByBookID(bookID);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("no object found with the book ID " + bookID);
            //throw new OperationException("no object found with the book ID " + bookID);
        }
        assert failureDetails != null;
        return failureDetails.getCommunicationCount();
    }

    private JSONObject airCancelRequest(OpsBooking opsBooking, OpsProduct opsProduct) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("cancelType", "ALL"); //for cancellation of the product
        requestBody.put("bookID", opsBooking.getBookID());
        JSONArray supplierBookingReferences = new JSONArray();
        JSONObject supplierBookingReference = new JSONObject();
        supplierBookingReference.put("orderID", opsProduct.getOrderID());
        supplierBookingReference.put("supplierRef", opsProduct.getSupplierID());
        supplierBookingReference.put("bookRefID", opsProduct.getOrderDetails().getFlightDetails().getAirlinePNR());
        supplierBookingReferences.put(supplierBookingReference);
        requestBody.put("supplierBookReferences", supplierBookingReferences);
        requestBody.put("paymentInfo", getPaymentInfo(opsBooking.getPaymentInfo(), null));
        JSONObject request = new JSONObject();
        //need to get Request header by doing search first and take the response header from search as per BE API
        JSONObject airSearchRequest = getAirSearchRequest(opsBooking, opsProduct);
        ResponseEntity<String> airSearchResponse = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(airSearchRequest.toString(), headers);
        ZonedDateTime time = ZonedDateTime.now();
        try {
            airSearchResponse = RestUtils.exchange(airSearchServiceUrl, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Search is unsuccessful for Air product");
        }
        logger.info("********************");
        logger.info(Duration.between(ZonedDateTime.now(), time));
        logger.info("printing time taken by BE search API");
        JSONObject searchRequestHeader = null;
        if (airSearchResponse != null) {
            searchRequestHeader = new JSONObject(jsonObjectProvider.getChildJSON(airSearchResponse.getBody(), "$.responseHeader"));
        }
        request.put("requestHeader", searchRequestHeader);
        request.put("requestBody", requestBody);
        return request;
    }

    private JSONObject accoSearchRequest(OpsBooking opsBooking, OpsProduct opsProduct) {
        JSONObject requestBody = new JSONObject();
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
            roomConfig.put("adultCount", setPaxCountAndChildAges(paxCount, childAges, paxInfo));
            roomConfig.put("childAges", childAges);
            roomConfigList.add(roomConfig);
        }
        requestBody.put("roomConfig", roomConfigList);
        JSONObject request = new JSONObject();
        request.put("requestHeader", getRequestHeader(opsBooking, false));
        request.put("requestBody", requestBody);

        return request;
    }


    private Integer setPaxCountAndChildAges(Integer
                                                    paxCount, List<Integer> childAges, List<OpsAccommodationPaxInfo> paxInfo) {
        for (OpsAccommodationPaxInfo opsAccommodationPaxInfo : paxInfo) {
            if (opsAccommodationPaxInfo.getPaxType().equalsIgnoreCase("ADT")) {
                paxCount++;
            }
            if (opsAccommodationPaxInfo.getPaxType().equalsIgnoreCase("CHD")) {
                String dob = opsAccommodationPaxInfo.getBirthDate();
                childAges.add(calculateAge(dob));
            }
        }
        return paxCount;
    }

    private Integer calculateAge(String dob) {
        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.parse(dob);
        Period period = Period.between(birthday, today);
        return period.getYears();
    }

    private JSONObject accoCancellationRequest(OpsBooking opsBooking, OpsProduct opsProduct) {
        JSONObject requeatHeader = getRequestHeader(opsBooking, true);
        JSONArray accommodationInfo = new JSONArray();
        List<OpsRoom> opsRooms = opsProduct.getOrderDetails().getHotelDetails().getRooms();
        for (OpsRoom opsRoom : opsRooms) {
            JSONObject accoInfo = new JSONObject();
            accoInfo.put("accommodationSubType", opsProduct.getProductSubCategory());
            accoInfo.put("orderID", opsProduct.getOrderID());
            accoInfo.put("checkIn", opsRoom.getCheckIn());
            accoInfo.put("supplierRef", opsProduct.getSupplierID());
            accoInfo.put("countryCode", opsProduct.getOrderDetails().getHotelDetails().getCountryCode());
            accoInfo.put("cityCode", opsProduct.getOrderDetails().getHotelDetails().getCityCode());
            accoInfo.put("checkOut", opsRoom.getCheckOut());
            accoInfo.put("modificationType", "FULLCANCELLATION"); //for cancellation of product
            accoInfo.put("supplierReferenceId", opsProduct.getSupplierReferenceId());
            accoInfo.put("clientReferenceId", opsProduct.getClientReferenceId());
            accoInfo.put("supplierCancellationId", opsProduct.getSupplierCancellationId());
            accoInfo.put("supplierReservationId", opsProduct.getSupplierReservationId());
            accoInfo.put("supplierRoomIndex", opsRoom.getSupplierRoomIndex());
            JSONArray paxInfoArray = new JSONArray();
            for (OpsAccommodationPaxInfo accommodationPaxInfo : opsProduct.getOrderDetails().getHotelDetails().getRooms().get(0).getPaxInfo()) {
                JSONObject paxInfo = new JSONObject(accommodationPaxInfo);
                paxInfoArray.put(paxInfo);
            }
            try {
                accoInfo.put("paxInfo", getPaxInfoAcco(opsProduct));
            } catch (Exception e) {
                e.printStackTrace();
            }
            accoInfo.put("roomInfo", accoCancellationService.getRoomInfo(opsRoom, opsProduct.getOrderDetails().getHotelDetails()));
            accommodationInfo.put(accoInfo);
        }
        JSONObject requestBody = new JSONObject();
        requestBody.put("accommodationInfo", accommodationInfo);
        requestBody.put("bookID", opsBooking.getBookID());
        JSONObject request = new JSONObject();
        request.put("requestHeader", requeatHeader);
        request.put("requestBody", requestBody);
        return request;
    }


    //fetching document details by order
    @Override
    public JSONArray getDocumentByOrder(String orderID, String productSubCategory) {
        JSONObject request = new JSONObject();
        request.put("orderID", orderID);
        request.put("productSubCategory", productSubCategory);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(request.toString(), headers);
        ResponseEntity<String> docDetails = null;
        try {
            docDetails = RestUtils.exchange(docUrl, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("unable to find document details for the provided combination");
            //throw new OperationException("unable  to find document details");
        }
        if (docDetails != null) {
            return (JSONArray) new JSONObject(docDetails.getBody()).get("PassengerDocumentDetails");
        }
        return new JSONArray();
    }

    //filtering document details for each pax
    @Override
    public JSONObject getDocumentsBypax(JSONArray docDetails, String paxID) {
        if (docDetails != null) {
            for (Integer i = 0; i < docDetails.length(); i++) {
                JSONObject docOfPax = (JSONObject) docDetails.get(i);
                if (docOfPax.get("paxID").equals(paxID)) {
                    return docOfPax.getJSONObject("documentDetails");
                }
            }
        }
        return new JSONObject();
    }

    //filtering search response based on roomTypename, hotelName, roomCategoryName
    private JSONObject getAccoPriceRequestBody(String accoSearchResponse, OpsProduct opsProduct) {
        List<RoomInfo> roomInfoList = new ArrayList<>();
        List<RoomInfo> roomInfos = jsonObjectProvider.getChildrenCollection(accoSearchResponse, "$.responseBody.accommodationInfo[*].roomStay[*].roomInfo", RoomInfo.class);
        OpsRoom opsRoom = opsProduct.getOrderDetails().getHotelDetails().getRooms().get(0);
        for (RoomInfo roomInfo : roomInfos) {
            if (roomInfo.getRoomTypeInfo().getRoomTypeName().equals(opsRoom.getRoomTypeInfo().getRoomTypeName())
                    && roomInfo.getRoomTypeInfo().getRoomCategoryName().equals(opsRoom.getRoomTypeInfo().getRoomCategoryName())
                    && roomInfo.getHotelInfo().getHotelName().equals(opsProduct.getOrderDetails().getHotelDetails().getHotelName())) {
                roomInfoList.add(roomInfo);
            }
        }
        if (roomInfoList.size() == 0) {
            logger.error("no results found for the product in search");
        }
        JSONObject accommodationInfo = new JSONObject();
        accommodationInfo.put("countryCode", opsProduct.getOrderDetails().getHotelDetails().getCountryCode());
        accommodationInfo.put("cityCode", opsProduct.getOrderDetails().getHotelDetails().getCityCode());
        accommodationInfo.put("hotelCode", opsProduct.getOrderDetails().getHotelDetails().getHotelCode());
        accommodationInfo.put("accommodationSubType", opsProduct.getProductSubCategory());
        accommodationInfo.put("supplierRef", opsProduct.getSupplierID());
        accommodationInfo.put("countryCode", opsProduct.getOrderDetails().getHotelDetails().getCountryCode());
        accommodationInfo.put("cityCode", opsProduct.getOrderDetails().getHotelDetails().getCityCode());
        accommodationInfo.put("checkIn", opsRoom.getCheckIn());
        accommodationInfo.put("checkOut", opsRoom.getCheckOut());
        accommodationInfo.put("paxNationality", "IN");
        int paxCount = 0;
        List<Integer> childAges = new ArrayList<>();
        for (OpsAccommodationPaxInfo opsAccommodationPaxInfo : opsRoom.getPaxInfo()) {
            if (opsAccommodationPaxInfo.getPaxType().equalsIgnoreCase("ADT")) {
                paxCount++;
            }
            if (opsAccommodationPaxInfo.getPaxType().equalsIgnoreCase("CHD")) {
                String dob = opsAccommodationPaxInfo.getBirthDate();
                childAges.add(calculateAge(dob));
            }
        }
        JSONArray roomConfigs = new JSONArray();
        for (RoomInfo roomInfo : roomInfoList) {
            JSONObject roomConfig = new JSONObject();
            roomConfig.put("adultCount", paxCount);
            roomConfig.put("childAges", childAges);
            try {
                roomConfig.put("roomInfo", new JSONObject(objectMapper.writeValueAsString(roomInfo)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            roomConfigs.put(roomConfig);
        }
        accommodationInfo.put("roomConfig", roomConfigs);

        JSONArray accommodationInfos = new JSONArray();
        accommodationInfos.put(accommodationInfo);
        JSONObject requestBody = new JSONObject();
        requestBody.put("accommodationInfo", accommodationInfos);
        return requestBody;
    }

    private JSONObject getAirSearchRequest(OpsBooking opsBooking, OpsProduct opsProduct) {
        JSONObject requestHeader = getRequestHeader(opsBooking, true);
        JSONObject requestBody = new JSONObject();
        //requestBody.put("tripIndicator", opsProduct.getOrderDetails().getFlightDetails().getTripIndicator());
        requestBody.put("tripType", opsProduct.getOrderDetails().getFlightDetails().getTripType());
        JSONArray originDestinationOptions = new JSONArray();
        for (OpsOriginDestinationOption opsOriginDestinationOption : opsProduct.getOrderDetails().getFlightDetails().getOriginDestinationOptions()) {

            for (OpsFlightSegment opsFlightSegment : opsOriginDestinationOption.getFlightSegment()) {
                JSONObject originDestinationOption = new JSONObject();
                originDestinationOption.put("destinationLocation", opsFlightSegment.getDestinationLocation());
                originDestinationOption.put("originLocation", opsFlightSegment.getOriginLocation());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                originDestinationOption.put("departureDate", opsFlightSegment.getDepartureDateZDT().format(formatter));
                originDestinationOptions.put(originDestinationOption);
            }
        }
        requestBody.put("originDestinationInfo", originDestinationOptions);
        requestBody.put("paxInfo", getPaxInfoListJsonAir(opsProduct));
        requestBody.put("cabinType", opsProduct.getOrderDetails().getFlightDetails().getOriginDestinationOptions().get(0).getFlightSegment().get(0).getCabinType());
        JSONObject request = new JSONObject();
        request.put("requestHeader", requestHeader);
        request.put("requestBody", requestBody);
        return request;
    }

    private String getErrorMessage(String errorResponse) {
        return jsonObjectProvider.getAttributeValue(errorResponse, "$.errorMessage", String.class);
    }

    @Override
    public JSONObject getUpdateAttributeRequest(OpsBooking opsBooking, OpsProduct opsProduct) {
        JSONObject request = new JSONObject();
        request.put("requestHeader", getRequestHeader(opsBooking, true));

        JSONObject requestBody = new JSONObject();
        List<OpsBookingAttribute> opsBookingAttributes = opsProduct.getOrderDetails().getOpsBookingAttribute();
        JSONArray updatedAttributes = new JSONArray();
        for (OpsBookingAttribute opsBookingAttribute : opsBookingAttributes) {
            if (!opsBookingAttribute.equals(OpsBookingAttribute.BF)) {
                updatedAttributes.put(opsBookingAttribute);
            }
        }
        requestBody.put("product", opsProduct.getProductSubCategory());
        requestBody.put("bookingAttribute", updatedAttributes);
        requestBody.put("orderID", opsProduct.getOrderID());

        request.put("requestBody", requestBody);
        request.put("userID", opsBooking.getUserID());

        return request;
    }

    @Override
    public void removeFailureFlagFromOrder(OpsBooking opsBooking, OpsProduct opsProduct) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity(getUpdateAttributeRequest(opsBooking, opsProduct).toString(), headers);
        try {
            RestUtils.exchange(updateOrderAttribute, HttpMethod.PUT, entity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Error while updating order attribute");
        }
    }

    private Boolean checkSupplierStatus(OpsProductSubCategory opsProductSubCategory, JSONArray supplierResponse) {
        String status = "";
        if (opsProductSubCategory.equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS)) {
            status = jsonObjectProvider.getAttributeValue(supplierResponse.toString(), "$.[0].status", String.class);
            return status.equalsIgnoreCase("CONF");
        } else if (opsProductSubCategory.equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT)) {
            status = jsonObjectProvider.getAttributeValue(supplierResponse.toString(), "$.[0].orderStatus", String.class);
            return status.equalsIgnoreCase("confirmed");
        }
        return false;
    }

    private Map<String, String> getLeadPaxName(OpsProduct opsProduct) {
        Map<String, String> result = new HashMap<>();
        StringBuilder leadPaxName = null;
        String paxCount = null;
        int paxSize = 0;
        OpsProductCategory opsProductCategory = opsProduct.getOpsProductCategory();
        OpsProductSubCategory opsProductSubCategory = opsProduct.getOpsProductSubCategory();
        switch (opsProductCategory) {
            case PRODUCT_CATEGORY_TRANSPORTATION:
                switch (opsProductSubCategory) {
                    case PRODUCT_SUB_CATEGORY_FLIGHT:
                        OpsOrderDetails orderDetails = opsProduct.getOrderDetails();
                        List<OpsFlightPaxInfo> paxInfo = orderDetails.getFlightDetails().getPaxInfo();
                        paxCount = String.valueOf(paxInfo.size());
                        for (OpsFlightPaxInfo flightPaxInfo : paxInfo)
                        {
                            if (flightPaxInfo.getLeadPax().equals(Boolean.TRUE)) {
                                leadPaxName = new StringBuilder();
                                leadPaxName.append(flightPaxInfo.getTitle()).append(".").append(" ").append(flightPaxInfo.getFirstName()).append(" ").append(flightPaxInfo.getLastName());

                            }
                        }
                        break;

                }
                break;
            case PRODUCT_CATEGORY_ACCOMMODATION:
                switch (opsProductSubCategory) {
                    case PRODUCT_SUB_CATEGORY_HOTELS:
                        OpsOrderDetails orderDetails = opsProduct.getOrderDetails();
                        List<OpsRoom> rooms = orderDetails.getHotelDetails().getRooms();
                        for (OpsRoom opsRoom : rooms) {
                            paxSize = paxSize + opsRoom.getPaxInfo().size();
                            List<OpsAccommodationPaxInfo> paxInfo = opsRoom.getPaxInfo();
                            for (OpsAccommodationPaxInfo opsAccommodationPaxInfo : paxInfo) {
                                Boolean leadPax = opsAccommodationPaxInfo.getLeadPax();
                                if (!StringUtils.isEmpty(leadPax)) {
                                    if (leadPax.equals(Boolean.TRUE)) {
                                        leadPaxName = new StringBuilder();
                                        leadPaxName.append(opsAccommodationPaxInfo.getTitle()).append(".").append(" ").append(opsAccommodationPaxInfo.getFirstName()).append(" ").append(opsAccommodationPaxInfo.getLastName());
                                        break;

                                    }
                                }
                            }
                        }
                        paxCount = String.valueOf(paxSize);
                        break;

                }
        }
        result.put("LeadPaxName", String.valueOf(leadPaxName));
        result.put("PaxCount", paxCount);
        return result;

    }

    @Override
    public String startCancellationProcess(String bookingId) throws OperationException {
        String s = null;
        List<String> orderIds = new ArrayList<>();
        OpsBooking opsBooking = opsBookingService.getBooking(bookingId);
        List<OpsProduct> products = opsBooking.getProducts();
        for (OpsProduct opsProduct : products) {
            OpsProductCategory opsProductCategory = opsProduct.getOpsProductCategory();
            OpsProductSubCategory opsProductSubCategory = opsProduct.getOpsProductSubCategory();
            switch (opsProductCategory) {
                case PRODUCT_CATEGORY_ACCOMMODATION: {
                    switch (opsProductSubCategory) {
                        case PRODUCT_SUB_CATEGORY_HOTELS:
                            try {
                                s = accoCancellationService.processCancellation(opsBooking, opsProduct);
                            } catch (OperationException e) {
                                throw new OperationException("Cancellation is not successful in book id " + opsBooking.getBookID() + "for the order id " + opsProduct.getOrderID());
                            }
                            break;
                    }
                }
                break;

                case PRODUCT_CATEGORY_TRANSPORTATION: {
                    switch (opsProductSubCategory) {
                        case PRODUCT_SUB_CATEGORY_FLIGHT:
                            try {
                                s = airCancellationService.processCancellation(opsBooking, opsProduct);
                            } catch (OperationException e) {
                                throw new OperationException("Cancellation is not successful in book id " + opsBooking.getBookID() + "for the order id " + opsProduct.getOrderID());
                            }

                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jsonObject1 = jsonObject.optJSONObject("responseBody");
                            if (jsonObject1 != null) {
                                JSONArray supplierBookReferences = jsonObject1.optJSONArray("supplierBookReferences");
                                JSONObject supp = supplierBookReferences.getJSONObject(0);
                                String errorMessage = supp.optString("errorMessage");
                                if (!StringUtils.isEmpty(errorMessage)) {
                                    throw new OperationException("Cancellation is not successful in book id " + opsBooking.getBookID() + "for the order id " + opsProduct.getOrderID());
                                } else {
                                    s = "Cancellation is Successfully done for Booking Id:" + bookingId;
                                }

                            }
                            break;
                    }
                }
                break;

                default:
                    break;
            }

        }
        return s;
    }

    @Override
    public String assignTo(AssignStaffResource assignStaffResource) throws OperationException {

        StringBuilder resStr = new StringBuilder();
        try {
            String message = null;
            opsBookingService.assignStaff(assignStaffResource);
            List<String> bookIds = assignStaffResource.getBookIDs();
            List<String> todoCreatedFor = new ArrayList<>();
            List<String> todoAlreadyCreatedFor = new ArrayList<>();
            List<String> todoNotCreatedFor = new ArrayList<>();
            for (String bookId : bookIds) {
                OpsBooking opsBooking = opsBookingService.getBooking(bookId);
                message = createTodo(assignStaffResource, bookId, null, ToDoTaskNameValues.ACTION, "assign To ", ToDoFunctionalAreaValues.OPERATIONS, MDMClientType.fromString(opsBooking.getClientType()));
                if (message.equalsIgnoreCase("Todo task was created successfully")) {
                    todoCreatedFor.add(bookId);
                } else if (message.equalsIgnoreCase("A ToDo task updated for this process")) {
                    todoAlreadyCreatedFor.add(bookId);
                } else if (message.equalsIgnoreCase("Error while creating todo task")) {
                    todoNotCreatedFor.add(bookId);
                } else {
                    //ideally code never comes into this block
                    message = "TODO creation failed";
                }
            }
            if (todoCreatedFor.size() > 0) {
                resStr.append("TODO created for ");
                resStr.append(String.join(",", todoCreatedFor));
            }

            if (todoAlreadyCreatedFor.size() > 0) {
                resStr.append("TODO task is updated for ");
                resStr.append(String.join(",", todoAlreadyCreatedFor));
            }
            if (todoNotCreatedFor.size() > 0) {
                resStr.append("TODO creation failed for ");
                resStr.append(String.join(",", todoNotCreatedFor));
            }

        } catch (OperationException oe) {
            throw oe;
        } catch (Exception e) {
            logger.error("Exception in assignTo :", e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception in assignTo :" + e.getMessage()));
            throw new OperationException(entity);
        }
        return resStr.toString();
    }


}
