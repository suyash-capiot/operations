package com.coxandkings.travel.operations.repository.forex;

import com.coxandkings.travel.operations.model.forex.ForexPassenger;

public interface ForexPassengerRepository {

    ForexPassenger saveOrUpdate(ForexPassenger forexPassenger);

    ForexPassenger getById(String id);
}
