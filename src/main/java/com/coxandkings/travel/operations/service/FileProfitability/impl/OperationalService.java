package com.coxandkings.travel.operations.service.FileProfitability.impl;

import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.modifiedFileProfitabiliy.FileProfitabilityBooking;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OperationalService {

    public void computeOperationalCalculations(OpsBooking opsBookings, List<FileProfitabilityBooking> oldBookings);

}
