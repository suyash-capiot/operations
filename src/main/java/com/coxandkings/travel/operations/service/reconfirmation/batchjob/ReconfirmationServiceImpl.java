package com.coxandkings.travel.operations.service.reconfirmation.batchjob;

import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductStatus;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.reconfirmation.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.reconfirmation.ReconfirmationMonitor;
import com.coxandkings.travel.operations.model.reconfirmation.ReconfirmationRequest;
import com.coxandkings.travel.operations.model.reconfirmation.client.ClientReconfirmationDetails;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationDetails;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationFilter;
import com.coxandkings.travel.operations.repository.reconfirmation.client.ClientReconfirmationRepository;
import com.coxandkings.travel.operations.repository.reconfirmation.supplier.SupplierReconfirmationRepository;
import com.coxandkings.travel.operations.resource.notification.NotificationResource;
import com.coxandkings.travel.operations.resource.reconfirmation.client.ClientReconfirmationResource;
import com.coxandkings.travel.operations.resource.reconfirmation.response.reconfirmation.ClientReconfirmationResponse;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.reconfirmation.client.ClientReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.client.ReconfirmFlightsClientService;
import com.coxandkings.travel.operations.service.reconfirmation.client.ReconfirmHotelsClientService;
import com.coxandkings.travel.operations.service.reconfirmation.common.*;
import com.coxandkings.travel.operations.service.reconfirmation.mdm.ReconfirmationMDMService;
import com.coxandkings.travel.operations.service.reconfirmation.supplier.ReconfirmFlightsSupplierService;
import com.coxandkings.travel.operations.service.reconfirmation.supplier.ReconfirmHotelsSupplierService;
import com.coxandkings.travel.operations.service.reconfirmation.supplier.SupplierReconfirmationService;
import com.coxandkings.travel.operations.service.template.TemplateLoaderService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.coxandkings.travel.operations.utils.adapter.OpsBookingAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationConfigFor.CLIENT;
import static com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationConfigFor.SUPPLIER;

/**
 *
 */
@Service("reconfirmationService")
public class ReconfirmationServiceImpl implements ReconfirmationService {

    @Autowired
    private ReconfirmationUtilityService reconfirmationUtilityService;

    @Autowired
    private ClientReconfirmationRepository clientReconfirmationRepository;

    @Autowired
    private SupplierReconfirmationRepository supplierReconfirmationRepository;

    @Autowired
    private TemplateLoaderService templateLoaderService;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private OpsBookingAdapter opsBookingAdapter;

    @Autowired
    private SupplierReconfirmationService supplierReconfirmationService;

    @Autowired
    private ClientReconfirmationService clientReconfirmationService;

    @Autowired
    private ReconfirmFlightsSupplierService reconfirmFlightsSupplierService;

    @Autowired
    private ReconfirmHotelsSupplierService reconfirmHotelsSupplierService;

    @Autowired
    private ReconfirmationMDMService reconfirmationMDMService;

    @Autowired
    private ReconfirmFlightsClientService reconfirmFlightsClientService;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private ReconfirmHotelsClientService reconfirmHotelsClientService;

    @Autowired
    private RestUtils restUtils;

    @Value(value = "${reconfirmation.supplier.communication.subject}")
    private String supplierCommunicationSubject;

    @Value(value = "${reconfirmation.supplier.be.update_reconfirmation_status}")
    private String updateSupplierReconfirmationStatusInBE;

    @Value(value = "${reconfirmation.alertConfig.businessProcess}")
    private String businessProcess;

    @Value(value = "${reconfirmation.alertConfig.function}")
    private String function;

    @Value(value = "${reconfirmation.alertConfig.alertName}")
    private String onHoldAlertName;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private HashGenerator hashGenerator;

    private static Logger logger = LogManager.getLogger(ReconfirmationServiceImpl.class);

    /**
     * @param clientReconfirmationDetails
     * @return
     */
    @Override
    public ClientReconfirmationDetails saveOrUpdateCCReconfirmation(ClientReconfirmationDetails clientReconfirmationDetails) {
        return clientReconfirmationRepository.saveOrUpdateClientReconfirmation(clientReconfirmationDetails);
    }

    /**
     * @param supplierReconfirmationDetails
     * @return
     */
    @Override
    public SupplierReconfirmationDetails saveUpdateSSReconfirmation(SupplierReconfirmationDetails supplierReconfirmationDetails) {
        return supplierReconfirmationRepository.saveOrUpdateSupplierReconfirmation(supplierReconfirmationDetails);
    }

    /**
     * @param reconfirmationCutOff
     * @return
     */
    @Override
    public ReconfirmationCutOffType getReconfirmationCutOffType(String reconfirmationCutOff) {
        if (reconfirmationCutOff.equalsIgnoreCase(ReconfirmationCutOffType.FROM_BOOKING_DATE.getValue())) {
            return ReconfirmationCutOffType.FROM_BOOKING_DATE;
        } else {
            return ReconfirmationCutOffType.PRIOR_TO_TRAVEL_DATE;
        }
    }

