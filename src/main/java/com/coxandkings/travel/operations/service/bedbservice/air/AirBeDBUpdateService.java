package com.coxandkings.travel.operations.service.bedbservice.air;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsProduct;

public interface AirBeDBUpdateService {
    String updateOrderPrice(OpsProduct opsProduct) throws OperationException;
}
