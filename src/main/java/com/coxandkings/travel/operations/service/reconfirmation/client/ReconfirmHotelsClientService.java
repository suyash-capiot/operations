package com.coxandkings.travel.operations.service.reconfirmation.client;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsHotelDetails;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.core.OpsRoom;
import com.coxandkings.travel.operations.model.reconfirmation.client.ClientReconfirmationDetails;

import java.util.List;

/**
 *
 */
public interface ReconfirmHotelsClientService {
    /**
     * @param bookingDetails
     * @param hotels
     * @param hotelDetails
     * @param clientReconfirmationDetails
     * @return
     * @throws OperationException
     */
    ClientReconfirmationDetails reconfirmClientForHotels(OpsBooking bookingDetails, OpsProduct hotels, OpsHotelDetails hotelDetails,
                                                         ClientReconfirmationDetails clientReconfirmationDetails) throws OperationException;

    /**
     * @param rooms
     * @param hotelDetails
     * @param clientReconDetails
     * @return
     * @throws OperationException
     */
    ClientReconfirmationDetails getCustomHotelDetails(List<OpsRoom> rooms, OpsHotelDetails hotelDetails, ClientReconfirmationDetails clientReconDetails) throws OperationException;

    /**
     * @param bookingDetails
     * @param hotels
     * @param hotelDetails
     * @param rooms
     * @param clientReconDetails
     * @return
     * @throws OperationException
     */
    ClientReconfirmationDetails handleClientReconfirmationForHotels(OpsBooking bookingDetails, OpsProduct hotels, OpsHotelDetails hotelDetails, List<OpsRoom> rooms, ClientReconfirmationDetails clientReconDetails) throws OperationException;
}
