package com.coxandkings.travel.operations.service.reconfirmation.client.impl;

import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductStatus;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.reconfirmation.ClientReconfirmationStatus;
import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationBookingAttribute;
import com.coxandkings.travel.operations.enums.reconfirmation.ResponseStatus;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.reconfirmation.ReconfirmationFinalResponse;
import com.coxandkings.travel.operations.model.reconfirmation.client.ClientReconfirmationDetails;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationFilter;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.repository.reconfirmation.client.ClientReconfirmationRepository;
import com.coxandkings.travel.operations.resource.outbound.be.ClientReconfirmationRequestBE;
import com.coxandkings.travel.operations.resource.outbound.be.ReconfirmationRequestResource;
import com.coxandkings.travel.operations.resource.reconfirmation.client.ClientCancelReconfirmationResource;
import com.coxandkings.travel.operations.resource.reconfirmation.client.ClientReconfirmationResource;
import com.coxandkings.travel.operations.resource.reconfirmation.client.ClientRejectionResource;
import com.coxandkings.travel.operations.resource.reconfirmation.client.ClientSendReconfirmationResource;
import com.coxandkings.travel.operations.resource.reconfirmation.response.reconfirmation.ClientReconfirmationResponse;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.ManageBookingStatusService;
import com.coxandkings.travel.operations.service.email.EmailService;
import com.coxandkings.travel.operations.service.reconfirmation.batchjob.ReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.client.ClientReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.client.ReconfirmFlightsClientService;
import com.coxandkings.travel.operations.service.reconfirmation.client.ReconfirmHotelsClientService;
import com.coxandkings.travel.operations.service.reconfirmation.common.*;
import com.coxandkings.travel.operations.service.reconfirmation.mdm.ReconfirmationMDMService;
import com.coxandkings.travel.operations.service.template.TemplateLoaderService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.DateTimeUtil;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
@Service("clientReconfirmationService")
public class ClientReconfirmationServiceImpl implements ClientReconfirmationService {

    @Autowired
    private ReconfirmationUtilityService reconfirmationUtilityService;

    @Autowired
    private ClientReconfirmationRepository clientReconfirmationRepository;

    @Autowired
    private TemplateLoaderService templateLoaderService;

    @Autowired
    private ReconfirmationService reconfirmationService;

    @Autowired
    private ReconfirmationMDMService reconfirmationMDMService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private ReconfirmHotelsClientService reconfirmHotelsClientService;

    @Autowired
    private ReconfirmFlightsClientService reconfirmFlightsClientService;

    @Autowired
    private HashGenerator hashGenerator;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ManageBookingStatusService manageBookingStatusService;

