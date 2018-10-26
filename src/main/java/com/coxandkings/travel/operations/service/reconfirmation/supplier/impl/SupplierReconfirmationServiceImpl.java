package com.coxandkings.travel.operations.service.reconfirmation.supplier.impl;

import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductStatus;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationBookingAttribute;
import com.coxandkings.travel.operations.enums.reconfirmation.ResponseStatus;
import com.coxandkings.travel.operations.enums.reconfirmation.SupplierReconfirmationStatus;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.reconfirmation.ReconfirmationFinalResponse;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationDetails;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationFilter;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.repository.reconfirmation.client.ClientReconfirmationRepository;
import com.coxandkings.travel.operations.repository.reconfirmation.supplier.SupplierReconfirmationRepository;
import com.coxandkings.travel.operations.resource.outbound.be.ReconfirmationRequestResource;
import com.coxandkings.travel.operations.resource.outbound.be.SupplierReconfirmationRequestBE;
import com.coxandkings.travel.operations.resource.reconfirmation.response.reconfirmation.SupplierReconfirmationResponse;
import com.coxandkings.travel.operations.resource.reconfirmation.supplier.SupplierCancelReconfirmationResource;
import com.coxandkings.travel.operations.resource.reconfirmation.supplier.SupplierReconfirmationResource;
import com.coxandkings.travel.operations.resource.reconfirmation.supplier.SupplierSendReconfirmationResource;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.ManageBookingStatusService;
import com.coxandkings.travel.operations.service.reconfirmation.batchjob.ReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.client.ClientReconfirmationService;
import com.coxandkings.travel.operations.service.reconfirmation.common.ReconfirmationConfiguration;
import com.coxandkings.travel.operations.service.reconfirmation.common.ReconfirmationUtilityService;
import com.coxandkings.travel.operations.service.reconfirmation.common.SupplierConfiguration;
import com.coxandkings.travel.operations.service.reconfirmation.common.UpdateAccoRefRequest;
import com.coxandkings.travel.operations.service.reconfirmation.mdm.ReconfirmationMDMService;
import com.coxandkings.travel.operations.service.reconfirmation.supplier.ReconfirmFlightsSupplierService;
import com.coxandkings.travel.operations.service.reconfirmation.supplier.ReconfirmHotelsSupplierService;
import com.coxandkings.travel.operations.service.reconfirmation.supplier.SupplierReconfirmationService;
import com.coxandkings.travel.operations.service.template.TemplateLoaderService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.*;
import com.coxandkings.travel.operations.utils.adapter.OpsBookingAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.coxandkings.travel.operations.enums.reconfirmation.SupplierReconfirmationStatus.*;

@Service("supplierReconfirmationService")
public class SupplierReconfirmationServiceImpl implements SupplierReconfirmationService {


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
    private ReconfirmationService reconfirmationService;

    @Autowired
    private ReconfirmationMDMService reconfirmationMDMService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private ReconfirmFlightsSupplierService reconfirmFlightsSupplierService;

    @Autowired
    private ReconfirmHotelsSupplierService reconfirmHotelsSupplierService;

    @Autowired
    private ClientReconfirmationService clientReconfirmationService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestUtils restUtils;

    @Value(value = "${reconfirmation.supplier.be.update_reconfirmation_date}")
    private String updateReconfirmationDateInBE;


    @Autowired
    private ManageBookingStatusService manageBookingStatusService;


    @Value(value = "${reconfirmation.supplier.communication.subject}")
    private String supplierCommunicationSubject;


    @Value(value = "${reconfirmation.supplier.communication.subject}")
    private String cancelSupplierCommunicationSubject;

    @Value(value = "${reconfirmation.update_acco_ref_number}")
    private String urlForAccoUpdateRef;

    @Autowired
    private MDMRestUtils mdmRestUtils;


    private static Logger logger = LogManager.getLogger(SupplierReconfirmationServiceImpl.class);

    /**
     * @param request
     * @return
     * @throws OperationException
     */

