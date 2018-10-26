package com.coxandkings.travel.operations.repository.booking;

import com.coxandkings.travel.operations.model.booking.AirlineUpdates;

import java.util.List;

public interface AirlineUpdatesRepository {
    public AirlineUpdates saveOrUpdateAirlineUpdates(AirlineUpdates airlineUpdates);
    public List<AirlineUpdates> getAirlineUpdates();
    public AirlineUpdates getAirlineUpdate(String bookingRef, String productId);
}
