package com.coxandkings.travel.operations.service.bedbservice;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsProduct;

public interface BeDBUpdateService {

    void updateOrderPrice(OpsProduct opsProduct) throws OperationException;
	
}