    @Override
    public SupplierReconfirmationResponse sendForReConfirmationToSupplier(SupplierSendReconfirmationResource request) throws OperationException {
        this.validateSupplierReconfirmationId(request.getSupplierReconfirmationID());
        if (logger.isDebugEnabled()) {
            logger.debug("request = [" + request + "]");
        }

        ReconfirmationFinalResponse response = null;
        SupplierReconfirmationDetails aSupplierReconDetails = this.supplierReconfirmationRepository.findBySupplierReconfirmationId(request.getSupplierReconfirmationID());
        //TODO  UNCOMMENT CODE ACCORDING TO TESTING
//        if ( aSupplierReconDetails.getReconfirmationConfiguredFor( ).equalsIgnoreCase( "supplier&client" ) ) {
//            ClientReconfirmationDetails clientReconfDetails = this.clientReconfirmationService.findByReconfirmationID( aSupplierReconDetails.getId( ) );
//            //BR 324 (second point)
//            if ( !clientReconfDetails.getClientReconfirmationStatus( ).equals( ClientReconfirmationStatus.RECONFIRMED_BY_CLIENT ) ) {
//                throw new OperationException( Constants.ER307 );
//            }
//            //BR 324 (third point)
//            if ( clientReconfDetails.getClientReconfirmationStatus( ).equals( ( ClientReconfirmationStatus.REJECTED_BY_CLIENT ) ) ) {
//                throw new OperationException( Constants.ER308 );
//            }
//        }
//
//
//        if ( aSupplierReconDetails.getReconfirmationConfiguredFor( ).equalsIgnoreCase( "supplier" ) ) {
//
//        }
        OpsBooking bookingDetails = this.reconfirmationService.getBookingById(aSupplierReconDetails.getBookRefNo());
        OpsProduct productDetails = reconfirmationService.getProductById(bookingDetails, aSupplierReconDetails.getOrderID());

        SupplierReconfirmationFilter filter = new SupplierReconfirmationFilter();
        filter.setProductCategory(productDetails.getProductCategory() != null ? productDetails.getProductCategory() : null);
        filter.setProductCatSubtype(productDetails.getProductSubCategory() != null ? productDetails.getProductSubCategory() : null);
        ReconfirmationConfiguration configuration = reconfirmationMDMService.getConfiguration(filter);
        if (configuration == null) {
            throw new OperationException(Constants.ER333);
        }
        if (configuration.getConfigFor() == null) {
            throw new OperationException(Constants.ER333);
        }

        aSupplierReconDetails.setProductConfirmationNumber(request.getProductConfirmationNumber());
        aSupplierReconDetails = handleSupplierReconfirmation(productDetails, bookingDetails, aSupplierReconDetails);
        // aSupplierReconDetails.setProductConfirmationNumber( "PCN" + System.currentTimeMillis( ) );
        aSupplierReconDetails = this.updateSupplierReconfirmation(aSupplierReconDetails);
        SupplierReconfirmationResponse responseRecon = this.convertTo(aSupplierReconDetails);
        if (logger.isDebugEnabled()) {
            logger.debug("response = [" + responseRecon + "]");
        }
        return responseRecon;

    }

