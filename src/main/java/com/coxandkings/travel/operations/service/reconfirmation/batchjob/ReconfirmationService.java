package com.coxandkings.travel.operations.service.reconfirmation.batchjob;

import com.coxandkings.travel.operations.enums.reconfirmation.ReconfirmationCutOffType;
import com.coxandkings.travel.operations.enums.reconfirmation.SupplierReconfirmationStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.reconfirmation.ReconfirmationMonitor;
import com.coxandkings.travel.operations.model.reconfirmation.ReconfirmationRequest;
import com.coxandkings.travel.operations.model.reconfirmation.client.ClientReconfirmationDetails;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationDetails;
import com.coxandkings.travel.operations.resource.reconfirmation.client.ClientReconfirmationResource;
import com.coxandkings.travel.operations.resource.reconfirmation.response.reconfirmation.ClientReconfirmationResponse;
import com.coxandkings.travel.operations.service.reconfirmation.common.ReconfirmationConfiguration;

import java.time.ZonedDateTime;
import java.util.List;

/**
 *
 */
public interface ReconfirmationService {
    /**
     * @param clientReconfirmationDetails
     * @return
     */
    ClientReconfirmationDetails saveOrUpdateCCReconfirmation(ClientReconfirmationDetails clientReconfirmationDetails);

    /**
     * @param supplierReconfirmationDetails
     * @return
     */
    SupplierReconfirmationDetails saveUpdateSSReconfirmation(SupplierReconfirmationDetails supplierReconfirmationDetails);

    /**
     * @param clientReconfirmationDetails
     * @return
     */
    ClientReconfirmationResponse convertTo(ClientReconfirmationDetails clientReconfirmationDetails);

    /**
     * @param clientReconfirmationResource
     * @return
     */
    ClientReconfirmationDetails saveClientReconfirmation(ClientReconfirmationResource clientReconfirmationResource);

    /**
     * @param allClientReconfirmation
     * @return
     */
    List<ClientReconfirmationResponse> convertTo(List<ClientReconfirmationDetails> allClientReconfirmation);

    /**
     * @return
     */
    List<ClientReconfirmationDetails> getAllCCReconfirmation();

    /**
     * @param reconfirmationCutOff
     * @param travelDate
     * @param bookingDate
     * @param hours
     * @return
     * @throws OperationException
     */
    ZonedDateTime getReconfirmationCutOffDate(String reconfirmationCutOff, ZonedDateTime travelDate, ZonedDateTime bookingDate, long hours) throws OperationException;

    /**
     * @param reconfirmationCutOff
     * @return
     */
    ReconfirmationCutOffType getReconfirmationCutOffType(String reconfirmationCutOff);

    /**
     *
     */
    void schedulerJob();

    /**
     * @param reconfirmationCutOff
     * @param travelDate
     * @param hours
     * @return
     * @throws OperationException
     */
    boolean getReconfirmationCutOffDate(String reconfirmationCutOff, String travelDate, long hours) throws OperationException;

    /**
     * @param cutOffDate
     * @return
     */
    boolean isReconfirmationCutOffReached(ZonedDateTime cutOffDate);

    /**
     * @param reconfConfig
     * @param request
     * @return
     * @throws OperationException
     */
    ReconfirmationRequest client(ReconfirmationConfiguration reconfConfig, ReconfirmationRequest request) throws OperationException;

    /**
     * @param reconfConfig
     * @param request
     * @return
     * @throws OperationException
     */
    ReconfirmationRequest supplier(ReconfirmationConfiguration reconfConfig, ReconfirmationRequest request) throws OperationException;

    /**
     * @param status
     * @param bookingRefId
     * @param productId
     * @return
     * @throws OperationException
     */
    boolean updateFlag(SupplierReconfirmationStatus status, String bookingRefId, String productId) throws OperationException;

    /**
     * @param status
     * @param bookingRefId
     * @param productId
     * @param reconfirmationOnHoldDate
     * @return
     * @throws OperationException
     */
    boolean updateFlag(SupplierReconfirmationStatus status, String bookingRefId, String productId, ZonedDateTime reconfirmationOnHoldDate) throws OperationException;

    /**
     * @param bookingDetails
     */
    void processBooking(OpsBooking bookingDetails,KafkaBookingMessage kafkaBookingMessage);

    /**
     * @param opsBooking
     * @param opsProduct
     * @param reconfirmationConfig
     * @param request
     * @return
     * @throws OperationException
     */
    ReconfirmationMonitor reconfigurationMonitor(OpsBooking opsBooking, OpsProduct opsProduct, ReconfirmationConfiguration reconfirmationConfig, ReconfirmationRequest request) throws OperationException;

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
    ReconfirmationRequest processReconfirmation(ReconfirmationConfiguration reconfirmationConfiguration, OpsProduct aProduct, OpsBooking aBooking, ReconfirmationRequest request, SupplierReconfirmationDetails supplierReconfirmation, ClientReconfirmationDetails clientReconfirmationDetails) throws OperationException;

    /**
     * @param bookingId
     * @return
     * @throws OperationException
     */
    OpsBooking getBookingById(String bookingId) throws OperationException;

    /**
     * @param bookingDetails
     * @param orderID
     * @return
     * @throws OperationException
     */
    OpsProduct getProductById(OpsBooking bookingDetails, final String orderID) throws OperationException;

    /**
     * @param aProduct
     * @param aBooking
     * @throws OperationException
     */
    void processBookingWithProduct(OpsProduct aProduct, OpsBooking aBooking) throws OperationException;

}
