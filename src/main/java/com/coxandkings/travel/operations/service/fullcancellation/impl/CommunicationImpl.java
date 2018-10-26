package com.coxandkings.travel.operations.service.fullcancellation.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.fullcancellation.Communication;
import com.coxandkings.travel.operations.service.fullcancellation.MDMService;
import com.coxandkings.travel.operations.utils.EmailUtils;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service("communication")
public class CommunicationImpl implements Communication {

    private static final Logger logger = LogManager.getLogger(CommunicationImpl.class);

    @Value("${communication.email.api}")
    private String emailUrl;

    @Value("${communication.email.from_address}")
    private String fromEmailAddress;

    @Value("${communication.alert.api}")
    private String alertUrl;

    @Value("${full_cancellation.user_alert}")
    private String opsUserAlertName;

    @Value("${full_cancellation.supplier_confirm_cancellation_alert}")
    private String opsUserSupplierConfirmAlert;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private MDMService mdmService;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private AlertService alertService;

    @Value("${full_cancellation.email.function}")
    private String function;

    @Value("${full_cancellation.email.client_confirmation.scenario}")
    private String clientConfirmationScenario;
    @Value("${full_cancellation.email.client_confirmation.subject}")
    private String clientConfirmationSubject;

    @Value("${full_cancellation.email.supplier_approval_reject.scenario}")
    private String supplierApprovalScenario;
    @Value("${full_cancellation.email.supplier_approval_reject.subject}")
    private String supplierApprovalSubject;

    @Value("${full_cancellation.email.email_alert_supplier.scenario}")
    private String supplierAlertScenario;
    @Value("${full_cancellation.email.email_alert_supplier.subject}")
    private String supplierAlertSubject;


    //for externet supplier
    @Override
    public boolean sendMailSupplier(String supplierEmail, KafkaBookingMessage kafkaBookingMessage, String supplierRefNumber) throws OperationException {
        ResponseEntity<EmailResponse> emailResponseEntity = null;
        try {
            logger.info("*** Full Cancellation: Sending mail to externet supplier**");
            // emailResponseEntity = RestUtils.postForEntity(emailUrl, buildMailSupplier(supplierEmail, kafkaBookingMessage, supplierRefNumber), EmailResponse.class);
            Map<String, String> dynamicvariables = new HashMap<>();
            dynamicvariables.put("booking_no", kafkaBookingMessage.getBookId());
            dynamicvariables.put("order_no", kafkaBookingMessage.getOrderNo());
            dynamicvariables.put("supplier_ref_no", supplierRefNumber);
            EmailResponse emailResponse = emailUtils.buildClientMail(function, supplierAlertScenario, supplierEmail, supplierAlertSubject, dynamicvariables, null, null);
            if (null != emailResponse && "SUCCESS".equalsIgnoreCase(emailResponse.getStatus())) {
                logger.info("*** Full Cancellation: email sent to externet supplier " + supplierEmail + " ***");
                return true;

            }

        } catch (Exception e) {
            logger.error("Unable to send mail to supplier:" + supplierEmail, e);

        }


        return false;
    }

    //for not externet supplier
    @Override
    public boolean sendMailNotExternetSupplier(String supplierEmail, String emailLink, KafkaBookingMessage kafkaBookingMessage) throws OperationException {

        try {
            //   emailResponseEntity = RestUtils.postForEntity(emailUrl, buildMailSupplier(supplierEmail, emailLink, kafkaBookingMessage), EmailResponse.class);
            logger.info("*** Full Cancellation: Sending mail to not externet supplier**");
            Map<String, String> dynamicvariables = new HashMap<>();
            dynamicvariables.put("booking_no", kafkaBookingMessage.getBookId());
            dynamicvariables.put("order_no", kafkaBookingMessage.getOrderNo());
            dynamicvariables.put("cancellation_link", emailLink);

            EmailResponse emailResponse = emailUtils.buildClientMail(function, supplierApprovalScenario, supplierEmail, supplierApprovalSubject, dynamicvariables, null, null);
            if (null != emailResponse && "SUCCESS".equalsIgnoreCase(emailResponse.getStatus())) {
                logger.info("*** Full Cancellation:email sent to externet supplier " + supplierEmail + " ***");
                return true;

            }

        } catch (Exception e) {
            logger.error("Unable to send mail to supplier:" + supplierEmail, e);

        }


        return false;
    }

