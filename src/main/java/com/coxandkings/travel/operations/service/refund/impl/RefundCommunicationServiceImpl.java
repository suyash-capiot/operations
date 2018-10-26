package com.coxandkings.travel.operations.service.refund.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.resource.notification.NotificationResource;
import com.coxandkings.travel.operations.service.refund.RefundCommunicationService;
import com.coxandkings.travel.operations.utils.EmailUtils;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RefundCommunicationServiceImpl implements RefundCommunicationService {
    @Value("${communication.email.api}")
    private String emailUrl;

    @Value("${communication.alert.api}")
    private String alertUrl;

    @Value("${communication.email.from_address}")
    private String fromEmailAddress;
    @Value("${communication.email.process}")
    private String process;
    @Value("${refund.email.function}")
    private String function;
    @Value("${refund.alert.finance_alert_name}")
    private String financeAlertName;

    @Value("${refund.alert.approve_alert}")
    private String approveAlert;

    @Value("${refund.email.refund_confirmation.scenario}")
    private String confirmationScenario;

    @Value("${refund.email.refund_confirmation.subject}")
    private String confirmationSubject;

    @Value("${refund.email.refund_promo_code.scenario}")
    private String promoScenario;

    @Value("${refund.email.refund_promo_code.subject}")
    private String promoSubject;

    private static final Logger logger = LogManager.getLogger(RefundCommunicationServiceImpl.class);

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private EmailUtils emailUtils;

    /**
     * when refund is processed then need to send email to client
     *
     * @param clientEmail
     * @param bookingNo
     * @param orderNo
     * @return
     */
    @Override
    public boolean sendMailToClient(String clientEmail, String clientName, String bookingNo, String orderNo) {
        EmailResponse emailResponseEntity = null;
        try {
            Map<String, String> dynamicVariable = new HashMap<>();
            dynamicVariable.put("booking_no", bookingNo);
            dynamicVariable.put("order_no", orderNo);
            dynamicVariable.put("first_name", clientName);

            emailResponseEntity = emailUtils.buildClientMail(function, confirmationScenario, clientEmail, confirmationSubject, dynamicVariable, null, null);


        } catch (Exception e) {
            logger.error("Unable to send mail to client:" + clientEmail, e);

        }
        if (null != emailResponseEntity && "SUCCESS".equalsIgnoreCase(emailResponseEntity.getStatus())) {
            return true;

        }

        return false;

    }

    /**
     * This method will be used to send refund Redeemable number or promo code
     *
     * @param clientEmail
     * @param bookingNo
     * @param orderNo
     * @param creditNoteNo
     * @return
     */

    @Override
    public boolean sendMailToClient(String clientEmail, String clientName, String bookingNo, String orderNo, String creditNoteNo) {
        EmailResponse emailResponseEntity = null;
        try {

            Map<String, String> dynamicVariable = new HashMap<>();
            dynamicVariable.put("booking_no", bookingNo);
            dynamicVariable.put("order_no", orderNo);
            dynamicVariable.put("promo_code", creditNoteNo);
            dynamicVariable.put("first_name", clientName);

            emailResponseEntity = emailUtils.buildClientMail(function, promoScenario, clientEmail, promoSubject, dynamicVariable, null, null);


        } catch (Exception e) {
            logger.error("Unable to send mail to client:" + clientEmail, e);

        }
        if (null != emailResponseEntity && "SUCCESS".equalsIgnoreCase(emailResponseEntity.getStatus())) {
            return true;

        }

        return false;
    }

    @Override
    public void sendAlert(String refundClaimNo) throws OperationException {
        InlineMessageResource inlineMessageResource = new InlineMessageResource();

        inlineMessageResource.setAlertName(financeAlertName);


        ConcurrentHashMap<String, String> dynamicVariable = new ConcurrentHashMap<>();
        dynamicVariable.put("Refund_Claim_No", refundClaimNo);

        inlineMessageResource.setDynamicVariables(dynamicVariable);
        inlineMessageResource.setNotificationType("System");
        try {
            RestUtils.postForEntity(alertUrl, inlineMessageResource, NotificationResource.class);
            logger.info("Alert send to finance user for Refund Type change Refund claim no:" + refundClaimNo);
        } catch (Exception e) {
            logger.error("Unable to send alert to finance user Refund Claim No:" + refundClaimNo);
        }


    }
    public void sendApproverAlert(String refundClaimNo){
        InlineMessageResource inlineMessageResource = new InlineMessageResource();

        inlineMessageResource.setAlertName(approveAlert);


        ConcurrentHashMap<String, String> dynamicVariable = new ConcurrentHashMap<>();
        dynamicVariable.put("claimNo", refundClaimNo);

        inlineMessageResource.setDynamicVariables(dynamicVariable);
        inlineMessageResource.setNotificationType("System");
        try {
            RestUtils.postForEntity(alertUrl, inlineMessageResource, NotificationResource.class);
            logger.info("Alert send to Ops user for Refund refund approved claim no:" + refundClaimNo);
        } catch (Exception e) {
            logger.error("Unable to send alert to ops User claim No:" + refundClaimNo);
        }
    }


}
