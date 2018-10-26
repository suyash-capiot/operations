package com.coxandkings.travel.operations.service.productsharing;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsAccommodationPaxInfo;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.core.OpsRoom;

public interface AddPaxService {
    /**
     * @param opsBooking
     * @param opsProduct
     * @param opsRoom
     * @param opsAccommodationPaxInfo
     * @return
     * @throws OperationException
     */
    Object addPaxDetails(OpsBooking opsBooking, OpsProduct opsProduct, OpsRoom opsRoom, OpsAccommodationPaxInfo opsAccommodationPaxInfo) throws OperationException;
}
