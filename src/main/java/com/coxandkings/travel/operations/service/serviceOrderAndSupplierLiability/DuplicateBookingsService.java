package com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.DuplicateBookings;

public interface DuplicateBookingsService {

    public DuplicateBookings saveDuplicateBookings(DuplicateBookings duplicateBookings);
    public DuplicateBookings getDuplicateBookingsById(String id) throws OperationException;

}
