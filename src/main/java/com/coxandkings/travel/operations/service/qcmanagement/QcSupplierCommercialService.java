package com.coxandkings.travel.operations.service.qcmanagement;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import org.json.JSONException;

public interface QcSupplierCommercialService {

    public Boolean qcCheckForOnlineAirSupplierCommercial(OpsBooking opsBooking) throws JSONException, OperationException;

    public Boolean qcCheckForOnlineAccoSupplierCommercial(OpsBooking opsBooking) throws JSONException, OperationException;
}
