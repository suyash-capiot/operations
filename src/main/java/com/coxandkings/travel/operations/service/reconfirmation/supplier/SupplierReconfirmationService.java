package com.coxandkings.travel.operations.service.reconfirmation.supplier;

import com.coxandkings.travel.operations.enums.reconfirmation.ResponseStatus;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationDetails;
import com.coxandkings.travel.operations.resource.outbound.be.ReconfirmationRequestResource;
import com.coxandkings.travel.operations.resource.reconfirmation.response.reconfirmation.SupplierReconfirmationResponse;
import com.coxandkings.travel.operations.resource.reconfirmation.supplier.SupplierCancelReconfirmationResource;
import com.coxandkings.travel.operations.resource.reconfirmation.supplier.SupplierReconfirmationResource;
import com.coxandkings.travel.operations.resource.reconfirmation.supplier.SupplierSendReconfirmationResource;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.List;

/**
 *
 */
public interface SupplierReconfirmationService {
    /**
     * @param request
     * @return
     * @throws OperationException
     */
    SupplierReconfirmationResponse sendForReConfirmationToSupplier(SupplierSendReconfirmationResource request) throws OperationException;

    /**
     * @param request
     * @return
     * @throws OperationException
     */
    SupplierReconfirmationResponse cancelReconfirmationToSupplier(SupplierCancelReconfirmationResource request) throws OperationException;

    /**
     * @param ccReconfirmation
     * @return
     */
    SupplierReconfirmationResponse convertTo(SupplierReconfirmationDetails ccReconfirmation);

    /**
     * @return
     */
    List<SupplierReconfirmationDetails> getAllSupplierReconfirmation();

    /**
     * @param allSupplierReconfirmation
     * @return
     */
    List<SupplierReconfirmationResponse> convertTo(List<SupplierReconfirmationDetails> allSupplierReconfirmation);

    /**
     * @param reconfirmationDetails
     * @param ssReconfirmationResource
     * @return
     */
    SupplierReconfirmationDetails convertToSupplierReconfirmation(SupplierReconfirmationDetails reconfirmationDetails, SupplierReconfirmationResource ssReconfirmationResource);

    /**
     * @param ssID
     * @return
     * @throws OperationException
     */
    SupplierReconfirmationDetails findByReconfirmationID(String ssID) throws OperationException;

    /**
     * @param bookRefNo
     * @param orderNo
     * @return
     */
    SupplierReconfirmationDetails findByBookRefAndOrderNo(String bookRefNo, String orderNo);

    /**
     * @param supplierReconfirmationDetails
     * @return
     */
    SupplierReconfirmationDetails saveSupplierReconfirmation(SupplierReconfirmationDetails supplierReconfirmationDetails);

    /**
     * @param supplierReconfirmationDetails
     * @return
     */
    SupplierReconfirmationDetails updateSupplierReconfirmation(SupplierReconfirmationDetails supplierReconfirmationDetails);

    /**
     * @param supplierReconfirmationDetails
     * @return
     */
    SupplierReconfirmationDetails saveOrUpdateSupplierReconfirmation(SupplierReconfirmationDetails supplierReconfirmationDetails);

    /**
     * @param status
     * @param hash
     * @param reconfirmationOnHoldDate
     * @return
     * @throws OperationException
     */
    SupplierReconfirmationDetails checkSupplierResponse(ResponseStatus status, String hash, ZonedDateTime reconfirmationOnHoldDate) throws OperationException;

    /**
     * @param reconfirmationId
     * @throws OperationException
     */
    void validateSupplierReconfirmationId(String reconfirmationId) throws OperationException;

    /**
     * @param userID
     * @param orderID
     * @param accoRefNumber
     * @return
     */
    String updateAccommodationRef(String userID, String orderID, String accoRefNumber);

    /**
     *
     * @param reconfirmationRequests
     * @return
     */
     ResponseEntity<?> updateSupplierReconfirmationDate(ReconfirmationRequestResource reconfirmationRequests);

    /**
     *
     * @param supplierReconfirmationDetails
     * @return
     */
     SupplierReconfirmationDetails updateSupplierReconfirmationDateInBookingEngine(SupplierReconfirmationDetails supplierReconfirmationDetails);
}