    private static Logger logger = LogManager.getLogger(ClientReconfirmationServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MDMRestUtils mdmRestUtils;


    @Value(value = "${reconfirmation.client.be.update_reconfirmation_date}")
    private String updateReconfirmationDateInBE;


    @Value(value = "${reconfirmation.client.be.update_reconfirmation_status}")
    private String updateClientReconfirmationStatusInBE;

    /**
     * @param status
     * @param bookingRefId
     * @param productId
     * @return
     * @throws OperationException
     */
    @Override
    public boolean updateStatusForClientAndCustomer(ResponseStatus status, String bookingRefId, String productId) throws OperationException {
        try {
            if (status.equals(ResponseStatus.ACCEPTED)) {
                // manageBookingStatusService.updateOrderStatus(productId, OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS, OpsOrderStatus.RCMF);
                return true;
            } else {
                return false;
            }
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
     */
    public boolean updateStatusOnBookingEngineAs(ClientReconfirmationStatus status, String bookingRefId, String productId, ZonedDateTime reconfirmationOnHoldDate) {
        try {
            if (status.equals(ClientReconfirmationStatus.RECONFIRMED_BY_CLIENT)) {
                // manageBookingStatusService.updateOrderStatus(productId, OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS, OpsOrderStatus.RCMF);
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param status
     * @param bookingRefID
     * @param orderID
     * @return
     */
    public boolean updateStatusOnBookingEngineAs(ClientReconfirmationStatus status, String bookingRefID, String orderID) {
        try {
            ClientReconfirmationStatusUpdateRequest request = new ClientReconfirmationStatusUpdateRequest();
            request.setClientReconfirmStatus(status.getReconfirmationStatus());
            request.setOrderID(orderID);
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
            URI uri = UriComponentsBuilder.fromUriString(updateClientReconfirmationStatusInBE).build().encode().toUri();
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
     * @param hash
     * @param reconfirmationOnHoldDate
     * @return
     * @throws OperationException
     */
    @Override
    public ClientReconfirmationDetails checkClientResponse(ResponseStatus status, String hash, ZonedDateTime reconfirmationOnHoldDate) throws OperationException {

        ClientReconfirmationDetails clientReconfirmationDetails = clientReconfirmationRepository.findByHash(hash);
        if (clientReconfirmationDetails == null) {
            throw new OperationException(Constants.ER348);
        }
        if (clientReconfirmationDetails.isExpiredLink() && clientReconfirmationDetails.getClientReconfirmationStatus().equals(ClientReconfirmationStatus.RECONFIRMED_BY_CLIENT)) {
            throw new OperationException(Constants.ER349);
        }
        if (clientReconfirmationDetails.isExpiredLink() && clientReconfirmationDetails.getClientReconfirmationStatus().equals(ClientReconfirmationStatus.REJECTED_BY_CLIENT)) {
            throw new OperationException(Constants.ER350);
        }
        String bookingRefId = clientReconfirmationDetails.getBookRefNo();
        String orderID = clientReconfirmationDetails.getOrderID();

        OpsBooking bookingDetails = this.reconfirmationService.getBookingById(bookingRefId);
        OpsProduct productDetails = reconfirmationService.getProductById(bookingDetails, orderID);

        switch (status) {
            case ACCEPTED: {
                boolean isUpdatedStatus = this.updateStatusOnBookingEngineAs(ClientReconfirmationStatus.RECONFIRMED_BY_CLIENT, bookingRefId, orderID);
                if (isUpdatedStatus) {
                    clientReconfirmationDetails.setClientReconfirmationStatus(ClientReconfirmationStatus.RECONFIRMED_BY_CLIENT);
                    clientReconfirmationDetails.setExpiredLink(true);
                    clientReconfirmationDetails.setClientReplyDate(ZonedDateTime.now());
                    clientReconfirmationDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMED);
                    clientReconfirmationDetails = this.saveOrUpdateClientReconfirmation(clientReconfirmationDetails);
                    return clientReconfirmationDetails;
                } else {
                    throw new OperationException(Constants.ER315);
                }
            }
            case REJECTED: {

                boolean isUpdatedStatus = this.updateStatusOnBookingEngineAs(ClientReconfirmationStatus.REJECTED_BY_CLIENT, bookingRefId, orderID);
                if (isUpdatedStatus) {
                    //  supplierReconDetails = this.sendRejectionCommunicationToClient(supplierReconDetails, bookingDetails, productDetails, supplierReconDetails.getClientEmailId());
                    clientReconfirmationDetails.setClientReconfirmationStatus(ClientReconfirmationStatus.REJECTED_BY_CLIENT);
                    clientReconfirmationDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_REJECTED);
                    clientReconfirmationDetails.setExpiredLink(true);
                    clientReconfirmationDetails.setClientReplyDate(ZonedDateTime.now());
                    clientReconfirmationDetails = this.createToDoTaskForOpsUserForAlternateOptionForClient(productDetails, bookingDetails, clientReconfirmationDetails.getId(), clientReconfirmationDetails);
                    clientReconfirmationDetails = this.createToDoTaskForOpsUserForCancellationForClient(productDetails, bookingDetails, clientReconfirmationDetails.getId(), clientReconfirmationDetails);
                    clientReconfirmationDetails = this.saveOrUpdateClientReconfirmation(clientReconfirmationDetails);
                    return clientReconfirmationDetails;
                } else {
                    throw new OperationException(Constants.ER315);
                }
            }
            case NO_RESPONSE: {

                //use reconfirmationOnHoldDate and update
                boolean isUpdatedStatus = this.updateStatusOnBookingEngineAs(ClientReconfirmationStatus.REJECTED_DUE_TO_NO_RESPONSE_FROM_CLIENT, bookingRefId, orderID);
                if (isUpdatedStatus) {
                    clientReconfirmationDetails.setClientReconfirmationStatus(ClientReconfirmationStatus.REJECTED_DUE_TO_NO_RESPONSE_FROM_CLIENT);
                    clientReconfirmationDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_CANCELLED);
                    clientReconfirmationDetails.setExpiredLink(true);
                    clientReconfirmationDetails.setClientReplyDate(ZonedDateTime.now());
                    this.reconfirmationService.reconfigurationMonitor(bookingDetails, productDetails, null, null);
                    clientReconfirmationDetails = this.saveOrUpdateClientReconfirmation(clientReconfirmationDetails);
                    return clientReconfirmationDetails;
                } else {
                    throw new OperationException(Constants.ER315);
                }
            }
            case ON_HOLD: {
                //: Enter Date until when request can be on Hold
                boolean isUpdatedStatus = this.updateStatusOnBookingEngineAs(ClientReconfirmationStatus.RECONFIRMATION_REQUEST_ON_HOLD_BY_CLIENT, bookingRefId, orderID, reconfirmationOnHoldDate);
                if (isUpdatedStatus) {
                    clientReconfirmationDetails.setClientReconfirmationStatus(ClientReconfirmationStatus.RECONFIRMATION_REQUEST_ON_HOLD_BY_CLIENT);
                    clientReconfirmationDetails.setExpiredLink(true);
                    clientReconfirmationDetails.setClientReplyDate(ZonedDateTime.now());
                    clientReconfirmationDetails.setReconfirmationOnHoldUntilDate(reconfirmationOnHoldDate);
                    clientReconfirmationDetails = this.saveOrUpdateClientReconfirmation(clientReconfirmationDetails);
                    return clientReconfirmationDetails;
                } else {
                    throw new OperationException(Constants.ER315);
                }
            }
            default: {
                throw new OperationException(Constants.ER314);
            }
        }
    }


    /**
     * @param clientReconfirmationDetails
     * @return
     */

    @Override
    public ClientReconfirmationDetails saveClientReconfirmation(ClientReconfirmationDetails clientReconfirmationDetails) {
        if (clientReconfirmationRepository != null) {
            clientReconfirmationDetails.setId(null);
            clientReconfirmationDetails.setTemplate("");
            return this.clientReconfirmationRepository.saveOrUpdateClientReconfirmation(clientReconfirmationDetails);
        }
        return null;
    }

    /**
     * @param clientReconfirmationDetails
     * @return
     */
    @Override
    public ClientReconfirmationDetails updateClientReconfirmation(ClientReconfirmationDetails clientReconfirmationDetails) {
        if (clientReconfirmationRepository != null && clientReconfirmationDetails.getId() != null) {
            return this.clientReconfirmationRepository.saveOrUpdateClientReconfirmation(clientReconfirmationDetails);
        }
        return null;
    }

    /**
     * @param clientReconfirmationDetails
     * @return
     */
    @Override
    public ClientReconfirmationDetails saveOrUpdateClientReconfirmation(ClientReconfirmationDetails clientReconfirmationDetails) {
        return this.clientReconfirmationRepository.saveOrUpdateClientReconfirmation(clientReconfirmationDetails);
    }


    /**
     * @param reconfirmationID
     * @return
     */
    @Override
    public ClientReconfirmationDetails findByReconfirmationID(String reconfirmationID) throws OperationException {
        ClientReconfirmationDetails clientReconfirmationDetails = this.clientReconfirmationRepository.findByClientReconfirmationId(reconfirmationID);
        if (clientReconfirmationDetails == null) {
            throw new OperationException(Constants.ER356);
        }

        return clientReconfirmationDetails;
    }

    /**
     * @param bookRefNo
     * @param orderNo
     * @return
     */
    @Override
    public ClientReconfirmationDetails findByBookRefAndOrderNo(String bookRefNo, String orderNo) {
        ClientReconfirmationDetails clientReconfirmationDetails = this.clientReconfirmationRepository.findByBookRefAndOrderNo(bookRefNo, orderNo);
        return clientReconfirmationDetails;
    }


    /**
     * @param request
     * @return
     * @throws OperationException
     */
    @Override
    public ClientReconfirmationResponse sendForReConfirmationToClient(ClientSendReconfirmationResource request) throws OperationException {
        ReconfirmationFinalResponse response = null;
        ClientReconfirmationDetails aClientReconfirmationDetails = this.findByReconfirmationID(request.getClientReconfirmationID());

        OpsBooking aBooking = this.reconfirmationService.getBookingById(aClientReconfirmationDetails.getBookRefNo());
        OpsProduct aProductDetails = reconfirmationService.getProductById(aBooking, aClientReconfirmationDetails.getOrderID());

        SupplierReconfirmationFilter filter = new SupplierReconfirmationFilter();
        filter.setProductCatSubtype(aProductDetails.getProductSubCategory() != null ? aProductDetails.getProductSubCategory() : null);
        filter.setProductCategory(aProductDetails.getProductCategory() != null ? aProductDetails.getProductCategory() : null);

        ReconfirmationConfiguration configuration = reconfirmationMDMService.getConfiguration(filter);


        if (configuration == null) {
            throw new OperationException(Constants.ER340);
        }
        if (configuration.getConfigFor() == null) {
            throw new OperationException(Constants.ER340);
        }
        aClientReconfirmationDetails = handleClientReconfirmation(aProductDetails, aBooking, aClientReconfirmationDetails);
        aClientReconfirmationDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_PENDING);
        ClientReconfirmationDetails clientReconfDetails = this.updateClientReconfirmation(aClientReconfirmationDetails);
        ClientReconfirmationResponse responseRecon = this.convertTo(clientReconfDetails);
        return responseRecon;
    }


    @Override
    public ClientReconfirmationResponse sendRejectionToClient(ClientRejectionResource request) throws OperationException {

        ClientReconfirmationDetails aClientReconfirmationDetails = this.findByReconfirmationID(request.getClientReconfirmationID());
        OpsBooking aBooking = this.reconfirmationService.getBookingById(aClientReconfirmationDetails.getBookRefNo());
        OpsProduct aProductDetails = reconfirmationService.getProductById(aBooking, aClientReconfirmationDetails.getOrderID());
        //added new condition
        SupplierReconfirmationFilter filter = new SupplierReconfirmationFilter();
        filter.setProductCategory(aProductDetails.getProductCategory() != null ? aProductDetails.getProductCategory() : null);
        filter.setProductCatSubtype(aProductDetails.getProductSubCategory() != null ? aProductDetails.getProductSubCategory() : null);
        ReconfirmationConfiguration configuration = reconfirmationMDMService.getConfiguration(filter);
        if (configuration == null) {
            throw new OperationException(Constants.ER342);
        }
        if (configuration.getConfigFor() == null) {
            throw new OperationException(Constants.ER342);
        }
        boolean isUpdatedStatus = this.updateStatusOnBookingEngineAs(ClientReconfirmationStatus.REJECTED_BY_CLIENT, aClientReconfirmationDetails.getBookRefNo(), aClientReconfirmationDetails.getOrderID());
        if (isUpdatedStatus) {

            aClientReconfirmationDetails = this.createToDoTaskForOpsUserForAlternateOptionForClient(aProductDetails, aBooking, aClientReconfirmationDetails.getId(), aClientReconfirmationDetails);
            aClientReconfirmationDetails = this.createToDoTaskForOpsUserForCancellationForClient(aProductDetails, aBooking, aClientReconfirmationDetails.getId(), aClientReconfirmationDetails);

            aClientReconfirmationDetails.setClientReconfirmationStatus(ClientReconfirmationStatus.REJECTED_BY_CLIENT);
            aClientReconfirmationDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_REJECTED);
            if (request.getRemarks() != null) {
                aClientReconfirmationDetails.setRemarks(request.getRemarks());
            }
            aClientReconfirmationDetails.setExpiredLink(false);
            aClientReconfirmationDetails.setHash(hashGenerator.getHash());
            ClientReconfirmationDetails clientReconfDetails = this.updateClientReconfirmation(aClientReconfirmationDetails);

            ClientReconfirmationResponse responseRecon = this.convertTo(clientReconfDetails);
            return responseRecon;
        } else {
            throw new OperationException(Constants.ER315);
        }
    }

    /**
     * @param request
     * @return
     * @throws OperationException
     */
    @Override
    public ClientReconfirmationResponse cancelReconfirmation(ClientCancelReconfirmationResource request) throws OperationException {

        ClientReconfirmationDetails aClientReconfirmationDetails = this.findByReconfirmationID(request.getClientReconfirmationID());
        OpsBooking aBooking = this.reconfirmationService.getBookingById(aClientReconfirmationDetails.getBookRefNo());
        OpsProduct aProductDetails = reconfirmationService.getProductById(aBooking, aClientReconfirmationDetails.getOrderID());

        SupplierReconfirmationFilter filter = new SupplierReconfirmationFilter();
        filter.setProductCategory(aProductDetails.getProductCategory() != null ? aProductDetails.getProductCategory() : null);
        filter.setProductCatSubtype(aProductDetails.getProductSubCategory() != null ? aProductDetails.getProductSubCategory() : null);
        ReconfirmationConfiguration configuration = reconfirmationMDMService.getConfiguration(filter);
        if (configuration == null) {
            throw new OperationException(Constants.ER341);
        }
        if (configuration.getConfigFor() == null) {
            throw new OperationException(Constants.ER341);
        }


        aClientReconfirmationDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_CANCELLED);
        aClientReconfirmationDetails.setHash(hashGenerator.getHash());
        aClientReconfirmationDetails.setExpiredLink(false);
        ClientReconfirmationDetails clientReconfDetails = this.updateClientReconfirmation(aClientReconfirmationDetails);
        ClientReconfirmationResponse responseRecon = this.convertTo(clientReconfDetails);
        return responseRecon;
    }

    /**
     * @param reconfirmationId
     * @param constants
     * @throws OperationException
     */
    @Override
    public void validate(String reconfirmationId, String constants) throws OperationException {
        if (reconfirmationId == null || reconfirmationId.isEmpty() == true) {
            throw new OperationException(constants);
        }
    }


    /**
     * @param aProduct
     * @param aBooking
     * @param clientReconfirmation
     * @return
     * @throws OperationException
     */
    public ClientReconfirmationDetails handleClientReconfirmation(OpsProduct aProduct, OpsBooking aBooking, ClientReconfirmationDetails clientReconfirmation) throws OperationException {
        if (logger.isDebugEnabled()) {
            logger.debug("aProduct = [" + aProduct + "], aBooking = [" + aBooking + "], supplierReconfirmation = [" + clientReconfirmation + "], clientReconfirmationDetails = [" + "]");
        }

        OpsProductCategory aProductCategory = OpsProductCategory.getProductCategory(aProduct.getProductCategory());
        String productSubCategory = aProduct.getProductSubCategory();
        switch (aProductCategory) {

            case PRODUCT_CATEGORY_TRANSPORTATION:
                if (productSubCategory.equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {

                    OpsOrderDetails orderDetails = aProduct.getOrderDetails();
                    OpsOrderStatus opsOrderStatus = orderDetails.getOpsOrderStatus();

                    if (opsOrderStatus.getProductStatus().equalsIgnoreCase(OpsProductStatus.OK.getProductStatus()) /*|| opsOrderStatus.getProductStatus().equalsIgnoreCase(OpsProductStatus.TL.getProductStatus())*/) {

                        ClientConfiguration clientConfig = reconfirmationMDMService.getReconfirmationConfigurationForClient(null, aProduct.getProductCategory(), aProduct.getProductSubCategory(), null, null, aProduct.getOrderID(), null, null);
                        OpsFlightDetails flightDetails = aProduct.getOrderDetails().getFlightDetails();
                        clientReconfirmation = reconfirmFlightsClientService.flightProcess(aBooking, aProduct, flightDetails, clientConfig, clientReconfirmation);

                        return clientReconfirmation;
                    } else {
                        throw new OperationException(Constants.ER311);
                    }
                } else {
                    throw new OperationException(Constants.ER312);
                }
            case PRODUCT_CATEGORY_ACCOMMODATION:
                if (productSubCategory.equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {
                    OpsOrderDetails orderDetails = aProduct.getOrderDetails();
                    OpsOrderStatus opsOrderStatus = orderDetails.getOpsOrderStatus();
                    if (opsOrderStatus.getProductStatus().equalsIgnoreCase(OpsProductStatus.OK.getProductStatus())) {

                        OpsHotelDetails hotelDetails = aProduct.getOrderDetails().getHotelDetails();
                        List<OpsRoom> rooms = hotelDetails.getRooms();
                        clientReconfirmation = reconfirmHotelsClientService.handleClientReconfirmationForHotels(aBooking, aProduct, hotelDetails, rooms, clientReconfirmation);
                        return clientReconfirmation;
                    } else {
                        throw new OperationException(Constants.ER311);
                    }
                } else {
                    throw new OperationException(Constants.ER312);
                }
            default:
                throw new OperationException(Constants.ER313);
        }
    }

    /**
     * @param clientReconfirmationDetails
     * @param request
     * @return
     */
    @Override
    public ClientReconfirmationDetails convertToClientReconfirmationDetails(ClientReconfirmationDetails clientReconfirmationDetails, ClientReconfirmationResource request) {
        if (request.getBookingAttribute() != null)
            clientReconfirmationDetails.setBookingAttribute(request.getBookingAttribute());

        if (request.getRemarks() != null)
            clientReconfirmationDetails.setRemarks(request.getRemarks());

        clientReconfirmationDetails.setRejectedDueToNoResponse(request.isRejectedDueToNoResponse());

        if (request.getReconfirmationOnHoldUntilDate() != null)
            clientReconfirmationDetails.setReconfirmationOnHoldUntilDate(request.getReconfirmationOnHoldUntilDate());

        if (request.getClientOrCustomerReconfirmationDate() != null) {
            clientReconfirmationDetails.setClientOrCustomerReconfirmationDate(request.getClientOrCustomerReconfirmationDate());

            try {
                ReconfirmationRequestResource requestForUpdate = new ReconfirmationRequestResource();
                requestForUpdate.setDate(request.getClientOrCustomerReconfirmationDate().toOffsetDateTime().toString());
                requestForUpdate.setProductId(clientReconfirmationDetails.getOrderID());
                requestForUpdate.setBookingRefId(clientReconfirmationDetails.getBookRefNo());
                try {
                    if (this.userService.getLoggedInUserId() != null) {
                        requestForUpdate.setUserID(this.userService.getLoggedInUserId());
                    } else {
                        requestForUpdate.setUserID("NA");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    requestForUpdate.setUserID("NA");
                }
                updateReconfirmationDateInBE(requestForUpdate);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (request.getClientReconfirmationID() != null)
            clientReconfirmationDetails.setId(request.getClientReconfirmationID());

        return clientReconfirmationDetails;
    }


    /**
     * @param clientReconfDetails
     * @return
     */

    @Override
    public ClientReconfirmationResponse convertTo(ClientReconfirmationDetails clientReconfDetails) {

        ClientReconfirmationResponse response = new ClientReconfirmationResponse();
        if (clientReconfDetails.getBookingAttribute() != null) {
            response.setBookingAttribute(clientReconfDetails.getBookingAttribute());
        }
        if (clientReconfDetails.getProductName() != null) {
            response.setProductName(clientReconfDetails.getProductName());
        }
        if (clientReconfDetails.getReconfirmationOnHoldUntilDate() != null) {
            response.setReconfirmationOnHoldUntilDate(clientReconfDetails.getReconfirmationOnHoldUntilDate().toOffsetDateTime().toString());
        }
        if (clientReconfDetails.getRemarks() != null) {
            response.setRemarks(clientReconfDetails.getRemarks());
        }
        if (clientReconfDetails.getOrderID() != null) {
            response.setOrderID(clientReconfDetails.getOrderID());
        }
        if (clientReconfDetails.getBookRefNo() != null) {
            response.setBookingRefNo(clientReconfDetails.getBookRefNo());
        }
        if (clientReconfDetails.getId() != null) {
            response.setClientReconfirmationID(clientReconfDetails.getId());
        }
        if (clientReconfDetails.getClientOrCustomerReconfirmationDate() != null) {
            response.setClientOrCustomerReconfirmationDate(clientReconfDetails.getClientOrCustomerReconfirmationDate().toOffsetDateTime().toString());
        }
        response.setRejectedDueToNoResponse(clientReconfDetails.isRejectedDueToNoResponse());
        return response;
    }

    /**
     * @param opsProduct
     * @param opsBooking
     * @param referenceID
     * @param details
     * @return
     */
    public ClientReconfirmationDetails createToDoTaskForOpsUserForAlternateOptionForClient(OpsProduct opsProduct, OpsBooking opsBooking, String referenceID, ClientReconfirmationDetails details) {
        ToDoTask toDoDetails = null;
        try {
            ToDoTaskResource toDo = new ToDoTaskResource();
            toDo.setBookingRefId(opsBooking.getBookID());
            try {
                toDo.setCreatedByUserId(userService.getLoggedInUserId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            toDo.setReferenceId(referenceID);
            toDo.setClientTypeId(opsBooking.getClientType());
            toDo.setCompanyId(opsBooking.getCompanyId());
            toDo.setClientId(opsBooking.getClientID());
            toDo.setDueOnDate(ZonedDateTime.now());
            toDo.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
            toDo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
            toDo.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
            toDo.setTaskSubTypeId(ToDoTaskSubTypeValues.RECONFIRMATION_ALTERNATE_OPTIONS_FOR_CLIENT.toString());
            toDo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
            toDo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
            toDoDetails = toDoTaskService.save(toDo);
            if (toDoDetails != null) {
                details.setToDoTaskAlternateOptionID(toDoDetails.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return details;
    }

    /**
     * @param opsProduct
     * @param opsBooking
     * @param referenceID
     * @param details
     * @return
     */
    public ClientReconfirmationDetails createToDoTaskForOpsUserForCancellationForClient(OpsProduct opsProduct, OpsBooking opsBooking, String referenceID, ClientReconfirmationDetails details) {
        ToDoTask toDoDetails = null;
        try {
            ToDoTaskResource toDo = new ToDoTaskResource();
            toDo.setBookingRefId(opsBooking.getBookID());
            try {
                toDo.setCreatedByUserId(userService.getLoggedInUserId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            toDo.setReferenceId(referenceID);
            toDo.setClientTypeId(opsBooking.getClientType());
            toDo.setCompanyId(opsBooking.getCompanyId());
            toDo.setProductId(opsProduct.getOrderID());
            toDo.setClientId(opsBooking.getClientID());
            toDo.setDueOnDate(ZonedDateTime.now());
            toDo.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
            toDo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
            toDo.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
            toDo.setTaskSubTypeId(ToDoTaskSubTypeValues.RECONFIRMATION_CANCELLATIONS_FOR_CLIENT.toString());
            toDo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
            toDo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
            toDoDetails = toDoTaskService.save(toDo);
            if (toDoDetails != null) {
                details.setToDoTaskCancellationID(toDoDetails.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return details;
    }

    /**
     * @param opsProduct
     * @param opsBooking
     * @param clientReconfirmationDetails
     * @return
     */
    //TODO BR 314 :
    //IF a booking is confirmed and all criteria for generatrion of handover document is met but reconfirmation is applicable , system should not generate the handover documents until the booking is flagged as
    // reconfirmed by supplier /service provider
    // or Reconfirmed by customer /client
    public ClientReconfirmationDetails generateHandoverDocument(OpsProduct opsProduct, OpsBooking opsBooking, ClientReconfirmationDetails clientReconfirmationDetails) {


        return clientReconfirmationDetails;
    }

    /**
     * @return
     */
    @Override
    public List<ReconfirmAttributeType> getReconfirmationBookingAttributes() {
        List<ReconfirmAttributeType> reconfirmationAttributesList = null;
        reconfirmationAttributesList = Arrays.asList(ReconfirmationBookingAttribute.values())
                .stream()
                .map(attributeType -> new ReconfirmAttributeType(attributeType, attributeType.getValue()))
                .collect(Collectors.toList());
        return reconfirmationAttributesList;
    }

    /**
     * @param reconfirmationRequests
     * @return
     * @throws OperationException
     */
    @Override
    public Map<String, String> updateReconfirmationDateInBE(ReconfirmationRequestResource reconfirmationRequests) throws OperationException {
        ClientReconfirmationRequestBE clientReconfirmationRequestBE = new ClientReconfirmationRequestBE(reconfirmationRequests);
        try {
            if (this.userService.getLoggedInUserId() != null) {
                clientReconfirmationRequestBE.setUserID(this.userService.getLoggedInUserId());
            } else {
                clientReconfirmationRequestBE.setUserID("NA");
            }
        } catch (Exception e) {
            e.printStackTrace();
            clientReconfirmationRequestBE.setUserID("NA");
        }
        URI uri = UriComponentsBuilder.fromUriString(updateReconfirmationDateInBE).build().encode().toUri();
        ResponseEntity<String> response = mdmRestUtils.exchange(uri, HttpMethod.PUT, clientReconfirmationRequestBE, String.class);

        if (response.getBody().contains("ErrorCode")) {
            logger.info("--Update Of Client Reconfirmation Date Failed In BookingDB--");
            throw new OperationException(Constants.OPS_ERR_30101);
        } else {
            Map<String, String> msg = new HashMap<>();
            msg.put("message", "Update successful!");

            try {
                ClientReconfirmationDetails clientReconfirmationDetails = this.clientReconfirmationRepository.findByBookRefAndOrderNo(reconfirmationRequests.getBookingRefId(), reconfirmationRequests.getProductId());
                if (clientReconfirmationDetails != null) {
                    clientReconfirmationDetails.setClientOrCustomerReconfirmationDate(DateTimeUtil.formatBEDateTimeZone(reconfirmationRequests.getDate()));
                    clientReconfirmationDetails.setReconfirmationCutOffDate(DateTimeUtil.formatBEDateTimeZone(reconfirmationRequests.getDate()));
                    clientReconfirmationRepository.saveOrUpdateClientReconfirmation(clientReconfirmationDetails);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return msg;
        }

    }


    private Map<String, String> updateReconfirmationDate(ReconfirmationRequestResource reconfirmationRequests) throws OperationException {
        ClientReconfirmationRequestBE clientReconfirmationRequestBE = new ClientReconfirmationRequestBE(reconfirmationRequests);
        try {
            if (this.userService.getLoggedInUserId() != null) {
                clientReconfirmationRequestBE.setUserID(this.userService.getLoggedInUserId());
            } else {
                clientReconfirmationRequestBE.setUserID("NA");
            }
        } catch (Exception e) {
            e.printStackTrace();
            clientReconfirmationRequestBE.setUserID("NA");
        }
        URI uri = UriComponentsBuilder.fromUriString(updateReconfirmationDateInBE).build().encode().toUri();
        ResponseEntity<String> response = mdmRestUtils.exchange(uri, HttpMethod.PUT, clientReconfirmationRequestBE, String.class);

        if (response.getBody().contains("ErrorCode")) {
            logger.info("--Update Of Client Reconfirmation Date Failed In BookingDB--");
            throw new OperationException(Constants.OPS_ERR_30101);
        } else {
            Map<String, String> msg = new HashMap<>();
            msg.put("message", "Update successful!");
            return msg;
        }

    }


    @Override
    public ClientReconfirmationDetails updateClientReconfirmationDateInBookingEngine(ClientReconfirmationDetails clientReconfirmationDetails) {
        try {
            ReconfirmationRequestResource reconfirmationRequestResource = new ReconfirmationRequestResource();
            reconfirmationRequestResource.setBookingRefId(clientReconfirmationDetails.getBookRefNo());
            reconfirmationRequestResource.setProductId(clientReconfirmationDetails.getOrderID());
            reconfirmationRequestResource.setDate(clientReconfirmationDetails.getClientOrCustomerReconfirmationDate().toOffsetDateTime().toString());
            reconfirmationRequestResource.setRoomId("");
            reconfirmationRequestResource.setUserID("");
            updateReconfirmationDate(reconfirmationRequestResource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientReconfirmationDetails;

    }


}
