package com.coxandkings.travel.operations.service.reconfirmation.supplier;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsFlightDetails;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationDetails;
import com.coxandkings.travel.operations.service.reconfirmation.common.CustomFlightDetails;
import com.coxandkings.travel.operations.service.reconfirmation.common.SupplierConfiguration;

/**
 *
 */
public interface ReconfirmFlightsSupplierService {
    /**
     *
     * @param bookingDetails
     * @param flight
     * @param flights
     * @param reconfConfig
     * @param supplierReconfirmationDetailsDetails
     * @return
     * @throws OperationException
     */
    SupplierReconfirmationDetails flightProcess( OpsBooking bookingDetails , OpsProduct flight , OpsFlightDetails flights , SupplierConfiguration reconfConfig , SupplierReconfirmationDetails supplierReconfirmationDetailsDetails ) throws OperationException;

    /**
     *
     * @param flights
     * @return
     */
    CustomFlightDetails getCustomFlightDetails( OpsFlightDetails flights );

    /**
     *
     * @param bookingDetails
     * @param flights
     * @param supplierReconDetails
     * @return
     * @throws OperationException
     */
    SupplierReconfirmationDetails reconfirmSupplierForFlights( OpsBooking bookingDetails , OpsProduct flights , SupplierReconfirmationDetails supplierReconDetails ) throws OperationException;
}
