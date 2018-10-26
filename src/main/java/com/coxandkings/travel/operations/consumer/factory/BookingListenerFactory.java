package com.coxandkings.travel.operations.consumer.factory;

import com.coxandkings.travel.ext.model.be.BookingActionConstants;
import com.coxandkings.travel.operations.consumer.listners.ForexBookingListener;
import com.coxandkings.travel.operations.consumer.listners.RetrieveOrderStatusListener;
import com.coxandkings.travel.operations.consumer.listners.impl.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.bookingHistory.BookingHistoryService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BookingListenerFactory {
    private static final Logger logger = LogManager.getLogger(BookingListenerFactory.class);

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationContext context;
    @Autowired
    @Qualifier("bookingHistory")
    private BookingHistoryService bookingHistoryService;

    @Autowired
    private ToDoTaskService toDoTaskService;

    public void processBooking(String payload) throws IOException, OperationException {
        logger.info("In processBooking() of BookingListenerFactory class");

        // Use this flag in future for enabling re-processing of Kafka message through Retry mechanism!
        boolean isProcessedSuccess = true;

        KafkaBookingMessage kafkaBookingMessage = objectMapper.readValue(payload, KafkaBookingMessage.class);
/*
        try {
            TrackingContext.setTrackingContext(kafkaBookingMessage);
        } catch (Exception e) {
            logger.error(e);
        }*/
        //logger.info(String.format("%s_RQ = %s", Constants.BOOKING_KAFKA_MSG, TrackingContext.getTrackingContext()));
        OpsBooking opsBooking = opsBookingService.getBooking(kafkaBookingMessage.getBookId());

        boolean isNewBooking = false;

        try{
            bookingHistoryService.captureDetailsForBookingHistory(kafkaBookingMessage, opsBooking);
        }catch (Exception e){
            logger.warn("Unable to set Booking Details in Booking History");
        }

        try {
            this.createTODOTaskForBooking(opsBooking, kafkaBookingMessage);
        }catch (Exception e){
            logger.warn("Unable to create ToDo Task");
        }

        String actionType = kafkaBookingMessage.getActionType();
        if (actionType != null && actionType.equalsIgnoreCase(BookingActionConstants.JSON_PROP_NEW_BOOKING)) {
            isNewBooking = true;
        }
        String errorCode = kafkaBookingMessage.getErrorCode();

        boolean isRejectedBooking = false;
        if (actionType != null && actionType.equalsIgnoreCase(BookingActionConstants.JSON_PROP_ERROR_BOOKING)) {
            isRejectedBooking = true;
        }

        if (isRejectedBooking) {
            try {
                AlternateOptionsBookingListenerImpl alternateOptionsBookingListener = getNewAlternateOptionsBookingListener(
                        this.context);
                alternateOptionsBookingListener.processBooking(opsBooking, kafkaBookingMessage);
            } catch (Exception e) {
                isProcessedSuccess = false;
                logger.error("Error occured in processing message for AlternateOptionsBookingListener", e);
            }
        } else {

            try {
                if (isNewBooking) {
                    NewSupplierFirstBookingListener newSupplierListener = getNewSupplierFirstBookingListener(
                            this.context);
                    newSupplierListener.processBooking(opsBooking);
                }
            } catch (Exception e) {
                isProcessedSuccess = false;
                logger.error("Error occured in processing message for NewSupplierFirstBooking", e);
            }

            try {
                ServiceOrderAndSupplierLiabilityListener serviceOrderListener = getServiceOrderAndLiabilityListener(
                        this.context);
                serviceOrderListener.processBooking(opsBooking, kafkaBookingMessage);
            } catch (Exception e) {
                isProcessedSuccess = false;
                logger.error("Error occured in processing message - ServiceOrderAndSupplierLiabilityListener", e);
            }

            try {
                ThirdPartyVouchersListenerImpl thirdPartyVoucherListener = getThirdPartyVouchers(this.context);
                thirdPartyVoucherListener.processBooking(opsBooking, kafkaBookingMessage);
            } catch (Exception e) {
                isProcessedSuccess = false;
                logger.error("Error occured in processing message for ThirdPartyVouchers", e);
            }

            try {
                TimeLimitBookingListener timeLimitBookingListener = getTimeLimitBookingListener(this.context);
                timeLimitBookingListener.processBooking(opsBooking);
            } catch (Exception e) {
                isProcessedSuccess = false;
                logger.error("Error occured in processing message for TimeLimitBooking", e);
            }

            try {
                ManageDocumentationListener manageDocumentationListener = getManageDocumentationListener(this.context);
                manageDocumentationListener.processBooking(opsBooking, kafkaBookingMessage);
            } catch (Exception e) {
                isProcessedSuccess = false;
                logger.error("Error occured in processing message for ManageDocumentation", e);
            }

            try {
                if (isNewBooking) {
                    PrePaymentToSupplierListener prePaymentToSupplierListener = getPrePaymentToSupplierListener(
                            this.context);
                    prePaymentToSupplierListener.processBooking(opsBooking);
                }
            } catch (Exception e) {
                isProcessedSuccess = false;
                logger.error("Error occured in processing message for PrePaymentToSupplier", e);
            }

             /* try { MergeBookingListener mergeBookingListener =
              getMergeBookingListener(this.context);
              mergeBookingListener.processBooking(opsBooking); } catch (Exception e) {
              isProcessedSuccess = false;
              logger.error("Error occured in processing message for MergeBookings", e); }*/

            try {
                logger.info("Started processing full cancellation ");
                FullCancellationListenerImpl fullCancellationListener = getFullCancellationListener();
                if (actionType != null &&
                        actionType.equalsIgnoreCase(BookingActionConstants.JSON_PROP_AIR_CANNCELTYPE_FULLCANCEL) ||
                        actionType.equalsIgnoreCase(BookingActionConstants.JSON_PROP_ACCO_CANNCELTYPE_FULLCANCEL)) {

                    fullCancellationListener.processBooking(kafkaBookingMessage);
                }
            } catch (Exception e) {
                isProcessedSuccess = false;
                logger.error("Error occured in processing message for Full Cancellation", e);
            }

            try {
                logger.info("Started processing full cancellation ");
                FullCancellationListenerImpl fullCancellationListener = getFullCancellationListener();
                if (kafkaBookingMessage.getActionType()
                        .equalsIgnoreCase(BookingActionConstants.JSON_PROP_AIR_CANNCELTYPE_FULLCANCEL)
                        || kafkaBookingMessage.getActionType()
                        .equalsIgnoreCase(BookingActionConstants.JSON_PROP_ACCO_CANNCELTYPE_FULLCANCEL)) {
                    fullCancellationListener.processBooking(kafkaBookingMessage);
                }
            } catch (Exception e) {
                isProcessedSuccess = false;
                logger.error("Error occured in processing message for Full Cancellation", e);
            }

            try {
                if (isNewBooking) {
                    ServiceReconfirmationListener reconfirmationListener = getReconfirmationListener(this.context);
                    reconfirmationListener.processBooking(opsBooking, kafkaBookingMessage);
                }
            } catch (Exception e) {
                isProcessedSuccess = false;
                logger.error("Error occured in processing message for Reconfirmation Master", e);
            }
        }

        try {
            FileProfitabilityListenerImpl fileProfitabilityListener = getFileProfitabilityListenerImpl(this.context);
            fileProfitabilityListener.processBooking(kafkaBookingMessage);
        } catch (Exception e) {
            isProcessedSuccess = false;
            logger.error("Error occured in FileProfitabilityListenerImpl", e);
        }

        /*try {
            QCManagementListenerImpl qcManagementListener = getQcManagementListener();
            qcManagementListener.processBooking(kafkaBookingMessage);
        } catch (Exception e) {
            isProcessedSuccess = false;
            logger.error("Error occured in processing message for QC Checks", e);
        }*/

        try {
            if (kafkaBookingMessage.getActionType().
                    equalsIgnoreCase(BookingActionConstants.JSON_PROP_ON_REQUEST_BOOKING)) {
                logger.info("Started processing Retrieve Order Status");
                RetrieveOrderStatusListener retrieveOrderStatusListener = getRetrieveOrderStatusListener(this.context);
                retrieveOrderStatusListener.processBooking(kafkaBookingMessage);
            }
        } catch (Exception e) {
            isProcessedSuccess = false;
            logger.error("Error occured in processing message for Retrieve Order Status", e);
        }

        try {
           ForexBookingListener forexBookingListener = getForexBookingListener(this.context);
           forexBookingListener.processBooking(opsBooking, kafkaBookingMessage);
        } catch (Exception e) {
            isProcessedSuccess = false;
            logger.error("Error occurred in processing message for ForexBooking", e);
        }

        if (!isProcessedSuccess) {
            // Should this be changed to use sub-type of JMS Exception to handle retries?? TBD
//            throw new OperationException("Error occurred while processing message for Booking Notifications");
        }
    }

    private void createTODOTaskForBooking(OpsBooking opsBooking, KafkaBookingMessage kafkaBookingMessage) {

        try{
            //TODO: To decide whether to create a TODO for succesfull booking.
           /* if(kafkaBookingMessage.getActionType().equalsIgnoreCase(BookingActionConstants.JSON_PROP_NEW_BOOKING)
                    && kafkaBookingMessage.getOperation().equalsIgnoreCase("Book"))
                toDoTaskService.createToDoTaskForNewBooking(opsBooking);*/
        }catch (Exception e){
            logger.warn("Unable to create TODO Task For New Booking" + kafkaBookingMessage.getBookId());
            return;
        }

        try{
            if((kafkaBookingMessage.getActionType().equalsIgnoreCase(BookingActionConstants.JSON_PROP_ERROR_BOOKING)
                    || kafkaBookingMessage.getActionType().equalsIgnoreCase(BookingActionConstants.JSON_PROP_ON_REQUEST_BOOKING))
                    && kafkaBookingMessage.getOperation().equalsIgnoreCase("Book")) {

                OpsProduct opsProduct = opsBookingService.getOpsProduct(opsBooking, kafkaBookingMessage.getOrderNo());
                toDoTaskService.createToDoTaskForFailedBooking(opsBooking, opsProduct, kafkaBookingMessage);
            }
        }catch (Exception e){
            logger.warn("Unable to create TODO Task For Failed/OnRequest Booking" + kafkaBookingMessage.getBookId());
            return;
        }
    }


    private AlternateOptionsBookingListenerImpl getNewAlternateOptionsBookingListener(ApplicationContext context) {
        return context.getBean(AlternateOptionsBookingListenerImpl.class);
    }

    public FileProfitabilityListenerImpl getFileProfitabilityListenerImpl(ApplicationContext context) {
        return context.getBean(FileProfitabilityListenerImpl.class);
    }

    public NewSupplierFirstBookingListener getNewSupplierFirstBookingListener(ApplicationContext context) {
        return context.getBean(NewSupplierFirstBookingListener.class);
    }

    public ServiceOrderAndSupplierLiabilityListener getServiceOrderAndLiabilityListener(ApplicationContext context) {
        return context.getBean(ServiceOrderAndSupplierLiabilityListener.class);
    }

    public ThirdPartyVouchersListenerImpl getThirdPartyVouchers(ApplicationContext context) {
        return context.getBean(ThirdPartyVouchersListenerImpl.class);
    }

    public TimeLimitBookingListener getTimeLimitBookingListener(ApplicationContext context) {
        return context.getBean(TimeLimitBookingListener.class);
    }

    @Lookup
    public FullCancellationListenerImpl getFullCancellationListener() {
        return null;
    }

    public ServiceReconfirmationListener getReconfirmationListener(ApplicationContext context) {
        return context.getBean(ServiceReconfirmationListener.class);
    }

    public MergeBookingListener getMergeBookingListener(ApplicationContext context) {
        return context.getBean(MergeBookingListener.class);
    }

    public QCManagementListenerImpl getQcManagementListener() {
        return context.getBean(QCManagementListenerImpl.class);
    }

    public ManageDocumentationListener getManageDocumentationListener(ApplicationContext context) {
        return context.getBean(ManageDocumentationListener.class);
    }

    public PrePaymentToSupplierListener getPrePaymentToSupplierListener(ApplicationContext context) {
        return context.getBean(PrePaymentToSupplierListener.class);
    }

    public RetrieveOrderStatusListener getRetrieveOrderStatusListener(ApplicationContext context) {
        return context.getBean(RetrieveOrderStatusListener.class);
    }

    public ForexBookingListener getForexBookingListener(ApplicationContext context) {
        return context.getBean(ForexBookingListener.class);
    }
}
