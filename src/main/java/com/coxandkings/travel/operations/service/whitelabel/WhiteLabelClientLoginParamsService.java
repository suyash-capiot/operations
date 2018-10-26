package com.coxandkings.travel.operations.service.whitelabel;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.whitelabel.ClientLoginParameter;
import com.coxandkings.travel.operations.resource.whitelabel.ClientLoginParameterResource;
import org.springframework.stereotype.Service;

@Service
public interface WhiteLabelClientLoginParamsService {

    ClientLoginParameter createOrUpdateClientLoginParameter(
            ClientLoginParameterResource clientLoginParameterResource) throws OperationException;

}
