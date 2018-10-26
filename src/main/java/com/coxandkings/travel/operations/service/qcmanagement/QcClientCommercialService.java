package com.coxandkings.travel.operations.service.qcmanagement;

import com.coxandkings.travel.operations.model.core.OpsBooking;
import org.json.JSONException;

public interface QcClientCommercialService {
    public Boolean qcCheckForAirClientCommercial(OpsBooking opsBooking) throws JSONException;

    public Boolean qcCheckForAccoClientCommercial(OpsBooking opsBooking) throws JSONException;
}
