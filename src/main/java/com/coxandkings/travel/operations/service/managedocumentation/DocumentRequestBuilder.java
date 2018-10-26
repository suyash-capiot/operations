package com.coxandkings.travel.operations.service.managedocumentation;

import com.coxandkings.travel.operations.model.core.OpsBooking;
import org.json.JSONObject;

public interface DocumentRequestBuilder {
    JSONObject buildHotelVoucherRequest(OpsBooking opsBooking);

}
