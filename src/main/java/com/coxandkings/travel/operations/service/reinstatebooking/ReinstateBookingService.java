package com.coxandkings.travel.operations.service.reinstatebooking;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.ErrorResponseResource;

public interface ReinstateBookingService {
    ErrorResponseResource reinstateBooking(String bookingRefNo,String orderID) throws OperationException;
}
