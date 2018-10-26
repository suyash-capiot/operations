package com.coxandkings.travel.operations.service.reconfirmation.client;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.reconfirmation.client.ClientReconfirmationDetails;

import java.time.ZonedDateTime;

public interface ClientReconfirmationUtilityService {
    /**
     *
     * @param bookingDetails
     * @param hotels
     * @param clientReconDetails
     * @param reconfirmationCutOffDate
     * @return
     * @throws OperationException
     */
    ClientReconfirmationDetails emailProcessForClient( OpsBooking bookingDetails , OpsProduct hotels , ClientReconfirmationDetails clientReconDetails , ZonedDateTime reconfirmationCutOffDate ) throws OperationException;
}
