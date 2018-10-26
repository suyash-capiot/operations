package com.coxandkings.travel.operations.service.productbookedthrother.impl;

import com.coxandkings.travel.operations.criteria.productbookedthrother.ProductBookedThrOtherCriteria;
import com.coxandkings.travel.operations.enums.common.MDMClientType;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.productbookedthrother.*;
import com.coxandkings.travel.operations.repository.productbookedthrother.AccomodationRepository;
import com.coxandkings.travel.operations.repository.productbookedthrother.BusRepository;
import com.coxandkings.travel.operations.repository.productbookedthrother.FlightRepository;
import com.coxandkings.travel.operations.repository.productbookedthrother.TrainRepository;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.productbookedthrother.ProductBookedThrOtherResource;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.service.productbookedthrother.ProductBookedThrOtherService;
import com.coxandkings.travel.operations.service.productbookedthrother.mdm.MdmClientService;
import com.coxandkings.travel.operations.utils.*;
import com.coxandkings.travel.operations.utils.email.EmailResourceService;
import com.coxandkings.travel.operations.utils.supplier.CommunicationType;
import com.coxandkings.travel.operations.utils.supplier.SupplierDetailsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service(value = "ProductBookedThrOtherService")
public class ProductBookedThrOtherServiceImpl implements ProductBookedThrOtherService {

    private static final Logger logger = LogManager.getLogger(ProductBookedThrOtherServiceImpl.class);

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private AccomodationRepository accomodationRepository;

    @Autowired
    private SupplierDetailsService supplierDetailsService;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private MdmClientService mdmClientService;

    @Autowired
    private EmailResourceService emailResourceService;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private ClientMasterDataService clientMasterDataService;


    //Comman Attribute
    @Value("${product_booked_thr_other_service.template_config.function}")
    private String function;

    @Value("${product_booked_thr_other_service.template_config.prod_name}")
    private String productNameValue;

    //Supplier
    @Value("${product_booked_thr_other_service.template_config.supplier.scenario}")
    private String supplierScenario;

    @Value("${product_booked_thr_other_service.template_config.supplier.subject}")
    private String supplierSubject;

    @Value("${product_booked_thr_other_service.dynamic_variables.supplier.product_name}")
    private String productNameForSupplier;

    @Value("${product_booked_thr_other_service.dynamic_variables.supplier.product_code}")
    private String productCodeForSupplier;

    @Value("${product_booked_thr_other_service.dynamic_variables.supplier.product_condition}")
    private String productConditionForSupplier;


    //Client
    @Value("${product_booked_thr_other_service.template_config.client.scenario}")
    private String clientScenario;

    @Value("${product_booked_thr_other_service.template_config.client.subject}")
    private String clientSubject;

    @Value("${product_booked_thr_other_service.dynamic_variables.client.product_name}")
    private String productNameForClient;

    @Value("${product_booked_thr_other_service.dynamic_variables.client.first_name}")
    private String firstNameOfClient;

    @Value("${product_booked_thr_other_service.dynamic_variables.client.product_code}")
    private String productCodeForClient;

    //URLs
    @Value("${product_booked_thr_other_service.links.flight}")
    private String flightUrl;

    @Value("${product_booked_thr_other_service.links.hotel}")
    private String hotelUrl;

    @Value("${product_booked_thr_other_service.links.rail}")
    private String railUrl;

    @Value("${product_booked_thr_other_service.links.bus}")
    private String busUrl;



