package com.coxandkings.travel.operations.repository.productbookedthrother;

import com.coxandkings.travel.operations.criteria.productbookedthrother.ProductBookedThrOtherCriteria;
import com.coxandkings.travel.operations.model.productbookedthrother.Flight;

public interface FlightRepository {


    public Flight saveOrUpdateFlight(Flight flight);

    public Boolean isFlightExists(String id);

    public Flight getFlightById(String id);

    public Flight getFlightByCriteria(ProductBookedThrOtherCriteria productBookedThrOtherCriteria);
}