    /**
     * @param aProduct
     * @param aBooking
     * @param supplierReconfirmation
     * @return
     * @throws OperationException
     */
    public SupplierReconfirmationDetails handleSupplierReconfirmation(OpsProduct aProduct, OpsBooking aBooking, SupplierReconfirmationDetails supplierReconfirmation) throws OperationException {
        if (logger.isDebugEnabled()) {
            logger.debug("aProduct = [" + aProduct + "], aBooking = [" + aBooking + "], supplierReconfirmation = [" + supplierReconfirmation + "], clientReconfirmationDetails = [" + "]");
        }
        OpsProductCategory aProductCategory = OpsProductCategory.getProductCategory(aProduct.getProductCategory());
        switch (aProductCategory) {

            case PRODUCT_CATEGORY_TRANSPORTATION:
                if (aProduct.getProductSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
                    OpsOrderDetails orderDetails = aProduct.getOrderDetails();
                    OpsOrderStatus opsOrderStatus = orderDetails.getOpsOrderStatus();
                    if (opsOrderStatus.getProductStatus().equalsIgnoreCase(OpsProductStatus.OK.getProductStatus()) /*|| opsOrderStatus.getProductStatus().equalsIgnoreCase(OpsProductStatus.TL.getProductStatus())*/) {

                        SupplierConfiguration supplierConfig = reconfirmationMDMService.getReconfirmationConfigForSupplier(null, aProduct.getProductCategory(), aProduct.getProductSubCategory(), null, null, aProduct.getOrderID(), null, null);
                        OpsFlightDetails flightDetails = aProduct.getOrderDetails().getFlightDetails();
                        supplierReconfirmation = reconfirmFlightsSupplierService.flightProcess(aBooking, aProduct, flightDetails, supplierConfig, supplierReconfirmation);

                        return supplierReconfirmation;
                    } else {
                        throw new OperationException(Constants.ER311);
                    }
                } else {
                    throw new OperationException(Constants.ER312);
                }
            case PRODUCT_CATEGORY_ACCOMMODATION:
                if (aProduct.getProductSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {

                    OpsOrderDetails orderDetails = aProduct.getOrderDetails();
                    OpsOrderStatus opsOrderStatus = orderDetails.getOpsOrderStatus();
                    if (opsOrderStatus.getProductStatus().equalsIgnoreCase(OpsProductStatus.OK.getProductStatus())) {

                        OpsHotelDetails hotelDetails = aProduct.getOrderDetails().getHotelDetails();
                        List<OpsRoom> rooms = hotelDetails.getRooms();
                        supplierReconfirmation = reconfirmHotelsSupplierService.handleSupplierReconfirmationForHotels(aBooking, aProduct, hotelDetails, rooms, supplierReconfirmation);
                        return supplierReconfirmation;
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
     * @param status
     * @param hash
     * @param reconfirmationOnHoldDate
     * @return
     * @throws OperationException
     */
    @Override
    public SupplierReconfirmationDetails checkSupplierResponse(ResponseStatus status, String hash, ZonedDateTime reconfirmationOnHoldDate) throws OperationException {

        SupplierReconfirmationDetails supplierReconDetails = this.supplierReconfirmationRepository.findByHash(hash);
        if (supplierReconDetails == null) {
            throw new OperationException(Constants.ER348);
        }
        if (supplierReconDetails.isExpiredLink() && supplierReconDetails.getSupplierReconfirmationStatus().equals(SupplierReconfirmationStatus.RECONFIRMED_BY_SUPPLIER)) {
            throw new OperationException(Constants.ER349);
        }
        if (supplierReconDetails.isExpiredLink() && supplierReconDetails.getSupplierReconfirmationStatus().equals(SupplierReconfirmationStatus.REJECTED_BY_SUPPLIER)) {
            throw new OperationException(Constants.ER350);
        }

        final String bookingRefId = supplierReconDetails.getBookRefNo();
        final String orderID = supplierReconDetails.getOrderID();

        OpsBooking bookingDetails = this.reconfirmationService.getBookingById(bookingRefId);
        OpsProduct productDetails = reconfirmationService.getProductById(bookingDetails, orderID);
        switch (status) {
            case ACCEPTED: {
                boolean isUpdatedStatus = this.reconfirmationService.updateFlag(RECONFIRMED_BY_SUPPLIER, bookingRefId, orderID);
                if (isUpdatedStatus) {
                    supplierReconDetails.setSupplierReconfirmationStatus(SupplierReconfirmationStatus.RECONFIRMED_BY_SUPPLIER);
                    supplierReconDetails.setExpiredLink(true);
                    supplierReconDetails.setSupplierReplyDate(ZonedDateTime.now());
                    supplierReconDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMED);
                    supplierReconDetails = this.updateSupplierReconfirmation(supplierReconDetails);
                    return supplierReconDetails;
                } else {
                    throw new OperationException(Constants.ER315);
                }
            }
            case REJECTED: {
                boolean isUpdatedStatus = this.reconfirmationService.updateFlag(REJECTED_BY_SUPPLIER, bookingRefId, orderID);
                if (isUpdatedStatus) {
                    supplierReconDetails = this.sendRejectionCommunicationToClient(supplierReconDetails, bookingDetails, productDetails, supplierReconDetails.getClientEmailId());
                    supplierReconDetails.setSupplierReconfirmationStatus(SupplierReconfirmationStatus.REJECTED_BY_SUPPLIER);
                    supplierReconDetails.setExpiredLink(true);
                    supplierReconDetails.setSupplierReplyDate(ZonedDateTime.now());
                    supplierReconDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_REJECTED);
                    supplierReconDetails = this.createToDoTaskForOpsUserForAlternateOption(productDetails, bookingDetails, supplierReconDetails.getId(), supplierReconDetails);
                    supplierReconDetails = this.createToDoTaskForOpsUserForCancellation(productDetails, bookingDetails, supplierReconDetails.getId(), supplierReconDetails);
                    supplierReconDetails = this.updateSupplierReconfirmation(supplierReconDetails);
                    return supplierReconDetails;
                } else {
                    throw new OperationException(Constants.ER315);
                }
            }
            case NO_RESPONSE: {
                boolean isUpdatedStatus = this.reconfirmationService.updateFlag(REJECTED_DUE_TO_NO_RESPONSE_FROM_SUPPLIER, bookingRefId, orderID);
                if (isUpdatedStatus) {
                    supplierReconDetails.setSupplierReconfirmationStatus(SupplierReconfirmationStatus.REJECTED_DUE_TO_NO_RESPONSE_FROM_SUPPLIER);
                    supplierReconDetails.setSupplierReplyDate(ZonedDateTime.now());
                    supplierReconDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_CANCELLED);

                    ToDoTask todoTask = this.createToDoTaskFollowUpForSupplierReconfirmation(productDetails, bookingDetails, supplierReconDetails.getId());
                    // supplierReconDetails.setToDoTaskFollowUpID(todoTask.getId());
                    this.reconfirmationService.reconfigurationMonitor(bookingDetails, productDetails, null, null);
                    supplierReconDetails = this.updateSupplierReconfirmation(supplierReconDetails);
                    return supplierReconDetails;

                } else {
                    throw new OperationException(Constants.ER315);
                }
            }
            case ON_HOLD: {
                //: Enter Date until when request can be on Hold
                boolean isUpdatedStatus = this.reconfirmationService.updateFlag(RECONFIRMATION_REQUEST_ON_HOLD_BY_SUPPLIER, bookingRefId, orderID, reconfirmationOnHoldDate);
                if (isUpdatedStatus) {
                    supplierReconDetails.setSupplierReplyDate(ZonedDateTime.now());
                    supplierReconDetails.setSupplierReconfirmationStatus(SupplierReconfirmationStatus.RECONFIRMATION_REQUEST_ON_HOLD_BY_SUPPLIER);
                    supplierReconDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_ON_HOLD);
                    supplierReconDetails = this.updateSupplierReconfirmation(supplierReconDetails);
                    return supplierReconDetails;
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
     * @param supplierReconDetails
     * @param opsBooking
     * @param opsProduct
     * @param communicationTo
     * @return
     * @throws OperationException
     */
    public SupplierReconfirmationDetails sendRejectionCommunicationToClient(SupplierReconfirmationDetails supplierReconDetails, OpsBooking opsBooking, OpsProduct opsProduct, String communicationTo) throws OperationException {
        String communicationEmailTemplate = null;

        boolean status = reconfirmationUtilityService.composeEmailForSupplier(supplierReconDetails, opsBooking, opsProduct, null, supplierReconDetails.getHash(), false, true);

// TODO  UNCOMMENT CODE ACCORDING TO TESTING
//        if (communicationEmailTemplate == null) {
//            throw new OperationException(Constants.ER309);
//        }
        //BR 324 (second point)
//        if ( !aProductDetails.getClientReconfirmationStatus( ).equals( ClientReconfirmationStatus.RECONFIRMED_BY_CLIENT ) ) {
//            throw new OperationException( Constants.ER307 );
//        }
//        //BR 324 (third point)
//        if ( aProductDetails.getClientReconfirmationStatus( ).equals( ( ClientReconfirmationStatus.REJECTED_BY_CLIENT ) ) ) {
//            throw new OperationException( Constants.ER308 );
//        }

//         if (request.getBookingAttribute().equalsIgnoreCase(ReconfirmationBookingAttribute.RECONFIRMATION_ON_HOLD.getReconfirmationStatus())) {
//            supplierReconfirmationDetailsDetails.setReconfirmationOnHoldUntilDate(request.getReconfirmationOnHoldUntilDate());
//        } else {
//            request.setReconfirmationOnHoldUntilDate(supplierReconfirmationDetailsDetails.getReconfirmationOnHoldUntilDate());
//        }
//
//        if (!bookingDetails.getStatus().equalsIgnoreCase(CommonEnums.CONFIRMED.getReconfirmationStatus())) {
//            throw new OperationException(Constants.BM01);
//        }

//        supplierReconfirmationDetailsDetails.setRemarks(request.getRemarks());
//        supplierReconfirmationDetailsDetails.setId(null);

        supplierReconDetails.setTemplate(communicationEmailTemplate);
        try {
            supplierReconDetails.setReconfirmationSentToSupplier(true);
            return supplierReconDetails;
        } catch (Exception e) {
            e.printStackTrace();
            supplierReconDetails.setReconfirmationSentToSupplier(false);
            throw new OperationException(Constants.ER310);
        }
    }

    /**
     * @param reconfirmationResource
     * @return
     * @throws OperationException
     */
    @Override
    public SupplierReconfirmationResponse cancelReconfirmationToSupplier(SupplierCancelReconfirmationResource reconfirmationResource) throws OperationException {
        this.validateSupplierReconfirmationId(reconfirmationResource.getSupplierReconfirmationID());
        SupplierReconfirmationDetails supplierReconDetails = findByReconfirmationID(reconfirmationResource.getSupplierReconfirmationID());
        OpsBooking bookingDetails = this.reconfirmationService.getBookingById(supplierReconDetails.getBookRefNo());
        OpsProduct productDetails = reconfirmationService.getProductById(bookingDetails, supplierReconDetails.getOrderID());

        SupplierReconfirmationFilter filter = new SupplierReconfirmationFilter();
        //  filter.setSupplierId(productDetails.getSupplierID() != null ? productDetails.getSupplierID() : null);
        filter.setProductCategory(productDetails.getProductCategory() != null ? productDetails.getProductCategory() : null);
        filter.setProductCatSubtype(productDetails.getProductSubCategory() != null ? productDetails.getProductSubCategory() : null);
        ReconfirmationConfiguration configuration = reconfirmationMDMService.getConfiguration(filter);
        if (configuration == null) {
            throw new OperationException(Constants.ER334);
        }
        if (configuration.getConfigFor() == null) {
            throw new OperationException(Constants.ER334);
        }

        supplierReconDetails = sendCancelCommunicationToClient(supplierReconDetails, bookingDetails, productDetails, "");
        supplierReconDetails.setBookingAttribute(ReconfirmationBookingAttribute.RECONFIRMATION_CANCELLED);
        SupplierReconfirmationResponse supplierReconfirmationResponse = this.convertTo(supplierReconDetails);
        return supplierReconfirmationResponse;

    }

    /**
     * @param supplierReconDetails
     * @param opsBooking
     * @param opsProduct
     * @param communicationTo
     * @return
     * @throws OperationException
     */
    public SupplierReconfirmationDetails sendCancelCommunicationToClient(SupplierReconfirmationDetails supplierReconDetails, OpsBooking opsBooking, OpsProduct opsProduct, String communicationTo) throws OperationException {
        boolean status = reconfirmationUtilityService.composeEmailForSupplier(supplierReconDetails, opsBooking, opsProduct, null, supplierReconDetails.getHash(), true, false);
        supplierReconDetails.setTemplate("");
        try {
            supplierReconDetails.setReconfirmationSentToSupplier(true);
            supplierReconDetails = this.saveOrUpdateSupplierReconfirmation(supplierReconDetails);
            return supplierReconDetails;
        } catch (Exception e) {
            e.printStackTrace();
            supplierReconDetails.setReconfirmationSentToSupplier(false);
            throw new OperationException(Constants.ER310);
        }
    }

    /**
     * @param supplierReconfirmationDetails
     * @param request
     * @return
     */
    @Override
    public SupplierReconfirmationDetails convertToSupplierReconfirmation(SupplierReconfirmationDetails supplierReconfirmationDetails, SupplierReconfirmationResource request) {

        if (request.getBookingAttribute() != null)
            supplierReconfirmationDetails.setBookingAttribute(request.getBookingAttribute());

        if (request.getRemarks() != null)
            supplierReconfirmationDetails.setRemarks(request.getRemarks());

        supplierReconfirmationDetails.setRejectedDueToNoResponse(request.isRejectedDueToNoResponse());

        if (request.getReconfirmationOnHoldUntilDate() != null)
            supplierReconfirmationDetails.setReconfirmationOnHoldUntilDate(request.getReconfirmationOnHoldUntilDate());

        if (request.getReconfirmedBy() != null)
            supplierReconfirmationDetails.setReconfirmedBy(request.getReconfirmedBy());

        if (request.getProductReferenceNumber() != null) {
            supplierReconfirmationDetails.setProductConfirmationNumber(request.getProductReferenceNumber());
            try {
                this.updateAccommodationRef(userService.getLoggedInUserId(), supplierReconfirmationDetails.getOrderID(), request.getProductReferenceNumber());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (request.getBookingAttribute() != null && request.getBookingAttribute().getValue().equalsIgnoreCase(ReconfirmationBookingAttribute.RECONFIRMATION_REJECTED.getValue())) {
            try {
                manageBookingStatusService.updateBookingStatus(supplierReconfirmationDetails.getBookRefNo(), OpsBookingStatus.RQ);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return supplierReconfirmationDetails;
    }

    /**
     * @param supplierReconfirmationDetails
     * @return
     */
    @Override
    public SupplierReconfirmationResponse convertTo(SupplierReconfirmationDetails supplierReconfirmationDetails) {

        SupplierReconfirmationResponse response = new SupplierReconfirmationResponse();

        if (supplierReconfirmationDetails.getBookingAttribute() != null) {
            response.setBookingAttribute(supplierReconfirmationDetails.getBookingAttribute());
        }
        if (supplierReconfirmationDetails.getSupplierReconfirmationDate() != null) {
            response.setSupplierReconfirmationDate(supplierReconfirmationDetails.getSupplierReconfirmationDate().toOffsetDateTime().toString());
        }
        if (supplierReconfirmationDetails.getProductName() != null) {
            response.setProductName(supplierReconfirmationDetails.getProductName());
        }
        if (supplierReconfirmationDetails.getReconfirmationOnHoldUntilDate() != null) {
            response.setReconfirmationOnHoldUntilDate(supplierReconfirmationDetails.getReconfirmationOnHoldUntilDate().toOffsetDateTime().toString());
        }
        if (supplierReconfirmationDetails.getRemarks() != null) {
            response.setRemarks(supplierReconfirmationDetails.getRemarks());
        }
        if (supplierReconfirmationDetails.getOrderID() != null) {
            response.setOrderID(supplierReconfirmationDetails.getOrderID());
        }
        if (supplierReconfirmationDetails.getBookRefNo() != null) {
            response.setBookingRefNo(supplierReconfirmationDetails.getBookRefNo());
        }
        if (supplierReconfirmationDetails.getId() != null) {
            response.setSupplierReconfirmationID(supplierReconfirmationDetails.getId());
        }
        if (supplierReconfirmationDetails.getSupplierName() != null) {
            response.setSupplierName(supplierReconfirmationDetails.getSupplierName());
        }
        if (supplierReconfirmationDetails.getReconfirmedBy() != null) {
            response.setReconfirmedBy(supplierReconfirmationDetails.getReconfirmedBy());
        }
        if (supplierReconfirmationDetails.getProductConfirmationNumber() != null) {
            response.setProductReferenceNumber(supplierReconfirmationDetails.getProductConfirmationNumber());
        }
        response.setRejectedDueToNoResponse(supplierReconfirmationDetails.isRejectedDueToNoResponse());
        return response;
    }

    /**
     * @param allSupplierReconfirmation
     * @return
     */
    @Override
    public List<SupplierReconfirmationResponse> convertTo(List<SupplierReconfirmationDetails> allSupplierReconfirmation) {
        List<SupplierReconfirmationResponse> clientReconfirmationResponses = new ArrayList<>();
        for (SupplierReconfirmationDetails details : allSupplierReconfirmation) {
            SupplierReconfirmationResponse reconfirmationResponse = new SupplierReconfirmationResponse();

            if (details.getBookingAttribute() != null)
                reconfirmationResponse.setBookingAttribute(details.getBookingAttribute());

            if (details.getBookRefNo() != null)
                reconfirmationResponse.setBookingRefNo(details.getBookRefNo());

            if (details.getCity() != null)
                reconfirmationResponse.setCity(details.getCity());

            if (details.getSupplierReconfirmationDate() != null)
                reconfirmationResponse.setSupplierReconfirmationDate(details.getSupplierReconfirmationDate().toOffsetDateTime().toString());

            if (details.getClientReconfirmationID() != null)
                reconfirmationResponse.setSupplierReconfirmationID(details.getClientReconfirmationID());

            if (details.getCountry() != null)
                reconfirmationResponse.setCountry(details.getCountry());

            if (details.getOrderID() != null)
                reconfirmationResponse.setOrderID(details.getOrderID());

            if (details.getProductName() != null)
                reconfirmationResponse.setProductName(details.getProductName());

            if (details.getReconfirmationOnHoldUntilDate() != null)
                reconfirmationResponse.setReconfirmationOnHoldUntilDate(details.getReconfirmationOnHoldUntilDate().toOffsetDateTime().toString());

            if (details.getRemarks() != null)
                reconfirmationResponse.setRemarks(details.getRemarks());

            if (details.getReconfirmedBy() != null)
                reconfirmationResponse.setReconfirmedBy(details.getReconfirmedBy());

            if (details.getSupplierName() != null)
                reconfirmationResponse.setSupplierName(details.getSupplierName());

            if (details.getId() != null)
                reconfirmationResponse.setSupplierReconfirmationID(details.getId());

            reconfirmationResponse.setRejectedDueToNoResponse(details.isRejectedDueToNoResponse());
            clientReconfirmationResponses.add(reconfirmationResponse);
        }
        return clientReconfirmationResponses;
    }

    /**
     * @return
     */
    @Override
    public List<SupplierReconfirmationDetails> getAllSupplierReconfirmation() {
        return this.supplierReconfirmationRepository.getAllSupplierReconfirmation();
    }

    /**
     * @param supplierReconfirmationDetails
     * @return
     */
    @Override
    public SupplierReconfirmationDetails saveSupplierReconfirmation(SupplierReconfirmationDetails supplierReconfirmationDetails) {
        if (supplierReconfirmationRepository != null) {
            supplierReconfirmationDetails.setTemplate("");
            supplierReconfirmationDetails.setId(null);
            return this.supplierReconfirmationRepository.saveOrUpdateSupplierReconfirmation(supplierReconfirmationDetails);
        }
        return null;
    }

    /**
     * @param supplierReconfirmationDetails
     * @return
     */
    @Override
    public SupplierReconfirmationDetails updateSupplierReconfirmation(SupplierReconfirmationDetails supplierReconfirmationDetails) {
        supplierReconfirmationDetails.setTemplate("");
        if (supplierReconfirmationRepository != null && supplierReconfirmationDetails.getId() != null) {
            return this.supplierReconfirmationRepository.saveOrUpdateSupplierReconfirmation(supplierReconfirmationDetails);
        }
        return null;
    }

    /**
     * @param supplierReconfirmationDetails
     * @return
     */
    @Override
    public SupplierReconfirmationDetails saveOrUpdateSupplierReconfirmation(SupplierReconfirmationDetails supplierReconfirmationDetails) {
        supplierReconfirmationDetails.setTemplate("");
        return this.supplierReconfirmationRepository.saveOrUpdateSupplierReconfirmation(supplierReconfirmationDetails);
    }

    /**
     * @param reconfirmationID
     * @return
     * @throws OperationException
     */
    @Override
    public SupplierReconfirmationDetails findByReconfirmationID(String reconfirmationID) throws OperationException {
        SupplierReconfirmationDetails supplierReconfirmationDetails = this.supplierReconfirmationRepository.findBySupplierReconfirmationId(reconfirmationID);
        return supplierReconfirmationDetails;
    }

    /**
     * @param bookRefNo
     * @param orderNo
     * @return
     */
    @Override
    public SupplierReconfirmationDetails findByBookRefAndOrderNo(String bookRefNo, String orderNo) {
        SupplierReconfirmationDetails supplierReconfirmationDetails = this.supplierReconfirmationRepository.findByBookRefAndOrderNo(bookRefNo, orderNo);
        return supplierReconfirmationDetails;
    }


    /**
     * @param opsProduct
     * @param opsBooking
     * @param referenceID
     * @return
     */

    //TODO: CREATE TODO TASK FOR OPS USER FOR FOLLOW UP WITH FOR SUPPLIER RECONFIRMATION
    public ToDoTask createToDoTaskFollowUpForSupplierReconfirmation(OpsProduct opsProduct, OpsBooking opsBooking, String referenceID) {
        ToDoTask toDoDetails = null;
        try {
            ToDoTaskResource toDo = new ToDoTaskResource();
            toDo.setBookingRefId(opsBooking.getBookID());

            try {
                String loggedInUserId = userService.getLoggedInUserId();
                toDo.setCreatedByUserId(loggedInUserId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            toDo.setProductId(opsProduct.getOrderID());
            toDo.setReferenceId(referenceID);
            toDo.setClientTypeId(opsBooking.getClientType());
            toDo.setCompanyId(opsBooking.getCompanyId());
            toDo.setClientId(opsBooking.getClientID());
            toDo.setCompanyMarketId(opsBooking.getCompanyId());
            toDo.setDueOnDate(ZonedDateTime.now());
            toDo.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
            toDo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
            toDo.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
            toDo.setTaskSubTypeId(ToDoTaskSubTypeValues.RECONFIRMATION_FOLLOW_UP_FOR_SUPPLIER.toString());
            toDo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
            toDo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
            toDoDetails = toDoTaskService.save(toDo);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return toDoDetails;
    }

    /**
     * @param opsProduct
     * @param opsBooking
     * @param referenceID
     * @param details
     * @return
     */
    public SupplierReconfirmationDetails createToDoTaskForOpsUserForAlternateOption(OpsProduct opsProduct, OpsBooking opsBooking, String referenceID, SupplierReconfirmationDetails details) {
        ToDoTask toDoDetails = null;
        try {
            ToDoTaskResource toDo = new ToDoTaskResource();
            toDo.setBookingRefId(opsBooking.getBookID());

            try {
                String loggedInUserId = userService.getLoggedInUserId();
                toDo.setCreatedByUserId(loggedInUserId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            toDo.setProductId(opsProduct.getOrderID());
            toDo.setReferenceId(referenceID);
            toDo.setClientTypeId(opsBooking.getClientType());
            toDo.setCompanyId(opsBooking.getCompanyId());
            toDo.setClientId(opsBooking.getClientID());
            toDo.setCompanyMarketId(opsBooking.getCompanyId());
            toDo.setDueOnDate(ZonedDateTime.now());
            toDo.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
            toDo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
            toDo.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
            toDo.setTaskSubTypeId(ToDoTaskSubTypeValues.RECONFIRMATION_ALTERNATE_OPTIONS_FOR_SUPPLIER.toString());
            toDo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
            toDo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
            toDoDetails = toDoTaskService.save(toDo);
            if (toDoDetails != null && toDoDetails.getId() != null) {
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
    public SupplierReconfirmationDetails createToDoTaskForOpsUserForCancellation(OpsProduct opsProduct, OpsBooking opsBooking, String referenceID, SupplierReconfirmationDetails details) {
        ToDoTask toDoDetails = null;
        try {
            ToDoTaskResource toDo = new ToDoTaskResource();
            toDo.setBookingRefId(opsBooking.getBookID());

            try {
                String loggedInUserId = userService.getLoggedInUserId();
                toDo.setCreatedByUserId(loggedInUserId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            toDo.setProductId(opsProduct.getOrderID());
            toDo.setReferenceId(referenceID);
            toDo.setClientTypeId(opsBooking.getClientType());
            toDo.setCompanyId(opsBooking.getCompanyId());
            toDo.setClientId(opsBooking.getClientID());
            toDo.setCompanyMarketId(opsBooking.getCompanyId());
            toDo.setDueOnDate(ZonedDateTime.now());
            toDo.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
            toDo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
            toDo.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
            toDo.setTaskSubTypeId(ToDoTaskSubTypeValues.RECONFIRMATION_CANCELLATIONS_FOR_SUPPLIER.toString());
            toDo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
            toDo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
            toDoDetails = toDoTaskService.save(toDo);
            if (toDoDetails != null && toDoDetails.getId() != null) {
                details.setToDoTaskCancellationID(toDoDetails.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return details;
    }


    /**
     * @param reconfirmationId
     * @throws OperationException
     */
    @Override
    public void validateSupplierReconfirmationId(String reconfirmationId) throws OperationException {
        if (reconfirmationId == null || reconfirmationId.isEmpty() == true) {
            throw new OperationException(Constants.ER331);
        }
    }

    @Override
    public String updateAccommodationRef(String userID, String orderID, String accoRefNumber) {
        String status = null;
        try {
            UpdateAccoRefRequest updateAccoRefRequest = new UpdateAccoRefRequest();
            updateAccoRefRequest.setAccoRefNumber(accoRefNumber);
            updateAccoRefRequest.setOrderID(orderID);
            updateAccoRefRequest.setUserID(userID);
            HttpEntity<UpdateAccoRefRequest> httpEntity = new HttpEntity<>(updateAccoRefRequest);
            ResponseEntity<String> response = restUtils.exchange(this.urlForAccoUpdateRef, HttpMethod.PUT, httpEntity, String.class);
            status = response.getBody();
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            return status;
        }
    }


    @Override
    public ResponseEntity<?> updateSupplierReconfirmationDate(ReconfirmationRequestResource reconfirmationRequests) {
        SupplierReconfirmationRequestBE supplierReconfirmationRequestBE = new SupplierReconfirmationRequestBE(reconfirmationRequests);
        try {
            if (this.userService.getLoggedInUserId() != null) {
                supplierReconfirmationRequestBE.setUserID(this.userService.getLoggedInUserId());
            } else {
                supplierReconfirmationRequestBE.setUserID("NA");
            }
        } catch (Exception e) {
            e.printStackTrace();
            supplierReconfirmationRequestBE.setUserID("NA");
        }
        try {
            URI uri = UriComponentsBuilder.fromUriString(this.updateReconfirmationDateInBE).build().encode().toUri();
            ResponseEntity<String> exchange = mdmRestUtils.exchange(uri, HttpMethod.PUT, supplierReconfirmationRequestBE, String.class);
            if (exchange.getBody().contains("ErrorCode")) {
                logger.info("--Update Of Client Reconfirmation Date Failed In BookingDB--");
                Map<String, String> response = new HashMap<>();
                response.put("message", "Update Failed!");
                return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Update successful!");

                try {
                    SupplierReconfirmationDetails supplierReconfirmedDetails = this.supplierReconfirmationRepository.findByBookRefAndOrderNo(reconfirmationRequests.getBookingRefId(), reconfirmationRequests.getProductId());
                    if (supplierReconfirmedDetails != null) {
                        supplierReconfirmedDetails.setSupplierReconfirmationDate(DateTimeUtil.formatBEDateTimeZone(reconfirmationRequests.getDate()));
                        supplierReconfirmationRepository.saveOrUpdateSupplierReconfirmation(supplierReconfirmedDetails);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new ResponseEntity(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Update Failed!");
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public SupplierReconfirmationDetails updateSupplierReconfirmationDateInBookingEngine(SupplierReconfirmationDetails supplierReconfirmationDetails) {
        try {
            ReconfirmationRequestResource reconfirmationRequestResource = new ReconfirmationRequestResource();
            reconfirmationRequestResource.setBookingRefId(supplierReconfirmationDetails.getBookRefNo());
            reconfirmationRequestResource.setProductId(supplierReconfirmationDetails.getOrderID());
            reconfirmationRequestResource.setDate(supplierReconfirmationDetails.getSupplierReconfirmationDate().toOffsetDateTime().toString());
            reconfirmationRequestResource.setRoomId("");
            reconfirmationRequestResource.setUserID("");
            updateSupplierReconfirmationDateInBE(reconfirmationRequestResource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplierReconfirmationDetails;

    }


    private ResponseEntity<?> updateSupplierReconfirmationDateInBE(ReconfirmationRequestResource reconfirmationRequests) {
        SupplierReconfirmationRequestBE supplierReconfirmationRequestBE = new SupplierReconfirmationRequestBE(reconfirmationRequests);
        try {
            if (this.userService.getLoggedInUserId() != null) {
                supplierReconfirmationRequestBE.setUserID(this.userService.getLoggedInUserId());
            } else {
                supplierReconfirmationRequestBE.setUserID("NA");
            }
        } catch (Exception e) {
            e.printStackTrace();
            supplierReconfirmationRequestBE.setUserID("NA");
        }
        try {
            URI uri = UriComponentsBuilder.fromUriString(this.updateReconfirmationDateInBE).build().encode().toUri();
            ResponseEntity<String> exchange = mdmRestUtils.exchange(uri, HttpMethod.PUT, supplierReconfirmationRequestBE, String.class);
            if (exchange.getBody().contains("ErrorCode")) {
                logger.info("--Update Of Client Reconfirmation Date Failed In BookingDB--");
                Map<String, String> response = new HashMap<>();
                response.put("message", "Update Failed!");
                return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Update successful!");
                return new ResponseEntity(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Update Failed!");
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Long dateBetween(ZonedDateTime submitDate) {
        if (submitDate == null) {
            submitDate = ZonedDateTime.now(ZoneId.systemDefault());
        }
        LocalDate input = submitDate.toLocalDate();
        LocalDate today = LocalDate.now();
        return ChronoUnit.DAYS.between(input, today);
    }

}
