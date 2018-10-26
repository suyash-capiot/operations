package com.coxandkings.travel.operations.service.qcmanagement;

import com.coxandkings.travel.operations.model.core.OpsBooking;

public interface QcBookingStatusService {
    public Boolean doQcCheckOpsBookingStatusWithSI(OpsBooking opsBooking);
}
