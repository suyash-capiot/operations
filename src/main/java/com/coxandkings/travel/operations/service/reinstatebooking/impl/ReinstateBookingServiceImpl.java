package com.coxandkings.travel.operations.service.reinstatebooking.impl;

import com.coxandkings.travel.operations.criteria.communication.CommunicationTagCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsBookingStatus;
import com.coxandkings.travel.operations.model.core.OpsOrderStatus;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.resource.ErrorResponseResource;
import com.coxandkings.travel.operations.service.amendmentandpartialcancellation.AmendAndPartCancService;
import com.coxandkings.travel.operations.service.beconsumer.acco.AccoCancellationService;
import com.coxandkings.travel.operations.service.beconsumer.air.AirCancellationService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.communication.CommunicationService;
import com.coxandkings.travel.operations.service.reinstatebooking.ReinstateBookingService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.EmailUtils;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.supplier.SupplierDetailsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class ReinstateBookingServiceImpl implements ReinstateBookingService {

    private final Logger logger = LogManager.getLogger(ReinstateBookingServiceImpl.class);

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private SupplierDetailsService supplierDetailsService;

    @Autowired
    private AirCancellationService airCancellationService;

    @Autowired
    private AccoCancellationService accoCancellationService;

    @Autowired
    private AmendAndPartCancService amendAndPartCancService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private EmailUtils emailUtils;

    @Value("${reinstateBooking.emails.template_config.function}")
    private String function;

    @Value("${reinstateBooking.emails.template_config.scenario}")
    private String scenario;

    @Value("${reinstateBooking.emails.template_config.subject}")
    private String subject;

    @Autowired
    private CommunicationService communicationService;

    /**
     * INFO: Below function will be called after user clicks on reInstate booking button/link.
     */
    @Override
    public ErrorResponseResource reinstateBooking(String bookingRefNo, String orderID) throws OperationException {
        logger.info("-- Entering ReinstateBookingServiceImpl.reinstateBooking");

        OpsBooking opsBooking = null;
        OpsProduct opsProduct = null;
        String supplierDetailStr = null;
        JSONObject suppDetailJSON = null;

        logger.info("-- Getting booking details for booking : "+bookingRefNo);
        opsBooking = opsBookingService.getBooking(bookingRefNo);

        if (null == opsBooking) {
            throw new OperationException(Constants.BOOKING_NOT_FOUND, bookingRefNo);
        }

        opsProduct = opsBooking.getProducts().stream().filter(obj -> obj.getOrderID().equalsIgnoreCase(orderID)).findFirst().get();
        if (null == opsProduct) {
            throw new OperationException(Constants.PRODUCT_NOT_FOUND_FOR_ORDER, orderID);
        }

        if (opsBooking.getStatus() == OpsBookingStatus.CNF) {
            supplierDetailStr = supplierDetailsService.getSupplierDetails(opsProduct.getSupplierID());
            if (null == supplierDetailStr) {
                throw new OperationException(Constants.SUPPLIER_DETAILS_NOT_AVAILABLE, opsProduct.getSupplierID());
            }

            suppDetailJSON = new JSONObject(supplierDetailStr);
            /**
             * INFO: Checking whether supplier is online or offline
             */
            if (suppDetailJSON.getJSONObject(Constants.JSON_CONTENT_UPDATE_FREQUENCY).getBoolean(Constants.JSON_IS_API_XML_SUPPLIER)) {
                throw new OperationException(Constants.REINSTATE_NOT_FOR_ONLINE_SUPPLIER, opsProduct.getSupplierID());
            } else {
                if (opsProduct.getOrderDetails().getOpsOrderStatus() == OpsOrderStatus.RAMD || opsProduct.getOrderDetails().getOpsOrderStatus() == OpsOrderStatus.RXL) {
                    if (checkWhetherCommunicationSentToSupplier(bookingRefNo, opsProduct)) {
                        sendEmailToSupplierToCheckWhetherChangesMade(opsProduct, suppDetailJSON);
                    } else {
                        throw new OperationException(Constants.EMAIL_COMMUNICATION_NOT_FOUND, opsProduct.getSupplierID());
                    }
                } else {
                    throw new OperationException(Constants.PRODUCT_NOT_REQUEST_AMENDMENT, opsProduct.getProductName(), opsProduct.getSupplierID());
                }
            }
        } else {
            /**
             * INFO: If booking is not confirmed then need to do full cancellation and rebooking through WEM.
             */
            switch (opsProduct.getProductSubCategory()) {
                case Constants.PROD_CAT_SUB_FLIGHT:
                    logger.info("-- Processing cancellation for flight with order # "+opsProduct.getOrderID()+" using airCancellationService");
                    airCancellationService.processCancellation(opsBooking, opsProduct);
                    break;
                case Constants.PROD_CAT_SUB_HOTEL:
                    logger.info("-- Processing cancellation for hotel with order # "+opsProduct.getOrderID()+" using airCancellationService");
                    accoCancellationService.processCancellation(opsBooking, opsProduct);
                    break;
            }
        }
        logger.info("-- Exiting ReinstateBookingServiceImpl.reinstateBooking");
        return amendAndPartCancService.getMessageToUser(messageSource.getMessage(Constants.REINSTATE_SUCCESS, new Object[]{bookingRefNo, orderID}, Locale.US), HttpStatus.OK);
    }

    private boolean checkWhetherCommunicationSentToSupplier(String bookingRefNo, OpsProduct opsProduct) throws OperationException {
        logger.info("-- Entering ReinstateBookingServiceImpl.checkWhetherCommunicationSentToSupplier");
        /**
         * INFO: This method will check whether communication is sent to supplier for a particular booking.
         */
        CommunicationTagCriteria communicationTagCriteria = null;

        try {
            communicationTagCriteria = new CommunicationTagCriteria();
            communicationTagCriteria.setBookId(bookingRefNo);
            communicationTagCriteria.setOrderID(opsProduct.getOrderID());
            communicationTagCriteria.setSupplierID(opsProduct.getSupplierID());
            communicationTagCriteria.setActionType(opsProduct.getOrderDetails().getOpsOrderStatus().toString());

            logger.info("-- Exiting ReinstateBookingServiceImpl.checkWhetherCommunicationSentToSupplier");
            return communicationService.getCommunicationByCommunicationTagsCriteria(communicationTagCriteria).size() == 0 ? false : true;
        } catch (Exception e) {
            logger.error("Exception is: ", e);
            throw new OperationException(Constants.TECHNICAL_ISSUE_EMAIL_SEARCH, opsProduct.getSupplierID());
        }
    }

    private void sendEmailToSupplierToCheckWhetherChangesMade(OpsProduct opsProduct, JSONObject suppDetailJSON) throws OperationException {
        logger.info("-- Entering ReinstateBookingServiceImpl.sendEmailToSupplierToCheckWhetherChangesMade");
        
        Map<String, String> dynamicValuesMap = null;
        String suppEmailID = null;

        try {
            //suppEmailID = jsonObjectProvider.getAttributeValue(suppDetailJSON.toString(), "$.contactInfo.contactDetails.email");
            /**
             * TODO: Due to data not available, for testing passing static email id. After getting proper data uncomment above line and remove below line.
             */
            suppEmailID = "ranjith.manchala@coxandkings.com";
            /**
             * INFO: Sending emails to supplier to check whether requested changes are made at its end or not.
             */

            dynamicValuesMap = new HashMap<String, String>();
            dynamicValuesMap.put("suppName", opsProduct.getSupplierID());
            dynamicValuesMap.put("suppRefNo", opsProduct.getSupplierRefNumber());
            logger.info("-- Sending email to : "+suppEmailID);
            emailUtils.buildClientMail(function, scenario, suppEmailID, subject + opsProduct.getSupplierRefNumber(), dynamicValuesMap, null, null);
            logger.info("-- Exiting ReinstateBookingServiceImpl.sendEmailToSupplierToCheckWhetherChangesMade");
        } catch (Exception e) {
            throw new OperationException(Constants.EMAIL_SENT_SUPPLIER_FAILURE, opsProduct.getSupplierID());
        }
    }
}