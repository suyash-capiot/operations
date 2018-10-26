package com.coxandkings.travel.operations.service.reconfirmation.client;

import com.coxandkings.travel.operations.enums.reconfirmation.ResponseStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.reconfirmation.client.ClientReconfirmationDetails;
import com.coxandkings.travel.operations.resource.outbound.be.ReconfirmationRequestResource;
import com.coxandkings.travel.operations.resource.reconfirmation.client.ClientCancelReconfirmationResource;
import com.coxandkings.travel.operations.resource.reconfirmation.client.ClientReconfirmationResource;
import com.coxandkings.travel.operations.resource.reconfirmation.client.ClientRejectionResource;
import com.coxandkings.travel.operations.resource.reconfirmation.client.ClientSendReconfirmationResource;
import com.coxandkings.travel.operations.resource.reconfirmation.response.reconfirmation.ClientReconfirmationResponse;
import com.coxandkings.travel.operations.service.reconfirmation.common.ReconfirmAttributeType;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public interface ClientReconfirmationService {
    /**
     * @param status
     * @param bookingRefId
     * @param productId
     * @return
     * @throws OperationException
     */
    boolean updateStatusForClientAndCustomer(ResponseStatus status, String bookingRefId, String productId) throws OperationException;

    /**
     * @param clientReconfirmationDetails
     * @return
     */
    ClientReconfirmationDetails saveClientReconfirmation(ClientReconfirmationDetails clientReconfirmationDetails);

    /**
     * @param clientReconfirmationDetails
     * @return
     */
    ClientReconfirmationDetails updateClientReconfirmation(ClientReconfirmationDetails clientReconfirmationDetails);

    /**
     * @param clientReconfirmationDetails
     * @return
     */
    ClientReconfirmationDetails saveOrUpdateClientReconfirmation(ClientReconfirmationDetails clientReconfirmationDetails);

    /**
     * @param reconfirmationID
     * @return
     * @throws OperationException
     */
    ClientReconfirmationDetails findByReconfirmationID(String reconfirmationID) throws OperationException;

    /**
     * @param bookRefNo
     * @param orderNo
     * @return
     */
    ClientReconfirmationDetails findByBookRefAndOrderNo(String bookRefNo, String orderNo);

    /**
     * @param status
     * @param clientReconfirmationId
     * @param reconfirmationOnHoldDate
     * @return
     * @throws OperationException
     */
    ClientReconfirmationDetails checkClientResponse(ResponseStatus status, String clientReconfirmationId, ZonedDateTime reconfirmationOnHoldDate) throws OperationException;

    /**
     * @param request
     * @return
     * @throws OperationException
     */
    ClientReconfirmationResponse sendForReConfirmationToClient(ClientSendReconfirmationResource request) throws OperationException;

    /**
     * @param clientReconfDetails
     * @return
     */
    ClientReconfirmationResponse convertTo(ClientReconfirmationDetails clientReconfDetails);

    /**
     * @param request
     * @return
     * @throws OperationException
     */
    ClientReconfirmationResponse sendRejectionToClient(ClientRejectionResource request) throws OperationException;

    /**
     * @param request
     * @return
     * @throws OperationException
     */
    ClientReconfirmationResponse cancelReconfirmation(ClientCancelReconfirmationResource request) throws OperationException;

    /**
     * @param reconfirmationId
     * @param constants
     * @throws OperationException
     */
    void validate(String reconfirmationId, String constants) throws OperationException;

    /**
     * @return
     */
    List<ReconfirmAttributeType> getReconfirmationBookingAttributes();

    /**
     * @param clientReconfirmationDetails
     * @param request
     * @return
     */
    ClientReconfirmationDetails convertToClientReconfirmationDetails(ClientReconfirmationDetails clientReconfirmationDetails, ClientReconfirmationResource request);

    /**
     * @param reconfirmationRequests
     * @return
     * @throws OperationException
     */
    Map<String, String> updateReconfirmationDateInBE(ReconfirmationRequestResource reconfirmationRequests) throws OperationException;

    /**
     * @param clientReconfirmationDetails
     * @return
     */
    ClientReconfirmationDetails updateClientReconfirmationDateInBookingEngine(ClientReconfirmationDetails clientReconfirmationDetails);
}
