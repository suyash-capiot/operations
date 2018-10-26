package com.coxandkings.travel.operations.service.bedbservice.acco;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsProduct;

public interface AccoBeDBUpdateService {
    String updateOrderPrice(OpsProduct opsProduct) throws OperationException;
}