    @Override
    public ProductBookedThrOtherResource getProduct(String bookingRefId, String orderId, String productCategorySubTypeValue) throws OperationException {
        OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.fromString(productCategorySubTypeValue);
        ProductBookedThrOtherCriteria criteria = new ProductBookedThrOtherCriteria();
        criteria.setProductCategorySubTypeValue(opsProductSubCategory);
        criteria.setOrderId(orderId);
        criteria.setBookingRefId(bookingRefId);

        ProductBookedThrOtherResource productBookedThrOtherResource = null;
        if (opsProductSubCategory.equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_BUS)) {
            productBookedThrOtherResource = new ProductBookedThrOtherResource();
            Attribute attribute = busRepository.getBusByCriteria(criteria);
            if (attribute == null) {
                String status = isOtherProductPresent(bookingRefId, orderId, productCategorySubTypeValue);
                throw new OperationException(status);
            }
            productBookedThrOtherResource.setProductCategorySubTypeValue(attribute.getProductCategorySubType().getSubCategory());
            CopyUtils.copy(attribute, productBookedThrOtherResource);
            return productBookedThrOtherResource;

        } else if (opsProductSubCategory.equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT)) {
            productBookedThrOtherResource = new ProductBookedThrOtherResource();
            Attribute attribute = flightRepository.getFlightByCriteria(criteria);
            if (attribute == null) {
                String status = isOtherProductPresent(bookingRefId, orderId, productCategorySubTypeValue);
                throw new OperationException(status);
            }
            productBookedThrOtherResource.setProductCategorySubTypeValue(attribute.getProductCategorySubType().getSubCategory());
            CopyUtils.copy(attribute, productBookedThrOtherResource);
            return productBookedThrOtherResource;
        } else if (opsProductSubCategory.equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_RAIL) || opsProductSubCategory.equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_INDIAN_RAIL)) {
            productBookedThrOtherResource = new ProductBookedThrOtherResource();
            Attribute attribute = trainRepository.getTrainByCriteria(criteria);
            if (attribute == null) {
                String status = isOtherProductPresent(bookingRefId, orderId, productCategorySubTypeValue);
                throw new OperationException(status);
            }
            productBookedThrOtherResource.setProductCategorySubTypeValue(attribute.getProductCategorySubType().getSubCategory());
            CopyUtils.copy(attribute, productBookedThrOtherResource);
            return productBookedThrOtherResource;
        } else {
            productBookedThrOtherResource = new ProductBookedThrOtherResource();
            Attribute attribute = accomodationRepository.getAccomodationByCriteria(criteria);
            if (attribute == null) {
                String status = isOtherProductPresent(bookingRefId, orderId, productCategorySubTypeValue);
                throw new OperationException(status);
            }
            productBookedThrOtherResource.setProductCategorySubTypeValue(attribute.getProductCategorySubType().getSubCategory());
            CopyUtils.copy(attribute, productBookedThrOtherResource);
            return productBookedThrOtherResource;
        }

    }



    @Override
    public Attribute saveProduct(ProductBookedThrOtherResource productBookedThrOtherResource) throws OperationException {
        boolean isDuplicateRecordFound = checkDuplicate(productBookedThrOtherResource.getBookingRefId(),
                productBookedThrOtherResource.getOrderId(),
                productBookedThrOtherResource.getProductCategorySubTypeValue());
        if (!isDuplicateRecordFound) {

            if (StringUtils.isEmpty(productBookedThrOtherResource.getId())) {
                if (OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()).equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_BUS)) {
                    Bus bus = new Bus();
                    bus.setProductCategorySubType(OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()));
                    CopyUtils.copy(productBookedThrOtherResource, bus);
                    bus = busRepository.saveOrUpdateBus(bus);
                    if (!StringUtils.isEmpty(bus.getId())) {
                        return bus;
                    }
                    try {
                        EmailResponse emailResponse = sendAnEmailToSupplier(productBookedThrOtherResource);
                    } catch (Exception e) {
                        logger.error("Error while sending an email");
                    }
                    return bus;

                } else if (OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()).equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT)) {
                    Flight flight = new Flight();
                    flight.setProductCategorySubType(OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()));
                    CopyUtils.copy(productBookedThrOtherResource, flight);
                    flight = flightRepository.saveOrUpdateFlight(flight);
                    try {
                        EmailResponse emailResponse = sendAnEmailToSupplier(productBookedThrOtherResource);
                    } catch (Exception e) {
                        logger.error("Error while sending an email");

                    }
                    return flight;
                } else if (OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()).equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_RAIL) ||
                        OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()).equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_INDIAN_RAIL)) {
                    Train train = new Train();
                    train.setProductCategorySubType(OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()));
                    CopyUtils.copy(productBookedThrOtherResource, train);
                    train = trainRepository.saveOrUpdateTrain(train);
                    try {
                        EmailResponse emailResponse = sendAnEmailToSupplier(productBookedThrOtherResource);
                    } catch (Exception e) {
                        logger.error("Error while sending an email");

                    }
                    return train;

                } else {
                    Accomodation accomodation = new Accomodation();
                    accomodation.setProductCategorySubType(OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()));
                    CopyUtils.copy(productBookedThrOtherResource, accomodation);
                    accomodation = accomodationRepository.saveOrUpdateAccomodation(accomodation);
                    try {
                        EmailResponse emailResponse = sendAnEmailToSupplier(productBookedThrOtherResource);
                    } catch (Exception e) {
                        logger.error("Error while sending an email");

                    }
                    return accomodation;
                }

            } else {
                throw new OperationException(Constants.ER05);
            }
        } else {
            throw new OperationException("Record is already saved for this booking id: " + productBookedThrOtherResource.getBookingRefId() + " and order id: " + productBookedThrOtherResource.getOrderId());
        }



    }


    @Override
    public Attribute updateProduct(ProductBookedThrOtherResource productBookedThrOtherResource) throws OperationException {
        if (!StringUtils.isEmpty(productBookedThrOtherResource.getId())) {
            if (OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()).equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_BUS)) {
                Bus existingBus = busRepository.getBusById(productBookedThrOtherResource.getId());
                if (existingBus == null) {
                    throw new OperationException(Constants.ER01);
                } else {
                    Bus bus = new Bus();
                    bus.setProductCategorySubType(OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()));
                    CopyUtils.copy(productBookedThrOtherResource, existingBus);
                    bus = existingBus;
                    bus = busRepository.saveOrUpdateBus(bus);
                    try {
                        EmailResponse emailResponse = sendAnEmailToSupplier(productBookedThrOtherResource);
                    } catch (Exception e) {
                        logger.error("Error while sending an email");

                    }
                    return bus;
                }

            } else if (OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()).equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT)) {
                Flight existingFlight = flightRepository.getFlightById(productBookedThrOtherResource.getId());
                if (existingFlight == null) {
                    throw new OperationException(Constants.ER01);
                } else {
                    Flight flight = new Flight();
                    flight.setProductCategorySubType(OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()));
                    CopyUtils.copy(productBookedThrOtherResource, existingFlight);
                    flight = existingFlight;
                    flight = flightRepository.saveOrUpdateFlight(flight);
                    try {
                        EmailResponse emailResponse = sendAnEmailToSupplier(productBookedThrOtherResource);
                    } catch (Exception e) {
                        logger.error("Error while sending an email");

                    }
                    return flight;
                }

            } else if (OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()).equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_RAIL) ||
                    productBookedThrOtherResource.getProductCategorySubTypeValue().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_INDIAN_RAIL)) {
                Train existingTrain = trainRepository.getTrainById(productBookedThrOtherResource.getId());
                if (existingTrain == null) {
                    throw new OperationException(Constants.ER01);
                } else {
                    Train train = new Train();
                    train.setProductCategorySubType(OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()));
                    CopyUtils.copy(productBookedThrOtherResource, existingTrain);
                    train = existingTrain;
                    train = trainRepository.saveOrUpdateTrain(train);
                    try {
                        EmailResponse emailResponse = sendAnEmailToSupplier(productBookedThrOtherResource);
                    } catch (Exception e) {
                        logger.error("Error while sending an email");

                    }
                    return train;
                }

            } else {
                Accomodation existingAccomodation = accomodationRepository.getAccomodationById(productBookedThrOtherResource.getId());
                if (existingAccomodation == null) {
                    throw new OperationException(Constants.ER01);
                } else {
                    Accomodation accomodation = new Accomodation();
                    accomodation.setProductCategorySubType(OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()));
                    CopyUtils.copy(productBookedThrOtherResource, existingAccomodation);
                    accomodation = existingAccomodation;
                    accomodation = accomodationRepository.saveOrUpdateAccomodation(accomodation);
                    try {
                        EmailResponse emailResponse = sendAnEmailToSupplier(productBookedThrOtherResource);
                    } catch (Exception e) {
                        logger.error("Error while sending an email");
                    }
                    return accomodation;
                }

            }
        }
        return null;
    }

    @Override
    public EmailResponse sendAnEmailToSupplier(ProductBookedThrOtherResource productBookedThrOtherResource) throws Exception {
        Map<String, String> dynamicVariables;
        EmailResponse emailResponse = null;
        OpsProduct opsProduct = opsBookingService.getProduct(productBookedThrOtherResource.getBookingRefId(), productBookedThrOtherResource.getOrderId());
        String supplierId = opsProduct.getSupplierID();
        CommunicationType communicationType = supplierDetailsService.getSupplierCommunicationTypeBySupplierId(supplierId);
        if (communicationType == null) {
            throw new Exception(Constants.ER01);
        } else {
            switch (communicationType) {
                case EMAIL:
                    String jsonSupplier = supplierDetailsService.getSupplierDetails(supplierId);
                    String emailId = String.valueOf((jsonObjectProvider.getChildObject(jsonSupplier, "$.contactInfo.contactDetails.email", String.class)));
                    dynamicVariables = getHtmlBody(productBookedThrOtherResource);
                    emailResponse = emailUtils.buildClientMail(function, supplierScenario, emailId, supplierSubject, dynamicVariables, null, null);
                    break;

                case FAX:
                    throw new OperationException(Constants.ER45 + opsProduct.getEnamblerSupplierName());

                case PHONE:
                    throw new OperationException(Constants.ER45 + opsProduct.getEnamblerSupplierName());
            }
        }
        return emailResponse;
    }

    @Override
    public EmailResponse sendAnEmailToClientOrCustomer(String bookingRefId, String orderId, String productCategorySubTypeValue) throws IOException, ParseException, OperationException {
        EmailResponse emailResponse = null;
        Map<String, String> dynamicVariable = new HashMap<>();
        OpsBooking opsBooking = opsBookingService.getBooking(bookingRefId);
        if (opsBooking != null) {
            String clientType = opsBooking.getClientType();
            String clientId = opsBooking.getClientID();
            if (!StringUtils.isEmpty(clientType) && !StringUtils.isEmpty(clientId)) {
                String clientEmailId = clientMasterDataService.getClientEmailId(clientId, MDMClientType.fromString(clientType));
                if ("B2B".equalsIgnoreCase(clientType)) {
                    Map<String, String> b2BClientNames = clientMasterDataService.getB2BClientNames(Collections.singletonList(clientId));
                    if (b2BClientNames != null && b2BClientNames.size() > 0) {
                        for (Map.Entry<String, String> entry : b2BClientNames.entrySet()) {
                            dynamicVariable.put(firstNameOfClient, entry.getValue());
                        }
                    }
                } else if ("B2C".equalsIgnoreCase(clientType)) {
                    Map<String, String> b2CClientNames = clientMasterDataService.getB2CClientNames(Collections.singletonList(clientId));
                    if (b2CClientNames != null && b2CClientNames.size() > 0) {
                        for (Map.Entry<String, String> entry : b2CClientNames.entrySet()) {
                            dynamicVariable.put(firstNameOfClient, entry.getValue());

                        }
                    }
                } else {
                    logger.info("Client type not specified");
                }

                OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.fromString(productCategorySubTypeValue);
                switch (opsProductSubCategory) {
                    case PRODUCT_SUB_CATEGORY_BUS: {
                        Bus bus = new Bus();
                        bus.setBookingRefId(bookingRefId);
                        bus.setOrderId(orderId);
                        bus = busRepository.saveOrUpdateBus(bus);
                        dynamicVariable.put(productCodeForSupplier, busUrl + "/" + bus.getId());
                        dynamicVariable.put(productNameForClient, productNameValue);
                        emailResponse = emailUtils.buildClientMail(function, clientScenario, clientEmailId, clientSubject, dynamicVariable, null, null);
                        if ("Success".equalsIgnoreCase(emailResponse.getStatus())) {
                            bus.setLinkSent(true);
                        } else {
                            bus.setLinkSent(false);
                        }
                        bus = busRepository.saveOrUpdateBus(bus);
                        break;

                    }
                    case PRODUCT_SUB_CATEGORY_FLIGHT: {
                        Flight flight = new Flight();
                        flight.setBookingRefId(bookingRefId);
                        flight.setOrderId(orderId);
                        flight = flightRepository.saveOrUpdateFlight(flight);
                        dynamicVariable.put(productCodeForSupplier, flightUrl + "/" + flight.getId());
                        dynamicVariable.put(productNameForClient, productNameValue);
                        emailResponse = emailUtils.buildClientMail(function, clientScenario, clientEmailId, clientSubject, dynamicVariable, null, null);
                        if ("Success".equalsIgnoreCase(emailResponse.getStatus())) {
                            flight.setLinkSent(true);
                        } else {
                            flight.setLinkSent(false);
                        }
                        flight = flightRepository.saveOrUpdateFlight(flight);
                        break;
                    }
                    case PRODUCT_SUB_CATEGORY_INDIAN_RAIL: {
                        Train train = new Train();
                        train.setBookingRefId(bookingRefId);
                        train.setOrderId(orderId);
                        train = trainRepository.saveOrUpdateTrain(train);
                        dynamicVariable.put(productCodeForSupplier, railUrl + "/" + train.getId());
                        dynamicVariable.put(productNameForClient, productNameValue);
                        emailResponse = emailUtils.buildClientMail(function, clientScenario, clientEmailId, clientSubject, dynamicVariable, null, null);
                        if ("Success".equalsIgnoreCase(emailResponse.getStatus())) {
                            train.setLinkSent(true);
                        } else {
                            train.setLinkSent(false);
                        }
                        train = trainRepository.saveOrUpdateTrain(train);
                        break;
                    }
                    case PRODUCT_SUB_CATEGORY_RAIL: {
                        Train train = new Train();
                        train.setBookingRefId(bookingRefId);
                        train.setOrderId(orderId);
                        train = trainRepository.saveOrUpdateTrain(train);
                        dynamicVariable.put(productCodeForSupplier, railUrl + "/" + train.getId());
                        dynamicVariable.put(productNameForClient, productNameValue);
                        emailResponse = emailUtils.buildClientMail(function, clientScenario, clientEmailId, clientSubject, dynamicVariable, null, null);
                        if ("Success".equalsIgnoreCase(emailResponse.getStatus())) {
                            train.setLinkSent(true);
                        } else {
                            train.setLinkSent(false);
                        }
                        train = trainRepository.saveOrUpdateTrain(train);
                        break;
                    }
                    case PRODUCT_SUB_CATEGORY_HOTELS: {

                        Accomodation accomodation = new Accomodation();
                        accomodation.setBookingRefId(bookingRefId);
                        accomodation.setOrderId(orderId);
                        accomodation = accomodationRepository.saveOrUpdateAccomodation(accomodation);
                        dynamicVariable.put(productCodeForSupplier, hotelUrl + "/" + accomodation.getId());
                        dynamicVariable.put(productNameForClient, productNameValue);
                        emailResponse = emailUtils.buildClientMail(function, clientScenario, clientEmailId, clientSubject, dynamicVariable, null, null);
                        if ("Success".equalsIgnoreCase(emailResponse.getStatus())) {
                            accomodation.setLinkSent(true);
                        } else {
                            accomodation.setLinkSent(false);
                        }
                        accomodation = accomodationRepository.saveOrUpdateAccomodation(accomodation);

                    }
                }
            }

        }
        return emailResponse;
    }

    @Override
    public Attribute updateProductByClient(ProductBookedThrOtherResource productBookedThrOtherResource) throws OperationException {
        if (!StringUtils.isEmpty(productBookedThrOtherResource.getId())) {
            if (OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()).equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_BUS)) {
                Bus existingBus = busRepository.getBusById(productBookedThrOtherResource.getId());
                if (existingBus == null) {
                    throw new OperationException(Constants.ER01);
                } else {
//                    if () {
                    Bus bus = new Bus();
                    existingBus.setProductCategorySubType(OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()));
                    String bookingRefId = existingBus.getBookingRefId();
                    String orderId = existingBus.getOrderId();
                    CopyUtils.copy(productBookedThrOtherResource, existingBus);
                    existingBus.setBookingRefId(bookingRefId);
                    existingBus.setOrderId(orderId);
                    bus = existingBus;
                    bus = busRepository.saveOrUpdateBus(bus);
                    try {
                        EmailResponse emailResponse = sendAnEmailToSupplier(productBookedThrOtherResource);
                    } catch (Exception e) {
                        logger.error("Error while sending an email");

                    }

                    return bus;
//                    }
                }

            } else if (OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()).equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT)) {
                Flight existingFlight = flightRepository.getFlightById(productBookedThrOtherResource.getId());
                if (existingFlight == null) {
                    throw new OperationException(Constants.ER01);
                } else {
                    Flight flight = new Flight();
                    existingFlight.setProductCategorySubType(OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()));
                    String bookingRefId = existingFlight.getBookingRefId();
                    String orderId = existingFlight.getOrderId();
                    CopyUtils.copy(productBookedThrOtherResource, existingFlight);
                    existingFlight.setBookingRefId(bookingRefId);
                    existingFlight.setOrderId(orderId);
                    flight = existingFlight;
                    flight = flightRepository.saveOrUpdateFlight(flight);
                    try {
                        EmailResponse emailResponse = sendAnEmailToSupplier(productBookedThrOtherResource);
                    } catch (Exception e) {
                        logger.error("Error while sending an email");

                    }
                    return flight;
                }

            } else if (OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()).equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_RAIL) ||
                    productBookedThrOtherResource.getProductCategorySubTypeValue().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_INDIAN_RAIL)) {
                Train existingTrain = trainRepository.getTrainById(productBookedThrOtherResource.getId());
                if (existingTrain == null) {
                    throw new OperationException(Constants.ER01);
                } else {
                    Train train = new Train();
                    existingTrain.setProductCategorySubType(OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()));
                    String bookingRefId = existingTrain.getBookingRefId();
                    String orderId = existingTrain.getOrderId();
                    CopyUtils.copy(productBookedThrOtherResource, existingTrain);
                    existingTrain.setBookingRefId(bookingRefId);
                    existingTrain.setOrderId(orderId);
                    train = existingTrain;
                    train = trainRepository.saveOrUpdateTrain(train);
                    try {
                        EmailResponse emailResponse = sendAnEmailToSupplier(productBookedThrOtherResource);
                    } catch (Exception e) {
                        logger.error("Error while sending an email");

                    }
                    return train;
                }

            } else {
                Accomodation existingAccomodation = accomodationRepository.getAccomodationById(productBookedThrOtherResource.getId());
                if (existingAccomodation == null) {
                    throw new OperationException(Constants.ER01);
                } else {
                    Accomodation accomodation = new Accomodation();
                    existingAccomodation.setProductCategorySubType(OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()));
                    String bookingRefId = existingAccomodation.getBookingRefId();
                    String orderId = existingAccomodation.getOrderId();
                    CopyUtils.copy(productBookedThrOtherResource, existingAccomodation);
                    existingAccomodation.setBookingRefId(bookingRefId);
                    existingAccomodation.setOrderId(orderId);
                    accomodation = existingAccomodation;
                    accomodation = accomodationRepository.saveOrUpdateAccomodation(accomodation);
                    try {
                        EmailResponse emailResponse = sendAnEmailToSupplier(productBookedThrOtherResource);
                    } catch (Exception e) {
                        logger.error("Error while sending an email");
                    }
                    return accomodation;
                }

            }
        }
        return null;
    }

    @Override
    public boolean isDetailSent(String productCategorySubTypeValue, String id) {
        boolean flag = false;
        OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.fromString(productCategorySubTypeValue);
        switch (opsProductSubCategory) {
            case PRODUCT_SUB_CATEGORY_FLIGHT: {
                Flight flight = flightRepository.getFlightById(id);
                if (flight != null) {
                    if (flight.getArrivalDateTime() != null)
                        flag = true;
                    else
                        flag = false;
                } else
                    flag = false;
                break;
            }
            case PRODUCT_SUB_CATEGORY_HOTELS: {
                Accomodation accomodation = accomodationRepository.getAccomodationById(id);
                if (accomodation != null) {
                    if (!StringUtils.isEmpty(accomodation.getDepartureDateTime())) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                } else
                    flag = false;
                break;
            }
            case PRODUCT_SUB_CATEGORY_BUS: {
                Bus bus = busRepository.getBusById(id);
                if (bus != null) {
                    if (!StringUtils.isEmpty(bus.getArrivalDateTime())) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                } else
                    flag = false;
                break;
            }

            case PRODUCT_SUB_CATEGORY_INDIAN_RAIL:
                flag = railObjectPresent(id);
                break;
            case PRODUCT_SUB_CATEGORY_RAIL:
                flag = railObjectPresent(id);
                break;
        }
        return flag;
    }

    private Map<String, String> getHtmlBody(ProductBookedThrOtherResource productBookedThrOtherResource) throws OperationException {

        Map<String, String> dynamicVariables = new HashMap<>();
        String header = new String();
        if ((OpsProductSubCategory.PRODUCT_SUB_CATEGORY_BUS).equals(OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()))) {

            header = "<table style= \"border: 1px solid black;border-collapse: collapse;\">\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Booking Reference Number</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getBookingRefId() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Order Id</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getOrderId() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Product Type</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getProductCategorySubTypeValue() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Departure Date</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(productBookedThrOtherResource.getDepartureDateTime()) + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Departure Time</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + DateTimeFormatter.ofPattern("hh:mm a").format(productBookedThrOtherResource.getDepartureDateTime()) + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">From City</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getFromCity() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">To City</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getToCity() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Arrival Date</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(productBookedThrOtherResource.getArrivalDateTime()) + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Arrival Time</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + DateTimeFormatter.ofPattern("hh:mm a").format(productBookedThrOtherResource.getArrivalDateTime()) + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Service Provider</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getServiceProvider() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Pickup Point Name</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getPickupPointName() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Drop Off Point Name\n" +
                    "</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getDropoffPointName() + "</td>\n" +
                    "</tr>\n" +
                    "</table>";


            dynamicVariables.put(productNameForSupplier, productNameValue);
            dynamicVariables.put(productCodeForSupplier, productBookedThrOtherResource.getProductCategorySubTypeValue());
            dynamicVariables.put(productConditionForSupplier, header);
            return dynamicVariables;
        } else if ((OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT).equals(OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()))) {

            header = "<table style= \"border: 1px solid black;border-collapse: collapse;\">\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Booking Reference Number</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getBookingRefId() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Order Id</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getOrderId() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Product Type</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getProductCategorySubTypeValue() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Departure Date</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(productBookedThrOtherResource.getDepartureDateTime()) + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Departure Time</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + DateTimeFormatter.ofPattern("hh:mm a").format(productBookedThrOtherResource.getDepartureDateTime()) + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">From City</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getFromCity() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">To City</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getToCity() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Arrival Date</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(productBookedThrOtherResource.getArrivalDateTime()) + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Arrival Time</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + DateTimeFormatter.ofPattern("hh:mm a").format(productBookedThrOtherResource.getArrivalDateTime()) + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Airline Name</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getAirlineName() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Flight No</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getFlightNo() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Terminal No\n" +
                    "</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getTerminalNo() + "</td>\n" +
                    "</tr>\n" +
                    "</table>";


            dynamicVariables.put(productNameForSupplier, productNameValue);
            dynamicVariables.put(productCodeForSupplier, productBookedThrOtherResource.getProductCategorySubTypeValue());
            dynamicVariables.put(productConditionForSupplier, header);
            return dynamicVariables;
        } else if (((OpsProductSubCategory.PRODUCT_SUB_CATEGORY_RAIL).equals(OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue()))) ||
                ((OpsProductSubCategory.PRODUCT_SUB_CATEGORY_INDIAN_RAIL).equals(OpsProductSubCategory.fromString(productBookedThrOtherResource.getProductCategorySubTypeValue())))) {

            header = "<table style= \"border: 1px solid black;border-collapse: collapse;\">\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Booking Reference Number</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getBookingRefId() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Order Id</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getOrderId() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Product Type</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getProductCategorySubTypeValue() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Departure Date</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(productBookedThrOtherResource.getDepartureDateTime()) + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Departure Time</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + DateTimeFormatter.ofPattern("hh:mm a").format(productBookedThrOtherResource.getDepartureDateTime()) + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">From City</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getFromCity() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">To City</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getToCity() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Arrival Date</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(productBookedThrOtherResource.getArrivalDateTime()) + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Arrival Time</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + DateTimeFormatter.ofPattern("hh:mm a").format(productBookedThrOtherResource.getArrivalDateTime()) + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Train Name</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getTrainName() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Train No</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getTrainNo() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Plat Form No\n" +
                    "</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getPlatformNo() + "</td>\n" +
                    "</tr>\n" +
                    "</table>";


            dynamicVariables.put(productNameForSupplier, productNameValue);
            dynamicVariables.put(productCodeForSupplier, productBookedThrOtherResource.getProductCategorySubTypeValue());
            dynamicVariables.put(productConditionForSupplier, header);
            return dynamicVariables;
        } else {

            header = "<table style= \"border: 1px solid black;border-collapse: collapse;\">\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Booking Reference Number</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getBookingRefId() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Order Id</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getOrderId() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Product Type</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getProductCategorySubTypeValue() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Departure Date</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(productBookedThrOtherResource.getDepartureDateTime()) + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Departure Time</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + DateTimeFormatter.ofPattern("hh:mm a").format(productBookedThrOtherResource.getDepartureDateTime()) + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">From City</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getFromCity() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">To City</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getToCity() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Arrival Date</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(productBookedThrOtherResource.getArrivalDateTime()) + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Arrival Time</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + DateTimeFormatter.ofPattern("hh:mm a").format(productBookedThrOtherResource.getArrivalDateTime()) + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Accomodation Name</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getAccomodationName() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Address</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getAddress() + "</td>\n" +
                    "</tr>\n" +
                    "\n" +
                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Postal Code\n" +
                    "</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getPostalCode() + "</td>\n" +
                    "</tr>\n" +

                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Country\n" +
                    "</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getCountry() + "</td>\n" +
                    "</tr>\n" +

                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">City\n" +
                    "</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getCity() + "</td>\n" +
                    "</tr>\n" +

                    "<tr>\n" +
                    "<th style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">Location\n" +
                    "</th>\n" +
                    "<td style=\"border: 1px solid black;border-collapse: collapse;text-align:center;\">" + productBookedThrOtherResource.getLocation() + "</td>\n" +
                    "</tr>\n" +
                    "</table>";


            dynamicVariables.put(productNameForSupplier, productNameValue);
            dynamicVariables.put(productCodeForSupplier, productBookedThrOtherResource.getProductCategorySubTypeValue());
            dynamicVariables.put(productConditionForSupplier, header);
            return dynamicVariables;
        }
    }


    private String isOtherProductPresent(String bookingRefId, String orderId, String productCategorySubTypeValue)
    {
        String product = null;
        Attribute attribute = null;
        ProductBookedThrOtherCriteria criteria = new ProductBookedThrOtherCriteria();
        criteria.setBookingRefId(bookingRefId);
        criteria.setOrderId(orderId);

        if ((OpsProductSubCategory.PRODUCT_SUB_CATEGORY_BUS).equals(OpsProductSubCategory.fromString(productCategorySubTypeValue))) {
            attribute = flightRepository.getFlightByCriteria(criteria);
            if (attribute != null)
                return "For this Booking Reference id and Order id, other product is " + attribute.getProductCategorySubType().getSubCategory();
            else {
                attribute = trainRepository.getTrainByCriteria(criteria);
                if (attribute != null) {
                    return "For this Booking Reference id and Order id, other product is " + attribute.getProductCategorySubType().getSubCategory();
                } else {
                    attribute = accomodationRepository.getAccomodationByCriteria(criteria);
                    if (attribute != null)
                        return "For this Booking Reference id and Order id, other product is " + attribute.getProductCategorySubType().getSubCategory();
                    else {
                        return "No Record found for any of product ";
                    }
                }
            }
        } else if ((OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT).equals(OpsProductSubCategory.fromString(productCategorySubTypeValue))) {
            attribute = busRepository.getBusByCriteria(criteria);
            if (attribute != null)
                return "For this Booking Reference id and Order id, other product is " + attribute.getProductCategorySubType().getSubCategory();
            else {
                attribute = trainRepository.getTrainByCriteria(criteria);
                if (attribute != null) {
                    return "For this Booking Reference id and Order id, other product is " + attribute.getProductCategorySubType().getSubCategory();
                } else {
                    attribute = accomodationRepository.getAccomodationByCriteria(criteria);
                    if (attribute != null)
                        return "For this Booking Reference id and Order id, other product is " + attribute.getProductCategorySubType().getSubCategory();
                    else {
                        return "No Record found for any of product ";
                    }
                }
            }
        } else if (((OpsProductSubCategory.PRODUCT_SUB_CATEGORY_RAIL).equals(OpsProductSubCategory.fromString(productCategorySubTypeValue))) ||
                ((OpsProductSubCategory.PRODUCT_SUB_CATEGORY_INDIAN_RAIL).equals(OpsProductSubCategory.fromString(productCategorySubTypeValue)))) {
            attribute = busRepository.getBusByCriteria(criteria);
            if (attribute != null)
                return "For this Booking Reference id and Order id, other product is " + attribute.getProductCategorySubType().getSubCategory();
            else {
                attribute = trainRepository.getTrainByCriteria(criteria);
                if (attribute != null) {
                    return "For this Booking Reference id and Order id, other product is " + attribute.getProductCategorySubType().getSubCategory();
                } else {
                    attribute = accomodationRepository.getAccomodationByCriteria(criteria);
                    if (attribute != null)
                        return "For this Booking Reference id and Order id, other product is " + attribute.getProductCategorySubType().getSubCategory();
                    else {
                        return "No Record found for any of product ";
                    }
                }
            }
        } else {
            attribute = busRepository.getBusByCriteria(criteria);
            if (attribute != null)
                return "For this Booking Reference id and Order id, other product is " + attribute.getProductCategorySubType().getSubCategory();
            else {
                attribute = trainRepository.getTrainByCriteria(criteria);
                if (attribute != null) {
                    return "For this Booking Reference id and Order id, other product is " + attribute.getProductCategorySubType().getSubCategory();
                } else {
                    attribute = flightRepository.getFlightByCriteria(criteria);
                    if (attribute != null)
                        return "For this Booking Reference id and Order id, other product is " + attribute.getProductCategorySubType().getSubCategory();
                    else {
                        return "No Record found for any of product ";
                    }
                }
            }
        }

    }


    private boolean checkDuplicate(String bookingId, String orderId, String productCategorySubTypeValue) {
        Attribute attribute = null;
        boolean flag = false;
        OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.fromString(productCategorySubTypeValue);
        ProductBookedThrOtherCriteria criteria = new ProductBookedThrOtherCriteria();
        criteria.setBookingRefId(bookingId);
        criteria.setOrderId(orderId);
        criteria.setProductCategorySubTypeValue(opsProductSubCategory);

        if ((OpsProductSubCategory.PRODUCT_SUB_CATEGORY_BUS).equals(opsProductSubCategory)) {
            attribute = busRepository.getBusByCriteria(criteria);
            if (attribute != null) {
                flag = true;
            } else {
                ProductBookedThrOtherCriteria criteria1 = new ProductBookedThrOtherCriteria();
                criteria1.setBookingRefId(bookingId);
                criteria1.setOrderId(orderId);
                attribute = flightRepository.getFlightByCriteria(criteria1);
                if (attribute != null) {
                    flag = true;
                } else {
                    attribute = trainRepository.getTrainByCriteria(criteria1);
                    if (attribute != null) {
                        flag = true;
                    } else {
                        attribute = accomodationRepository.getAccomodationByCriteria(criteria1);
                        if (attribute != null) {
                            flag = true;
                        } else {
                            flag = false;

                        }
                    }
                }

            }
        } else if ((OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT).equals(opsProductSubCategory)) {
            attribute = flightRepository.getFlightByCriteria(criteria);
            if (attribute != null) {
                flag = true;
            } else {
                ProductBookedThrOtherCriteria criteria1 = new ProductBookedThrOtherCriteria();
                criteria1.setBookingRefId(bookingId);
                criteria1.setOrderId(orderId);
                attribute = busRepository.getBusByCriteria(criteria1);
                if (attribute != null) {
                    flag = true;
                } else {
                    attribute = trainRepository.getTrainByCriteria(criteria1);
                    if (attribute != null) {
                        flag = true;
                    } else {
                        attribute = accomodationRepository.getAccomodationByCriteria(criteria1);
                        if (attribute != null) {
                            flag = true;
                        } else {
                            flag = false;

                        }
                    }
                }
            }
        } else if ((OpsProductSubCategory.PRODUCT_SUB_CATEGORY_RAIL).equals(opsProductSubCategory) ||
                (OpsProductSubCategory.PRODUCT_SUB_CATEGORY_INDIAN_RAIL).equals(opsProductSubCategory)) {
            attribute = trainRepository.getTrainByCriteria(criteria);
            if (attribute != null) {
                flag = true;
            } else {
                ProductBookedThrOtherCriteria criteria1 = new ProductBookedThrOtherCriteria();
                criteria1.setBookingRefId(bookingId);
                criteria1.setOrderId(orderId);
                attribute = busRepository.getBusByCriteria(criteria1);
                if (attribute != null) {
                    flag = true;
                } else {
                    attribute = flightRepository.getFlightByCriteria(criteria1);
                    if (attribute != null) {
                        flag = true;
                    } else {
                        attribute = accomodationRepository.getAccomodationByCriteria(criteria1);
                        if (attribute != null) {
                            flag = true;
                        } else {
                            flag = false;

                        }
                    }
                }
            }

        } else {
            attribute = accomodationRepository.getAccomodationByCriteria(criteria);
            if (attribute != null) {
                flag = true;
            } else {
                ProductBookedThrOtherCriteria criteria1 = new ProductBookedThrOtherCriteria();
                criteria1.setBookingRefId(bookingId);
                criteria1.setOrderId(orderId);
                attribute = busRepository.getBusByCriteria(criteria1);
                if (attribute != null) {
                    flag = true;
                } else {
                    attribute = flightRepository.getFlightByCriteria(criteria1);
                    if (attribute != null) {
                        flag = true;
                    } else {
                        attribute = trainRepository.getTrainByCriteria(criteria1);
                        if (attribute != null) {
                            flag = true;
                        } else {
                            flag = false;

                        }
                    }
                }

            }
        }

        return flag;
    }

    private boolean railObjectPresent(String id) {
        Train train = trainRepository.getTrainById(id);
        if (train != null) {
            if (StringUtils.isEmpty(train.getArrivalDateTime())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
