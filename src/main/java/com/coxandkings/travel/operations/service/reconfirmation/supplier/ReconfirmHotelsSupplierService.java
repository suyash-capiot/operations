package com.coxandkings.travel.operations.service.reconfirmation.supplier;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsHotelDetails;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.core.OpsRoom;
import com.coxandkings.travel.operations.model.reconfirmation.client.ClientReconfirmationDetails;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationDetails;

import java.util.List;

public interface ReconfirmHotelsSupplierService {
    /**
     *
     * @param bookingDetails
     * @param hotels
     * @param hotelDetails
     * @param rooms
     * @param supplierReconfirmationDetailsDetails
     * @return
     * @throws OperationException
     */
    SupplierReconfirmationDetails handleSupplierReconfirmationForHotels(OpsBooking bookingDetails, OpsProduct hotels, OpsHotelDetails hotelDetails,
                                                                        List<OpsRoom> rooms, SupplierReconfirmationDetails supplierReconfirmationDetailsDetails) throws OperationException;

    /**
     *
     * @param rooms
     * @param hotelDetails
     * @param supplierReconfirmationDetails
     * @return
     * @throws OperationException
     */
    SupplierReconfirmationDetails getCustomHotelDetails(List<OpsRoom> rooms, OpsHotelDetails hotelDetails, SupplierReconfirmationDetails supplierReconfirmationDetails) throws OperationException;

    /**
     *
     * @param bookingDetails
     * @param hotels
     * @param supplierReconfirmation
     * @param clientReconfirmationDetails
     * @return
     * @throws OperationException
     */
    SupplierReconfirmationDetails reconfirmSupplierForHotels(OpsBooking bookingDetails, OpsProduct hotels,
                                                             SupplierReconfirmationDetails supplierReconfirmation,
                                                             ClientReconfirmationDetails clientReconfirmationDetails) throws OperationException;
}
