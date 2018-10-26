package com.coxandkings.travel.operations.repository.commercials;

import com.coxandkings.travel.operations.model.commercials.BookingIneligibleFor;

import java.util.List;

public interface BookingNotEligibleForRepository {
    public List<BookingIneligibleFor> getAllStatus();
}