    //for client
    @Override
    public boolean sendMailClient(String clientEmail, OpsBooking booking, KafkaBookingMessage kafkaBookingMessage) throws OperationException {

        try {
            logger.info("*** Full Cancellation: Sending mail to client***");
            // emailResponseResponseEntity = RestUtils.postForEntity(emailUrl, buildClientMail(clientEmail, booking, kafkaBookingMessage), EmailResponse.class);
            Map<String, String> dynamicvariables = new HashMap<>();
            dynamicvariables.put("First Name", mdmService.getClientName(booking.getClientID()));
            dynamicvariables.put("booking_cancellation_id", kafkaBookingMessage.getOrderNo());

            EmailResponse emailResponse = emailUtils.buildClientMail(function, clientConfirmationScenario, clientEmail, clientConfirmationSubject, dynamicvariables, null, null);
            if (null != emailResponse && "SUCCESS".equalsIgnoreCase(emailResponse.getStatus())) {
                logger.info("*** Full Cancellation: email sent to client " + clientEmail);
                return true;

            }
        } catch (Exception e) {
            logger.error("Unable to send mail to Client:" + clientEmail, e);

        }

        return false;
    }

    //Inventory booking  and date reached
    @Override
    public boolean sendMailInventorySupplier(String supplierEmail, String emailLink, KafkaBookingMessage kafkaBookingMessage) throws OperationException {
        ResponseEntity<EmailResponse> emailResponseEntity = null;
        try {
            logger.info("*** Full Cancellation: Sending mail to Inventory supplier**");
            Map<String, String> dynamicvariables = new HashMap<>();
            dynamicvariables.put("booking_no", kafkaBookingMessage.getBookId());
            dynamicvariables.put("order_no", kafkaBookingMessage.getOrderNo());
            dynamicvariables.put("cancellation_link", emailLink);

            EmailResponse emailResponse = emailUtils.buildClientMail(function, supplierApprovalScenario, supplierEmail, supplierApprovalSubject, dynamicvariables, null, null);
            if (null != emailResponse && "SUCCESS".equalsIgnoreCase(emailResponse.getStatus())) {
                logger.info("*** Full Cancellation:email sent  to Inventory  supplier " + supplierEmail + " ***");
                return true;

            }
        } catch (Exception e) {
            logger.error("Unable to send mail to supplier:" + supplierEmail, e);
        }

        if (null != emailResponseEntity && "SUCCESS".equalsIgnoreCase(emailResponseEntity.getBody().getStatus())) {
            return true;
        }

        return false;
    }



    /**
     * @param kafkaBookingMessage
     * @throws OperationException
     */

    @Override
    public void sendAlert(KafkaBookingMessage kafkaBookingMessage, String alertType) throws OperationException {
        InlineMessageResource inlineMessageResource = new InlineMessageResource();
        if (alertType.equalsIgnoreCase("request")) {
            inlineMessageResource.setAlertName(opsUserAlertName);
        } else if (alertType.equalsIgnoreCase("supplierConfirm")) {
            inlineMessageResource.setAlertName(opsUserSupplierConfirmAlert);
        }

        ConcurrentHashMap<String, String> dynamicVariable = new ConcurrentHashMap<>();
        dynamicVariable.put("BOOKING_ID", kafkaBookingMessage.getBookId());
        dynamicVariable.put("ORDER_NO", kafkaBookingMessage.getOrderNo());
        inlineMessageResource.setDynamicVariables(dynamicVariable);
        inlineMessageResource.setNotificationType("System");
        //ResponseEntity<NotificationResource> notificationResourceResponseEntity = mdmRestUtils.postForEntity(alertUrl, inlineMessageResource, NotificationResource.class);
        alertService.sendInlineMessageAlert(inlineMessageResource);
        logger.debug("Alert send to ops user for cancellation request BookingId:" + kafkaBookingMessage.getBookId());

    }
}
