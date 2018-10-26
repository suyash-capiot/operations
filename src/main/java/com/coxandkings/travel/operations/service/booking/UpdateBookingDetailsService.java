package com.coxandkings.travel.operations.service.booking;


import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.booking.UpdateAccommodationDetailResource;
import com.coxandkings.travel.operations.resource.booking.UpdateFlightDetailResource;
import com.coxandkings.travel.operations.resource.booking.UpdatePaxInfoResource;

import java.util.List;


public interface UpdateBookingDetailsService {

    MessageResource updateOriginDestinationInformation(UpdateFlightDetailResource updateFlightDetailResource)
            throws OperationException;

    MessageResource updatePaxInfo(UpdatePaxInfoResource updatePaxInfoResource) throws OperationException;

    MessageResource updateAccommodationDetails(UpdateAccommodationDetailResource updateAccommodationDetailResource) throws OperationException;
    
    String updateFlightConfirmationDetails(UpdateFlightDetailResource flightDetails) throws OperationException;

    List<String> getPaxStatus();
}
