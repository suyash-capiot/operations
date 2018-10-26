package com.coxandkings.travel.operations.service.timelimitbooking.impl;

import com.coxandkings.travel.operations.criteria.todo.ToDoCriteria;
import com.coxandkings.travel.operations.enums.PaymentType;
import com.coxandkings.travel.operations.enums.common.MDMClientType;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.timelimit.MDMDaysOrMonths;
import com.coxandkings.travel.operations.enums.timelimit.MDMType;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.repository.timelimitbooking.TimeLimitClientInfoRepository;
import com.coxandkings.travel.operations.repository.timelimitbooking.TimeLimitRepository;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.notification.NotificationResource;
import com.coxandkings.travel.operations.resource.timelimitbooking.*;
import com.coxandkings.travel.operations.resource.timelimitbooking.convert.*;
import com.coxandkings.travel.operations.resource.todo.ToDoResponse;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResponse;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.service.timelimitbooking.MDMTimeLimitService;
import com.coxandkings.travel.operations.service.timelimitbooking.TimeLimitBookingService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.*;
import com.coxandkings.travel.operations.utils.supplierBillPassing.DateConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class TimeLimitBookingServiceImpl implements TimeLimitBookingService {
    private static final Logger logger = LogManager.getLogger(TimeLimitBookingServiceImpl.class);

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private ClientMasterDataService clientMasterDataService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    OpsBookingService opsBookingService;

    @Autowired
    MDMTimeLimitService mdmTimeLimitService;

    @Autowired
    TimeLimitClientInfoRepository timeLimitClientInfoRepository;

    @Autowired
    TimeLimitRepository timeLimitRepository;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private UserService userService;

    @Value(value = "${timelimit.mdm.timelimit-config-master}")
    private String getTimeLimitMDMUrl;

    @Value(value = "${timelimit.be.update}")
    private String getUpdateExpiryUrl;

    //email
    @Value(value = "${timelimit.function}")
    private String getFunctionName;

    @Value(value = "${timelimit.revised_date.scenario}")
    private String getRevisedDateScenario;

    @Value(value = "${timelimit.reduced_date.scenario}")
    private String getReducedDateScenario;

    @Value(value = "${timelimit.unable_to_extend.scenario}")
    private String getUnableToExtendScenario;

    //json Expression
    @Value(value = "${timelimit.jsonExpression.mdm.products}")
    private String jsonExpForMDMProducts;

    @Value(value = "${timelimit.jsonExpression.mdm.extensionFlag}")
    private String jsonExpForMDMExtensionFlag;

    @Value(value = "${timelimit.jsonExpression.mdm.expiryType}")
    private String jsonExpForMDMExpiryType;

    @Value(value = "${timelimit.jsonExpression.mdm.bufferCount}")
    private String jsonExpForMDMBuffer;

    @Value(value = "${timelimit.jsonExpression.mdm.bookingDateOptions}")
    private String jsonExpForMDMBookingDateOptions;

    @Value(value = "${timelimit.jsonExpression.mdm.travelDateOptions}")
    private String jsonExpForMDMTravelDateOptions;

    @Value(value = "${timelimit.alertConfig.approvalAlertName}")
    private String approvalAlertName;

    @Value(value = "${timelimit.alertConfig.businessProcess}")
    private String businessProcess;

    @Value(value = "${timelimit.alertConfig.function}")
    private String function;

    @Value(value = "${timelimit.finance.url}")
    private String financeUrl;

    @Value(value = "${timelimit.finance.totalGrossProfit}")
    private String totalGrossProfit;

    @Value(value = "${timelimit.finance.totalRevenue}")
    private String totalRevenue;

    @Value(value = "${timelimit.be.orderAttribute}")
    private String getUpdateAttributeUrl;

    //to unable or disable action

    /**
     * purpose of this method is,
     * if tlExtensionAllowed is true then allow Ops User to update new_time_limit_date
     * if false then don't allow, don't show
     *
     * @param bookId
     * @param clientId
     * @param clientType
     * @param aProduct
     * @throws OperationException
     */
    public String doExtensionCheck(String bookId, String clientId, String clientType, OpsProduct aProduct) throws OperationException {
        logger.debug("\n entering into updateProductAction method");
        List<MDMProduct> listOfAllProductFromMDM = new ArrayList<>();
        Boolean tlExtAllowed = false;
        String jsonBodyAsString = "";
        jsonBodyAsString = mdmTimeLimitService.getMDMInfoByClientId(clientId);
        listOfAllProductFromMDM = this.getMdmProduct(jsonBodyAsString);
        tlExtAllowed = this.getExtensionCheckFlag(jsonBodyAsString);
        if (listOfAllProductFromMDM.size() > 0) {
            //getting list of products from mdm
            for (MDMProduct mdmProduct : listOfAllProductFromMDM) {
                //for Each product from OpsBooking, check if its there in MDM ProductInfo
                if (mdmProduct.getProductCat().equalsIgnoreCase(aProduct.getProductCategory())
                        && mdmProduct.getProductCatSubtype().equalsIgnoreCase(aProduct.getProductSubCategory())) {
                    if (tlExtAllowed == true) {
                        return jsonBodyAsString;
                    } else {
                        String message = "Revised date Can not be extend or reduced";
                        //sending email to customer/client
                        String clientEmailId = clientMasterDataService.getClientEmailId(clientId, MDMClientType.fromString(clientType));
                        if (clientEmailId != null) {
                            Map<String, String> dynamicVariables = new HashMap<>();
                            dynamicVariables.put("booking_ref_no", bookId);
                            dynamicVariables.put("order_id", aProduct.getOrderID());
                            dynamicVariables.put("product_name", aProduct.getProductSubCategory());
                            try {
                                emailUtils.buildClientMail(getFunctionName,
                                        getUnableToExtendScenario,
                                        clientEmailId,
                                        message,
                                        dynamicVariables, null, null);
                            } catch (Exception e) {
                                logger.info("Email is not sent to customer/client " + clientEmailId);
                            }
                        }
                        throw new OperationException("Extention Not Allowed");
                    }
                }
            }
        }
        return null;
    }

    public List<MDMProduct> getMdmProduct(String jsonBodyAsString) {
        List<MDMProduct> list = new ArrayList<>();
        MDMProduct[] mdmProduct = (MDMProduct[]) jsonObjectProvider.getChildObject(jsonBodyAsString, jsonExpForMDMProducts, MDMProduct[].class);
        for (MDMProduct product : mdmProduct) {
            list.add(product);
        }
        return list;
    }

    public Boolean getExtensionCheckFlag(String jsonBodyAsString) {
        Boolean val = (Boolean) jsonObjectProvider.getChildObject(jsonBodyAsString, jsonExpForMDMExtensionFlag, Boolean.class);
        if (val)
            return true;
        else
            return false;
    }

    /**
     * Use this method to Update New Time Limit Expiry
     *
     * @param timeLimitExpiryResource
     * @return
     * @throws OperationException
     */
    public Map updateNewDate(TimeLimitExpiryResource timeLimitExpiryResource) throws OperationException {
        logger.debug("\n entering into updateNewDate method");
        String jsonBodyAsString = "";
        Integer bufferDays = null;
        Integer bufferHours = null;
        ZonedDateTime newExpiryDate = null;
        OpsBooking opsBooking = null;


        opsBooking = opsBookingService.getBooking(timeLimitExpiryResource.getBookID());

        if (StringUtils.isEmpty(opsBooking) || StringUtils.isEmpty(timeLimitExpiryResource.getBookID()) || StringUtils.isEmpty(timeLimitExpiryResource.getOrderID()) || StringUtils.isEmpty(timeLimitExpiryResource.getNewDate())) {
            logger.debug(Constants.ER34);
            throw new OperationException(Constants.ER34);
        }

        newExpiryDate = ZonedDateTime.parse(timeLimitExpiryResource.getNewDate());
        //checking request orderId with perticular product orderId
        OpsProduct timeLimitProduct = null;
        for (OpsProduct aProduct : opsBooking.getProducts()) {
            if (aProduct.getOrderID().equalsIgnoreCase(timeLimitExpiryResource.getOrderID())) {
                timeLimitProduct = aProduct;
                break;
            }
        }
        Map<String, String> response = new HashMap<>();
        if (timeLimitProduct != null && newExpiryDate != null) {
            //checking tlextension allowed or not.
            jsonBodyAsString = this.doExtensionCheck(opsBooking.getBookID(), opsBooking.getClientID(), opsBooking.getClientType(), timeLimitProduct);
            //calculating actual timelimitexpiry date
            ZonedDateTime actualTimeLimitCutOffExpiry = calculateActualTimeLimitExpiry(timeLimitProduct, jsonBodyAsString);
            if (jsonBodyAsString != null && actualTimeLimitCutOffExpiry != null) {
                Boolean emailFlag = false;
                //getting buffer days from MDM
                MDMBufferDaysInfo mdmBufferDaysInfo = (MDMBufferDaysInfo) jsonObjectProvider.getChildObject(jsonBodyAsString, jsonExpForMDMBuffer, MDMBufferDaysInfo.class);
                bufferDays = mdmBufferDaysInfo.getBufferDays();
                bufferHours = mdmBufferDaysInfo.getBufferHrs();

                //getting booking date
                ZonedDateTime bookingDate = opsBooking.getBookingDateZDT();

                //get booking engine timelimit date
                ZonedDateTime bookingEngineTLDate = null;
                if (timeLimitProduct.getProductSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {
                    OpsHotelDetails opsHotelDetails = timeLimitProduct.getOrderDetails().getHotelDetails();
                    bookingEngineTLDate = opsHotelDetails.getTimeLimitExpiryDate();
                }
                if (timeLimitProduct.getProductSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
                    OpsFlightDetails opsFlightDetails = timeLimitProduct.getOrderDetails().getFlightDetails();
                    bookingEngineTLDate = opsFlightDetails.getTimeLimitExpiryDate();
                }
                /* Checking
                 * 1)To extend
                 *  a)newTLDate is after bookingEngineTlDate
                 *  b)newTLDate is before actualtimelimitexpiry.
                 *  c)newTLDate is after current date
                 *
                 * 2)To Reduce
                 *  a)newTLDate is before bookingEngineTLDate
                 *  b)newTLDate is after bookingdate
                 *  c)newTLDate is after current date
                 *
                 * 1)ops user can not add past date from current date
                 * 2)ops user can add current date and he can reduce also
                 * */


                if (newExpiryDate.isAfter(bookingEngineTLDate)
                        && newExpiryDate.isBefore(actualTimeLimitCutOffExpiry)
                        && newExpiryDate.isAfter(ZonedDateTime.now())) {
                    String message = "Revised Time Limit Expiry Due Date Has Extended";

                    /*TimeLimitExpiryInfo info = new TimeLimitExpiryInfo();
                    CopyUtils.copy(timeLimitExpiryResource, info);
                    timeLimitRepository.saveExpiryInfo(info);*/

                    UpdateExpiryResource updateExpiryResource = new UpdateExpiryResource();
                    updateExpiryResource.setOrderID(timeLimitProduct.getOrderID());
                    updateExpiryResource.setUserID(opsBooking.getUserID());
                    updateExpiryResource.setProductSubCategory(timeLimitProduct.getProductSubCategory());
                    updateExpiryResource.setExpiryTimeLimit(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(Date.from(newExpiryDate.toInstant())));
                    HttpEntity<UpdateExpiryResource> requestEntity = new HttpEntity<>(updateExpiryResource);
                    ResponseEntity<String> updateResponse = null;
                    try {
                        updateResponse = RestUtils.exchange(getUpdateExpiryUrl, HttpMethod.PUT, requestEntity, String.class);
                        emailFlag = true;
                        response.put("message", message);
                    } catch (Exception e) {
                        logger.info("Error while updating timelimitexpiry date in BE" + e);
                        throw new OperationException(Constants.OPS_ERR_31002);
                    }
                    //sending email to customer/client
                    String clientEmailId = clientMasterDataService.getClientEmailId(opsBooking.getClientID(), MDMClientType.fromString(opsBooking.getClientType()));
                    if (clientEmailId != null && emailFlag == true) {
                        Map<String, String> dynamicVariables = new HashMap<>();
                        dynamicVariables.put("booking_ref_no", opsBooking.getBookID());
                        dynamicVariables.put("order_id", timeLimitProduct.getOrderID());
                        dynamicVariables.put("product_name", timeLimitProduct.getProductSubCategory());
                        dynamicVariables.put("expiry_date", newExpiryDate.toString());
                        try {
                            emailUtils.buildClientMail(getFunctionName,
                                    getRevisedDateScenario,
                                    clientEmailId,
                                    message,
                                    dynamicVariables, null, null);
                        } catch (Exception e) {
                            logger.info("Email is not sent to customer/client " + clientEmailId);
                        }
                    }
                } else if (newExpiryDate.isBefore(bookingEngineTLDate)
                        && newExpiryDate.isAfter(bookingDate)
                        && newExpiryDate.isAfter(ZonedDateTime.now())) {
                    String message = "Revised Time Limit Expiry Due Date Has Reduced";

                    /*TimeLimitExpiryInfo info = new TimeLimitExpiryInfo();
                    CopyUtils.copy(timeLimitExpiryResource, info);
                    timeLimitRepository.saveExpiryInfo(info);*/

                    UpdateExpiryResource updateExpiryResource = new UpdateExpiryResource();
                    updateExpiryResource.setOrderID(timeLimitProduct.getOrderID());
                    updateExpiryResource.setUserID(opsBooking.getUserID());
                    updateExpiryResource.setProductSubCategory(timeLimitProduct.getProductSubCategory());
                    updateExpiryResource.setExpiryTimeLimit(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(Date.from(newExpiryDate.toInstant())));
                    HttpEntity<UpdateExpiryResource> requestEntity = new HttpEntity<>(updateExpiryResource);
                    ResponseEntity<String> updateResponse = null;
                    try {
                        updateResponse = RestUtils.exchange(getUpdateExpiryUrl, HttpMethod.PUT, requestEntity, String.class);
                        emailFlag = true;
                        response.put("message", message);
                    } catch (Exception e) {
                        logger.info("Error while updating timelimitexpiry date in BE" + e);
                        throw new OperationException(Constants.OPS_ERR_31002);
                    }
                    //sending email to customer/client
                    String clientEmailId = clientMasterDataService.getClientEmailId(opsBooking.getClientID(), MDMClientType.fromString(opsBooking.getClientType()));
                    if (clientEmailId != null && emailFlag == true) {
                        Map<String, String> dynamicVariables = new HashMap<>();
                        dynamicVariables.put("booking_ref_no", opsBooking.getBookID());
                        dynamicVariables.put("order_id", timeLimitProduct.getOrderID());
                        dynamicVariables.put("product_name", timeLimitProduct.getProductSubCategory());
                        dynamicVariables.put("expiry_date", newExpiryDate.toString());
                        try {
                            emailUtils.buildClientMail(getFunctionName,
                                    getReducedDateScenario,
                                    clientEmailId,
                                    message,
                                    dynamicVariables, null, null);
                        } catch (Exception e) {
                            logger.info("Email is not sent to customer/client " + clientEmailId);
                        }
                    }
                } else if (newExpiryDate.isBefore(ZonedDateTime.now())) {
                    throw new OperationException(Constants.ER43);
                } else {
                    String message = "Revised date Can not be extend or reduced";
                    //sending email to customer/client
                    String clientEmailId = clientMasterDataService.getClientEmailId(opsBooking.getClientID(), MDMClientType.fromString(opsBooking.getClientType()));
                    if (clientEmailId != null) {
                        Map<String, String> dynamicVariables = new HashMap<>();
                        dynamicVariables.put("booking_ref_no", opsBooking.getBookID());
                        dynamicVariables.put("order_id", timeLimitProduct.getOrderID());
                        dynamicVariables.put("product_name", timeLimitProduct.getProductSubCategory());
                        try {
                            emailUtils.buildClientMail(getFunctionName,
                                    getUnableToExtendScenario,
                                    clientEmailId,
                                    message,
                                    dynamicVariables, null, null);
                        } catch (Exception e) {
                            logger.info("Email is not sent to customer/client " + clientEmailId);
                        }
                    }
                    throw new OperationException(Constants.ER44);
                }
            }
        }
        return response;
    }

    /**
     * The purpose of this method is to calculate Actual TL expiry Date for a ProductResource based on MDM configurations.
     * Example below:
     * Booking2 Date: 01 Jan 2018
     * Travel Date: 01 Apr 2018
     * #Scenario 1: From MDM, if expiry type is based on "booking date":
     * MDM Settings: Booking2 Date: 30 days; TL set for 15 days
     * Date X = (01 Apr 2018 - 30 days)
     * Actual TL Expiry Date = Date X + TL set (15 days)
     * <p>
     * #Scenario 2: From MDM, if expiry type is based on "travel date":
     * MDM Settings: Booking2 Date: 30 days; TL set for 15 days
     * Date X = (01 Apr 2018 - 30 days)
     * Actual TL Expiry Date = Date X + TL set (15 days)
     *
     * @param opsProduct
     * @return
     * @throws OperationException
     */

    public ZonedDateTime calculateActualTimeLimitExpiry(OpsProduct opsProduct, String jsonBodyAsString) throws OperationException {
        ZonedDateTime travelDate = null;
        OpsProductCategory opsProductCategory = OpsProductCategory.getProductCategory(opsProduct.getProductCategory());
        OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(opsProductCategory, opsProduct.getProductSubCategory());
        switch (opsProductCategory) {
            case PRODUCT_CATEGORY_ACCOMMODATION: {
                switch (opsProductSubCategory) {
                    case PRODUCT_SUB_CATEGORY_HOTELS: {
                        List<OpsRoom> rooms = opsProduct.getOrderDetails().getHotelDetails().getRooms();
                        //Collections.sort(rooms, (r1, r2) -> DateTimeUtil.formatBEDateTime(r1.getCheckIn()).compareTo(DateTimeUtil.formatBEDateTime(r2.getCheckIn())));
                        String checkInDate = rooms.get(0).getCheckIn();
                        travelDate = DateTimeUtil.formatBEDateTimeZone(checkInDate);
                    }
                    break;

                    default: { //Default clause for Sub Category Switch case
                        throw new OperationException("product SubCategory not matching the required");
                    }
                }// End of Inner Switch case for Sub Category Switch case
            }
            break; // Break for ProductResource Category Switch case

            case PRODUCT_CATEGORY_TRANSPORTATION: {
                switch (opsProductSubCategory) {
                    case PRODUCT_SUB_CATEGORY_FLIGHT: {
                        List<OpsFlightSegment> flightSegments = opsProduct.getOrderDetails().getFlightDetails().getOriginDestinationOptions().get(0).getFlightSegment();
                        //Collections.sort(flightSegments, (f1, f2) -> ZonedDateTime.parse(f1.getDepartureDate()).compareTo(ZonedDateTime.parse(f2.getDepartureDate())));
                        //travelDate = flightSegments.get(0).getDepartureDate();
                        //TODO
                        Collections.sort(flightSegments, (f1, f2) -> f1.getDepartureDateZDT().compareTo(f2.getDepartureDateZDT()));
                        travelDate = flightSegments.get(0).getDepartureDateZDT();
                    }
                    break;

                    default: {//Default clause for Sub Category Switch case
                        throw new OperationException("product Subcategory not matching the required");
                    }
                }//End of Inner Switch case for Sub Category Switch case
            }
            break; // Break for ProductResource Category Switch case
            default: {
                throw new OperationException("product category not matching the required");
            }
        }//End of switch


        Integer bookingDaysCount = null;
        Integer timeLimitSetForDaysCount = null;

        Integer bookingMonthsCount = null;
        Integer timeLimitSetForMonthsCount = null;

        MDMExpiryType mdmExpiryType = (MDMExpiryType) jsonObjectProvider.getChildObject(jsonBodyAsString, jsonExpForMDMExpiryType, MDMExpiryType.class);
        if (mdmExpiryType != null) {
            if (mdmExpiryType.getExpiryType().equalsIgnoreCase(MDMType.EXPIRY_TYPE_BOOKING_DATE.getType())) {
                MDMBookingDateOption mdmBookingDateOption = (MDMBookingDateOption) jsonObjectProvider.getChildObject(jsonBodyAsString, jsonExpForMDMBookingDateOptions, MDMBookingDateOption.class);
                BookingDate mdmBookingDate = mdmBookingDateOption.getBookingDate();
                if (mdmBookingDate.getDaysOrMonths().equalsIgnoreCase(MDMDaysOrMonths.DAYS.getType())) {
                    bookingDaysCount = mdmBookingDate.getCount();
                } else {
                    bookingMonthsCount = mdmBookingDate.getCount();
                }
                TlSetFor tlSetFor = mdmBookingDateOption.getTlSetFor();
                if (tlSetFor.getDaysOrMonths().equalsIgnoreCase(MDMDaysOrMonths.DAYS.getType())) {
                    timeLimitSetForDaysCount = tlSetFor.getCount();

                } else {
                    timeLimitSetForMonthsCount = tlSetFor.getCount();
                }
            }

            if (mdmExpiryType.getExpiryType().equalsIgnoreCase(MDMType.EXPIRY_TYPE_TRAVEL_DATE.getType())) {
                MDMTravelDateOption mdmTravelDateOption = (MDMTravelDateOption) jsonObjectProvider.getChildObject(jsonBodyAsString, jsonExpForMDMTravelDateOptions, MDMTravelDateOption.class);
                TravelDate mdmTravelDate = mdmTravelDateOption.getTravelDate();
                if (mdmTravelDate.getDaysOrMonths().equalsIgnoreCase(MDMDaysOrMonths.DAYS.getType())) {
                    bookingDaysCount = mdmTravelDate.getCount();
                } else {
                    bookingMonthsCount = mdmTravelDate.getCount();
                }
                TlSetFor tlSetFor = mdmTravelDateOption.getTlSetFor();
                if (tlSetFor.getDaysOrMonths().equalsIgnoreCase(MDMDaysOrMonths.DAYS.getType())) {
                    timeLimitSetForDaysCount = tlSetFor.getCount();
                } else {
                    timeLimitSetForMonthsCount = tlSetFor.getCount();
                }
            }
        }
        ZonedDateTime xDays = null;
        ZonedDateTime actualExpiryDate = null;
        //from travelDate - booking date days present in MDM
        if (bookingDaysCount != null) {
            xDays = travelDate.minusDays(bookingDaysCount);
            actualExpiryDate = xDays.plusDays(timeLimitSetForDaysCount);
        }
        if (bookingMonthsCount != null) {
            xDays = travelDate.minusMonths(bookingMonthsCount);
            actualExpiryDate = xDays.plusMonths(timeLimitSetForMonthsCount);
        }
        //return xdays + timeLimitSetForValue
        return actualExpiryDate;
    }

    /**
     * Use of this method to convert time limit booking to paid booking
     *
     * @param bookRefId
     * @return
     * @throws OperationException
     */
    @Override
    public MessageResource convertToDefinite(String bookRefId, String orderID) throws OperationException {
        MessageResource messageResource = new MessageResource();
        String flag = null;
        OpsBooking opsBooking = opsBookingService.getBooking(bookRefId);
        //TODO enable payment detail section if booking done with payment

        OpsProduct opsProduct = opsBookingService.getOpsProduct(opsBooking, orderID);

        List<OpsPaymentInfo> paymentInfo = opsBooking.getPaymentInfo();
        if (paymentInfo.size() > 0 && opsProduct.getOrderDetails().getOpsBookingAttribute()
                .contains(OpsBookingAttribute.BOOKING_TYPE_TIME_LIMIT)) {
            for (OpsPaymentInfo opsPaymentInfo : paymentInfo) {
                if (PaymentType.PART_PAYMENT.getType().equalsIgnoreCase(opsPaymentInfo.getPaymentType())) {
                    //Creating Approval request for Approver when PART_PAYMENT is done
                    flag = this.createTodoTaskForApprover(opsBooking, orderID, PaymentType.PART_PAYMENT);
                    messageResource.setMessage(flag);
                    messageResource.setCode("200");
                    break;
                }
                else{
                  throw new OperationException("As Payment type of this booking is"+opsPaymentInfo.getPaymentType()+" So can not convert this booking into Paid booking.");
                }
            }
        } else if (paymentInfo.size() == 0) {
            //Creating Approval request for Approver when NO_PAYMENT is done
            flag = this.createTodoTaskForApprover(opsBooking, orderID, PaymentType.NO_PAYMENT);
            messageResource.setMessage(flag);
            messageResource.setCode("200");
        } else if (opsProduct.getOrderDetails().getOpsBookingAttribute().contains(OpsBookingAttribute.BOOKING_TYPE_PAID_BOOKING)) {
            messageResource.setMessage("Already Converted To Paid");
            messageResource.setCode("200");
        }
        else {
           throw new OperationException("This booking is not time limit booking. Please select time limit booking");
        }
        return messageResource;
    }


    private String createTodoTaskForApprover(OpsBooking opsBooking, String orderID, PaymentType paymentType) throws OperationException {
        ToDoTaskResource todo = new ToDoTaskResource();
        todo.setTaskSubTypeId(ToDoTaskSubTypeValues.CONVERT_TIME_LIMIT_BOOKING_TO_PAID.name());
        todo.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
        todo.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
        todo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
        todo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
        todo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.name());
        todo.setBookingRefId(opsBooking.getBookID());
        todo.setReferenceId(orderID);
        todo.setTaskStatusId(ToDoTaskStatusValues.OPEN.getValue());
        ToDoTask toDoTask = null;
        if (!isAlreadyExists(todo)) {
            todo.setCreatedByUserId(userService.getLoggedInUserId());
            todo.setRemark("");
            todo.setSuggestedActions("");
            if (PaymentType.NO_PAYMENT.getType().equalsIgnoreCase(paymentType.getType())) {
                todo.setNote("No Payment Booking");
            } else if (PaymentType.PART_PAYMENT.getType().equalsIgnoreCase(paymentType.getType())) {
                todo.setNote("Part Payment Booking");
            }
            try {
                toDoTask = toDoTaskService.save(todo);
                this.createAlertForApprover(opsBooking);
            } catch (Exception e) {
                logger.error("Error while creating ToDoTask for timelimit " + e);
            }
        }
        if (toDoTask != null)
            return "ToDoTask Created Successfully";
        else
            throw new OperationException(Constants.TODO_TASK_CREATION_FAIL);
    }

    private boolean isAlreadyExists(ToDoTaskResource toDoTaskResource) throws OperationException {
        ToDoCriteria toDoCriteria = new ToDoCriteria();
        CopyUtils.copy(toDoTaskResource, toDoCriteria);
        ToDoResponse toDoResponse = toDoTaskService.getByCriteria(toDoCriteria);
        if (toDoResponse != null && toDoResponse.getData() != null && toDoResponse.getData().size() >= 1)
            throw new OperationException("ToDo Task Already Exists");
        else
            return false;
    }

    private void createAlertForApprover(OpsBooking opsBooking) {
        NotificationResource notificationResource = null;
        try {
            notificationResource = alertService.createAlert(businessProcess, function, opsBooking.getCompanyId(), approvalAlertName, userService.getLoggedInUserId(), "ToDoTask is created for approving the request- bookId :" + opsBooking.getBookID());
        } catch (OperationException e) {
            logger.error("Error while sending alerts to whome you are assigned" + e);
        }
    }

    @Override
    public TotalRevenueAndGrossProfitResource getOneYearClientBackgroundInfo(String referenceID) {
        ToDoCriteria toDoCriteria = new ToDoCriteria();
        toDoCriteria.setReferenceId(referenceID);
        toDoCriteria.setTaskSubTypeId(ToDoTaskSubTypeValues.CONVERT_TIME_LIMIT_BOOKING_TO_PAID.name());
        toDoCriteria.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.name());
        toDoCriteria.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.name());
        ToDoResponse toDoResponse = null;
        try {
            toDoResponse = toDoTaskService.getByCriteria(toDoCriteria);
        } catch (OperationException e) {
            e.printStackTrace();
        }
        String bookID = null;
        if (toDoResponse != null) {
            List<ToDoTaskResponse> data = toDoResponse.getData();
            for (ToDoTaskResponse toDoTask : data) {
                if (ToDoTaskStatusValues.OPEN.getValue().equalsIgnoreCase(toDoTask.getTaskStatusValue())
                        || ToDoTaskStatusValues.CLOSED.getValue().equalsIgnoreCase(toDoTask.getTaskStatusValue())) {
                    bookID = toDoTask.getBookingRefId();
                }
            }
        }
        ZonedDateTime currentDate = ZonedDateTime.now();
        String startDate = DateConverter.zonedDateTimeToString(currentDate.minusYears(1));
        String endDate = DateConverter.zonedDateTimeToString(currentDate);
        JSONObject clientBackgroundInfo = this.getTotalRevenueAndTotalGrossProfitValues(bookID, startDate, endDate);
        TotalRevenueAndGrossProfitResource revenueResource = new TotalRevenueAndGrossProfitResource();
        revenueResource.setBookId(bookID);
        revenueResource.setFromDateRange(startDate);
        revenueResource.setToDateRange(endDate);
        revenueResource.setEntityType(clientBackgroundInfo.getString("entityType"));
        revenueResource.setEntityId(clientBackgroundInfo.getString("entityId"));
        revenueResource.setCompanyId(clientBackgroundInfo.getString("companyId"));
        revenueResource.setNoOfBookings(0);
        revenueResource.setTotalGrossProfitAmount(new BigDecimal(clientBackgroundInfo.getInt("totalGrossProfit")));
        revenueResource.setTotalRevenueAmount(new BigDecimal(clientBackgroundInfo.getInt("totalRevenue")));
        return revenueResource;
    }

    public TotalRevenueAndGrossProfitResource getClientBackgroundInfo(ClientBackgroundInfoCriteriaResource criteriaResource) throws OperationException {
        if (!ZonedDateTime.parse(criteriaResource.getFromDateRange()).isBefore(ZonedDateTime.parse(criteriaResource.getToDateRange()))) {
            throw new OperationException("From Range Cannot be greater than To Range");
        }
        String startDate = DateConverter.zonedDateTimeToString(ZonedDateTime.parse(criteriaResource.getFromDateRange()));
        String endDate = DateConverter.zonedDateTimeToString(ZonedDateTime.parse(criteriaResource.getToDateRange()));
        JSONObject clientBackgroundInfo = this.getTotalRevenueAndTotalGrossProfitValues(criteriaResource.getBookID(), startDate, endDate);
        TotalRevenueAndGrossProfitResource revenueResource = new TotalRevenueAndGrossProfitResource();
        revenueResource.setBookId(criteriaResource.getBookID());
        revenueResource.setFromDateRange(startDate);
        revenueResource.setToDateRange(endDate);
        revenueResource.setEntityType(clientBackgroundInfo.getString("entityType"));
        revenueResource.setEntityId(clientBackgroundInfo.getString("entityId"));
        revenueResource.setCompanyId(clientBackgroundInfo.getString("companyId"));
        revenueResource.setNoOfBookings(0);
        revenueResource.setTotalGrossProfitAmount(new BigDecimal(clientBackgroundInfo.getInt("totalGrossProfit")));
        revenueResource.setTotalRevenueAmount(new BigDecimal(clientBackgroundInfo.getInt("totalRevenue")));
        return revenueResource;
    }

    private JSONObject getTotalRevenueAndTotalGrossProfitValues(String bookID, String fromRange, String toRange) {
        JSONObject backgroundInfo = new JSONObject();
        OpsBooking opsBooking = null;
        try {
            opsBooking = opsBookingService.getBooking(bookID);
        } catch (OperationException e) {
            e.printStackTrace();
        }
        //Total Revenue
        StringBuilder url = new StringBuilder();
        url.append(financeUrl).append(opsBooking.getCompanyId()).append(totalRevenue).append(opsBooking.getClientID()).append("&from=")
                .append(fromRange).append("&to=").append(toRange);
        URI uri = UriComponentsBuilder.fromUriString(url.toString()).build().encode().toUri();
        ResponseEntity<String> totalRevenueResponse = null;
        try {
            totalRevenueResponse = mdmRestUtils.exchange(uri, HttpMethod.GET, null, String.class);
        } catch (Exception e) {
            logger.error("Error while retreiving total revenue info from finance" + e);
            e.printStackTrace();
        }
        JSONObject totalRevenueJson = new JSONObject(totalRevenueResponse.getBody());
        Integer totalRevenueAmount = totalRevenueJson.getInt("amount");
        String entityType = totalRevenueJson.getString("entityType");
        //Gross Profit
        StringBuilder profitUrl = new StringBuilder();
        profitUrl.append(financeUrl).append(opsBooking.getCompanyId()).append(totalGrossProfit).append(opsBooking.getClientID()).append("&from=")
                .append(fromRange).append("&to=").append(toRange);
        URI uriForGrossProfit = UriComponentsBuilder.fromUriString(profitUrl.toString()).build().encode().toUri();
        ResponseEntity<String> grossProfitResponse = null;
        try {
            grossProfitResponse = mdmRestUtils.exchange(uriForGrossProfit, HttpMethod.GET, null, String.class);
        } catch (Exception e) {
            logger.error("Error while retreiving total revenue info from finance" + e);
            e.printStackTrace();
        }
        JSONObject grossProfitJson = new JSONObject(grossProfitResponse.getBody());
        Integer grossProfitAmount = grossProfitJson.getInt("amount");

        backgroundInfo.put("bookID", opsBooking.getBookID());
        backgroundInfo.put("companyId", opsBooking.getCompanyId());
        backgroundInfo.put("entityId", opsBooking.getClientID());
        backgroundInfo.put("entityType", entityType);
        backgroundInfo.put("totalRevenue", totalRevenueAmount);
        backgroundInfo.put("totalGrossProfit", grossProfitAmount);
        return backgroundInfo;
    }

    /**
     * Use of this method is when Approval Approves the request
     *
     * @param referenceID
     * @return
     */
    public MessageResource changeApproverStatusToApproved(String referenceID) throws OperationException {
        MessageResource messageResource = new MessageResource();
        ToDoCriteria toDoCriteria = new ToDoCriteria();
        toDoCriteria.setReferenceId(referenceID);
        toDoCriteria.setTaskSubTypeId(ToDoTaskSubTypeValues.CONVERT_TIME_LIMIT_BOOKING_TO_PAID.name());
        toDoCriteria.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.name());
        toDoCriteria.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.name());
        ToDoResponse toDoResponse = null;
        String bookID = null;
        try {
            toDoResponse = toDoTaskService.getByCriteria(toDoCriteria);
            if (toDoResponse != null) {
                List<ToDoTaskResponse> data = toDoResponse.getData();
                for (ToDoTaskResponse toDoTask : data) {
                    if (ToDoTaskStatusValues.OPEN.getValue().equalsIgnoreCase(toDoTask.getTaskStatusValue())) {
                        bookID = toDoTask.getBookingRefId();
                    }
                }
            }
        } catch (OperationException e) {
            e.printStackTrace();
        }

        if (bookID != null) {
            OpsBooking opsBooking = opsBookingService.getBooking(bookID);
            OpsProduct opsProduct = opsBookingService.getOpsProduct(opsBooking, referenceID);
            //update booking attribute to paid booking
            TimeLimitClientContext clientContext = new TimeLimitClientContext();
            clientContext.setClientID(opsBooking.getClientID());
            clientContext.setClientType(opsBooking.getClientType());
            clientContext.setClientMarket(opsBooking.getClientMarket());
            clientContext.setClientCurrency(opsBooking.getClientCurrency());
            clientContext.setClientLanguage(opsBooking.getClientLanguage());

            TimeLimitResponseHeader responseHeader = new TimeLimitResponseHeader();
            responseHeader.setClientContext(clientContext);
            responseHeader.setSessionID(opsBooking.getSessionID());
            responseHeader.setTransactionID(opsBooking.getTransactionID());
            responseHeader.setUserID(opsBooking.getUserID());

            TimeLimitRequestBody timeLimitRequestBody = new TimeLimitRequestBody();
            timeLimitRequestBody.setOrderID(opsProduct.getOrderID());
            timeLimitRequestBody.setProduct(opsProduct.getOpsProductSubCategory().getSubCategory());
            //getting old attributes
            List<OpsBookingAttribute> oldAttributes = opsProduct.getOrderDetails().getOpsBookingAttribute();
            List<Map<OpsBookingAttribute, String>> list = new ArrayList<>();
            for (OpsBookingAttribute attribute : oldAttributes) {
                Map<OpsBookingAttribute, String> temp = new HashMap<>();
                if (OpsBookingAttribute.BOOKING_TYPE_TIME_LIMIT.getBookingAttribute().equalsIgnoreCase(attribute.getBookingAttribute())) {
                    temp.put(OpsBookingAttribute.BOOKING_TYPE_PAID_BOOKING, OpsBookingAttribute.BOOKING_TYPE_PAID_BOOKING.getBookingAttribute());
                    list.add(temp);
                } else {
                    temp.put(attribute, attribute.getBookingAttribute());
                    list.add(temp);
                }
            }
            timeLimitRequestBody.setBookingAttribute(list);

            UpdateAttributeResource resource = new UpdateAttributeResource();
            resource.setResponseHeader(responseHeader);
            resource.setRequestBody(timeLimitRequestBody);
            resource.setUserID(opsBooking.getUserID());
            HttpEntity<UpdateAttributeResource> requestEntity = new HttpEntity<>(resource);
            ResponseEntity<String> updatedResponse = null;
            try {
                updatedResponse = RestUtils.exchange(getUpdateAttributeUrl, HttpMethod.PUT, requestEntity, String.class);
            } catch (Exception e) {
                logger.error("Error occurred while updating no_show Booking Attribute");
                throw new OperationException(Constants.OPS_ERR_31007);
            }
            //update booking attribute
            toDoTaskService.updateToDoTaskStatus(referenceID, ToDoTaskSubTypeValues.CONVERT_TIME_LIMIT_BOOKING_TO_PAID, ToDoTaskStatusValues.COMPLETED);
            approvedAlert(opsBooking);
            messageResource.setMessage("Time Limit Booking Successfully Converted Into Paid Booking");
            messageResource.setCode(updatedResponse.getStatusCode().toString());
        } else {
            messageResource.setMessage("Already Converted");
        }
        return messageResource;
    }

    private void approvedAlert(OpsBooking opsBooking) {
        NotificationResource notificationResource = null;
        try {
            notificationResource = alertService.createAlert(businessProcess, function, opsBooking.getCompanyId(), approvalAlertName, userService.getLoggedInUserId(), "Approver approves the request for bookID" + opsBooking.getBookID());
        } catch (OperationException e) {
            logger.error("Error while sending alerts to whome you are assigned" + e);
        }
    }

    /**
     * Use of this method is when Approval Rejects the request
     *
     * @param referenceID
     * @return
     */
    public MessageResource changeApproverStatusToRejected(String referenceID) {
        MessageResource messageResource = new MessageResource();
        ToDoCriteria toDoCriteria = new ToDoCriteria();
        toDoCriteria.setReferenceId(referenceID);
        toDoCriteria.setTaskSubTypeId(ToDoTaskSubTypeValues.CONVERT_TIME_LIMIT_BOOKING_TO_PAID.name());
        toDoCriteria.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.name());
        toDoCriteria.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.name());
        ToDoResponse toDoResponse = null;
        String bookID = null;
        try {
            toDoResponse = toDoTaskService.getByCriteria(toDoCriteria);
            if (toDoResponse != null) {
                List<ToDoTaskResponse> data = toDoResponse.getData();
                for (ToDoTaskResponse toDoTask : data) {
                    if (ToDoTaskStatusValues.CLOSED.getValue().equalsIgnoreCase(toDoTask.getTaskStatusValue()) ||
                            ToDoTaskStatusValues.COMPLETED.getValue().equalsIgnoreCase(toDoTask.getTaskStatusValue())) {
                        messageResource.setMessage("Already Completed/Closed");
                    }
                }
            }
        } catch (OperationException e) {
            e.printStackTrace();
        }
        toDoTaskService.updateToDoTaskStatus(referenceID, ToDoTaskSubTypeValues.CONVERT_TIME_LIMIT_BOOKING_TO_PAID, ToDoTaskStatusValues.CLOSED);
        if (messageResource.getMessage() == null) {
            messageResource.setMessage("Updated");
        }
        return messageResource;
    }
}
