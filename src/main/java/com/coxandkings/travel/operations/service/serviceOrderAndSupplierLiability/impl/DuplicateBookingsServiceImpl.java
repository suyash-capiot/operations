package com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.DuplicateBookings;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.DuplicateBookingsRepository;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.DuplicateBookingsService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DuplicateBookingsServiceImpl implements DuplicateBookingsService {

    @Autowired
    private DuplicateBookingsRepository duplicateBookingsRepository;

    @Override
    public DuplicateBookings saveDuplicateBookings(DuplicateBookings duplicateBookings) {
        return duplicateBookingsRepository.saveDuplicateBookings(duplicateBookings);
    }

    @Override
    public DuplicateBookings getDuplicateBookingsById(String id) throws OperationException {
        DuplicateBookings duplicateBookings = duplicateBookingsRepository.getDuplicateBookingsById(id);
        if(duplicateBookings == null)
            throw new OperationException(Constants.ER01);
        else
            return duplicateBookings;
    }
}
