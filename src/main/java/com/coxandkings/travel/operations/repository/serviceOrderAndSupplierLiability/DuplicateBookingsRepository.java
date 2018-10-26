package com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.DuplicateBookings;

public interface DuplicateBookingsRepository {

    public DuplicateBookings saveDuplicateBookings(DuplicateBookings duplicateBookings);
    public DuplicateBookings getDuplicateBookingsById(String id);

}