    /**
     *
     */
    @Override
    public void schedulerJob() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<SupplierReconfirmationDetails> allSupplierReconfirmation = supplierReconfirmationRepository.getAllSupplierReconfirmation();
                    for (SupplierReconfirmationDetails reconfirmation : allSupplierReconfirmation) {
                        try {
                            ZonedDateTime reconfirmationCutOffDate = reconfirmation.getSupplierReconfirmationDate();
                            if (reconfirmationCutOffDate != null
                                    && isReconfirmationCutOffReached(reconfirmationCutOffDate)
                                    && reconfirmation.isReconfirmationSentToSupplier() == false
                                    && reconfirmation.getSupplierEmailId() != null) {

                                OpsBooking bookingDetails = getBookingById(reconfirmation.getBookRefNo());
                                OpsProduct productDetails = getProductById(bookingDetails, reconfirmation.getOrderID());
                                createAlertForSupplierReconfirmation(reconfirmation);
                                sendReconfirmationRequestToSupplier(bookingDetails, productDetails, reconfirmation);
                            }

                            if (reconfirmationCutOffDate != null && isReconfirmationCutOffPassed(reconfirmationCutOffDate) && reconfirmation.isReconfirmationSentToSupplier() == false) {
                                OpsBooking bookingDetails = getBookingById(reconfirmation.getBookRefNo());
                                OpsProduct productDetails = getProductById(bookingDetails, reconfirmation.getOrderID());
                                sendReconfirmationRequestToSupplier(bookingDetails, productDetails, reconfirmation);
                            }


                        } catch (OperationException e) {
                            // e.printStackTrace();
                            logger.error(e.toString());
                        }
                    }
                } catch (Exception e) {
                    // e.printStackTrace();
                    logger.error(e.toString());
                }
            }

        }).start();


        try {
            List<ClientReconfirmationDetails> allClientReconfirmation = this.clientReconfirmationRepository.getAllClientReconfirmation();
            for (ClientReconfirmationDetails reconfirmation : allClientReconfirmation) {
                try {
                    ZonedDateTime reconfirmationCutOffDate = reconfirmation.getClientOrCustomerReconfirmationDate();
                    if (reconfirmationCutOffDate != null
                            && isReconfirmationCutOffReached(reconfirmationCutOffDate)
                            && reconfirmation.isReconfirmationSentToClient() == false
                            && reconfirmation.getClientEmailId() != null) {

                        OpsBooking bookingDetails = getBookingById(reconfirmation.getBookRefNo());
                        OpsProduct productDetails = getProductById(bookingDetails, reconfirmation.getOrderID());
                        createAlertForClientReconfirmation(reconfirmation);
                        sendReconfirmationRequestToCustomer(productDetails, bookingDetails, reconfirmation);
                    }
                    if (reconfirmationCutOffDate != null
                            && isReconfirmationCutOffPassed(reconfirmationCutOffDate)
                            && reconfirmation.isReconfirmationSentToClient() == false) {

                        OpsBooking bookingDetails = getBookingById(reconfirmation.getBookRefNo());
                        OpsProduct productDetails = getProductById(bookingDetails, reconfirmation.getOrderID());
                        sendReconfirmationRequestToCustomer(productDetails, bookingDetails, reconfirmation);
                    }
                } catch (Exception e) {
                    // e.printStackTrace();
                    logger.error(e.toString());
                }
            }

        } catch (Exception e) {
            // e.printStackTrace();
            logger.error(e.toString());
        }


    }


    //: BR 327 if the reconfirmation has to be sent to suppler then
    //: if there are multiple products within a booking from hte
    //: same supplier and all products are eligible for reconfirmation then system shall send a common
    //: reconfirmation request to supplier with the product wise details
    //: supplier will have provision to reconfirm /reject option at product level

    /**
     * @param isSameSupplier
     * @param isMultipleProducts
     * @param isAllProductsAreEligibleForReconfirmation
     * @param products
     * @param aBooking
     * @param supplierReoconfDetails
     * @param clientReconfirmationDetails
     * @throws OperationException
     */
    private void requestForReconfirmation(boolean isSameSupplier, boolean isMultipleProducts, boolean isAllProductsAreEligibleForReconfirmation,
                                          List<OpsProduct> products, OpsBooking aBooking, SupplierReconfirmationDetails supplierReoconfDetails,
                                          ClientReconfirmationDetails clientReconfirmationDetails) throws OperationException {
        if (isSameSupplier) {


        } else {
            // TODO: if the supplier are different then system shall send separate reconfirmation request product wise
            for (OpsProduct aProduct : products) {

                try {
                    ReconfirmationRequest reconfirmationRequest = new ReconfirmationRequest();
                    this.selectionProcess(aProduct, aBooking, reconfirmationRequest);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * @param aProduct
     * @param aBooking
     * @throws OperationException
     */
    @Override
    public void processBookingWithProduct(OpsProduct aProduct, OpsBooking aBooking) throws OperationException {
        ReconfirmationRequest reconfirmationRequest = new ReconfirmationRequest();
        this.selectionProcess(aProduct, aBooking, reconfirmationRequest);

    }

    /**
     * @param bookingDetails
     */
    @Override
    public void processBooking(OpsBooking bookingDetails, KafkaBookingMessage kafkaBookingMessage) {
        List<OpsProduct> products = bookingDetails.getProducts();
        try {
            this.requestForReconfirmation(false, false, false, products, bookingDetails, null, null);
        } catch (OperationException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param reconfirmationCutOff
     * @param travelDate
     * @param hours
     * @return
     * @throws OperationException
     */
    @Override
    public boolean getReconfirmationCutOffDate(String reconfirmationCutOff, String travelDate, long hours) throws OperationException {

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime reconfirmationCutOffDate = this.getReconfirmationCutOffDate(reconfirmationCutOff, now, now, hours);
        boolean reconfirmationCutOffReached = isReconfirmationCutOffReached(reconfirmationCutOffDate);
        return reconfirmationCutOffReached;
    }

    /**
     * @param opsBooking
     * @param opsProduct
     * @param reconfirmationConfig
     * @param request
     * @return
     * @throws OperationException
     */
    @Override
    public ReconfirmationMonitor reconfigurationMonitor(OpsBooking opsBooking, OpsProduct opsProduct,
                                                        ReconfirmationConfiguration reconfirmationConfig, ReconfirmationRequest request) throws OperationException {

        return null;
    }

    /**
     * @param cutOffDateFromMDM
     * @return
     */
    @Override
    public boolean isReconfirmationCutOffReached(ZonedDateTime cutOffDateFromMDM) {
        ZoneId zone = cutOffDateFromMDM.getZone();
        ZonedDateTime today = ZonedDateTime.now(zone);
        // logger.info("comparing this two dates : " + cutOffDateFromMDM.toString() + " and " + today.toString());
        if (cutOffDateFromMDM.getYear() == today.getYear() && cutOffDateFromMDM.getDayOfMonth() == today.getDayOfMonth()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param cutOffDateFromMDM
     * @return
     */
    public boolean isReconfirmationCutOffPassed(ZonedDateTime cutOffDateFromMDM) {
        ZoneId zone = cutOffDateFromMDM.getZone();
        ZonedDateTime today = ZonedDateTime.now(zone);
        // logger.info("comparing this two dates : " + cutOffDateFromMDM.toString() + " and " + today.toString());
        if (cutOffDateFromMDM.getYear() == today.getYear() && cutOffDateFromMDM.getDayOfMonth() > today.getDayOfMonth()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param aProduct
     * @param aBooking
     * @param clientReconfirmationDetails
     * @return
     */
    private ClientReconfirmationDetails sendReconfirmationRequestToCustomer(OpsProduct aProduct, OpsBooking aBooking, ClientReconfirmationDetails clientReconfirmationDetails) {
        try {
            String supplierEmailId = null;
            String clientEmailId = null;

            CustomSupplierDetails supplierContactDetails = this.reconfirmationMDMService.getSupplierContactDetails(aProduct.getSupplierID());
            if (supplierContactDetails != null) {
                supplierEmailId = supplierContactDetails.getEmailId() != null ? supplierContactDetails.getEmailId() : "";
                clientReconfirmationDetails.setSupplierEmailId(supplierEmailId);
            }
            CustomClientDetails clientContactDetails = this.reconfirmationMDMService.getB2BClientContactDetails(aBooking.getClientID(), aBooking.getClientType());
            if (clientContactDetails != null) {
                clientEmailId = clientContactDetails.getEmailId() != null ? clientContactDetails.getEmailId() : "";
                clientReconfirmationDetails.setClientEmailId(clientEmailId);
            }
            if (null != clientReconfirmationDetails.getHash()) {
                clientReconfirmationDetails.setHash(hashGenerator.getHash());
            }
            try {
                boolean status = reconfirmationUtilityService.composeEmailForClient(clientReconfirmationDetails, aBooking, aProduct, clientContactDetails, clientReconfirmationDetails.getHash(), true, false);
                clientReconfirmationDetails.setReconfirmationSentToClient(status);
                clientReconfirmationDetails.setNumberOfTimesReconfirmationSent(clientReconfirmationDetails.getNumberOfTimesReconfirmationSent()
                        == null ? 1 : clientReconfirmationDetails.getNumberOfTimesReconfirmationSent() + 1);
                clientReconfirmationDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_PENDING);
            } catch (Exception e) {
                e.printStackTrace();
            }
            clientReconfirmationDetails.setTemplate("");
            clientReconfirmationDetails = this.clientReconfirmationService.saveOrUpdateClientReconfirmation(clientReconfirmationDetails);

            return clientReconfirmationDetails;
        } catch (Exception e) {
            return null;
        }

    }


    /**
     * @param aBooking
     * @param aProduct
     * @param supplierReoconfDetails
     * @return
     * @throws OperationException
     */
    private SupplierReconfirmationDetails sendReconfirmationRequestToSupplier(OpsBooking aBooking, OpsProduct aProduct, SupplierReconfirmationDetails supplierReoconfDetails) throws OperationException {
        try {
            String emailId = null;
            CustomSupplierDetails supplierContactDetails = this.reconfirmationMDMService.getSupplierContactDetails(aProduct.getSupplierID());
            if (supplierContactDetails != null) {
                emailId = supplierContactDetails.getEmailId();
                supplierReoconfDetails.setSupplierEmailId(emailId);
                supplierReoconfDetails.setSupplierName(supplierContactDetails.getSupplierName());
            }
            try {
                boolean status = reconfirmationUtilityService.composeEmailForSupplier(supplierReoconfDetails, aBooking, aProduct, supplierContactDetails, supplierReoconfDetails.getHash(), false, true);
                supplierReoconfDetails.setReconfirmationSentToSupplier(status);
                supplierReoconfDetails.setNumberOfTimesReconfirmationSent(supplierReoconfDetails.getNumberOfTimesReconfirmationSent()
                        == null ? 1 : supplierReoconfDetails.getNumberOfTimesReconfirmationSent() + 1);
                supplierReoconfDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_PENDING);
            } catch (Exception e) {
                e.printStackTrace();
            }
            supplierReoconfDetails.setTemplate("");

            supplierReoconfDetails = this.supplierReconfirmationService.saveOrUpdateSupplierReconfirmation(supplierReoconfDetails);
            this.reconfirmationUtilityService.prettyJSON(supplierReoconfDetails);
            return supplierReoconfDetails;
        } catch (Exception e) {
            supplierReoconfDetails.setReconfirmationSentToSupplier(false);
            return supplierReoconfDetails;
        }
    }

    /**
     * @param reconfirmationConfiguration
     * @param aProduct
     * @param aBooking
     * @param request
     * @param supplierReconfirmation
     * @param clientReconfirmationDetails
     * @return
     * @throws OperationException
     */
    @Override
    public ReconfirmationRequest processReconfirmation(ReconfirmationConfiguration reconfirmationConfiguration, OpsProduct aProduct, OpsBooking aBooking,
                                                       ReconfirmationRequest request, SupplierReconfirmationDetails supplierReconfirmation,
                                                       ClientReconfirmationDetails clientReconfirmationDetails) throws OperationException {

        switch (reconfirmationConfiguration.getConfigFor()) {
            case SUPPLIER: {
                request = this.supplier(reconfirmationConfiguration, request);
                if (request.isReconfirmationConfigurationDefined()) {
                    ReconfirmationMonitor reconfirmationMonitor = this.reconfigurationMonitor(aBooking, aProduct, reconfirmationConfiguration, request);
                    if (request.isCutOffReached()) {
                        supplierReconfirmation.setReconfirmedBy(reconfirmationConfiguration.getConfigFor().getValue());
                        supplierReconfirmation.setReconfirmationCutOffDate(request.getReconfirmationCutOffDate());
                        supplierReconfirmation.setSupplierReconfirmationDate(request.getSupplierReconfirmationDate());
                        logger.debug("reconfirmation started for supplier");
                        supplierReconfirmation = this.sendReconfirmationRequestToSupplier(aBooking, aProduct, supplierReconfirmation);
                    } else {
                        if (request.isCutOffPassed()) {
                            logger.debug(": send reconfirmation immediately (Manually)");
                            supplierReconfirmation.setReconfirmedBy(reconfirmationConfiguration.getConfigFor().getValue());
                            supplierReconfirmation.setReconfirmationCutOffDate(request.getReconfirmationCutOffDate());
                            supplierReconfirmation.setSupplierReconfirmationDate(request.getSupplierReconfirmationDate());
                            supplierReconfirmation = this.sendReconfirmationRequestToSupplier(aBooking, aProduct, supplierReconfirmation);
                        } else {
                            logger.debug(": This product is not eligible for reconfirmation");
                            throw new OperationException(Constants.ER320);
                        }
                    }
                } else {
                    logger.debug("Reconfirmation Configuration record does not exist for product/client/supplier");
                    throw new OperationException(Constants.ER319);
                }
                return request;
            }
            case CLIENT: {
                request = this.client(reconfirmationConfiguration, request);

                if (request.isReconfirmationConfigurationDefined()) {
                    ReconfirmationMonitor reconfirmationMonitor = this.reconfigurationMonitor(aBooking, aProduct, reconfirmationConfiguration, request);
                    if (request.isCutOffReached()) {

                        clientReconfirmationDetails.setReconfirmationCutOffDate(request.getReconfirmationCutOffDate());
                        clientReconfirmationDetails.setClientOrCustomerReconfirmationDate(request.getClientReconfirmationDate());

                        logger.debug("reconfirmation started for client");
                        clientReconfirmationDetails = this.sendReconfirmationRequestToCustomer(aProduct, aBooking, clientReconfirmationDetails);
                    } else {
                        if (request.isCutOffPassed()) {
                            logger.debug(" send reconfirmation immediately ");
                            clientReconfirmationDetails = reconfirmFlightsClientService.reconfirmClientForFlights(aBooking, aProduct, clientReconfirmationDetails);
                            clientReconfirmationDetails.setReconfirmationCutOffDate(request.getReconfirmationCutOffDate());
                            clientReconfirmationDetails.setClientOrCustomerReconfirmationDate(request.getClientReconfirmationDate());
                            logger.debug("reconfirmation started for client");
                            clientReconfirmationDetails = this.sendReconfirmationRequestToCustomer(aProduct, aBooking, clientReconfirmationDetails);
                        } else {
                            logger.debug(" This product is not eligible for reconfirmation");
                            throw new OperationException(Constants.ER320);
                        }
                    }
                } else {
                    logger.debug("Reconfirmation Configuration record does not exist for product/client/supplier");
                    throw new OperationException(Constants.ER319);
                }
                return request;
            }

            case SUPPLIER_AND_CUSTOMER: {
                request = this.client(reconfirmationConfiguration, request);
                if (request.isReconfirmationConfigurationDefined()) {
                    ReconfirmationMonitor reconfirmationMonitor = this.reconfigurationMonitor(aBooking, aProduct, reconfirmationConfiguration, request);
                    if (request.isCutOffReached()) {
                        clientReconfirmationDetails = reconfirmFlightsClientService.reconfirmClientForFlights(aBooking, aProduct, clientReconfirmationDetails);
                        clientReconfirmationDetails.setReconfirmationCutOffDate(request.getReconfirmationCutOffDate());
                        clientReconfirmationDetails.setClientOrCustomerReconfirmationDate(request.getClientReconfirmationDate());
                        logger.debug("reconfirmation started for client");
                        clientReconfirmationDetails = this.sendReconfirmationRequestToCustomer(aProduct, aBooking, clientReconfirmationDetails);
                    } else {
                        if (request.isCutOffPassed()) {
                            logger.debug(" send reconfirmation immediately ");
                            clientReconfirmationDetails = reconfirmFlightsClientService.reconfirmClientForFlights(aBooking, aProduct, clientReconfirmationDetails);
                            clientReconfirmationDetails.setReconfirmationCutOffDate(request.getReconfirmationCutOffDate());
                            clientReconfirmationDetails.setClientOrCustomerReconfirmationDate(request.getClientReconfirmationDate());
                            logger.debug("reconfirmation started for client");
                            clientReconfirmationDetails = this.sendReconfirmationRequestToCustomer(aProduct, aBooking, clientReconfirmationDetails);  //
                        } else {
                            logger.debug("This product is not eligible for reconfirmation , either throw error or skip");
                            throw new OperationException(Constants.ER320);
                        }
                    }
                } else {
                    logger.debug(" Reconfirmation Configuration record does not exist for product/client/supplier");
                    throw new OperationException(Constants.ER319);
                }
                return request;
            }
            default: {
                throw new OperationException(Constants.ER301);
            }
        }
    }

    /**
     * @param reconfirmationCutOff
     * @param travelDate
     * @param bookingDate
     * @param hours
     * @return
     * @throws OperationException
     */
    @Override
    public ZonedDateTime getReconfirmationCutOffDate(String reconfirmationCutOff, ZonedDateTime travelDate, ZonedDateTime bookingDate, long hours) throws OperationException {
        if (reconfirmationCutOff != null) {
            // : BR 316
            if (reconfirmationCutOff.equalsIgnoreCase(ClientSupplierCommercialCommonEnums.FROM_BOOKING_DATE.getValue())) {
                bookingDate = bookingDate.plusHours(hours);
                //: BR 317
                DayOfWeek ifSaturday = bookingDate.getDayOfWeek();
                if (ifSaturday == DayOfWeek.SATURDAY) {
                    bookingDate = bookingDate.plusHours(Long.parseLong(CommonEnums.HOURS.getValue()));
                }
                DayOfWeek ifSunday = bookingDate.getDayOfWeek();
                if (ifSunday == DayOfWeek.SUNDAY) {
                    bookingDate = bookingDate.plusHours(Long.parseLong(CommonEnums.HOURS.getValue()));
                }
                return bookingDate;
            } else
                // : BR 315
                if (reconfirmationCutOff.equalsIgnoreCase(ClientSupplierCommercialCommonEnums.PRIOR_TO_TRAVEL_DATE.getValue())) {
                    travelDate = travelDate.minusHours(hours);

                    DayOfWeek ifSunday = travelDate.getDayOfWeek();
                    if (ifSunday == DayOfWeek.SUNDAY) {
                        travelDate = travelDate.minusHours(Long.parseLong(CommonEnums.HOURS.getValue()));
                    }
                    //: BR 317
                    DayOfWeek ifSaturday = travelDate.getDayOfWeek();
                    if (ifSaturday == DayOfWeek.SATURDAY) {
                        travelDate = travelDate.minusHours(Long.parseLong(CommonEnums.HOURS.getValue()));
                    }

                    return travelDate;
                } else {
                    throw new OperationException(Constants.ER301);
                }
        } else {
            throw new OperationException(Constants.ER321);
        }

    }


    private ZonedDateTime updateReconfirmationDate(String reconfirmationCutOff, ZonedDateTime travelDate, ZonedDateTime bookingDate, long hours) {
        try {
            ZonedDateTime reconfirmationCutOffDate = getReconfirmationCutOffDate(reconfirmationCutOff, travelDate, bookingDate, hours);
            return reconfirmationCutOffDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ZonedDateTime.now();
    }

    /**
     * @param aProduct
     * @param aBooking
     * @param reconfirmationRequest
     * @throws OperationException
     */
    public void selectionProcess(OpsProduct aProduct, OpsBooking aBooking, ReconfirmationRequest reconfirmationRequest) throws OperationException {

        OpsProductCategory aProductCategory = OpsProductCategory.getProductCategory(aProduct.getProductCategory());
        switch (aProductCategory) {

            case PRODUCT_CATEGORY_TRANSPORTATION:
                if (aProduct.getProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
                    OpsOrderDetails orderDetails = aProduct.getOrderDetails();
                    OpsOrderStatus opsOrderStatus = orderDetails.getOpsOrderStatus();
                    if (opsOrderStatus.getProductStatus().equalsIgnoreCase(OpsProductStatus.OK.getProductStatus()) /*|| opsOrderStatus.getProductStatus().equalsIgnoreCase(OpsProductStatus.TL.getProductStatus())*/) {

                        OpsFlightDetails flightDetails = aProduct.getOrderDetails().getFlightDetails();
                        SupplierReconfirmationFilter filter = new SupplierReconfirmationFilter();
                        //  filter.setSupplierId(aProduct.getSupplierID() != null ? aProduct.getSupplierID() : null);
                        filter.setProductCategory(aProduct.getProductCategory() != null ? aProduct.getProductCategory() : null);
                        filter.setProductCatSubtype(aProduct.getProductSubCategory() != null ? aProduct.getProductSubCategory() : null);

                        ReconfirmationConfiguration configuration = reconfirmationMDMService.getConfiguration(filter);
                        if (configuration == null) {
                            configuration = new ReconfirmationConfiguration();
                            configuration.setConfigFor(ReconfirmationConfigFor.CLIENT_AND_SUPPLIER);
                            // throw new OperationException(Constants.ER311);
                        }

                        if (configuration.getConfigFor() == null) {
                            configuration = new ReconfirmationConfiguration();
                            configuration.setConfigFor(ReconfirmationConfigFor.CLIENT_AND_SUPPLIER);
                        }
                        CustomFlightDetails customFlightDetails = this.reconfirmFlightsSupplierService.getCustomFlightDetails(flightDetails);

                        reconfirmationRequest.setClientReconfirmationDate(customFlightDetails.getClientReconfirmationDate() != null ? customFlightDetails.getClientReconfirmationDate() : null);
                        reconfirmationRequest.setSupplierReconfirmationDate(customFlightDetails.getSupplierReconfirmationDate() != null ? customFlightDetails.getSupplierReconfirmationDate() : null);
                        reconfirmationRequest.setTravelDate(customFlightDetails.getTravelDate() != null ? customFlightDetails.getTravelDate() : null);
                        reconfirmationRequest.setBookingDate(aBooking.getBookingDateZDT());
                        reconfirmationRequest.setBookingRefNumber(aBooking.getBookID());
                        reconfirmationRequest.setOrderID(aProduct.getOrderID());


                        switch (configuration.getConfigFor()) {
                            case SUPPLIER: {
                                long hours = reconfirmationUtilityService.convertToHours(configuration.getSupplierConfiguration().getDuration(), configuration.getSupplierConfiguration().getDurationType());
                                ZonedDateTime supplierReconfirmationDate = updateReconfirmationDate(configuration.getSupplierConfiguration().getReconfirmationCutOff(), customFlightDetails.getTravelDate(), aBooking.getBookingDateZDT(), hours);
                                SupplierReconfirmationDetails supplierReconfirmation = null;
                                supplierReconfirmation = this.supplierReconfirmationService.findByBookRefAndOrderNo(aBooking.getBookID(), aProduct.getOrderID());
                                if (supplierReconfirmation == null) {
                                    supplierReconfirmation = new SupplierReconfirmationDetails();
                                }
                                supplierReconfirmation = this.reconfirmFlightsSupplierService.reconfirmSupplierForFlights(aBooking, aProduct, supplierReconfirmation);
                                supplierReconfirmation.setReconfirmationCutOffDate(supplierReconfirmationDate);
                                this.supplierReconfirmationService.saveOrUpdateSupplierReconfirmation(supplierReconfirmation);
                                supplierReconfirmationService.updateSupplierReconfirmationDateInBookingEngine(supplierReconfirmation);
                                // this.processReconfirmation( configuration , aProduct , aBooking , reconfirmationRequest , supplierReconfirmation , null );
                                break;
                            }
                            case CLIENT: {

                                long hours = reconfirmationUtilityService.convertToHours(configuration.getClientConfiguration().getDuration(), configuration.getSupplierConfiguration().getDurationType());
                                ZonedDateTime clientReconfirmationDate = updateReconfirmationDate(configuration.getSupplierConfiguration().getReconfirmationCutOff(), customFlightDetails.getTravelDate(), aBooking.getBookingDateZDT(), hours);

                                ClientReconfirmationDetails clientReconfirmationDetails = null;
                                clientReconfirmationDetails = this.clientReconfirmationService.findByBookRefAndOrderNo(aBooking.getBookID(), aProduct.getOrderID());
                                if (clientReconfirmationDetails == null) {
                                    clientReconfirmationDetails = new ClientReconfirmationDetails();
                                }
                                clientReconfirmationDetails = reconfirmFlightsClientService.reconfirmClientForFlights(aBooking, aProduct, clientReconfirmationDetails);
                                CustomFlightDetails customFlightDetailsForClient = reconfirmFlightsClientService.getCustomFlightDetails(flightDetails);
                                clientReconfirmationDetails.setReconfirmationCutOffDate(clientReconfirmationDate);
                                this.clientReconfirmationService.saveOrUpdateClientReconfirmation(clientReconfirmationDetails);
                                clientReconfirmationService.updateClientReconfirmationDateInBookingEngine(clientReconfirmationDetails);
                                // this.processReconfirmation( configuration , aProduct , aBooking , reconfirmationRequest , null , clientReconfirmationDetails );
                                break;

                            }
                            case CLIENT_AND_SUPPLIER: {
                                SupplierReconfirmationDetails supplierReconfirmation = null;
                                supplierReconfirmation = this.supplierReconfirmationService.findByBookRefAndOrderNo(aBooking.getBookID(), aProduct.getOrderID());
                                if (supplierReconfirmation == null) {
                                    supplierReconfirmation = new SupplierReconfirmationDetails();
                                }
                                ClientReconfirmationDetails clientReconfirmationDetails = null;
                                clientReconfirmationDetails = this.clientReconfirmationService.findByBookRefAndOrderNo(aBooking.getBookID(), aProduct.getOrderID());
                                if (clientReconfirmationDetails == null) {
                                    clientReconfirmationDetails = new ClientReconfirmationDetails();
                                }

                                clientReconfirmationDetails = reconfirmFlightsClientService.reconfirmClientForFlights(aBooking, aProduct, clientReconfirmationDetails);
                                supplierReconfirmation = this.reconfirmFlightsSupplierService.reconfirmSupplierForFlights(aBooking, aProduct, supplierReconfirmation);

                                //  CustomFlightDetails customFlightDetailsForClient = reconfirmFlightsClientService.getCustomFlightDetails(flightDetails);
                                long hours = reconfirmationUtilityService.convertToHours(configuration.getClientConfiguration().getDuration(), configuration.getSupplierConfiguration().getDurationType());
                                ZonedDateTime clientReconfirmationDate = updateReconfirmationDate(configuration.getClientConfiguration().getReconfirmationCutOff(), customFlightDetails.getTravelDate(), aBooking.getBookingDateZDT(), hours);
                                clientReconfirmationDetails.setReconfirmationCutOffDate(clientReconfirmationDate);
                                clientReconfirmationDetails = this.clientReconfirmationService.saveOrUpdateClientReconfirmation(clientReconfirmationDetails);
                                clientReconfirmationService.updateClientReconfirmationDateInBookingEngine(clientReconfirmationDetails);

                                supplierReconfirmation.setClientReconfirmationID(clientReconfirmationDetails.getId());
                                long hourss = reconfirmationUtilityService.convertToHours(configuration.getSupplierConfiguration().getDuration(), configuration.getSupplierConfiguration().getDurationType());
                                ZonedDateTime supplierReconfirmationDate = updateReconfirmationDate(configuration.getSupplierConfiguration().getReconfirmationCutOff(), customFlightDetails.getTravelDate(), aBooking.getBookingDateZDT(), hourss);
                                supplierReconfirmation.setReconfirmationCutOffDate(supplierReconfirmationDate);
                                this.supplierReconfirmationService.saveOrUpdateSupplierReconfirmation(supplierReconfirmation);
                                supplierReconfirmationService.updateSupplierReconfirmationDateInBookingEngine(supplierReconfirmation);
                                //  this.processReconfirmation( configuration , aProduct , aBooking , reconfirmationRequest , supplierReconfirmation , clientReconfirmationDetails );
                                break;
                            }
                        }
                    } else {
                        throw new OperationException(Constants.ER311);
                    }
                } else {
                    throw new OperationException(Constants.ER312);
                }
            case PRODUCT_CATEGORY_ACCOMMODATION:
                if (aProduct.getProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {
                    OpsOrderDetails orderDetails = aProduct.getOrderDetails();
                    OpsOrderStatus opsOrderStatus = orderDetails.getOpsOrderStatus();
                    OpsHotelDetails hotelDetails = aProduct.getOrderDetails().getHotelDetails();
                    List<OpsRoom> rooms = aProduct.getOrderDetails().getHotelDetails().getRooms();

                    if (opsOrderStatus.getProductStatus().equalsIgnoreCase(OpsProductStatus.OK.getProductStatus())) {
                        SupplierReconfirmationFilter filter = new SupplierReconfirmationFilter();
                        //  filter.setSupplierId( aProduct.getSupplierID( ) != null ? aProduct.getSupplierID( ) : null );
                        filter.setProductCategory(aProduct.getProductCategory() != null ? aProduct.getProductCategory() : null);
                        filter.setProductCatSubtype(aProduct.getProductSubCategory() != null ? aProduct.getProductSubCategory() : null);

                        ReconfirmationConfiguration configuration = reconfirmationMDMService.getConfiguration(filter);
                        if (configuration == null) {

                            configuration = new ReconfirmationConfiguration();
                            configuration.setConfigFor(ReconfirmationConfigFor.CLIENT_AND_SUPPLIER);
                            // throw new OperationException(Constants.ER311);
                        }
                        if (configuration.getConfigFor() == null) {
                            configuration = new ReconfirmationConfiguration();
                            configuration.setConfigFor(ReconfirmationConfigFor.CLIENT_AND_SUPPLIER);
                        }
                        reconfirmationRequest.setBookingDate(aBooking.getBookingDateZDT());
                        reconfirmationRequest.setBookingRefNumber(aBooking.getBookID());
                        reconfirmationRequest.setOrderID(aProduct.getOrderID());
                        switch (configuration.getConfigFor()) {
                            case SUPPLIER: {

                                SupplierReconfirmationDetails supplierReconfirmation = null;
                                supplierReconfirmation = this.supplierReconfirmationService.findByBookRefAndOrderNo(aBooking.getBookID(), aProduct.getOrderID());
                                if (supplierReconfirmation == null) {
                                    supplierReconfirmation = new SupplierReconfirmationDetails();
                                }
                                supplierReconfirmation.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_PENDING);
                                if (configuration != null) {
                                    supplierReconfirmation.setReconfirmedBy(configuration.getConfigFor().getValue());
                                }

                                supplierReconfirmation = reconfirmHotelsSupplierService.getCustomHotelDetails(rooms, hotelDetails, supplierReconfirmation);
                                supplierReconfirmation = this.reconfirmHotelsSupplierService.reconfirmSupplierForHotels(aBooking, aProduct, supplierReconfirmation, null);
                                long hours = reconfirmationUtilityService.convertToHours(configuration.getSupplierConfiguration().getDuration(), configuration.getSupplierConfiguration().getDurationType());

                                ZonedDateTime supplierReconfirmationDate = updateReconfirmationDate(configuration.getSupplierConfiguration().getReconfirmationCutOff(), supplierReconfirmation.getTravelDate(), aBooking.getBookingDateZDT(), hours);
                                supplierReconfirmation.setSupplierReconfirmationDate(supplierReconfirmationDate);
                                this.supplierReconfirmationService.saveOrUpdateSupplierReconfirmation(supplierReconfirmation);
                                supplierReconfirmationService.updateSupplierReconfirmationDateInBookingEngine(supplierReconfirmation);
                                break;
                            }
                            case CLIENT: {
                                ClientReconfirmationDetails clientReconfirmationDetails = null;
                                clientReconfirmationDetails = this.clientReconfirmationService.findByBookRefAndOrderNo(aBooking.getBookID(), aProduct.getOrderID());
                                if (clientReconfirmationDetails == null) {
                                    clientReconfirmationDetails = new ClientReconfirmationDetails();
                                }
                                clientReconfirmationDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_PENDING);

                                clientReconfirmationDetails = this.reconfirmHotelsClientService.getCustomHotelDetails(rooms, hotelDetails, clientReconfirmationDetails);
                                long hours = reconfirmationUtilityService.convertToHours(configuration.getSupplierConfiguration().getDuration(), configuration.getSupplierConfiguration().getDurationType());

                                ZonedDateTime clientReconfirmationDate = updateReconfirmationDate(configuration.getClientConfiguration().getReconfirmationCutOff(), clientReconfirmationDetails.getTravelDate(), aBooking.getBookingDateZDT(), hours);
                                clientReconfirmationDetails.setClientOrCustomerReconfirmationDate(clientReconfirmationDate);

                                clientReconfirmationDetails = this.reconfirmHotelsClientService.reconfirmClientForHotels(aBooking, aProduct, hotelDetails, clientReconfirmationDetails);
                                this.clientReconfirmationService.saveOrUpdateClientReconfirmation(clientReconfirmationDetails);
                                clientReconfirmationService.updateClientReconfirmationDateInBookingEngine(clientReconfirmationDetails);
                                break;
                            }
                            case CLIENT_AND_SUPPLIER: {
                                SupplierReconfirmationDetails supplierReconfirmation = null;
                                supplierReconfirmation = this.supplierReconfirmationService.findByBookRefAndOrderNo(aBooking.getBookID(), aProduct.getOrderID());
                                if (supplierReconfirmation == null) {
                                    supplierReconfirmation = new SupplierReconfirmationDetails();
                                }
                                ClientReconfirmationDetails clientReconfirmationDetails = null;
                                clientReconfirmationDetails = this.clientReconfirmationService.findByBookRefAndOrderNo(aBooking.getBookID(), aProduct.getOrderID());
                                if (clientReconfirmationDetails == null) {
                                    clientReconfirmationDetails = new ClientReconfirmationDetails();
                                }
                                clientReconfirmationDetails = this.reconfirmHotelsClientService.reconfirmClientForHotels(aBooking, aProduct, hotelDetails, clientReconfirmationDetails);
                                supplierReconfirmation = this.reconfirmHotelsSupplierService.reconfirmSupplierForHotels(aBooking, aProduct, supplierReconfirmation, null);
                                clientReconfirmationDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_PENDING);

                                clientReconfirmationDetails = this.reconfirmHotelsClientService.getCustomHotelDetails(rooms, hotelDetails, clientReconfirmationDetails);
                                long hours = reconfirmationUtilityService.convertToHours(configuration.getSupplierConfiguration().getDuration(), configuration.getSupplierConfiguration().getDurationType());

                                ZonedDateTime clientReconfirmationDate = updateReconfirmationDate(configuration.getSupplierConfiguration().getReconfirmationCutOff(), clientReconfirmationDetails.getTravelDate(), aBooking.getBookingDateZDT(), hours);
                                clientReconfirmationDetails.setClientOrCustomerReconfirmationDate(clientReconfirmationDate);
                                ClientReconfirmationDetails details = this.clientReconfirmationService.saveOrUpdateClientReconfirmation(clientReconfirmationDetails);
                                clientReconfirmationService.updateClientReconfirmationDateInBookingEngine(clientReconfirmationDetails);

                                supplierReconfirmation.setClientReconfirmationID(details.getId());
                                supplierReconfirmation.setReconfirmedBy(configuration.getConfigFor().getValue());
                                supplierReconfirmation.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_PENDING);

                                supplierReconfirmation = reconfirmHotelsSupplierService.getCustomHotelDetails(rooms, hotelDetails, supplierReconfirmation);
                                long hourss = reconfirmationUtilityService.convertToHours(configuration.getSupplierConfiguration().getDuration(), configuration.getSupplierConfiguration().getDurationType());

                                ZonedDateTime supplierReconfirmationDate = updateReconfirmationDate(configuration.getClientConfiguration().getReconfirmationCutOff(), supplierReconfirmation.getTravelDate(), aBooking.getBookingDateZDT(), hourss);
                                supplierReconfirmation.setSupplierReconfirmationDate(supplierReconfirmationDate);
                                this.supplierReconfirmationService.saveOrUpdateSupplierReconfirmation(supplierReconfirmation);
                                supplierReconfirmationService.updateSupplierReconfirmationDateInBookingEngine(supplierReconfirmation);
                                break;
                            }
                        }

                    } else {
                        //Product is not in a confirmed state
                        logger.error("Product is not in a confirmed state");
                        throw new OperationException(Constants.ER311);
                    }
                } else {
                    throw new OperationException(Constants.ER312);
                }
            default: {
                break;
            }

        }
    }

    /**
     * @param clientReconfirmationResource
     * @return
     */
    @Override
    public ClientReconfirmationDetails saveClientReconfirmation(ClientReconfirmationResource clientReconfirmationResource) {

        ClientReconfirmationDetails clientReconfirmationDetails = new ClientReconfirmationDetails();
        clientReconfirmationDetails.setBookingAttribute(clientReconfirmationResource.getBookingAttribute());
        clientReconfirmationDetails.setClientOrCustomerReconfirmationDate(clientReconfirmationResource.getClientOrCustomerReconfirmationDate());
        clientReconfirmationDetails.setRemarks(clientReconfirmationResource.getRemarks());
        clientReconfirmationDetails.setRejectedDueToNoResponse(clientReconfirmationResource.isRejectedDueToNoResponse());
        clientReconfirmationDetails.setReconfirmationOnHoldUntilDate(clientReconfirmationResource.getReconfirmationOnHoldUntilDate());

        return this.saveOrUpdateCCReconfirmation(clientReconfirmationDetails);
    }

    /**
     * @param clientReconfirmationDetails
     * @return
     */
    @Override
    public ClientReconfirmationResponse convertTo(ClientReconfirmationDetails clientReconfirmationDetails) {
        if (clientReconfirmationDetails.getBookingAttribute().equals(ReconfirmationBookingAttribute.RECONFIRMATION_ON_HOLD)) {
            //alert the operations user
            createAlertForClientReconfirmation(clientReconfirmationDetails);
        }
        ClientReconfirmationResponse response = new ClientReconfirmationResponse();
        if (clientReconfirmationDetails.getBookingAttribute() != null)
            response.setBookingAttribute(clientReconfirmationDetails.getBookingAttribute());

        if (clientReconfirmationDetails.getClientOrCustomerReconfirmationDate() != null)
            response.setClientOrCustomerReconfirmationDate(clientReconfirmationDetails.getClientOrCustomerReconfirmationDate().toOffsetDateTime().toString());

        if (clientReconfirmationDetails.getProductName() != null)
            response.setProductName(clientReconfirmationDetails.getProductName());

        if (clientReconfirmationDetails.getReconfirmationOnHoldUntilDate() != null)
            response.setReconfirmationOnHoldUntilDate(clientReconfirmationDetails.getReconfirmationOnHoldUntilDate().toOffsetDateTime().toString());

        if (clientReconfirmationDetails.getRemarks() != null)
            response.setRemarks(clientReconfirmationDetails.getRemarks());

        if (clientReconfirmationDetails.getOrderID() != null)
            response.setOrderID(clientReconfirmationDetails.getOrderID());

        if (clientReconfirmationDetails.getBookRefNo() != null)
            response.setBookingRefNo(clientReconfirmationDetails.getBookRefNo());

        if (clientReconfirmationDetails.getId() != null)
            response.setClientReconfirmationID(clientReconfirmationDetails.getId());

        response.setRejectedDueToNoResponse(clientReconfirmationDetails.isRejectedDueToNoResponse());

        if (clientReconfirmationDetails.getCity() != null)
            response.setCity(clientReconfirmationDetails.getCity());

        if (clientReconfirmationDetails.getCountry() != null)
            response.setCountry(clientReconfirmationDetails.getCountry());

        return response;
    }

    private void createAlertForClientReconfirmation(ClientReconfirmationDetails clientReconfirmationDetails) {
        NotificationResource notificationResource = null;
        try {
            OpsBooking opsBooking = opsBookingService.getBooking(clientReconfirmationDetails.getBookRefNo());
            notificationResource = alertService.createAlert(businessProcess, function, opsBooking.getCompanyId(), onHoldAlertName, userService.getLoggedInUserId(), "Reconfirmation is hold until :" + clientReconfirmationDetails.getReconfirmationOnHoldUntilDate() + " for product: " + clientReconfirmationDetails.getProductName());
        } catch (OperationException e) {
            logger.error("Error while sending alerts to whome you are assigned" + e);
        }
    }

    private void createAlertForSupplierReconfirmation(SupplierReconfirmationDetails supplierReconfirmationDetails) {
        NotificationResource notificationResource = null;
        try {
            OpsBooking opsBooking = opsBookingService.getBooking(supplierReconfirmationDetails.getBookRefNo());
            notificationResource = alertService.createAlert(businessProcess, function, opsBooking.getCompanyId(), onHoldAlertName, userService.getLoggedInUserId(), "Reconfirmation is hold until :" + supplierReconfirmationDetails.getReconfirmationOnHoldUntilDate() + " for product: " + supplierReconfirmationDetails.getProductName());
        } catch (OperationException e) {
            logger.error("Error while sending alerts to whome you are assigned" + e);
        }
    }

    /**
     * @param allClientReconfirmation
     * @return
     */
    @Override
    public List<ClientReconfirmationResponse> convertTo(List<ClientReconfirmationDetails> allClientReconfirmation) {
        List<ClientReconfirmationResponse> clientReconfirmationResponses = new ArrayList<>();
        for (ClientReconfirmationDetails details : allClientReconfirmation) {
            ClientReconfirmationResponse reconfirmationResponse = new ClientReconfirmationResponse();

            reconfirmationResponse.setRejectedDueToNoResponse(details.isRejectedDueToNoResponse());
            if (details.getBookingAttribute() != null)
                reconfirmationResponse.setBookingAttribute(details.getBookingAttribute());

            if (details.getBookRefNo() != null)
                reconfirmationResponse.setBookingRefNo(details.getBookRefNo());

            if (details.getCity() != null)
                reconfirmationResponse.setCity(details.getCity());

            if (details.getClientOrCustomerReconfirmationDate() != null)
                reconfirmationResponse.setClientOrCustomerReconfirmationDate(details.getClientOrCustomerReconfirmationDate().toOffsetDateTime().toString());

            if (details.getId() != null)
                reconfirmationResponse.setClientReconfirmationID(details.getId());

            if (details.getCountry() != null)
                reconfirmationResponse.setCountry(details.getCountry());

            if (details.getOrderID() != null)
                reconfirmationResponse.setOrderID(details.getOrderID());

            if (details.getProductName() != null)
                reconfirmationResponse.setProductName(details.getProductName());

            if (details.getReconfirmationCutOffDate() != null)
                reconfirmationResponse.setReconfirmationOnHoldUntilDate(details.getReconfirmationCutOffDate().toOffsetDateTime().toString());

            if (details.getRemarks() != null)
                reconfirmationResponse.setRemarks(details.getRemarks());

            clientReconfirmationResponses.add(reconfirmationResponse);
        }
        return clientReconfirmationResponses;
    }

    /**
     * @return
     */
    @Override
    public List<ClientReconfirmationDetails> getAllCCReconfirmation() {
        return this.clientReconfirmationRepository.getAllClientReconfirmation();
    }

    /**
     * @param reconfConfig
     * @param request
     * @return
     * @throws OperationException
     */
    @Override
    public ReconfirmationRequest client(ReconfirmationConfiguration reconfConfig, ReconfirmationRequest request) throws OperationException {
        ReconfirmationRequest reconfReq = new ReconfirmationRequest();
        String reconfirmationCutOff = reconfConfig.getClientConfiguration().getReconfirmationCutOff();
        long hours = reconfirmationUtilityService.convertToHours(reconfConfig.getClientConfiguration().getDuration(), reconfConfig.getClientConfiguration().getDurationType());
        reconfReq.setReconfirmationConfigFor(CLIENT);
        reconfReq.setReconfirmationConfigurationDefined(true);
        reconfReq.setReconfirmationToBeSent(reconfConfig.getClientConfiguration().getReconfirmationToBeSentTo());
        reconfReq.setReconfirmationCutOffType(this.getReconfirmationCutOffType(reconfirmationCutOff));
        reconfReq.setEnableCancellationBooking(false);
        ZonedDateTime reconfirmationCutOffDate = this.getReconfirmationCutOffDate(reconfirmationCutOff, request.getTravelDate(), request.getBookingDate(), hours);
        boolean reconfirmationCutOffReached = this.isReconfirmationCutOffReached(reconfirmationCutOffDate);
        reconfReq.setCutOffReached(reconfirmationCutOffReached);
        reconfReq.setCutOffReached(true);
        boolean reconfirmationCutOffPassed = this.isReconfirmationCutOffPassed(reconfirmationCutOffDate);
        reconfReq.setCutOffPassed(reconfirmationCutOffPassed);
        reconfReq.setClientReconfirmationDate(reconfirmationCutOffDate);
        reconfReq.setNumberOfDaysOrHours(hours);
        return reconfReq;
    }

    /**
     * @param reconfConfig
     * @param request
     * @return
     * @throws OperationException
     */
    @Override
    public ReconfirmationRequest supplier(ReconfirmationConfiguration reconfConfig, ReconfirmationRequest request) throws OperationException {
        ReconfirmationRequest reconfReq = new ReconfirmationRequest();

        String reconfirmationCutOff = null;
        if (reconfConfig.getSupplierConfiguration() != null) {
            reconfirmationCutOff = reconfConfig.getSupplierConfiguration().getReconfirmationCutOff();
            long hours = reconfirmationUtilityService.convertToHours(reconfConfig.getSupplierConfiguration().getDuration(), reconfConfig.getSupplierConfiguration().getDurationType());
            reconfReq.setReconfirmationConfigFor(SUPPLIER);
            reconfReq.setReconfirmationConfigurationDefined(true);
            reconfReq.setReconfirmationToBeSent(reconfConfig.getSupplierConfiguration().getReconfirmationToBeSentTo());
            reconfReq.setEnableCancellationBooking(false);
            reconfReq.setReconfirmationCutOffType(this.getReconfirmationCutOffType(reconfirmationCutOff));
            ZonedDateTime reconfirmationCutOffDate = this.getReconfirmationCutOffDate(reconfirmationCutOff, request.getTravelDate(), request.getBookingDate(), hours);
            reconfReq.setReconfirmationCutOffDate(reconfirmationCutOffDate);
            boolean reconfirmationCutOffReached = this.isReconfirmationCutOffReached(reconfirmationCutOffDate);
            // reconfReq.setCutOffReached( reconfirmationCutOffReached );
            reconfReq.setCutOffReached(true);
            boolean reconfirmationCutOffPassed = this.isReconfirmationCutOffPassed(reconfirmationCutOffDate);
            reconfReq.setCutOffPassed(reconfirmationCutOffPassed);
        }
        return reconfReq;
    }

    /**
     * @param status
     * @param bookingRefId
     * @param productId
     * @return
     * @throws OperationException
     */
    @Override
    public boolean updateFlag(SupplierReconfirmationStatus status, String bookingRefId, String productId) throws OperationException {
        try {
            SupplierReconfirmationStatusUpdateRequest request = new SupplierReconfirmationStatusUpdateRequest();
            request.setSuppReconfirmStatus(status.getValue());
            request.setOrderID(productId);
            try {
                if (this.userService.getLoggedInUserId() != null) {
                    request.setUserID(this.userService.getLoggedInUserId());
                } else {
                    request.setUserID("NA");
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setUserID("NA");
            }
            URI uri = UriComponentsBuilder.fromUriString(updateSupplierReconfirmationStatusInBE).build().encode().toUri();
            ResponseEntity<String> exchange = mdmRestUtils.exchange(uri, HttpMethod.PUT, request, String.class);
            if (exchange != null && exchange.getBody() != null) {
                exchange.getBody();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param status
     * @param bookingRefId
     * @param productId
     * @param reconfirmationOnHoldDate
     * @return
     * @throws OperationException
     */
    @Override
    public boolean updateFlag(SupplierReconfirmationStatus status, String bookingRefId, String productId, ZonedDateTime reconfirmationOnHoldDate) throws OperationException {
        try {
            SupplierReconfirmationStatusUpdateRequest request = new SupplierReconfirmationStatusUpdateRequest();
            request.setSuppReconfirmStatus(status.getValue());
            request.setOrderID(productId);
            try {
                if (this.userService.getLoggedInUserId() != null) {
                    request.setUserID(this.userService.getLoggedInUserId());
                } else {
                    request.setUserID("NA");
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setUserID("NA");
            }
            URI uri = UriComponentsBuilder.fromUriString(updateSupplierReconfirmationStatusInBE).build().encode().toUri();
            ResponseEntity<String> exchange = mdmRestUtils.exchange(uri, HttpMethod.PUT, request, String.class);
            if (exchange != null && exchange.getBody() != null) {
                exchange.getBody();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param bookingId
     * @return
     * @throws OperationException
     */
    @Override
    public OpsBooking getBookingById(String bookingId) throws OperationException {
        OpsBooking bookingDetails = null;
        try {
            bookingDetails = this.opsBookingService.getBooking(bookingId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException(Constants.ER305);
        }
        if (bookingDetails == null) {
            throw new OperationException(Constants.ER318);
        }
        return bookingDetails;
    }

    /**
     * @param bookingDetails
     * @param orderID
     * @return
     * @throws OperationException
     */
    @Override
    public OpsProduct getProductById(OpsBooking bookingDetails, final String orderID) throws OperationException {
        OpsProduct productDetails = null;
        try {
            productDetails = bookingDetails.getProducts().stream().filter(aProduct -> aProduct.getOrderID().equalsIgnoreCase(orderID)).findFirst().get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException(Constants.ER317);
        }
        if (productDetails == null) {
            throw new OperationException(Constants.ER317);
        }
        return productDetails;
    }

}

