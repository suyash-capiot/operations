package com.coxandkings.travel.operations.service.timelimitbooking.impl;

import com.coxandkings.travel.operations.enums.common.MDMClientType;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.timelimit.BatchJobStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.timelimitbooking.TLCancellationBatchJobInfo;
import com.coxandkings.travel.operations.model.timelimitbooking.TimeLimitBatchJobInfo;
import com.coxandkings.travel.operations.repository.timelimitbooking.CancellationBatchJobInfoRepository;
import com.coxandkings.travel.operations.repository.timelimitbooking.TimeLimitBatchJobRepository;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.beconsumer.acco.AccoCancellationService;
import com.coxandkings.travel.operations.service.beconsumer.air.AirCancellationService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.service.timelimitbooking.MDMTimeLimitService;
import com.coxandkings.travel.operations.service.timelimitbooking.TimeLimitBatchJobInfoService;
import com.coxandkings.travel.operations.service.timelimitbooking.TimeLimitBookingService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.EmailUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TimeLimitBatchJobInfoServiceImpl implements TimeLimitBatchJobInfoService {
    private static final Logger logger = LogManager.getLogger(TimeLimitBatchJobInfoServiceImpl.class);
    @Autowired
    MDMTimeLimitService mdmTimeLimitService;

    @Autowired
    private TimeLimitBatchJobRepository timeLimitBatchJobRepository;

    @Autowired
    private CancellationBatchJobInfoRepository cancellationBatchJobInfoRepository;

    @Autowired
    private TimeLimitBookingService timeLimitBookingService;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private ClientMasterDataService clientMasterDataService;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private AlertService alertService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccoCancellationService accoCancellationService;

    @Autowired
    private AirCancellationService airCancellationService;

    @Value(value = "timelimit.close_to_expiry.scenario")
    private String closeToExpiryScenario;

    @Value(value = "timelimit.process")
    private String process;

    @Value(value = "timelimit.function")
    private String function;

    @Value(value = "timelimit.alertConfig.businessProcess")
    private String businessProcess;

    @Value(value = "timelimit.alertConfig.alertName")
    private String alertName;

    @Value(value = "timelimit.alertConfig.cancellationAlerName")
    private String cancellationAlertName;

    @Value(value = "timelimit.alertConfig.function")
    private String alertFunctionName;

    @Transactional
    public void tlAboutToExpire(OpsBooking aOpsBooking) {
        Integer days = 1;
        Integer hours = 24;
        ZonedDateTime bookingEngineTLDate = null;
        for (OpsProduct aOpsProduct : aOpsBooking.getProducts()) {
            TimeLimitBatchJobInfo timeLimitBatchJobInfo = new TimeLimitBatchJobInfo();
            timeLimitBatchJobInfo.setBookId(aOpsBooking.getBookID());
            timeLimitBatchJobInfo.setOrderId(aOpsProduct.getOrderID());

            if (aOpsProduct.getProductSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {
                OpsHotelDetails opsHotelDetails = aOpsProduct.getOrderDetails().getHotelDetails();
                bookingEngineTLDate = opsHotelDetails.getTimeLimitExpiryDate();
            } else if (aOpsProduct.getProductSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
                OpsFlightDetails opsFlightDetails = aOpsProduct.getOrderDetails().getFlightDetails();
                bookingEngineTLDate = opsFlightDetails.getTimeLimitExpiryDate();
            }

            timeLimitBatchJobInfo.setCalculatedExpiryDueDate(
                    getCutOffDate(bookingEngineTLDate, days, hours));

            timeLimitBatchJobInfo.setBatchJobStatus(BatchJobStatus.NOT_PROCESSED);
            timeLimitBatchJobRepository.saveTimeLimitBacthJobInfo(timeLimitBatchJobInfo);
        }
    }

    @Override
    public List<TimeLimitBatchJobInfo> getTLBatchJobInfoByCriteria() {
        return timeLimitBatchJobRepository.getTLBatchJobInfoByCriteria();
    }

    public ZonedDateTime getCutOffDate(ZonedDateTime bookingEngineTLDate, Integer days, Integer hours) {
        //ToDo cut of date should be coming from Alert Master
        ZonedDateTime cutOffDate = bookingEngineTLDate.minusDays(days);
        cutOffDate = cutOffDate.minusHours(hours);
        return cutOffDate;
    }

    public void cancelTLIfNotConverted(OpsBooking opsBooking) {
        String jsonBodyAsString = mdmTimeLimitService.getMDMInfoByClientId(opsBooking.getClientID());
        for (OpsProduct opsProduct : opsBooking.getProducts()) {
            try {
                if (opsProduct.getOrderDetails().getOpsBookingAttribute().contains(OpsBookingAttribute.BOOKING_TYPE_TIME_LIMIT.getBookingAttribute())) {
                    ZonedDateTime actualTImeLimitExpiry = timeLimitBookingService.calculateActualTimeLimitExpiry(opsProduct, jsonBodyAsString);
                    //Todo one day less(this should be come from MDM)
                    ZonedDateTime calculatedBeforeDate = actualTImeLimitExpiry.minusDays(1);

                    TLCancellationBatchJobInfo cancellationBatchJobInfo = new TLCancellationBatchJobInfo();
                    cancellationBatchJobInfo.setBookId(opsBooking.getBookID());
                    cancellationBatchJobInfo.setOrderId(opsProduct.getOrderID());
                    cancellationBatchJobInfo.setCalculatedBeforeDueDate(calculatedBeforeDate);
                    cancellationBatchJobInfo.setBatchJobStatus(BatchJobStatus.NOT_PROCESSED);
                    //save into db
                    cancellationBatchJobInfoRepository.saveCancellationBacthJobInfo(cancellationBatchJobInfo);
                }
            } catch (OperationException e) {
                logger.debug("Error while calculating actual time limit expiry " + e);
            }
        }
    }

    public List<TLCancellationBatchJobInfo> getTLCancellationBatchInfoByCriteria() {
        return cancellationBatchJobInfoRepository.getTLCancellationBatchJobInfoByCriteria();
    }

    public void sendEmailAndAlertWhenCloseToExpiry(String bookId, String orderId, String dueDate) {
        OpsBooking opsBooking = null;
        try {
            opsBooking = opsBookingService.getBooking(bookId);
        } catch (OperationException e) {
            logger.error("Error while retriving booking details" + e);
        }

        if (opsBooking != null) {
            String alertMessage = "Due Date close for BookId :- " + bookId + "and orderId :- " + orderId;
            try {
                alertService.createAlert(businessProcess, function, opsBooking.getCompanyId(), alertName, userService.getSystemUserIdFromMDMToken(), alertMessage);
            } catch (OperationException e) {
                logger.error("Error while sending alert to logged in user when close to expiry" + e);
            }

            String subjectMessage = "Booking Due Date Is Close To Expiry";
            //sending email to customer/client
            try {
                String clientEmailId = clientMasterDataService.getClientEmailId(opsBooking.getClientID(), MDMClientType.fromString(opsBooking.getClientType()));
                if (clientEmailId != null) {
                    Map<String, String> dynamicVariables = new HashMap<>();
                    dynamicVariables.put("book_ref_id", bookId);
                    dynamicVariables.put("due_date", dueDate);
                    dynamicVariables.put("orderId", orderId);
                    try {
                        emailUtils.buildClientMail(function,
                                closeToExpiryScenario,
                                clientEmailId,
                                subjectMessage,
                                dynamicVariables, null, null);
                    } catch (Exception e) {
                        logger.error("Error while sending email to customer when close to expiry" + e);
                    }
                }
            } catch (OperationException e) {
                logger.error("Error while getting email ID from MDM Master" + e);
            }
        }
    }

    public void cancellationRequest(String bookId, String orderId, String calculatedBeforeDate) {
        OpsBooking opsBooking = null;
        try {
            opsBooking = opsBookingService.getBooking(bookId);
        } catch (OperationException e) {
            logger.error("Error while retriving booking details" + e);
        }
        if (opsBooking != null) {
            String alertMessage = "Processing Cancellation for BookId :- " + bookId + "and orderId :- " + orderId;
            try {
                alertService.createAlert(businessProcess, function, opsBooking.getCompanyId(), cancellationAlertName, userService.getSystemUserIdFromMDMToken(), alertMessage);
            } catch (OperationException e) {
                logger.error("Error while sending alert to logged in user when close to expiry" + e);
            }
            OpsProduct opsProduct = null;
            for (OpsProduct aProduct : opsBooking.getProducts()) {
                if (aProduct.getOrderID().equalsIgnoreCase(orderId)) {
                    opsProduct = aProduct;
                    break;
                }
            }
            if (opsProduct.getOpsProductSubCategory().getSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
                try {
                    airCancellationService.processCancellation(opsBooking, opsProduct);
                } catch (OperationException e) {
                    e.printStackTrace();

                }
            }
            if (opsProduct.getOpsProductSubCategory().getSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {
                try {
                    accoCancellationService.processCancellation(opsBooking, opsProduct);
                } catch (OperationException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
