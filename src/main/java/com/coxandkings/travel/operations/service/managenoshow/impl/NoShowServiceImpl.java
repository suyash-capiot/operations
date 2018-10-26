package com.coxandkings.travel.operations.service.managenoshow.impl;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.resource.managenoshow.*;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.beconsumer.acco.AccoCancellationService;
import com.coxandkings.travel.operations.service.beconsumer.air.AirCancellationService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.managenoshow.NoShowService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.DateTimeUtil;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class NoShowServiceImpl implements NoShowService {
    private static final Logger logger = LogManager.getLogger(NoShowServiceImpl.class);

    @Autowired
    private OpsBookingService opsBookingService;


    @Value(value = "${manage_no_show.update.no-show}")
    private String getNoShowUpdateUrl;

    @Value(value = "${manage_no_show.alertNames.no-show-cancel-alert}")
    private String noShowCancelAlertName;

    @Value(value = "${manage_no_show.alertConfig.businessProcess}")
    private String businessProcess;

    @Value(value = "${manage_no_show.alertConfig.function}")
    private String function;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private UserService userService;

    @Autowired
    private AirCancellationService airCancellationService;

    @Autowired
    private AccoCancellationService accoCancellationService;

    @Autowired
    private AlertService alertService;

    @Override
    public Boolean isNoShowAttributeAlreadyApplied(String bookId, String orderId) {
        OpsProduct opsProduct = null;
        try {
            opsProduct = opsBookingService.getProduct(bookId, orderId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error Occurred While Retrieving AbstractProductFactory Details");
        }
        return opsProduct.getOrderDetails().getOpsBookingAttribute().contains("No_Show");
    }

    /**
     * Purpose of this method is to update the booking attribute
     * to No show and initiate cancellation if required.
     *
     * @param noShowResource
     * @return
     */
    @Override
    public String updateNoShow(NoShowResource noShowResource) throws OperationException {
        //TODO create ToDoTask manually assuming this is already created by Inbound communication module

        OpsBooking opsBooking = opsBookingService.getBooking(noShowResource.getBookId());
        OpsProduct opsProduct = null;
        for (OpsProduct aProduct : opsBooking.getProducts()) {
            if (aProduct.getOrderID().equalsIgnoreCase(noShowResource.getOrderId())) {
                opsProduct = aProduct;
                break;
            }
        }

        ZonedDateTime travelDate = null;
        OpsProductSubCategory subCategory = OpsProductSubCategory.fromString(opsProduct.getOpsProductSubCategory().getSubCategory());
        switch (subCategory) {
            case PRODUCT_SUB_CATEGORY_HOTELS:
                OpsHotelDetails opsHotelDetails = opsProduct.getOrderDetails().getHotelDetails();
                List<OpsRoom> rooms = opsProduct.getOrderDetails().getHotelDetails().getRooms();
                Collections.sort(rooms, (r1, r2) -> {
                    try {
                        return DateTimeUtil.formatBEDateTime(r1.getCheckIn()).compareTo(DateTimeUtil.formatBEDateTime(r2.getCheckIn()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return 0;
                });
                String checkInDate = rooms.get(0).getCheckIn();
                travelDate = DateTimeUtil.formatBEDateTimeZone(checkInDate);
                break;
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                OpsFlightDetails opsFlightDetails = opsProduct.getOrderDetails().getFlightDetails();
                List<OpsFlightSegment> flightSegments = opsProduct.getOrderDetails().getFlightDetails().getOriginDestinationOptions().get(0).getFlightSegment();
                Collections.sort(flightSegments, (f1, f2) -> f1.getDepartureDateZDT().compareTo(f2.getDepartureDateZDT()));
                travelDate = flightSegments.get(0).getDepartureDateZDT();
                break;
				
			case PRODUCT_SUB_CATEGORY_HOLIDAYS:
              OpsHolidaysDetails opsHolidaysDetails = opsProduct.getOrderDetails().getPackageDetails();
              
              if(opsHolidaysDetails.getTourStartDate()!=null && !opsHolidaysDetails.getTourStartDate().isEmpty())
                travelDate = ZonedDateTime.parse(opsHolidaysDetails.getTourStartDate());
              else
                travelDate = ZonedDateTime.parse(opsHolidaysDetails.getTravelStartDate());
              
              break;
        }

        //checks if current date is greater than travel date/check in date or not.
        if (ZonedDateTime.now().isAfter(travelDate)) {

            NoShowClientContext noShowClientContext = new NoShowClientContext();
            noShowClientContext.setClientID(opsBooking.getClientID());
            noShowClientContext.setClientType(opsBooking.getClientType());
            noShowClientContext.setClientMarket(opsBooking.getClientMarket());
            noShowClientContext.setClientCurrency(opsBooking.getClientCurrency());
            noShowClientContext.setClientLanguage(opsBooking.getClientLanguage());

            NoShowResponseHeader noShowHeader = new NoShowResponseHeader();
            noShowHeader.setClientContext(noShowClientContext);
            noShowHeader.setSessionID(opsBooking.getSessionID());
            noShowHeader.setTransactionID(opsBooking.getTransactionID());
            noShowHeader.setUserID(opsBooking.getUserID());

            NoShowRequestBody noShowRequestBody = new NoShowRequestBody();
            noShowRequestBody.setOrderID(opsProduct.getOrderID());
            noShowRequestBody.setProduct(opsProduct.getOpsProductSubCategory().getSubCategory());
            List<OpsBookingAttribute> oldAttributes = opsProduct.getOrderDetails().getOpsBookingAttribute();
            List<Map<OpsBookingAttribute, String>> list = new ArrayList<>();
            Map<OpsBookingAttribute, String> bookingAttribute = new HashMap<>();
            for (OpsBookingAttribute attribute : oldAttributes) {
                Map<OpsBookingAttribute, String> temp = new HashMap<>();
                temp.put(attribute, attribute.getBookingAttribute());
                //Todo BE is giving dublicate objects so to avoid this checking
                if (!attribute.getBookingAttribute().equalsIgnoreCase(OpsBookingAttribute.NO_SHOW.getBookingAttribute())) {
                    list.add(temp);
                }
            }

            bookingAttribute.put(OpsBookingAttribute.NO_SHOW, OpsBookingAttribute.NO_SHOW.getBookingAttribute());

            list.add(bookingAttribute);
            noShowRequestBody.setBookingAttribute(list);

            UpdateBookingAtrributeResource resource = new UpdateBookingAtrributeResource();
            resource.setResponseHeader(noShowHeader);
            resource.setRequestBody(noShowRequestBody);
            resource.setUserID(opsBooking.getUserID());

            HttpEntity<UpdateBookingAtrributeResource> requestEntity = new HttpEntity<>(resource);
            ResponseEntity<String> res = null;
            try {
                res = RestUtils.exchange(getNoShowUpdateUrl, HttpMethod.PUT, requestEntity, String.class);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Error occurred while updating no_show Booking Attribute");
            }

            /**
             * check if no show already cancelled or not
             * 1)if already not cancelled Alert user and create todo task
             * to cancel the product
             * 2)if already cancelled do nothing.
             */
            if (!isNoShowAlreadyCancelled(opsProduct)) {
                //alert
                sendAlertToCancelProduct(opsBooking.getBookID(), opsProduct.getOrderID(), opsBooking.getCompanyId());

                ToDoTask toDoTaskToCancelProduct = null;
                try {
                    toDoTaskToCancelProduct = createToDoTaskToCancelProduct(opsBooking, opsProduct);
                } catch (OperationException e) {
                    logger.error("Error occurred while creating ToDO Task to cancel the product");
                }

                //call cancellation api
                if (opsProduct.getOpsProductSubCategory().getSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
                    try {
                        airCancellationService.processCancellation(opsBooking, opsProduct);
                    } catch (Exception e) {
                        logger.info("Error while processing cancellation for No-Show Product productId: " + opsProduct.getOrderID() + " " + e);
                    }
                }
                if (opsProduct.getOpsProductSubCategory().getSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {
                    try {
                        accoCancellationService.processCancellation(opsBooking, opsProduct);
                    } catch (Exception e) {
                        logger.info("Error while processing cancellation for No-Show Product productId: " + opsProduct.getOrderID() + " " + e);
                    }
                }
				if (opsProduct.getOpsProductSubCategory().getSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOLIDAYS.getSubCategory())) {
                  try {
                      
                    //MAKE CANCELLATION SERVICE FOR HOLIDAYS
                    //BE Service not yet Done
                    
                  } catch (Exception e) {
                      logger.info("Error while processing cancellation for No-Show Product productId: " + opsProduct.getOrderID() + " " + e);
                  }
              }
            }//inner if

            return res.getBody();
        } else
            throw new OperationException(Constants.ER1694);

    }

    private boolean isNoShowAlreadyCancelled(OpsProduct opsProduct) {
        Boolean flag = false;
        if (opsProduct != null) {
            return opsProduct.getOrderDetails().getOpsOrderStatus().equals(OpsOrderStatus.XL);
        }
        return flag;
    }

    private void sendAlertToCancelProduct(String bookId, String orderId, String companyID) {
        String message = "Cancel no show product bookId - " + bookId + "& orderId - " + orderId;
        try {
            alertService.createAlert(businessProcess, function, companyID, noShowCancelAlertName, userService.getLoggedInUserId(), message);
        } catch (OperationException e) {
            logger.error("Error while sending cancel alert to userId " + userService.getLoggedInUserId());
        }
    }

    private ToDoTask createToDoTaskToCancelProduct(OpsBooking opsBooking, OpsProduct opsProduct) throws OperationException {
        ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
        toDoTaskResource.setCreatedByUserId(userService.getLoggedInUserId());
        toDoTaskResource.setNote("");
        toDoTaskResource.setFileHandlerId("OPS_USER"); //Assign to Operations function
        toDoTaskResource.setClientId(opsBooking.getClientID());
        String orderId = opsProduct.getOrderID();
        toDoTaskResource.setProductId(orderId);
        toDoTaskResource.setDueOnDate(ZonedDateTime.now());
        toDoTaskResource.setClientSubCategoryId(opsBooking.getClientSubCategory());
        toDoTaskResource.setReferenceId(orderId);
        toDoTaskResource.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
        toDoTaskResource.setSecondaryFileHandlerId("");
        toDoTaskResource.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
        toDoTaskResource.setTaskNameId(ToDoTaskNameValues.CANCEL.getValue());
        toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
        toDoTaskResource.setCompanyId(opsBooking.getCompanyId());
        toDoTaskResource.setClientCategoryId(opsBooking.getClientCategory());
        toDoTaskResource.setClientTypeId(opsBooking.getClientType());
        toDoTaskResource.setCompanyMarketId(opsBooking.getClientMarket());
        toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.ORDER.toString());
        toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
        toDoTaskResource.setBookingRefId(opsBooking.getBookID());
        toDoTaskResource.setProductCategory(opsProduct.getProductCategory());
        toDoTaskResource.setProductSubCategory(opsProduct.getProductSubCategory());
        toDoTaskResource.setTaskOrientedTypeId(ToDoTaskOrientedValues.ACTION_ORIENTED.getValue());
        toDoTaskResource.setProductId(opsProduct.getProductName());
        ToDoTask toDoTask = null;

        try {
            toDoTask = toDoTaskService.save(toDoTaskResource);
        } catch (Exception e) {
            logger.error("Unable to create ToDO task to cancel the product", e);
            throw new OperationException("Unable to create ToDO task to cancel the product");
        }
        return toDoTask;
    }
}