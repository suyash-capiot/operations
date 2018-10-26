package com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.PassengerDetailsCriteria;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.PassengersDetails;

public interface PassengerDetailsRepository {
    public PassengersDetails getPassengerDetails(PassengerDetailsCriteria criteria);
}
