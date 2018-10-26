package com.coxandkings.travel.operations.service.reconfirmation.client;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsFlightDetails;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.reconfirmation.client.ClientReconfirmationDetails;
import com.coxandkings.travel.operations.service.reconfirmation.common.ClientConfiguration;
import com.coxandkings.travel.operations.service.reconfirmation.common.CustomFlightDetails;

/**
 *
 */
public interface ReconfirmFlightsClientService {
    /**
     * @param bookingDetails
     * @param flight
     * @param flights
     * @param reconfConfig
     * @param clientReconfirmationDetails
     * @return
     * @throws OperationException
     */
    ClientReconfirmationDetails flightProcess(OpsBooking bookingDetails, OpsProduct flight, OpsFlightDetails flights, ClientConfiguration reconfConfig, ClientReconfirmationDetails clientReconfirmationDetails) throws OperationException;

    /**
     * @param bookingDetails
     * @param flights
     * @param clientReconDetails
     * @return
     * @throws OperationException
     */
    ClientReconfirmationDetails reconfirmClientForFlights(OpsBooking bookingDetails, OpsProduct flights, ClientReconfirmationDetails clientReconDetails) throws OperationException;

    /**
     * @param flights
     * @return
     */
    CustomFlightDetails getCustomFlightDetails(OpsFlightDetails flights);
}
