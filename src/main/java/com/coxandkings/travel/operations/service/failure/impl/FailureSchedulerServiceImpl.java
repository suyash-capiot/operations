package com.coxandkings.travel.operations.service.failure.impl;

import com.coxandkings.travel.operations.enums.todo.ToDoTaskStatusValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskSubTypeValues;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.failure.FailureDetails;
import com.coxandkings.travel.operations.repository.failure.FailureRepository;
import com.coxandkings.travel.operations.resource.failure.FailureDetailsResource;
import com.coxandkings.travel.operations.resource.failure.FailureSearchCriteria;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.failure.FailureSchedulerService;
import com.coxandkings.travel.operations.service.failure.FailureService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FailureSchedulerServiceImpl implements FailureSchedulerService {


    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private FailureService failureService;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private FailureRepository failureRepository;

    @Autowired
    private RestUtils restUtils;

    private static final Logger logger = LogManager.getLogger(FailureSchedulerService.class);

    @Value("${failures.update-order-attribute}")
    private String updateOrderAttribute;

    @Override
    public void processBooking() throws OperationException {
        logger.info("In Manage Failure Scheduler Service");
        FailureSearchCriteria failureSearchCriteria = new FailureSearchCriteria();
        failureSearchCriteria.setFailureFlag("Failure Booking");
        failureSearchCriteria.setPaymentFailureFlag("FAILED");
        List<FailureDetailsResource> failureDetailsResources = null;
        try {
            failureDetailsResources = failureService.getFailures(failureSearchCriteria);
        } catch (Exception e) {
            logger.error("Unable to fetch failed bookings");
            e.printStackTrace();
            //throw new OperationException("Unable to fetch failed booking");
        }
        if (failureDetailsResources != null) {
            if (!StringUtils.isEmpty(failureDetailsResources.size())) {
                for (FailureDetailsResource failureDetailsResource : failureDetailsResources) {
                    OpsBooking opsBooking = null;
                    try {
                        opsBooking = opsBookingService.getBooking(failureDetailsResource.getBookID());
                    } catch (Exception e) {
                        logger.error("Unable to get booking with the book id : " + failureDetailsResource.getBookID());
                        e.printStackTrace();
                        //throw new OperationException("Unable to find booking with book id: " + failureDetailsResource.getBookID());
                    }
                    if (!failureRepository.getExists(failureDetailsResource.getBookID()) && opsBooking != null && opsBooking.getPaymentInfo() != null) {
                        for (OpsPaymentInfo opsPaymentInfo : opsBooking.getPaymentInfo()) {
                            if (opsPaymentInfo.getPayStatus() != null) {
                                if (opsPaymentInfo.getPayStatus().equalsIgnoreCase("FAILED")) {
                                    logger.info("Payment failure booking found " + opsBooking.getBookID());
                                    failureService.actionPaymentFailure(opsBooking);
                                }
                            } else {
                                logger.info("Payment status is null not able to process the booking");
                                logger.error("Unable to check payment status");
                            }
                        }
                        for (OpsProduct opsProduct : opsBooking.getProducts()) {
                            if (opsProduct.getOrderDetails().getOpsBookingAttribute().toString().contains("BF") && !opsProduct.getOrderDetails().getOpsOrderStatus().equals(OpsOrderStatus.RQ)) {
                                logger.info("Failure Booking found " + opsBooking.getBookID());
                                failureService.actionBookingFailure(opsBooking, opsProduct);
                            }
                        }
                    } else {
                        logger.info("The current booking " + failureDetailsResource.getBookID() + " was processed earlier");
                    }
                }

            } else {

                logger.info("No failed booking found");
            }
        }
    }


    //updating the todo tasks and updating the order attributes
    @Override
    public void closeToDo() {
        List<FailureDetails> failureDetailsList = failureRepository.getAll();
        for (FailureDetails failureDetails : failureDetailsList) {
            OpsBooking opsBooking = null;
            try {
                opsBooking = opsBookingService.getBooking(failureDetails.getBookID());
            } catch (Exception e) {
                logger.info("No booking found with the id " + failureDetails.getBookID());
            }
            if (opsBooking != null) {
                if (opsBooking.getStatus().equals(OpsBookingStatus.CNF) || opsBooking.getStatus().equals(OpsBookingStatus.XL)) {
                    logger.info("In scheduler closing todo's that are completed");
                    logger.info(opsBooking.getBookID());
                    toDoTaskService.updateToDoTaskStatus(opsBooking.getBookID(), ToDoTaskSubTypeValues.MANAGE_FAILURE, ToDoTaskStatusValues.COMPLETED);
                    failureDetails.setDeleted(true);
                    failureRepository.saveOrUpdate(failureDetails);
                }
                List<OpsProduct> opsProductList = opsBooking.getProducts().stream().filter(opsProduct -> opsProduct.getOrderDetails().getOpsBookingAttribute().toString().contains("BF")).collect(Collectors.toList());
                for (OpsProduct opsProduct : opsProductList) {
                    if (opsProduct.getOrderDetails().getOpsOrderStatus().equals(OpsOrderStatus.OK) || opsProduct.getOrderDetails().getOpsOrderStatus().equals(OpsOrderStatus.XL)) {
                        failureService.removeFailureFlagFromOrder(opsBooking, opsProduct);
                    }
                }
            }
        }
    }

}
