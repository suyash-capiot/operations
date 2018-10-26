package com.coxandkings.travel.operations.service.productbookedthrother.mdm;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2BClient;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2CClient;

public interface MdmClientService {

    public B2BClient getB2bClient(String clientId) throws OperationException;
    public B2CClient getB2cClient(String clientId) throws OperationException;
}
